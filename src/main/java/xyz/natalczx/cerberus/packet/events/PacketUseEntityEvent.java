// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Event;

public class PacketUseEntityEvent extends Event
{
    private static final HandlerList handlers;
    public EnumWrappers.EntityUseAction Action;
    public Player Attacker;
    public Entity Attacked;
    
    public PacketUseEntityEvent(final EnumWrappers.EntityUseAction Action, final Player Attacker, final Entity Attacked) {
        this.Action = Action;
        this.Attacker = Attacker;
        this.Attacked = Attacked;
    }
    
    public static HandlerList getHandlerList() {
        return PacketUseEntityEvent.handlers;
    }
    
    public EnumWrappers.EntityUseAction getAction() {
        return this.Action;
    }
    
    public Player getAttacker() {
        return this.Attacker;
    }
    
    public Entity getAttacked() {
        return this.Attacked;
    }
    
    public HandlerList getHandlers() {
        return PacketUseEntityEvent.handlers;
    }
    
    static {
        handlers = new HandlerList();
    }
}
