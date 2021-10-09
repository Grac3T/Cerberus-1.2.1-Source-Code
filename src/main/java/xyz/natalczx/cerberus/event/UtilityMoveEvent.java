// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.event;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Location;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import xyz.natalczx.cerberus.helper.TimerUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.user.UserDataManager;
import org.bukkit.event.Listener;

public class UtilityMoveEvent implements Listener
{
    private final UserDataManager dataManager;
    
    public UtilityMoveEvent() {
        this.dataManager = CerberusAntiCheat.getInstance().getDataManager();
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onMove(final PlayerMoveEvent e) {
        final Location l;
        final int x;
        final int y;
        final int z;
        final Location to;
        Player player;
        UserData data;
        Location loc1;
        double distance;
        boolean onGround;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            l = e.getFrom();
            x = l.getBlockX();
            y = l.getBlockY();
            z = l.getBlockZ();
            to = e.getTo();
            if (x != to.getX() || y != to.getY() || z != to.getZ()) {
                player = e.getPlayer();
                data = this.dataManager.getData(player);
                if (data != null) {
                    if (data.isNearIce() && TimerUtils.elapsed(data.getIsNearIceTicks(), 500L)) {
                        if (!UtilsA.isNearIce(player)) {
                            data.setNearIce(false);
                        }
                        else {
                            data.setIsNearIceTicks(TimerUtils.nowlong());
                        }
                    }
                    loc1 = new Location(player.getWorld(), (double)x, (double)(y + 1), (double)z);
                    if (loc1.getBlock().getType() != Material.AIR) {
                        if (!data.isBlockAbove_Set()) {
                            data.setBlockAbove_Set(true);
                            data.setBlockAbove(TimerUtils.nowlong());
                        }
                        else if (TimerUtils.elapsed(data.getBlockAbove(), 1000L)) {
                            if (loc1.getBlock().getType() == Material.AIR) {
                                data.setBlockAbove_Set(false);
                            }
                            else {
                                data.setBlockAbove_Set(true);
                                data.setBlockAbove(TimerUtils.nowlong());
                            }
                        }
                    }
                    else if (data.isBlockAbove_Set() && TimerUtils.elapsed(data.getBlockAbove(), 1000L)) {
                        if (loc1.getBlock().getType() == Material.AIR) {
                            data.setBlockAbove_Set(false);
                        }
                        else {
                            data.setBlockAbove_Set(true);
                            data.setBlockAbove(TimerUtils.nowlong());
                        }
                    }
                    if (UtilsA.hasIceNear(player)) {
                        if (data.getIceTicks() < 60) {
                            data.setIceTicks(data.getIceTicks() + 1);
                        }
                    }
                    else if (data.getIceTicks() > 0) {
                        data.setIceTicks(data.getIceTicks() - 1);
                    }
                    if (UtilsA.wasOnSlime(player)) {
                        if (data.getSlimeTicks() < 50) {
                            data.setSlimeTicks(data.getSlimeTicks() + 1);
                        }
                        else if (data.getSlimeTicks() > 0) {
                            data.setSlimeTicks(data.getSlimeTicks() - 1);
                        }
                    }
                    if (UtilsA.isHalfBlock(player.getLocation().clone().add(0.0, -0.5, 0.0).getBlock()) || UtilsA.isNearHalfBlock(player)) {
                        if (!data.isHalfBlocks_MS_Set()) {
                            data.setHalfBlocks_MS_Set(true);
                            data.setHalfBlocks_MS(TimerUtils.nowlong());
                        }
                        else if (TimerUtils.elapsed(data.getHalfBlocks_MS(), 900L)) {
                            if (UtilsA.isHalfBlock(player.getLocation().clone().add(0.0, -0.5, 0.0).getBlock()) || UtilsA.isNearHalfBlock(player)) {
                                data.setHalfBlocks_MS_Set(true);
                                data.setHalfBlocks_MS(TimerUtils.nowlong());
                            }
                            else {
                                data.setHalfBlocks_MS_Set(false);
                            }
                        }
                    }
                    else if (TimerUtils.elapsed(data.getHalfBlocks_MS(), 900L)) {
                        if (UtilsA.isHalfBlock(player.getLocation().clone().add(0.0, -0.5, 0.0).getBlock()) || UtilsA.isNearHalfBlock(player)) {
                            data.setHalfBlocks_MS_Set(true);
                            data.setHalfBlocks_MS(TimerUtils.nowlong());
                        }
                        else {
                            data.setHalfBlocks_MS_Set(false);
                        }
                    }
                    if (UtilsA.isNearIce(player) && !data.isNearIce()) {
                        data.setNearIce(true);
                        data.setIsNearIceTicks(TimerUtils.nowlong());
                    }
                    else if (UtilsA.isNearIce(player)) {
                        data.setIsNearIceTicks(TimerUtils.nowlong());
                    }
                    distance = UtilsA.getVerticalDistance(e.getFrom(), e.getTo());
                    onGround = UtilsA.isOnGround4(player);
                    if (!onGround && e.getFrom().getY() > e.getTo().getY()) {
                        data.setFallDistance(data.getFallDistance() + distance);
                    }
                    else {
                        data.setFallDistance(0.0);
                    }
                    if (onGround) {
                        data.setGroundTicks(data.getGroundTicks() + 1);
                        data.setAirTicks(0);
                    }
                    else {
                        data.setAirTicks(data.getAirTicks() + 1);
                        data.setGroundTicks(0);
                    }
                    if (UtilsA.isOnGround(player.getLocation().clone().add(0.0, 2.0, 0.0))) {
                        data.setAboveBlockTicks(data.getAboveBlockTicks() + 2);
                    }
                    else if (data.getAboveBlockTicks() > 0) {
                        data.setAboveBlockTicks(data.getAboveBlockTicks() - 1);
                    }
                    if (UtilsA.isInWater(player.getLocation().clone()) || UtilsA.isInWater(player.getLocation().clone().add(0.0, 1.0, 0.0))) {
                        data.setWaterTicks(data.getWaterTicks() + 1);
                    }
                    else if (data.getWaterTicks() > 0) {
                        data.setWaterTicks(data.getWaterTicks() - 1);
                    }
                }
            }
        });
    }
}
