// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import org.bukkit.event.EventHandler;
import org.bukkit.ChatColor;
import java.util.Collections;
import xyz.natalczx.cerberus.helper.needscleanup.ExtraUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.natalczx.cerberus.user.UserData;
import java.util.Optional;
import org.bukkit.entity.LivingEntity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.Collection;
import org.bukkit.entity.Entity;
import java.util.ArrayList;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class AimAssistA extends Check implements PacketBased
{
    public AimAssistA(final CerberusAntiCheat AntiCheat) {
        super("AimAssistA", "AimAssist", AntiCheat);
        this.setViolationResetTime(3000L);
        this.setViolationsToNotify(5);
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPlayer() == null) {
                    return;
                }
                try {
                    if (event.getPlayer() instanceof TemporaryPlayer) {
                        return;
                    }
                }
                catch (Exception ex) {}
                try {
                    final Optional<Entity> entityOp = new ArrayList<Entity>(event.getPlayer().getWorld().getEntities()).stream().filter(entity -> entity.getEntityId() == (int)event.getPacket().getIntegers().read(0)).findFirst();
                    if (entityOp.isPresent()) {
                        final Entity entity2 = entityOp.get();
                        final EnumWrappers.EntityUseAction action = (EnumWrappers.EntityUseAction)event.getPacket().getEntityUseActions().read(0);
                        if (action.equals((Object)EnumWrappers.EntityUseAction.ATTACK) && entity2 instanceof LivingEntity) {
                            final UserData data = CerberusAntiCheat.getInstance().getDataManager().getDataPlayer(event.getPlayer());
                            if (data != null) {
                                data.lastAttack = System.currentTimeMillis();
                                data.lastHitEntity = (LivingEntity)entity2;
                            }
                        }
                    }
                }
                catch (Exception ex2) {}
            }
        };
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final UserData data;
        float offset;
        float range;
        this.async(() -> {
            data = CerberusAntiCheat.getInstance().getDataManager().getDataPlayer(event.getPlayer());
            if (data != null && data.lastHitEntity != null && System.currentTimeMillis() - data.lastAttack <= 150L) {
                offset = ExtraUtils.yawTo180F((float)ExtraUtils.getOffsetFromEntity(event.getPlayer(), data.lastHitEntity)[0]);
                if (data.patterns.size() >= 10) {
                    Collections.sort(data.patterns);
                    range = Math.abs(data.patterns.get(data.patterns.size() - 1) - data.patterns.get(0));
                    if (Math.abs(range - data.lastRange) < 4.0f) {
                        this.getAnticheat().failure(this, event.getPlayer(), ChatColor.RED + "Last hit & rotation check " + Math.abs(range - data.lastRange) + " < 4", "(Type: A)");
                    }
                    data.lastRange = range;
                    data.patterns.clear();
                }
                else {
                    data.patterns.add(offset);
                }
            }
        });
    }
}
