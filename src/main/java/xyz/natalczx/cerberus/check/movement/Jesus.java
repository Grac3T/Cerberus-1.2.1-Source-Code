// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.UtilCheat;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.WeakHashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class Jesus extends Check
{
    private final Map<Player, Integer> onWater;
    private final List<Player> placedBlockOnWater;
    private final Map<Player, Integer> count;
    private final Map<Player, Long> velocity;
    
    public Jesus(final CerberusAntiCheat AntiCheat) {
        super("Jesus", "Jesus", AntiCheat);
        this.count = new WeakHashMap<Player, Integer>();
        this.placedBlockOnWater = new ArrayList<Player>();
        this.onWater = new WeakHashMap<Player, Integer>();
        this.velocity = new WeakHashMap<Player, Long>();
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent e) {
        this.placedBlockOnWater.remove(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(final PlayerDeathEvent e) {
        this.onWater.remove(e.getEntity());
        this.placedBlockOnWater.remove(e.getEntity());
        this.count.remove(e.getEntity());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVelocity(final PlayerVelocityEvent e) {
        this.velocity.put(e.getPlayer(), System.currentTimeMillis());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void OnPlace(final BlockPlaceEvent e) {
        this.async(() -> {
            if (e.getBlockReplacedState().getBlock().getType() == Material.WATER) {
                this.placedBlockOnWater.add(e.getPlayer());
            }
        });
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Player p;
        UserData data;
        Location from;
        Location to;
        int Count;
        this.async(() -> {
            try {
                if (!(!this.getAnticheat().isEnabled())) {
                    p = event.getPlayer();
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
                                if (System.currentTimeMillis() - data.getLastExplode() >= 2000L) {
                                    from = event.getFrom().clone();
                                    to = event.getTo().clone();
                                    if (!event.isCancelled() && (from.getX() != to.getX() || from.getZ() != to.getZ()) && !p.getAllowFlight() && !UtilCheat.isOnLilyPad(p) && !p.getLocation().clone().add(0.0, 0.4, 0.0).getBlock().getType().isSolid() && !this.placedBlockOnWater.remove(p)) {
                                        Count = this.count.getOrDefault(p, 0);
                                        if (UtilCheat.cantStandAtWater(p.getWorld().getBlockAt(p.getLocation())) && UtilCheat.isHoveringOverWater(p.getLocation()) && !UtilCheat.isFullyInWater(p.getLocation())) {
                                            Count += 2;
                                        }
                                        else {
                                            Count = ((Count > 0) ? (Count - 1) : Count);
                                        }
                                        if (Count > 19) {
                                            Count = 0;
                                            this.getAnticheat().failure(this, p, null, (String[])null);
                                        }
                                        this.count.put(p, Count);
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
