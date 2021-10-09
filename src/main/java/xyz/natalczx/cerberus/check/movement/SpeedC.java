// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import org.bukkit.util.Vector;
import org.bukkit.potion.PotionEffect;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.events.RealMoveEvent;
import java.util.Iterator;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.Listener;
import xyz.natalczx.cerberus.check.Check;

public class SpeedC extends Check implements Listener
{
    public SpeedC(final CerberusAntiCheat AntiCheat) {
        super("SpeedC", "Speed", AntiCheat);
        this.setViolationResetTime(TimeUnit.MINUTES.toMillis(2L));
        this.setViolationsToNotify(4);
    }
    
    public static boolean isBlock(final Block block, final Material[] materials) {
        final Material type = block.getType();
        final Material[] arrayOfMaterial = materials;
        for (int j = materials.length, i = 0; i < j; ++i) {
            final Material m = arrayOfMaterial[i];
            if (m == type) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().clone().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.WEST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.NORTH).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.EAST).getType().equals((Object)Material.AIR) && b.getRelative(BlockFace.SOUTH).getType().equals((Object)Material.AIR);
    }
    
    public static boolean isReallyOnGround(final Player p) {
        final Location l = p.getLocation();
        if (!l.getWorld().isChunkLoaded(l.getBlockX() / 16, l.getBlockZ() / 16)) {
            return false;
        }
        final int x = l.getBlockX();
        final int y = l.getBlockY();
        final int z = l.getBlockZ();
        final Location b = new Location(p.getWorld(), (double)x, (double)(y - 1), (double)z);
        return p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB && !b.getBlock().isLiquid();
    }
    
    public static boolean flaggyStuffNear(final Location loc) {
        boolean nearBlocks = false;
        for (final Block bl : UtilsB.getSurrounding(loc.getBlock(), true)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        for (final Block bl : UtilsB.getSurrounding(loc.getBlock(), false)) {
            if (bl.getType().equals((Object)Material.STEP) || bl.getType().equals((Object)Material.DOUBLE_STEP) || bl.getType().equals((Object)Material.BED) || bl.getType().equals((Object)Material.WOOD_DOUBLE_STEP) || bl.getType().equals((Object)Material.WOOD_STEP)) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[] { Material.STEP, Material.BED, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP })) {
            nearBlocks = true;
        }
        return nearBlocks;
    }
    
    @EventHandler
    public void onMove(final RealMoveEvent e) {
        final Location from = e.getFrom().clone();
        final Location to = e.getTo().clone();
        if (!from.getWorld().isChunkLoaded(from.getBlockX() / 16, from.getBlockZ() / 16)) {
            return;
        }
        final World w = from.getWorld();
        final int x = from.getBlockX();
        final int y = from.getBlockY();
        final int z = from.getBlockZ();
        final Location blockLoc = new Location(w, (double)x, (double)(y - 1), (double)z);
        final Location loc = new Location(w, (double)x, (double)y, (double)z);
        final Location loc2 = new Location(w, (double)x, (double)(y + 1), (double)z);
        final Location above = new Location(w, (double)x, (double)(y + 2), (double)z);
        final Location above2 = new Location(w, (double)(x - 1), (double)(y + 2), (double)(z - 1));
        final Player p = e.getPlayer();
        final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(p);
        if (data == null) {
            return;
        }
        if (System.currentTimeMillis() - data.getLastKnocked() < 2000L) {
            return;
        }
        if (System.currentTimeMillis() - data.getLastTP() < 300L) {
            return;
        }
        final long lastHitDiff = Math.abs(System.currentTimeMillis() - SpeedD.lastHit.getOrDefault(p.getUniqueId(), 0L));
        if (lastHitDiff < 1500L || p.getNoDamageTicks() != 0 || p.getVehicle() != null || p.getGameMode().equals((Object)GameMode.SPECTATOR) || p.getAllowFlight() || UtilsB.isNearIce(p) || UtilsB.isNearSlime(p) || UtilsA.wasOnSlime(p)) {
            return;
        }
        double Airmaxspeed = 0.54;
        double maxSpeed = 0.42;
        double newmaxspeed = 0.75;
        if (data.onIce) {
            newmaxspeed = 1.0;
        }
        double ig = 0.4;
        final double speed = UtilsB.offset(this.getHV(to.toVector()), this.getHV(from.toVector()));
        final double onGroundDiff = to.getY() - from.getY();
        if (p.hasPotionEffect(PotionEffectType.SPEED)) {
            final int level = getPotionEffectLevel(p, PotionEffectType.SPEED);
            if (level > 0) {
                newmaxspeed *= level * 20 * 0.011 + 1.0;
                Airmaxspeed *= level * 20 * 0.011 + 1.0;
                maxSpeed *= level * 20 * 0.011 + 1.0;
                ig *= level * 20 * 0.011 + 1.0;
            }
        }
        Airmaxspeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
        maxSpeed += ((p.getWalkSpeed() > 0.2) ? (p.getWalkSpeed() * 0.8) : 0.0);
        if (isReallyOnGround(p) && to.getY() == from.getY() && speed >= maxSpeed && p.isOnGround() && p.getFallDistance() < 0.15 && blockLoc.getBlock().getType() != Material.ICE && blockLoc.getBlock().getType() != Material.PACKED_ICE && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR) {
            this.getAnticheat().failure(this, p, "On Ground, " + speed + ">= " + maxSpeed + ", " + p.getFallDistance() + " < 0.15", "(Type: C)");
        }
        if (!isReallyOnGround(p) && speed >= Airmaxspeed && !data.onIce && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid() && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE && above.getBlock().getType() == Material.AIR && above2.getBlock().getType() == Material.AIR) {
            for (int i = 0; i < speed / Airmaxspeed; ++i) {
                this.getAnticheat().failure(this, p, "Mid Air, " + speed + " > " + Airmaxspeed, "(Type: C2)");
            }
        }
        if (speed >= newmaxspeed && data.onIce && p.getFallDistance() < 0.6 && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR && loc2.getBlock().getType() == Material.AIR) {
            this.getAnticheat().failure(this, p, "Limit", "(Type: C3)");
        }
    }
    
    public static int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    private Vector getHV(final Vector V) {
        V.setY(0);
        return V;
    }
}
