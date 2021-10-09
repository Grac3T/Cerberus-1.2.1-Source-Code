// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.Location;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class CriticalsB extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> CritTicks;
    private final Map<UUID, Double> FallDistance;
    
    public CriticalsB(final CerberusAntiCheat AntiCheat) {
        super("CriticalsB", "Criticals", AntiCheat);
        this.CritTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.FallDistance = new HashMap<UUID, Double>();
        this.setEnabled(true);
        this.setBannable(true);
        this.setMaxViolations(4);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            this.CritTicks.remove(uuid);
            if (this.FallDistance.containsKey(uuid)) {
                this.CritTicks.remove(uuid);
            }
        });
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
        Player player;
        Location pL;
        int Count;
        long Time;
        double realFallDistance;
        int Count2;
        this.async(() -> {
            if (e.getDamager() instanceof Player && e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                player = (Player)e.getDamager();
                if (!player.getAllowFlight() && !this.getAnticheat().getVelocityManager().getLastVelocity().containsKey(player.getUniqueId()) && !UtilCheat.slabsNear(player.getLocation().clone())) {
                    pL = player.getLocation().clone();
                    pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
                    if (!UtilCheat.blocksNear(pL)) {
                        Count = 0;
                        Time = System.currentTimeMillis();
                        if (this.CritTicks.containsKey(player.getUniqueId())) {
                            Count = this.CritTicks.get(player.getUniqueId()).getKey();
                            Time = this.CritTicks.get(player.getUniqueId()).getValue();
                        }
                        if (!(!this.FallDistance.containsKey(player.getUniqueId()))) {
                            realFallDistance = this.FallDistance.get(player.getUniqueId());
                            Count2 = ((player.getFallDistance() > 0.0 && !player.isOnGround() && realFallDistance == 0.0) ? (++Count) : 0);
                            if (this.CritTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 10000L)) {
                                Count2 = 0;
                                Time = UtilTime.nowlong();
                            }
                            if (Count2 >= 2) {
                                Count2 = 0;
                                this.getAnticheat().failure(this, player, "Type: B", "(Type: B)");
                            }
                            this.CritTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count2, Time));
                        }
                    }
                }
            }
        });
    }
    
    @EventHandler
    public void Move(final PlayerMoveEvent e) {
        final Player Player2;
        double Falling;
        this.async(() -> {
            Player2 = e.getPlayer();
            Falling = 0.0;
            if (!Player2.isOnGround() && e.getFrom().getY() > e.getTo().getY()) {
                if (this.FallDistance.containsKey(Player2.getUniqueId())) {
                    Falling = this.FallDistance.get(Player2.getUniqueId());
                }
                Falling += e.getFrom().getY() - e.getTo().getY();
            }
            this.FallDistance.put(Player2.getUniqueId(), Falling);
        });
    }
}
