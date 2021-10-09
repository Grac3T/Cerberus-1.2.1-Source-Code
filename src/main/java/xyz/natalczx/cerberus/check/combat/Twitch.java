// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.packet.PacketPlayerType;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class Twitch extends Check
{
    public Twitch(final CerberusAntiCheat AntiCheat) {
        super("Twitch", "Twitch", AntiCheat);
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void Player(final PacketPlayerEventB e) {
        if (e.getType() != PacketPlayerType.LOOK) {
            return;
        }
        if (e.getPitch() > 90.1f || e.getPitch() < -90.1f) {
            this.getAnticheat().failure(this, e.getPlayer(), null, (String[])null);
        }
    }
}
