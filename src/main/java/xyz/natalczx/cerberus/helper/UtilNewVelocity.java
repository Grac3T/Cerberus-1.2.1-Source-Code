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

public class UtilNewVelocity implements Listener
{
    public static boolean didTakeVel(final Player p) {
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        return data != null && data.isLastVelUpdateBoolean();
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && data.isLastVelUpdateBoolean()) {
            if (TimerUtils.elapsed(data.getLastVelUpdate(), ConfigValues.VelTimeReset_1_FORCE_RESET)) {
                data.setLastVelUpdateBoolean(false);
            }
            if (TimerUtils.elapsed(data.getLastVelUpdate(), ConfigValues.VelTimeReset_1)) {
                if (!p.isOnGround()) {
                    data.setLastVelUpdate(TimerUtils.nowlong());
                }
                else {
                    data.setLastVelUpdateBoolean(false);
                }
            }
        }
    }
    
    @EventHandler
    public void onVelChange(final PlayerVelocityEvent e) {
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && p.getNoDamageTicks() <= 0 && !data.isLastVelUpdateBoolean()) {
            data.setLastVelUpdateBoolean(true);
            data.setLastVelUpdate(TimerUtils.nowlong());
        }
    }
}
