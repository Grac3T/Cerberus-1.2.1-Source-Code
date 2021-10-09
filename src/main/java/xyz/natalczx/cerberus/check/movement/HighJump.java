// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import java.util.Iterator;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.listener.PlayerMove;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class HighJump extends Check
{
    private final Map<UUID, Long> flyTicksA;
    public static boolean allowblockbugging;
    
    public HighJump(final CerberusAntiCheat AntiCheat) {
        super("HighJump", "HighJump", AntiCheat);
        this.flyTicksA = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        this.flyTicksA.remove(e.getPlayer().getUniqueId());
    }
    
    public void onMove(final PlayerMoveEvent event) {
        Location to;
        Location from;
        Player player;
        double dif;
        UserData data;
        long time;
        double maxdif;
        int level;
        int i;
        this.async(() -> {
            if (!(!this.getAnticheat().isEnabled())) {
                to = event.getTo();
                from = event.getFrom();
                if (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) {
                    player = event.getPlayer();
                    if (!PlayerMove.slimed.contains(player.getUniqueId())) {
                        dif = event.getTo().clone().getY() - event.getFrom().clone().getY();
                        if (player.getGameMode() != GameMode.CREATIVE) {
                            data = this.getAnticheat().getDataManager().getData(player);
                            if (data != null) {
                                if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                                    if (System.currentTimeMillis() - data.getLastTP() >= 300L) {
                                        if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                            if (System.currentTimeMillis() - data.getLastExplode() >= 3000L) {
                                                if (System.currentTimeMillis() - data.getLastKnocked() >= 2000L) {
                                                    if (this.getAnticheat().getLagAssist().getPing(player) <= 150) {
                                                        time = 0L;
                                                        if (this.flyTicksA.containsKey(player.getUniqueId())) {
                                                            time = this.flyTicksA.get(player.getUniqueId());
                                                        }
                                                        if (System.currentTimeMillis() - time >= 3000L) {
                                                            if (dif >= 0.77) {
                                                                maxdif = 0.77;
                                                                if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                                                                    level = this.getPotionEffectLevel(player, PotionEffectType.JUMP);
                                                                    if (level > 0) {
                                                                        maxdif *= level * 0.3 + 1.0;
                                                                    }
                                                                }
                                                                if (dif > maxdif) {
                                                                    for (i = 0; i < dif / maxdif; ++i) {
                                                                        this.getAnticheat().failure(this, player, "HighJump: " + dif + ">" + maxdif, "(Type: A)");
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
        });
    }
    
    private int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    static {
        HighJump.allowblockbugging = false;
    }
}
