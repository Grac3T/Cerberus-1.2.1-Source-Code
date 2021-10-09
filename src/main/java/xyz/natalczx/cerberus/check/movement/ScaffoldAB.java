// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import org.bukkit.GameMode;
import java.util.Set;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class ScaffoldAB extends Check
{
    public ScaffoldAB(final CerberusAntiCheat AntiCheat) {
        super("ScaffoldAB", "Scaffold", AntiCheat);
        this.setViolationResetTime(1000L);
        this.setViolationsToNotify(2);
    }
    
    @EventHandler
    public void onPlaceBlock(final BlockPlaceEvent event) {
        final Player player;
        final Block target;
        this.async(() -> {
            player = event.getPlayer();
            target = player.getTargetBlock((Set)null, 5);
            if (!player.getGameMode().equals((Object)GameMode.CREATIVE) && !player.getAllowFlight() && event.getPlayer().getVehicle() == null && this.getAnticheat().isEnabled() && !UtilVelocity.didTakeVelocity(player)) {
                if (event.getBlock().getWorld().getBlockAt(event.getBlock().getLocation().subtract(0.0, 1.0, 0.0)).getType() == Material.AIR) {
                    if (!event.getBlock().getLocation().equals((Object)target.getLocation()) && !event.isCancelled() && target.getType().isSolid() && !target.getType().name().toLowerCase().contains("sign") && !target.getType().toString().toLowerCase().contains("fence") && player.getLocation().clone().getY() > event.getBlock().getLocation().getY()) {
                        this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental (A)", "(Type: A)");
                    }
                    if (event.getBlockAgainst().isLiquid() && event.getBlock().getType() != Material.WATER_LILY) {
                        this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental (B)", "(Type: B)");
                    }
                }
            }
        });
    }
}
