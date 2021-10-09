// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.event;

import org.bukkit.event.player.PlayerVelocityEvent;
import java.util.Iterator;
import org.bukkit.block.Block;
import xyz.natalczx.cerberus.helper.needscleanup.ExtraUtils;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.Location;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.Listener;

public class MoveEvents implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(event.getPlayer());
        if (data == null) {
            return;
        }
        final boolean onGround = UtilsB.isGround(from);
        data.onGround = onGround;
        data.onStairSlab = this.isOnStairs(from);
        data.inLiquid = this.isInLiquid(from);
        data.onIce = this.isOnIce(from);
        data.onClimbable = isOnClimbable(from);
        data.underBlock = this.isUnderBlock(from);
        if (onGround) {
            final UserData userData = data;
            ++userData.groundTicks;
            data.airTicks = 0;
        }
        else {
            final UserData userData2 = data;
            ++userData2.airTicks;
            data.groundTicks = 0;
        }
        data.iceTicks = Math.max(0, data.onIce ? (data.iceTicks + 1) : (data.iceTicks - 1));
        data.liquidTicks = Math.max(0, data.inLiquid ? (data.liquidTicks + 1) : (data.liquidTicks - 1));
        data.blockTicks = Math.max(0, data.underBlock ? (data.blockTicks + 1) : (data.blockTicks - 1));
    }
    
    public static boolean isOnClimbable(final Location loc) {
        return ExtraUtils.isClimbableBlock(loc.getBlock()) || ExtraUtils.isClimbableBlock(loc.add(0.0, 1.0, 0.0).getBlock());
    }
    
    private boolean isOnIce(final Location loc) {
        for (int i = 0; i < 2; ++i) {
            for (final Block b : UtilsB.getBlocksAroundCenter(loc.clone().add(0.0, (double)(-i), 0.0), 2)) {
                if (b.getType().name().contains("ICE")) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isOnStairs(final Location loc) {
        for (final Block b : UtilsB.getBlocksAroundCenter(loc.clone().add(0.0, -0.1, 0.0), 1)) {
            if (b.getType().name().contains("STAIRS")) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isInLiquid(final Location loc) {
        for (int i = 0; i < 2; ++i) {
            for (final Block b : UtilsB.getBlocksAroundCenter(loc.clone().add(0.0, (double)i, 0.0), 2)) {
                if (b.isLiquid()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isUnderBlock(final Location loc) {
        for (final Block b : UtilsB.getBlocksAroundCenter(loc.clone().add(0.0, 1.0, 0.0), 2)) {
            if (b.getType().isSolid()) {
                return true;
            }
        }
        return false;
    }
    
    @EventHandler
    public void onVelocity(final PlayerVelocityEvent event) {
        final long time = System.currentTimeMillis();
        final UserData data;
        final long lastVelocityTaken;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            data = CerberusAntiCheat.getInstance().getDataManager().getDataPlayer(event.getPlayer());
            if (data != null) {
                if (event.getVelocity().getY() > -0.078 || event.getVelocity().getY() < -0.08) {
                    data.lastVelocityTaken = lastVelocityTaken;
                }
            }
        });
    }
}
