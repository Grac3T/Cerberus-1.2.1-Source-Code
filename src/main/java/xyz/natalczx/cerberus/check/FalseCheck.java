// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check;

import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.UtilCheat;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FalseCheck
{
    public static boolean fly(final Player p, final Location from, final Location to) {
        if (!main()) {
            return false;
        }
        if (p == null) {
            return false;
        }
        try {
            if (p instanceof TemporaryPlayer) {
                return false;
            }
        }
        catch (Exception ex) {}
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        return data != null && System.currentTimeMillis() - data.getJoinTime() >= 2000L && System.currentTimeMillis() - data.getLastBlockPlace() >= 2000L && System.currentTimeMillis() - data.getLastExplode() >= 2000L && (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ()) && !p.getAllowFlight() && p.getVehicle() == null && CerberusAntiCheat.getInstance().getLagAssist().getTPS() >= Settings.IMP.LAG_ASSIST.MAX_TPS && !UtilsB.isInWater(p) && !UtilCheat.isInWeb(p);
    }
    
    private static boolean main() {
        return CerberusAntiCheat.getInstance().isEnabled();
    }
}
