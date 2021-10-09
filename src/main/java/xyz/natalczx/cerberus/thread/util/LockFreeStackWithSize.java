// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.thread.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStackWithSize<T>
{
    private static final Node<?> tail;
    private final AtomicReference<Node<T>> root;
    
    public LockFreeStackWithSize() {
        this.root = new AtomicReference<Node<T>>((Node<T>)LockFreeStackWithSize.tail);
    }
    
    public boolean add(final T value) {
        final Node<T> newRoot = new Node<T>();
        ((Node<Object>)newRoot).payload = value;
        Node<T> oldRoot;
        do {
            oldRoot = this.root.get();
            ((Node<Object>)newRoot).next = (Node<Object>)oldRoot;
            ((Node<Object>)newRoot).size = ((Node<Object>)oldRoot).size + 1;
        } while (!this.root.compareAndSet(oldRoot, newRoot));
        return true;
    }
    
    public int size() {
        return ((Node<Object>)this.root.get()).size;
    }
    
    public List<T> removeAllReversed() {
        final List<T> result = new ArrayList<T>(this.size() + 100);
        Node<T> r;
        do {
            r = this.root.get();
        } while (!this.root.compareAndSet(r, (Node<T>)LockFreeStackWithSize.tail));
        while (r != LockFreeStackWithSize.tail) {
            result.add((T)((Node<Object>)r).payload);
            r = (Node<T>)((Node<Object>)r).next;
        }
        return result;
    }
    
    static {
        tail = new Node<Object>();
    }
    
    private static class Node<T>
    {
        private volatile Node<T> next;
        private int size;
        private T payload;
    }
}
