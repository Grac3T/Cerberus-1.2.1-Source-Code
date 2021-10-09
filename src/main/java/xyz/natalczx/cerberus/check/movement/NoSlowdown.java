// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.movement;

import java.util.HashMap;
import java.util.Arrays;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import java.util.Iterator;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.natalczx.cerberus.helper.needscleanup.UtilsB;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import org.bukkit.util.Vector;
import java.util.UUID;
import java.util.Map;
import java.util.Collection;
import xyz.natalczx.cerberus.check.Check;

public class NoSlowdown extends Check
{
    static final Collection<String> foodz;
    static final Map<UUID, Vector> started;
    
    public NoSlowdown(final CerberusAntiCheat AntiCheat) {
        super("NoSlowdown", "NoSlowdown", AntiCheat);
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEvent e) {
        Vector[] v2;
        this.async(() -> {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getMaterial() != null) {
                    if (NoSlowdown.foodz.contains(e.getMaterial().name())) {
                        NoSlowdown.started.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation().clone().toVector());
                    }
                    else if (e.getMaterial().name().contains("SWORD")) {
                        v2 = new Vector[] { e.getPlayer().getLocation().clone().toVector() };
                        new BukkitRunnable() {
                            double i;
                            final /* synthetic */ PlayerInteractEvent val$e;
                            final /* synthetic */ Vector[] val$v2;
                            
                            {
                                this.i = 0.0;
                            }
                            
                            public void run() {
                                if (!this.val$e.getPlayer().isOnline()) {
                                    this.cancel();
                                    return;
                                }
                                if (!this.val$e.getPlayer().isBlocking()) {
                                    if (this.i > 3.0) {
                                        double z = 0.0;
                                        if (this.val$e.getPlayer().isSprinting()) {
                                            z = 0.2;
                                        }
                                        NoSlowdown.this.doCheck(this.val$e.getPlayer(), this.i + z / 10.0 - 0.5, this.val$v2[0]);
                                    }
                                    this.cancel();
                                    return;
                                }
                                final UserData data = NoSlowdown.this.getAnticheat().getDataManager().getData(this.val$e.getPlayer());
                                if (data == null) {
                                    return;
                                }
                                if (data.onIce) {
                                    return;
                                }
                                ++this.i;
                                if (this.i > 10.0) {
                                    NoSlowdown.this.doCheck(this.val$e.getPlayer(), 1.0, this.val$v2[0]);
                                    this.i = 1.0;
                                    this.val$v2[0] = this.val$e.getPlayer().getLocation().clone().toVector();
                                    this.val$v2[0] = this.val$e.getPlayer().getLocation().clone().toVector();
                                }
                            }
                        }.runTaskTimerAsynchronously((Plugin)CerberusAntiCheat.getInstance(), 2L, 2L);
                    }
                }
            }
        });
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onEat(final PlayerItemConsumeEvent e) {
        if (!NoSlowdown.foodz.contains(e.getItem().getType().name())) {
            return;
        }
        this.doCheck(e.getPlayer(), 1.0, NoSlowdown.started.get(e.getPlayer().getUniqueId()));
    }
    
    private void doCheck(final Player player, final double time, final Vector v2) {
        UserData data;
        double max;
        Vector v3;
        double max2;
        double speed;
        double max3;
        double toadd;
        int level;
        this.async(() -> {
            if (!(!this.isEnabled())) {
                data = CerberusAntiCheat.getInstance().getDataManager().getData(player);
                if (data != null) {
                    if (!player.isInsideVehicle()) {
                        if (this.getAnticheat().getLagAssist().getTPS() >= Settings.IMP.LAG_ASSIST.MAX_TPS) {
                            max = 2.8;
                            if (System.currentTimeMillis() - data.getLastKnocked() > time * 3000.0) {
                                v3 = player.getLocation().clone().toVector();
                                max2 = max * (Math.abs(v3.getY() - v2.getY()) + 1.0);
                                v3.setY(0);
                                v2.setY(0);
                                speed = UtilsB.offset(v3, v2);
                                if (!data.onIce) {
                                    max3 = max2 * time;
                                    toadd = CerberusAntiCheat.getInstance().getLagAssist().getPing(player) / 100 - 1.3;
                                    if (toadd > 0.0) {
                                        max3 += toadd;
                                    }
                                    if (player.hasPotionEffect(PotionEffectType.SPEED)) {
                                        level = this.getPotionEffectLevel(player, PotionEffectType.SPEED);
                                        if (level > 0) {
                                            max3 *= level * 0.5 + 1.0;
                                        }
                                    }
                                    if (speed > max3) {
                                        this.getAnticheat().failure(this, player, speed + " > " + max3, "(Type: A)");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    private int getPotionEffectLevel(final Player p, final PotionEffectType pet) {
        for (final PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void BowShoot(final EntityShootBowEvent event) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/bukkit/event/entity/EntityShootBowEvent.getEntity:()Lorg/bukkit/entity/LivingEntity;
        //     4: instanceof      Lorg/bukkit/entity/Player;
        //     7: ifne            11
        //    10: return         
        //    11: aload_1         /* event */
        //    12: invokevirtual   org/bukkit/event/entity/EntityShootBowEvent.getEntity:()Lorg/bukkit/entity/LivingEntity;
        //    15: checkcast       Lorg/bukkit/entity/Player;
        //    18: astore_2        /* player */
        //    19: aload_2         /* player */
        //    20: invokeinterface org/bukkit/entity/Player.isSprinting:()Z
        //    25: istore_3        /* sprinting */
        //    26: aload_0         /* this */
        //    27: aload_0         /* this */
        //    28: aload_1         /* event */
        //    29: aload_2         /* player */
        //    30: iload_3         /* sprinting */
        //    31: invokedynamic   BootstrapMethod #2, run:(Lxyz/natalczx/cerberus/check/movement/NoSlowdown;Lorg/bukkit/event/entity/EntityShootBowEvent;Lorg/bukkit/entity/Player;Z)Ljava/lang/Runnable;
        //    36: invokevirtual   xyz/natalczx/cerberus/check/movement/NoSlowdown.async:(Ljava/lang/Runnable;)V
        //    39: return         
        //    StackMapTable: 00 01 0B
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
    
    static {
        foodz = Arrays.asList("APPLE", "MUSHROOM_SOUP", "BREAD", "PORK", "GRILLED_PORK", "GOLDEN_APPLE", "RAW_FISH", "COOKED_FISH", "COOKIE", "MELON", "COOKED_BEEF", "RAW_BEEF", "COOKED_CHICKEN", "RAW_CHICKEN", "ROTTEN_FLESH", "CARROT", "POTATO", "BAKED_POTATO", "POISONOUS_POTATO", "PUMPKIN_PIE", "RABBIT", "COOKED_RABBIT", "RABBIT_STEW", "MUTTON", "COOKED_MUTTON", "MILK_BUCKET", "POTION");
        started = new HashMap<UUID, Vector>();
    }
}
