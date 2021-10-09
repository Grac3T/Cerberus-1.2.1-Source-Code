// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventPriority;
import java.util.AbstractMap;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraE extends Check
{
    private final Map<Player, Map.Entry<Integer, Long>> lastAttack;
    
    public KillAuraE(final CerberusAntiCheat AntiCheat) {
        super("KillAuraE", "KillAura", AntiCheat);
        this.lastAttack = new HashMap<Player, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(false);
        this.setViolationsToNotify(2);
        this.setMaxViolations(7);
        this.setViolationResetTime(1800000L);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        this.async(() -> this.lastAttack.remove(e.getPlayer()));
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void Damage(final EntityDamageByEntityEvent e) {
        Player player;
        Integer entityid;
        Long time;
        this.async(() -> {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                player = (Player)e.getDamager();
                if (this.lastAttack.containsKey(player)) {
                    entityid = this.lastAttack.get(player).getKey();
                    time = this.lastAttack.get(player).getValue();
                    if (entityid != e.getEntity().getEntityId() && System.currentTimeMillis() - time < 6L) {
                        this.getAnticheat().failure(this, player, "MultiAura (E)", "(Type: E)");
                    }
                    this.lastAttack.remove(player);
                }
                else {
                    this.lastAttack.put(player, new AbstractMap.SimpleEntry<Integer, Long>(e.getEntity().getEntityId(), System.currentTimeMillis()));
                }
            }
        });
    }
}
