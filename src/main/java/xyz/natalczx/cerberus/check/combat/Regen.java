// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventPriority;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.Difficulty;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class Regen extends Check
{
    private final Map<UUID, Long> LastHeal;
    private final Map<UUID, Map.Entry<Integer, Long>> FastHealTicks;
    
    public Regen(final CerberusAntiCheat AntiCheat) {
        super("Regen", "Regen", AntiCheat);
        this.LastHeal = new HashMap<UUID, Long>();
        this.FastHealTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationsToNotify(3);
        this.setMaxViolations(12);
        this.setViolationResetTime(60000L);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            this.LastHeal.remove(uuid);
            this.FastHealTicks.remove(uuid);
        });
    }
    
    public boolean checkFastHeal(final Player player) {
        if (this.LastHeal.containsKey(player.getUniqueId())) {
            final long l = this.LastHeal.get(player.getUniqueId());
            this.LastHeal.remove(player.getUniqueId());
            return System.currentTimeMillis() - l < 3000L;
        }
        return false;
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHeal(final EntityRegainHealthEvent event) {
        Player player;
        int Count;
        long Time;
        this.async(() -> {
            if (event.getRegainReason().equals((Object)EntityRegainHealthEvent.RegainReason.SATIATED) && event.getEntity() instanceof Player && this.getAnticheat().getLagAssist().getTPS() >= Settings.IMP.LAG_ASSIST.MAX_TPS) {
                player = (Player)event.getEntity();
                if (!player.getWorld().getDifficulty().equals((Object)Difficulty.PEACEFUL)) {
                    Count = 0;
                    Time = System.currentTimeMillis();
                    if (this.FastHealTicks.containsKey(player.getUniqueId())) {
                        Count = this.FastHealTicks.get(player.getUniqueId()).getKey();
                        Time = this.FastHealTicks.get(player.getUniqueId()).getValue();
                    }
                    if (this.checkFastHeal(player) && !UtilsB.isFullyStuck(player) && !UtilsB.isPartiallyStuck(player)) {
                        ++Count;
                    }
                    else {
                        Count = ((Count > 0) ? (Count - 1) : Count);
                    }
                    if (Count > 2) {
                        this.getAnticheat().failure(this, player, null, (String[])null);
                    }
                    if (this.FastHealTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 60000L)) {
                        Count = 0;
                        Time = UtilTime.nowlong();
                    }
                    this.LastHeal.put(player.getUniqueId(), System.currentTimeMillis());
                    this.FastHealTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
                }
            }
        });
    }
}
