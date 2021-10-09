// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.player;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import xyz.natalczx.cerberus.packet.PacketCoreA;
import xyz.natalczx.cerberus.helper.TimerUtils;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.user.UserDataManager;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class PacketsA extends Check
{
    private final Map<UUID, Integer> packets;
    private final Map<UUID, Integer> verbose;
    private final Map<UUID, Long> lastPacket;
    private final List<UUID> toCancel;
    private final UserDataManager dataManager;
    
    public PacketsA(final CerberusAntiCheat AntiCheat) {
        super("PacketsA", "Packets", AntiCheat);
        this.dataManager = CerberusAntiCheat.getInstance().getDataManager();
        this.packets = new HashMap<UUID, Integer>();
        this.verbose = new HashMap<UUID, Integer>();
        this.toCancel = new ArrayList<UUID>();
        this.lastPacket = new HashMap<UUID, Long>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(10);
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
    
    @EventHandler
    public final void packetPlayer(final PacketPlayerEventA event) {
        final Player player = event.getPlayer();
        final UserData data = this.dataManager.getData(player);
        int packets = this.packets.getOrDefault(player.getUniqueId(), 0);
        long Time = this.lastPacket.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
        if (data == null) {
            return;
        }
        if (System.currentTimeMillis() - data.getLastPacket() > 100L) {
            this.toCancel.add(player.getUniqueId());
        }
        final double threshold = 30.0;
        if (TimerUtils.elapsed(Time, 1000L)) {
            if (this.toCancel.remove(player) && packets <= 67) {
                this.packets.put(player.getUniqueId(), 0);
                return;
            }
            if (packets > threshold + PacketCoreA.movePackets.getOrDefault(player.getUniqueId(), 0)) {
                ++verbose;
            }
            else {
                verbose = 0;
            }
            if (verbose > 2) {
                this.getAnticheat().failure(this, player, "sent over " + packets + " packet! ", "(Type: A)");
            }
            if (packets > 400) {
                this.getAnticheat().failure(this, player, ChatColor.RED + "Kicked, " + ChatColor.RED + "sent over " + packets + " packet! ", "(Type: A)");
                CerberusAntiCheat.instance.getServer().getScheduler().runTask((Plugin)CerberusAntiCheat.instance, (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        player.kickPlayer("Too many packets!");
                    }
                });
            }
            packets = 0;
            Time = System.currentTimeMillis();
            PacketCoreA.movePackets.remove(player.getUniqueId());
        }
        ++packets;
        this.packets.put(player.getUniqueId(), packets);
        this.verbose.put(player.getUniqueId(), verbose);
        this.lastPacket.put(player.getUniqueId(), Time);
    }
}
