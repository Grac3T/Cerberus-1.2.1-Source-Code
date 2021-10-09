// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import xyz.natalczx.cerberus.helper.needscleanup.ExtraUtils;
import org.bukkit.Material;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import org.bukkit.GameMode;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class ScaffoldC extends Check
{
    public ScaffoldC(final CerberusAntiCheat AntiCheat) {
        super("ScaffoldC", "Scaffold", AntiCheat);
        this.setViolationResetTime(1000L);
        this.setViolationsToNotify(2);
    }
    
    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent event) {
        final Player player;
        this.async(() -> {
            player = event.getPlayer();
            if (!player.getGameMode().equals((Object)GameMode.CREATIVE) && !player.getAllowFlight() && event.getPlayer().getVehicle() == null && this.getAnticheat().isEnabled() && !UtilVelocity.didTakeVelocity(player)) {
                if (!Settings.IMP.SETTINGS.ALLOW_AMBUSE) {
                    if (event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0, 1.0, 0.0)).getType() == Material.AIR && !player.isSneaking() && !player.isFlying() && ExtraUtils.groundAround(player.getLocation().clone()) && event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0, 1.0, 0.0)).getType() == Material.AIR && player.getWorld().getBlockAt(player.getLocation().clone().subtract(0.0, 1.0, 0.0)).equals(event.getBlock())) {
                        this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental (C) - Ambuse? Very good player?", "(Type: C)");
                    }
                }
            }
        });
    }
}
