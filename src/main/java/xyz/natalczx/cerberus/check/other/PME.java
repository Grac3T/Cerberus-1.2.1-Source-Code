// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.player.PlayerQuitEvent;
import com.google.common.io.ByteArrayDataInput;
import org.bukkit.ChatColor;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.HashMap;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.json.simple.parser.JSONParser;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import xyz.natalczx.cerberus.check.Check;

public class PME extends Check implements PluginMessageListener, Listener
{
    public static String type;
    private final Map<UUID, Map<String, String>> forgeMods;
    private final JSONParser parser;
    
    public PME(final CerberusAntiCheat AntiCheat) {
        super("PME", "PME", AntiCheat);
        this.parser = new JSONParser();
        this.forgeMods = new HashMap<UUID, Map<String, String>>();
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent playerJoinEvent) {
        this.async(() -> this.getClientType(playerJoinEvent.getPlayer()));
    }
    
    public void onPluginMessageReceived(final String string, final Player player, final byte[] arrby) {
        final ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(arrby);
        if ("ForgeMods".equals(byteArrayDataInput.readUTF())) {
            final String string2 = byteArrayDataInput.readUTF();
            try {
                final Map map = (Map)this.parser.parse(string2);
                this.forgeMods.put(player.getUniqueId(), map);
                final String string3 = this.getClientType(player);
                if (string3 != null) {
                    PME.type = string3;
                    this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", "(Type: B)");
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent playerQuitEvent) {
        this.async(() -> this.forgeMods.remove(playerQuitEvent.getPlayer().getUniqueId()));
    }
    
    public String getClientType(final Player player) {
        final Map<String, String> map = this.forgeMods.get(player.getUniqueId());
        if (map != null) {
            if (map.containsKey("gc")) {
                PME.type = "A";
                this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", PME.type);
                return "A";
            }
            if (map.containsKey("ethylene")) {
                PME.type = "B";
                this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", PME.type);
                return "B";
            }
            if ("1.0".equals(map.get("OpenComputers"))) {
                PME.type = "C";
                this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", PME.type);
                return "C";
            }
            if ("1.7.6.git".equals(map.get("Schematica"))) {
                PME.type = "D";
                this.getAnticheat().failure(this, player, ChatColor.RED + "Experimental", PME.type);
                return "D";
            }
        }
        return null;
    }
}
