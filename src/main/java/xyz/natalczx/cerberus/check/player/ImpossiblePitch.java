// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class ImpossiblePitch extends Check
{
    public ImpossiblePitch(final CerberusAntiCheat AntiCheat) {
        super("ImpossiblePitch", "ImpossiblePitch", AntiCheat);
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        final double Pitch;
        this.async(() -> {
            Pitch = e.getPlayer().getLocation().clone().getPitch();
            if (Pitch > 90.0 || Pitch < -90.0) {
                this.getAnticheat().failure(this, e.getPlayer(), "Players head went back too far. P:[" + Pitch + "]", (String[])null);
            }
        });
    }
}
