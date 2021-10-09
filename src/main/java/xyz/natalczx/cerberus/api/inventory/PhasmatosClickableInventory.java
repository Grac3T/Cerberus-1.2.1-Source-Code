// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.entity.Player;
import java.util.function.Consumer;

public interface PhasmatosClickableInventory
{
    PhasmatosClickableInventory addItemAction(final int p0, final Consumer<Player> p1);
    
    void removeItemAction(final int p0);
    
    void onClick(final InventoryClickEvent p0);
}
