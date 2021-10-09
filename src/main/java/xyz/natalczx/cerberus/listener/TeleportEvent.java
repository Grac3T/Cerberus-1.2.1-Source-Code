// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.Listener;

public class TeleportEvent implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(final PlayerTeleportEvent e) {
        UserData data;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                data = CerberusAntiCheat.getInstance().getDataManager().getData(e.getPlayer());
                if (data != null) {
                    data.setLastTp(System.currentTimeMillis());
                }
            }
        });
    }
}
