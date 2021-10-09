// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public class AngleY
{
    public static Vector getRotation(final Location location, final Location location2) {
        final double d = location2.getX() - location.getX();
        final double d2 = location2.getY() - location.getY();
        final double d3 = location2.getZ() - location.getZ();
        final double d4 = Math.sqrt(d * d + d3 * d3);
        final float f = (float)(Math.atan2(d3, d) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new Vector(f, f2, 0.0f);
    }
    
    public static double clamp180(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }
    
    public static double getVerticalDistance(final Location location, final Location location2) {
        final double d = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d2 = Math.sqrt(d);
        final double d3 = Math.abs(d2);
        return d3;
    }
    
    public static double getHorizontalDistance(final Location location, final Location location2) {
        double d = 0.0;
        final double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d4 = Math.sqrt(d2 + d3);
        d = Math.abs(d4);
        return d;
    }
    
    public static double fix180(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }
    
    public static double getDistance3D(final Location location, final Location location2) {
        final double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d4 = Math.sqrt(d + d2 + d3);
        final double d5 = Math.abs(d4);
        return d5;
    }
    
    public static float getOffset(final Player player, final LivingEntity livingEntity) {
        double d = 0.0;
        final Location location = player.getLocation().clone().add(0.0, player.getEyeHeight(), 0.0);
        final Location location2 = player.getLocation().clone().add(0.0, player.getEyeHeight(), 0.0);
        final Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        final Vector vector2 = getRotation(location2, location);
        final double d2 = clamp180(vector.getX() - vector2.getX());
        final double d3 = clamp180(vector.getY() - vector2.getY());
        final double d4 = getHorizontalDistance(location2, location);
        final double d5 = getDistance3D(location2, location);
        final double d6 = d2 * d4 * d5;
        final double d7 = d3 * Math.abs(location.getY() - location2.getY()) * d5;
        d += Math.abs(d6);
        d += Math.abs(d7);
        return 0.0f;
    }
}
