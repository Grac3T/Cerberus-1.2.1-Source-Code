// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class PhasmatosStableInventory implements PhasmatosInventory
{
    private final String name;
    private final Inventory inventory;
    
    public PhasmatosStableInventory(final String title, final int size) {
        this.name = title;
        this.inventory = Bukkit.createInventory((InventoryHolder)null, size, title);
    }
    
    @Override
    public PhasmatosInventory addItem(final int slot, final ItemStack item) {
        this.inventory.setItem(slot, item);
        return this;
    }
    
    @Override
    public Map<Integer, ItemStack> getItems() {
        final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
        for (int i = 0; i < this.inventory.getSize(); ++i) {
            final ItemStack item = this.inventory.getItem(i);
            if (item != null) {
                items.put(i, item);
            }
        }
        return items;
    }
    
    @Override
    public void open(final Player player) {
        player.openInventory(this.inventory);
    }
    
    @Override
    public int getSize() {
        return this.inventory.getSize();
    }
    
    @Override
    public String getTitle() {
        return this.name;
    }
}
