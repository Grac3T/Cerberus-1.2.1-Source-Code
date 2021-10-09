// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketEntityActionEvent extends Event
{
    private static final HandlerList handlers;
    public int Action;
    public Player Player;
    
    public PacketEntityActionEvent(final Player Player, final int Action) {
        this.Player = Player;
        this.Action = Action;
    }
    
    public static HandlerList getHandlerList() {
        return PacketEntityActionEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public int getAction() {
        return this.Action;
    }
    
    public HandlerList getHandlers() {
        return PacketEntityActionEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
    
    public class PlayerAction
    {
    }
}
