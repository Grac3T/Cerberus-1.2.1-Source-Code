// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import xyz.natalczx.cerberus.packet.PacketPlayerType;
import xyz.natalczx.cerberus.packet.events.PacketAttackEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class KillAuraG extends Check
{
    public KillAuraG(final CerberusAntiCheat AntiCheat) {
        super("KillAuraG", "KillAura", AntiCheat);
    }
    
    public static float angleDistance(final float alpha, final float beta) {
        final float phi = Math.abs(beta - alpha) % 360.0f;
        return (phi > 180.0f) ? (360.0f - phi) : phi;
    }
    
    @EventHandler
    public void onAttack(final PacketAttackEvent e) {
        if (e.getType() != PacketPlayerType.USE) {
            return;
        }
        final Player player = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
        if (data == null) {
            return;
        }
        int verboseA = data.getKillauraAVerbose();
        long time = data.getLastAimTime();
        if (UtilsA.elapsed(time, 1100L)) {
            time = System.currentTimeMillis();
            verboseA = 0;
        }
        if ((Math.abs(data.getLastKillauraPitch() - player.getEyeLocation().getPitch()) > 1.0 || angleDistance((float)data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) > 1.0f || Double.compare(player.getEyeLocation().getYaw(), data.getLastKillauraYaw()) != 0) && !UtilsA.elapsed(data.getLastPacket(), 100L)) {
            if (angleDistance((float)data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) != data.getLastKillauraYawDif()) {
                final int i = ++verboseA;
                if (i > 13) {
                    this.getAnticheat().failure(this, player, "Type G, " + i + " > 13", "(Type: G)");
                }
            }
            data.setLastKillauraYawDif(angleDistance((float)data.getLastKillauraYaw(), player.getEyeLocation().getYaw()));
        }
        else {
            verboseA = 0;
        }
        data.setKillauraAVerbose(verboseA);
        data.setLastAimTime(time);
    }
}
