// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import xyz.natalczx.cerberus.packet.events.PacketBlockPlacementEvent;
import xyz.natalczx.cerberus.packet.events.PacketHeldItemChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.packet.events.PacketSwingArmEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class CrashABC extends Check
{
    public static Map<UUID, Map.Entry<Integer, Long>> crashTicks;
    public static Map<UUID, Map.Entry<Integer, Long>> crash2Ticks;
    public static Map<UUID, Map.Entry<Integer, Long>> crash3Ticks;
    public List<UUID> crashs;
    
    public CrashABC(final CerberusAntiCheat AntiCheat) {
        super("Crash", "Crash", AntiCheat);
        CrashABC.crashTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        CrashABC.crash2Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        CrashABC.crash3Ticks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.crashs = new ArrayList<UUID>();
    }
    
    @EventHandler
    public void Swing(final PacketSwingArmEvent e) {
        final Player crash = e.getPlayer();
        if (this.crashs.contains(crash.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (CrashABC.crashTicks.containsKey(crash.getUniqueId())) {
            Count = CrashABC.crashTicks.get(crash.getUniqueId()).getKey();
            Time = CrashABC.crashTicks.get(crash.getUniqueId()).getValue();
        }
        ++Count;
        if (CrashABC.crashTicks.containsKey(crash.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getAnticheat().failure(this, crash, null, "(Type: A)");
            this.crashs.add(crash.getUniqueId());
        }
        CrashABC.crashTicks.put(crash.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
    
    @EventHandler
    public void Switch(final PacketHeldItemChangeEvent e) {
        final Player crash = e.getPlayer();
        if (this.crashs.contains(crash.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (CrashABC.crash2Ticks.containsKey(crash.getUniqueId())) {
            Count = CrashABC.crash2Ticks.get(crash.getUniqueId()).getKey();
            Time = CrashABC.crash2Ticks.get(crash.getUniqueId()).getValue();
        }
        ++Count;
        if (CrashABC.crash2Ticks.containsKey(crash.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getAnticheat().failure(this, crash, null, "(Type: B)");
            this.crashs.add(crash.getUniqueId());
        }
        CrashABC.crash2Ticks.put(crash.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
    
    @EventHandler
    public void BlockPlace(final PacketBlockPlacementEvent e) {
        final Player crash = e.getPlayer();
        if (this.crashs.contains(crash.getUniqueId())) {
            e.getPacketEvent().setCancelled(true);
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (CrashABC.crash3Ticks.containsKey(crash.getUniqueId())) {
            Count = CrashABC.crash3Ticks.get(crash.getUniqueId()).getKey();
            Time = CrashABC.crash3Ticks.get(crash.getUniqueId()).getValue();
        }
        ++Count;
        if (CrashABC.crash3Ticks.containsKey(crash.getUniqueId()) && UtilTime.elapsed(Time, 100L)) {
            Count = 0;
            Time = UtilTime.nowlong();
        }
        if (Count > 2000) {
            this.getAnticheat().failure(this, crash, null, "(Type: C)");
            this.crashs.add(crash.getUniqueId());
        }
        CrashABC.crash3Ticks.put(crash.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
