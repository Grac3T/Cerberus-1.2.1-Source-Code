// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class RealMoveEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private Location from;
    private Location to;
    
    public RealMoveEvent(final Player player, final Location from, final Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }
    
    public static HandlerList getHandlerList() {
        return RealMoveEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public HandlerList getHandlers() {
        return RealMoveEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
