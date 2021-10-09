// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.natalczx.cerberus.check.Check;

public class VapeCracked extends Check implements PluginMessageListener
{
    public VapeCracked(final CerberusAntiCheat AntiCheat) {
        super("Vape", "Vape", AntiCheat);
    }
    
    public void onPluginMessageReceived(final String s, final Player player, final byte[] data) {
        this.getAnticheat().failure(this, player, "Using Cracked Vape!", "Vape");
    }
}
