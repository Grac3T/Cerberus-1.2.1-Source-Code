// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet;

import xyz.natalczx.cerberus.user.UserData;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventA;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.event.Event;
import xyz.natalczx.cerberus.packet.events.PacketAttackEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.entity.Player;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.PacketType;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import com.comphenix.protocol.ProtocolLibrary;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class PacketCoreA
{
    public static Map<UUID, Integer> movePackets;
    
    public static void start() {
        PacketCoreA.movePackets = new HashMap<UUID, Integer>();
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Server.POSITION }) {
            public void onPacketSending(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                PacketCoreA.movePackets.put(player.getUniqueId(), PacketCoreA.movePackets.getOrDefault(player.getUniqueId(), 0) + 1);
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                EnumWrappers.EntityUseAction type;
                try {
                    type = (EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0);
                }
                catch (Exception ex) {
                    return;
                }
                final Entity entity = (Entity)event.getPacket().getEntityModifier(player.getWorld()).read(0);
                if (entity == null) {
                    return;
                }
                if (type == EnumWrappers.EntityUseAction.ATTACK) {
                    Bukkit.getServer().getPluginManager().callEvent((Event)new PacketAttackEvent(player, entity, PacketPlayerType.USE));
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.LOOK }) {
            public void onPacketReceiving(final PacketEvent packetEvent) {
                final Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventA(player, player.getLocation().clone().getX(), player.getLocation().clone().getY(), player.getLocation().clone().getZ(), (float)packetEvent.getPacket().getFloat().read(0), (float)packetEvent.getPacket().getFloat().read(1), PacketPlayerType.LOOK));
                final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.POSITION }) {
            public void onPacketReceiving(final PacketEvent packetEvent) {
                final Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventA(player, (double)packetEvent.getPacket().getDoubles().read(0), (double)packetEvent.getPacket().getDoubles().read(1), (double)packetEvent.getPacket().getDoubles().read(2), player.getLocation().clone().getYaw(), player.getLocation().clone().getPitch(), PacketPlayerType.POSITION));
                final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
            public void onPacketReceiving(final PacketEvent packetEvent) {
                final Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventA(player, (double)packetEvent.getPacket().getDoubles().read(0), (double)packetEvent.getPacket().getDoubles().read(1), (double)packetEvent.getPacket().getDoubles().read(2), (float)packetEvent.getPacket().getFloat().read(0), (float)packetEvent.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
                final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    data.setLastKillauraPitch((float)packetEvent.getPacket().getFloat().read(1));
                    data.setLastKillauraYaw((float)packetEvent.getPacket().getFloat().read(0));
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(CerberusAntiCheat.getInstance(), new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent packetEvent) {
                final Player player = packetEvent.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventA(player, player.getLocation().clone().getX(), player.getLocation().clone().getY(), player.getLocation().clone().getZ(), player.getLocation().clone().getYaw(), player.getLocation().clone().getPitch(), PacketPlayerType.FLYING));
                final UserData data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    data.setLastPacket(System.currentTimeMillis());
                }
            }
        });
    }
}
