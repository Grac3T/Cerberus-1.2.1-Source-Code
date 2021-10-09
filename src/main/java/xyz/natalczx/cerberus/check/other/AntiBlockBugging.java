// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.ProtocolManager;
import java.util.Map;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.InvocationTargetException;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.Material;
import com.comphenix.protocol.wrappers.BlockPosition;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.Listener;

public class AntiBlockBugging implements Listener
{
    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        HashMap<BlockPosition, Integer> locs;
        Location loc;
        int x;
        int y;
        int z;
        Location acloc;
        final Location location;
        Block b;
        final Block block;
        final Location location2;
        BlockPosition bloc;
        final Map<BlockPosition, Integer> map;
        ProtocolManager pm;
        PacketContainer fakeBlock;
        final RuntimeException ex3;
        final Map<K, Integer> map2;
        final Iterator<BlockPosition> iterator;
        BlockPosition bloc2;
        ProtocolManager pm2;
        PacketContainer fakeBlock2;
        final RuntimeException ex4;
        Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
            if (e.isCancelled()) {
                locs = new HashMap<BlockPosition, Integer>();
                loc = e.getBlockPlaced().getLocation().clone();
                for (x = -4; x < 5; ++x) {
                    for (y = -4; y < 5; ++y) {
                        for (z = -4; z < 5; ++z) {
                            acloc = loc.clone().add((double)x, (double)y, (double)z);
                            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
                                b = location.getBlock();
                                Bukkit.getScheduler().runTaskAsynchronously((Plugin)CerberusAntiCheat.getInstance(), () -> {
                                    if (block.getType() == Material.AIR || UtilsA.isLiquid(block)) {
                                        bloc = new BlockPosition(location2.getBlockX(), location2.getBlockY(), location2.getBlockZ());
                                        try {
                                            map.put(bloc, block.getType().getId());
                                        }
                                        catch (Exception ex5) {}
                                        pm = ProtocolLibrary.getProtocolManager();
                                        fakeBlock = pm.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
                                        fakeBlock.getBlockPositionModifier().write(0, (Object)bloc);
                                        fakeBlock.getBlockData().write(0, (Object)WrappedBlockData.createData(Material.getMaterial(36)));
                                        try {
                                            pm.sendServerPacket(e.getPlayer(), fakeBlock);
                                        }
                                        catch (InvocationTargetException ex) {
                                            new RuntimeException("Cannot send packet " + fakeBlock, ex);
                                            throw ex3;
                                        }
                                    }
                                });
                                return;
                            });
                        }
                    }
                }
                Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
                    map2.keySet().iterator();
                    while (iterator.hasNext()) {
                        bloc2 = iterator.next();
                        try {
                            pm2 = ProtocolLibrary.getProtocolManager();
                            fakeBlock2 = pm2.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
                            fakeBlock2.getBlockPositionModifier().write(0, (Object)bloc2);
                            fakeBlock2.getBlockData().write(0, (Object)WrappedBlockData.createData(Material.getMaterial((int)map2.get(bloc2))));
                            try {
                                pm2.sendServerPacket(e.getPlayer(), fakeBlock2);
                            }
                            catch (InvocationTargetException ex2) {
                                new RuntimeException("Cannot send packet " + fakeBlock2, ex2);
                                throw ex4;
                            }
                        }
                        catch (Exception ex6) {}
                    }
                }, 20L);
            }
        }, 1L);
    }
}
