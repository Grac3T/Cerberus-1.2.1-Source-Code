// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.EventPriority;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.packet.PacketPlayerType;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class Latency implements Listener
{
    private static final Map<UUID, Integer> packets;
    private final Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    private final Map<UUID, Long> lastPacket;
    private final List<UUID> blacklist;
    private CerberusAntiCheat ping;
    
    public Latency(final CerberusAntiCheat Ping) {
        this.ping = Ping;
        this.packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.lastPacket = new HashMap<UUID, Long>();
        this.blacklist = new ArrayList<UUID>();
    }
    
    public static int getLag(final Player player) {
        if (Latency.packets.containsKey(player.getUniqueId())) {
            return Latency.packets.get(player.getUniqueId());
        }
        return 0;
    }
    
    @EventHandler
    public void PlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)CerberusAntiCheat.getInstance(), () -> this.blacklist.add(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)CerberusAntiCheat.getInstance(), () -> {
            this.packetTicks.remove(e.getPlayer().getUniqueId());
            this.lastPacket.remove(e.getPlayer().getUniqueId());
            this.blacklist.remove(e.getPlayer().getUniqueId());
            Latency.packets.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void PlayerChangedWorld(final PlayerChangedWorldEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void PlayerRespawn(final PlayerRespawnEvent event) {
        this.blacklist.add(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PacketPlayer(final PacketPlayerEventB event) {
        final Player player = event.getPlayer();
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (this.ping.lagAssist.getTPS() > 21.0 || this.ping.lagAssist.getTPS() < Settings.IMP.LAG_ASSIST.MAX_TPS) {
            return;
        }
        if (event.getType() != PacketPlayerType.FLYING) {
            return;
        }
        int count = 0;
        long time = System.currentTimeMillis();
        if (this.packetTicks.containsKey(player.getUniqueId())) {
            count = this.packetTicks.get(player.getUniqueId()).getKey();
            time = this.packetTicks.get(player.getUniqueId()).getValue();
        }
        if (this.lastPacket.containsKey(player.getUniqueId())) {
            final long MS = System.currentTimeMillis() - this.lastPacket.get(player.getUniqueId());
            if (MS >= 100L) {
                this.blacklist.add(player.getUniqueId());
            }
            else if (MS > 1L) {
                this.blacklist.remove(player.getUniqueId());
            }
        }
        if (!this.blacklist.contains(player.getUniqueId())) {
            ++count;
            if (this.packetTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(time, 1000L)) {
                Latency.packets.put(player.getUniqueId(), count);
                count = 0;
                time = UtilTime.nowlong();
            }
        }
        this.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(count, time));
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    static {
        packets = new HashMap<UUID, Integer>();
    }
}
