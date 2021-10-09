// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import java.util.Iterator;
import com.comphenix.protocol.reflect.StructureModifier;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import java.util.AbstractMap;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.helper.UtilTime;
import org.bukkit.Location;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.event.EventHandler;
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

public class AscensionA extends Check implements PacketBased
{
    private final Map<UUID, Map.Entry<Long, Double>> AscensionTicks;
    private final Map<UUID, Double> velocity;
    private final AscensionAAsync ascensionAAsync;
    
    public AscensionA(final CerberusAntiCheat AntiCheat) {
        super("AscensionA", "Ascension", AntiCheat);
        this.AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();
        this.velocity = new HashMap<UUID, Double>();
        this.ascensionAAsync = new AscensionAAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.ascensionAAsync;
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        this.async(() -> {
            this.AscensionTicks.remove(e.getPlayer().getUniqueId());
            this.velocity.remove(e.getPlayer().getUniqueId());
        });
    }
    
    private class AscensionAAsync extends PacketAdapter
    {
        public AscensionAAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            try {
                try {
                    if (!AscensionA.this.getAnticheat().isEnabled()) {
                        return;
                    }
                    final Player player = event.getPlayer();
                    if (player == null) {
                        return;
                    }
                    try {
                        if (player instanceof TemporaryPlayer) {
                            return;
                        }
                    }
                    catch (Exception ex) {}
                    final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                    if (data == null) {
                        return;
                    }
                    if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
                        return;
                    }
                    final Location from = player.getLocation().clone();
                    final StructureModifier<Double> doubles = (StructureModifier<Double>)event.getPacket().getDoubles();
                    final Location to = new Location(from.getWorld(), (double)doubles.read(0), (double)doubles.read(1), (double)doubles.read(2));
                    if (from.getY() >= to.getY() || !AscensionA.this.getAnticheat().isEnabled() || player.getAllowFlight() || player.getVehicle() != null || !UtilTime.elapsed(AscensionA.this.getAnticheat().getVelocityManager().getLastVelocity().getOrDefault(player.getUniqueId(), 0L), 4200L)) {
                        return;
                    }
                    long Time = System.currentTimeMillis();
                    double TotalBlocks = 0.0;
                    if (AscensionA.this.AscensionTicks.containsKey(player.getUniqueId())) {
                        Time = AscensionA.this.AscensionTicks.get(player.getUniqueId()).getKey();
                        TotalBlocks = AscensionA.this.AscensionTicks.get(player.getUniqueId()).getValue();
                    }
                    final long MS = System.currentTimeMillis() - Time;
                    final double OffsetY = UtilsB.offset(UtilsB.getVerticalVector(from.toVector()), UtilsB.getVerticalVector(to.toVector()));
                    if (OffsetY > 0.0) {
                        TotalBlocks += OffsetY;
                    }
                    final Location a = player.getLocation().clone().subtract(0.0, 1.0, 0.0);
                    if (UtilCheat.blocksNear(a)) {
                        TotalBlocks = 0.0;
                    }
                    double Limit = 1.05;
                    if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                        for (final PotionEffect effect : player.getActivePotionEffects()) {
                            if (effect.getType().equals((Object)PotionEffectType.JUMP)) {
                                final int level = effect.getAmplifier() + 1;
                                Limit += Math.pow(level + 4.2, 2.0) / 16.0 + 0.3;
                                break;
                            }
                        }
                    }
                    if (TotalBlocks > Limit) {
                        if (MS > 250L) {
                            if (AscensionA.this.velocity.containsKey(player.getUniqueId())) {
                                AscensionA.this.getAnticheat().failure(AscensionA.this, player, "Flew up " + UtilsB.trim(1, TotalBlocks) + " blocks", "(Type: A)");
                            }
                            Time = System.currentTimeMillis();
                        }
                    }
                    else {
                        Time = System.currentTimeMillis();
                    }
                    AscensionA.this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(Time, TotalBlocks));
                }
                catch (Exception ex2) {}
            }
            catch (Exception ex3) {}
        }
    }
}
