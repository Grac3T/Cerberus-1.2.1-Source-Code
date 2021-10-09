// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check;

import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.thread.CerberusThread;
import org.bukkit.event.Listener;

public class Check implements Listener
{
    private static final CerberusThread THREAD;
    private String identifier;
    private String name;
    private CerberusAntiCheat anticheat;
    private boolean enabled;
    private boolean bannable;
    private int maxViolations;
    private int violationsToNotify;
    private long violationResetTime;
    
    public Check(final String Identifier, final String Name, final CerberusAntiCheat anticheat) {
        this.enabled = true;
        this.bannable = true;
        this.maxViolations = 5;
        this.violationsToNotify = 1;
        this.violationResetTime = 600000L;
        this.name = Name;
        this.anticheat = anticheat;
        this.identifier = Identifier;
    }
    
    protected void sync(final Runnable runnable) {
        this.anticheat.getServer().getScheduler().runTask((Plugin)this.anticheat, runnable);
    }
    
    protected void async(final Runnable runnable) {
        Check.THREAD.addPacket(runnable);
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public boolean isBannable() {
        return this.bannable;
    }
    
    public CerberusAntiCheat getAnticheat() {
        return this.anticheat;
    }
    
    public int getMaxViolations() {
        return this.maxViolations;
    }
    
    public int getViolationsToNotify() {
        return this.violationsToNotify;
    }
    
    public void setViolationsToNotify(final int ViolationsToNotify) {
        this.violationsToNotify = ViolationsToNotify;
    }
    
    public Long getViolationResetTime() {
        return this.violationResetTime;
    }
    
    public void setViolationResetTime(final long ViolationResetTime) {
        this.violationResetTime = ViolationResetTime;
    }
    
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setBannable(final boolean bannable) {
        this.bannable = bannable;
    }
    
    public void setMaxViolations(final int maxViolations) {
        this.maxViolations = maxViolations;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getIdentifier() {
        return this.identifier;
    }
    
    static {
        THREAD = CerberusAntiCheat.getInstance().getCerberusThread();
    }
}
