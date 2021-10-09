// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.Location;
import xyz.natalczx.cerberus.api.FixedLoc;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.events.FlyMoveEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.HashMap;
import xyz.natalczx.cerberus.check.Check;

public class AirJump extends Check
{
    public static HashMap<UUID, Long> falldmg;
    
    public AirJump(final CerberusAntiCheat plugin) {
        super("AirJump", "Fly", plugin);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();
        AirJump.falldmg.remove(uuid);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDmg(final EntityDamageEvent e) {
        this.async(() -> {
            if (e.getEntity() instanceof Player) {
                AirJump.falldmg.put(e.getEntity().getUniqueId(), System.currentTimeMillis());
            }
        });
    }
    
    @EventHandler
    public void onFly(final FlyMoveEvent e) {
        if (e.isArounding()) {
            return;
        }
        double max = 1.26;
        if (e.getAmplifier() > 0) {
            max *= e.getAmplifier() + 0.5;
        }
        final UserData data = this.getAnticheat().getDataManager().getData(e.getPlayer());
        if (data == null) {
            return;
        }
        if (AirJump.falldmg.containsKey(e.getPlayer().getUniqueId()) && data.getActualTime() - AirJump.falldmg.get(e.getPlayer().getUniqueId()) < 1000L) {
            max *= 2.0;
        }
        final FixedLoc from = e.getFrom();
        final Location to = e.getTo();
        if (to.getY() - from.getY() <= max) {
            return;
        }
        CerberusAntiCheat.getInstance().failure(this, e.getPlayer(), "Flying high.", "(Type: A)");
    }
    
    static {
        AirJump.falldmg = new HashMap<UUID, Long>();
    }
}
