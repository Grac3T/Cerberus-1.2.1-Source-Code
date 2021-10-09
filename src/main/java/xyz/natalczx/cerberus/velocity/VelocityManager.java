// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.velocity;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerVelocityEvent;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class VelocityManager implements Listener
{
    private final Map<UUID, Long> lastVelocity;
    
    public VelocityManager() {
        this.lastVelocity = new HashMap<UUID, Long>();
    }
    
    public Map<UUID, Long> getLastVelocity() {
        return this.lastVelocity;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVelocity(final PlayerVelocityEvent event) {
        final UUID uniqueId = event.getPlayer().getUniqueId();
        this.lastVelocity.put(uniqueId, System.currentTimeMillis());
    }
}
