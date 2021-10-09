// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.needscleanup;

import org.bukkit.util.Vector;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.GameMode;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.entity.Entity;
import java.util.logging.Level;
import org.apache.commons.lang.Validate;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Location;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.Random;
import java.util.HashSet;

public class UtilsC
{
    public static HashSet<Byte> blockPassSet;
    public static HashSet<Byte> blockAirFoliageSet;
    public static HashSet<Byte> fullSolid;
    public static HashSet<Byte> blockUseSet;
    public static Random random;
    private static Logger log;
    private static HashMap<String, Class<?>> classCache;
    private static HashMap<String, Field> fieldCache;
    private static HashMap<String, Method> methodCache;
    private static HashMap<String, Constructor> constructorCache;
    private static String obcPrefix;
    private static String nmsPrefix;
    private static Object MC_SERVER_OBJ;
    private static Field MC_SERVER_TPS_FIELD;
    
    public static Block getLowestBlockAt(final Location location) {
        Block block = location.getWorld().getBlockAt((int)location.getX(), 0, (int)location.getZ());
        if (block == null || block.getType().equals((Object)Material.AIR)) {
            block = location.getBlock();
            for (int n = (int)location.getY(); n > 0; --n) {
                final Block block2 = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
                final Block block3 = block2.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (block3 == null || block3.getType().equals((Object)Material.AIR)) {
                    block = block2;
                }
            }
        }
        return block;
    }
    
    public static boolean containsBlock(final Location location, final Material material) {
        for (int n = 0; n < 256; ++n) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && block.getType().equals((Object)material)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsBlock(final Location location) {
        for (int n = 0; n < 256; ++n) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && !block.getType().equals((Object)Material.AIR)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean containsBlockBelow(final Location location) {
        for (int n = 0; n < (int)location.getY(); ++n) {
            final Block block = location.getWorld().getBlockAt((int)location.getX(), n, (int)location.getZ());
            if (block != null && !block.getType().equals((Object)Material.AIR)) {
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<Block> getBlocksAroundCenter(final Location location, final int n) {
        final ArrayList<Block> arrayList = new ArrayList<Block>();
        for (int n2 = location.getBlockX() - n; n2 <= location.getBlockX() + n; ++n2) {
            for (int n3 = location.getBlockY() - n; n3 <= location.getBlockY() + n; ++n3) {
                for (int n4 = location.getBlockZ() - n; n4 <= location.getBlockZ() + n; ++n4) {
                    final Location location2 = new Location(location.getWorld(), (double)n2, (double)n3, (double)n4);
                    if (location2.distance(location) <= n) {
                        arrayList.add(location2.getBlock());
                    }
                }
            }
        }
        return arrayList;
    }
    
    public static Location stringToLocation(final String string) {
        final String[] arrstring = string.split(",");
        final World world = Bukkit.getWorld(arrstring[0]);
        final double d = Double.parseDouble(arrstring[1]);
        final double d2 = Double.parseDouble(arrstring[2]);
        final double d3 = Double.parseDouble(arrstring[3]);
        final float f = Float.parseFloat(arrstring[4]);
        final float f2 = Float.parseFloat(arrstring[5]);
        return new Location(world, d, d2, d3, f, f2);
    }
    
    public static String LocationToString(final Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }
    
    public static boolean isStair(final Block block) {
        final String string = block.getType().name().toLowerCase();
        return string.contains("stair") || string.contains("_step") || string.equals("step");
    }
    
    public static boolean isWeb(final Block block) {
        return block.getType() == Material.WEB;
    }
    
    public static boolean containsBlockType(final Material[] arrmaterial, final Block block) {
        for (final Material material : arrmaterial) {
            if (material == block.getType()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLiquid(final Block block) {
        return block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA);
    }
    
    public static boolean isSolid(final Block block) {
        return block != null && isSolid(block.getTypeId());
    }
    
    public static boolean isIce(final Block block) {
        return block != null && (block.getType() == Material.ICE || block.getType() == Material.PACKED_ICE);
    }
    
    public static boolean isAny(final Block block, final Material[] arrmaterial) {
        for (final Material material : arrmaterial) {
            if (block.getType().equals((Object)material)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isSolid(final int n) {
        return isSolid((byte)n);
    }
    
    public static boolean isSolid(final byte block) {
        if (UtilsC.blockPassSet.isEmpty()) {
            UtilsC.blockPassSet.add((Byte)0);
            UtilsC.blockPassSet.add((Byte)6);
            UtilsC.blockPassSet.add((Byte)8);
            UtilsC.blockPassSet.add((Byte)9);
            UtilsC.blockPassSet.add((Byte)10);
            UtilsC.blockPassSet.add((Byte)11);
            UtilsC.blockPassSet.add((Byte)27);
            UtilsC.blockPassSet.add((Byte)28);
            UtilsC.blockPassSet.add((Byte)30);
            UtilsC.blockPassSet.add((Byte)31);
            UtilsC.blockPassSet.add((Byte)32);
            UtilsC.blockPassSet.add((Byte)37);
            UtilsC.blockPassSet.add((Byte)38);
            UtilsC.blockPassSet.add((Byte)39);
            UtilsC.blockPassSet.add((Byte)40);
            UtilsC.blockPassSet.add((Byte)50);
            UtilsC.blockPassSet.add((Byte)51);
            UtilsC.blockPassSet.add((Byte)55);
            UtilsC.blockPassSet.add((Byte)59);
            UtilsC.blockPassSet.add((Byte)63);
            UtilsC.blockPassSet.add((Byte)66);
            UtilsC.blockPassSet.add((Byte)68);
            UtilsC.blockPassSet.add((Byte)69);
            UtilsC.blockPassSet.add((Byte)70);
            UtilsC.blockPassSet.add((Byte)72);
            UtilsC.blockPassSet.add((Byte)75);
            UtilsC.blockPassSet.add((Byte)76);
            UtilsC.blockPassSet.add((Byte)77);
            UtilsC.blockPassSet.add((Byte)78);
            UtilsC.blockPassSet.add((Byte)83);
            UtilsC.blockPassSet.add((Byte)90);
            UtilsC.blockPassSet.add((Byte)104);
            UtilsC.blockPassSet.add((Byte)105);
            UtilsC.blockPassSet.add((Byte)115);
            UtilsC.blockPassSet.add((Byte)119);
            UtilsC.blockPassSet.add((Byte)(-124));
            UtilsC.blockPassSet.add((Byte)(-113));
            UtilsC.blockPassSet.add((Byte)(-81));
            UtilsC.blockPassSet.add((Byte)(-85));
        }
        return !UtilsC.blockPassSet.contains(block);
    }
    
    public static boolean airFoliage(final Block block) {
        return block != null && airFoliage(block.getTypeId());
    }
    
    public static boolean airFoliage(final int n) {
        return airFoliage((byte)n);
    }
    
    public static boolean airFoliage(final byte block) {
        if (UtilsC.blockAirFoliageSet.isEmpty()) {
            UtilsC.blockAirFoliageSet.add((Byte)0);
            UtilsC.blockAirFoliageSet.add((Byte)6);
            UtilsC.blockAirFoliageSet.add((Byte)31);
            UtilsC.blockAirFoliageSet.add((Byte)32);
            UtilsC.blockAirFoliageSet.add((Byte)37);
            UtilsC.blockAirFoliageSet.add((Byte)38);
            UtilsC.blockAirFoliageSet.add((Byte)39);
            UtilsC.blockAirFoliageSet.add((Byte)40);
            UtilsC.blockAirFoliageSet.add((Byte)51);
            UtilsC.blockAirFoliageSet.add((Byte)59);
            UtilsC.blockAirFoliageSet.add((Byte)104);
            UtilsC.blockAirFoliageSet.add((Byte)105);
            UtilsC.blockAirFoliageSet.add((Byte)115);
            UtilsC.blockAirFoliageSet.add((Byte)(-115));
            UtilsC.blockAirFoliageSet.add((Byte)(-114));
        }
        return UtilsC.blockAirFoliageSet.contains(block);
    }
    
    public static boolean fullSolid(final Block block) {
        return block != null && fullSolid(block.getTypeId());
    }
    
    public static boolean fullSolid(final int n) {
        return fullSolid((byte)n);
    }
    
    public static boolean fullSolid(final byte block) {
        if (UtilsC.fullSolid.isEmpty()) {
            UtilsC.fullSolid.add((Byte)1);
            UtilsC.fullSolid.add((Byte)2);
            UtilsC.fullSolid.add((Byte)3);
            UtilsC.fullSolid.add((Byte)4);
            UtilsC.fullSolid.add((Byte)5);
            UtilsC.fullSolid.add((Byte)7);
            UtilsC.fullSolid.add((Byte)12);
            UtilsC.fullSolid.add((Byte)13);
            UtilsC.fullSolid.add((Byte)14);
            UtilsC.fullSolid.add((Byte)15);
            UtilsC.fullSolid.add((Byte)16);
            UtilsC.fullSolid.add((Byte)17);
            UtilsC.fullSolid.add((Byte)19);
            UtilsC.fullSolid.add((Byte)20);
            UtilsC.fullSolid.add((Byte)21);
            UtilsC.fullSolid.add((Byte)22);
            UtilsC.fullSolid.add((Byte)23);
            UtilsC.fullSolid.add((Byte)24);
            UtilsC.fullSolid.add((Byte)25);
            UtilsC.fullSolid.add((Byte)29);
            UtilsC.fullSolid.add((Byte)33);
            UtilsC.fullSolid.add((Byte)35);
            UtilsC.fullSolid.add((Byte)41);
            UtilsC.fullSolid.add((Byte)42);
            UtilsC.fullSolid.add((Byte)43);
            UtilsC.fullSolid.add((Byte)44);
            UtilsC.fullSolid.add((Byte)45);
            UtilsC.fullSolid.add((Byte)46);
            UtilsC.fullSolid.add((Byte)47);
            UtilsC.fullSolid.add((Byte)48);
            UtilsC.fullSolid.add((Byte)49);
            UtilsC.fullSolid.add((Byte)56);
            UtilsC.fullSolid.add((Byte)57);
            UtilsC.fullSolid.add((Byte)58);
            UtilsC.fullSolid.add((Byte)60);
            UtilsC.fullSolid.add((Byte)61);
            UtilsC.fullSolid.add((Byte)62);
            UtilsC.fullSolid.add((Byte)73);
            UtilsC.fullSolid.add((Byte)74);
            UtilsC.fullSolid.add((Byte)79);
            UtilsC.fullSolid.add((Byte)80);
            UtilsC.fullSolid.add((Byte)82);
            UtilsC.fullSolid.add((Byte)84);
            UtilsC.fullSolid.add((Byte)86);
            UtilsC.fullSolid.add((Byte)87);
            UtilsC.fullSolid.add((Byte)88);
            UtilsC.fullSolid.add((Byte)89);
            UtilsC.fullSolid.add((Byte)91);
            UtilsC.fullSolid.add((Byte)95);
            UtilsC.fullSolid.add((Byte)97);
            UtilsC.fullSolid.add((Byte)98);
            UtilsC.fullSolid.add((Byte)99);
            UtilsC.fullSolid.add((Byte)100);
            UtilsC.fullSolid.add((Byte)103);
            UtilsC.fullSolid.add((Byte)110);
            UtilsC.fullSolid.add((Byte)112);
            UtilsC.fullSolid.add((Byte)121);
            UtilsC.fullSolid.add((Byte)123);
            UtilsC.fullSolid.add((Byte)124);
            UtilsC.fullSolid.add((Byte)125);
            UtilsC.fullSolid.add((Byte)126);
            UtilsC.fullSolid.add((Byte)(-127));
            UtilsC.fullSolid.add((Byte)(-123));
            UtilsC.fullSolid.add((Byte)(-119));
            UtilsC.fullSolid.add((Byte)(-118));
            UtilsC.fullSolid.add((Byte)(-104));
            UtilsC.fullSolid.add((Byte)(-103));
            UtilsC.fullSolid.add((Byte)(-101));
            UtilsC.fullSolid.add((Byte)(-98));
        }
        return UtilsC.fullSolid.contains(block);
    }
    
    public static boolean usable(final Block block) {
        return block != null && usable(block.getTypeId());
    }
    
    public static boolean usable(final int n) {
        return usable((byte)n);
    }
    
    public static boolean usable(final byte block) {
        if (UtilsC.blockUseSet.isEmpty()) {
            UtilsC.blockUseSet.add((Byte)23);
            UtilsC.blockUseSet.add((Byte)26);
            UtilsC.blockUseSet.add((Byte)33);
            UtilsC.blockUseSet.add((Byte)47);
            UtilsC.blockUseSet.add((Byte)54);
            UtilsC.blockUseSet.add((Byte)58);
            UtilsC.blockUseSet.add((Byte)61);
            UtilsC.blockUseSet.add((Byte)62);
            UtilsC.blockUseSet.add((Byte)64);
            UtilsC.blockUseSet.add((Byte)69);
            UtilsC.blockUseSet.add((Byte)71);
            UtilsC.blockUseSet.add((Byte)77);
            UtilsC.blockUseSet.add((Byte)93);
            UtilsC.blockUseSet.add((Byte)94);
            UtilsC.blockUseSet.add((Byte)96);
            UtilsC.blockUseSet.add((Byte)107);
            UtilsC.blockUseSet.add((Byte)116);
            UtilsC.blockUseSet.add((Byte)117);
            UtilsC.blockUseSet.add((Byte)(-126));
            UtilsC.blockUseSet.add((Byte)(-111));
            UtilsC.blockUseSet.add((Byte)(-110));
            UtilsC.blockUseSet.add((Byte)(-102));
            UtilsC.blockUseSet.add((Byte)(-98));
        }
        return UtilsC.blockUseSet.contains(block);
    }
    
    public static HashMap<Block, Double> getInRadius(final Location location, final double d) {
        return getInRadius(location, d, 999.0);
    }
    
    public static HashMap<Block, Double> getInRadius(final Location location, final double d, final double d2) {
        final HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        for (int n = (int)d + 1, n2 = -n; n2 <= n; ++n2) {
            for (int n3 = -n; n3 <= n; ++n3) {
                for (int n4 = -n; n4 <= n; ++n4) {
                    final Block block;
                    final double d3;
                    if (Math.abs(n4) <= d2 && (d3 = offset(location, (block = location.getWorld().getBlockAt((int)(location.getX() + n2), (int)(location.getY() + n4), (int)(location.getZ() + n3))).getLocation().add(0.5, 0.5, 0.5))) <= d) {
                        hashMap.put(block, 1.0 - d3 / d);
                    }
                }
            }
        }
        return hashMap;
    }
    
    public static HashMap<Block, Double> getInRadius(final Block block, final double d) {
        final HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        for (int n = (int)d + 1, n2 = -n; n2 <= n; ++n2) {
            for (int n3 = -n; n3 <= n; ++n3) {
                for (int n4 = -n; n4 <= n; ++n4) {
                    final Block block2 = block.getRelative(n2, n4, n3);
                    final double d2 = offset(block.getLocation(), block2.getLocation());
                    if (d2 <= d) {
                        hashMap.put(block2, 1.0 - d2 / d);
                    }
                }
            }
        }
        return hashMap;
    }
    
    public static boolean isBlock(final ItemStack itemStack) {
        return itemStack != null && itemStack.getTypeId() > 0 && itemStack.getTypeId() < 256;
    }
    
    public static Block getHighest(final Location location) {
        return getHighest(location, null);
    }
    
    public static Block getHighest(final Location location, final HashSet<Material> hashSet) {
        location.setY(0.0);
        for (int n = 0; n < 256; ++n) {
            location.setY((double)(256 - n));
            if (isSolid(location.getBlock())) {
                break;
            }
        }
        return location.getBlock().getRelative(BlockFace.UP);
    }
    
    public static boolean isInAir(final Player player) {
        boolean bl = false;
        for (final Block block : getSurrounding(player.getLocation().clone().getBlock(), true)) {
            if (block.getType() == Material.AIR) {
                continue;
            }
            bl = true;
            break;
        }
        return bl;
    }
    
    public static ArrayList<Block> getSurrounding(final Block block, final boolean bl) {
        final ArrayList<Block> arrayList = new ArrayList<Block>();
        if (bl) {
            for (int n = -1; n <= 1; ++n) {
                for (int n2 = -1; n2 <= 1; ++n2) {
                    for (int n3 = -1; n3 <= 1; ++n3) {
                        if (n != 0 || n2 != 0 || n3 != 0) {
                            arrayList.add(block.getRelative(n, n2, n3));
                        }
                    }
                }
            }
        }
        else {
            arrayList.add(block.getRelative(BlockFace.UP));
            arrayList.add(block.getRelative(BlockFace.DOWN));
            arrayList.add(block.getRelative(BlockFace.NORTH));
            arrayList.add(block.getRelative(BlockFace.SOUTH));
            arrayList.add(block.getRelative(BlockFace.EAST));
            arrayList.add(block.getRelative(BlockFace.WEST));
        }
        return arrayList;
    }
    
    public static ArrayList<Block> getSurroundingXZ(final Block block) {
        final ArrayList<Block> arrayList = new ArrayList<Block>();
        arrayList.add(block.getRelative(BlockFace.NORTH));
        arrayList.add(block.getRelative(BlockFace.NORTH_EAST));
        arrayList.add(block.getRelative(BlockFace.NORTH_WEST));
        arrayList.add(block.getRelative(BlockFace.SOUTH));
        arrayList.add(block.getRelative(BlockFace.SOUTH_EAST));
        arrayList.add(block.getRelative(BlockFace.SOUTH_WEST));
        arrayList.add(block.getRelative(BlockFace.EAST));
        arrayList.add(block.getRelative(BlockFace.WEST));
        return arrayList;
    }
    
    public static String serializeLocation(final Location location) {
        final int n = (int)location.getX();
        final int n2 = (int)location.getY();
        final int n3 = (int)location.getZ();
        final int n4 = (int)location.getPitch();
        final int n5 = (int)location.getYaw();
        return location.getWorld().getName() + "," + n + "," + n2 + "," + n3 + "," + n4 + "," + n5;
    }
    
    public static Location deserializeLocation(final String string) {
        if (string == null) {
            return null;
        }
        final String[] arrstring = string.split(",");
        final World world = Bukkit.getServer().getWorld(arrstring[0]);
        final Double d = Double.parseDouble(arrstring[1]);
        final Double d2 = Double.parseDouble(arrstring[2]);
        final Double d3 = Double.parseDouble(arrstring[3]);
        final Float f = Float.parseFloat(arrstring[4]);
        final Float f2 = Float.parseFloat(arrstring[5]);
        final Location location = new Location(world, (double)d, (double)d2, (double)d3);
        location.setPitch((float)f);
        location.setYaw((float)f2);
        return location;
    }
    
    public static boolean isVisible(final Block block) {
        for (final Block block2 : getSurrounding(block, false)) {
            if (block2.getType().isOccluding()) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static Class<?> getCraftBukkitClass(final String string) {
        return getClass(UtilsC.obcPrefix + string);
    }
    
    public static Class<?> getNMSClass(final String string) {
        return getClass(UtilsC.nmsPrefix + string);
    }
    
    public static Class<?> getClass(final String string) {
        Validate.notNull((Object)string);
        if (UtilsC.classCache.containsKey(string)) {
            return UtilsC.classCache.get(string);
        }
        Class class_ = null;
        try {
            class_ = Class.forName(string);
        }
        catch (ClassNotFoundException classNotFoundException) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to find the the class " + string);
        }
        if (class_ != null) {
            UtilsC.classCache.put(string, class_);
        }
        return (Class<?>)class_;
    }
    
    public static Field getField(final String string, final Class<?> class_) {
        Validate.notNull((Object)string);
        Validate.notNull((Object)class_);
        final String string2 = class_.getCanonicalName() + "@" + string;
        if (UtilsC.fieldCache.containsKey(string2)) {
            return UtilsC.fieldCache.get(string2);
        }
        Field field = null;
        try {
            field = class_.getField(string);
        }
        catch (NoSuchFieldException noSuchFieldException) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to find the the field " + string + " in class " + class_.getSimpleName());
        }
        if (field != null) {
            UtilsC.fieldCache.put(string2, field);
        }
        return field;
    }
    
    public static Method getMethod(final Class<?> class_, final String string, final Class<?>[] arrclass) {
        Validate.notNull((Object)string);
        Validate.notNull((Object)class_);
        final String string2 = class_.getCanonicalName() + "@" + string;
        if (UtilsC.methodCache.containsKey(string2)) {
            return UtilsC.methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, arrclass);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            UtilsC.methodCache.put(string2, method);
        }
        return method;
    }
    
    public static Method getMethod(final String string, final Class<?> class_, final Class<?>[] arrclass) {
        Validate.notNull((Object)string);
        Validate.notNull((Object)class_);
        final String string2 = class_.getCanonicalName() + "@" + string;
        if (UtilsC.methodCache.containsKey(string2)) {
            return UtilsC.methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, arrclass);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            UtilsC.methodCache.put(string2, method);
        }
        return method;
    }
    
    public static Method getMethod(final String string, final Class<?> class_) {
        Validate.notNull((Object)string);
        Validate.notNull((Object)class_);
        final String string2 = class_.getCanonicalName() + "@" + string;
        if (UtilsC.methodCache.containsKey(string2)) {
            return UtilsC.methodCache.get(string2);
        }
        Method method = null;
        try {
            method = class_.getMethod(string, (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to find the the method " + string + " in class " + class_.getSimpleName());
        }
        if (method != null) {
            UtilsC.methodCache.put(string2, method);
        }
        return method;
    }
    
    public static Object getEntityHandle(final Entity entity) {
        try {
            final Method method = getMethod("getHandle", entity.getClass());
            return method.invoke(entity, new Object[0]);
        }
        catch (Exception exception) {
            UtilsC.log.log(Level.SEVERE, "[Reflection] Unable to getHandle of " + entity.getType());
            return null;
        }
    }
    
    public static void sendPacket(final Player player, final Object object) {
        Object object2 = null;
        try {
            object2 = getEntityHandle((Entity)player);
            final Object object3 = object2.getClass().getField("playerConnection").get(object2);
            getMethod("sendPacket", object3.getClass()).invoke(object3, object);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException) {
            invocationTargetException.printStackTrace();
        }
        catch (NoSuchFieldException noSuchFieldException) {
            noSuchFieldException.printStackTrace();
        }
    }
    
    public static void clear(final Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setMaxHealth(20.0);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.setExp(0.0f);
        player.setWalkSpeed(0.2f);
        player.setFlySpeed(0.1f);
        player.getInventory().clear();
        player.getInventory().setHelmet((ItemStack)null);
        player.getInventory().setChestplate((ItemStack)null);
        player.getInventory().setLeggings((ItemStack)null);
        player.getInventory().setBoots((ItemStack)null);
        player.updateInventory();
        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
    }
    
    public static Location getEyeLocation(final Player player) {
        final Location location = player.getLocation().clone();
        location.setY(location.getY() + player.getEyeHeight());
        return location;
    }
    
    public static boolean isOnClimbable(final Player player) {
        for (final Block block : getSurrounding(player.getLocation().clone().getBlock(), false)) {
            if (block.getType() != Material.LADDER && block.getType() != Material.VINE) {
                continue;
            }
            return true;
        }
        return player.getLocation().clone().getBlock().getType() == Material.LADDER || player.getLocation().clone().getBlock().getType() == Material.VINE;
    }
    
    public static boolean isOnGround(final Location location, final int n) {
        final double d = location.getX();
        final double d2 = location.getZ();
        final double d3 = (getFraction(d) > 0.0) ? Math.abs(getFraction(d)) : (1.0 - Math.abs(getFraction(d)));
        final double d4 = (getFraction(d2) > 0.0) ? Math.abs(getFraction(d2)) : (1.0 - Math.abs(getFraction(d2)));
        final int n2 = location.getBlockX();
        final int n3 = location.getBlockY() - n;
        final int n4 = location.getBlockZ();
        final World world = location.getWorld();
        if (isSolid(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (isSolid(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isSolid(world.getBlockAt(n2, n3, n4 - 1)) || isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isSolid(world.getBlockAt(n2, n3, n4 + 1)) || isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        else {
            if (d3 <= 0.7) {
                return (d4 < 0.3) ? isSolid(world.getBlockAt(n2, n3, n4 - 1)) : (d4 > 0.7 && isSolid(world.getBlockAt(n2, n3, n4 + 1)));
            }
            if (isSolid(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isSolid(world.getBlockAt(n2, n3, n4 - 1)) || isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isSolid(world.getBlockAt(n2, n3, n4 + 1)) || isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        return false;
    }
    
    public static boolean isOnGround(final Player player, final int n) {
        return isOnGround(player.getLocation().clone(), n);
    }
    
    public static boolean isOnBlock(final Location location, final int n, final Material[] arrmaterial) {
        final double d = location.getX();
        final double d2 = location.getZ();
        final double d3 = (getFraction(d) > 0.0) ? Math.abs(getFraction(d)) : (1.0 - Math.abs(getFraction(d)));
        final double d4 = (getFraction(d2) > 0.0) ? Math.abs(getFraction(d2)) : (1.0 - Math.abs(getFraction(d2)));
        final int n2 = location.getBlockX();
        final int n3 = location.getBlockY() - n;
        final int n4 = location.getBlockZ();
        final World world = location.getWorld();
        if (!world.isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() / 16)) {
            return false;
        }
        if (containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        else {
            if (d3 <= 0.7) {
                return (d4 < 0.3) ? containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)) : (d4 > 0.7 && containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1)));
            }
            if (containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1)) || containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        return false;
    }
    
    public static boolean isOnBlock(final Player player, final int n, final Material[] arrmaterial) {
        return isOnBlock(player.getLocation().clone(), n, arrmaterial);
    }
    
    public static boolean isHoveringOverWater(final Location location, final int n) {
        final double d = location.getX();
        final double d2 = location.getZ();
        final double d3 = (getFraction(d) > 0.0) ? Math.abs(getFraction(d)) : (1.0 - Math.abs(getFraction(d)));
        final double d4 = (getFraction(d2) > 0.0) ? Math.abs(getFraction(d2)) : (1.0 - Math.abs(getFraction(d2)));
        final int n2 = location.getBlockX();
        final int n3 = location.getBlockY() - n;
        final int n4 = location.getBlockZ();
        final World world = location.getWorld();
        if (!world.isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() / 16)) {
            return false;
        }
        if (isLiquid(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (isLiquid(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isLiquid(world.getBlockAt(n2, n3, n4 - 1)) || isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isLiquid(world.getBlockAt(n2, n3, n4 + 1)) || isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        else {
            if (d3 <= 0.7) {
                return (d4 < 0.3) ? isLiquid(world.getBlockAt(n2, n3, n4 - 1)) : (d4 > 0.7 && isLiquid(world.getBlockAt(n2, n3, n4 + 1)));
            }
            if (isLiquid(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isLiquid(world.getBlockAt(n2, n3, n4 - 1)) || isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isLiquid(world.getBlockAt(n2, n3, n4 + 1)) || isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        return false;
    }
    
    public static boolean isHoveringOverWater(final Player player, final int n) {
        return isHoveringOverWater(player.getLocation().clone(), n);
    }
    
    public static boolean isOnStairs(final Location location, final int n) {
        final double d = location.getX();
        final double d2 = location.getZ();
        final double d3 = (getFraction(d) > 0.0) ? Math.abs(getFraction(d)) : (1.0 - Math.abs(getFraction(d)));
        final double d4 = (getFraction(d2) > 0.0) ? Math.abs(getFraction(d2)) : (1.0 - Math.abs(getFraction(d2)));
        final int n2 = location.getBlockX();
        final int n3 = location.getBlockY() - n;
        final int n4 = location.getBlockZ();
        final World world = location.getWorld();
        if (!world.isChunkLoaded(location.getBlockX() / 16, location.getBlockZ() / 16)) {
            return false;
        }
        if (isStair(world.getBlockAt(n2, n3, n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (isStair(world.getBlockAt(n2 - 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isStair(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isStair(world.getBlockAt(n2, n3, n4 - 1)) || isStair(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isStair(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isStair(world.getBlockAt(n2, n3, n4 + 1)) || isStair(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        else {
            if (d3 <= 0.7) {
                return (d4 < 0.3) ? isStair(world.getBlockAt(n2, n3, n4 - 1)) : (d4 > 0.7 && isStair(world.getBlockAt(n2, n3, n4 + 1)));
            }
            if (isStair(world.getBlockAt(n2 + 1, n3, n4))) {
                return true;
            }
            if (d4 < 0.3) {
                return isStair(world.getBlockAt(n2 - 1, n3, n4 - 1)) || isStair(world.getBlockAt(n2, n3, n4 - 1)) || isStair(world.getBlockAt(n2 + 1, n3, n4 - 1));
            }
            if (d4 > 0.7) {
                return isStair(world.getBlockAt(n2 - 1, n3, n4 + 1)) || isStair(world.getBlockAt(n2, n3, n4 + 1)) || isStair(world.getBlockAt(n2 + 1, n3, n4 + 1));
            }
        }
        return false;
    }
    
    public static boolean isOnStairs(final Player player, final int n) {
        return isOnStairs(player.getLocation().clone(), n);
    }
    
    public static List<Entity> getEntities(final World world) {
        return new ArrayList<Entity>(world.getEntities());
    }
    
    public static int getPing(final Player player) {
        final Object object = getEntityHandle((Entity)player);
        if (object != null) {
            final Field field = getField("ping", object.getClass());
            try {
                return field.getInt(object);
            }
            catch (Exception ex) {}
        }
        return 150;
    }
    
    public static double[] getTps() {
        if (UtilsC.MC_SERVER_OBJ == null) {
            try {
                UtilsC.MC_SERVER_OBJ = getMethod("getServer", getNMSClass("MinecraftServer")).invoke(null, new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException reflectiveOperationException = ex;
                reflectiveOperationException.printStackTrace();
            }
            UtilsC.MC_SERVER_TPS_FIELD = getField("recentTps", getNMSClass("MinecraftServer"));
        }
        try {
            return (double[])UtilsC.MC_SERVER_TPS_FIELD.get(UtilsC.MC_SERVER_OBJ);
        }
        catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
            return null;
        }
    }
    
    public static double[] getRoundedTps() {
        final double[] arrd = getTps();
        for (int n = 0; n < arrd.length; ++n) {
            double d = arrd[n];
            d = (arrd[n] = Math.round(d * 100.0) / 100.0);
        }
        return arrd;
    }
    
    public static double getFraction(final double d) {
        return d % 1.0;
    }
    
    public static double round(final double d, final int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
    
    public static double trim(final int n, final double d) {
        String string = "#.#";
        for (int n2 = 1; n2 < n; ++n2) {
            string += "#";
        }
        final DecimalFormat decimalFormat = new DecimalFormat(string);
        return Double.valueOf(decimalFormat.format(d));
    }
    
    public static boolean close(final Double[] arrdouble, final int n) {
        final double d = arrdouble[4];
        final double d2 = arrdouble[3];
        final double d3 = arrdouble[2];
        final double d4 = arrdouble[1];
        final double d5 = arrdouble[0];
        final boolean bl2 = ((d >= d2) ? (d - d2) : (d2 - d)) <= n;
        final boolean bl3 = ((d >= d3) ? (d - d3) : (d3 - d)) <= n;
        final boolean bl4 = ((d >= d4) ? (d - d4) : (d4 - d)) <= n;
        final boolean bl6;
        final boolean bl5 = bl6 = (((d >= d5) ? (d - d5) : (d5 - d)) <= n);
        return bl2 && bl3 && bl4 && bl5;
    }
    
    public static double getXZDistance(final Location location, final Location location2) {
        final double d = Math.abs(Math.abs(location.getX()) - Math.abs(location2.getX()));
        final double d2 = Math.abs(Math.abs(location.getZ()) - Math.abs(location2.getZ()));
        return Math.sqrt(d * d + d2 * d2);
    }
    
    public static int r(final int n) {
        return UtilsC.random.nextInt(n);
    }
    
    public static double abs(final double d) {
        return (d <= 0.0) ? (0.0 - d) : d;
    }
    
    public static float getYawDifference(final Location location, final Location location2) {
        final float f = getYaw(location);
        final float f2 = getYaw(location2);
        float f3 = Math.abs(f - f2);
        if ((f < 90.0f && f2 > 270.0f) || (f2 < 90.0f && f > 270.0f)) {
            f3 -= 360.0f;
        }
        return Math.abs(f3);
    }
    
    public static float getYaw(final Location location) {
        float f = (location.getYaw() - 90.0f) % 360.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        return f;
    }
    
    public static String arrayToString(final String[] arrstring) {
        String string = "";
        for (final String string2 : arrstring) {
            string = string + string2 + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }
    
    public static String arrayToString(final List<String> list) {
        String string = "";
        for (final String string2 : list) {
            string = string + string2 + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }
    
    public static String[] stringtoArray(final String string, final String string2) {
        return string.split(string2);
    }
    
    public static double offset2d(final Entity entity, final Entity entity2) {
        return offset2d(entity.getLocation().toVector(), entity2.getLocation().toVector());
    }
    
    public static double offset2d(final Location location, final Location location2) {
        return offset2d(location.toVector(), location2.toVector());
    }
    
    public static double offset2d(final Vector vector, final Vector vector2) {
        vector.setY(0);
        vector2.setY(0);
        return vector.subtract(vector2).length();
    }
    
    public static double distanceXZSquared(final Location location, final Location location2) {
        final double d = location.getX() - location2.getX();
        final double d2 = location.getZ() - location2.getZ();
        return d * d + d2 * d2;
    }
    
    public static double offset(final Entity entity, final Entity entity2) {
        return offset(entity.getLocation().toVector(), entity2.getLocation().toVector());
    }
    
    public static double offset(final Location location, final Location location2) {
        return offset(location.toVector(), location2.toVector());
    }
    
    public static double offset(final Vector vector, final Vector vector2) {
        return vector.subtract(vector2).length();
    }
    
    public static Vector getHorizontalVector(final Vector vector) {
        vector.setY(0);
        return vector;
    }
    
    public static Vector getVerticalVector(final Vector vector) {
        vector.setX(0);
        vector.setZ(0);
        return vector;
    }
    
    public static long averageLong(final List<Long> list) {
        long l = 0L;
        for (final Long l2 : list) {
            l += l2;
        }
        return l / list.size();
    }
    
    public static double averageDouble(final List<Double> list) {
        Double d = 0.0;
        for (final Double d2 : list) {
            d += d2;
        }
        return d / list.size();
    }
    
    public static float averageFloat(final List<Float> list) {
        Float f = 0.0f;
        for (final Float f2 : list) {
            f += f2;
        }
        return f / list.size();
    }
    
    static {
        UtilsC.blockPassSet = new HashSet<Byte>();
        UtilsC.blockAirFoliageSet = new HashSet<Byte>();
        UtilsC.fullSolid = new HashSet<Byte>();
        UtilsC.blockUseSet = new HashSet<Byte>();
        UtilsC.random = new Random();
        UtilsC.log = Logger.getLogger("BoxUtils");
        UtilsC.classCache = new HashMap<String, Class<?>>(128);
        UtilsC.fieldCache = new HashMap<String, Field>(128);
        UtilsC.methodCache = new HashMap<String, Method>(128);
        UtilsC.constructorCache = new HashMap<String, Constructor>(128);
        UtilsC.obcPrefix = null;
        UtilsC.nmsPrefix = null;
        UtilsC.MC_SERVER_OBJ = null;
        UtilsC.MC_SERVER_TPS_FIELD = null;
        try {
            UtilsC.nmsPrefix = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
            UtilsC.obcPrefix = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }
        catch (Exception exception) {
            UtilsC.nmsPrefix = "net.minecraft.server.";
            UtilsC.obcPrefix = "org.bukkit.craftbukkit.";
        }
    }
}
