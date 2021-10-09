// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import xyz.natalczx.cerberus.api.FixedLoc;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.events.FlyMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class Spider extends Check
{
    public Spider(final CerberusAntiCheat AntiCheat) {
        super("Spider", "Spider (WallClimb)", AntiCheat);
        this.setViolationResetTime(1000L);
        this.setViolationsToNotify(2);
    }
    
    @EventHandler
    public void onFly(final FlyMoveEvent e) {
        if (!e.isArounding()) {
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
        CerberusAntiCheat.getInstance().failure(this, e.getPlayer(), "Cosplaying spider.", "(Type: A)");
    }
}
