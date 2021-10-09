// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import xyz.natalczx.cerberus.packet.PacketPlayerType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketAttackEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private Entity entity;
    private PacketPlayerType type;
    
    public PacketAttackEvent(final Player player, final Entity entity, final PacketPlayerType type) {
        this.player = player;
        this.entity = entity;
        this.type = type;
    }
    
    public static HandlerList getHandlerList() {
        return PacketAttackEvent.handlers;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public PacketPlayerType getType() {
        return this.type;
    }
    
    public HandlerList getHandlers() {
        return PacketAttackEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
