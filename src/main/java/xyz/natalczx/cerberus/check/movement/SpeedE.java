// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.GameMode;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.Listener;
import xyz.natalczx.cerberus.check.Check;

public class SpeedE extends Check implements Listener
{
    public SpeedE(final CerberusAntiCheat AntiCheat) {
        super("SpeedE", "Speed", AntiCheat);
        this.setViolationResetTime(TimeUnit.MINUTES.toMillis(2L));
    }
    
    @EventHandler
    public void onMove(final RealMoveEvent e) {
        Player player;
        UserData data;
        Location from;
        Location to;
        long lastClearDiff;
        this.async(() -> {
            try {
                player = e.getPlayer();
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
                            if (System.currentTimeMillis() - data.getLastKnocked() >= 2000L) {
                                from = e.getFrom();
                                if (player.getGameMode() == GameMode.CREATIVE || UtilsB.isLiquid(from.getBlock()) || this.getAnticheat().getLagAssist().getPing(player) > 150) {
                                    data.setSpeedEDistanceMade(0.0);
                                    data.setLastSpeedEClear(System.currentTimeMillis());
                                }
                                if (this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                    data.setSpeedEDistanceMade(0.0);
                                    data.setLastSpeedEClear(System.currentTimeMillis());
                                }
                                else {
                                    to = e.getTo();
                                    if (player.getFallDistance() > 1.0f) {
                                        data.setSpeedEDistanceMade(0.0);
                                        data.setLastSpeedEClear(System.currentTimeMillis());
                                    }
                                    else if (player.isFlying() && player.getAllowFlight()) {
                                        data.setSpeedEDistanceMade(0.0);
                                        data.setLastSpeedEClear(System.currentTimeMillis());
                                    }
                                    else if (player.hasPotionEffect(PotionEffectType.SPEED) || player.hasPotionEffect(PotionEffectType.JUMP) || UtilsB.shouldCancelSpeedE(player)) {
                                        data.setSpeedEDistanceMade(0.0);
                                        data.setLastSpeedEClear(System.currentTimeMillis());
                                    }
                                    else {
                                        data.setSpeedEDistanceMade(data.getSpeedEDistanceMade() + Math.abs(from.distance(to)));
                                        lastClearDiff = System.currentTimeMillis() - data.getLastSpeedEClear();
                                        if (lastClearDiff >= 1100L) {
                                            if (lastClearDiff < 1200L) {
                                                if (data.getSpeedEDistanceMade() > 15.0) {
                                                    this.getAnticheat().failure(this, player, "Made " + data.getSpeedEDistanceMade() + " in " + lastClearDiff + "ms, that is very fast! (E)", "(Type: E)");
                                                }
                                                data.setSpeedEDistanceMade(0.0);
                                                data.setLastSpeedEClear(System.currentTimeMillis());
                                            }
                                            else {
                                                data.setSpeedEDistanceMade(0.0);
                                                data.setLastSpeedEClear(System.currentTimeMillis());
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
