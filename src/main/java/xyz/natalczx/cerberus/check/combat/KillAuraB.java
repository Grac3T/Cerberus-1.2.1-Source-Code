// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import org.bukkit.entity.LivingEntity;
import xyz.natalczx.cerberus.helper.UtilCheat;
import com.comphenix.protocol.wrappers.EnumWrappers;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraB extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> AuraTicks;
    
    public KillAuraB(final CerberusAntiCheat AntiCheat) {
        super("KillAuraB", "KillAura", AntiCheat);
        KillAuraB.AuraTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(false);
        this.setBannable(true);
        this.setMaxViolations(150);
        this.setViolationsToNotify(140);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            KillAuraB.AuraTicks.remove(uuid);
        });
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK || !(e.getAttacked() instanceof Player)) {
            return;
        }
        final Player damager = e.getAttacker();
        final Player player = (Player)e.getAttacked();
        if (damager.getAllowFlight() || player.getAllowFlight()) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (KillAuraB.AuraTicks.containsKey(damager.getUniqueId())) {
            Count = KillAuraB.AuraTicks.get(damager.getUniqueId()).getKey();
            Time = KillAuraB.AuraTicks.get(damager.getUniqueId()).getValue();
        }
        final double OffsetXZ = UtilCheat.getAimbotoffset(damager.getLocation(), damager.getEyeHeight(), (LivingEntity)player);
        double LimitOffset = 200.0;
        if (damager.getVelocity().length() > 0.08 || this.getAnticheat().getVelocityManager().getLastVelocity().containsKey(damager.getUniqueId())) {
            LimitOffset += 200.0;
        }
        final int Ping = this.getAnticheat().getLagAssist().getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            LimitOffset += 50.0;
        }
        else if (Ping >= 200 && Ping < 250) {
            LimitOffset += 75.0;
        }
        else if (Ping >= 250 && Ping < 300) {
            LimitOffset += 150.0;
        }
        else if (Ping >= 300 && Ping < 350) {
            LimitOffset += 300.0;
        }
        else if (Ping >= 350 && Ping < 400) {
            LimitOffset += 400.0;
        }
        else if (Ping > 400) {
            return;
        }
        if (OffsetXZ > LimitOffset * 4.0) {
            Count += 12;
        }
        else if (OffsetXZ > LimitOffset * 3.0) {
            Count += 10;
        }
        else if (OffsetXZ > LimitOffset * 2.0) {
            Count += 8;
        }
        else if (OffsetXZ > LimitOffset) {
            Count += 4;
        }
        if (KillAuraB.AuraTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 60000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count >= 16) {
            this.getAnticheat().failure(this, damager, "Hit miss ratio, " + Count + " >= 16 (B)", "(Type: B)");
            Count = 0;
        }
        KillAuraB.AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
