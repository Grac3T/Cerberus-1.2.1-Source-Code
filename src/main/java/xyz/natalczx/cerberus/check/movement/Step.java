// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import java.util.List;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import java.util.ArrayList;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class Step extends Check
{
    public Step(final CerberusAntiCheat AntiCheat) {
        super("Step", "Step", AntiCheat);
        this.setViolationsToNotify(1);
        this.setViolationResetTime(90000L);
    }
    
    public boolean isOnGround(final Player player) {
        if (UtilsB.isOnClimbable(player, 0)) {
            return false;
        }
        if (player.getVehicle() != null) {
            return false;
        }
        if (!player.getLocation().clone().getWorld().isChunkLoaded(player.getLocation().clone().getBlockX() / 16, player.getLocation().clone().getBlockZ() / 16)) {
            return false;
        }
        Material type = player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN).getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        type = a.getBlock().getType();
        if (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) {
            return true;
        }
        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        type = a.getBlock().getRelative(BlockFace.DOWN).getType();
        return (type != Material.AIR && type.isBlock() && type.isSolid() && type != Material.LADDER && type != Material.VINE) || UtilCheat.isBlock(player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER });
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player;
        UserData data;
        Location from;
        Location to;
        double yDist;
        double YSpeed;
        ArrayList<Block> blocks;
        final Iterator<Block> iterator;
        Block block;
        this.async(() -> {
            player = event.getPlayer();
            if (player != null) {
                try {
                    if (player instanceof TemporaryPlayer) {
                        return;
                    }
                }
                catch (Exception ex) {}
                data = this.getAnticheat().getDataManager().getData(player);
                if (data != null) {
                    if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                        if (this.getAnticheat().getLagAssist().getPing(player) != 0) {
                            if (this.getAnticheat().isEnabled() && this.isOnGround(player) && !player.getAllowFlight() && !player.hasPotionEffect(PotionEffectType.JUMP) && !this.getAnticheat().getVelocityManager().getLastVelocity().containsKey(player.getUniqueId()) && !UtilsB.isOnClimbable(player, 0) && !UtilCheat.slabsNear(player.getLocation().clone()) && !player.getLocation().clone().getBlock().getType().equals((Object)Material.WATER) && !player.getLocation().clone().getBlock().getType().equals((Object)Material.STATIONARY_WATER)) {
                                from = event.getFrom().clone();
                                to = event.getTo().clone();
                                yDist = to.getY() - from.getY();
                                if (yDist >= 0.0) {
                                    YSpeed = UtilsB.offset(UtilsB.getVerticalVector(from.toVector()), UtilsB.getVerticalVector(to.toVector()));
                                    if (yDist > 1.95) {
                                        this.getAnticheat().failure(this, player, Math.round(yDist) + " blocks (yDist: " + yDist + " > 0.95)", "(Type: A)");
                                    }
                                    else if ((((YSpeed == 0.25 || (YSpeed >= 0.58 && YSpeed < 0.581)) && yDist > 0.0) || (YSpeed > 0.2457 && YSpeed < 0.24582) || (YSpeed > 0.329 && YSpeed < 0.33)) && !player.getLocation().clone().subtract(0.0, 0.1, 0.0).getBlock().getType().equals((Object)Material.SNOW)) {
                                        this.getAnticheat().failure(this, player, "Speed: " + YSpeed + " Block: " + player.getLocation().clone().subtract(0.0, 0.1, 0.0).getBlock().getType().toString(), "(Type: B)");
                                    }
                                    else {
                                        blocks = UtilsB.getBlocksAroundCenter(player.getLocation().clone(), 1);
                                        blocks.iterator();
                                        while (iterator.hasNext()) {
                                            block = iterator.next();
                                            if (block.getType().isSolid() && YSpeed >= 0.321 && YSpeed < 0.322) {
                                                this.getAnticheat().failure(this, player, "Speed: " + YSpeed, "(Type: C)");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
