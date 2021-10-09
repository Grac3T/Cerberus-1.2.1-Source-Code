// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import java.util.List;

public interface PhasmatosInventoryAPI
{
    void addInventory(final PhasmatosInventory p0);
    
    List<PhasmatosInventory> getInventories();
    
    PhasmatosInventory findByTitle(final String p0);
    
    PhasmatosInventory findByTitleAndSize(final String p0, final int p1);
}
