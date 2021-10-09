// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import xyz.natalczx.cerberus.packet.PacketPlayerType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketKillauraEvent extends Event
{
    private static final HandlerList handlers;
    private Player Player;
    private PacketPlayerType type;
    
    public PacketKillauraEvent(final Player Player, final PacketPlayerType type) {
        this.Player = Player;
        this.type = type;
    }
    
    public static HandlerList getHandlerList() {
        return PacketKillauraEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.Player;
    }
    
    public PacketPlayerType getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return PacketKillauraEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
