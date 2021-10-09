// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.config.Settings;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class FlyG extends Check
{
    private final Map<UUID, Long> flyTicksA;
    
    public FlyG(final CerberusAntiCheat plugin) {
        super("FlyG", "Fly", plugin);
        this.flyTicksA = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        this.flyTicksA.remove(uuid);
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Player p;
        UserData data;
        long time;
        Location from;
        Location to;
        Player player;
        int i;
        Material mat;
        this.async(() -> {
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
                            time = 0L;
                            if (this.flyTicksA.containsKey(p.getUniqueId())) {
                                time = this.flyTicksA.get(p.getUniqueId());
                            }
                            if (System.currentTimeMillis() - time >= 3000L) {
                                from = event.getFrom().clone();
                                to = event.getTo().clone();
                                player = event.getPlayer();
                                if (to.getX() != from.getX() || to.getZ() != from.getZ()) {
                                    if (!event.isCancelled() && !player.getAllowFlight() && player.getVehicle() == null && this.getAnticheat().getLagAssist().getTPS() >= Settings.IMP.LAG_ASSIST.MAX_TPS && !UtilsB.isInWater(player) && !UtilCheat.isInWeb(player)) {
                                        if (to.getY() >= from.getY()) {
                                            i = 0;
                                            while (i > -3) {
                                                if (UtilCheat.blocksNear(from.add(0.0, (double)i, 0.0))) {
                                                    this.flyTicksA.put(p.getUniqueId(), System.currentTimeMillis() + 2900L);
                                                    return;
                                                }
                                                else if (UtilCheat.blocksNear(to.add(0.0, (double)i, 0.0))) {
                                                    this.flyTicksA.put(p.getUniqueId(), System.currentTimeMillis() + 2900L);
                                                    return;
                                                }
                                                else {
                                                    --i;
                                                }
                                            }
                                            while (from.getBlockY() > 0) {
                                                mat = from.getBlock().getType();
                                                if (mat != Material.AIR && mat.isSolid() && mat == Material.SLIME_BLOCK) {
                                                    this.flyTicksA.put(p.getUniqueId(), System.currentTimeMillis());
                                                    return;
                                                }
                                                else {
                                                    from.add(0.0, -1.0, 0.0);
                                                }
                                            }
                                            this.getAnticheat().failure(this, player, "Just flying", "(Type: G)");
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
