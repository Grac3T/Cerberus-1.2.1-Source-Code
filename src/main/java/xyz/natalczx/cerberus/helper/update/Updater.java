// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper.update;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.CerberusAntiCheat;

public class Updater implements Runnable
{
    private CerberusAntiCheat AntiCheat;
    private int updater;
    
    public Updater(final CerberusAntiCheat AntiCheat) {
        this.AntiCheat = AntiCheat;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this.AntiCheat, (Runnable)this, 0L, 1L);
    }
    
    public void Disable() {
        Bukkit.getScheduler().cancelTask(this.updater);
    }
    
    @Override
    public void run() {
        for (final UpdateType updateType : UpdateType.values()) {
            if (updateType != null && updateType.Elapsed()) {
                try {
                    final UpdateEvent event = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent((Event)event);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
