// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsC;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraK extends Check
{
    private double allowedDistance;
    
    public KillAuraK(final CerberusAntiCheat AntiCheat) {
        super("KillAuraK", "KillAura", AntiCheat);
        this.allowedDistance = 3.9;
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        Player player2;
        double d;
        double d2;
        int n2;
        int n3;
        int n4;
        int n5;
        double d3;
        final Iterator<PotionEffect> iterator;
        PotionEffect potionEffect;
        int n6;
        final Iterator<PotionEffect> iterator2;
        PotionEffect potionEffect2;
        int n7;
        double d4;
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getEntity() instanceof Player))) {
                if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                    player = (Player)entityDamageByEntityEvent.getDamager();
                    player2 = (Player)entityDamageByEntityEvent.getEntity();
                    d = UtilCheat.getHorizontalDistance(player.getLocation().clone(), player2.getLocation());
                    d2 = this.allowedDistance;
                    n2 = UtilsC.getPing(player);
                    n3 = UtilsC.getPing(player2);
                    n4 = n2 + n3 / 2;
                    n5 = (int)(n4 * 0.0017);
                    d3 = d2 + n5;
                    if (!player2.isSprinting()) {
                        d3 += 0.2;
                    }
                    if (!(!player2.isOnGround())) {
                        player2.getActivePotionEffects().iterator();
                        while (iterator.hasNext()) {
                            potionEffect = iterator.next();
                            if (potionEffect.getType() != PotionEffectType.SPEED) {
                                continue;
                            }
                            else {
                                n6 = potionEffect.getAmplifier() + 1;
                                d3 += 0.15 * n6;
                                break;
                            }
                        }
                        player.getActivePotionEffects().iterator();
                        while (iterator2.hasNext()) {
                            potionEffect2 = iterator2.next();
                            if (potionEffect2.getType() != PotionEffectType.SPEED) {
                                continue;
                            }
                            else {
                                n7 = potionEffect2.getAmplifier() + 1;
                                d3 += 0.15 * n7;
                                break;
                            }
                        }
                        d4 = d3 * 1.15;
                        if (d > d4) {
                            this.getAnticheat().failure(this, player, ChatColor.RED + "Heuristic (Flows) -> " + d + " is bigger than " + d4, "(Type: K)");
                        }
                    }
                }
            }
        });
    }
}
