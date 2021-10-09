// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.check.FalseCheck;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class FlyABCD extends Check
{
    public FlyABCD(final CerberusAntiCheat AntiCheat) {
        super("FlyABCD", "Fly", AntiCheat);
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player p;
        final Location from;
        final Location to;
        int plus;
        int plus2;
        int i;
        int i2;
        int i3;
        final Location location;
        final Player player;
        this.async(() -> {
            p = event.getPlayer();
            from = event.getFrom().clone();
            to = event.getTo().clone();
            if (!(!FalseCheck.fly(p, from, to))) {
                if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
                    if (Math.abs(from.getY() - to.getY()) <= 0.1) {
                        plus = 0;
                        plus2 = 0;
                        if (p.hasPotionEffect(PotionEffectType.JUMP)) {
                            plus = SpeedC.getPotionEffectLevel(p, PotionEffectType.JUMP) / 2;
                        }
                        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                            plus2 = SpeedC.getPotionEffectLevel(p, PotionEffectType.SPEED) / 2 + plus;
                        }
                        for (i = 0; i > -3 - plus; --i) {
                            for (i2 = -1; i2 < 2 + plus2; ++i2) {
                                i3 = -1;
                                while (i3 < 2 + plus2) {
                                    if (UtilCheat.blocksNear(from.clone().add((double)i2, (double)i, (double)i3)) || UtilCheat.blocksNear(to.clone().add((double)i2, (double)i, (double)i3))) {
                                        return;
                                    }
                                    else {
                                        ++i3;
                                    }
                                }
                            }
                        }
                        Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
                            if (Math.abs(location.getY() - player.getLocation().clone().getY()) <= 0.1) {
                                this.getAnticheat().failure(this, player, "Just flying", "(Type: E)");
                            }
                        }, 3L);
                    }
                }
            }
        });
    }
}
