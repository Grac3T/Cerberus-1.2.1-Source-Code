// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.check.other.Latency;
import xyz.natalczx.cerberus.config.Settings;
import com.comphenix.protocol.wrappers.EnumWrappers;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class ReachC extends Check
{
    private final Map<Player, Map.Entry<Double, Double>> offsets;
    private final Map<Player, Long> reachTicks;
    private final List<Player> projectileHit;
    
    public ReachC(final CerberusAntiCheat AntiCheat) {
        super("ReachC", "Reach", AntiCheat);
        this.offsets = new HashMap<Player, Map.Entry<Double, Double>>();
        this.reachTicks = new HashMap<Player, Long>();
        this.projectileHit = new ArrayList<Player>();
        this.setViolationResetTime(30000L);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onMove(final RealMoveEvent event) {
        final double OffsetXZ;
        final double horizontal;
        this.async(() -> {
            OffsetXZ = UtilsB.offset(UtilsB.getHorizontalVector(event.getFrom().toVector()), UtilsB.getHorizontalVector(event.getTo().toVector()));
            horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0) + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
            this.offsets.put(event.getPlayer(), new AbstractMap.SimpleEntry<Double, Double>(OffsetXZ, horizontal));
        });
    }
    
    @EventHandler
    public void onDmg(final EntityDamageByEntityEvent e) {
        Player player;
        this.async(() -> {
            if (e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                player = (Player)e.getDamager();
                this.projectileHit.add(player);
            }
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(final PlayerQuitEvent e) {
        this.offsets.remove(e.getPlayer());
        this.reachTicks.remove(e.getPlayer());
        this.projectileHit.remove(e.getPlayer());
    }
    
    @EventHandler
    public void onDamage(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK || !(e.getAttacked() instanceof Player) || e.getAttacker().getAllowFlight() || this.getAnticheat().getLagAssist().getTPS() < Settings.IMP.LAG_ASSIST.MAX_TPS) {
            return;
        }
        final Player damager = e.getAttacker();
        final Player player = (Player)e.getAttacked();
        final double ydist = Math.abs(damager.getEyeLocation().getY() - player.getEyeLocation().getY());
        double Reach = UtilsB.trim(2, UtilsB.getEyeLocation(damager).distance(player.getEyeLocation()) - ydist - 0.32);
        final int PingD = this.getAnticheat().getLagAssist().getPing(damager);
        final int PingP = this.getAnticheat().getLagAssist().getPing(player);
        long attackTime = System.currentTimeMillis();
        if (this.reachTicks.containsKey(damager)) {
            attackTime = this.reachTicks.get(damager);
        }
        final double yawdif = Math.abs(180.0f - Math.abs(damager.getLocation().getYaw() - player.getLocation().clone().getYaw()));
        if (Latency.getLag(damager) > 92 || Latency.getLag(player) > 92) {
            return;
        }
        double offsetsp = 0.0;
        double lastHorizontal = 0.0;
        double offsetsd = 0.0;
        if (this.offsets.containsKey(damager)) {
            offsetsd = this.offsets.get(damager).getKey();
            lastHorizontal = this.offsets.get(damager).getValue();
        }
        if (this.offsets.containsKey(player)) {
            offsetsp = this.offsets.get(player).getKey();
            lastHorizontal = this.offsets.get(player).getValue();
        }
        Reach -= UtilsB.trim(2, offsetsd);
        Reach -= UtilsB.trim(2, offsetsp);
        double maxReach2 = 3.1;
        if (yawdif > 90.0) {
            maxReach2 += 0.38;
        }
        maxReach2 += lastHorizontal * 0.87;
        maxReach2 += (PingD + PingP) / 2 * 0.0024;
        if (Reach > maxReach2 && UtilTime.elapsed(attackTime, 1100L) && !this.projectileHit.contains(player)) {
            this.getAnticheat().failure(this, damager, "First Hit Reach", "(Type: C)");
        }
        this.reachTicks.put(damager, UtilTime.nowlong());
        this.projectileHit.remove(player);
    }
}
