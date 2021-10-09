// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.task;

import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.config.Settings;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;

public class ViolationsResetTask implements Runnable
{
    private final CerberusAntiCheat antiCheat;
    
    public ViolationsResetTask(final CerberusAntiCheat antiCheat) {
        this.antiCheat = antiCheat;
    }
    
    public static void start(final CerberusAntiCheat plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)plugin, (Runnable)new ViolationsResetTask(plugin), 0L, TimeUnit.SECONDS.toMillis(Settings.IMP.MESSAGES.VIOLATIONS_RESET_TIME));
    }
    
    @Override
    public void run() {
        this.antiCheat.getViolationManager().resetAllViolations();
    }
}
