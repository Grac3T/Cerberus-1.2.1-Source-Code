// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.Listener;

public class EntityDamageEvent implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent e) {
        UserData data;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            if (e.getEntity() instanceof Player) {
                data = CerberusAntiCheat.getInstance().getDataManager().getData((Player)e.getEntity());
                if (data != null) {
                    data.setLastKnocked(System.currentTimeMillis());
                    if (e.getDamager() instanceof TNTPrimed) {
                        data.setLastExplode(System.currentTimeMillis());
                    }
                }
            }
        });
    }
}
