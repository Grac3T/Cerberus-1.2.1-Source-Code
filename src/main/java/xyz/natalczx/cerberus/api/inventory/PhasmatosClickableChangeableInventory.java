// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.HashMap;
import org.bukkit.entity.Player;
import java.util.function.Consumer;
import java.util.Map;

public abstract class PhasmatosClickableChangeableInventory extends PhasmatosChangeableInventory implements PhasmatosClickableInventory
{
    protected final Map<Integer, Consumer<Player>> itemActions;
    protected final Map<Integer, Consumer<Player>> rightClickActions;
    
    public PhasmatosClickableChangeableInventory(final String title, final int size) {
        super(title, size);
        this.itemActions = new HashMap<Integer, Consumer<Player>>();
        this.rightClickActions = new HashMap<Integer, Consumer<Player>>();
    }
    
    @Override
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        event.setCancelled(true);
        if (event.isLeftClick()) {
            if (this.itemActions.containsKey(event.getSlot())) {
                this.itemActions.get(event.getSlot()).accept((Player)event.getWhoClicked());
            }
        }
        else if (this.rightClickActions.containsKey(event.getSlot())) {
            this.rightClickActions.get(event.getSlot()).accept((Player)event.getWhoClicked());
        }
    }
    
    public PhasmatosClickableInventory addRightClickAction(final int slot, final Consumer<Player> action) {
        this.rightClickActions.put(slot, action);
        return this;
    }
    
    @Override
    public PhasmatosClickableInventory addItemAction(final int slot, final Consumer<Player> action) {
        this.itemActions.put(slot, action);
        return this;
    }
    
    @Override
    public void removeItemAction(final int slot) {
        this.itemActions.remove(slot);
    }
}
