// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.user;

import java.util.HashMap;
import com.google.common.collect.Lists;
import org.bukkit.inventory.EquipmentSlot;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.Location;

public class UserData
{
    public int airTicks;
    public int groundTicks;
    public int iceTicks;
    public Location lastAntiKB;
    public Player player;
    public boolean onGround;
    public boolean inLiquid;
    public boolean onStairSlab;
    public boolean onIce;
    public boolean onClimbable;
    public boolean underBlock;
    public int liquidTicks;
    public int blockTicks;
    public long lastVelocityTaken;
    public long lastAttack;
    private long lastTp;
    private long lastExplode;
    private long actualTime;
    public LivingEntity lastHitEntity;
    public final List<Float> patterns;
    public float lastRange;
    public int speedThreshold;
    public double highestFly;
    private boolean alerts;
    private double fallDistance;
    private int aboveBlockTicks;
    private int waterTicks;
    private long lastKnocked;
    private long LastBlockPlacedTicks;
    private boolean LastBlockPlaced_GroundSpoof;
    private boolean ShouldSetBack;
    private int setBackTicks;
    private long LastVelMS;
    private boolean DidTakeVelocity;
    private long lastDelayedPacket;
    private long lastPlayerPacket;
    private Location setbackLocation;
    private double GoingUp_Blocks;
    private double LastY_Gravity;
    private int Gravity_VL;
    private int AntiCactus_VL;
    private double lastVelocityFlyY;
    private double lastKillauraPitch;
    private double lastKillauraYaw;
    private long lastPacket;
    private long lastAimTime;
    private long Speed_Ticks;
    private boolean Speed_TicksSet;
    private boolean isNearIce;
    private long isNearIceTicks;
    private long LastVelUpdate;
    private boolean LastVelUpdateBoolean;
    private double lastKillauraYawDif;
    private long lastPacketTimer;
    private long LastTimeTimer;
    private int LastPACKETSTimer;
    private long WebFloatMS;
    private boolean WebFloatMS_Set;
    private int WebFloat_BlockCount;
    private long AboveSpeedTicks;
    private boolean AboveSpeedSet;
    private long HalfBlocks_MS;
    private boolean HalfBlocks_MS_Set;
    private boolean Speed_C_2_Set;
    private long Speed_C_2_MS;
    private long GlideTicks;
    private long Speed_PistonExpand_MS;
    private boolean Speed_PistonExpand_Set;
    private long BlockAbove;
    private boolean BlockAbove_Set;
    private long Speed_YPORT_MS;
    private boolean Speed_YPORT_Set;
    private long Speed_YPort2_MS;
    private boolean Speed_YPort2_Set;
    private long speedGroundReset;
    private int slimeTicks;
    private int criticalsVerbose;
    private int flyHoverVerbose;
    private int flyVelocityVerbose;
    private int GroundSpoofVL;
    private int killauraAVerbose;
    private int Speed2Verbose;
    private int Speed_OnGround_Verbose;
    private int TimerVerbose;
    private int SpeedAC2_Verbose;
    private int SpeedC_Verbose;
    private int Speed_C_3_Verbose;
    private int Speed_YPORT_Verbose;
    private int Speed_YPort2_Verbose;
    private int NEWSpeed_Verbose;
    private int speedAVerbose;
    private int Speed_C3_Verbose;
    private int Jesus_Verbose;
    private long lastSpeedEClear;
    private long lastSpeedCClear;
    private double speedEDistanceMade;
    private long joinTime;
    private long lastStoneBreak;
    private long lastPickup;
    private ItemStack itemPickup;
    private long lastBlockPlace;
    private final Map<EquipmentSlot, Long> breakTime;
    private boolean blockInteractKick;
    
    public long getActualTime() {
        return this.actualTime;
    }
    
    public void setActualTime(final long actualTime) {
        this.actualTime = actualTime;
    }
    
    public long getLastExplode() {
        return this.lastExplode;
    }
    
    public void setLastExplode(final long lastExplode) {
        this.lastExplode = lastExplode;
    }
    
    public double getHighestFly() {
        return this.highestFly;
    }
    
    public void setHighestFly(final double highestFly) {
        this.highestFly = highestFly;
    }
    
    public UserData(final Player player) {
        this.airTicks = 0;
        this.groundTicks = 0;
        this.iceTicks = 0;
        this.lastAntiKB = null;
        this.lastTp = 0L;
        this.lastExplode = 0L;
        this.actualTime = 0L;
        this.patterns = (List<Float>)Lists.newArrayList();
        this.highestFly = 0.0;
        this.alerts = false;
        this.fallDistance = 0.0;
        this.aboveBlockTicks = 0;
        this.waterTicks = 0;
        this.lastKnocked = 0L;
        this.LastBlockPlacedTicks = 0L;
        this.LastBlockPlaced_GroundSpoof = false;
        this.ShouldSetBack = false;
        this.setBackTicks = 0;
        this.LastVelMS = 0L;
        this.DidTakeVelocity = false;
        this.lastVelocityFlyY = 0.0;
        this.lastKillauraPitch = 0.0;
        this.lastKillauraYaw = 0.0;
        this.lastPacket = 0L;
        this.lastAimTime = System.currentTimeMillis();
        this.Speed_Ticks = 0L;
        this.Speed_TicksSet = false;
        this.isNearIce = false;
        this.isNearIceTicks = 0L;
        this.LastVelUpdate = 0L;
        this.LastVelUpdateBoolean = false;
        this.lastKillauraYawDif = 0.0;
        this.lastPacketTimer = 0L;
        this.LastTimeTimer = 0L;
        this.LastPACKETSTimer = 0;
        this.WebFloatMS = 0L;
        this.WebFloatMS_Set = false;
        this.WebFloat_BlockCount = 0;
        this.AboveSpeedTicks = 0L;
        this.AboveSpeedSet = false;
        this.HalfBlocks_MS = 0L;
        this.HalfBlocks_MS_Set = false;
        this.Speed_C_2_Set = false;
        this.Speed_C_2_MS = 0L;
        this.GlideTicks = 0L;
        this.Speed_PistonExpand_MS = 0L;
        this.Speed_PistonExpand_Set = false;
        this.BlockAbove = 0L;
        this.BlockAbove_Set = false;
        this.Speed_YPORT_MS = 0L;
        this.Speed_YPORT_Set = false;
        this.Speed_YPort2_MS = 0L;
        this.Speed_YPort2_Set = false;
        this.speedGroundReset = 0L;
        this.slimeTicks = 0;
        this.criticalsVerbose = 0;
        this.flyHoverVerbose = 0;
        this.flyVelocityVerbose = 0;
        this.GroundSpoofVL = 0;
        this.killauraAVerbose = 0;
        this.Speed2Verbose = 0;
        this.Speed_OnGround_Verbose = 0;
        this.TimerVerbose = 0;
        this.SpeedAC2_Verbose = 0;
        this.SpeedC_Verbose = 0;
        this.Speed_C_3_Verbose = 0;
        this.Speed_YPORT_Verbose = 0;
        this.Speed_YPort2_Verbose = 0;
        this.NEWSpeed_Verbose = 0;
        this.speedAVerbose = 0;
        this.Speed_C3_Verbose = 0;
        this.Jesus_Verbose = 0;
        this.lastSpeedCClear = 0L;
        this.breakTime = new HashMap<EquipmentSlot, Long>();
        this.blockInteractKick = false;
        this.player = player;
        this.joinTime = System.currentTimeMillis();
    }
    
    public long getLastBlockPlace() {
        return this.lastBlockPlace;
    }
    
    public void setLastBlockPlace(final long lastBlockPlace) {
        this.lastBlockPlace = lastBlockPlace;
    }
    
    public long getLastPickup() {
        return this.lastPickup;
    }
    
    public void setLastPickup(final long lastPickup) {
        this.lastPickup = lastPickup;
    }
    
    public ItemStack getItemPickup() {
        return this.itemPickup;
    }
    
    public void setItemPickup(final ItemStack itemPickup) {
        this.itemPickup = itemPickup;
    }
    
    public void breakArmor(final EquipmentSlot slot) {
        this.breakTime.put(slot, System.currentTimeMillis());
    }
    
    public long getBreakTime(final EquipmentSlot slot) {
        return this.breakTime.getOrDefault(slot, -1L);
    }
    
    public long getLastStoneBreak() {
        return this.lastStoneBreak;
    }
    
    public void setLastStoneBreak(final long lastStoneBreak) {
        this.lastStoneBreak = lastStoneBreak;
    }
    
    public boolean isBlockInteractKick() {
        return this.blockInteractKick;
    }
    
    public void setBlockInteractKick(final boolean blockInteractKick) {
        this.blockInteractKick = blockInteractKick;
    }
    
    public long getJoinTime() {
        return this.joinTime;
    }
    
    public long getLastSpeedEClear() {
        return this.lastSpeedEClear;
    }
    
    public void setLastSpeedEClear(final long lastSpeedEClear) {
        this.lastSpeedEClear = lastSpeedEClear;
    }
    
    public long getLastSpeedCClear() {
        return this.lastSpeedCClear;
    }
    
    public void setLastSpeedCClear(final long lastSpeedCClear) {
        this.lastSpeedCClear = lastSpeedCClear;
    }
    
    public double getSpeedEDistanceMade() {
        return this.speedEDistanceMade;
    }
    
    public void setSpeedEDistanceMade(final double speedEDistanceMade) {
        this.speedEDistanceMade = speedEDistanceMade;
    }
    
    public int getJesus_Verbose() {
        return this.Jesus_Verbose;
    }
    
    public void setJoinTime(final long joinTime) {
        this.joinTime = joinTime;
    }
    
    public void setJesus_Verbose(final int jesus_Verbose) {
        this.Jesus_Verbose = jesus_Verbose;
    }
    
    public int getSpeed_C3_Verbose() {
        return this.Speed_C3_Verbose;
    }
    
    public void setSpeed_C3_Verbose(final int speed_C3_Verbose) {
        this.Speed_C3_Verbose = speed_C3_Verbose;
    }
    
    public int getNEWSpeed_Verbose() {
        return this.NEWSpeed_Verbose;
    }
    
    public void setNEWSpeed_Verbose(final int NEWSpeed_Verbose) {
        this.NEWSpeed_Verbose = NEWSpeed_Verbose;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public void setPlayer(final Player player) {
        this.player = player;
    }
    
    public boolean isAlerts() {
        return this.alerts;
    }
    
    public void setAlerts(final boolean alerts) {
        this.alerts = alerts;
    }
    
    public double getFallDistance() {
        return this.fallDistance;
    }
    
    public void setFallDistance(final double fallDistance) {
        this.fallDistance = fallDistance;
    }
    
    public int getIceTicks() {
        return this.iceTicks;
    }
    
    public void setIceTicks(final int iceTicks) {
        this.iceTicks = iceTicks;
    }
    
    public int getAboveBlockTicks() {
        return this.aboveBlockTicks;
    }
    
    public void setAboveBlockTicks(final int aboveBlockTicks) {
        this.aboveBlockTicks = aboveBlockTicks;
    }
    
    public int getWaterTicks() {
        return this.waterTicks;
    }
    
    public void setWaterTicks(final int waterTicks) {
        this.waterTicks = waterTicks;
    }
    
    public int getSpeedAVerbose() {
        return this.speedAVerbose;
    }
    
    public void setSpeedAVerbose(final int speedAVerbose) {
        this.speedAVerbose = speedAVerbose;
    }
    
    public int getSlimeTicks() {
        return this.slimeTicks;
    }
    
    public void setSlimeTicks(final int slimeTicks) {
        this.slimeTicks = slimeTicks;
    }
    
    public long getSpeedGroundReset() {
        return this.speedGroundReset;
    }
    
    public void setSpeedGroundReset(final long speedGroundReset) {
        this.speedGroundReset = speedGroundReset;
    }
    
    public int getCriticalsVerbose() {
        return this.criticalsVerbose;
    }
    
    public void setCriticalsVerbose(final int criticalsVerbose) {
        this.criticalsVerbose = criticalsVerbose;
    }
    
    public double getLastKillauraYawDif() {
        return this.lastKillauraYawDif;
    }
    
    public void setLastKillauraYawDif(final double lastKillauraYawDif) {
        this.lastKillauraYawDif = lastKillauraYawDif;
    }
    
    public double getLastKillauraPitch() {
        return this.lastKillauraPitch;
    }
    
    public void setLastKillauraPitch(final double lastKillauraPitch) {
        this.lastKillauraPitch = lastKillauraPitch;
    }
    
    public double getLastKillauraYaw() {
        return this.lastKillauraYaw;
    }
    
    public void setLastKillauraYaw(final double lastKillauraYaw) {
        this.lastKillauraYaw = lastKillauraYaw;
    }
    
    public int getKillauraAVerbose() {
        return this.killauraAVerbose;
    }
    
    public void setKillauraAVerbose(final int killauraAVerbose) {
        this.killauraAVerbose = killauraAVerbose;
    }
    
    public long getLastPacket() {
        return this.lastPacket;
    }
    
    public void setLastPacket(final long lastPacket) {
        this.lastPacket = lastPacket;
    }
    
    public long getLastAimTime() {
        return this.lastAimTime;
    }
    
    public void setLastAimTime(final long lastAimTime) {
        this.lastAimTime = lastAimTime;
    }
    
    public long getLastBlockPlacedTicks() {
        return this.LastBlockPlacedTicks;
    }
    
    public void setLastBlockPlacedTicks(final long lastBlockPlacedTicks) {
        this.LastBlockPlacedTicks = lastBlockPlacedTicks;
    }
    
    public boolean isLastBlockPlaced_GroundSpoof() {
        return this.LastBlockPlaced_GroundSpoof;
    }
    
    public void setLastBlockPlaced_GroundSpoof(final boolean lastBlockPlaced_GroundSpoof) {
        this.LastBlockPlaced_GroundSpoof = lastBlockPlaced_GroundSpoof;
    }
    
    public int getAirTicks() {
        return this.airTicks;
    }
    
    public void setAirTicks(final int airTicks) {
        this.airTicks = airTicks;
    }
    
    public int getGroundTicks() {
        return this.groundTicks;
    }
    
    public void setGroundTicks(final int groundTicks) {
        this.groundTicks = groundTicks;
    }
    
    public int getFlyHoverVerbose() {
        return this.flyHoverVerbose;
    }
    
    public void setFlyHoverVerbose(final int flyHoverVerbose) {
        this.flyHoverVerbose = flyHoverVerbose;
    }
    
    public int getGroundSpoofVL() {
        return this.GroundSpoofVL;
    }
    
    public void setGroundSpoofVL(final int groundSpoofVL) {
        this.GroundSpoofVL = groundSpoofVL;
    }
    
    public boolean isShouldSetBack() {
        return this.ShouldSetBack;
    }
    
    public void setShouldSetBack(final boolean shouldSetBack) {
        this.ShouldSetBack = shouldSetBack;
    }
    
    public double getLastVelocityFlyY() {
        return this.lastVelocityFlyY;
    }
    
    public void setLastVelocityFlyY(final double lastVelocityFlyY) {
        this.lastVelocityFlyY = lastVelocityFlyY;
    }
    
    public int getSetBackTicks() {
        return this.setBackTicks;
    }
    
    public void setSetBackTicks(final int setBackTicks) {
        this.setBackTicks = setBackTicks;
    }
    
    public long getLastVelMS() {
        return this.LastVelMS;
    }
    
    public void setLastVelMS(final long lastVelMS) {
        this.LastVelMS = lastVelMS;
    }
    
    public boolean isDidTakeVelocity() {
        return this.DidTakeVelocity;
    }
    
    public void setDidTakeVelocity(final boolean didTakeVelocity) {
        this.DidTakeVelocity = didTakeVelocity;
    }
    
    public int getFlyVelocityVerbose() {
        return this.flyVelocityVerbose;
    }
    
    public void setFlyVelocityVerbose(final int flyVelocityVerbose) {
        this.flyVelocityVerbose = flyVelocityVerbose;
    }
    
    public long getLastDelayedPacket() {
        return this.lastDelayedPacket;
    }
    
    public void setLastDelayedPacket(final long l) {
        this.lastDelayedPacket = l;
    }
    
    public long getLastPlayerPacketDiff() {
        return System.currentTimeMillis() - this.getLastPlayerPacket();
    }
    
    public long getLastPlayerPacket() {
        return this.lastPlayerPacket;
    }
    
    public void setLastPlayerPacket(final long l) {
        this.lastPlayerPacket = l;
    }
    
    public Location getSetbackLocation() {
        return this.setbackLocation;
    }
    
    public void setSetbackLocation(final Location setbackLocation) {
        this.setbackLocation = setbackLocation;
    }
    
    public double getGoingUp_Blocks() {
        return this.GoingUp_Blocks;
    }
    
    public void setGoingUp_Blocks(final double goingUp_Blocks) {
        this.GoingUp_Blocks = goingUp_Blocks;
    }
    
    public double getLastY_Gravity() {
        return this.LastY_Gravity;
    }
    
    public void setLastY_Gravity(final double lastY_Gravity) {
        this.LastY_Gravity = lastY_Gravity;
    }
    
    public int getGravity_VL() {
        return this.Gravity_VL;
    }
    
    public void setGravity_VL(final int gravity_VL) {
        this.Gravity_VL = gravity_VL;
    }
    
    public int getAntiCactus_VL() {
        return this.AntiCactus_VL;
    }
    
    public void setAntiCactus_VL(final int antiCactus_VL) {
        this.AntiCactus_VL = antiCactus_VL;
    }
    
    public long getSpeed_Ticks() {
        return this.Speed_Ticks;
    }
    
    public boolean isSpeed_TicksSet() {
        return this.Speed_TicksSet;
    }
    
    public void setSpeed_TicksSet(final boolean speed_TicksSet) {
        this.Speed_TicksSet = speed_TicksSet;
    }
    
    public boolean isNearIce() {
        return this.isNearIce;
    }
    
    public void setNearIce(final boolean nearIce) {
        this.isNearIce = nearIce;
    }
    
    public long getIsNearIceTicks() {
        return this.isNearIceTicks;
    }
    
    public void setIsNearIceTicks(final long isNearIceTicks) {
        this.isNearIceTicks = isNearIceTicks;
    }
    
    public long getLastVelUpdate() {
        return this.LastVelUpdate;
    }
    
    public void setLastVelUpdate(final long lastVelUpdate) {
        this.LastVelUpdate = lastVelUpdate;
    }
    
    public boolean isLastVelUpdateBoolean() {
        return this.LastVelUpdateBoolean;
    }
    
    public void setLastVelUpdateBoolean(final boolean lastVelUpdateBoolean) {
        this.LastVelUpdateBoolean = lastVelUpdateBoolean;
    }
    
    public int getSpeed2Verbose() {
        return this.Speed2Verbose;
    }
    
    public void setSpeed2Verbose(final int speed2Verbose) {
        this.Speed2Verbose = speed2Verbose;
    }
    
    public int getSpeed_OnGround_Verbose() {
        return this.Speed_OnGround_Verbose;
    }
    
    public void setSpeed_OnGround_Verbose(final int speed_OnGround_Verbose) {
        this.Speed_OnGround_Verbose = speed_OnGround_Verbose;
    }
    
    public long getLastPacketTimer() {
        return this.lastPacketTimer;
    }
    
    public void setLastPacketTimer(final long lastPacketTimer) {
        this.lastPacketTimer = lastPacketTimer;
    }
    
    public long getLastTimeTimer() {
        return this.LastTimeTimer;
    }
    
    public void setLastTimeTimer(final long lastTimeTimer) {
        this.LastTimeTimer = lastTimeTimer;
    }
    
    public int getTimerVerbose() {
        return this.TimerVerbose;
    }
    
    public void setTimerVerbose(final int timerVerbose) {
        this.TimerVerbose = timerVerbose;
    }
    
    public int getLastPACKETSTimer() {
        return this.LastPACKETSTimer;
    }
    
    public void setLastPACKETSTimer(final int lastPACKETSTimer) {
        this.LastPACKETSTimer = lastPACKETSTimer;
    }
    
    public long getWebFloatMS() {
        return this.WebFloatMS;
    }
    
    public void setWebFloatMS(final long webFloatMS) {
        this.WebFloatMS = webFloatMS;
    }
    
    public boolean isWebFloatMS_Set() {
        return this.WebFloatMS_Set;
    }
    
    public void setWebFloatMS_Set(final boolean webFloatMS_Set) {
        this.WebFloatMS_Set = webFloatMS_Set;
    }
    
    public int getWebFloat_BlockCount() {
        return this.WebFloat_BlockCount;
    }
    
    public void setWebFloat_BlockCount(final int webFloat_BlockCount) {
        this.WebFloat_BlockCount = webFloat_BlockCount;
    }
    
    public long getAboveSpeedTicks() {
        return this.AboveSpeedTicks;
    }
    
    public void setAboveSpeedTicks(final long aboveSpeedTicks) {
        this.AboveSpeedTicks = aboveSpeedTicks;
    }
    
    public boolean isAboveSpeedSet() {
        return this.AboveSpeedSet;
    }
    
    public void setAboveSpeedSet(final boolean aboveSpeedSet) {
        this.AboveSpeedSet = aboveSpeedSet;
    }
    
    public int getSpeedAC2_Verbose() {
        return this.SpeedAC2_Verbose;
    }
    
    public void setSpeedAC2_Verbose(final int speedAC2_Verbose) {
        this.SpeedAC2_Verbose = speedAC2_Verbose;
    }
    
    public long getHalfBlocks_MS() {
        return this.HalfBlocks_MS;
    }
    
    public void setHalfBlocks_MS(final long halfBlocks_MS) {
        this.HalfBlocks_MS = halfBlocks_MS;
    }
    
    public boolean isHalfBlocks_MS_Set() {
        return this.HalfBlocks_MS_Set;
    }
    
    public void setHalfBlocks_MS_Set(final boolean halfBlocks_MS_Set) {
        this.HalfBlocks_MS_Set = halfBlocks_MS_Set;
    }
    
    public boolean isSpeed_C_2_Set() {
        return this.Speed_C_2_Set;
    }
    
    public void setSpeed_C_2_Set(final boolean speed_C_2_Set) {
        this.Speed_C_2_Set = speed_C_2_Set;
    }
    
    public long getSpeed_C_2_MS() {
        return this.Speed_C_2_MS;
    }
    
    public void setSpeed_C_2_MS(final long speed_C_2_MS) {
        this.Speed_C_2_MS = speed_C_2_MS;
    }
    
    public int getSpeedC_Verbose() {
        return this.SpeedC_Verbose;
    }
    
    public void setSpeedC_Verbose(final int speedC_Verbose) {
        this.SpeedC_Verbose = speedC_Verbose;
    }
    
    public long getGlideTicks() {
        return this.GlideTicks;
    }
    
    public void setGlideTicks(final long glideTicks) {
        this.GlideTicks = glideTicks;
    }
    
    public int getSpeed_C_3_Verbose() {
        return this.Speed_C_3_Verbose;
    }
    
    public void setSpeed_C_3_Verbose(final int speed_C_3_Verbose) {
        this.Speed_C_3_Verbose = speed_C_3_Verbose;
    }
    
    public long getSpeed_PistonExpand_MS() {
        return this.Speed_PistonExpand_MS;
    }
    
    public void setSpeed_PistonExpand_MS(final long speed_PistonExpand_MS) {
        this.Speed_PistonExpand_MS = speed_PistonExpand_MS;
    }
    
    public boolean isSpeed_PistonExpand_Set() {
        return this.Speed_PistonExpand_Set;
    }
    
    public void setSpeed_PistonExpand_Set(final boolean speed_PistonExpand_Set) {
        this.Speed_PistonExpand_Set = speed_PistonExpand_Set;
    }
    
    public long getBlockAbove() {
        return this.BlockAbove;
    }
    
    public void setBlockAbove(final long blockAbove) {
        this.BlockAbove = blockAbove;
    }
    
    public boolean isBlockAbove_Set() {
        return this.BlockAbove_Set;
    }
    
    public void setBlockAbove_Set(final boolean blockAbove_Set) {
        this.BlockAbove_Set = blockAbove_Set;
    }
    
    public int getSpeed_YPORT_Verbose() {
        return this.Speed_YPORT_Verbose;
    }
    
    public void setSpeed_YPORT_Verbose(final int speed_YPORT_Verbose) {
        this.Speed_YPORT_Verbose = speed_YPORT_Verbose;
    }
    
    public long getSpeed_YPORT_MS() {
        return this.Speed_YPORT_MS;
    }
    
    public void setSpeed_YPORT_MS(final long speed_YPORT_MS) {
        this.Speed_YPORT_MS = speed_YPORT_MS;
    }
    
    public boolean isSpeed_YPORT_Set() {
        return this.Speed_YPORT_Set;
    }
    
    public void setSpeed_YPORT_Set(final boolean speed_YPORT_Set) {
        this.Speed_YPORT_Set = speed_YPORT_Set;
    }
    
    public long getSpeed_YPort2_MS() {
        return this.Speed_YPort2_MS;
    }
    
    public void setSpeed_YPort2_MS(final long speed_YPort2_MS) {
        this.Speed_YPort2_MS = speed_YPort2_MS;
    }
    
    public boolean isSpeed_YPort2_Set() {
        return this.Speed_YPort2_Set;
    }
    
    public void setSpeed_YPort2_Set(final boolean speed_YPort2_Set) {
        this.Speed_YPort2_Set = speed_YPort2_Set;
    }
    
    public int getSpeed_YPort2_Verbose() {
        return this.Speed_YPort2_Verbose;
    }
    
    public void setSpeed_YPort2_Verbose(final int speed_YPort2_Verbose) {
        this.Speed_YPort2_Verbose = speed_YPort2_Verbose;
    }
    
    public void setLastAntiKB(final Location loc) {
        this.lastAntiKB = loc;
    }
    
    public Location getLastAntiKB() {
        return this.lastAntiKB;
    }
    
    public void setLastKnocked(final long time) {
        this.lastKnocked = time;
    }
    
    public long getLastKnocked() {
        return this.lastKnocked;
    }
    
    public long getLastTP() {
        return this.lastTp;
    }
    
    public void setLastTp(final long time) {
        this.lastTp = time;
    }
}
