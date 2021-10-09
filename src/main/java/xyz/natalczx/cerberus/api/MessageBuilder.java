// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.api;

import org.bukkit.ChatColor;
import org.apache.commons.lang.StringUtils;

public class MessageBuilder
{
    private String text;
    
    public MessageBuilder(final String text) {
        this.text = text;
    }
    
    public static MessageBuilder newBuilder(final String text) {
        return new MessageBuilder(text);
    }
    
    public MessageBuilder withField(final String field, final String value) {
        this.text = StringUtils.replace(this.text, field, value);
        return this;
    }
    
    public MessageBuilder coloured() {
        this.text = ChatColor.translateAlternateColorCodes('&', this.text);
        return this;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}
