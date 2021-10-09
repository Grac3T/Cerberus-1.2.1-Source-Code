// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.helper;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.lang.reflect.Field;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.natalczx.cerberus.CerberusAntiCheat;

public class Ping
{
    private double tps;
    
    public Ping(final CerberusAntiCheat Dreya) {
        new BukkitRunnable() {
            long sec;
            long currentSec;
            int ticks;
            
            public void run() {
                this.sec = System.currentTimeMillis() / 1000L;
                if (this.currentSec == this.sec) {
                    ++this.ticks;
                }
                else {
                    this.currentSec = this.sec;
                    Ping.this.tps = ((Ping.this.tps == 0.0) ? this.ticks : ((Ping.this.tps + this.ticks) / 2.0));
                    this.ticks = 0;
                }
            }
        }.runTaskTimerAsynchronously((Plugin)Dreya, 1L, 1L);
    }
    
    public static Object getFieldValue(final Object instance, final String fieldName) throws Exception {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
    
    public static int getPing(final Player who) {
        try {
            final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            final Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            final Object handle = craftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]).invoke(who, new Object[0]);
            final Integer ping = (Integer)handle.getClass().getDeclaredField("ping").get(handle);
            return ping;
        }
        catch (Exception e) {
            return -1;
        }
    }
    
    public double getTPS() {
        return (this.tps + 1.0 > 20.0) ? 20.0 : (this.tps + 1.0);
    }
}
