// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import java.util.Iterator;
import xyz.natalczx.cerberus.user.UserData;
import java.util.AbstractMap;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.helper.UtilTime;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.GameMode;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import xyz.natalczx.cerberus.check.Check;

public class SpeedD extends Check implements Listener
{
    public static final Map<UUID, Long> lastHit;
    private final Map<UUID, Map.Entry<Integer, Long>> speedTicks;
    private final Map<UUID, Map.Entry<Integer, Long>> tooFastTicks;
    private final Map<UUID, Double> velocity;
    
    public SpeedD(final CerberusAntiCheat AntiCheat) {
        super("SpeedD", "Speed (Type: D)", AntiCheat);
        this.speedTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.tooFastTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.velocity = new HashMap<UUID, Double>();
        this.setViolationResetTime(TimeUnit.MINUTES.toMillis(4L));
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onHit(final EntityDamageByEntityEvent e) {
        Player player;
        this.async(() -> {
            if (e.getEntity() instanceof Player) {
                player = (Player)e.getEntity();
                SpeedD.lastHit.put(player.getUniqueId(), System.currentTimeMillis());
            }
        });
    }
    
    public boolean isOnIce(final Player player) {
        final Location a = player.getLocation().clone();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals((Object)Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals((Object)Material.ICE);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLog(final PlayerQuitEvent e) {
        SpeedD.lastHit.remove(e.getPlayer().getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(final RealMoveEvent e) {
        final Player player;
        UserData data;
        Location from;
        Location to;
        long lastHitDiff;
        int Count;
        long Time;
        int TooFastCount;
        double percent;
        double OffsetXZ;
        double LimitXZ;
        double LimitXZ2;
        Location b;
        Location below;
        float speed;
        double LimitXZ3;
        final Iterator<PotionEffect> iterator;
        PotionEffect effect;
        int n;
        this.async(() -> {
            player = e.getPlayer();
            if (!(!this.getAnticheat().isEnabled())) {
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
                            if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                if (System.currentTimeMillis() - data.getLastKnocked() >= 1000L) {
                                    from = e.getFrom();
                                    to = e.getTo();
                                    if (!(!from.getWorld().isChunkLoaded(from.getBlockX() / 16, from.getBlockZ() / 16))) {
                                        if (!player.getAllowFlight() && player.getVehicle() == null && !player.getGameMode().equals((Object)GameMode.CREATIVE) && !UtilsB.isNearIce(player) && !UtilsA.wasOnSlime(player) && !UtilsB.isNearSlime(player) && player.getVelocity().length() + 0.1 >= this.velocity.getOrDefault(player.getUniqueId(), -1.0) && (!this.getAnticheat().getVelocityManager().getLastVelocity().containsKey(player.getUniqueId()) || player.hasPotionEffect(PotionEffectType.POISON) || player.hasPotionEffect(PotionEffectType.WITHER) || player.getFireTicks() != 0)) {
                                            lastHitDiff = (SpeedD.lastHit.containsKey(player.getUniqueId()) ? (SpeedD.lastHit.get(player.getUniqueId()) - System.currentTimeMillis()) : 2001L);
                                            Count = 0;
                                            Time = UtilTime.nowlong();
                                            if (this.speedTicks.containsKey(player.getUniqueId())) {
                                                Count = this.speedTicks.get(player.getUniqueId()).getKey();
                                                Time = this.speedTicks.get(player.getUniqueId()).getValue();
                                            }
                                            TooFastCount = 0;
                                            percent = 0.0;
                                            if (this.tooFastTicks.containsKey(player.getUniqueId())) {
                                                OffsetXZ = UtilsB.offset(UtilsB.getHorizontalVector(from.toVector()), UtilsB.getHorizontalVector(to.toVector()));
                                                LimitXZ = 0.0;
                                                if (UtilsB.isOnGround(player) && player.getVehicle() == null) {
                                                    LimitXZ2 = 0.34;
                                                }
                                                else {
                                                    LimitXZ2 = 0.39;
                                                }
                                                if (lastHitDiff < 800L) {
                                                    ++LimitXZ2;
                                                }
                                                else if (lastHitDiff < 1600L) {
                                                    LimitXZ2 += 0.4;
                                                }
                                                else if (lastHitDiff < 2000L) {
                                                    LimitXZ2 += 0.1;
                                                }
                                                if (UtilCheat.slabsNear(from)) {
                                                    LimitXZ2 += 0.05;
                                                }
                                                b = UtilsB.getEyeLocation(player);
                                                b.add(0.0, 1.0, 0.0);
                                                if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                                                    LimitXZ2 = 0.69;
                                                }
                                                below = from.add(0.0, -1.0, 0.0);
                                                if (UtilCheat.isStair(below.getBlock())) {
                                                    LimitXZ2 += 0.6;
                                                }
                                                if (this.isOnIce(player)) {
                                                    if (b.getBlock().getType() != Material.AIR && !UtilCheat.canStandWithin(b.getBlock())) {
                                                        LimitXZ2 = 1.0;
                                                    }
                                                    else {
                                                        LimitXZ2 = 0.75;
                                                    }
                                                }
                                                speed = player.getWalkSpeed();
                                                LimitXZ3 = LimitXZ2 + ((speed > 0.2f) ? (speed * 10.0f * 0.33f) : 0.0f);
                                                player.getActivePotionEffects().iterator();
                                                while (iterator.hasNext()) {
                                                    effect = iterator.next();
                                                    if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                                                        if (player.isOnGround()) {
                                                            LimitXZ3 += 0.061 * (effect.getAmplifier() + 1);
                                                        }
                                                        else {
                                                            LimitXZ3 += 0.031 * (effect.getAmplifier() + 1);
                                                        }
                                                    }
                                                }
                                                if (OffsetXZ > LimitXZ3 && !UtilTime.elapsed(this.tooFastTicks.get(player.getUniqueId()).getValue(), 150L)) {
                                                    percent = (OffsetXZ - LimitXZ3) * 100.0;
                                                    TooFastCount = this.tooFastTicks.get(player.getUniqueId()).getKey() + 3;
                                                }
                                                else {
                                                    if (TooFastCount > -150) {
                                                        n = TooFastCount--;
                                                    }
                                                    else {
                                                        n = -150;
                                                    }
                                                    TooFastCount = n;
                                                }
                                            }
                                            if (TooFastCount >= 11) {
                                                TooFastCount = 0;
                                                ++Count;
                                            }
                                            if (this.speedTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 30000L)) {
                                                Count = 0;
                                                Time = UtilTime.nowlong();
                                            }
                                            if (Count >= 3) {
                                                Count = 0;
                                                this.getAnticheat().failure(this, player, Math.round(percent) + "% faster than normal", "(Type: D)");
                                            }
                                            if (!player.isOnGround()) {
                                                this.velocity.put(player.getUniqueId(), player.getVelocity().length());
                                            }
                                            else {
                                                this.velocity.put(player.getUniqueId(), -1.0);
                                            }
                                            this.tooFastTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(TooFastCount, System.currentTimeMillis()));
                                            this.speedTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
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
    
    static {
        lastHit = new HashMap<UUID, Long>();
    }
}
