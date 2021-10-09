// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.util.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class HitBoxB extends Check
{
    private double HITBOX_LENGTH;
    
    public HitBoxB(final CerberusAntiCheat AntiCheat) {
        super("HitBoxB", "HitBox", AntiCheat);
        this.HITBOX_LENGTH = 1.05;
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onHitPlayer(final EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Player player;
        LivingEntity victim;
        this.async(() -> {
            if (entityDamageByEntityEvent.getDamager() instanceof Player && entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
                player = (Player)entityDamageByEntityEvent.getDamager();
                victim = (LivingEntity)entityDamageByEntityEvent.getEntity();
                if (!this.hasInHitBox(victim)) {
                    this.getAnticheat().failure(this, player, ChatColor.RED + "(Type: RayTrace)", "(Type: RayTrace)");
                }
            }
        });
    }
    
    public boolean hasInHitBox(final LivingEntity livingEntity) {
        boolean bl = false;
        final Vector vector = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        final Vector vector2 = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        if ((livingEntity.getLocation().getDirection().normalize().crossProduct(vector).lengthSquared() < this.HITBOX_LENGTH || livingEntity.getLocation().getDirection().normalize().crossProduct(vector2).lengthSquared() < this.HITBOX_LENGTH) && (vector.normalize().dot(livingEntity.getLocation().getDirection().normalize()) >= 0.0 || vector2.normalize().dot(livingEntity.getLocation().getDirection().normalize()) >= 0.0)) {
            bl = true;
        }
        return bl;
    }
}
