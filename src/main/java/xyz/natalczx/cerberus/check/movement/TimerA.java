// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class TimerA extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> packets;
    private final Map<UUID, Integer> verbose;
    private final Map<UUID, Long> lastPacket;
    private final List<UUID> toCancel;
    
    public TimerA(final CerberusAntiCheat AntiCheat) {
        super("TimerA", "Timer", AntiCheat);
        this.packets = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.verbose = new HashMap<UUID, Integer>();
        this.toCancel = new ArrayList<UUID>();
        this.lastPacket = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.packets.remove(e.getPlayer().getUniqueId());
            this.verbose.remove(e.getPlayer().getUniqueId());
            this.lastPacket.remove(e.getPlayer().getUniqueId());
            this.toCancel.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler(ignoreCancelled = true)
    public void PacketPlayer(final PacketPlayerEventB event) {
        final Player player = event.getPlayer();
        if (!this.getAnticheat().isEnabled()) {
            return;
        }
        if (player == null) {
            return;
        }
        if (this.getAnticheat().getLagAssist().getTPS() < Settings.IMP.LAG_ASSIST.MAX_TPS) {
            return;
        }
        final UserData data = this.getAnticheat().getDataManager().getData(player);
        if (data == null) {
            return;
        }
        if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
            return;
        }
        final long lastPacket = this.lastPacket.getOrDefault(player.getUniqueId(), 0L);
        int packets = 0;
        long Time = System.currentTimeMillis();
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
        if (this.packets.containsKey(player.getUniqueId())) {
            packets = this.packets.get(player.getUniqueId()).getKey();
            Time = this.packets.get(player.getUniqueId()).getValue();
        }
        if (System.currentTimeMillis() - lastPacket < 5L) {
            this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
            return;
        }
        final double threshold = 21 + this.getAnticheat().getLagAssist().getPing(player) / 100;
        if (UtilTime.elapsed(Time, 1000L)) {
            if (this.toCancel.remove(player.getUniqueId()) && packets <= 13) {
                return;
            }
            if (packets > threshold + this.getAnticheat().packet.movePackets.getOrDefault(player.getUniqueId(), 0) && this.getAnticheat().packet.movePackets.getOrDefault(player.getUniqueId(), 0) < 5) {
                verbose = ((packets - threshold > 10.0) ? (verbose + 2) : (verbose + 1));
            }
            else {
                verbose = 0;
            }
            if (verbose > 2) {
                this.getAnticheat().failure(this, player, "Packets: " + packets, "(Type: A)");
            }
            packets = 0;
            Time = UtilTime.nowlong();
            this.getAnticheat().packet.movePackets.remove(player.getUniqueId());
        }
        ++packets;
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
        this.packets.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(packets, Time));
        this.verbose.put(player.getUniqueId(), verbose);
    }
}
