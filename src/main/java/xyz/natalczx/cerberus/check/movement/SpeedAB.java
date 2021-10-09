// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import java.util.Iterator;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import xyz.natalczx.cerberus.helper.UtilNewVelocity;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.TimerUtils;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.GameMode;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.Listener;
import xyz.natalczx.cerberus.check.Check;

public class SpeedAB extends Check implements Listener
{
    public SpeedAB(final CerberusAntiCheat AntiCheat) {
        super("SpeedAB", "Speed", AntiCheat);
        this.setViolationResetTime(TimeUnit.MINUTES.toMillis(3L));
    }
    
    @EventHandler
    public void onMove(final RealMoveEvent e) {
        Player p;
        UserData data;
        Location from;
        Location to;
        double speed;
        int verbose;
        double speedEffect;
        double n;
        double n2;
        double speedAThreshold;
        double speedAThreshold2;
        double speedAThreshold3;
        double speedAThreshold4;
        double speedAThreshold5;
        int x;
        int y;
        int z;
        Location blockLoc;
        Location loc;
        Location loc2;
        Location above;
        Location above2;
        double MaxAirSpeed;
        double maxSpeed;
        double MaxSpeedNEW;
        double Max;
        int level;
        double maxSpeed2;
        double MaxAirSpeed2;
        double maxSpeed3;
        double onGroundDiff;
        int speed_c_3_verbose;
        int level2;
        double maxSpeed4;
        double MaxSpeedNEW2;
        double MaxAirSpeed3;
        double maxSpeed5;
        boolean speedPot;
        final Iterator<PotionEffect> iterator;
        PotionEffect effect;
        this.async(() -> {
            try {
                if (!(!this.getAnticheat().isEnabled())) {
                    p = e.getPlayer();
                    if (p != null) {
                        try {
                            if (p instanceof TemporaryPlayer) {
                                return;
                            }
                        }
                        catch (Exception ex) {}
                        data = this.getAnticheat().getDataManager().getData(p);
                        if (data != null) {
                            if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                                if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                    if (System.currentTimeMillis() - data.getLastKnocked() >= 1000L) {
                                        if (this.getAnticheat().getLagAssist().getPing(p) <= 150) {
                                            if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                                from = e.getFrom();
                                                to = e.getTo();
                                                if (!(!from.getWorld().isChunkLoaded(from.getBlockX() / 16, from.getBlockZ() / 16))) {
                                                    if (!p.getAllowFlight() && !p.getGameMode().equals((Object)GameMode.CREATIVE) && p.getVehicle() == null && !UtilsA.isNearIce(p) && !UtilsB.isNearSlime(p) && !UtilsA.wasOnSlime(p)) {
                                                        if (data.isSpeed_PistonExpand_Set() && TimerUtils.elapsed(data.getSpeed_PistonExpand_MS(), 9900L)) {
                                                            data.setSpeed_PistonExpand_Set(false);
                                                        }
                                                        speed = UtilsA.getHorizontalDistance(from, to);
                                                        if (UtilsA.elapsed(data.getLastVelMS(), 3000L)) {
                                                            verbose = data.getSpeedAVerbose();
                                                            speedEffect = UtilsA.getPotionEffectLevel(p, PotionEffectType.SPEED);
                                                            n = ((data.getAirTicks() > 0) ? ((data.getAirTicks() >= 6) ? ((data.getAirTicks() == 13) ? 0.466 : 0.35) : (0.345 * Math.pow(0.986938064, data.getAirTicks()))) : ((data.getGroundTicks() > 5) ? 0.362 : ((data.getGroundTicks() == 3) ? 0.62 : 0.4)));
                                                            if (data.getAirTicks() > 0) {
                                                                n2 = -0.001 * data.getAirTicks() + 0.014;
                                                            }
                                                            else {
                                                                n2 = (0.018 - ((data.getGroundTicks() >= 6) ? 0.0 : (data.getGroundTicks() * 0.001))) * speedEffect;
                                                            }
                                                            speedAThreshold = n + n2;
                                                            speedAThreshold2 = ((data.getAboveBlockTicks() > 0) ? (speedAThreshold + 0.25) : speedAThreshold);
                                                            speedAThreshold3 = ((data.getIceTicks() > 0) ? (speedAThreshold2 + 0.14) : speedAThreshold2);
                                                            speedAThreshold4 = ((data.getSlimeTicks() > 0) ? (speedAThreshold3 + 0.1) : speedAThreshold3);
                                                            speedAThreshold5 = ((data.getIceTicks() > 0 && data.getAboveBlockTicks() > 0) ? (speedAThreshold4 + 0.24) : speedAThreshold4);
                                                            if (UtilsA.isOnStair(from) || UtilsA.isOnSlab(from)) {
                                                                speedAThreshold5 += 0.12;
                                                            }
                                                            if ((speed > speedAThreshold5 && speed < 47.0) || (speed > 59.9 && speed < 60.0)) {
                                                                verbose += 8;
                                                            }
                                                            else {
                                                                verbose = ((verbose > 0) ? (verbose - 1) : 0);
                                                            }
                                                            if (verbose > 40) {
                                                                if ((to.getX() == from.getX() && to.getY() == from.getY() && to.getZ() == from.getZ()) || p.getAllowFlight() || p.getGameMode().equals((Object)GameMode.CREATIVE) || p.getVehicle() != null || UtilsA.isNearIce(p) || UtilsB.isNearSlime(p) || UtilsA.wasOnSlime(p)) {
                                                                    return;
                                                                }
                                                                else {
                                                                    verbose = 0;
                                                                }
                                                            }
                                                            data.setSpeedAVerbose(verbose);
                                                        }
                                                        else {
                                                            data.setSpeedAVerbose(0);
                                                        }
                                                        x = from.getBlockX();
                                                        y = from.getBlockY();
                                                        z = from.getBlockZ();
                                                        blockLoc = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
                                                        loc = new Location(p.getWorld(), (double)x, (double)y, (double)z);
                                                        loc2 = new Location(p.getWorld(), (double)x, (double)(y + 1), (double)z);
                                                        above = new Location(p.getWorld(), (double)x, (double)(y + 2), (double)z);
                                                        above2 = new Location(p.getWorld(), (double)(x - 1), (double)(y + 2), (double)(z - 1));
                                                        MaxAirSpeed = 0.7;
                                                        maxSpeed = 0.42;
                                                        MaxSpeedNEW = 0.75;
                                                        if (data.isNearIce()) {
                                                            MaxSpeedNEW = 1.0;
                                                        }
                                                        Max = 0.28;
                                                        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                                                            level = UtilsA.getPotionEffectLevel(p, PotionEffectType.SPEED);
                                                            if (level > 0) {
                                                                maxSpeed2 = maxSpeed * (level * 20 * 0.011 + 1.0);
                                                                MaxAirSpeed *= level * 20 * 0.011 + 1.0;
                                                                maxSpeed = maxSpeed2 * (level * 20 * 0.011 + 1.0);
                                                                MaxSpeedNEW *= level * 20 * 0.011 + 1.0;
                                                            }
                                                        }
                                                        MaxAirSpeed2 = MaxAirSpeed + ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                                                        maxSpeed3 = maxSpeed + ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                                                        Label_1327_1: {
                                                            if (!UtilsA.isOnGround4(p) && speed >= MaxAirSpeed2 && !data.isNearIce() && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR && !UtilNewVelocity.didTakeVel(p) && !UtilsA.isNearStiar(p)) {
                                                                if (!UtilNewVelocity.didTakeVel(p) && UtilsA.getDistanceToGround(p) <= 4) {
                                                                    if (data.getSpeed2Verbose() < 8) {
                                                                        if (p.getNoDamageTicks() == 0 || UtilVelocity.didTakeVelocity(p) || UtilNewVelocity.didTakeVel(p) || from.clone().add(0.0, 1.94, 0.0).getBlock().getType() == Material.AIR) {
                                                                            data.setSpeed2Verbose(data.getSpeed2Verbose() + 1);
                                                                            break Label_1327_1;
                                                                        }
                                                                    }
                                                                    this.getAnticheat().failure(this, p, "[1] - Player Moved Too Fast (A).", "(Type: B)");
                                                                }
                                                                else {
                                                                    data.setSpeed2Verbose(0);
                                                                }
                                                            }
                                                        }
                                                        onGroundDiff = to.getY() - from.getY();
                                                        if (speed > Max && !UtilsA.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && to.getY() != from.getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && data.getAboveBlockTicks() != 0) {
                                                            this.getAnticheat().failure(this, p, "[2] - Player moved too fast", "(Type: B)");
                                                        }
                                                        if (speed > MaxAirSpeed2 && !UtilsA.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && to.getY() != from.getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && !UtilNewVelocity.didTakeVel(p) && !UtilVelocity.didTakeVelocity(p) && !UtilsA.hasPistonNear(p) && from.getBlock().getType() != Material.PISTON_MOVING_PIECE && from.getBlock().getType() != Material.PISTON_BASE && from.getBlock().getType() != Material.PISTON_STICKY_BASE && !UtilsA.isNearPistion(p) && !data.isSpeed_PistonExpand_Set()) {
                                                            if (!data.isSpeed_PistonExpand_Set()) {
                                                                speed_c_3_verbose = data.getSpeed_C_3_Verbose();
                                                                if (speed_c_3_verbose > 1) {
                                                                    this.getAnticheat().failure(this, p, "[3] - Player moved too fast (" + speed_c_3_verbose + " > 1)", "(Type: B)");
                                                                }
                                                                else {
                                                                    data.setSpeed_C_3_Verbose(data.getSpeed_C_3_Verbose() + 1);
                                                                }
                                                            }
                                                            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                                                                level2 = UtilsA.getPotionEffectLevel(p, PotionEffectType.SPEED);
                                                                if (level2 > 0) {
                                                                    maxSpeed4 = maxSpeed3 * (level2 * 20 * 0.011 + 1.0);
                                                                    MaxAirSpeed2 *= level2 * 20 * 0.011 + 1.0;
                                                                    maxSpeed3 = maxSpeed4 * (level2 * 20 * 0.011 + 1.0);
                                                                    MaxSpeedNEW2 = MaxSpeedNEW * (level2 * 20 * 0.011 + 1.0);
                                                                }
                                                            }
                                                            MaxAirSpeed3 = MaxAirSpeed2 + ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                                                            maxSpeed5 = maxSpeed3 + ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
                                                            if (!UtilsA.isOnGround4(p) && speed >= MaxAirSpeed3 && !data.isNearIce() && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && blockLoc.getBlock().getType() != Material.AIR && !UtilNewVelocity.didTakeVel(p) && !UtilsA.isNearStiar(p)) {
                                                                if (!UtilNewVelocity.didTakeVel(p) && UtilsA.getDistanceToGround(p) <= 4) {
                                                                    if (data.getSpeed2Verbose() >= 8 || (p.getNoDamageTicks() != 0 && !UtilVelocity.didTakeVelocity(p) && !UtilNewVelocity.didTakeVel(p) && from.clone().add(0.0, 1.94, 0.0).getBlock().getType() != Material.AIR)) {
                                                                        this.getAnticheat().failure(this, p, "[1] - Player moved too fast (B)", "(Type: B)");
                                                                    }
                                                                    else {
                                                                        data.setSpeed2Verbose(data.getSpeed2Verbose() + 1);
                                                                    }
                                                                }
                                                                else {
                                                                    data.setSpeed2Verbose(0);
                                                                }
                                                                speedPot = false;
                                                                p.getActivePotionEffects().iterator();
                                                                while (iterator.hasNext()) {
                                                                    effect = iterator.next();
                                                                    if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                                                                        speedPot = true;
                                                                    }
                                                                }
                                                                if (speed > 0.29 && UtilsA.isOnGround(p) && !data.isNearIce() && !UtilsA.isNearStiar(p) && !UtilNewVelocity.didTakeVel(p) && !speedPot) {
                                                                    if (data.getSpeed_OnGround_Verbose() >= 5) {}
                                                                    if (speed > Max && !UtilsA.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && to.getY() != from.getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && data.getIceTicks() == 0 && !UtilsA.hasIceNear(p)) {
                                                                        this.getAnticheat().failure(this, p, "[2] - Player Moved Too Fast (B)", "(Type: B)");
                                                                    }
                                                                    if (speed > 0.7 && !UtilsA.isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4 && blockLoc.getBlock().getType() != Material.ICE && to.getY() != from.getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR && !UtilNewVelocity.didTakeVel(p) && !UtilVelocity.didTakeVelocity(p) && !UtilsA.hasPistonNear(p) && from.getBlock().getType() != Material.PISTON_MOVING_PIECE && from.getBlock().getType() != Material.PISTON_BASE && from.getBlock().getType() != Material.PISTON_STICKY_BASE && !UtilsA.isNearPistion(p)) {
                                                                        this.getAnticheat().failure(this, p, "[4] - Player Moved Too Fast (B)", "(Type: B)");
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception ex2) {}
        });
    }
}
