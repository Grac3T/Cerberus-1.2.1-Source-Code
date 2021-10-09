// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import java.lang.reflect.InvocationTargetException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import xyz.natalczx.cerberus.CerberusAntiCheat;

public class LagAssist implements Runnable
{
    private final CerberusAntiCheat plugin;
    private double tps;
    private long sec;
    private long currentSec;
    private int ticks;
    private Method getHandleMethod;
    private Field pingField;
    
    public LagAssist(final CerberusAntiCheat plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)plugin, (Runnable)this, 1L, 1L);
    }
    
    public boolean shouldCancelTPS() {
        return Settings.IMP.LAG_ASSIST.MAX_TPS >= this.getTPS();
    }
    
    public double getTPS() {
        return Math.min(this.tps + 1.0, 20.0);
    }
    
    @Override
    public void run() {
        this.sec = System.currentTimeMillis() / 1000L;
        if (this.currentSec == this.sec) {
            ++this.ticks;
        }
        else {
            this.currentSec = this.sec;
            this.tps = ((this.tps == 0.0) ? this.ticks : ((this.tps + this.ticks) / 2.0));
            this.ticks = 0;
        }
    }
    
    public int getPing(final Player who) {
        try {
            final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            final Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            final Object handle = craftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(who, new Object[0]);
            return (int)handle.getClass().getDeclaredField("ping").get(handle);
        }
        catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException ex2) {
            final Exception ex;
            final Exception e = ex;
            return -1;
        }
    }
}
