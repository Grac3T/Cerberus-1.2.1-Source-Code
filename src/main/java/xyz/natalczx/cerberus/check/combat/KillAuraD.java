// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import java.util.AbstractMap;
import xyz.natalczx.cerberus.packet.PacketPlayerType;
import xyz.natalczx.cerberus.packet.events.PacketKillauraEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraD extends Check
{
    private final Map<UUID, Map.Entry<Double, Double>> packetTicks;
    
    public KillAuraD(final CerberusAntiCheat AntiCheat) {
        super("KillAuraD", "Expierimental KillAura", AntiCheat);
        this.setEnabled(true);
        this.setBannable(false);
        this.setViolationsToNotify(50);
        this.setMaxViolations(150);
        this.setViolationResetTime(3000L);
        this.packetTicks = new HashMap<UUID, Map.Entry<Double, Double>>();
    }
    
    @EventHandler
    public void packet(final PacketKillauraEvent e) {
        if (!this.getAnticheat().isEnabled()) {
            return;
        }
        double Count = 0.0;
        double Other = 0.0;
        if (this.packetTicks.containsKey(e.getPlayer().getUniqueId())) {
            Count = this.packetTicks.get(e.getPlayer().getUniqueId()).getKey();
            Other = this.packetTicks.get(e.getPlayer().getUniqueId()).getValue();
        }
        if (e.getType() == PacketPlayerType.ARM_SWING) {
            ++Other;
        }
        if (e.getType() == PacketPlayerType.USE) {
            ++Count;
        }
        if (Count > Other && Other == 2.0) {
            this.getAnticheat().failure(this, e.getPlayer(), "Packet (D) " + Count + " > " + Other + " & " + Other + " is 2", "(Type: D)");
        }
        if (Count > 3.0 || Other > 3.0) {
            Count = 0.0;
            Other = 0.0;
        }
        this.packetTicks.put(e.getPlayer().getUniqueId(), new AbstractMap.SimpleEntry<Double, Double>(Count, Other));
    }
    
    @EventHandler
    public void logout(final PlayerQuitEvent e) {
        this.async(() -> this.packetTicks.remove(e.getPlayer().getUniqueId()));
    }
}
