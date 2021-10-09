// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.api.FixedLoc;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.plugin.Plugin;
import org.bukkit.Location;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.listener.PlayerMove;
import xyz.natalczx.cerberus.events.FlyMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class FlyE extends Check
{
    public FlyE(final CerberusAntiCheat plugin) {
        super("FlyE", "Fly", plugin);
        this.setViolationResetTime(10000L);
    }
    
    @EventHandler
    public void onFly(final FlyMoveEvent e) {
        double max = 1300.0;
        final int amplifier = e.getAmplifier();
        if (amplifier > 0) {
            max += amplifier * 300 / 2;
        }
        double add = (e.getTo().getY() - e.getFrom().getY()) / -0.6;
        if (add < 0.0) {
            add = 0.0;
        }
        max += add * 100.0;
        final UserData data = this.getAnticheat().getDataManager().getData(e.getPlayer());
        if (data == null) {
            return;
        }
        final long timez = data.getActualTime();
        final Long aLong = PlayerMove.pstarted.get(e.getPlayer().getUniqueId());
        if (aLong == null) {
            return;
        }
        if (timez - aLong > max) {
            this.getAnticheat().failure(this, e.getPlayer(), "Hovering in air longer than " + max + "ms", "(Type: E)");
            final FixedLoc from = e.getFrom();
            PlayerMove.pstarted.put(e.getPlayer().getUniqueId(), timez);
            final FixedLoc fixedLoc;
            Bukkit.getScheduler().runTask((Plugin)this.getAnticheat(), () -> e.getPlayer().teleport(new Location(e.getTo().getWorld(), fixedLoc.getX(), fixedLoc.getY(), fixedLoc.getZ())));
        }
    }
}
