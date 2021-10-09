// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class HitBoxA extends Check
{
    private final Map<UUID, Integer> count;
    private final Map<UUID, Player> lastHit;
    private final Map<UUID, Double> yawDif;
    
    public HitBoxA(final CerberusAntiCheat AntiCheat) {
        super("HitBoxA", "Hitbox", AntiCheat);
        this.count = new HashMap<UUID, Integer>();
        this.lastHit = new HashMap<UUID, Player>();
        this.yawDif = new HashMap<UUID, Double>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setViolationResetTime(1000L);
        this.setViolationsToNotify(150);
        this.setMaxViolations(300);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        this.count.remove(e.getPlayer().getUniqueId());
        this.yawDif.remove(e.getPlayer().getUniqueId());
        this.lastHit.remove(e.getPlayer().getUniqueId());
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onUse(final EntityDamageByEntityEvent e) {
        Player player;
        LivingEntity attacked;
        int verbose;
        double offset;
        this.async(() -> {
            if (!(!(e.getDamager() instanceof Player))) {
                player = (Player)e.getDamager();
                if (!(!(e.getEntity() instanceof LivingEntity))) {
                    attacked = (LivingEntity)e.getEntity();
                    if (!player.getAllowFlight()) {
                        verbose = this.count.getOrDefault(player.getUniqueId(), 0);
                        offset = UtilCheat.getOffsetOffCursor(player, attacked);
                        if (offset > 30.0) {
                            verbose += 2;
                            if (verbose > 25) {
                                this.getAnticheat().failure(this, player, UtilsB.round(offset, 4) + " >= 30 (A)", "(Type: A)");
                            }
                        }
                        else if (verbose > 0) {
                            --verbose;
                        }
                        this.count.put(player.getUniqueId(), verbose);
                    }
                }
            }
        });
    }
}
