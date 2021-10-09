// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class FastBow extends Check
{
    private final Map<Player, Long> bowPull;
    private final Map<Player, Integer> count;
    
    public FastBow(final CerberusAntiCheat AntiCheat) {
        super("FastBow", "FastBow", AntiCheat);
        this.bowPull = new HashMap<Player, Long>();
        this.count = new HashMap<Player, Integer>();
        this.setViolationsToNotify(2);
        this.setMaxViolations(7);
        this.setEnabled(true);
        this.setBannable(true);
    }
    
    @EventHandler
    public void Interact(final PlayerInteractEvent e) {
        final Player Player;
        this.async(() -> {
            Player = e.getPlayer();
            if (Player.getItemInHand() != null && Player.getItemInHand().getType().equals((Object)Material.BOW)) {
                this.bowPull.put(Player, System.currentTimeMillis());
            }
        });
    }
    
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
        this.async(() -> {
            this.bowPull.remove(e.getPlayer());
            this.count.remove(e.getPlayer());
        });
    }
    
    @EventHandler
    public void onShoot(final ProjectileLaunchEvent e) {
        Arrow arrow;
        Player player;
        Long time;
        double power;
        Long timeLimit;
        int Count;
        this.async(() -> {
            if (!(!this.isEnabled())) {
                if (e.getEntity() instanceof Arrow) {
                    arrow = (Arrow)e.getEntity();
                    if (arrow.getShooter() != null && arrow.getShooter() instanceof Player) {
                        player = (Player)arrow.getShooter();
                        if (this.bowPull.containsKey(player)) {
                            time = System.currentTimeMillis() - this.bowPull.get(player);
                            power = arrow.getVelocity().length();
                            timeLimit = 300L;
                            Count = 0;
                            if (this.count.containsKey(player)) {
                                Count = this.count.get(player);
                            }
                            if (power > 2.5 && time < timeLimit) {
                                this.count.put(player, Count + 1);
                            }
                            else {
                                this.count.put(player, (Count > 0) ? (Count - 1) : Count);
                            }
                            if (Count > 8) {
                                this.getAnticheat().failure(this, player, "Too fast bowing! (" + time + " ms)", (String[])null);
                                this.count.remove(player);
                            }
                        }
                    }
                }
            }
        });
    }
}
