// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.violation;

import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import xyz.natalczx.cerberus.api.MessageBuilder;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.check.other.Latency;
import org.bukkit.event.EventHandler;
import java.util.Iterator;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.helper.update.UpdateType;
import xyz.natalczx.cerberus.helper.update.UpdateEvent;
import org.bukkit.entity.Player;
import java.util.HashMap;
import xyz.natalczx.cerberus.helper.LagAssist;
import xyz.natalczx.cerberus.check.Check;
import java.util.UUID;
import java.util.Map;
import org.bukkit.event.Listener;

public class ViolationManager implements Listener
{
    private final Map<UUID, Map<Check, Integer>> violations;
    private final Map<UUID, Map<Check, Long>> violationsReset;
    private final Map<UUID, Map.Entry<Check, Long>> autoBan;
    private final LagAssist lagAssist;
    
    public ViolationManager(final LagAssist lagAssist) {
        this.violations = new HashMap<UUID, Map<Check, Integer>>();
        this.violationsReset = new HashMap<UUID, Map<Check, Long>>();
        this.autoBan = new HashMap<UUID, Map.Entry<Check, Long>>();
        this.lagAssist = lagAssist;
    }
    
    public int getViolations(final Player player, final Check check) {
        if (this.violations.containsKey(player.getUniqueId())) {
            return this.violations.get(player.getUniqueId()).get(check);
        }
        return 0;
    }
    
    public Map<Check, Integer> getViolations(final Player player) {
        if (this.violations.containsKey(player.getUniqueId())) {
            return new HashMap<Check, Integer>(this.violations.get(player.getUniqueId()));
        }
        return null;
    }
    
    public void addViolation(final Player player, final Check check) {
        Map<Check, Integer> map = new HashMap<Check, Integer>();
        if (this.violations.containsKey(player.getUniqueId())) {
            map = this.violations.get(player.getUniqueId());
        }
        if (!map.containsKey(check)) {
            map.put(check, 1);
        }
        else {
            map.put(check, map.get(check) + 1);
        }
        this.violations.put(player.getUniqueId(), map);
    }
    
    public void removeViolations(final Player player, final Check check) {
        if (this.violations.containsKey(player.getUniqueId())) {
            this.violations.get(player.getUniqueId()).remove(check);
        }
    }
    
    public void setViolationResetTime(final Player player, final Check check, final long time) {
        Map<Check, Long> map = new HashMap<Check, Long>();
        if (this.violationsReset.containsKey(player.getUniqueId())) {
            map = this.violationsReset.get(player.getUniqueId());
        }
        map.put(check, time);
        this.violationsReset.put(player.getUniqueId(), map);
    }
    
    @EventHandler
    public void autoBanUpdate(final UpdateEvent event) {
        if (!event.getType().equals(UpdateType.SEC)) {
            return;
        }
        final Map<UUID, Map.Entry<Check, Long>> autoBan = new HashMap<UUID, Map.Entry<Check, Long>>(this.autoBan);
        for (final UUID uuid : autoBan.keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                this.autoBan.remove(uuid);
            }
            else {
                final Long time = autoBan.get(player.getUniqueId()).getValue();
                if (System.currentTimeMillis() < time) {
                    continue;
                }
                this.autobanOver(player, CerberusAntiCheat.getInstance().getLagAssist().getPing(player));
            }
        }
        final Map<UUID, Map<Check, Long>> ViolationResets = new HashMap<UUID, Map<Check, Long>>(this.violationsReset);
        for (final UUID uid : ViolationResets.keySet()) {
            if (!this.violations.containsKey(uid)) {
                continue;
            }
            final Map<Check, Long> Checks = new HashMap<Check, Long>(ViolationResets.get(uid));
            for (final Check check : Checks.keySet()) {
                final Long time2 = Checks.get(check);
                if (System.currentTimeMillis() >= time2) {
                    this.violationsReset.get(uid).remove(check);
                    this.violations.get(uid).remove(check);
                }
            }
        }
    }
    
    public void autobanOver(final Player player, final int ping) {
        final Map<UUID, Map.Entry<Check, Long>> AutoBan = new HashMap<UUID, Map.Entry<Check, Long>>(this.autoBan);
        if (AutoBan.containsKey(player.getUniqueId())) {
            this.banPlayer(player, AutoBan.get(player.getUniqueId()).getKey(), ping);
            this.autoBan.remove(player.getUniqueId());
        }
    }
    
    public void resetAllViolations() {
        this.violations.clear();
        this.violationsReset.clear();
    }
    
    public void autoban(final Check check, final Player player, final int ping) {
        if (this.lagAssist.getTPS() < 17.0) {
            return;
        }
        this.banPlayer(player, check, ping);
    }
    
    public void banPlayer(final Player player, final Check check, final int ping) {
        this.removeViolations(player, check);
        if (Latency.getLag(player) < 250) {
            Bukkit.getServer().broadcastMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.BAN_BROADCAST).withField("{PLAYER}", player.getName()).withField("{CHEAT}", check.getName()).withField("{PREFIX}", Settings.IMP.MESSAGES.PREFIX).withField("{PING}", String.valueOf(ping)).coloured().toString());
            for (final String command : Settings.IMP.MESSAGES.BAN_COMMANDS) {
                final String string = new MessageBuilder(command).withField("{PLAYER}", player.getName()).withField("{CHEAT}", check.getName()).withField("{PREFIX}", Settings.IMP.MESSAGES.PREFIX).withField("{PING}", String.valueOf(ping)).toString();
                CerberusAntiCheat.getInstance().getDataManager().remove(player);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)CerberusAntiCheat.getInstance(), () -> Bukkit.getServer().dispatchCommand((CommandSender)Bukkit.getConsoleSender(), string));
            }
        }
        if (this.violations.containsKey(player.getUniqueId())) {
            this.violations.remove(player.getUniqueId());
        }
    }
}
