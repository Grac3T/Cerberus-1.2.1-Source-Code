// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class CriticalsA extends Check
{
    public CriticalsA(final CerberusAntiCheat AntiCheat) {
        super("CriticalsA", "Criticals", AntiCheat);
    }
    
    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent e) {
        Player player;
        UserData data;
        double pfalldst;
        double dfalldst;
        Entity entity;
        int verbose;
        this.async(() -> {
            if (!(!(e.getDamager() instanceof Player))) {
                player = (Player)e.getDamager();
                data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    pfalldst = player.getFallDistance();
                    dfalldst = data.getFallDistance();
                    if (!(!Bukkit.getOnlinePlayers().contains(player))) {
                        entity = e.getEntity();
                        if (data.getAboveBlockTicks() <= 0 && !UtilsA.isInWeb(player) && data.getWaterTicks() <= 0 && !UtilsA.hasSlabsNear(player.getLocation().clone())) {
                            verbose = data.getCriticalsVerbose();
                            if (pfalldst > 0.0 && dfalldst == 0.0) {
                                if (++verbose > 5) {
                                    this.getAnticheat().failure(this, player, "Packet, " + verbose + " > 5 (A)", "(Type: A)");
                                    verbose = 0;
                                }
                            }
                            else {
                                verbose = 0;
                            }
                            data.setCriticalsVerbose(verbose);
                        }
                    }
                }
            }
        });
    }
}
