// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.packet.PacketCoreA;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class SafeWalk extends Check
{
    private final Map<UUID, Integer> verbose;
    private final Map<UUID, Location> lastBlockLoc;
    private final Map<UUID, Long> lastverbose;
    
    public SafeWalk(final CerberusAntiCheat AntiCheat) {
        super("SafeWalk", "Experimental SafeWalk", AntiCheat);
        this.verbose = new HashMap<UUID, Integer>();
        this.lastBlockLoc = new HashMap<UUID, Location>();
        this.lastverbose = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.verbose.remove(e.getPlayer().getUniqueId());
            this.lastBlockLoc.remove(e.getPlayer().getUniqueId());
            this.lastverbose.remove(e.getPlayer().getUniqueId());
        });
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        this.async(() -> this.lastverbose.put(e.getPlayer().getUniqueId(), 0L));
    }
    
    @EventHandler
    public void EntityAction(final RealMoveEvent event) {
        final Player player = event.getPlayer();
        final boolean sneaking = player.isSneaking();
        double x = event.getTo().clone().getX();
        double z = event.getTo().clone().getZ();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
        if (data == null) {
            return;
        }
        if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
            return;
        }
        if (x == 0.0 || z == 0.0) {
            return;
        }
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);
        final Location bloc = this.lastBlockLoc.get(player.getUniqueId());
        final Location acbloc = player.getLocation().getBlock().getLocation().clone().add(0.0, -1.0, 0.0);
        this.lastBlockLoc.put(player.getUniqueId(), acbloc);
        if (System.currentTimeMillis() - this.lastverbose.get(player.getUniqueId()) > 2000L) {
            verbose = 0;
        }
        if (bloc == null) {
            return;
        }
        try {
            String xs = x + "";
            String zs = z + "";
            xs = xs.substring(xs.indexOf(".") + 1, xs.indexOf(".") + 3);
            zs = zs.substring(zs.indexOf(".") + 1, zs.indexOf(".") + 3);
            x = Integer.parseInt(xs);
            z = Integer.parseInt(zs);
        }
        catch (Exception ignored) {
            return;
        }
        if ((x == 29.0 || z == 29.0) && !sneaking && acbloc != bloc) {
            if (verbose > 2) {
                this.getAnticheat().failure(this, player, "SafeWalk ", "(Type: A)");
            }
            else {
                ++verbose;
                this.lastverbose.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
        PacketCoreA.movePackets.remove(player.getUniqueId());
        this.verbose.put(player.getUniqueId(), verbose);
    }
}
