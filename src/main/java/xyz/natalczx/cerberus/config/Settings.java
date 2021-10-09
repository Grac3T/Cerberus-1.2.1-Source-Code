// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.config;

import java.util.Collections;
import java.util.List;
import java.io.File;

public class Settings extends Config
{
    @Ignore
    public static final Settings IMP;
    @Comment({ "Cerberus - The new era of cheat prevention", "Made by yooniks and lmpR, discords: yooniks#0289 lmpR#9852" })
    @Create
    public MESSAGES MESSAGES;
    public String LICENSE;
    @Create
    public LAG_ASSIST LAG_ASSIST;
    @Create
    public SETTINGS SETTINGS;
    
    public Settings() {
        this.LICENSE = "";
    }
    
    public void reload(final File file) {
        this.load(file);
        this.save(file);
    }
    
    static {
        IMP = new Settings();
    }
    
    @Comment({ "Do not use '\\ n', use %nl%" })
    public static class MESSAGES
    {
        public String PREFIX;
        @Comment({ "Commands that console will execute when players reach the limit of violations", "Available variables:", "{PLAYER} - name of the player", "{CHEAT} - type of cheat that player used e.g Speed", "{PING} - ping of the player", "{PREFIX} - cerberus message prefix" })
        public List<String> BAN_COMMANDS;
        @Comment({ "Broadcast message when player got banned for cheating", "Available variables:", "{PLAYER} - name of the player", "{CHEAT} - type of cheat that player used", "{PING} - ping of the player" })
        public String BAN_BROADCAST;
        public String NOTIFICATION_FAILED_FIRST;
        public String NOTIFICATION_FAILED_SECOND;
        public String NOTIFICATION_FAILED_THIRD;
        public String NOTIFICATION_HOVER_CLICK_TO_TELEPORT;
        public int VIOLATIONS_RESET_TIME;
        public String NOTIFICATION_KICKED_FOR_FLYING;
        public String NOTIFICATION_PERMISSION;
        @Create
        public COMMANDS COMMANDS;
        
        public MESSAGES() {
            this.PREFIX = "&6&lC&e&lAC &c» ";
            this.BAN_COMMANDS = Collections.singletonList("ban {PLAYER} {PREFIX} &7You have been banned for cheating. &8(&6{CHEAT}&7, ping: &6{PING}&8)");
            this.BAN_BROADCAST = "{PREFIX} &8(&f{PING}&7ms&8) &e{PLAYER} &7has been banned for &6cheating&7 &8(&e{CHEAT}&8)";
            this.NOTIFICATION_FAILED_FIRST = "&8(&f{PING}&7ms, &f{LAG}&7lag&8) &e{PLAYER} &7failed ";
            this.NOTIFICATION_FAILED_SECOND = "&e{CHEAT} ";
            this.NOTIFICATION_FAILED_THIRD = "&8(&6{VL}&8/&c{MAX-VL}&8) ";
            this.NOTIFICATION_HOVER_CLICK_TO_TELEPORT = "&7Click to &eteleport &7to &6{PLAYER}";
            this.VIOLATIONS_RESET_TIME = 60;
            this.NOTIFICATION_KICKED_FOR_FLYING = "{PREFIX} &e{PLAYER} &7has been kicked for flying!";
            this.NOTIFICATION_PERMISSION = "cerberus.notification";
        }
        
        public static class COMMANDS
        {
            public String PING_COMMAND_YOURSELF;
            public String PING_COMMAND_OTHER;
            public String PLAYER_NOT_ONLINE;
            public String RELOAD_COMMAND;
            
            public COMMANDS() {
                this.PING_COMMAND_YOURSELF = "&7Your server ping is: &f{SERVER-PING}ms, &7anticheat ping: &f{ANTICHEAT-PING}ms";
                this.PING_COMMAND_OTHER = "&e{PLAYER}&7 server ping is: &f{SERVER-PING}ms&7, anticheat ping: &f{ANTICHEAT-PING}ms";
                this.PLAYER_NOT_ONLINE = "&cThat player is not online!";
                this.RELOAD_COMMAND = "&cReloaded &6cerberus&c. Only configs has been reloaded, to fully reload plugin please use plugman or something similar.";
            }
        }
    }
    
    public static class SETTINGS
    {
        @Comment({ "If is false, then it can has false detects on legit players if they are really good in pvp in e.g bedwars or bridges" })
        public boolean ALLOW_AMBUSE;
        
        public SETTINGS() {
            this.ALLOW_AMBUSE = true;
        }
    }
    
    public static class LAG_ASSIST
    {
        public int MAX_PING;
        public double MAX_TPS;
        
        public LAG_ASSIST() {
            this.MAX_PING = 300;
            this.MAX_TPS = 19.3;
        }
    }
}
