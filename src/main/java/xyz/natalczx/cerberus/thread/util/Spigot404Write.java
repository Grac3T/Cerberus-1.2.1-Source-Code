// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.thread.util;

import com.google.common.collect.Queues;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_8_R3.Packet;
import io.netty.channel.Channel;
import java.util.Queue;

public class Spigot404Write
{
    private static Queue<PacketQueue> queue;
    private static Tasks tasks;
    private final Channel channel;
    
    public Spigot404Write(final Channel channel) {
        this.channel = channel;
    }
    
    public static void writeThenFlush(final Channel channel, final Packet value, final GenericFutureListener<? extends Future<? super Void>>[] listener) {
        final Spigot404Write writer = new Spigot404Write(channel);
        Spigot404Write.queue.add(new PacketQueue(value, (GenericFutureListener[])listener));
        if (Spigot404Write.tasks.addTask()) {
            channel.pipeline().lastContext().executor().execute(writer::writeQueueAndFlush);
        }
    }
    
    public void writeQueueAndFlush() {
        while (Spigot404Write.tasks.fetchTask()) {
            while (Spigot404Write.queue.size() > 0) {
                final PacketQueue messages = Spigot404Write.queue.poll();
                if (messages != null) {
                    final ChannelFuture future = this.channel.write((Object)messages.getPacket());
                    if (messages.getListener() != null) {
                        future.addListeners((GenericFutureListener[])messages.getListener());
                    }
                    future.addListener((GenericFutureListener)ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            }
        }
        this.channel.flush();
    }
    
    static {
        Spigot404Write.queue = (Queue<PacketQueue>)Queues.newConcurrentLinkedQueue();
        Spigot404Write.tasks = new Tasks();
    }
    
    private static class PacketQueue
    {
        private Packet item;
        private GenericFutureListener<? extends Future<? super Void>>[] listener;
        
        private PacketQueue(final Packet item, final GenericFutureListener<? extends Future<? super Void>>[] listener) {
            this.item = item;
            this.listener = listener;
        }
        
        public Packet getPacket() {
            return this.item;
        }
        
        public GenericFutureListener<? extends Future<? super Void>>[] getListener() {
            return this.listener;
        }
    }
}
