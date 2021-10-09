// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.WeakHashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AutoClickerC extends Check
{
    private final Map<UUID, Long> cpsMS;
    private final Map<UUID, Double[]> cps;
    private final Map<UUID, Long> lastTickWithPacketSent;
    private final Map<UUID, Boolean> lastPacketTick;
    private final Map<UUID, Long> packetHitsSinceLastCheck;
    private final Map<UUID, Long> lastCheckedTick;
    private final Map<UUID, Long> hitsSinceLastCheck;
    
    public AutoClickerC(final CerberusAntiCheat AntiCheat) {
        super("AutoClickerC", "AutoClicker", AntiCheat);
        this.cpsMS = new WeakHashMap<UUID, Long>();
        this.cps = new WeakHashMap<UUID, Double[]>();
        this.lastTickWithPacketSent = new WeakHashMap<UUID, Long>();
        this.lastPacketTick = new WeakHashMap<UUID, Boolean>();
        this.packetHitsSinceLastCheck = new WeakHashMap<UUID, Long>();
        this.lastCheckedTick = new WeakHashMap<UUID, Long>();
        this.hitsSinceLastCheck = new WeakHashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                this.analyzeDouble((Player)entityDamageByEntityEvent.getDamager());
            }
        });
    }
    
    public void analyzeDouble(final Player player) {
        final UUID uUID = player.getUniqueId();
        if (this.cpsMS.containsKey(uUID) && System.currentTimeMillis() - this.cpsMS.get(player.getUniqueId()) <= 1L) {
            this.getAnticheat().failure(this, player, ChatColor.RED + "(Type: A)", "(Type: A)");
        }
        this.cpsMS.put(uUID, System.currentTimeMillis());
    }
    
    @EventHandler
    public void onDisconnect(final PlayerQuitEvent playerQuitEvent) {
        final UUID uUID;
        this.async(() -> {
            uUID = playerQuitEvent.getPlayer().getUniqueId();
            this.packetHitsSinceLastCheck.remove(uUID);
            this.lastCheckedTick.remove(uUID);
            this.lastPacketTick.remove(uUID);
            this.lastTickWithPacketSent.remove(uUID);
            this.hitsSinceLastCheck.remove(uUID);
            this.cps.remove(uUID);
            this.cpsMS.remove(uUID);
        });
    }
}
