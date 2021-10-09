// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.event;

import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.user.UserDataManager;
import org.bukkit.event.Listener;

public class UtilityJoinQuitEvent implements Listener
{
    private final UserDataManager dataManager;
    
    public UtilityJoinQuitEvent() {
        this.dataManager = CerberusAntiCheat.getInstance().getDataManager();
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        this.dataManager.addPlayerData(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(final PlayerQuitEvent e) {
        this.dataManager.removePlayerData(e.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onKick(final PlayerKickEvent e) {
        this.dataManager.removePlayerData(e.getPlayer());
    }
}
