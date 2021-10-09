// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api;

import org.bukkit.util.Vector;

public class FixedLoc
{
    private double x;
    private double y;
    private double z;
    
    public FixedLoc(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }
}
