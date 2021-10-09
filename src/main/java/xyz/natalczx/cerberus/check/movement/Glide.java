// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import com.comphenix.protocol.reflect.StructureModifier;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.Location;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class Glide extends Check implements PacketBased
{
    private final Map<UUID, Long> flyTicks;
    private final GlideAsync glideAsync;
    
    public Glide(final CerberusAntiCheat AntiCheat) {
        super("Glide", "Glide", AntiCheat);
        this.flyTicks = new HashMap<UUID, Long>();
        this.glideAsync = new GlideAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.glideAsync;
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            this.flyTicks.remove(uuid);
        });
    }
    
    private class GlideAsync extends PacketAdapter
    {
        public GlideAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            try {
                if (!Glide.this.getAnticheat().isEnabled()) {
                    return;
                }
                final Player p = event.getPlayer();
                if (p == null) {
                    return;
                }
                try {
                    if (p instanceof TemporaryPlayer) {
                        return;
                    }
                }
                catch (Exception ex) {}
                final UserData data = Glide.this.getAnticheat().getDataManager().getData(p);
                if (data == null) {
                    return;
                }
                if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
                    return;
                }
                final Location from = p.getLocation().clone();
                final StructureModifier<Double> doubles = (StructureModifier<Double>)event.getPacket().getDoubles();
                final Location to = new Location(from.getWorld(), (double)doubles.read(0), (double)doubles.read(1), (double)doubles.read(2));
                final Player player = event.getPlayer();
                if (event.isCancelled() || to.getX() != from.getX() || to.getZ() != from.getZ() || player.getVehicle() != null || player.getAllowFlight() || Glide.this.getAnticheat().getLagAssist().getTPS() < Settings.IMP.LAG_ASSIST.MAX_TPS || UtilCheat.isInWeb(player)) {
                    return;
                }
                if (UtilCheat.blocksNear(player)) {
                    Glide.this.flyTicks.remove(player.getUniqueId());
                    return;
                }
                final double OffsetY = from.getY() - to.getY();
                if (OffsetY <= 0.0 || OffsetY > 0.16) {
                    Glide.this.flyTicks.remove(player.getUniqueId());
                    return;
                }
                long Time = System.currentTimeMillis();
                if (Glide.this.flyTicks.containsKey(player.getUniqueId())) {
                    Time = Glide.this.flyTicks.get(player.getUniqueId());
                }
                final long MS = System.currentTimeMillis() - Time;
                if (MS > 1000L) {
                    Glide.this.flyTicks.remove(player.getUniqueId());
                    if (Glide.this.getAnticheat().getLagAssist().getPing(player) < 275) {
                        Glide.this.getAnticheat().failure(Glide.this, player, null, (String[])null);
                    }
                    return;
                }
                Glide.this.flyTicks.put(player.getUniqueId(), Time);
            }
            catch (Exception ex2) {}
        }
    }
}
