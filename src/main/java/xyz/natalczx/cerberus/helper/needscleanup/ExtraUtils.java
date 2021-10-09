// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.needscleanup;

import org.bukkit.entity.LivingEntity;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Location;
import xyz.natalczx.cerberus.helper.UtilReflection;
import org.bukkit.entity.Player;

public class ExtraUtils
{
    public static boolean isOnGround(final Player player) {
        return isOnGround(player, 0.25);
    }
    
    public static boolean isOnGround(final Player player, final double yExpanded) {
        final Object box = UtilReflection.modifyBoundingBox(UtilReflection.getBoundingBox(player), 0.0, -yExpanded, 0.0, 0.0, 0.0, 0.0);
        return UtilReflection.getCollidingBlocks(player, box).size() > 0;
    }
    
    public static boolean isNotSpider(final Player player) {
        return isOnGround(player, 1.25);
    }
    
    public static boolean isInLiquid(final Player player) {
        final Object box = UtilReflection.getBoundingBox(player);
        final double minX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "a"), box);
        final double minY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "b"), box);
        final double minZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "c"), box);
        final double maxX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "d"), box);
        final double maxY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "e"), box);
        final double maxZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (isLiquid(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isInStairs(final Player player) {
        final Object box = UtilReflection.modifyBoundingBox(UtilReflection.getBoundingBox(player), 0.0, -0.5, 0.0, 0.0, 0.0, 0.0);
        final double minX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "a"), box);
        final double minY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "b"), box);
        final double minZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "c"), box);
        final double maxX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "d"), box);
        final double maxY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "e"), box);
        final double maxZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (isStair(block) || isSlab(block) || block.getType().equals((Object)Material.SKULL) || block.getType().equals((Object)Material.CAKE_BLOCK)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnClimbable(final Player player) {
        return isClimbableBlock(player.getLocation().clone().getBlock()) || isClimbableBlock(player.getLocation().clone().add(0.0, 1.0, 0.0).getBlock());
    }
    
    public static boolean inUnderBlock(final Player player) {
        final Object box = UtilReflection.modifyBoundingBox(UtilReflection.getBoundingBox(player), 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        final double minX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "a"), box);
        final double minY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "b"), box);
        final double minZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "c"), box);
        final double maxX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "d"), box);
        final double maxY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "e"), box);
        final double maxZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (block.getType().isSolid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean isOnIce(final Player player) {
        final Object box = UtilReflection.modifyBoundingBox(UtilReflection.getBoundingBox(player), 0.0, -0.5, 0.0, 0.0, 0.0, 0.0);
        final double minX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "a"), box);
        final double minY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "b"), box);
        final double minZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "c"), box);
        final double maxX = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "d"), box);
        final double maxY = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "e"), box);
        final double maxZ = (double)UtilReflection.getInvokedField(UtilReflection.getField(box.getClass(), "f"), box);
        for (double x = minX; x < maxX; ++x) {
            for (double y = minY; y < maxY; ++y) {
                for (double z = minZ; z < maxZ; ++z) {
                    final Block block = new Location(player.getWorld(), x, y, z).getBlock();
                    if (block.getType().equals((Object)Material.ICE) || block.getType().equals((Object)Material.PACKED_ICE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static float yawTo180F(float flub) {
        if ((flub %= 360.0f) >= 180.0f) {
            flub -= 360.0f;
        }
        if (flub < -180.0f) {
            flub += 360.0f;
        }
        return flub;
    }
    
    public static float[] getRotations(final Location one, final Location two) {
        final double diffX = two.getX() - one.getX();
        final double diffZ = two.getZ() - one.getZ();
        final double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[] { yaw, pitch };
    }
    
    public static double[] getOffsetFromEntity(final Player player, final LivingEntity entity) {
        final double yawOffset = Math.abs(yawTo180F(player.getEyeLocation().getYaw()) - yawTo180F(getRotations(player.getLocation().clone(), entity.getLocation())[0]));
        final double pitchOffset = Math.abs(Math.abs(player.getEyeLocation().getPitch()) - Math.abs(getRotations(player.getLocation().clone(), entity.getLocation())[1]));
        return new double[] { yawOffset, pitchOffset };
    }
    
    public static boolean isLiquid(final Block block) {
        final Material type = block.getType();
        return type.equals((Object)Material.WATER) || type.equals((Object)Material.STATIONARY_LAVA) || type.equals((Object)Material.LAVA) || type.equals((Object)Material.STATIONARY_LAVA);
    }
    
    public static boolean isClimbableBlock(final Block block) {
        return block.getType().equals((Object)Material.LADDER) || block.getType().equals((Object)Material.VINE);
    }
    
    public static boolean isSlab(final Block block) {
        return block.getTypeId() == 44 || block.getTypeId() == 126 || block.getTypeId() == 205 || block.getTypeId() == 182;
    }
    
    public static boolean isStair(final Block block) {
        return block.getType().equals((Object)Material.ACACIA_STAIRS) || block.getType().equals((Object)Material.BIRCH_WOOD_STAIRS) || block.getType().equals((Object)Material.BRICK_STAIRS) || block.getType().equals((Object)Material.COBBLESTONE_STAIRS) || block.getType().equals((Object)Material.DARK_OAK_STAIRS) || block.getType().equals((Object)Material.NETHER_BRICK_STAIRS) || block.getType().equals((Object)Material.JUNGLE_WOOD_STAIRS) || block.getType().equals((Object)Material.QUARTZ_STAIRS) || block.getType().equals((Object)Material.SMOOTH_STAIRS) || block.getType().equals((Object)Material.WOOD_STAIRS) || block.getType().equals((Object)Material.SANDSTONE_STAIRS) || block.getType().equals((Object)Material.SPRUCE_WOOD_STAIRS) || block.getTypeId() == 203 || block.getTypeId() == 180;
    }
    
    public static boolean groundAround(final Location loc) {
        for (int radius = 2, x = -radius; x < radius; ++x) {
            for (int y = -radius; y < radius; ++y) {
                for (int z = -radius; z < radius; ++z) {
                    final Material mat = loc.getWorld().getBlockAt(loc.add((double)x, (double)y, (double)z)).getType();
                    if (mat.isSolid() || mat == Material.WATER || mat == Material.STATIONARY_WATER || mat == Material.LAVA || mat == Material.STATIONARY_LAVA) {
                        loc.subtract((double)x, (double)y, (double)z);
                        return true;
                    }
                    loc.subtract((double)x, (double)y, (double)z);
                }
            }
        }
        return false;
    }
}
