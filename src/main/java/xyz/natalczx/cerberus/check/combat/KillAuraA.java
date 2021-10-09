// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import xyz.natalczx.cerberus.helper.UtilTime;
import com.comphenix.protocol.wrappers.EnumWrappers;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraA extends Check
{
    public static Map<UUID, Long> lastMS;
    public static Map<UUID, List<Long>> clicks;
    public static Map<UUID, Map.Entry<Integer, Long>> clickTicks;
    
    public KillAuraA(final CerberusAntiCheat AntiCheat) {
        super("KillAuraA", "KillAura", AntiCheat);
        KillAuraA.lastMS = new HashMap<UUID, Long>();
        KillAuraA.clicks = new HashMap<UUID, List<Long>>();
        KillAuraA.clickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationResetTime(300000L);
        this.setMaxViolations(10);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            KillAuraA.lastMS.remove(uuid);
            KillAuraA.clicks.remove(uuid);
            KillAuraA.clickTicks.remove(uuid);
        });
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK || !(e.getAttacked() instanceof Player)) {
            return;
        }
        final Player damager = e.getAttacker();
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (KillAuraA.clickTicks.containsKey(damager.getUniqueId())) {
            Count = KillAuraA.clickTicks.get(damager.getUniqueId()).getKey();
            Time = KillAuraA.clickTicks.get(damager.getUniqueId()).getValue();
        }
        if (KillAuraA.lastMS.containsKey(damager.getUniqueId())) {
            final long MS = UtilTime.nowlong() - KillAuraA.lastMS.get(damager.getUniqueId());
            if (MS > 500L || MS < 5L) {
                KillAuraA.lastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (KillAuraA.clicks.containsKey(damager.getUniqueId())) {
                final List<Long> Clicks = KillAuraA.clicks.get(damager.getUniqueId());
                if (Clicks.size() == 10) {
                    KillAuraA.clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    final long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range < 30L) {
                        ++Count;
                        Time = System.currentTimeMillis();
                    }
                }
                else {
                    Clicks.add(MS);
                    KillAuraA.clicks.put(damager.getUniqueId(), Clicks);
                }
            }
            else {
                final List<Long> Clicks = new ArrayList<Long>();
                Clicks.add(MS);
                KillAuraA.clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (KillAuraA.clickTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if ((Count > 2 && this.getAnticheat().getLagAssist().getPing(damager) < 100) || (Count > 4 && this.getAnticheat().getLagAssist().getPing(damager) <= 400)) {
            Count = 0;
            this.getAnticheat().failure(this, damager, "Click pattern (A)", "(Type: A)");
            KillAuraA.clickTicks.remove(damager.getUniqueId());
        }
        KillAuraA.lastMS.put(damager.getUniqueId(), UtilTime.nowlong());
        KillAuraA.clickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
