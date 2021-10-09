// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.HashMap;
import xyz.natalczx.cerberus.check.Check;

public class TimerB extends Check
{
    private static HashMap<UUID, Long> t;
    private static HashMap<UUID, Integer> ts;
    private static HashMap<UUID, Double> verbose;
    
    public TimerB(final CerberusAntiCheat AntiCheat) {
        super("TimerB", "Timer", AntiCheat);
        this.setViolationsToNotify(5);
    }
    
    @EventHandler
    public void onMove(final RealMoveEvent e) {
        final Player p;
        final UserData data;
        UUID uuid;
        int pac;
        double v;
        this.async(() -> {
            p = e.getPlayer();
            data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
            if (data != null) {
                if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                    uuid = p.getUniqueId();
                    if (!TimerB.t.containsKey(uuid)) {
                        TimerB.t.put(p.getUniqueId(), System.currentTimeMillis());
                        TimerB.ts.put(p.getUniqueId(), 1);
                        TimerB.verbose.put(p.getUniqueId(), 0.0);
                    }
                    if (System.currentTimeMillis() - TimerB.t.get(uuid) > 1000L) {
                        if (TimerB.ts.get(uuid) < 23 && TimerB.verbose.get(uuid) > 0.9) {
                            TimerB.verbose.put(uuid, TimerB.verbose.get(uuid) - 1.0);
                        }
                        TimerB.t.put(uuid, System.currentTimeMillis());
                        TimerB.ts.put(uuid, 0);
                    }
                    else {
                        pac = TimerB.ts.get(uuid);
                        v = TimerB.verbose.get(uuid);
                        if (pac > 20) {
                            if (pac > 22) {
                                v += 0.3;
                            }
                            if (pac > 30.0 - v) {
                                p.teleport(e.getFrom().clone());
                            }
                        }
                        TimerB.verbose.put(uuid, v);
                        TimerB.ts.put(p.getUniqueId(), pac + 1);
                    }
                }
            }
        });
    }
    
    static {
        TimerB.t = new HashMap<UUID, Long>();
        TimerB.ts = new HashMap<UUID, Integer>();
        TimerB.verbose = new HashMap<UUID, Double>();
    }
}
