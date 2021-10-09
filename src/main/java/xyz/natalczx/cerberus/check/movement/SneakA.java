// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.entity.Player;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.helper.UtilTime;
import xyz.natalczx.cerberus.packet.events.PacketEntityActionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class SneakA extends Check
{
    private final Map<UUID, Map.Entry<Integer, Long>> sneakTicks;
    
    public SneakA(final CerberusAntiCheat AntiCheat) {
        super("SneakA", "Sneak", AntiCheat);
        this.sneakTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
    }
    
    @EventHandler
    public void onLog(final PlayerQuitEvent e) {
        this.sneakTicks.remove(e.getPlayer().getUniqueId());
    }
    
    @EventHandler
    public void EntityAction(final PacketEntityActionEvent event) {
        if (event.getAction() != 1) {
            return;
        }
        final Player player = event.getPlayer();
        int Count = 0;
        long Time = -1L;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            Count = this.sneakTicks.get(player.getUniqueId()).getKey();
            Time = this.sneakTicks.get(player.getUniqueId()).getValue();
        }
        ++Count;
        if (this.sneakTicks.containsKey(player.getUniqueId())) {
            if (UtilTime.elapsed(Time, 100L)) {
                Count = 0;
                Time = System.currentTimeMillis();
            }
            else {
                Time = System.currentTimeMillis();
            }
        }
        if (Count > 50) {
            Count = 0;
            this.getAnticheat().failure(this, player, null, (String[])null);
        }
        this.sneakTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}
