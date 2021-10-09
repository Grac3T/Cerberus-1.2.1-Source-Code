// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.EventHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class FalsePackets extends Check
{
    public FalsePackets(final CerberusAntiCheat AntiCheat) {
        super("FalsePackets", "Packets", AntiCheat);
    }
    
    @EventHandler
    public final void PacketPlayer(final PacketPlayerEventB event) {
        final Player player = event.getPlayer();
        final Location loc = player.getLocation().clone();
    }
}
