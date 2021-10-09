// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class AimAssistB extends Check
{
    private int smoothAim;
    
    public AimAssistB(final CerberusAntiCheat AntiCheat) {
        super("AimAssistB", "AimAssist", AntiCheat);
        this.smoothAim = 0;
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    public static double getFrac(final double d) {
        return d % 1.0;
    }
    
    public int getSmoothAim() {
        return this.smoothAim;
    }
    
    public void setSmoothAim(final int n) {
        this.smoothAim = n;
        if (this.smoothAim < 0) {
            this.smoothAim = 0;
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(final PlayerMoveEvent playerMoveEvent) {
        final Location location;
        final Location location2;
        final Player player;
        final double d;
        this.async(() -> {
            location = playerMoveEvent.getFrom().clone();
            location2 = playerMoveEvent.getTo().clone();
            player = playerMoveEvent.getPlayer();
            d = Math.abs(location.getYaw() - location2.getYaw());
            if (d > 0.0 && d < 360.0) {
                if (getFrac(d) == 0.0) {
                    this.setSmoothAim(this.getSmoothAim() + 100);
                    if (this.getSmoothAim() > 2000) {
                        this.getAnticheat().failure(this, player, ChatColor.RED + "Aim check " + this.getSmoothAim() + " > 2000", "(Type: B)");
                        this.setSmoothAim(0);
                    }
                }
                else {
                    this.setSmoothAim(this.getSmoothAim() - 21);
                }
            }
        });
    }
}
