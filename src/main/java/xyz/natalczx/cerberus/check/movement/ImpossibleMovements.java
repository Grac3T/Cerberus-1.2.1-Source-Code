// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.helper.TimerUtils;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.GameMode;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class ImpossibleMovements extends Check implements PacketBased
{
    private final IMAsync imAsync;
    
    public ImpossibleMovements(final CerberusAntiCheat AntiCheat) {
        super("ImpossibleMovements", "ImpossibleMovements", AntiCheat);
        this.imAsync = new IMAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.imAsync;
    }
    
    private class IMAsync extends PacketAdapter
    {
        public IMAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            try {
                if (!ImpossibleMovements.this.getAnticheat().isEnabled()) {
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
                if (p.getGameMode() == GameMode.CREATIVE) {
                    return;
                }
                final Location from = p.getLocation().clone();
                final StructureModifier<Double> doubles = (StructureModifier<Double>)event.getPacket().getDoubles();
                final Location to = new Location(from.getWorld(), (double)doubles.read(0), (double)doubles.read(1), (double)doubles.read(2));
                final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
                if (data != null) {
                    if (p.getLocation().add(0.0, -0.3, 0.0).getBlock().getType() == Material.CACTUS && p.getLocation().getBlock().getType() == Material.AIR) {
                        if (data.getAntiCactus_VL() >= 3) {
                            ImpossibleMovements.this.getAnticheat().failure(ImpossibleMovements.this, p, "(Anti Cactus)", "(Type: A)");
                        }
                        else {
                            data.setAntiCactus_VL(data.getAntiCactus_VL() + 1);
                        }
                    }
                    else {
                        data.setAntiCactus_VL(0);
                    }
                    if (!data.isWebFloatMS_Set() && p.getLocation().add(0.0, -0.5, 0.0).getBlock().getType() == Material.WEB) {
                        data.setWebFloatMS_Set(true);
                        data.setWebFloatMS(TimerUtils.nowlong());
                    }
                    else if (data.isWebFloatMS_Set()) {
                        if (to.getY() == from.getY()) {
                            final double x = Math.floor(from.getX());
                            final double z = Math.floor(from.getZ());
                            if (Math.floor(to.getX()) != x || Math.floor(to.getZ()) != z) {
                                if (data.getWebFloat_BlockCount() > 0) {
                                    if (p.getLocation().add(0.0, -0.5, 0.0).getBlock().getType() != Material.WEB) {
                                        data.setWebFloatMS_Set(false);
                                        data.setWebFloat_BlockCount(0);
                                    }
                                    ImpossibleMovements.this.getAnticheat().failure(ImpossibleMovements.this, p, "(Web Float)", "(Type: B)");
                                }
                                else {
                                    data.setWebFloat_BlockCount(data.getWebFloat_BlockCount() + 1);
                                }
                            }
                        }
                        else {
                            data.setWebFloatMS_Set(false);
                            data.setWebFloat_BlockCount(0);
                        }
                    }
                }
            }
            catch (Exception ex2) {}
        }
    }
}
