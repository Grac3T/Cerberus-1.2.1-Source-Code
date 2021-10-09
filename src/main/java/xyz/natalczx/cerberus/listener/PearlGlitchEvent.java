// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import xyz.natalczx.cerberus.helper.PearlGlitchType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public class PearlGlitchEvent extends Event implements Cancellable
{
    private static HandlerList handlers;
    private Player player;
    private Location from;
    private Location to;
    private ItemStack pearls;
    private PearlGlitchType type;
    private boolean cancelled;
    
    public PearlGlitchEvent(final Player player, final Location from, final Location to, final ItemStack pearls, final PearlGlitchType type) {
        this.cancelled = false;
        this.player = player;
        this.from = from;
        this.to = to;
        this.pearls = pearls;
        this.type = type;
    }
    
    public static HandlerList getHandlerList() {
        return PearlGlitchEvent.handlers;
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
    
    public ItemStack getItems() {
        return this.pearls;
    }
    
    public PearlGlitchType getType() {
        return this.type;
    }
    
    public boolean isType(final PearlGlitchType type) {
        return type == this.type;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public HandlerList getHandlers() {
        return PearlGlitchEvent.handlers;
    }
    
    static {
        PearlGlitchEvent.handlers = new HandlerList();
    }
}
