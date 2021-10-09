// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketKeepAliveEvent extends Event
{
    private static final HandlerList handlers;
    public Player Player;
    
    public PacketKeepAliveEvent(final Player Player) {
        this.Player = Player;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKeepAliveEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public HandlerList getHandlers() {
        return PacketKeepAliveEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
