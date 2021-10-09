// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import java.util.Collection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.List;

public class PhasmatosBukkitInventoryAPI implements PhasmatosInventoryAPI
{
    private final List<PhasmatosInventory> inventories;
    
    public PhasmatosBukkitInventoryAPI() {
        this.inventories = new ArrayList<PhasmatosInventory>();
    }
    
    public void register(final Plugin plugin) {
        final PluginManager pluginManager = plugin.getServer().getPluginManager();
        if (plugin.getServer().getVersion().contains("1.14")) {
            pluginManager.registerEvents((Listener)new PhasmatosInventory1_14Listeners(this), plugin);
        }
        else {
            pluginManager.registerEvents((Listener)new PhasmatosInventoryOlderListeners(this), plugin);
        }
    }
    
    @Override
    public PhasmatosInventory findByTitle(final String title) {
        return this.inventories.stream().filter(inventory -> inventory.getTitle().equalsIgnoreCase(title)).findFirst().orElse(null);
    }
    
    @Override
    public PhasmatosInventory findByTitleAndSize(final String title, final int size) {
        return this.inventories.stream().filter(inventory -> inventory.getTitle().equalsIgnoreCase(title) && inventory.getSize() == size).findFirst().orElse(null);
    }
    
    @Override
    public void addInventory(final PhasmatosInventory inventory) {
        this.inventories.add(inventory);
    }
    
    @Override
    public List<PhasmatosInventory> getInventories() {
        return new ArrayList<PhasmatosInventory>(this.inventories);
    }
}
