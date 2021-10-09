// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventPriority;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.check.other.Latency;
import xyz.natalczx.cerberus.packet.events.PacketSwingArmEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AutoClickerA extends Check
{
    public Map<UUID, Integer> clicks;
    private Map<UUID, Long> recording;
    
    public AutoClickerA(final CerberusAntiCheat AntiCheat) {
        super("AutoClickerA", "AutoClicker", AntiCheat);
        this.setEnabled(true);
        this.setBannable(true);
        this.setViolationsToNotify(1);
        this.setMaxViolations(5);
        this.clicks = new HashMap<UUID, Integer>();
        this.recording = new HashMap<UUID, Long>();
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        final Player p;
        final UUID uuid;
        this.async(() -> {
            p = e.getPlayer();
            uuid = p.getUniqueId();
            this.clicks.remove(uuid);
            this.recording.remove(uuid);
        });
    }
    
    @EventHandler
    public void onSwing(final PacketSwingArmEvent e) {
        final Player player;
        int clicks;
        long time;
        this.async(() -> {
            player = e.getPlayer();
            if (this.getAnticheat().getLagAssist().getTPS() >= 19.0 && Latency.getLag(player) <= 100) {
                clicks = this.clicks.getOrDefault(this, 0);
                time = this.recording.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
                if (UtilTime.elapsed(time, 1000L)) {
                    if (clicks > 18) {
                        this.getAnticheat().failure(this, player, clicks + " clicks/s", "(Type: A)");
                    }
                    clicks = 0;
                    this.recording.remove(player.getUniqueId());
                }
                else {
                    ++clicks;
                }
                this.clicks.put(player.getUniqueId(), clicks);
                this.recording.put(player.getUniqueId(), time);
            }
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent e) {
        this.clicks.remove(e.getPlayer().getUniqueId());
        this.recording.remove(e.getPlayer().getUniqueId());
    }
}
