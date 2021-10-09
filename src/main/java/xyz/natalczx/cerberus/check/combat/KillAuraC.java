// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import org.bukkit.entity.Player;
import com.comphenix.protocol.wrappers.EnumWrappers;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraC extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> AimbotTicks;
    private final Map<UUID, Double> Differences;
    private final Map<UUID, Location> LastLocation;
    
    public KillAuraC(final CerberusAntiCheat AntiCheat) {
        super("KillAuraC", "KillAura", AntiCheat);
        this.AimbotTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.Differences = new HashMap<UUID, Double>();
        this.LastLocation = new HashMap<UUID, Location>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(14);
        this.setViolationResetTime(120000L);
        this.setViolationsToNotify(4);
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.AimbotTicks.remove(e.getPlayer().getUniqueId());
            this.Differences.remove(e.getPlayer().getUniqueId());
            this.LastLocation.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK || !(e.getAttacked() instanceof Player)) {
            return;
        }
        final Player damager = e.getAttacker();
        if (damager.getAllowFlight()) {
            return;
        }
        Location from = null;
        final Location to = damager.getLocation();
        if (this.LastLocation.containsKey(damager.getUniqueId())) {
            from = this.LastLocation.get(damager.getUniqueId());
        }
        this.LastLocation.put(damager.getUniqueId(), damager.getLocation());
        double Count = 0.0;
        long Time = System.currentTimeMillis();
        double LastDifference = -111111.0;
        if (this.Differences.containsKey(damager.getUniqueId())) {
            LastDifference = this.Differences.get(damager.getUniqueId());
        }
        if (this.AimbotTicks.containsKey(damager.getUniqueId())) {
            Count = this.AimbotTicks.get(damager.getUniqueId()).getKey();
            Time = this.AimbotTicks.get(damager.getUniqueId()).getValue();
        }
        if (from == null || (to.getX() == from.getX() && to.getZ() == from.getZ())) {
            return;
        }
        final double Difference = Math.abs(to.getYaw() - from.getYaw());
        if (Difference == 0.0) {
            return;
        }
        if (Difference > 2.4) {
            final double diff = Math.abs(LastDifference - Difference);
            if (e.getAttacked().getVelocity().length() < 0.1) {
                if (diff < 1.4) {
                    ++Count;
                }
                else {
                    Count = 0.0;
                }
            }
            else if (diff < 1.8) {
                ++Count;
            }
            else {
                Count = 0.0;
            }
        }
        this.Differences.put(damager.getUniqueId(), Difference);
        if (this.AimbotTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
            Count = 0.0;
            Time = UtilTime.nowlong();
        }
        if (Count > 5.0) {
            Count = 0.0;
            this.getAnticheat().failure(this, damager, "Aimbot (C)", "(Type: C)");
        }
        this.AimbotTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>((int)Math.round(Count), Time));
    }
}
