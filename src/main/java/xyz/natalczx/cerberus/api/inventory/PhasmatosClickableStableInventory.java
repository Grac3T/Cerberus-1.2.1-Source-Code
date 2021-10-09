// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.function.Consumer;
import java.util.Map;

public class PhasmatosClickableStableInventory extends PhasmatosStableInventory implements PhasmatosClickableInventory
{
    private final Map<Integer, Consumer<Player>> itemActions;
    
    public PhasmatosClickableStableInventory(final String title, final int size) {
        super(title, size);
        this.itemActions = new HashMap<Integer, Consumer<Player>>();
    }
    
    @Override
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        if (this.itemActions.containsKey(event.getSlot())) {
            this.itemActions.get(event.getSlot()).accept((Player)event.getWhoClicked());
        }
    }
    
    @Override
    public void removeItemAction(final int slot) {
        this.itemActions.remove(slot);
    }
    
    @Override
    public PhasmatosClickableInventory addItemAction(final int slot, final Consumer<Player> action) {
        this.itemActions.put(slot, action);
        return this;
    }
}
