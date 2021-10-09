// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import java.util.ArrayList;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import java.util.List;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class BlockInteract extends Check
{
    public BlockInteract(final CerberusAntiCheat AntiCheat) {
        super("BlockInteract", "BlockInteract", AntiCheat);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void checkFreecam(final PlayerInteractEvent e) {
        Player player;
        UserData data;
        boolean isValid;
        Location scanLocation;
        double x;
        double y;
        double z;
        double sX;
        double sY;
        double sZ;
        Location relative;
        List<Location> blocks;
        boolean valid;
        final Iterator<Location> iterator;
        Location l;
        this.async(() -> {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                player = e.getPlayer();
                if (player != null) {
                    data = this.getAnticheat().getDataManager().getData(player);
                    if (data != null) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                isValid = false;
                                scanLocation = e.getClickedBlock().getRelative(e.getBlockFace()).getLocation();
                                x = scanLocation.getX();
                                y = scanLocation.getY();
                                z = scanLocation.getZ();
                                for (sX = x; sX < x + 2.0; ++sX) {
                                    for (sY = y; sY < y + 2.0; ++sY) {
                                        for (sZ = z; sZ < z + 2.0; ++sZ) {
                                            relative = new Location(scanLocation.getWorld(), sX, sY, sZ);
                                            blocks = this.rayTrace(player.getLocation().clone(), relative);
                                            valid = true;
                                            blocks.iterator();
                                            while (iterator.hasNext()) {
                                                l = iterator.next();
                                                if (!this.checkPhase(l.getBlock().getType())) {
                                                    valid = false;
                                                }
                                            }
                                            if (valid) {
                                                isValid = true;
                                            }
                                        }
                                    }
                                }
                                if (!isValid && !player.getPlayer().getItemInHand().getType().equals((Object)Material.ENDER_PEARL)) {
                                    if (data.isBlockInteractKick()) {
                                        e.setCancelled(true);
                                    }
                                    else {
                                        data.setBlockInteractKick(true);
                                        this.getAnticheat().failure(this, player, "Nuker/Freecam/Scaffold/Speedmine and similars!", (String[])null);
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    private List<Location> rayTrace(final Location from, final Location to) {
        final List<Location> a = new ArrayList<Location>();
        if (from == null || to == null) {
            return a;
        }
        if (!from.getWorld().equals(to.getWorld())) {
            return a;
        }
        if (from.distance(to) > 10.0) {
            return a;
        }
        double x1 = from.getX();
        double y1 = from.getY() + 1.62;
        double z1 = from.getZ();
        final double x2 = to.getX();
        final double y2 = to.getY();
        final double z2 = to.getZ();
        for (boolean scanning = true; scanning; scanning = false) {
            a.add(new Location(from.getWorld(), x1, y1, z1));
            x1 += (x2 - x1) / 10.0;
            y1 += (y2 - y1) / 10.0;
            z1 += (z2 - z1) / 10.0;
            if (Math.abs(x1 - x2) < 0.01 && Math.abs(y1 - y2) < 0.01 && Math.abs(z1 - z2) < 0.01) {}
        }
        return a;
    }
    
    public boolean checkPhase(final Material m) {
        final int[] array;
        final int[] whitelist = array = new int[] { 355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 13, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37 };
        for (final int ids : array) {
            if (m.getId() == ids) {
                return true;
            }
        }
        return false;
    }
}
