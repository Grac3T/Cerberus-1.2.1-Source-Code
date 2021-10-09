// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.api.MessageBuilder;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.Listener;

public class KickListener implements Listener
{
    @EventHandler
    public void Kick(final PlayerKickEvent event) {
        if (event.getReason().equals("Flying is not enabled on this server")) {
            final String message = MessageBuilder.newBuilder(Settings.IMP.MESSAGES.NOTIFICATION_KICKED_FOR_FLYING).withField("{PREFIX}", Settings.IMP.MESSAGES.PREFIX).withField("{PLAYER}", event.getPlayer().getName()).coloured().toString();
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (!player.hasPermission(Settings.IMP.MESSAGES.NOTIFICATION_PERMISSION)) {
                    continue;
                }
                player.sendMessage(message);
            }
        }
    }
    
    @EventHandler(ignoreCancelled = false)
    public void onPlace(final BlockPlaceEvent event) {
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(event.getPlayer());
        if (data == null) {
            return;
        }
        data.setLastBlockPlace(System.currentTimeMillis());
    }
}
