// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.needscleanup;

import com.google.common.collect.Sets;
import java.text.DecimalFormat;
import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.Chunk;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.block.Block;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Field;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;

public class UtilsA
{
    private static final String version;
    public static final Class<?> EntityPlayer;
    public static final Class<?> Entity;
    private static final Class<?> CraftPlayer;
    private static final Class<?> CraftWorld;
    private static final Class<?> World;
    private static final Method getCubes;
    static String[] HalfBlocksArray;
    private static ImmutableSet<Material> ground;
    
    public static Object getEntityPlayer(final Player player) {
        return getMethodValue(getMethod(UtilsA.CraftPlayer, "getHandle", (Class<?>[])new Class[0]), player, new Object[0]);
    }
    
    public static Object getBoundingBox(final Player player) {
        return isBukkitVerison("1_7") ? getFieldValue(getFieldByName(UtilsA.Entity, "boundingBox"), getEntityPlayer(player)) : getMethodValue(getMethod(UtilsA.EntityPlayer, "getBoundingBox", (Class<?>[])new Class[0]), getEntityPlayer(player), new Object[0]);
    }
    
    public static boolean isBukkitVerison(final String version) {
        final String bukkit = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        return bukkit.contains(version);
    }
    
    public static Class<?> getCBClass(final String string) {
        return getClass("org.bukkit.craftbukkit." + UtilsA.version + "." + string);
    }
    
    public static Class<?> getClass(final String string) {
        try {
            return Class.forName(string);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getFieldByName(final Class<?> clazz, final String fieldName) {
        try {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean inBlock(final Player player, final Object axisAlignedBB) {
        final Object world = getMethodValue(getMethod(UtilsA.CraftWorld, "getHandle", (Class<?>[])new Class[0]), player.getWorld(), new Object[0]);
        final Object d = getMethodValue(UtilsA.getCubes, world, axisAlignedBB);
        if (d instanceof Boolean) {
            return (boolean)d;
        }
        return ((Collection)d).size() > 0;
    }
    
    public static Collection<?> getCollidingBlocks(final Player player, final Object axisAlignedBB) {
        final Object world = getMethodValue(getMethod(UtilsA.CraftWorld, "getHandle", (Class<?>[])new Class[0]), player.getWorld(), new Object[0]);
        final Object d = getMethodValue(UtilsA.getCubes, world, axisAlignedBB);
        if (d instanceof Boolean) {
            return new ArrayList<Object>();
        }
        return (Collection<?>)d;
    }
    
    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... args) {
        try {
            final Method method = clazz.getMethod(methodName, args);
            method.setAccessible(true);
            return method;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getMethodValue(final Method method, final Object object, final Object... args) {
        try {
            return method.invoke(object, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getFieldValue(final Field field, final Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Class<?> getNMSClass(final String string) {
        return getClass("net.minecraft.server." + UtilsA.version + "." + string);
    }
    
    public static boolean onGround2(final Player p) {
        return p.getLocation().getBlock().getType() != Material.AIR;
    }
    
    public static boolean isOnGround4(final Player player) {
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
        return a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR || isBlock(player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    public static int getDrs(final Location loc) {
        int distance = 0;
        for (double i = loc.getBlockY(); i >= 0.0; i -= 2.0) {
            loc.add(0.0, -1.0, 0.0);
            if (loc.getBlock().getType().isSolid()) {
                break;
            }
            distance += 2;
        }
        return distance;
    }
    
    public static int getDistanceToGround(final Player p) {
        final Location loc = p.getLocation().clone();
        final double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0.0; --i) {
            if (!loc.getWorld().isChunkLoaded(loc.getBlockX() / 16, loc.getBlockZ() / 16)) {
                return distance;
            }
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) {
                break;
            }
            ++distance;
        }
        return distance;
    }
    
    private static boolean isGround(final Material material) {
        return UtilsA.ground.contains((Object)material);
    }
    
    public static boolean isOnGround(final Location loc) {
        final double diff = 0.3;
        return !isGround(loc.clone().add(0.0, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(0.0, -0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(diff, -0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, -0.1, -diff).getBlock().getType()) || (getBlockHeight(loc.clone().subtract(0.0, 0.5, 0.0).getBlock()) != 0.0 && (!isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, 0.0).getBlock().getType()) || !isGround(loc.clone().add(0.0, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(0.0, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(diff, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, diff).getBlock().getType()) || !isGround(loc.clone().add(-diff, getBlockHeight(loc.getBlock()) - 0.1, -diff).getBlock().getType())));
    }
    
    public static boolean isOnGround(final Player player) {
        final Object box = getBoundingBox(player);
        final Object outcome = getMethodValue(getMethod(box.getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), box, 0.0, 0.1, 0.0);
        return inBlock(player, outcome);
    }
    
    public static boolean hasPistonNear(final Player player) {
        final Object box = getMethodValue(getMethod(getBoundingBox(player).getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), getBoundingBox(player), 2.0, 3.0, 2.0);
        final Collection<?> collidingBlocks = getCollidingBlocks(player, box);
        for (final Object object : collidingBlocks) {
            final double x = (double)getFieldValue(getFieldByName(object.getClass(), "a"), object);
            final double y = (double)getFieldValue(getFieldByName(object.getClass(), "b"), object);
            final double z = (double)getFieldValue(getFieldByName(object.getClass(), "c"), object);
            final Block block = new Location(player.getWorld(), x, y, z).getBlock();
            if (isPiston(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasIceNear(final Player player) {
        final Object box = getMethodValue(getMethod(getBoundingBox(player).getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), getBoundingBox(player), 0.0, 1.5, 0.0);
        final Collection<?> collidingBlocks = getCollidingBlocks(player, box);
        for (final Object object : collidingBlocks) {
            final double x = (double)getFieldValue(getFieldByName(object.getClass(), "a"), object);
            final double y = (double)getFieldValue(getFieldByName(object.getClass(), "b"), object);
            final double z = (double)getFieldValue(getFieldByName(object.getClass(), "c"), object);
            final Block block = new Location(player.getWorld(), x, y, z).getBlock();
            if (isIce(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean wasOnSlime(final Player player) {
        try {
            final UserData user = CerberusAntiCheat.getInstance().getDataManager().getData(player);
            if (user != null && user.getSetbackLocation() != null) {
                final Location location = user.getSetbackLocation().clone().subtract(0.0, 1.0, 0.0);
                return location.getBlock().getTypeId() == 165;
            }
        }
        catch (Exception ex) {
            final UserData user2 = CerberusAntiCheat.getInstance().getDataManager().getData(player);
            if (user2 != null && user2.getSetbackLocation() != null) {
                final Location location2 = user2.getSetbackLocation().clone().subtract(0.0, 1.0, 0.0);
                final Chunk chunk;
                Bukkit.getScheduler().runTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
                    chunk = location2.getBlock().getChunk();
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    return;
                });
            }
            return false;
        }
        return false;
    }
    
    public static boolean isOnGround3(final Player player) {
        final Object box = getBoundingBox(player);
        final Object outcome = getMethodValue(getMethod(box.getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), box, 0.0, 0.3, 0.0);
        return inBlock(player, outcome);
    }
    
    public static boolean isInWater(final Location loc) {
        final double diff = 0.3;
        return isLiquid(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || isLiquid(loc.clone().add(diff, 0.0, 0.0).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || isLiquid(loc.clone().add(0.0, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(0.0, 0.0, -diff).getBlock()) || isLiquid(loc.clone().add(diff, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(diff, 0.0, -diff).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, -diff).getBlock()) || (getBlockHeight(loc.clone().subtract(0.0, 0.5, 0.0).getBlock()) != 0.0 && (isLiquid(loc.clone().add(diff, 0.0, 0.0).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || isLiquid(loc.clone().add(0.0, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(0.0, 0.0, -diff).getBlock()) || isLiquid(loc.clone().add(diff, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(diff, 0.0, -diff).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, diff).getBlock()) || isLiquid(loc.clone().add(-diff, 0.0, -diff).getBlock())));
    }
    
    public static boolean isOnSlab(final Location loc) {
        final double diff = 0.3;
        return isSlab(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || isSlab(loc.clone().add(diff, 0.0, 0.0).getBlock()) || isSlab(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || isSlab(loc.clone().add(0.0, 0.0, diff).getBlock()) || isSlab(loc.clone().add(0.0, 0.0, -diff).getBlock()) || isSlab(loc.clone().add(diff, 0.0, diff).getBlock()) || isSlab(loc.clone().add(diff, 0.0, -diff).getBlock()) || isSlab(loc.clone().add(-diff, 0.0, diff).getBlock()) || isSlab(loc.clone().add(-diff, 0.0, -diff).getBlock());
    }
    
    public static boolean isOnStair(final Location loc) {
        final double diff = 0.3;
        return isStair(loc.clone().add(0.0, 0.0, 0.0).getBlock()) || isStair(loc.clone().add(diff, 0.0, 0.0).getBlock()) || isStair(loc.clone().add(-diff, 0.0, 0.0).getBlock()) || isStair(loc.clone().add(0.0, 0.0, diff).getBlock()) || isStair(loc.clone().add(0.0, 0.0, -diff).getBlock()) || isStair(loc.clone().add(diff, 0.0, diff).getBlock()) || isStair(loc.clone().add(diff, 0.0, -diff).getBlock()) || isStair(loc.clone().add(-diff, 0.0, diff).getBlock()) || isStair(loc.clone().add(-diff, 0.0, -diff).getBlock());
    }
    
    public static boolean hasSlabsNear(final Location location) {
        for (final Block block : getSurroundingXZ(location.getBlock(), true)) {
            if (isSlab(block)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isOnClimbable(final Player player, final int blocks) {
        if (blocks == 0) {
            for (final Block block : getSurrounding(player.getLocation().clone().getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        else {
            for (final Block block : getSurrounding(player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock(), false)) {
                if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                    return true;
                }
            }
        }
        return player.getLocation().clone().getBlock().getType() == Material.LADDER || player.getLocation().clone().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isInWeb(final Player player) {
        return player.getLocation().clone().getBlock().getType() == Material.WEB || player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB || player.getLocation().clone().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB;
    }
    
    public static ArrayList<Block> getSurrounding(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            for (int x = -1; x <= 1; ++x) {
                for (int y = -1; y <= 1; ++y) {
                    for (int z = -1; z <= 1; ++z) {
                        if (x != 0 || y != 0 || z != 0) {
                            blocks.add(block.getRelative(x, y, z));
                        }
                    }
                }
            }
        }
        else {
            blocks.add(block.getRelative(BlockFace.UP));
            blocks.add(block.getRelative(BlockFace.DOWN));
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location eye = player.getLocation().clone();
        eye.setY(eye.getY() + player.getEyeHeight());
        return eye;
    }
    
    public static boolean isBlock(final Block block, final Material[] materials) {
        final Material type = block.getType();
        final Material[] arrayOfMaterial = materials;
        for (int j = materials.length, i = 0; i < j; ++i) {
            final Material m = arrayOfMaterial[i];
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.WEST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.NORTH).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.EAST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.SOUTH).getType().equals((Object)Material.AIR);
    }
    
    public static int getPotionEffectLevel(final Player player, final PotionEffectType pet) {
        for (final PotionEffect pe : player.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    public static boolean isLiquid(final Block block) {
        return block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA;
    }
    
    public static boolean isIce(final Block block) {
        return block.getType().equals((Object)Material.ICE) || block.getType().equals((Object)Material.PACKED_ICE) || block.getType().equals((Object)Material.getMaterial("FROSTED_ICE"));
    }
    
    public static double getBlockHeight(final Block block) {
        if (block.getTypeId() == 44) {
            return 0.5;
        }
        if (block.getTypeId() == 53) {
            return 0.5;
        }
        if (block.getTypeId() == 85) {
            return 0.2;
        }
        if (block.getTypeId() == 54 || block.getTypeId() == 130) {
            return 0.125;
        }
        return 0.0;
    }
    
    public static boolean isPiston(final Block block) {
        return block.getType().equals((Object)Material.PISTON_MOVING_PIECE) || block.getType().equals((Object)Material.PISTON_EXTENSION) || block.getType().equals((Object)Material.PISTON_BASE) || block.getType().equals((Object)Material.PISTON_STICKY_BASE);
    }
    
    public static boolean isStair(final Block block) {
        return block.getType().equals((Object)Material.ACACIA_STAIRS) || block.getType().equals((Object)Material.BIRCH_WOOD_STAIRS) || block.getType().equals((Object)Material.BRICK_STAIRS) || block.getType().equals((Object)Material.COBBLESTONE_STAIRS) || block.getType().equals((Object)Material.DARK_OAK_STAIRS) || block.getType().equals((Object)Material.NETHER_BRICK_STAIRS) || block.getType().equals((Object)Material.JUNGLE_WOOD_STAIRS) || block.getType().equals((Object)Material.QUARTZ_STAIRS) || block.getType().equals((Object)Material.SMOOTH_STAIRS) || block.getType().equals((Object)Material.WOOD_STAIRS) || block.getType().equals((Object)Material.SANDSTONE_STAIRS) || block.getType().equals((Object)Material.SPRUCE_WOOD_STAIRS) || block.getTypeId() == 203 || block.getTypeId() == 180;
    }
    
    public static boolean isSlab(final Block block) {
        return block.getTypeId() == 44 || block.getTypeId() == 126 || block.getTypeId() == 205 || block.getTypeId() == 182;
    }
    
    public static ArrayList<Block> getSurroundingXZ(final Block block, final boolean diagonals) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        if (diagonals) {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.NORTH_EAST));
            blocks.add(block.getRelative(BlockFace.NORTH_WEST));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        else {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }
    
    public static boolean isHalfBlock(final Block block) {
        final Material type = block.getType();
        for (final String types : UtilsA.HalfBlocksArray) {
            if (type.toString().toLowerCase().contains(types)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isNearHalfBlock(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isHalfBlock(b)) {
                out = true;
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
        for (final Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isIce(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearStiar(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isStair(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearLiquid(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (isLiquid(b)) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean isNearPistion(final Player p) {
        boolean out = false;
        for (final Block b : getNearbyBlocks(p.getLocation(), 1)) {
            if (b.getType() == Material.PISTON_BASE || b.getType() == Material.PISTON_MOVING_PIECE || b.getType() == Material.PISTON_STICKY_BASE || b.getType() == Material.PISTON_EXTENSION) {
                out = true;
            }
        }
        return out;
    }
    
    public static boolean elapsed(final long from, final long required) {
        return System.currentTimeMillis() - from > required;
    }
    
    public static double trim(final int degree, final double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format += "#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }
    
    public static double getHorizontalDistance(final Location to, final Location from) {
        final double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        final double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));
        return Math.sqrt(x * x + z * z);
    }
    
    public static double getVerticalDistance(final Location to, final Location from) {
        final double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));
        return Math.sqrt(y * y);
    }
    
    static {
        version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        EntityPlayer = getNMSClass("EntityPlayer");
        Entity = getNMSClass("Entity");
        CraftPlayer = getCBClass("entity.CraftPlayer");
        CraftWorld = getCBClass("CraftWorld");
        World = getNMSClass("World");
        getCubes = getMethod(UtilsA.World, "a", getNMSClass("AxisAlignedBB"));
        UtilsA.HalfBlocksArray = new String[] { "pot", "flower", "step", "slab", "snow", "detector", "daylight", "comparator", "repeater", "diode", "water", "lava", "ladder", "vine", "carpet", "sign", "pressure", "plate", "button", "mushroom", "torch", "frame", "armor", "banner", "lever", "hook", "redstone", "rail", "brewing", "rose", "skull", "enchantment", "cake", "bed" };
        UtilsA.ground = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.SUGAR_CANE, (Enum[])new Material[] { Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.ACTIVATOR_RAIL, Material.AIR, Material.CARROT, Material.CROPS, Material.DEAD_BUSH, Material.DETECTOR_RAIL, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.DOUBLE_PLANT, Material.FIRE, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LAVA, Material.LEVER, Material.LONG_GRASS, Material.MELON_STEM, Material.NETHER_WARTS, Material.PORTAL, Material.POTATO, Material.POWERED_RAIL, Material.PUMPKIN_STEM, Material.RAILS, Material.RED_ROSE, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.REDSTONE_WIRE, Material.SAPLING, Material.SEEDS, Material.SIGN, Material.SIGN_POST, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.STONE_BUTTON, Material.STONE_PLATE, Material.SUGAR_CANE_BLOCK, Material.TORCH, Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WALL_SIGN, Material.WATER, Material.WEB, Material.WOOD_BUTTON, Material.WOOD_PLATE, Material.YELLOW_FLOWER });
    }
}
