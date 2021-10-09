// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.GameMode;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class NoFall extends Check
{
    private final Map<UUID, Map.Entry<Long, Integer>> noFallTicks;
    private final Map<UUID, Double> FallDistance;
    private final List<UUID> cancel;
    
    public NoFall(final CerberusAntiCheat AntiCheat) {
        super("NoFall", "NoFall", AntiCheat);
        this.noFallTicks = new HashMap<UUID, Map.Entry<Long, Integer>>();
        this.FallDistance = new HashMap<UUID, Double>();
        this.cancel = new ArrayList<UUID>();
        this.setViolationResetTime(120000L);
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        this.async(() -> this.cancel.add(e.getEntity().getUniqueId()));
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.FallDistance.remove(e.getPlayer().getUniqueId());
            this.cancel.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        this.async(() -> {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                this.cancel.add(e.getPlayer().getUniqueId());
            }
        });
    }
    
    @EventHandler
    public void move(final PlayerMoveEvent e) {
        final Location from;
        final Location to;
        Player player;
        UserData data;
        double falling;
        double def;
        double zef;
        long time;
        int count;
        double finalFalling;
        this.async(() -> {
            from = e.getFrom().clone();
            to = e.getTo().clone();
            if (from.getY() != to.getY()) {
                player = e.getPlayer();
                if (player != null) {
                    try {
                        if (player instanceof TemporaryPlayer) {
                            return;
                        }
                    }
                    catch (Exception ex) {}
                    data = this.getAnticheat().getDataManager().getData(player);
                    if (data == null) {
                        this.getAnticheat().getDataManager().addPlayerData(player);
                    }
                    else if (System.currentTimeMillis() - data.getLastBlockPlace() >= 1000L) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            if (this.getAnticheat().getLagAssist().getPing(player) != 0) {
                                if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                    if (!player.getAllowFlight() && !player.getGameMode().equals((Object)GameMode.CREATIVE) && player.getVehicle() == null && !this.cancel.remove(player.getUniqueId()) && !UtilsB.isOnClimbable(player, 0) && !UtilsB.isInWater(player)) {
                                        if (player.getHealth() > 0.0) {
                                            falling = 0.0;
                                            if (!UtilsB.isOnGround(player) && from.getY() > to.getY()) {
                                                if (this.FallDistance.containsKey(player.getUniqueId())) {
                                                    falling = this.FallDistance.get(player.getUniqueId());
                                                }
                                                falling += from.getY() - to.getY();
                                            }
                                            this.FallDistance.put(player.getUniqueId(), falling);
                                            if (falling >= 3.5) {
                                                for (def = -0.4; def < 0.5; def += 0.4) {
                                                    zef = -0.4;
                                                    while (zef < 0.5) {
                                                        if (to.add(def, -1.0, zef).getBlock().getType() != Material.AIR) {
                                                            return;
                                                        }
                                                        else {
                                                            zef += 0.4;
                                                        }
                                                    }
                                                }
                                                time = System.currentTimeMillis();
                                                count = 0;
                                                if (this.noFallTicks.containsKey(player.getUniqueId())) {
                                                    time = this.noFallTicks.get(player.getUniqueId()).getKey();
                                                    count = this.noFallTicks.get(player.getUniqueId()).getValue();
                                                }
                                                if (player.isOnGround() || player.getFallDistance() == 0.0f) {
                                                    this.getAnticheat().failure(this, player, "Fake ground (A)", "(Type: A)");
                                                    finalFalling = falling;
                                                    this.sync(() -> player.damage(finalFalling));
                                                    count += 2;
                                                }
                                                else {
                                                    --count;
                                                }
                                                if (this.noFallTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(time, 10000L)) {
                                                    count = 0;
                                                    time = System.currentTimeMillis();
                                                }
                                                if (count >= 4) {
                                                    count = 0;
                                                    this.FallDistance.put(player.getUniqueId(), 0.0);
                                                    this.getAnticheat().failure(this, player, count + ">= 4 (B)", "(Type: B)");
                                                }
                                                this.noFallTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(time, count));
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
