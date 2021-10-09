// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import xyz.natalczx.cerberus.helper.UtilTime;
import com.comphenix.protocol.wrappers.EnumWrappers;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AutoClickerB extends Check
{
    private final Map<UUID, Long> LastMS;
    private final Map<UUID, List<Long>> Clicks;
    private final Map<UUID, Map.Entry<Integer, Long>> ClickTicks;
    
    public AutoClickerB(final CerberusAntiCheat AntiCheat) {
        super("AutoClickerB", "AutoClicker", AntiCheat);
        this.LastMS = new HashMap<UUID, Long>();
        this.Clicks = new HashMap<UUID, List<Long>>();
        this.ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.setViolationsToNotify(2);
        this.setEnabled(true);
        this.setBannable(false);
        this.setMaxViolations(5);
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            this.LastMS.remove(uuid);
            this.Clicks.remove(uuid);
            if (this.ClickTicks.containsKey(uuid)) {
                this.Clicks.remove(uuid);
            }
        });
    }
    
    @EventHandler
    public void UseEntity(final PacketUseEntityEvent e) {
        Player damager;
        int Count;
        long Time;
        long MS;
        List<Long> Clicks;
        long Range;
        ArrayList<Long> Clicks2;
        this.async(() -> {
            if (e.getAction() == EnumWrappers.EntityUseAction.ATTACK && e.getAttacked() instanceof Player) {
                damager = e.getAttacker();
                Count = 0;
                Time = System.currentTimeMillis();
                if (this.ClickTicks.containsKey(damager.getUniqueId())) {
                    Count = this.ClickTicks.get(damager.getUniqueId()).getKey();
                    Time = this.ClickTicks.get(damager.getUniqueId()).getValue();
                }
                if (this.LastMS.containsKey(damager.getUniqueId())) {
                    MS = UtilTime.nowlong() - this.LastMS.get(damager.getUniqueId());
                    if (MS > 500L || MS < 5L) {
                        this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                        return;
                    }
                    else if (this.Clicks.containsKey(damager.getUniqueId())) {
                        Clicks = this.Clicks.get(damager.getUniqueId());
                        if (Clicks.size() == 3) {
                            Clicks.remove(damager.getUniqueId());
                            Collections.sort(Clicks);
                            Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                            if (Range >= 0L && Range <= 2L) {
                                ++Count;
                                Time = System.currentTimeMillis();
                            }
                        }
                        else {
                            Clicks.add(MS);
                            this.Clicks.put(damager.getUniqueId(), Clicks);
                        }
                    }
                    else {
                        Clicks2 = new ArrayList<Long>();
                        Clicks2.add(MS);
                        this.Clicks.put(damager.getUniqueId(), Clicks2);
                    }
                }
                if (this.ClickTicks.containsKey(damager.getUniqueId()) && UtilTime.elapsed(Time, 5000L)) {
                    Count = 0;
                    Time = UtilTime.nowlong();
                }
                if ((Count > 4 && this.getAnticheat().getLagAssist().getPing(damager) < 100) || (Count > 6 && this.getAnticheat().getLagAssist().getPing(damager) < 200)) {
                    Count = 0;
                    this.getAnticheat().failure(this, damager, "Continuous/Repeating Patterns", "(Type: B)");
                    this.ClickTicks.remove(damager.getUniqueId());
                }
                this.LastMS.put(damager.getUniqueId(), UtilTime.nowlong());
                this.ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
            }
        });
    }
}
