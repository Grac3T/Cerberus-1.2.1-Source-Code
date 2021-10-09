// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.util.Vector;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.ChatColor;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.natalczx.cerberus.helper.Ping;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsC;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AntiKBA extends Check
{
    private final Map<Player, Long> lastVelocity;
    private final Map<Player, Integer> awaitingVelocity;
    private final Map<Player, Double> totalMoved;
    
    public AntiKBA(final CerberusAntiCheat AntiCheat) {
        super("AntiKB", "AntiKB", AntiCheat);
        this.lastVelocity = new HashMap<Player, Long>();
        this.awaitingVelocity = new HashMap<Player, Integer>();
        this.totalMoved = new HashMap<Player, Double>();
        this.setMaxViolations(10);
        this.setBannable(false);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent playerQuitEvent) {
        final Player player;
        this.async(() -> {
            player = playerQuitEvent.getPlayer();
            this.lastVelocity.remove(player);
            this.awaitingVelocity.remove(player);
            this.totalMoved.remove(player);
        });
    }
    
    @EventHandler
    public void Move(final PlayerMoveEvent playerMoveEvent) {
        final Player player;
        UserData data;
        Location lastloc;
        Location actualloc;
        int n;
        long l;
        double d2;
        double d3;
        final Object o;
        int n2;
        int n3;
        int n4;
        this.async(() -> {
            player = playerMoveEvent.getPlayer();
            if (!UtilsC.isOnBlock(player, 0, new Material[] { Material.WEB })) {
                if (!UtilsC.isOnBlock(player, 1, new Material[] { Material.WEB }) && !UtilsC.isHoveringOverWater(player, 1) && !UtilsC.isHoveringOverWater(player, 0) && !player.getAllowFlight() && Ping.getPing(player) <= 400) {
                    data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                    if (data != null) {
                        lastloc = data.getLastAntiKB();
                        actualloc = player.getLocation().clone();
                        if (lastloc == null || lastloc != actualloc) {
                            data.setLastAntiKB(actualloc);
                            n = 0;
                            if (this.awaitingVelocity.containsKey(player)) {
                                n = this.awaitingVelocity.get(player);
                            }
                            l = 0L;
                            if (this.lastVelocity.containsKey(player)) {
                                l = this.lastVelocity.get(player);
                            }
                            if (player.getLastDamageCause() == null || (player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.PROJECTILE)) {
                                n = 0;
                            }
                            if (System.currentTimeMillis() - l > 2000L && n > 0) {
                                --n;
                            }
                            d2 = 0.0;
                            if (this.totalMoved.containsKey(player)) {
                                d2 = this.totalMoved.get(player);
                            }
                            d3 = playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY();
                            if (o > 0.0) {
                                d2 += d3;
                            }
                            n2 = 0;
                            n3 = 1;
                            if (n > 0) {
                                if (d2 < 0.3) {
                                    n2 += 9;
                                }
                                else {
                                    n2 = 0;
                                    d2 = 0.0;
                                    --n;
                                }
                                if (UtilsC.isOnGround(player, -1) || UtilsC.isOnGround(player, -2) || UtilsC.isOnGround(player, -3)) {
                                    n2 -= 9;
                                }
                            }
                            if (n2 > n3) {
                                if (d2 == 0.0) {
                                    if (Ping.getPing(player) > 500) {
                                        return;
                                    }
                                    else {
                                        this.getAnticheat().failure(this, player, ChatColor.RED + "difference is 0.0 (Type: A)", "(Type: A)");
                                    }
                                }
                                else if (Ping.getPing(player) > 220) {
                                    return;
                                }
                                n4 = 0;
                                d2 = 0.0;
                                --n;
                            }
                            this.awaitingVelocity.put(player, n);
                            this.totalMoved.put(player, d2);
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void Velocity(final PlayerVelocityEvent playerVelocityEvent) {
        final Player player;
        long l;
        final Object o;
        Vector vector;
        double d2;
        double d3;
        final Object o2;
        int n;
        this.async(() -> {
            player = playerVelocityEvent.getPlayer();
            if (!UtilsC.isOnBlock(player, 0, new Material[] { Material.WEB })) {
                if (!UtilsC.isOnBlock(player, 1, new Material[] { Material.WEB })) {
                    if (!UtilsC.isHoveringOverWater(player, 1) && !UtilsC.isHoveringOverWater(player, 0)) {
                        if (!UtilsC.isOnGround(player, -1) && !UtilsC.isOnGround(player, -2) && !UtilsC.isOnGround(player, -3)) {
                            if (!player.getAllowFlight()) {
                                if (this.lastVelocity.containsKey(player)) {
                                    l = System.currentTimeMillis() - this.lastVelocity.get(player);
                                    if (o < 500L) {
                                        return;
                                    }
                                }
                                vector = playerVelocityEvent.getVelocity();
                                d2 = Math.abs(vector.getY());
                                if (d2 > 0.0) {
                                    d3 = (int)(Math.pow(d2 + 2.0, 2.0) * 5.0);
                                    if (o2 > 20.0) {
                                        n = 0;
                                        if (this.awaitingVelocity.containsKey(player)) {
                                            n = this.awaitingVelocity.get(player);
                                        }
                                        this.awaitingVelocity.put(player, ++n);
                                        this.lastVelocity.put(player, System.currentTimeMillis());
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
