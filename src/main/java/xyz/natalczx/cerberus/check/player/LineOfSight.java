// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.player;

import java.util.Iterator;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.helper.lineofsight.BlockPathFinder;
import org.bukkit.event.block.BlockBreakEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class LineOfSight extends Check
{
    public LineOfSight(final CerberusAntiCheat AntiCheat) {
        super("LineOfSight", "LineOfSight", AntiCheat);
    }
    
    @EventHandler
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p;
        this.async(() -> {
            p = e.getPlayer();
            if (e.getBlock().getLocation().distance(p.getPlayer().getEyeLocation()) > 5.0 && !BlockPathFinder.line(p.getPlayer().getEyeLocation(), e.getBlock().getLocation()).contains(e.getBlock()) && !e.isCancelled() && p.getGameMode() != GameMode.CREATIVE) {
                this.getAnticheat().failure(this, p, "Broke a block without a line of sight too it.", (String[])null);
                e.setCancelled(true);
            }
        });
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player p;
        this.async(() -> {
            p = e.getPlayer();
            if (e.getBlock().getLocation().distance(p.getPlayer().getEyeLocation()) > 5.0 && !BlockPathFinder.line(p.getPlayer().getEyeLocation(), e.getBlock().getLocation()).contains(e.getBlock()) && !e.isCancelled() && p.getGameMode() != GameMode.CREATIVE) {
                this.getAnticheat().failure(this, p, "Placed a block without a line of sight too it.", (String[])null);
                e.setCancelled(true);
            }
        });
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        Player p;
        final Iterator<PotionEffect> iterator;
        PotionEffect potionEffect;
        this.async(() -> {
            if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.ENDER_CHEST)) {
                p = e.getPlayer();
                if (e.getClickedBlock().getLocation().distance(p.getPlayer().getEyeLocation()) > 5.0 && !BlockPathFinder.line(p.getPlayer().getEyeLocation(), e.getClickedBlock().getLocation()).contains(e.getClickedBlock()) && !e.isCancelled() && p.getGameMode() != GameMode.CREATIVE) {
                    p.getActivePotionEffects().iterator();
                    while (iterator.hasNext()) {
                        potionEffect = iterator.next();
                        if (potionEffect.getType() == PotionEffectType.JUMP) {
                            return;
                        }
                    }
                    this.getAnticheat().failure(this, p, "Interacted without a line of sight too it.", (String[])null);
                    e.setCancelled(true);
                }
            }
        });
    }
}
