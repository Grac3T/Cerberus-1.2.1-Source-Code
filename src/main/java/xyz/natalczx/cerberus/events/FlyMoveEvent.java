// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.events;

import org.bukkit.Location;
import xyz.natalczx.cerberus.api.FixedLoc;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class FlyMoveEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private FixedLoc from;
    private Location to;
    private int amplifier;
    private int amplifiers;
    boolean arounding;
    
    public FlyMoveEvent(final Player player, final FixedLoc from, final Location to, final int amplifier, final int amplifiers, final boolean arounding) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.amplifier = amplifier;
        this.arounding = arounding;
        this.amplifiers = amplifiers;
    }
    
    public static HandlerList getHandlerList() {
        return FlyMoveEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public FixedLoc getFrom() {
        return this.from;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public HandlerList getHandlers() {
        return FlyMoveEvent.handlers;
    }
    
    public int getAmplifier() {
        return this.amplifier;
    }
    
    public boolean isArounding() {
        return this.arounding;
    }
    
    public int getAmplifiers() {
        return this.amplifiers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
