// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.thread.util;

import java.util.concurrent.atomic.AtomicInteger;

public class Tasks
{
    private final AtomicInteger state;
    
    public Tasks() {
        this.state = new AtomicInteger();
    }
    
    public boolean fetchTask() {
        final int old = this.state.getAndDecrement();
        if (old == State.RUNNING_GOT_TASKS.ordinal()) {
            return true;
        }
        if (old == State.RUNNING_NO_TASKS.ordinal()) {
            return false;
        }
        throw new AssertionError();
    }
    
    public boolean addTask() {
        if (this.state.get() == State.RUNNING_GOT_TASKS.ordinal()) {
            return false;
        }
        final int old = this.state.getAndSet(State.RUNNING_GOT_TASKS.ordinal());
        return old == State.WAITING.ordinal();
    }
    
    private enum State
    {
        WAITING, 
        RUNNING_NO_TASKS, 
        RUNNING_GOT_TASKS;
    }
}
