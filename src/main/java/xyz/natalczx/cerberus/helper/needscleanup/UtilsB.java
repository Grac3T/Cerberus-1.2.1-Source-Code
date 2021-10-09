// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.needscleanup;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.util.Vector;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.math.BigDecimal;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import java.util.Iterator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.Collection;

public class UtilsB
{
    static String[] HalfBlocksArray;
    private static Collection<String> ill;
    private static Collection<String> fences;
    
    public static boolean isNearHalfBlock(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if (isHalfBlock(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static List<Block> getNearbyBlocks(final Location location, final int radius) {
        final List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
    
    public static boolean isNearIce(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if (isIce(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isNearStairPhase(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 2)) {
            if (isStair(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isNearFence(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if (isFence(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isNearSlime(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if (isSlime(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isHalfBlock(final Block block) {
        final Material type = block.getType();
        for (final String types : UtilsB.HalfBlocksArray) {
            if (type.toString().toLowerCase().contains(types)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAbuseBlock(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 2)) {
            if (b != null && b.getType() != Material.AIR) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean shouldCancelSpeedE(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if ((b != null && b.getType().name().contains("ICE")) || b.getType().name().contains("TRAP")) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isNearLiquid(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation().clone(), 1)) {
            if (isLiquid(b)) {
                out = true;
                break;
            }
        }
        return out;
    }
    
    public static boolean isIce(final Block block) {
        return block.getType().equals((Object)Material.ICE) || block.getType().equals((Object)Material.PACKED_ICE) || block.getType().equals((Object)Material.getMaterial("FROSTED_ICE"));
    }
    
    public static boolean isFence(final Block block) {
        return block.getType().name().contains("FENCE");
    }
    
    public static boolean isStair(final Block block) {
        return block.getType().name().contains("STAIR");
    }
    
    public static boolean isSlime(final Block block) {
        return block.getType().equals((Object)Material.SLIME_BLOCK);
    }
    
    public static boolean isLiquid(final Block block) {
        return block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA);
    }
    
    public static boolean isSolid(final Block material) {
        return material != null && isSolid(material.getTypeId());
    }
    
    public static boolean isSolid(final int block) {
        return isSolid((byte)block);
    }
    
    public static ArrayList<Block> getBlocksAroundCenter(final Location loc, final int radius) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = loc.getX() - radius; x <= loc.getX() + radius; ++x) {
            for (double z = loc.getZ() - radius; z <= loc.getZ() + radius; ++z) {
                final Location l = new Location(loc.getWorld(), x, (double)loc.getBlockY(), z);
                if (!l.getWorld().isChunkLoaded(l.getBlockX() / 16, l.getBlockZ() / 16)) {
                    return blocks;
                }
                blocks.add(l.getBlock());
            }
        }
        return blocks;
    }
    
    public static ArrayList<Block> getSurrounding(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            if (!block.getWorld().isChunkLoaded(block.getX() / 16, block.getZ() / 16)) {
                                return blocks;
                            }
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            if (!block.getWorld().isChunkLoaded(block.getX() / 16, block.getZ() / 16)) {
                return blocks;
            }
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static ArrayList<Block> getSurroundingB(final Block block) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        for (double x = -0.5; x <= 0.5; x += 0.5) {
            for (double y = -0.5; y <= 0.5; y += 0.5) {
                for (double z = -0.5; z <= 0.5; z += 0.5) {
                    if (x != 0.0 || y != 0.0 || z != 0.0) {
                        if (!block.getWorld().isChunkLoaded(block.getX() / 16, block.getZ() / 16)) {
                            return blocks;
                        }
                        blocks.add(block.getLocation().add(x, y, z).getBlock());
                    }
                }
            }
        }
        return blocks;
    }
    
    public static ArrayList<Player> getOnlinePlayers() {
        final ArrayList<Player> list = new ArrayList<Player>();
        for (final Player player : Bukkit.getOnlinePlayers()) {
            list.add(player);
        }
        return list;
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation().clone();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }
    
    public static boolean isInWater(final Player player) {
        final Location loc = player.getLocation().clone();
        if (!loc.getWorld().isChunkLoaded(loc.getBlockX() / 16, loc.getBlockZ() / 16)) {
            return false;
        }
        final Material m = loc.getBlock().getType();
        return m == Material.STATIONARY_WATER || m == Material.WATER;
    }
    
    public static boolean isOnClimbable(final Player player, final int blocks) {
        if (blocks == 0) {
            final Location ploc = player.getLocation().clone();
            if (!ploc.getWorld().isChunkLoaded(ploc.getBlockX() / 16, ploc.getBlockZ() / 16)) {
                return false;
            }
            for (final Block block : getSurrounding(ploc.getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        else {
            final Location loc = player.getLocation().clone().add(0.0, 1.0, 0.0);
            for (final Block block : getSurrounding(loc.getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        return player.getLocation().clone().getBlock().getType() == Material.LADDER || player.getLocation().clone().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isPartiallyStuck(final Player player) {
        if (player.getLocation().clone().getBlock() == null) {
            return false;
        }
        final Block block = player.getLocation().clone().getBlock();
        return !UtilCheat.isSlab(block) && !UtilCheat.isStair(block) && (player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || player.getLocation().clone().getBlock().getRelative(BlockFace.UP).getType().isSolid() || (player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock().getRelative(BlockFace.UP).getType().isSolid()) || block.getType().isSolid());
    }
    
    public static boolean isFullyStuck(final Player player) {
        final Block block1 = player.getLocation().clone().getBlock();
        final Block block2 = player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock();
        return (block1.getType().isSolid() && block2.getType().isSolid()) || block1.getRelative(BlockFace.DOWN).getType().isSolid() || (block1.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid() && block2.getRelative(BlockFace.DOWN).getType().isSolid()) || block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid();
    }
    
    public static boolean isOnGround(final Player player) {
        if (player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (a.getBlock().getType() != Material.AIR) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        return a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || UtilCheat.isBlock(player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static double getFraction(final double value) {
        return value % 1.0;
    }
    
    public static double trim(final int degree, final double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format += "#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }
    
    public static String decrypt(final String strEncrypted) {
        String strData = "";
        try {
            final byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = new String(decoded, StandardCharsets.UTF_8) + "\n";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }
    
    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }
    
    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }
    
    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }
    
    public static boolean isStable(final Block block) {
        final String name = block.getType().name();
        for (final String s : UtilsB.ill) {
            if (name.contains(s)) {
                return false;
            }
        }
        return !name.equals("REDSTONE") && !name.equals("AIR");
    }
    
    public static boolean isSemi(final Block block) {
        boolean ret = isStable(block);
        for (final String s : UtilsB.HalfBlocksArray) {
            if (block.getType().name().toLowerCase().contains(s)) {
                ret = false;
            }
        }
        if (block.getType().name().contains("_PANE")) {
            ret = false;
        }
        return ret;
    }
    
    public static boolean isFencez(final Block block) {
        final String name = block.getType().name();
        for (final String s : UtilsB.fences) {
            if (name.contains(s)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isGround(final Location location) {
        for (double i = -0.35; i < 0.36; i += 0.35) {
            for (double j = -0.35; j < 0.36; j += 0.35) {
                final Location t = location.clone().add(i, -0.1, j);
                if (!t.getWorld().isChunkLoaded(t.getBlockX() / 16, t.getBlockZ() / 16)) {
                    return false;
                }
                if (t.getBlock().getType() == Material.LADDER || t.getBlock().getType() == Material.VINE) {
                    return true;
                }
                if ((!isFencez(t.clone().add(0.0, -0.5, 0.0).getBlock()) || isStable(t.getBlock())) && !isSemi(t.add(0.0, 1.0, 0.0).getBlock())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        UtilsB.HalfBlocksArray = new String[] { "pot", "flower", "step", "slab", "snow", "detector", "daylight", "comparator", "repeater", "diode", "water", "lava", "ladder", "vine", "carpet", "sign", "pressure", "plate", "button", "mushroom", "torch", "frame", "armor", "banner", "lever", "hook", "redstone", "rail", "brewing", "rose", "skull", "enchantment", "cake", "bed", "chest", "anvil", "fence" };
        UtilsB.ill = (Collection<String>)ConcurrentHashMap.newKeySet();
        UtilsB.fences = (Collection<String>)ConcurrentHashMap.newKeySet();
        UtilsB.ill.add("TORCH");
        UtilsB.ill.add("PLATE");
        UtilsB.ill.add("LONG_GRASS");
        UtilsB.ill.add("BUTTON");
        UtilsB.ill.add("FLOWER");
        UtilsB.ill.add("SAPPLING");
        UtilsB.ill.add("ROSE");
        UtilsB.ill.add("SIGN");
        UtilsB.ill.add("RAIL");
        UtilsB.fences.add("FENCE");
    }
}
