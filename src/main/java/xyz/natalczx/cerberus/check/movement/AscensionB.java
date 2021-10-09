// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.Location;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import java.util.WeakHashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class AscensionB extends Check implements PacketBased
{
    private final Map<Player, Integer> verbose;
    private final Map<Player, Float> lastYMovement;
    private final AscensionBAsync ascensionBAsync;
    
    public AscensionB(final CerberusAntiCheat AntiCheat) {
        super("AscensionB", "Ascension", AntiCheat);
        this.verbose = new WeakHashMap<Player, Integer>();
        this.lastYMovement = new WeakHashMap<Player, Float>();
        this.ascensionBAsync = new AscensionBAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.ascensionBAsync;
    }
    
    private class AscensionBAsync extends PacketAdapter
    {
        public AscensionBAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            if (!AscensionB.this.getAnticheat().isEnabled()) {
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
            final Location from = player.getLocation().clone();
            final StructureModifier<Double> doubles = (StructureModifier<Double>)event.getPacket().getDoubles();
            final Location to = new Location(from.getWorld(), (double)doubles.read(0), (double)doubles.read(1), (double)doubles.read(2));
            int verbose = AscensionB.this.verbose.getOrDefault(player, 0);
            final float yDelta = (float)(to.getY() - from.getY());
            if (player.getAllowFlight() || !AscensionB.this.lastYMovement.containsKey(player) || Math.abs(yDelta - AscensionB.this.lastYMovement.get(player)) > 0.002) {
                return;
            }
            if (verbose++ > 5) {
                CerberusAntiCheat.instance.failure(AscensionB.this, player, Math.abs(yDelta - AscensionB.this.lastYMovement.get(player)) + "<-" + 0.002, "(Type B)");
            }
            AscensionB.this.lastYMovement.put(player, yDelta);
            AscensionB.this.verbose.put(player, verbose);
        }
    }
}
