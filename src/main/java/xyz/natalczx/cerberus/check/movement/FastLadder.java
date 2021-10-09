// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.WeakHashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class FastLadder extends Check
{
    private final Map<Player, Integer> count;
    
    public FastLadder(final CerberusAntiCheat AntiCheat) {
        super("FastLadder", "FastLadder", AntiCheat);
        this.count = new WeakHashMap<Player, Integer>();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Player player;
        UserData data;
        Location from;
        Location to;
        int Count;
        double OffsetY;
        double Limit;
        double updown;
        long percent;
        this.async(() -> {
            if (!(!this.getAnticheat().isEnabled())) {
                player = event.getPlayer();
                if (player != null) {
                    try {
                        if (player instanceof TemporaryPlayer) {
                            return;
                        }
                    }
                    catch (Exception ex) {}
                    data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                    if (data != null) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            from = event.getFrom().clone();
                            to = event.getTo().clone();
                            if (!event.isCancelled() && from.getY() != to.getY() && !player.getAllowFlight() && !this.getAnticheat().getVelocityManager().getLastVelocity().containsKey(player.getUniqueId()) && UtilsB.isOnClimbable(player, 1) && UtilsB.isOnClimbable(player, 0)) {
                                Count = this.count.getOrDefault(player, 0);
                                OffsetY = UtilsB.offset(UtilsB.getVerticalVector(from.toVector()), UtilsB.getVerticalVector(to.toVector()));
                                Limit = 0.13;
                                updown = to.getY() - from.getY();
                                if (updown > 0.0) {
                                    if (OffsetY > Limit) {
                                        ++Count;
                                    }
                                    else {
                                        Count = ((Count > -2) ? (Count - 1) : 0);
                                    }
                                    percent = Math.round((OffsetY - Limit) * 120.0);
                                    if (Count > 11) {
                                        Count = 0;
                                        this.getAnticheat().failure(this, player, percent + "% faster than normal", (String[])null);
                                    }
                                    this.count.put(player, Count);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
