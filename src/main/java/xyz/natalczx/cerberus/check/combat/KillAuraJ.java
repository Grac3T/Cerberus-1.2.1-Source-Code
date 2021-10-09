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

public class KillAuraJ extends Check
{
    private float lastYaw;
    private float lastBad;
    
    public KillAuraJ(final CerberusAntiCheat AntiCheat) {
        super("KillAuraJ", "KillAura", AntiCheat);
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onHit(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        float f;
        float f2;
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                if (!(!(entityDamageByEntityEvent.getEntity() instanceof Player))) {
                    player = (Player)entityDamageByEntityEvent.getDamager();
                    f = (this.lastYaw = player.getLocation().clone().getYaw());
                    f2 = Math.abs(f - this.lastYaw) % 180.0f;
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
            this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental Huzuni [V5X01]", "(Type: J)");
            return true;
        }
        return false;
    }
}
