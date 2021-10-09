// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import org.bukkit.entity.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import xyz.natalczx.cerberus.events.FlyMoveEvent;
import org.bukkit.entity.EntityType;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.check.FalseCheck;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.natalczx.cerberus.check.movement.SlimeJump;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.HashMap;
import xyz.natalczx.cerberus.check.Check;
import xyz.natalczx.cerberus.api.FixedLoc;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class PlayerMove implements Listener
{
    public static final Map<UUID, Double> falldistance;
    private final Set<UUID> inair;
    private final Map<UUID, Long> lastjump;
    private final Map<UUID, Integer> pverbose;
    private final ConcurrentHashMap<UUID, FixedLoc> pjumped;
    public static final ConcurrentHashMap<UUID, Long> pstarted;
    public static final Set<UUID> slimed;
    private final Check check;
    
    public PlayerMove() {
        this.inair = (Set<UUID>)ConcurrentHashMap.newKeySet();
        this.lastjump = new HashMap<UUID, Long>();
        this.pverbose = new HashMap<UUID, Integer>();
        this.pjumped = new ConcurrentHashMap<UUID, FixedLoc>();
        this.check = new SlimeJump(CerberusAntiCheat.getInstance());
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            PlayerMove.falldistance.remove(uuid);
            PlayerMove.slimed.remove(uuid);
            this.pverbose.remove(uuid);
            this.inair.remove(uuid);
            this.pverbose.remove(uuid);
            this.lastjump.remove(uuid);
            this.pjumped.remove(uuid);
            PlayerMove.pstarted.remove(uuid);
        });
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final Location loc = p.getLocation();
        this.pjumped.put(p.getUniqueId(), new FixedLoc(loc.getX(), loc.getY(), loc.getZ()));
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent e) {
        final Player p;
        final UserData data;
        Location from;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            p = e.getPlayer();
            data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
            if (data != null) {
                from = e.getBlock().getLocation().clone().add(0.0, 1.0, 0.0);
                if (!this.pjumped.containsKey(p.getUniqueId()) && UtilsB.isGround(from) && !UtilsB.isGround(p.getLocation().clone())) {
                    this.pjumped.put(p.getUniqueId(), new FixedLoc(from.getX(), from.getY(), from.getZ()));
                    PlayerMove.pstarted.put(p.getUniqueId(), System.currentTimeMillis());
                }
            }
        });
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void moveTimer(final PlayerMoveEvent e) {
        final long time = System.currentTimeMillis();
        final UserData data;
        final long n;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            data = CerberusAntiCheat.getInstance().getDataManager().getData(e.getPlayer());
            if (data != null) {
                data.setActualTime(n);
                if (e.getPlayer().getAllowFlight()) {
                    data.setLastKnocked(n);
                }
            }
        });
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final Location from = e.getFrom();
        final Location to = e.getTo();
        if (from.getX() != to.getX() || from.getZ() != to.getZ() || from.getY() != to.getY()) {
            int amplifier = 0;
            int amplifiers = 0;
            for (final PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().getName().equals(PotionEffectType.JUMP.getName())) {
                    amplifier = effect.getAmplifier() + 1;
                }
                else {
                    if (!effect.getType().getName().equals(PotionEffectType.SPEED.getName())) {
                        continue;
                    }
                    amplifiers = effect.getAmplifier() + 1;
                }
            }
            final int finalAmplifier = amplifier;
            final int finalAmplifiers = amplifiers;
            final boolean veh = p.isInsideVehicle();
            final Location location;
            final Location location2;
            final boolean b2;
            boolean arounding;
            final Iterator<Block> iterator2;
            Block b;
            final Player player;
            final boolean isgto;
            Location tloc;
            ArrayList<Block> blocks;
            int a;
            final Iterator<Block> iterator3;
            Block block;
            Material mat;
            UserData data;
            int m;
            final int amplifier2;
            int i;
            final Entity[] array;
            int length;
            int j = 0;
            Entity entity;
            Location eloc;
            FixedLoc jd;
            final int amplifiers2;
            CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
                if ((location.getX() != location2.getX() || location.getZ() != location2.getZ()) && !b2) {
                    Bukkit.getServer().getPluginManager().callEvent((Event)new RealMoveEvent(e.getPlayer(), location, location2));
                }
                arounding = false;
                UtilsB.getBlocksAroundCenter(location2, 1).iterator();
                while (iterator2.hasNext()) {
                    b = iterator2.next();
                    if (b.getType() != Material.AIR) {
                        arounding = true;
                    }
                }
                if (location2.getY() < location.getY()) {
                    if (!this.inair.contains(player.getUniqueId())) {
                        this.inair.add(player.getUniqueId());
                        PlayerMove.falldistance.put(player.getUniqueId(), location.getY());
                    }
                    else if (location.getY() > PlayerMove.falldistance.get(player.getUniqueId())) {
                        PlayerMove.falldistance.put(player.getUniqueId(), location.getY());
                    }
                }
                isgto = UtilsB.isGround(location2);
                if (this.pjumped.containsKey(player.getUniqueId()) && isgto) {
                    this.pjumped.remove(player.getUniqueId());
                    PlayerMove.pstarted.remove(player.getUniqueId());
                }
                if (!(!FalseCheck.fly(player, location, location2))) {
                    if (!PlayerMove.slimed.contains(player.getUniqueId())) {
                        tloc = location.clone();
                        while (tloc.getY() > 0.0) {
                            blocks = UtilsB.getSurroundingB(tloc.getBlock());
                            blocks.add(tloc.getBlock());
                            a = 0;
                            blocks.iterator();
                            while (iterator3.hasNext()) {
                                block = iterator3.next();
                                mat = block.getType();
                                if (mat.isSolid()) {
                                    if (mat == Material.SLIME_BLOCK) {
                                        PlayerMove.slimed.add(player.getUniqueId());
                                        return;
                                    }
                                    else {
                                        ++a;
                                    }
                                }
                            }
                            if (a > 8) {
                                break;
                            }
                            else {
                                tloc.add(0.0, -1.0, 0.0);
                            }
                        }
                    }
                    data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                    if (data != null) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            if (PlayerMove.slimed.contains(player.getUniqueId())) {
                                if (location2.clone().add(0.0, -1.0, 0.0).getBlock().getType().isSolid()) {
                                    if (location2.clone().add(0.0, -1.0, 0.0).getBlock().getType() != Material.SLIME_BLOCK) {
                                        PlayerMove.slimed.remove(player.getUniqueId());
                                    }
                                    Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)CerberusAntiCheat.getInstance(), () -> this.inair.remove(player.getUniqueId()), 5L);
                                }
                                m = 5;
                                i = (int)(m + amplifier2 * 0.7);
                                if (location2.getY() > location.getY() && ((!PlayerMove.falldistance.containsKey(player.getUniqueId()) && UtilsA.getDrs(location2) > i) || location2.getY() - PlayerMove.falldistance.get(player.getUniqueId()) > 6.0)) {
                                    CerberusAntiCheat.getInstance().failure(this.check, player, "High Jumping on Slime.", "(Type: A)");
                                }
                            }
                            else if (!this.pjumped.containsKey(player.getUniqueId())) {
                                if (UtilsB.isGround(location) && !isgto) {
                                    this.pjumped.put(player.getUniqueId(), new FixedLoc(location.getX(), location.getY(), location.getZ()));
                                    PlayerMove.pstarted.put(player.getUniqueId(), System.currentTimeMillis());
                                }
                            }
                            else if (System.currentTimeMillis() - data.getLastBlockPlace() < 1000L || System.currentTimeMillis() - data.getLastExplode() < 5000L) {
                                this.pjumped.remove(player.getUniqueId());
                                PlayerMove.pstarted.remove(player.getUniqueId());
                            }
                            else if (!isgto) {
                                if (amplifier2 > 0) {
                                    this.lastjump.put(player.getUniqueId(), System.currentTimeMillis());
                                }
                                else if (this.lastjump.containsKey(player.getUniqueId()) && System.currentTimeMillis() - this.lastjump.get(player.getUniqueId()) < 1000L) {
                                    return;
                                }
                                if (System.currentTimeMillis() - data.getLastKnocked() >= 2000L) {
                                    if (System.currentTimeMillis() - data.getLastTP() >= 2000L) {
                                        location2.getChunk().getEntities();
                                        for (length = array.length; j < length; ++j) {
                                            entity = array[j];
                                            if (entity.getType() == EntityType.BOAT) {
                                                eloc = entity.getLocation();
                                                if (Math.abs(eloc.getX() - location2.getX()) < 2.0 && Math.abs(eloc.getY() - location2.getY()) < 2.0 && Math.abs(eloc.getZ() - location2.getZ()) < 2.0) {
                                                    return;
                                                }
                                            }
                                        }
                                        jd = this.pjumped.get(player.getUniqueId());
                                        Bukkit.getServer().getPluginManager().callEvent((Event)new FlyMoveEvent(e.getPlayer(), jd, location2, amplifier2, amplifiers2, arounding));
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }
    
    static {
        falldistance = new HashMap<UUID, Double>();
        pstarted = new ConcurrentHashMap<UUID, Long>();
        slimed = ConcurrentHashMap.newKeySet();
    }
}
