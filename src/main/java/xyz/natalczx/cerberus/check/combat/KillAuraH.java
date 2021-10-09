// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import xyz.natalczx.cerberus.helper.AngleUtil;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsC;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraH extends Check
{
    public KillAuraH(final CerberusAntiCheat AntiCheat) {
        super("KillAuraH", "KillAura", AntiCheat);
        this.setEnabled(true);
    }
    
    @EventHandler
    public void onAngleHit(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        Player player2;
        double d;
        double d2;
        double d3;
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getEntity() instanceof Player))) {
                if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                    player = (Player)entityDamageByEntityEvent.getDamager();
                    player2 = (Player)entityDamageByEntityEvent.getEntity();
                    d = UtilsC.getPing(player);
                    d2 = UtilsC.getPing(player2);
                    d3 = AngleUtil.getOffsets(player, (LivingEntity)player2)[0];
                    if (d2 <= 450.0) {
                        if (d >= 100.0 && d < 200.0) {
                            d3 -= 50.0;
                        }
                        else if (d >= 200.0 && d < 250.0) {
                            d3 -= 75.0;
                        }
                        else if (d >= 250.0 && d < 300.0) {
                            d3 -= 150.0;
                        }
                        else if (d >= 300.0 && d < 350.0) {
                            d3 -= 300.0;
                        }
                        else if (d >= 350.0 && d < 400.0) {
                            d3 -= 350.0;
                        }
                        else if (d > 400.0) {
                            return;
                        }
                        if (d3 >= 600.0) {
                            this.getAnticheat().failure(this, player, ChatColor.RED + "(Type: RayTrace v2) " + d3 + " >= 350.0", "(Type: H)");
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void onAngleHit2(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        double d;
        double d2;
        this.async(() -> {
            if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
                if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                    player = (Player)entityDamageByEntityEvent.getDamager();
                    d = UtilsC.getPing(player);
                    d2 = AngleUtil.getOffsets(player, (LivingEntity)entityDamageByEntityEvent.getEntity())[0];
                    if (d >= 100.0 && d < 200.0) {
                        d2 -= 50.0;
                    }
                    else if (d >= 200.0 && d < 250.0) {
                        d2 -= 75.0;
                    }
                    else if (d >= 250.0 && d < 300.0) {
                        d2 -= 150.0;
                    }
                    else if (d >= 300.0 && d < 350.0) {
                        d2 -= 300.0;
                    }
                    else if (d >= 350.0 && d < 400.0) {
                        d2 -= 350.0;
                    }
                    else if (d > 400.0) {
                        return;
                    }
                    if (d2 >= 300.0) {
                        this.getAnticheat().failure(this, player, ChatColor.RED + "(Type: RayTrace v3) " + d2 + " >= 300.0", "(Type: H2)");
                    }
                }
            }
        });
    }
}
