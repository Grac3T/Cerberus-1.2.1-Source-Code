// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.player;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.helper.SetBackSystem;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import xyz.natalczx.cerberus.helper.TimerUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class GroundSpoof extends Check
{
    public GroundSpoof(final CerberusAntiCheat AntiCheat) {
        super("GroundsSpoof", "GroundSpoof", AntiCheat);
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p;
        final UserData data;
        Location to;
        Location from;
        double diff;
        int dist;
        this.async(() -> {
            p = e.getPlayer();
            data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
            if (data != null) {
                if (e.getTo().getY() <= e.getFrom().getY()) {
                    if (data.isLastBlockPlaced_GroundSpoof()) {
                        if (TimerUtils.elapsed(data.getLastBlockPlacedTicks(), 500L)) {
                            data.setLastBlockPlaced_GroundSpoof(false);
                        }
                    }
                    else {
                        to = e.getTo().clone();
                        from = e.getFrom().clone();
                        diff = to.toVector().distance(from.toVector());
                        dist = UtilsA.getDistanceToGround(p);
                        if (!(!to.getWorld().isChunkLoaded(to.getBlockX() / 16, to.getBlockZ() / 16))) {
                            if (from.add(0.0, -1.5, 0.0).getBlock().getType() != Material.AIR) {
                                data.setGroundSpoofVL(0);
                            }
                            else if (to.getY() > from.getY() || UtilsA.isOnGround4(p) || UtilVelocity.didTakeVelocity(p)) {
                                data.setGroundSpoofVL(0);
                            }
                            else if (p.isOnGround() && diff > 0.0 && !UtilsA.isOnGround(p) && dist >= 2 && e.getTo().getY() < e.getFrom().getY()) {
                                if (data.getGroundSpoofVL() >= 4) {
                                    if (data.getAirTicks() >= 10) {
                                        this.getAnticheat().failure(this, p, "Spoofed On-Ground Packet. [NoFall]", "(Type: A)");
                                    }
                                    else {
                                        this.getAnticheat().failure(this, p, "Spoofed On-Ground Packet.", "(Type: B)");
                                    }
                                    SetBackSystem.setBack(p);
                                }
                                else {
                                    data.setGroundSpoofVL(data.getGroundSpoofVL() + 1);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player p;
        final UserData data;
        this.async(() -> {
            p = e.getPlayer();
            data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
            if (data != null && !data.isLastBlockPlaced_GroundSpoof()) {
                data.setLastBlockPlaced_GroundSpoof(true);
                data.setLastBlockPlacedTicks(TimerUtils.nowlong());
            }
        });
    }
}
