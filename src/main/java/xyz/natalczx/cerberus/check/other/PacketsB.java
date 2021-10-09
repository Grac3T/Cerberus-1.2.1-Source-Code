// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.EventPriority;
import java.util.AbstractMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import xyz.natalczx.cerberus.helper.SetBackSystem;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.HashMap;
import java.util.ArrayList;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class PacketsB extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> packetTicks;
    private final Map<UUID, Long> lastPacket;
    private final List<UUID> blacklist;
    
    public PacketsB(final CerberusAntiCheat AntiCheat) {
        super("PacketsB", "Packets", AntiCheat);
        this.blacklist = new ArrayList<UUID>();
        this.lastPacket = new HashMap<UUID, Long>();
        this.packetTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler
    public void PlayerJoin(final PlayerJoinEvent event) {
        this.async(() -> this.blacklist.add(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.packetTicks.remove(e.getPlayer().getUniqueId());
            this.lastPacket.remove(e.getPlayer().getUniqueId());
            this.blacklist.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void PlayerChangedWorld(final PlayerChangedWorldEvent event) {
        this.async(() -> this.blacklist.add(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler
    public void PlayerRespawn(final PlayerRespawnEvent event) {
        this.async(() -> this.blacklist.add(event.getPlayer().getUniqueId()));
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public final void PacketPlayer(final PacketPlayerEventB event) {
        final Player player = event.getPlayer();
        if (!this.getAnticheat().isEnabled()) {
            return;
        }
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (this.getAnticheat().lagAssist.getTPS() > 21.0 || this.getAnticheat().lagAssist.getTPS() < Settings.IMP.LAG_ASSIST.MAX_PING) {
            return;
        }
        if (this.getAnticheat().lagAssist.getPing(player) > 200) {
            return;
        }
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (this.packetTicks.containsKey(player.getUniqueId())) {
            Count = this.packetTicks.get(player.getUniqueId()).getKey();
            Time = this.packetTicks.get(player.getUniqueId()).getValue();
        }
        if (this.lastPacket.containsKey(player.getUniqueId())) {
            final long MS = System.currentTimeMillis() - this.lastPacket.get(player.getUniqueId());
            if (MS >= 100L) {
                this.blacklist.add(player.getUniqueId());
            }
            else if (MS > 1L && this.blacklist.contains(player.getUniqueId())) {
                this.blacklist.remove(player.getUniqueId());
            }
        }
        if (!this.blacklist.contains(player.getUniqueId())) {
            ++Count;
            if (this.packetTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000L)) {
                final int maxPackets = 50;
                if (Count > maxPackets && !UtilsB.isFullyStuck(player) && !UtilsB.isPartiallyStuck(player)) {
                    this.getAnticheat().failure(this, player, "sent over " + Count + " packet! ", "(Type: B)");
                    SetBackSystem.setBack(player);
                }
                if (Count > 400) {
                    this.getAnticheat().failure(this, player, ChatColor.RED + "Kicked, " + ChatColor.WHITE + "sent over " + Count + " packet! ", "(Type: B)");
                    CerberusAntiCheat.instance.getServer().getScheduler().runTask((Plugin)CerberusAntiCheat.instance, (Runnable)new Runnable() {
                        final Player p = event.getPlayer();
                        
                        @Override
                        public void run() {
                            player.kickPlayer("Too many packets");
                        }
                    });
                }
                Count = 0;
                Time = UtilTime.nowlong();
            }
        }
        this.packetTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
