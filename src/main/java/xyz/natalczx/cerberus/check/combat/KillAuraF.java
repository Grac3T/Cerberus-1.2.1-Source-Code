// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.natalczx.cerberus.check.movement.Phase;
import org.bukkit.entity.LivingEntity;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.ArrayList;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraF extends Check
{
    private final Map<Player, Integer> counts;
    private final List<Player> blockGlitched;
    
    public KillAuraF(final CerberusAntiCheat AntiCheat) {
        super("KillAuraF", "KillAura", AntiCheat);
        this.counts = new HashMap<Player, Integer>();
        this.blockGlitched = new ArrayList<Player>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(7);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogout(final PlayerQuitEvent e) {
        this.counts.remove(e.getPlayer());
        this.blockGlitched.remove(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(final BlockBreakEvent e) {
        this.async(() -> {
            if (e.isCancelled()) {
                this.blockGlitched.add(e.getPlayer());
            }
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void checkKillaura(final EntityDamageByEntityEvent e) {
        Player p;
        int Count;
        Player attacked;
        Location dloc;
        Location aloc;
        double zdif;
        double xdif;
        int y;
        Location zBlock;
        Location xBlock;
        this.async(() -> {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && this.getAnticheat().isEnabled() && e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                p = (Player)e.getDamager();
                if (!UtilCheat.slabsNear(p.getEyeLocation()) && !UtilCheat.slabsNear(p.getEyeLocation().clone().add(0.0, 0.5, 0.0))) {
                    Count = 0;
                    if (this.counts.containsKey(p)) {
                        Count = this.counts.get(p);
                    }
                    attacked = (Player)e.getEntity();
                    dloc = p.getLocation();
                    aloc = attacked.getLocation();
                    zdif = Math.abs(dloc.getZ() - aloc.getZ());
                    xdif = Math.abs(dloc.getX() - aloc.getX());
                    if (xdif != 0.0 && zdif != 0.0 && UtilCheat.getOffsetOffCursor(p, (LivingEntity)attacked) <= 20.0) {
                        for (y = 0; y < 1; ++y) {
                            zBlock = ((zdif < -0.2) ? dloc.clone().add(0.0, (double)y, zdif) : aloc.clone().add(0.0, (double)y, zdif));
                            if (!Phase.allowed.contains(zBlock.getBlock().getType()) && zBlock.getBlock().getType().isSolid() && !p.hasLineOfSight((Entity)attacked) && !UtilCheat.isSlab(zBlock.getBlock())) {
                                ++Count;
                            }
                            xBlock = ((xdif < -0.2) ? dloc.clone().add(xdif, (double)y, 0.0) : aloc.clone().add(xdif, (double)y, 0.0));
                            if (!Phase.allowed.contains(xBlock.getBlock().getType()) && xBlock.getBlock().getType().isSolid() && !p.hasLineOfSight((Entity)attacked) && !UtilCheat.isSlab(xBlock.getBlock())) {
                                ++Count;
                            }
                        }
                        if (Count > 3) {
                            this.getAnticheat().failure(this, p, "Wall (F) count > 3", "(Type: F)");
                            Count = 0;
                        }
                        this.counts.put(p, Count);
                    }
                }
            }
        });
    }
}
