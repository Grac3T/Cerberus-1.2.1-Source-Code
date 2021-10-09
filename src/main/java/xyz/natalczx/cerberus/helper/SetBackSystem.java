// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class SetBackSystem implements Listener
{
    public static void setBack(final Player p) {
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null && !data.isShouldSetBack()) {
            data.setShouldSetBack(true);
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data != null) {
            if (data.isShouldSetBack()) {
                if (data.getSetBackTicks() >= 5) {
                    final Location setback = (data.getSetbackLocation() != null) ? data.getSetbackLocation() : e.getFrom();
                    e.setTo(setback);
                    data.setShouldSetBack(false);
                }
                else {
                    final Location setback = (data.getSetbackLocation() != null) ? data.getSetbackLocation() : e.getFrom();
                    e.setTo(setback);
                    data.setSetBackTicks(data.getSetBackTicks() + 1);
                }
            }
            else if (UtilsA.isOnGround(p)) {
                data.setSetbackLocation(e.getFrom());
            }
        }
    }
}
