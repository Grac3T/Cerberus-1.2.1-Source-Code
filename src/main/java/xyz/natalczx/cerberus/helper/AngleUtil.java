// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.Location;

public class AngleUtil
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
    
    public static double getVerticalDistance(final Location location, final Location location2) {
        final double d = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d2 = Math.sqrt(d);
        final double d3 = Math.abs(d2);
        return d3;
    }
    
    public static double getHorizontalDistance(final Location location, final Location location2) {
        final double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d2 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d3 = Math.sqrt(d + d2);
        final double d4 = Math.abs(d3);
        return d4;
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
    
    public static double[] getOffsets(final Player player, final LivingEntity livingEntity) {
        final Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        final Location location2 = player.getLocation().clone().add(0.0, player.getEyeHeight(), 0.0);
        final Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        final Vector vector2 = getRotation(location2, location);
        final double d = fix180(vector.getX() - vector2.getX());
        final double d2 = fix180(vector.getY() - vector2.getY());
        final double d3 = getHorizontalDistance(location2, location);
        final double d4 = getDistance3D(location2, location);
        final double d5 = d * d3 * d4;
        final double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[] { Math.abs(d5), Math.abs(d6) };
    }
}
