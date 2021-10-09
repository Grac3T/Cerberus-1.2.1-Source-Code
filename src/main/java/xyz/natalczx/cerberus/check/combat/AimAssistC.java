// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class AimAssistC extends Check
{
    private float lastYaw;
    private float lastBad;
    
    public AimAssistC(final CerberusAntiCheat AntiCheat) {
        super("AimAssistC", "AimAssist", AntiCheat);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        float f;
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getEntity() instanceof Player))) {
                if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                    player = (Player)entityDamageByEntityEvent.getDamager();
                    f = player.getLocation().clone().getYaw();
                    this.onAim(player, f);
                    this.onAim3(player, f);
                }
            }
        });
    }
    
    public boolean onAim(final Player player, final float f) {
        final float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        this.lastBad = Math.round(f2 * 10.0f) * 0.1f;
        if (f < 0.1) {
            return true;
        }
        if (f2 <= 1.0f || Math.round(f2 * 10.0f) * 0.1f != f2 || Math.round(f2) == f2) {
            return true;
        }
        if (f2 == this.lastBad) {
            this.getAnticheat().failure(this, player, ChatColor.RED + "yaw check (C)", "(Type: C)");
            return true;
        }
        return false;
    }
    
    public float onAim3(final Player player, final float f) {
        final float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        if (f2 > 0.1f && Math.round(f2) == f2) {
            if (f2 == this.lastBad) {
                this.getAnticheat().failure(this, player, ChatColor.RED + "yaw check (C2)", "(Type: C2)");
                return f2;
            }
            this.lastBad = (float)Math.round(f2);
        }
        else {
            this.lastBad = 0.0f;
        }
        return 0.0f;
    }
}
