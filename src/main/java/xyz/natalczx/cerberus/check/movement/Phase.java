// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.comphenix.protocol.events.PacketEvent;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.material.TrapDoor;
import org.bukkit.material.Directional;
import org.bukkit.material.Gate;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import xyz.natalczx.cerberus.helper.UtilCheat;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.listener.PearlGlitchEvent;
import xyz.natalczx.cerberus.helper.PearlGlitchType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType;
import com.google.common.collect.Sets;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.bukkit.Material;
import java.util.List;
import org.bukkit.Location;
import java.util.UUID;
import java.util.Map;
import xyz.natalczx.cerberus.check.PacketBased;
import xyz.natalczx.cerberus.check.Check;

public class Phase extends Check implements PacketBased
{
    public static final Map<UUID, Location> lastLocation;
    public static List<Material> allowed;
    public static List<Material> semi;
    public static Set<UUID> teleported;
    private final ImmutableSet<Material> blockedPearlTypes;
    private final PhaseAsync phaseAsync;
    
    public Phase(final CerberusAntiCheat AntiCheat) {
        super("Phase", "Phase", AntiCheat);
        this.blockedPearlTypes = (ImmutableSet<Material>)Sets.immutableEnumSet((Enum)Material.THIN_GLASS, (Enum[])new Material[] { Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE, Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS, Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS, Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS, Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS });
        this.phaseAsync = new PhaseAsync((Plugin)AntiCheat, new PacketType[] { PacketType.Play.Client.POSITION });
    }
    
    @Override
    public PacketAdapter getPacketListener() {
        return this.phaseAsync;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void teleport(final PlayerTeleportEvent e) {
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            Phase.teleported.add(e.getPlayer().getUniqueId());
        }
    }
    
    @EventHandler
    public void death(final PlayerDeathEvent e) {
        Phase.teleported.add(e.getEntity().getUniqueId());
    }
    
    @EventHandler
    public void respawn(final PlayerRespawnEvent e) {
        Phase.teleported.add(e.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPlayerInteract(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasItem() && event.getItem().getType() == Material.ENDER_PEARL) {
            final Block block = event.getClickedBlock();
            if (block.getType().isSolid() && this.blockedPearlTypes.contains((Object)block.getType()) && !(block.getState() instanceof InventoryHolder)) {
                final PearlGlitchEvent event2 = new PearlGlitchEvent(event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation(), event.getPlayer().getItemInHand(), PearlGlitchType.INTERACT);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    event.setCancelled(true);
                    final Player player = event.getPlayer();
                    player.setItemInHand(event.getItem());
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onPearlClip(final PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            final Location to = event.getTo();
            if (this.blockedPearlTypes.contains((Object)to.getBlock().getType()) && to.getBlock().getType() != Material.FENCE_GATE && to.getBlock().getType() != Material.TRAP_DOOR) {
                final PearlGlitchEvent event2 = new PearlGlitchEvent(event.getPlayer(), event.getFrom(), event.getTo(), event.getPlayer().getItemInHand(), PearlGlitchType.TELEPORT);
                Bukkit.getPluginManager().callEvent((Event)event2);
                if (!event2.isCancelled()) {
                    final Player player = event.getPlayer();
                    event.setCancelled(true);
                }
                return;
            }
            to.setX(to.getBlockX() + 0.5);
            to.setZ(to.getBlockZ() + 0.5);
            if ((!Phase.allowed.contains(to.getBlock().getType()) || !Phase.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType())) && (to.getBlock().getType().isSolid() || to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) && (to.clone().subtract(0.0, 1.0, 0.0).getBlock().getType().isSolid() & !UtilCheat.isSlab(to.getBlock()))) {
                final Player player2 = event.getPlayer();
                final PearlGlitchEvent event3 = new PearlGlitchEvent(player2, event.getFrom(), event.getTo(), event.getPlayer().getItemInHand(), PearlGlitchType.SAFE_LOCATION);
                Bukkit.getPluginManager().callEvent((Event)event3);
                if (!event3.isCancelled()) {
                    event.setCancelled(true);
                }
                return;
            }
            if (!Phase.allowed.contains(to.clone().add(0.0, 1.0, 0.0).getBlock().getType()) && to.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid() && !to.getBlock().getType().isSolid()) {
                to.setY(to.getY() - 0.7);
            }
            event.setTo(to);
        }
    }
    
    public boolean isLegit(final UUID playerId, final Location loc1, final Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return true;
        }
        if (Phase.teleported.remove(playerId)) {
            return true;
        }
        int moveMaxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        final int moveMinX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        final int moveMaxY = Math.max(loc1.getBlockY(), loc2.getBlockY()) + 1;
        int moveMinY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        final int moveMaxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        final int moveMinZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        if (moveMaxY > 256) {
            moveMaxX = 256;
        }
        if (moveMinY > 256) {
            moveMinY = 256;
        }
        for (int x = moveMinX; x <= moveMaxX; ++x) {
            for (int z = moveMinZ; z <= moveMaxZ; ++z) {
                for (int y = moveMinY; y <= moveMaxY; ++y) {
                    final Block block = loc1.getWorld().getBlockAt(x, y, z);
                    if ((y != moveMinY || loc1.getBlockY() == loc2.getBlockY()) && this.hasPhased(block, loc1, loc2, Bukkit.getPlayer(playerId))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean hasPhased(final Block block, final Location loc1, final Location loc2, final Player p) {
        if (Phase.allowed.contains(block.getType()) || UtilCheat.isStair(block) || UtilCheat.isSlab(block) || UtilCheat.isClimbableBlock(block) || block.isLiquid()) {
            return false;
        }
        final double moveMaxX = Math.max(loc1.getX(), loc2.getX());
        final double moveMinX = Math.min(loc1.getX(), loc2.getX());
        final double moveMaxY = Math.max(loc1.getY(), loc2.getY()) + 1.8;
        final double moveMinY = Math.min(loc1.getY(), loc2.getY());
        final double moveMaxZ = Math.max(loc1.getZ(), loc2.getZ());
        final double moveMinZ = Math.min(loc1.getZ(), loc2.getZ());
        double blockMaxX = block.getLocation().getBlockX() + 1;
        double blockMinX = block.getLocation().getBlockX();
        double blockMaxY = block.getLocation().getBlockY() + 2;
        double blockMinY = block.getLocation().getBlockY();
        double blockMaxZ = block.getLocation().getBlockZ() + 1;
        double blockMinZ = block.getLocation().getBlockZ();
        if (blockMinY > moveMinY) {
            --blockMaxY;
        }
        if (block.getType().equals((Object)Material.IRON_DOOR_BLOCK) || block.getType().equals((Object)Material.WOODEN_DOOR)) {
            final Door door = (Door)block.getType().getNewData(block.getData());
            if (door.isTopHalf()) {
                return false;
            }
            BlockFace facing = door.getFacing();
            if (door.isOpen()) {
                final Block up = block.getRelative(BlockFace.UP);
                if (!up.getType().equals((Object)Material.IRON_DOOR_BLOCK) && !up.getType().equals((Object)Material.WOODEN_DOOR)) {
                    return false;
                }
                final boolean hinge = (up.getData() & 0x1) == 0x1;
                if (facing == BlockFace.NORTH) {
                    facing = (hinge ? BlockFace.WEST : BlockFace.EAST);
                }
                else if (facing == BlockFace.EAST) {
                    facing = (hinge ? BlockFace.NORTH : BlockFace.SOUTH);
                }
                else if (facing == BlockFace.SOUTH) {
                    facing = (hinge ? BlockFace.EAST : BlockFace.WEST);
                }
                else {
                    facing = (hinge ? BlockFace.SOUTH : BlockFace.NORTH);
                }
            }
            if (facing == BlockFace.WEST) {
                blockMaxX -= 0.8;
            }
            if (facing == BlockFace.EAST) {
                blockMinX += 0.8;
            }
            if (facing == BlockFace.NORTH) {
                blockMaxZ -= 0.8;
            }
            if (facing == BlockFace.SOUTH) {
                blockMinZ += 0.8;
            }
        }
        else if (block.getType().equals((Object)Material.FENCE_GATE)) {
            if (((Gate)block.getType().getNewData(block.getData())).isOpen()) {
                return false;
            }
            final BlockFace face = ((Directional)block.getType().getNewData(block.getData())).getFacing();
            if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
                blockMaxX -= 0.2;
                blockMinX += 0.2;
            }
            else {
                blockMaxZ -= 0.2;
                blockMinZ += 0.2;
            }
        }
        else if (block.getType().equals((Object)Material.TRAP_DOOR)) {
            final TrapDoor door2 = (TrapDoor)block.getType().getNewData(block.getData());
            if (door2.isOpen()) {
                return false;
            }
            if (door2.isInverted()) {
                blockMinY += 0.85;
            }
            else {
                blockMaxY -= ((blockMinY > moveMinY) ? 0.85 : 1.85);
            }
        }
        else if (block.getType().equals((Object)Material.FENCE) || Phase.semi.contains(block.getType())) {
            blockMaxX -= 0.2;
            blockMinX += 0.2;
            blockMaxZ -= 0.2;
            blockMinZ += 0.2;
            if ((moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ > blockMaxZ && moveMinZ > blockMaxZ) || (moveMaxX > blockMaxX && moveMinX > blockMaxX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ) || (moveMaxX < blockMinX && moveMinX < blockMinX && moveMaxZ < blockMinZ && moveMinZ < blockMinZ)) {
                return false;
            }
            if (block.getRelative(BlockFace.EAST).getType() == block.getType()) {
                blockMaxX += 0.2;
            }
            if (block.getRelative(BlockFace.WEST).getType() == block.getType()) {
                blockMinX -= 0.2;
            }
            if (block.getRelative(BlockFace.SOUTH).getType() == block.getType()) {
                blockMaxZ += 0.2;
            }
            if (block.getRelative(BlockFace.NORTH).getType() == block.getType()) {
                blockMinZ -= 0.2;
            }
        }
        final boolean x = loc1.getX() < loc2.getX();
        final boolean y = loc1.getY() < loc2.getY();
        final boolean z = loc1.getZ() < loc2.getZ();
        final double distance = loc1.distance(loc2) - Math.abs(loc1.getY() - loc2.getY());
        return (distance > 0.5 && block.getType().isSolid()) || (moveMinX != moveMaxX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((x && moveMinX <= blockMinX && moveMaxX >= blockMinX) || (!x && moveMinX <= blockMaxX && moveMaxX >= blockMaxX))) || (moveMinY != moveMaxY && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinZ <= blockMaxZ && moveMaxZ >= blockMinZ && ((y && moveMinY <= blockMinY && moveMaxY >= blockMinY) || (!y && moveMinY <= blockMaxY && moveMaxY >= blockMaxY))) || (moveMinZ != moveMaxZ && moveMinX <= blockMaxX && moveMaxX >= blockMinX && moveMinY <= blockMaxY && moveMaxY >= blockMinY && ((z && moveMinZ <= blockMinZ && moveMaxZ >= blockMinZ) || (!z && moveMinZ <= blockMaxZ && moveMaxZ >= blockMaxZ)));
    }
    
    static {
        lastLocation = new HashMap<UUID, Location>();
        Phase.allowed = new ArrayList<Material>();
        Phase.semi = new ArrayList<Material>();
        Phase.teleported = new HashSet<UUID>();
        Phase.allowed.add(Material.SIGN);
        Phase.allowed.add(Material.BANNER);
        Phase.allowed.add(Material.FENCE);
        Phase.allowed.add(Material.ANVIL);
        Phase.allowed.add(Material.TRAP_DOOR);
        Phase.allowed.add(Material.IRON_TRAPDOOR);
        Phase.allowed.add(Material.WALL_BANNER);
        Phase.allowed.add(Material.STANDING_BANNER);
        Phase.allowed.add(Material.SIGN_POST);
        Phase.allowed.add(Material.WALL_SIGN);
        Phase.allowed.add(Material.SUGAR_CANE_BLOCK);
        Phase.allowed.add(Material.WHEAT);
        Phase.allowed.add(Material.POTATO);
        Phase.allowed.add(Material.CARROT);
        Phase.allowed.add(Material.STEP);
        Phase.allowed.add(Material.AIR);
        Phase.allowed.add(Material.WOOD_STEP);
        Phase.allowed.add(Material.SOUL_SAND);
        Phase.allowed.add(Material.CARPET);
        Phase.allowed.add(Material.STONE_PLATE);
        Phase.allowed.add(Material.WOOD_PLATE);
        Phase.allowed.add(Material.LADDER);
        Phase.allowed.add(Material.CHEST);
        Phase.allowed.add(Material.WATER);
        Phase.allowed.add(Material.STATIONARY_WATER);
        Phase.allowed.add(Material.LAVA);
        Phase.allowed.add(Material.STATIONARY_LAVA);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR_OFF);
        Phase.allowed.add(Material.REDSTONE_COMPARATOR_ON);
        Phase.allowed.add(Material.IRON_PLATE);
        Phase.allowed.add(Material.GOLD_PLATE);
        Phase.allowed.add(Material.DAYLIGHT_DETECTOR);
        Phase.allowed.add(Material.STONE_BUTTON);
        Phase.allowed.add(Material.WOOD_BUTTON);
        Phase.allowed.add(Material.HOPPER);
        Phase.allowed.add(Material.RAILS);
        Phase.allowed.add(Material.ACTIVATOR_RAIL);
        Phase.allowed.add(Material.DETECTOR_RAIL);
        Phase.allowed.add(Material.POWERED_RAIL);
        Phase.allowed.add(Material.TRIPWIRE_HOOK);
        Phase.allowed.add(Material.TRIPWIRE);
        Phase.allowed.add(Material.SNOW_BLOCK);
        Phase.allowed.add(Material.REDSTONE_TORCH_OFF);
        Phase.allowed.add(Material.REDSTONE_TORCH_ON);
        Phase.allowed.add(Material.DIODE_BLOCK_OFF);
        Phase.allowed.add(Material.DIODE_BLOCK_ON);
        Phase.allowed.add(Material.DIODE);
        Phase.allowed.add(Material.SEEDS);
        Phase.allowed.add(Material.MELON_SEEDS);
        Phase.allowed.add(Material.PUMPKIN_SEEDS);
        Phase.allowed.add(Material.DOUBLE_PLANT);
        Phase.allowed.add(Material.LONG_GRASS);
        Phase.allowed.add(Material.WEB);
        Phase.allowed.add(Material.SNOW);
        Phase.allowed.add(Material.FLOWER_POT);
        Phase.allowed.add(Material.BREWING_STAND);
        Phase.allowed.add(Material.CAULDRON);
        Phase.allowed.add(Material.CACTUS);
        Phase.allowed.add(Material.WATER_LILY);
        Phase.allowed.add(Material.RED_ROSE);
        Phase.allowed.add(Material.ENCHANTMENT_TABLE);
        Phase.allowed.add(Material.ENDER_PORTAL_FRAME);
        Phase.allowed.add(Material.PORTAL);
        Phase.allowed.add(Material.ENDER_PORTAL);
        Phase.allowed.add(Material.ENDER_CHEST);
        Phase.allowed.add(Material.NETHER_FENCE);
        Phase.allowed.add(Material.NETHER_WARTS);
        Phase.allowed.add(Material.REDSTONE_WIRE);
        Phase.allowed.add(Material.LEVER);
        Phase.allowed.add(Material.YELLOW_FLOWER);
        Phase.allowed.add(Material.CROPS);
        Phase.allowed.add(Material.WATER);
        Phase.allowed.add(Material.LAVA);
        Phase.allowed.add(Material.SKULL);
        Phase.allowed.add(Material.TRAPPED_CHEST);
        Phase.allowed.add(Material.FIRE);
        Phase.allowed.add(Material.BROWN_MUSHROOM);
        Phase.allowed.add(Material.RED_MUSHROOM);
        Phase.allowed.add(Material.DEAD_BUSH);
        Phase.allowed.add(Material.SAPLING);
        Phase.allowed.add(Material.TORCH);
        Phase.allowed.add(Material.MELON_STEM);
        Phase.allowed.add(Material.PUMPKIN_STEM);
        Phase.allowed.add(Material.COCOA);
        Phase.allowed.add(Material.BED);
        Phase.allowed.add(Material.BED_BLOCK);
        Phase.allowed.add(Material.PISTON_EXTENSION);
        Phase.allowed.add(Material.PISTON_MOVING_PIECE);
        Phase.semi.add(Material.IRON_FENCE);
        Phase.semi.add(Material.THIN_GLASS);
        Phase.semi.add(Material.STAINED_GLASS_PANE);
        Phase.semi.add(Material.COBBLE_WALL);
    }
    
    private class PhaseAsync extends PacketAdapter
    {
        public PhaseAsync(final Plugin plugin, final PacketType... types) {
            super(plugin, types);
        }
        
        public void onPacketReceiving(final PacketEvent event) {
            if (!Phase.this.isEnabled()) {
                return;
            }
            final Player player = event.getPlayer();
            if (player == null || event.isCancelled()) {
                return;
            }
            try {
                if (player instanceof TemporaryPlayer) {
                    return;
                }
            }
            catch (Exception ex) {}
            final UserData data = Phase.this.getAnticheat().getDataManager().getData(player);
            if (data == null) {
                return;
            }
            if (System.currentTimeMillis() - data.getJoinTime() < 2000L) {
                return;
            }
            if (Phase.this.getAnticheat().getLagAssist().getPing(player) == 0) {
                return;
            }
            if (player.isDead() || (UtilsB.isNearLiquid(player) && UtilsB.isNearHalfBlock(player)) || UtilsB.isNearFence(player) || UtilsB.isNearStairPhase(player)) {
                return;
            }
            final Location from = player.getLocation().clone();
            final UUID playerId = player.getUniqueId();
            final Location loc1 = Phase.lastLocation.containsKey(playerId) ? Phase.lastLocation.get(playerId) : player.getLocation().clone();
            final Location loc2 = from;
            if (player.getAllowFlight()) {
                Phase.teleported.add(player.getUniqueId());
            }
            if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
                Phase.teleported.add(player.getUniqueId());
            }
            if (loc1.getWorld() == loc2.getWorld() && !Phase.teleported.contains(playerId) && loc1.distance(loc2) > 10.0) {
                if (from.getBlock().getType().isSolid() || from.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) {
                    return;
                }
                Phase.this.getAnticheat().failure(Phase.this, player, null, "(Type: A)");
            }
            else if (Phase.this.isLegit(playerId, loc1, loc2)) {
                Phase.lastLocation.put(playerId, loc2);
            }
            else if (Phase.lastLocation.containsKey(playerId)) {
                if (from.getBlock().getType().isSolid() || from.clone().add(0.0, 1.0, 0.0).getBlock().getType().isSolid()) {
                    return;
                }
                Phase.this.getAnticheat().failure(Phase.this, player, null, "(Type: B)");
            }
        }
    }
}
