// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import java.util.HashMap;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AutoArmor extends Check
{
    private static final Map<UUID, Long> lastarmorclick;
    private static final Map<UUID, Long> lastclick;
    private static final Map<UUID, Long> lastopen;
    
    public AutoArmor(final CerberusAntiCheat AntiCheat) {
        super("AutoArmor", "AutoArmor", AntiCheat);
        this.setViolationResetTime(TimeUnit.SECONDS.toMillis(90L));
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        this.async(() -> {
            AutoArmor.lastarmorclick.put(e.getPlayer().getUniqueId(), 0L);
            AutoArmor.lastclick.put(e.getPlayer().getUniqueId(), 0L);
            AutoArmor.lastopen.put(e.getPlayer().getUniqueId(), 0L);
        });
    }
    
    @EventHandler(ignoreCancelled = true)
    public void itemDurabilityChange(final PlayerItemDamageEvent e) {
        final Player player = e.getPlayer();
        if (player == null) {
            return;
        }
        final ItemStack helmet = player.getInventory().getHelmet();
        final ItemStack chestplate = player.getInventory().getChestplate();
        final ItemStack leggings = player.getInventory().getLeggings();
        final ItemStack boots = player.getInventory().getBoots();
        final Player player2;
        final UserData data;
        ItemStack[] item;
        final Player player3;
        final ItemStack itemStack;
        final Object o;
        final ItemStack itemStack2;
        final ItemStack itemStack3;
        final ItemStack itemStack4;
        this.async(() -> {
            data = this.getAnticheat().getDataManager().getData(player2);
            if (data != null) {
                if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                    if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                        item = new ItemStack[] { e.getItem() };
                        if (item[0].getDurability() + e.getDamage() > item[0].getType().getMaxDurability()) {
                            Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> {
                                if (System.currentTimeMillis() - AutoArmor.lastopen.get(player3.getUniqueId()) <= 300L) {
                                    if (itemStack != null && itemStack.isSimilar(o[0])) {
                                        o[0] = player3.getInventory().getHelmet();
                                    }
                                    else if (itemStack2 != null && itemStack2.isSimilar(o[0])) {
                                        o[0] = player3.getInventory().getChestplate();
                                    }
                                    else if (itemStack3 != null && itemStack3.isSimilar(o[0])) {
                                        o[0] = player3.getInventory().getLeggings();
                                    }
                                    else if (itemStack4 != null && itemStack4.isSimilar(o[0])) {
                                        o[0] = player3.getInventory().getBoots();
                                    }
                                    if (o[0] != null) {
                                        this.getAnticheat().failure(this, player3, "Too fast.", new String[0]);
                                    }
                                }
                            }, 5L);
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void onInvOpen(final InventoryOpenEvent e) {
        this.async(() -> AutoArmor.lastopen.put(e.getPlayer().getUniqueId(), System.currentTimeMillis()));
    }
    
    @EventHandler
    public void onInvClick(final InventoryClickEvent event) {
        Player player;
        UserData data;
        int slot;
        this.async(() -> {
            if (!(!(event.getWhoClicked() instanceof Player))) {
                player = (Player)event.getWhoClicked();
                if (!this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                    data = this.getAnticheat().getDataManager().getData(player);
                    if (data != null) {
                        slot = event.getSlot();
                        AutoArmor.lastclick.put(player.getUniqueId(), System.currentTimeMillis());
                        if (slot >= 36 && slot <= 39) {
                            AutoArmor.lastarmorclick.put(player.getUniqueId(), System.currentTimeMillis());
                        }
                    }
                }
            }
        });
    }
    
    static {
        lastarmorclick = new HashMap<UUID, Long>();
        lastclick = new HashMap<UUID, Long>();
        lastopen = new HashMap<UUID, Long>();
    }
}
