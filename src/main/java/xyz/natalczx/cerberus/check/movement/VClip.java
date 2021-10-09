// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.config.Settings;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import java.util.ArrayList;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.Location;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import java.util.List;
import xyz.natalczx.cerberus.check.Check;

public class VClip extends Check
{
    private final List<Material> allowed;
    private final List<UUID> teleported;
    private final Map<UUID, Location> lastLocation;
    
    public VClip(final CerberusAntiCheat AntiCheat) {
        super("VClip", "VClip", AntiCheat);
        this.allowed = new ArrayList<Material>();
        this.teleported = new ArrayList<UUID>();
        this.lastLocation = new HashMap<UUID, Location>();
        this.allowed.add(Material.PISTON_EXTENSION);
        this.allowed.add(Material.PISTON_STICKY_BASE);
        this.allowed.add(Material.PISTON_BASE);
        this.allowed.add(Material.SIGN_POST);
        this.allowed.add(Material.WALL_SIGN);
        this.allowed.add(Material.STRING);
        this.allowed.add(Material.AIR);
        this.allowed.add(Material.FENCE_GATE);
        this.allowed.add(Material.CHEST);
        this.setViolationResetTime(10000L);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.async(() -> {
            this.teleported.remove(event.getPlayer().getUniqueId());
            this.lastLocation.remove(event.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        Player p;
        UserData data;
        int ping;
        Location from;
        Location to;
        double yDist;
        double y;
        Location l;
        final Player player;
        final Player player2;
        this.async(() -> {
            if (!(!this.getAnticheat().isEnabled())) {
                p = event.getPlayer();
                if (p != null) {
                    try {
                        if (p instanceof TemporaryPlayer) {
                            return;
                        }
                    }
                    catch (Exception ex) {}
                    data = this.getAnticheat().getDataManager().getData(p);
                    if (data != null) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            ping = this.getAnticheat().getLagAssist().getPing(p);
                            if (ping != 0 && ping <= Settings.IMP.LAG_ASSIST.MAX_PING) {
                                from = event.getFrom().clone();
                                to = event.getTo().clone();
                                if (this.getAnticheat().isEnabled() && from.getY() != to.getY() && !p.getAllowFlight() && p.getVehicle() == null && !this.teleported.remove(p.getUniqueId()) && to.getY() > 0.0 && to.getY() < p.getWorld().getMaxHeight() && UtilCheat.blocksNear(p) && p.getLocation().getY() >= 5.0 && p.getLocation().getY() <= p.getWorld().getMaxHeight()) {
                                    yDist = from.getBlockY() - to.getBlockY();
                                    y = 0.0;
                                    while (y < Math.abs(yDist)) {
                                        l = ((yDist < -0.2) ? from.getBlock().getLocation().clone().add(0.0, y, 0.0) : to.getBlock().getLocation().clone().add(0.0, y, 0.0));
                                        if ((yDist > 20.0 || yDist < -20.0) && l.getBlock().getType() != Material.AIR && l.getBlock().getType().isSolid() && !this.allowed.contains(l.getBlock().getType())) {
                                            this.getAnticheat().failure(this, p, "More than 20 blocks.", "(Type: A)");
                                            this.sync(() -> {
                                                player.teleport(from);
                                                player.kickPlayer("No");
                                            });
                                        }
                                        else if (Math.abs(yDist) >= 0.1) {
                                            if (l.getBlock().getType() != Material.AIR && Math.abs(yDist) > 1.0 && l.getBlock().getType().isSolid() && !this.allowed.contains(l.getBlock().getType()) && this.lastLocation.get(p.getUniqueId()) != null) {
                                                this.getAnticheat().failure(this, p, Math.abs(yDist) + " blocks", "(Type: B)");
                                                this.sync(() -> player2.teleport((Location)this.lastLocation.get(player2.getUniqueId())));
                                            }
                                            else {
                                                this.lastLocation.put(p.getUniqueId(), p.getLocation());
                                            }
                                            ++y;
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
}
