// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import com.comphenix.protocol.reflect.StructureModifier;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import org.bukkit.Material;
import xyz.natalczx.cerberus.helper.UtilNewVelocity;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.Location;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class Gravity extends Check implements PacketBased
{
    private final GravityAsync gravityAsync;
    
    public Gravity(final CerberusAntiCheat AntiCheat) {
        super("Gravity", "Gravity", AntiCheat);
        this.gravityAsync = new GravityAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.gravityAsync;
    }
    
    private class GravityAsync extends PacketAdapter
    {
        public GravityAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            try {
                if (!Gravity.this.getAnticheat().isEnabled()) {
                    return;
                }
                final Player p = event.getPlayer();
                if (p == null) {
                    return;
                }
                try {
                    if (p instanceof TemporaryPlayer) {
                        return;
                    }
                }
                catch (Exception ex) {}
                final UserData data = Gravity.this.getAnticheat().getDataManager().getData(p);
                if (data == null) {
                    return;
                }
                if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
                    return;
                }
                if (p.isFlying()) {
                    return;
                }
                final Location from = p.getLocation().clone();
                final StructureModifier<Double> doubles = (StructureModifier<Double>)event.getPacket().getDoubles();
                final Location to = new Location(from.getWorld(), (double)doubles.read(0), (double)doubles.read(1), (double)doubles.read(2));
                final double diff = UtilsA.getVerticalDistance(from, to);
                final double LastY = data.getLastY_Gravity();
                final double MaxG = 7.0001;
                if (UtilsA.wasOnSlime(p)) {
                    data.setGravity_VL(0);
                    return;
                }
                if (to.getY() < from.getY()) {
                    return;
                }
                if (UtilsA.isHalfBlock(p.getLocation().add(0.0, -1.5, 0.0).getBlock()) || UtilsA.isNearHalfBlock(p) || UtilsA.isStair(p.getLocation().add(0.0, 1.5, 0.0).getBlock()) || UtilsA.isNearStiar(p) || !p.getGameMode().equals((Object)GameMode.CREATIVE) || UtilNewVelocity.didTakeVel(p) || UtilsA.wasOnSlime(p)) {
                    data.setGravity_VL(0);
                    return;
                }
                if (p.getLocation().getBlock().getType() != Material.CHEST && p.getLocation().getBlock().getType() != Material.TRAPPED_CHEST && p.getLocation().getBlock().getType() != Material.ENDER_CHEST && data.getAboveBlockTicks() == 0 && !UtilsA.onGround2(p) && !UtilsA.isOnGround3(p) && !UtilsA.isOnGround(p)) {
                    if ((((UtilsA.isBukkitVerison("1_7") || UtilsA.isBukkitVerison("1_8")) && Math.abs(p.getVelocity().getY() - LastY) > 1.0E-6) || (!UtilsA.isBukkitVerison("1_7") && !UtilsA.isBukkitVerison("1_8") && Math.abs(p.getVelocity().getY() - diff) > 1.0E-6)) && !UtilsA.onGround2(p) && from.getY() < to.getY() && (p.getVelocity().getY() >= 0.0 || p.getVelocity().getY() < -0.392) && !UtilVelocity.didTakeVelocity(p) && p.getNoDamageTicks() == 0.0) {
                        if (data.getGravity_VL() >= MaxG) {
                            Gravity.this.getAnticheat().failure(Gravity.this, p, "Player's motion was changed to an unexpected value. (" + data.getGravity_VL() + " > " + MaxG + ")", "(Type: A)");
                        }
                        else {
                            data.setGravity_VL(data.getGravity_VL() + 1);
                        }
                    }
                    else {
                        data.setGravity_VL(0);
                    }
                }
                data.setLastY_Gravity(diff);
            }
            catch (Exception ex2) {}
        }
    }
}
