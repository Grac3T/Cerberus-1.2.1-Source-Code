// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsC;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.needscleanup.ExtraUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.ArrayList;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.List;
import xyz.natalczx.cerberus.check.Check;

public class Change extends Check
{
    private final List<UUID> built;
    private final List<UUID> falling;
    
    public Change(final CerberusAntiCheat AntiCheat) {
        super("Change", "Change", AntiCheat);
        this.built = new ArrayList<UUID>();
        this.falling = new ArrayList<UUID>();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent playerMoveEvent) {
        Player player;
        int n;
        int n2;
        int n3;
        this.async(() -> {
            if (!(!this.isEnabled())) {
                player = playerMoveEvent.getPlayer();
                if (!player.getAllowFlight()) {
                    if (!player.isInsideVehicle()) {
                        try {
                            if (!player.getNearbyEntities(1.0, 1.0, 1.0).isEmpty()) {
                                return;
                            }
                        }
                        catch (Exception ex) {}
                        if (!this.built.contains(player.getUniqueId())) {
                            n = 0;
                            n2 = 5;
                            Label_0264_1: {
                                if (!ExtraUtils.isOnGround(player)) {
                                    if (!UtilsC.isOnBlock(player, 0, new Material[] { Material.CARPET }) && !UtilsC.isHoveringOverWater(player, 0) && player.getLocation().clone().getBlock().getType() == Material.AIR) {
                                        if (playerMoveEvent.getFrom().getY() > playerMoveEvent.getTo().getY()) {
                                            if (!this.falling.contains(player.getUniqueId())) {
                                                this.falling.add(player.getUniqueId());
                                                break Label_0264_1;
                                            }
                                            else {
                                                break Label_0264_1;
                                            }
                                        }
                                        else {
                                            n = ((playerMoveEvent.getTo().getY() > playerMoveEvent.getFrom().getY()) ? (this.falling.contains(player.getUniqueId()) ? (++n) : (--n)) : (--n));
                                            break Label_0264_1;
                                        }
                                    }
                                }
                                this.falling.remove(player.getUniqueId());
                            }
                            if (n > n2) {
                                this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", (String[])null);
                                n3 = 0;
                                this.falling.remove(player.getUniqueId());
                            }
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent playerQuitEvent) {
        final Player player;
        this.async(() -> {
            player = playerQuitEvent.getPlayer();
            this.falling.remove(player.getUniqueId());
        });
    }
    
    @EventHandler
    public void onAttack(final BlockPlaceEvent blockPlaceEvent) {
        Player player;
        this.async(() -> {
            if (blockPlaceEvent.getPlayer() != null) {
                player = blockPlaceEvent.getPlayer();
                this.built.add(player.getUniqueId());
                Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)CerberusAntiCheat.instance, () -> this.built.remove(player.getUniqueId()), 60L);
            }
        });
    }
}
