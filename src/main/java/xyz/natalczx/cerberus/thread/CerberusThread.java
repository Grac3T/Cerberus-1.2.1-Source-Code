// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.thread;

public class CerberusThread extends AbstractThread
{
    @Override
    public void run() {
        while (this.packets.size() > 0) {
            this.packets.poll().run();
        }
    }
}
