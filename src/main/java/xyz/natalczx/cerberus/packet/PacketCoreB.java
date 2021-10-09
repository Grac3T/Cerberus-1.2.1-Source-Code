// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.packet;

import xyz.natalczx.cerberus.packet.events.PacketBlockPlacementEvent;
import xyz.natalczx.cerberus.packet.events.PacketHeldItemChangeEvent;
import xyz.natalczx.cerberus.packet.events.PacketSwingArmEvent;
import xyz.natalczx.cerberus.packet.events.PacketKeepAliveEvent;
import xyz.natalczx.cerberus.packet.events.PacketEntityActionEvent;
import xyz.natalczx.cerberus.packet.events.PacketPlayerEventB;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.entity.Player;
import com.comphenix.protocol.events.PacketContainer;
import xyz.natalczx.cerberus.packet.events.PacketKillauraEvent;
import org.bukkit.event.Event;
import xyz.natalczx.cerberus.packet.events.PacketUseEntityEvent;
import org.bukkit.entity.Entity;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.ProtocolLibrary;
import java.util.HashMap;
import org.bukkit.entity.EntityType;
import java.util.HashSet;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import com.comphenix.protocol.PacketType;

public class PacketCoreB
{
    private static final PacketType[] ENTITY_PACKETS;
    public final CerberusAntiCheat AntiCheat;
    public final Map<UUID, Integer> movePackets;
    private final HashSet<EntityType> enabled;
    
    public PacketCoreB(final CerberusAntiCheat AntiCheat) {
        this.AntiCheat = AntiCheat;
        (this.enabled = new HashSet<EntityType>()).add(EntityType.valueOf("PLAYER"));
        this.movePackets = new HashMap<UUID, Integer>();
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                try {
                    final Object playEntity = PacketCoreB.this.getNMSClass("PacketPlayInUseEntity");
                    final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                    if (version.contains("1_7")) {
                        if (packet.getHandle() == playEntity && playEntity.getClass().getMethod("c", (Class<?>[])new Class[0]) == null) {
                            return;
                        }
                    }
                    else if (packet.getHandle() == playEntity && playEntity.getClass().getMethod("a", (Class<?>[])new Class[0]) == null) {
                        return;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
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
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketUseEntityEvent(type, player, entity));
                if (type == EnumWrappers.EntityUseAction.ATTACK) {
                    Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, PacketPlayerType.USE));
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventB(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), (float)event.getPacket().getFloat().read(0), (float)event.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventB(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), (float)event.getPacket().getFloat().read(0), (float)event.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventB(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), player.getLocation().clone().getYaw(), player.getLocation().clone().getPitch(), PacketPlayerType.POSITION));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Server.POSITION }) {
            public void onPacketSending(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                int i = PacketCoreB.this.movePackets.getOrDefault(player.getUniqueId(), 0);
                ++i;
                PacketCoreB.this.movePackets.put(player.getUniqueId(), i);
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.ENTITY_ACTION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEntityActionEvent(player, (int)packet.getIntegers().read(1)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.KEEP_ALIVE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKeepAliveEvent(player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.ARM_ANIMATION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, PacketPlayerType.ARM_SWING));
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketSwingArmEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.HELD_ITEM_SLOT }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketHeldItemChangeEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.BLOCK_PLACE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketBlockPlacementEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.AntiCheat, new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEventB(player, player.getLocation().clone().getX(), player.getLocation().clone().getY(), player.getLocation().clone().getZ(), player.getLocation().clone().getYaw(), player.getLocation().clone().getPitch(), PacketPlayerType.FLYING));
            }
        });
    }
    
    public Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        ENTITY_PACKETS = new PacketType[] { PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_METADATA };
    }
}
