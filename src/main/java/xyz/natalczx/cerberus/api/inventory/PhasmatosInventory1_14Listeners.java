// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public class PhasmatosInventory1_14Listeners implements Listener
{
    private final PhasmatosInventoryAPI inventoryAPI;
    
    public PhasmatosInventory1_14Listeners(final PhasmatosInventoryAPI inventoryAPI) {
        this.inventoryAPI = inventoryAPI;
    }
    
    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        final Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }
        final PhasmatosInventory phasmatosInventory = this.inventoryAPI.findByTitleAndSize(event.getView().getTitle(), inventory.getSize());
        if (phasmatosInventory instanceof PhasmatosClickableInventory) {
            ((PhasmatosClickableInventory)phasmatosInventory).onClick(event);
        }
    }
}
