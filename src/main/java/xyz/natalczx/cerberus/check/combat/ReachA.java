// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class ReachA extends Check
{
    public ReachA(final CerberusAntiCheat AntiCheat) {
        super("ReachA", "Reach", AntiCheat);
        this.setViolationResetTime(30000L);
    }
    
    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent e) {
        Player player;
        Entity entity;
        double distance;
        double maxReach;
        double yawDifference;
        double maxReach2;
        double maxReach3;
        this.async(() -> {
            if (!(!(e.getDamager() instanceof Player))) {
                player = (Player)e.getDamager();
                entity = e.getEntity();
                distance = UtilsA.getHorizontalDistance(player.getLocation().clone(), entity.getLocation()) - 0.35;
                maxReach = 4.22;
                yawDifference = 180.0f - Math.abs(Math.abs(player.getEyeLocation().getYaw()) - Math.abs(entity.getLocation().getYaw()));
                maxReach2 = maxReach + Math.abs(player.getVelocity().length() + entity.getVelocity().length()) * 0.4;
                maxReach3 = maxReach2 + yawDifference * 0.01;
                if (maxReach3 < 4.2) {
                    maxReach3 = 4.2;
                }
                if (distance > maxReach3) {
                    this.getAnticheat().failure(this, player, UtilsA.trim(3, distance) + " > " + UtilsA.trim(3, maxReach3), "(Type: A)");
                }
            }
        });
    }
}
