// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.Bukkit;
import java.util.Collection;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UtilReflection
{
    private static final String serverVersion;
    public static final Class<?> EntityPlayer;
    public static final Class<?> Entity;
    public static final Class<?> CraftPlayer;
    public static final Class<?> CraftWorld;
    public static final Class<?> World;
    private static final Class<?> CraftEntity;
    private static final Method getBlocks;
    private static final Method getBlocks1_12;
    
    public static Method getMethod(final Class<?> object, final String method, final Class<?>... args) {
        try {
            final Method methodObject = object.getMethod(method, args);
            methodObject.setAccessible(true);
            return methodObject;
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getInvokedMethod(final Method method, final Object object, final Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(object, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Field getField(final Class<?> object, final String field) {
        try {
            final Field fieldObject = object.getField(field);
            fieldObject.setAccessible(true);
            return fieldObject;
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getInvokedField(final Field field, final Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Class<?> getNMSClass(final String string) {
        return getClass("net.minecraft.server." + UtilReflection.serverVersion + "." + string);
    }
    
    public static boolean isBukkitVerison(final String version) {
        return UtilReflection.serverVersion.contains(version);
    }
    
    public static boolean isNewVersion() {
        return isBukkitVerison("1_9") || isBukkitVerison("1_1");
    }
    
    public static Class<?> getCBClass(final String string) {
        return getClass("org.bukkit.craftbukkit." + UtilReflection.serverVersion + "." + string);
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
    
    public static Collection<?> getCollidingBlocks(final Player player, final Object axisAlignedBB) {
        final Object world = getInvokedMethod(getMethod(UtilReflection.CraftWorld, "getHandle", (Class<?>[])new Class[0]), player.getWorld(), new Object[0]);
        return (Collection<?>)(isNewVersion() ? getInvokedMethod(UtilReflection.getBlocks1_12, world, null, axisAlignedBB) : getInvokedMethod(UtilReflection.getBlocks, world, axisAlignedBB));
    }
    
    public static Object getBoundingBox(final Player player) {
        return isBukkitVerison("1_7") ? getInvokedField(getField(UtilReflection.Entity, "boundingBox"), getEntityPlayer(player)) : getInvokedMethod(getMethod(UtilReflection.EntityPlayer, "getBoundingBox", (Class<?>[])new Class[0]), getEntityPlayer(player), new Object[0]);
    }
    
    public static Object expandBoundingBox(final Object box, final double x, final double y, final double z) {
        return getInvokedMethod(getMethod(box.getClass(), "grow", Double.TYPE, Double.TYPE, Double.TYPE), box, x, y, z);
    }
    
    public static Object modifyBoundingBox(final Object box, final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        final double newminX = (double)getInvokedField(getField(box.getClass(), "a"), box) + minX;
        final double newminY = (double)getInvokedField(getField(box.getClass(), "b"), box) + minY;
        final double newminZ = (double)getInvokedField(getField(box.getClass(), "c"), box) + minZ;
        final double newmaxX = (double)getInvokedField(getField(box.getClass(), "d"), box) + maxX;
        final double newmaxY = (double)getInvokedField(getField(box.getClass(), "e"), box) + maxY;
        final double newmaxZ = (double)getInvokedField(getField(box.getClass(), "f"), box) + maxZ;
        try {
            return getNMSClass("AxisAlignedBB").getConstructor(Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE).newInstance(newminX, newminY, newminZ, newmaxX, newmaxY, newmaxZ);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Object getEntityPlayer(final Player player) {
        return getInvokedMethod(getMethod(UtilReflection.CraftPlayer, "getHandle", (Class<?>[])new Class[0]), player, new Object[0]);
    }
    
    static {
        serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        EntityPlayer = getNMSClass("EntityPlayer");
        Entity = getNMSClass("Entity");
        CraftPlayer = getCBClass("entity.CraftPlayer");
        CraftWorld = getCBClass("CraftWorld");
        World = getNMSClass("World");
        CraftEntity = getCBClass("entity.CraftEntity");
        getBlocks = getMethod(UtilReflection.World, "a", getNMSClass("AxisAlignedBB"));
        getBlocks1_12 = getMethod(UtilReflection.World, "getCubes", getNMSClass("Entity"), getNMSClass("AxisAlignedBB"));
    }
}
