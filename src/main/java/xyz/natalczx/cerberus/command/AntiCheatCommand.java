// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.command;

import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.check.other.Latency;
import org.bukkit.entity.Player;
import java.io.File;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.api.MessageBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.command.CommandExecutor;

public class AntiCheatCommand implements CommandExecutor
{
    private CerberusAntiCheat antiCheat;
    
    public AntiCheatCommand(final CerberusAntiCheat AntiCheat) {
        this.antiCheat = AntiCheat;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageBuilder.newBuilder("&eCerberus &8- &7A new era of &ccheat&7 detection &8(&ehttps://mc-protection.eu/&8)").coloured().toString());
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            this.antiCheat.reloadConfig();
            Settings.IMP.reload(new File(this.antiCheat.getDataFolder(), "cerberus.yml"));
            sender.sendMessage(new MessageBuilder(Settings.IMP.MESSAGES.COMMANDS.RELOAD_COMMAND).coloured().toString());
            return true;
        }
        if (!args[0].equalsIgnoreCase("ping")) {
            if (args[0].equalsIgnoreCase("configure")) {}
            return true;
        }
        if (!(sender instanceof Player)) {
            return true;
        }
        final Player p = (Player)sender;
        if (args.length == 1) {
            sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.COMMANDS.PING_COMMAND_YOURSELF).withField("{SERVER-PING}", String.valueOf(this.antiCheat.getLagAssist().getPing(p))).withField("{ANTICHEAT-PING}", String.valueOf(Math.round((float)(Latency.getLag(p) / 2 * 6)))).coloured().toString());
            return true;
        }
        final Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.COMMANDS.PLAYER_NOT_ONLINE).coloured().toString());
            return true;
        }
        sender.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.COMMANDS.PING_COMMAND_OTHER).withField("{PLAYER}", target.getName()).withField("{SERVER-PING}", String.valueOf(this.antiCheat.getLagAssist().getPing(target))).withField("{ANTICHEAT-PING}", String.valueOf(Math.round((float)(Latency.getLag(target) / 2 * 6)))).coloured().toString());
        return true;
    }
}
