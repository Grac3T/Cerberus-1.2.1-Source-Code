// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.listener;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.check.movement.HighJump;
import org.bukkit.Material;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.Listener;

public class BlockPlace implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player player = e.getPlayer();
        if (player == null) {
            return;
        }
        final ItemStack item = player.getItemInHand();
        final Player player2;
        final UserData data;
        final ItemStack itemStack;
        CerberusAntiCheat.getInstance().getCerberusThread().addPacket(() -> {
            data = CerberusAntiCheat.getInstance().getDataManager().getData(player2);
            if (data != null) {
                data.setLastBlockPlace(System.currentTimeMillis());
            }
            if (itemStack.getType() == Material.SLIME_BLOCK && HighJump.allowblockbugging) {
                PlayerMove.slimed.add(player2.getUniqueId());
                PlayerMove.falldistance.put(player2.getUniqueId(), 250.0);
            }
        });
    }
}
