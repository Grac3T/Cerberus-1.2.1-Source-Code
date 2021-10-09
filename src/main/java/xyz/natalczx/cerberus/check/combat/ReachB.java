// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventPriority;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.check.other.Latency;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.WeakHashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class ReachB extends Check
{
    public Map<Player, Integer> count;
    public Map<Player, Map.Entry<Double, Double>> offsets;
    
    public ReachB(final CerberusAntiCheat AntiCheat) {
        super("ReachB", "Reach", AntiCheat);
        this.setEnabled(true);
        this.setMaxViolations(7);
        this.setViolationResetTime(30000L);
        this.setBannable(true);
        this.setViolationsToNotify(1);
        this.offsets = new WeakHashMap<Player, Map.Entry<Double, Double>>();
        this.count = new WeakHashMap<Player, Integer>();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        double OffsetXZ;
        double horizontal;
        this.async(() -> {
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
                OffsetXZ = UtilsB.offset(UtilsB.getHorizontalVector(event.getFrom().toVector()), UtilsB.getHorizontalVector(event.getTo().toVector()));
                horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0) + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
                this.offsets.put(event.getPlayer(), new AbstractMap.SimpleEntry<Double, Double>(OffsetXZ, horizontal));
            }
        });
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(final EntityDamageByEntityEvent e) {
        Player damager;
        Player player;
        double Reach;
        double Reach2;
        int Count;
        long Time;
        double MaxReach;
        double YawDifference;
        double speedToVelocityDif;
        double offsets;
        double lastHorizontal;
        double speedToVelocityDif2;
        double MaxReach2;
        double MaxReach3;
        double MaxReach4;
        double Difference;
        double Difference2;
        double MaxReach5;
        int PingD;
        int PingP;
        double MaxReach6;
        long Time2;
        this.async(() -> {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && this.getAnticheat().getLagAssist().getTPS() >= Settings.IMP.LAG_ASSIST.MAX_TPS) {
                damager = (Player)e.getDamager();
                player = (Player)e.getEntity();
                Reach = UtilsB.trim(2, UtilsB.getEyeLocation(damager).distance(player.getEyeLocation()) - 0.32);
                Reach2 = UtilsB.trim(2, UtilsB.getEyeLocation(damager).distance(player.getEyeLocation()) - 0.32);
                if (!damager.getAllowFlight() && !player.getAllowFlight()) {
                    if (!this.count.containsKey(damager)) {
                        this.count.put(damager, 0);
                    }
                    Count = this.count.get(damager);
                    Time = System.currentTimeMillis();
                    MaxReach = 3.1;
                    YawDifference = Math.abs(damager.getEyeLocation().getYaw() - player.getEyeLocation().getYaw());
                    speedToVelocityDif = 0.0;
                    offsets = 0.0;
                    lastHorizontal = 0.0;
                    if (this.offsets.containsKey(damager)) {
                        offsets = this.offsets.get(damager).getKey();
                        lastHorizontal = this.offsets.get(damager).getValue();
                    }
                    if (Latency.getLag(damager) <= 92 && Latency.getLag(player) <= 92) {
                        speedToVelocityDif2 = Math.abs(offsets - player.getVelocity().length());
                        MaxReach2 = MaxReach + YawDifference * 0.001;
                        MaxReach3 = MaxReach2 + lastHorizontal * 1.5;
                        MaxReach4 = MaxReach3 + speedToVelocityDif2 * 0.08;
                        if (damager.getLocation().getY() > player.getLocation().clone().getY()) {
                            Difference = damager.getLocation().getY() - player.getLocation().clone().getY();
                            MaxReach4 += Difference / 2.5;
                        }
                        else if (player.getLocation().clone().getY() > damager.getLocation().getY()) {
                            Difference2 = player.getLocation().clone().getY() - damager.getLocation().getY();
                            MaxReach4 += Difference2 / 2.5;
                        }
                        MaxReach5 = MaxReach4 + ((damager.getWalkSpeed() <= 0.2) ? 0.0 : (damager.getWalkSpeed() - 0.2));
                        PingD = this.getAnticheat().getLagAssist().getPing(damager);
                        PingP = this.getAnticheat().getLagAssist().getPing(player);
                        MaxReach6 = MaxReach5 + (PingD + PingP) / 2 * 0.0024;
                        if (PingD > 400) {
                            ++MaxReach6;
                        }
                        if (UtilTime.elapsed(Time, 10000L)) {
                            this.count.remove(damager);
                            Time2 = System.currentTimeMillis();
                        }
                        if (Reach > MaxReach6) {
                            this.count.put(damager, Count + 1);
                        }
                        else if (Count >= -2) {
                            this.count.put(damager, Count - 1);
                        }
                        if (Reach2 > 6.0) {
                            e.setCancelled(true);
                        }
                        if (Count >= 2 && Reach > MaxReach6 && Reach < 20.0) {
                            this.count.remove(damager);
                            if (Latency.getLag(player) < 115) {
                                this.getAnticheat().failure(this, damager, Reach + " > " + MaxReach6 + " MS: " + PingD + " Velocity Difference: " + speedToVelocityDif2, "(Type: B)");
                            }
                        }
                    }
                }
            }
        });
    }
}
