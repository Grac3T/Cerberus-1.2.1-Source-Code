// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.thread;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

public abstract class AbstractThread
{
    protected Queue<Runnable> packets;
    private boolean running;
    private int TICK_TIME;
    private Thread t;
    
    public AbstractThread() {
        this.running = false;
        this.TICK_TIME = 50000000;
        this.packets = new ConcurrentLinkedQueue<Runnable>();
        this.running = true;
        (this.t = new Thread(this::loop)).start();
    }
    
    public void loop() {
        long lastTick = System.nanoTime();
        long catchupTime = 0L;
        while (this.running) {
            final long curTime = System.nanoTime();
            final long wait = this.TICK_TIME - (curTime - lastTick) - catchupTime;
            if (wait > 0L) {
                try {
                    Thread.sleep(wait / 1000000L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catchupTime = 0L;
            }
            else {
                catchupTime = Math.min(1000000000L, Math.abs(wait));
                try {
                    this.run();
                }
                catch (Throwable ex) {
                    ex.printStackTrace();
                }
                lastTick = curTime;
            }
        }
        this.t.interrupt();
    }
    
    public void stop() {
        this.running = false;
    }
    
    public abstract void run();
    
    public void addPacket(final Runnable runnable) {
        this.packets.add(runnable);
    }
    
    public Thread getThread() {
        return this.t;
    }
}
