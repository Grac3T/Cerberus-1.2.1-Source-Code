// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus;

import org.bukkit.plugin.PluginManager;
import xyz.natalczx.cerberus.listener.TeleportEvent;
import xyz.natalczx.cerberus.listener.BlockPlace;
import xyz.natalczx.cerberus.listener.KickListener;
import xyz.natalczx.cerberus.event.MoveEvents;
import xyz.natalczx.cerberus.listener.PlayerMove;
import xyz.natalczx.cerberus.helper.UtilNewVelocity;
import xyz.natalczx.cerberus.helper.UtilVelocity;
import xyz.natalczx.cerberus.helper.SetBackSystem;
import xyz.natalczx.cerberus.event.UtilityJoinQuitEvent;
import xyz.natalczx.cerberus.event.UtilityMoveEvent;
import xyz.natalczx.cerberus.listener.EntityDamageEvent;
import com.comphenix.protocol.ProtocolLibrary;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.apache.commons.lang.StringUtils;
import xyz.natalczx.cerberus.check.other.Latency;
import xyz.natalczx.cerberus.api.MessageBuilder;
import xyz.natalczx.cerberus.helper.UtilActionMessage;
import org.bukkit.entity.Player;
import xyz.natalczx.cerberus.check.Check;
import xyz.natalczx.cerberus.task.ViolationsResetTask;
import org.bukkit.command.CommandExecutor;
import xyz.natalczx.cerberus.command.AntiCheatCommand;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.check.other.VapeCracked;
import xyz.natalczx.cerberus.packet.PacketCoreA;
import xyz.natalczx.cerberus.helper.Ping;
import java.io.File;
import xyz.natalczx.cerberus.config.Settings;
import xyz.natalczx.cerberus.velocity.VelocityManager;
import xyz.natalczx.cerberus.violation.ViolationManager;
import xyz.natalczx.cerberus.thread.CerberusThread;
import xyz.natalczx.cerberus.check.CheckManager;
import xyz.natalczx.cerberus.user.UserDataManager;
import xyz.natalczx.cerberus.helper.LagAssist;
import xyz.natalczx.cerberus.packet.PacketCoreB;
import xyz.natalczx.cerberus.helper.update.Updater;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CerberusAntiCheat extends JavaPlugin implements Listener
{
    public static CerberusAntiCheat instance;
    public Updater updater;
    public PacketCoreB packet;
    public LagAssist lagAssist;
    private UserDataManager dataManager;
    private CheckManager checkManager;
    private CerberusThread cerberusThread;
    private ViolationManager violationManager;
    private VelocityManager velocityManager;
    
    public static CerberusAntiCheat getInstance() {
        return CerberusAntiCheat.instance;
    }
    
    public void onEnable() {
        this.saveDefaultConfig();
        this.getDataFolder().mkdirs();
        Settings.IMP.reload(new File(this.getDataFolder(), "cerberus.yml"));
        CerberusAntiCheat.instance = this;
        this.dataManager = new UserDataManager();
        this.lagAssist = new LagAssist(this);
        this.violationManager = new ViolationManager(this.lagAssist);
        this.velocityManager = new VelocityManager();
        this.checkManager = new CheckManager();
        this.cerberusThread = new CerberusThread();
        this.registerListeners();
        new Ping(this);
        PacketCoreA.start();
        this.dataManager = new UserDataManager();
        this.checkManager.loadDefaultChecks(this);
        this.checkManager.updateChecks();
        this.packet = new PacketCoreB(this);
        this.updater = new Updater(this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this, "LOLIMAHCKER", (PluginMessageListener)new VapeCracked(this));
        this.getCommand("anticheat").setExecutor((CommandExecutor)new AntiCheatCommand(this));
        ViolationsResetTask.start(this);
        this.addDataPlayers();
    }
    
    public CerberusThread getCerberusThread() {
        return this.cerberusThread;
    }
    
    public LagAssist getLagAssist() {
        return this.lagAssist;
    }
    
    public void failure(final Check check, final Player player, final String hoverableText, final String... identifier) {
        int ping;
        int violations;
        UtilActionMessage msg;
        Settings.MESSAGES messages;
        String notificationFailedSecond;
        StringBuilder builder;
        int length;
        int i = 0;
        String id;
        String type;
        UtilActionMessage.AMText checkText;
        final Iterator<Player> iterator;
        Player admin;
        this.getCerberusThread().addPacket(() -> {
            if (this.getDataManager().getData(player) != null) {
                ping = this.lagAssist.getPing(player);
                this.violationManager.addViolation(player, check);
                this.violationManager.setViolationResetTime(player, check, System.currentTimeMillis() + check.getViolationResetTime());
                violations = this.violationManager.getViolations(player, check);
                if (violations >= check.getViolationsToNotify()) {
                    msg = new UtilActionMessage();
                    messages = Settings.IMP.MESSAGES;
                    msg.addText(MessageBuilder.newBuilder(messages.PREFIX).coloured().toString());
                    msg.addText(MessageBuilder.newBuilder(messages.NOTIFICATION_FAILED_FIRST).withField("{PLAYER}", player.getName()).withField("{PING}", String.valueOf(ping)).withField("{LAG}", String.valueOf(Latency.getLag(player))).coloured().toString()).addHoverText(MessageBuilder.newBuilder(messages.NOTIFICATION_HOVER_CLICK_TO_TELEPORT).withField("{PLAYER}", player.getName()).coloured().toString()).setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/tp " + player.getName());
                    notificationFailedSecond = messages.NOTIFICATION_FAILED_SECOND;
                    if (identifier != null) {
                        notificationFailedSecond = StringUtils.replace(notificationFailedSecond, " &8(&e{TYPE}&8)", "");
                    }
                    builder = new StringBuilder();
                    if (identifier != null) {
                        for (length = identifier.length; i < length; ++i) {
                            id = identifier[i];
                            builder.append(id);
                        }
                    }
                    type = builder.toString();
                    checkText = msg.addText(MessageBuilder.newBuilder(notificationFailedSecond).withField("{TYPE}", (identifier == null) ? "" : type).withField("{CHEAT}", check.getName()).coloured().toString());
                    if (hoverableText != null) {
                        checkText.addHoverText(hoverableText);
                    }
                    msg.addText(MessageBuilder.newBuilder(messages.NOTIFICATION_FAILED_THIRD).withField("{VL}", String.valueOf(violations)).withField("{MAX-VL}", String.valueOf(check.getMaxViolations())).coloured().toString());
                    if (violations % check.getViolationsToNotify() == 0) {
                        Bukkit.getOnlinePlayers().iterator();
                        while (iterator.hasNext()) {
                            admin = iterator.next();
                            if (!admin.hasPermission(Settings.IMP.MESSAGES.NOTIFICATION_PERMISSION)) {
                                continue;
                            }
                            else {
                                msg.sendToPlayer(admin);
                            }
                        }
                    }
                    if (violations >= check.getMaxViolations() && check.isBannable()) {
                        this.violationManager.autoban(check, player, ping);
                    }
                }
            }
        });
    }
    
    public UserDataManager getDataManager() {
        return this.dataManager;
    }
    
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListeners((Plugin)this);
        this.getCerberusThread().stop();
    }
    
    private void registerListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents((Listener)new EntityDamageEvent(), (Plugin)this);
        pluginManager.registerEvents((Listener)new UtilityMoveEvent(), (Plugin)this);
        pluginManager.registerEvents((Listener)new UtilityJoinQuitEvent(), (Plugin)this);
        pluginManager.registerEvents((Listener)new SetBackSystem(), (Plugin)this);
        pluginManager.registerEvents((Listener)new UtilVelocity(), (Plugin)this);
        pluginManager.registerEvents((Listener)new UtilNewVelocity(), (Plugin)this);
        pluginManager.registerEvents((Listener)new PlayerMove(), (Plugin)this);
        pluginManager.registerEvents((Listener)new MoveEvents(), (Plugin)this);
        pluginManager.registerEvents((Listener)new Latency(this), (Plugin)this);
        pluginManager.registerEvents((Listener)this.velocityManager, (Plugin)this);
        pluginManager.registerEvents((Listener)this.violationManager, (Plugin)this);
        pluginManager.registerEvents((Listener)new KickListener(), (Plugin)this);
        pluginManager.registerEvents((Listener)new BlockPlace(), (Plugin)this);
        pluginManager.registerEvents((Listener)new TeleportEvent(), (Plugin)this);
    }
    
    private void addDataPlayers() {
        final UserDataManager dataManager = getInstance().getDataManager();
        for (final Player playerLoop : Bukkit.getOnlinePlayers()) {
            dataManager.addPlayerData(playerLoop);
        }
    }
    
    public CheckManager getCheckManager() {
        return this.checkManager;
    }
    
    public VelocityManager getVelocityManager() {
        return this.velocityManager;
    }
    
    public ViolationManager getViolationManager() {
        return this.violationManager;
    }
}
