// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api.inventory;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import org.bukkit.ChatColor;

public final class MessageHelper
{
    private MessageHelper() {
    }
    
    public static String colored(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    public static List<String> colored(final List<String> texts) {
        return texts.stream().map((Function<? super Object, ?>)MessageHelper::colored).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
    }
}
