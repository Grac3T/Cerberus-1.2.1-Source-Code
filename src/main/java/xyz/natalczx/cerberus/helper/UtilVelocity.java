// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class UtilVelocity implements Listener
{
    public static boolean didTakeVelocity(final Player p) {
        boolean out = false;
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && data.isDidTakeVelocity()) {
            out = true;
        }
        return out;
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && data.isDidTakeVelocity() && TimerUtils.elapsed(data.getLastVelMS(), 2000L)) {
            data.setDidTakeVelocity(false);
        }
    }
    
    @EventHandler
    public void onVelEvent(final PlayerVelocityEvent e) {
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null) {
            data.setDidTakeVelocity(true);
            data.setLastVelMS(TimerUtils.nowlong());
        }
    }
}
