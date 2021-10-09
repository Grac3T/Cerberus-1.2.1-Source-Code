// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class SneakB extends Check
{
    public SneakB(final CerberusAntiCheat AntiCheat) {
        super("SneakB", "Sneak", AntiCheat);
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (player.isSneaking()) {
            this.getAnticheat().failure(this, player, "Typing on chat while sneaking/shifting", "(Type: B)");
        }
    }
    
    @EventHandler
    public void onCraft(final CraftItemEvent event) {
        Player player;
        this.async(() -> {
            if (!(!(event.getWhoClicked() instanceof Player))) {
                player = (Player)event.getWhoClicked();
                if (player.isSneaking()) {
                    this.getAnticheat().failure(this, player, "Crafting while sneaking", "(Type: C)");
                }
            }
        });
    }
}
