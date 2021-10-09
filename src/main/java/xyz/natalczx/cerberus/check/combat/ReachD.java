// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.combat;

import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class ReachD extends Check
{
    public ReachD(final CerberusAntiCheat AntiCheat) {
        super("ReachD", "Reach", AntiCheat);
        this.setViolationResetTime(30000L);
        this.setViolationsToNotify(1);
    }
    
    @EventHandler
    public void onATTACK(final EntityDamageByEntityEvent e) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/bukkit/event/entity/EntityDamageByEntityEvent.getDamager:()Lorg/bukkit/entity/Entity;
        //     4: instanceof      Lorg/bukkit/entity/Player;
        //     7: ifeq            20
        //    10: aload_1         /* e */
        //    11: invokevirtual   org/bukkit/event/entity/EntityDamageByEntityEvent.getEntity:()Lorg/bukkit/entity/Entity;
        //    14: instanceof      Lorg/bukkit/entity/Player;
        //    17: ifne            21
        //    20: return         
        //    21: aload_1         /* e */
        //    22: invokevirtual   org/bukkit/event/entity/EntityDamageByEntityEvent.getDamager:()Lorg/bukkit/entity/Entity;
        //    25: checkcast       Lorg/bukkit/entity/Player;
        //    28: astore_2        /* player */
        //    29: aload_2         /* player */
        //    30: invokeinterface org/bukkit/entity/Player.isSprinting:()Z
        //    35: istore_3        /* sprinting */
        //    36: aload_1         /* e */
        //    37: invokevirtual   org/bukkit/event/entity/EntityDamageByEntityEvent.getEntity:()Lorg/bukkit/entity/Entity;
        //    40: checkcast       Lorg/bukkit/entity/Player;
        //    43: astore          damaged
        //    45: aload_2         /* player */
        //    46: invokeinterface org/bukkit/entity/Player.getLocation:()Lorg/bukkit/Location;
        //    51: invokevirtual   org/bukkit/Location.clone:()Lorg/bukkit/Location;
        //    54: astore          ploc
        //    56: aload           damaged
        //    58: invokeinterface org/bukkit/entity/Player.getLocation:()Lorg/bukkit/Location;
        //    63: invokevirtual   org/bukkit/Location.clone:()Lorg/bukkit/Location;
        //    66: astore          dloc
        //    68: aload_0         /* this */
        //    69: aload_0         /* this */
        //    70: aload_1         /* e */
        //    71: aload_2         /* player */
        //    72: aload           damaged
        //    74: aload           ploc
        //    76: aload           dloc
        //    78: iload_3         /* sprinting */
        //    79: invokedynamic   BootstrapMethod #0, run:(Lxyz/natalczx/cerberus/check/combat/ReachD;Lorg/bukkit/event/entity/EntityDamageByEntityEvent;Lorg/bukkit/entity/Player;Lorg/bukkit/entity/Player;Lorg/bukkit/Location;Lorg/bukkit/Location;Z)Ljava/lang/Runnable;
        //    84: invokevirtual   xyz/natalczx/cerberus/check/combat/ReachD.async:(Ljava/lang/Runnable;)V
        //    87: return         
        //    StackMapTable: 00 02 14 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.generateNameForVariable(NameVariables.java:264)
        //     at com.strobel.decompiler.languages.java.ast.NameVariables.assignNamesToVariables(NameVariables.java:198)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:276)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:126)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private Vector getHV(final Vector V) {
        V.setY(0);
        return V;
    }
}
