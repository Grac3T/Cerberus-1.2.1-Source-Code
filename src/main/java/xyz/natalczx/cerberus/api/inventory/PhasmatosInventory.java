// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public interface PhasmatosInventory
{
    void open(final Player p0);
    
    PhasmatosInventory addItem(final int p0, final ItemStack p1);
    
    Map<Integer, ItemStack> getItems();
    
    String getTitle();
    
    int getSize();
}
