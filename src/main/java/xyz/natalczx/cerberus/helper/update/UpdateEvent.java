// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.update;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class UpdateEvent extends Event
{
    private static final HandlerList handlers;
    private UpdateType Type;
    
    public UpdateEvent(final UpdateType Type) {
        this.Type = Type;
    }
    
    public static HandlerList getHandlerList() {
        return UpdateEvent.handlers;
    }
    
    public UpdateType getType() {
        return this.Type;
    }
    
    public HandlerList getHandlers() {
        return UpdateEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
