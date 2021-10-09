// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check;

import java.util.Collection;
import com.google.common.collect.ImmutableList;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.configuration.ConfigurationSection;
import java.util.Iterator;
import org.bukkit.configuration.file.FileConfiguration;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import xyz.natalczx.cerberus.check.other.AutoArmor;
import xyz.natalczx.cerberus.check.other.FastBreak;
import xyz.natalczx.cerberus.check.movement.ScaffoldC;
import xyz.natalczx.cerberus.check.movement.SneakB;
import xyz.natalczx.cerberus.check.combat.AimAssistC;
import xyz.natalczx.cerberus.check.combat.AimAssistB;
import xyz.natalczx.cerberus.check.combat.AutoClickerC;
import xyz.natalczx.cerberus.check.combat.AntiKBB;
import xyz.natalczx.cerberus.check.other.FalsePackets;
import xyz.natalczx.cerberus.check.combat.KillAuraJ;
import xyz.natalczx.cerberus.check.combat.HitBoxB;
import xyz.natalczx.cerberus.check.combat.KillAuraK;
import xyz.natalczx.cerberus.check.other.Change;
import xyz.natalczx.cerberus.check.combat.KillAuraI;
import xyz.natalczx.cerberus.check.combat.KillAuraH;
import xyz.natalczx.cerberus.check.player.PacketsA;
import xyz.natalczx.cerberus.check.player.LineOfSight;
import xyz.natalczx.cerberus.check.player.ImpossiblePitch;
import xyz.natalczx.cerberus.check.player.GroundSpoof;
import xyz.natalczx.cerberus.check.other.VapeCracked;
import xyz.natalczx.cerberus.check.other.PacketsB;
import xyz.natalczx.cerberus.check.other.BlockInteract;
import xyz.natalczx.cerberus.check.other.Exploit;
import xyz.natalczx.cerberus.check.other.CrashABC;
import xyz.natalczx.cerberus.check.movement.Step;
import xyz.natalczx.cerberus.check.movement.Spider;
import xyz.natalczx.cerberus.check.movement.SpeedF;
import xyz.natalczx.cerberus.check.movement.SpeedC;
import xyz.natalczx.cerberus.check.movement.SpeedD;
import xyz.natalczx.cerberus.check.movement.SpeedAB;
import xyz.natalczx.cerberus.check.movement.SneakA;
import xyz.natalczx.cerberus.check.movement.HighJump;
import xyz.natalczx.cerberus.check.movement.NoSlowdown;
import xyz.natalczx.cerberus.check.movement.NoFall;
import xyz.natalczx.cerberus.check.movement.Jesus;
import xyz.natalczx.cerberus.check.movement.ImpossibleMovements;
import xyz.natalczx.cerberus.check.movement.Glide;
import xyz.natalczx.cerberus.check.movement.FlyE;
import xyz.natalczx.cerberus.check.movement.FlyABCD;
import xyz.natalczx.cerberus.check.movement.AirJump;
import xyz.natalczx.cerberus.check.movement.FastLadder;
import xyz.natalczx.cerberus.check.movement.AscensionB;
import xyz.natalczx.cerberus.check.movement.AscensionA;
import xyz.natalczx.cerberus.check.combat.Twitch;
import xyz.natalczx.cerberus.check.combat.Regen;
import xyz.natalczx.cerberus.check.combat.ReachD;
import xyz.natalczx.cerberus.check.combat.ReachC;
import xyz.natalczx.cerberus.check.combat.ReachB;
import xyz.natalczx.cerberus.check.combat.ReachA;
import xyz.natalczx.cerberus.check.combat.AimAssistA;
import xyz.natalczx.cerberus.check.combat.KillAuraG;
import xyz.natalczx.cerberus.check.combat.KillAuraF;
import xyz.natalczx.cerberus.check.combat.KillAuraE;
import xyz.natalczx.cerberus.check.combat.KillAuraD;
import xyz.natalczx.cerberus.check.combat.KillAuraC;
import xyz.natalczx.cerberus.check.combat.KillAuraB;
import xyz.natalczx.cerberus.check.combat.KillAuraA;
import xyz.natalczx.cerberus.check.combat.HitBoxA;
import xyz.natalczx.cerberus.check.combat.FastBow;
import xyz.natalczx.cerberus.check.combat.CriticalsA;
import xyz.natalczx.cerberus.check.combat.CriticalsB;
import xyz.natalczx.cerberus.check.combat.AutoClickerB;
import xyz.natalczx.cerberus.check.combat.AutoClickerA;
import xyz.natalczx.cerberus.check.combat.AntiKBA;
import xyz.natalczx.cerberus.check.movement.ScaffoldAB;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import java.util.ArrayList;
import java.util.List;

public class CheckManager
{
    private final List<Check> checks;
    
    public CheckManager() {
        this.checks = new ArrayList<Check>();
    }
    
    public void loadDefaultChecks(final CerberusAntiCheat antiCheat) {
        this.checks.add(new ScaffoldAB(antiCheat));
        this.checks.add(new AntiKBA(antiCheat));
        this.checks.add(new AutoClickerA(antiCheat));
        this.checks.add(new AutoClickerB(antiCheat));
        this.checks.add(new CriticalsB(antiCheat));
        this.checks.add(new CriticalsA(antiCheat));
        this.checks.add(new FastBow(antiCheat));
        this.checks.add(new HitBoxA(antiCheat));
        this.checks.add(new KillAuraA(antiCheat));
        this.checks.add(new KillAuraB(antiCheat));
        this.checks.add(new KillAuraC(antiCheat));
        this.checks.add(new KillAuraD(antiCheat));
        this.checks.add(new KillAuraE(antiCheat));
        this.checks.add(new KillAuraF(antiCheat));
        this.checks.add(new KillAuraG(antiCheat));
        this.checks.add(new AimAssistA(antiCheat));
        this.checks.add(new ReachA(antiCheat));
        this.checks.add(new ReachB(antiCheat));
        this.checks.add(new ReachC(antiCheat));
        this.checks.add(new ReachD(antiCheat));
        this.checks.add(new Regen(antiCheat));
        this.checks.add(new Twitch(antiCheat));
        this.checks.add(new AscensionA(antiCheat));
        this.checks.add(new AscensionB(antiCheat));
        this.checks.add(new FastLadder(antiCheat));
        this.checks.add(new AirJump(antiCheat));
        this.checks.add(new FlyABCD(antiCheat));
        this.checks.add(new FlyE(antiCheat));
        this.checks.add(new Glide(antiCheat));
        this.checks.add(new ImpossibleMovements(antiCheat));
        this.checks.add(new Jesus(antiCheat));
        this.checks.add(new NoFall(antiCheat));
        this.checks.add(new NoSlowdown(antiCheat));
        this.checks.add(new HighJump(antiCheat));
        this.checks.add(new SneakA(antiCheat));
        this.checks.add(new SpeedAB(antiCheat));
        this.checks.add(new SpeedD(antiCheat));
        this.checks.add(new SpeedC(antiCheat));
        this.checks.add(new SpeedF(antiCheat));
        this.checks.add(new Spider(antiCheat));
        this.checks.add(new Step(antiCheat));
        this.checks.add(new CrashABC(antiCheat));
        this.checks.add(new Exploit(antiCheat));
        this.checks.add(new BlockInteract(antiCheat));
        this.checks.add(new PacketsB(antiCheat));
        this.checks.add(new VapeCracked(antiCheat));
        this.checks.add(new GroundSpoof(antiCheat));
        this.checks.add(new ImpossiblePitch(antiCheat));
        this.checks.add(new LineOfSight(antiCheat));
        this.checks.add(new PacketsA(antiCheat));
        this.checks.add(new KillAuraH(antiCheat));
        this.checks.add(new KillAuraI(antiCheat));
        this.checks.add(new Change(antiCheat));
        this.checks.add(new KillAuraK(antiCheat));
        this.checks.add(new HitBoxB(antiCheat));
        this.checks.add(new KillAuraJ(antiCheat));
        this.checks.add(new FalsePackets(antiCheat));
        this.checks.add(new AntiKBB(antiCheat));
        this.checks.add(new AutoClickerC(antiCheat));
        this.checks.add(new AimAssistB(antiCheat));
        this.checks.add(new AimAssistC(antiCheat));
        this.checks.add(new SneakB(antiCheat));
        this.checks.add(new ScaffoldC(antiCheat));
        this.checks.add(new FastBreak(antiCheat));
        this.checks.add(new AutoArmor(antiCheat));
    }
    
    public void updateChecks() {
        final CerberusAntiCheat antiCheat = CerberusAntiCheat.getInstance();
        final FileConfiguration config = antiCheat.getConfig();
        for (final Check check : this.getChecks()) {
            final ConfigurationSection data = config.getConfigurationSection("checks." + check.getIdentifier());
            check.setEnabled(data.getBoolean("enabled", check.isEnabled()));
            check.setBannable(data.getBoolean("bannable", check.isBannable()));
            check.setViolationsToNotify(data.getInt("violations-to-notify-staff", check.getViolationsToNotify()));
            check.setMaxViolations(data.getInt("max-violations", check.getMaxViolations()));
        }
        final ConfigurationSection data2 = config.getConfigurationSection("checks.AllowBlockBugging");
        if (data2.getBoolean("enabled")) {
            HighJump.allowblockbugging = true;
        }
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        for (final Check check2 : this.getChecks()) {
            if (check2.isEnabled()) {
                pluginManager.registerEvents((Listener)check2, (Plugin)antiCheat);
                if (!(check2 instanceof PacketBased)) {
                    continue;
                }
                protocolManager.addPacketListener((PacketListener)((PacketBased)check2).getPacketListener());
            }
        }
    }
    
    public List<Check> getChecks() {
        return (List<Check>)ImmutableList.copyOf((Collection)this.checks);
    }
}
