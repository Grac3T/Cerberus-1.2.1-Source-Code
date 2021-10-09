// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketHeldItemChangeEvent extends Event
{
    private static final HandlerList handlers;
    public Player Player;
    public PacketEvent Event;
    
    public PacketHeldItemChangeEvent(final PacketEvent Event, final Player Player) {
        this.Player = Player;
        this.Event = Event;
    }
    
    public static HandlerList getHandlerList() {
        return PacketHeldItemChangeEvent.handlers;
    }
    
    public PacketEvent getPacketEvent() {
        return this.Event;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public HandlerList getHandlers() {
        return PacketHeldItemChangeEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
