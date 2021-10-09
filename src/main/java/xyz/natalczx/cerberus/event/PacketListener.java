// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.event;

import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventA;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.user.UserDataManager;
import org.bukkit.event.Listener;

public class PacketListener implements Listener
{
    private final UserDataManager dataManager;
    
    public PacketListener() {
        this.dataManager = CerberusAntiCheat.getInstance().getDataManager();
    }
    
    @EventHandler
    public void onPacketPlayerEvent(final PacketPlayerEventA e) {
        final Player p = e.getPlayer();
        final UserData data = this.dataManager.getData(p);
        if (data != null) {
            if (data.getLastPlayerPacketDiff() > 200L) {
                data.setLastDelayedPacket(System.currentTimeMillis());
            }
            data.setLastPlayerPacket(System.currentTimeMillis());
        }
    }
}
