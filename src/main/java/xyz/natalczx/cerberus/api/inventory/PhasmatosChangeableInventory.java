// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.inventory.ItemStack;
import java.util.Map;

public abstract class PhasmatosChangeableInventory implements PhasmatosInventory
{
    private final String title;
    private final int size;
    protected final Map<Integer, ItemStack> items;
    
    public PhasmatosChangeableInventory(final String title, final int size) {
        this.items = new HashMap<Integer, ItemStack>();
        this.title = title;
        this.size = size;
    }
    
    @Override
    public PhasmatosInventory addItem(final int slot, final ItemStack item) {
        this.items.put(slot, item);
        return this;
    }
    
    @Override
    public void open(final Player player) {
        final Inventory inv = Bukkit.createInventory((InventoryHolder)null, this.size, this.title);
        this.getItems().forEach((slot, item) -> inv.setItem((int)slot, this.updateItem(item, slot, player)));
        player.openInventory(inv);
        player.updateInventory();
    }
    
    @Override
    public String getTitle() {
        return this.title;
    }
    
    @Override
    public int getSize() {
        return this.size;
    }
    
    public abstract ItemStack updateItem(final ItemStack p0, final int p1, final Player p2);
}
