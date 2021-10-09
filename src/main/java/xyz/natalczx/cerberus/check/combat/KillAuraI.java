// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraI extends Check
{
    Map<UUID, Integer> hits;
    
    public KillAuraI(final CerberusAntiCheat AntiCheat) {
        super("KillAuraI", "KillAura", AntiCheat);
        this.hits = new HashMap<UUID, Integer>();
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        int n;
        Player player;
        int n2;
        int n3;
        int n4;
        int n5;
        this.async(() -> {
            if (!(!(entityDamageByEntityEvent.getDamager() instanceof Player))) {
                if (!(!(entityDamageByEntityEvent.getEntity() instanceof Player))) {
                    if (!((Player)entityDamageByEntityEvent.getDamager()).hasLineOfSight(entityDamageByEntityEvent.getEntity()) && !this.isPlayerInCorner((Player)entityDamageByEntityEvent.getDamager())) {
                        n = 0;
                        player = (Player)entityDamageByEntityEvent.getDamager();
                        this.hits.putIfAbsent(entityDamageByEntityEvent.getDamager().getUniqueId(), 1);
                        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 5) {
                            n2 = 1;
                            this.getAnticheat().failure(this, player, ChatColor.RED + "Experemental1 [0x01]", "(Type: I)");
                        }
                        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 10) {
                            n3 = 2;
                            this.getAnticheat().failure(this, player, ChatColor.RED + "Experemental2 [0x01]", "(Type: I)");
                        }
                        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) > 19) {
                            n4 = 3;
                            this.getAnticheat().failure(this, player, ChatColor.RED + "Experemental3 [0x01]", "(Type: I)");
                            this.hits.remove(entityDamageByEntityEvent.getDamager().getUniqueId());
                            n5 = 0;
                        }
                    }
                }
            }
        });
    }
    
    public boolean isPlayerInCorner(final Player player) {
        float f = player.getLocation().clone().getYaw();
        if (f < 0.0f) {
            f += 360.0f;
        }
        final int n;
        return (n = (int)(((f %= 360.0f) + 8.0f) / 22.5)) != 0 && n != 4 && n != 8 && n != 12;
    }
}
