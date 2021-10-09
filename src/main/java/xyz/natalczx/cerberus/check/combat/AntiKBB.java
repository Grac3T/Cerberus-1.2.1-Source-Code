// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.entity.Player;
import java.util.Map;
import xyz.natalczx.cerberus.check.Check;

public class AntiKBB extends Check
{
    private final Map<Player, Long> lastSprintStart;
    private final Map<Player, Long> lastSprintStop;
    
    public AntiKBB(final CerberusAntiCheat AntiCheat) {
        super("AntiKBB", "AntiKB", AntiCheat);
        this.lastSprintStart = new HashMap<Player, Long>();
        this.lastSprintStop = new HashMap<Player, Long>();
        this.setEnabled(true);
        this.setMaxViolations(10);
        this.setBannable(false);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent playerQuitEvent) {
        final Player player;
        this.async(() -> {
            player = playerQuitEvent.getPlayer();
            this.lastSprintStart.remove(player);
            this.lastSprintStop.remove(player);
        });
    }
    
    @EventHandler
    public void Sprint(final PlayerToggleSprintEvent playerToggleSprintEvent) {
        final Player player;
        int n;
        int n2;
        long l;
        int n4;
        int n3;
        this.async(() -> {
            player = playerToggleSprintEvent.getPlayer();
            if (playerToggleSprintEvent.isSprinting() && this.lastSprintStop.containsKey(player)) {
                n = 0;
                n2 = 1;
                l = System.currentTimeMillis() - this.lastSprintStop.get(player);
                if (l < 5L) {
                    n4 = ++n;
                }
                else if (l > 1000L) {
                    n4 = --n;
                }
                else {
                    n -= 2;
                    n4 = n;
                }
                n3 = n4;
                if (n3 > n2) {
                    this.getAnticheat().failure(this, player, ChatColor.RED + "(Type: B, " + n3 + " > " + n2 + ")", "(Type: B)");
                }
            }
            if (!playerToggleSprintEvent.isSprinting()) {
                this.lastSprintStop.put(player, System.currentTimeMillis());
            }
            else if (playerToggleSprintEvent.isSprinting()) {
                this.lastSprintStart.put(player, System.currentTimeMillis());
            }
        });
    }
}
