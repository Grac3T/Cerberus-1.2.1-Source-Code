// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.util.Vector;
import java.util.Iterator;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.UUID;
import java.util.HashMap;
import xyz.natalczx.cerberus.check.Check;

public class SpeedF extends Check
{
    private final HashMap<UUID, Long> time;
    private final HashMap<UUID, Double> dist;
    private final HashMap<UUID, Long> bplac;
    private final HashMap<UUID, Long> last;
    
    public SpeedF(final CerberusAntiCheat anticheat) {
        super("SpeedF", "Experimental Speed", anticheat);
        this.time = new HashMap<UUID, Long>();
        this.dist = new HashMap<UUID, Double>();
        this.bplac = new HashMap<UUID, Long>();
        this.last = new HashMap<UUID, Long>();
        this.setViolationResetTime(30000L);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final UUID uuid;
        this.getAnticheat().getCerberusThread().addPacket(() -> {
            uuid = e.getPlayer().getUniqueId();
            this.time.remove(uuid);
            this.dist.remove(uuid);
            this.bplac.remove(uuid);
            this.last.remove(uuid);
        });
    }
    
    @EventHandler
    public void onPlace(final BlockPlaceEvent e) {
        final Location ploc = e.getPlayer().getLocation().clone();
        final Location location;
        this.async(() -> {
            if (location.getY() - e.getBlockPlaced().getLocation().getY() > 1.0 && Math.abs(location.getX() - e.getBlockPlaced().getLocation().getX()) < 3.0 && Math.abs(location.getZ() - e.getBlockPlaced().getLocation().getZ()) < 3.0) {
                this.bplac.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            }
        });
    }
    
    @EventHandler
    public void onRlM(final RealMoveEvent e) {
        final Player p = e.getPlayer();
        if (p.getAllowFlight()) {
            return;
        }
        final UserData data = this.getAnticheat().getDataManager().getDataPlayer(p);
        if (data == null) {
            return;
        }
        final long timez = data.getActualTime();
        if (timez - data.getLastExplode() < 2600L) {
            return;
        }
        if (timez - data.getLastTP() < 500L) {
            return;
        }
        final UUID uuid = p.getUniqueId();
        if (!this.time.containsKey(uuid)) {
            this.time.put(uuid, timez);
            this.dist.put(uuid, 0.0);
        }
        if (timez - this.time.get(uuid) > 2500L) {
            if (this.dist.get(uuid) > 18.5) {
                if (this.last.containsKey(uuid) && timez - this.last.get(uuid) < 3000L && this.dist.get(uuid) < 23.0) {
                    this.getAnticheat().failure(this, p, this.dist.get(uuid) + " > 18.5", "(Type: F)");
                }
                this.last.put(uuid, timez);
            }
            this.time.put(uuid, timez);
            this.dist.put(uuid, 0.0);
        }
        final Location from = e.getFrom();
        final Location to = e.getTo();
        double speed = UtilsB.offset(this.getHV(to.toVector()), this.getHV(from.toVector()));
        if (data.onIce) {
            speed /= 1.7;
        }
        if (data.underBlock) {
            speed /= 1.6;
        }
        if (timez - data.getLastKnocked() < 2000L) {
            speed /= 1.5;
        }
        if (AirJump.falldmg.containsKey(e.getPlayer().getUniqueId()) && timez - AirJump.falldmg.get(uuid) < 1000L) {
            speed /= 1.5;
        }
        if (this.bplac.containsKey(uuid) && timez - this.bplac.get(uuid) < 250L) {
            speed /= 1.5;
            this.bplac.remove(uuid);
        }
        for (final PotionEffect pot : p.getActivePotionEffects()) {
            if (pot.getType().getName().equals(PotionEffectType.SPEED.getName())) {
                speed /= pot.getAmplifier() + 1.7 - pot.getAmplifier() / 2;
                break;
            }
        }
        this.dist.put(uuid, this.dist.get(uuid) + speed);
    }
    
    private Vector getHV(final Vector V) {
        V.setY(0);
        return V;
    }
}
