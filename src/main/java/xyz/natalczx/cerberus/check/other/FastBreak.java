// 
// Decompiled by Procyon v0.5.36
// 

package xyz.natalczx.cerberus.check.other;

import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import xyz.natalczx.cerberus.user.UserData;
import org.bukkit.entity.Player;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.GameMode;
import xyz.natalczx.cerberus.config.Settings;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import java.util.concurrent.TimeUnit;
import xyz.natalczx.cerberus.CerberusAntiCheat;
import xyz.natalczx.cerberus.check.Check;

public class FastBreak extends Check
{
    public FastBreak(final CerberusAntiCheat AntiCheat) {
        super("FastBreak", "FastBreak", AntiCheat);
        this.setViolationResetTime(TimeUnit.SECONDS.toMillis(90L));
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent e) {
        final Player player;
        UserData data;
        ItemStack item;
        long diff;
        int level;
        int level2;
        int level3;
        this.async(() -> {
            player = e.getPlayer();
            if (e.getBlock().getType() == Material.STONE) {
                if (player != null) {
                    data = this.getAnticheat().getDataManager().getData(player);
                    if (data != null) {
                        if (System.currentTimeMillis() - data.getJoinTime() >= 2000L) {
                            if (this.getAnticheat().getLagAssist().getPing(player) <= Settings.IMP.LAG_ASSIST.MAX_PING && !this.getAnticheat().getLagAssist().shouldCancelTPS()) {
                                if (player.getGameMode() != GameMode.CREATIVE) {
                                    if (!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                        item = e.getPlayer().getItemInHand();
                                        if (item != null && item.getType() != Material.AIR) {
                                            if (!item.hasItemMeta() || !item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED) || item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED) <= 5) {
                                                diff = System.currentTimeMillis() - data.getLastStoneBreak();
                                                if (item.getType() == Material.DIAMOND_PICKAXE) {
                                                    if (item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED)) {
                                                        level = item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED);
                                                        if (level == 1) {
                                                            if (diff < 470L) {
                                                                this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 470ms, EFFICIENCY I", new String[0]);
                                                            }
                                                        }
                                                        else if (level == 2) {
                                                            if (diff < 450L) {
                                                                this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 450ms, EFFICIENCY II", new String[0]);
                                                            }
                                                        }
                                                        else if (level == 3) {
                                                            if (diff < 390L) {
                                                                this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 390ms, EFFICIENCY III", new String[0]);
                                                            }
                                                        }
                                                        else if (level == 4) {
                                                            if (diff < 320L) {
                                                                this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 320ms, EFFICIENCY IV", new String[0]);
                                                            }
                                                        }
                                                        else if (level == 5 && diff < 290L) {
                                                            this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 290ms, EFFICIENCY V", new String[0]);
                                                        }
                                                    }
                                                    else if (diff < 490L) {
                                                        this.getAnticheat().failure(this, player, "Diamond pickaxe, " + diff + " < 490ms, no enchants", new String[0]);
                                                    }
                                                }
                                                else if (item.getType() == Material.IRON_PICKAXE) {
                                                    if (item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED)) {
                                                        level2 = item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED);
                                                        if (level2 == 1) {
                                                            if (diff < 540L) {
                                                                this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 540ms, EFFICIENCY I", new String[0]);
                                                            }
                                                        }
                                                        else if (level2 == 2) {
                                                            if (diff < 490L) {
                                                                this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 490ms, EFFICIENCY II", new String[0]);
                                                            }
                                                        }
                                                        else if (level2 == 3) {
                                                            if (diff < 390L) {
                                                                this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 390ms, EFFICIENCY III", new String[0]);
                                                            }
                                                        }
                                                        else if (level2 == 4) {
                                                            if (diff < 330L) {
                                                                this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 330ms, EFFICIENCY IV", new String[0]);
                                                            }
                                                        }
                                                        else if (level2 == 5 && diff < 300L) {
                                                            this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 300ms, EFFICIENCY V", new String[0]);
                                                        }
                                                    }
                                                    else if (diff < 640L) {
                                                        this.getAnticheat().failure(this, player, "Iron pickaxe, " + diff + " < 640ms, no enchants", new String[0]);
                                                    }
                                                }
                                                else if (item.getType() == Material.STONE_PICKAXE) {
                                                    if (item.hasItemMeta() && item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED)) {
                                                        level3 = item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED);
                                                        if (level3 == 1) {
                                                            if (diff < 640L) {
                                                                this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " <640ms, EFFICIENCY I", new String[0]);
                                                            }
                                                        }
                                                        else if (level3 == 2) {
                                                            if (diff < 530L) {
                                                                this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " < 530ms, EFFICIENCY II", new String[0]);
                                                            }
                                                        }
                                                        else if (level3 == 3) {
                                                            if (diff < 430L) {
                                                                this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " < 430ms, EFFICIENCY III", new String[0]);
                                                            }
                                                        }
                                                        else if (level3 == 4) {
                                                            if (diff < 380L) {
                                                                this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " < 380ms, EFFICIENCY IV", new String[0]);
                                                            }
                                                        }
                                                        else if (level3 == 5 && diff < 310L) {
                                                            this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " < 310ms, EFFICIENCY V", new String[0]);
                                                        }
                                                    }
                                                    else if (diff < 800L) {
                                                        this.getAnticheat().failure(this, player, "Stone pickaxe, " + diff + " < 800ms, no enchants", new String[0]);
                                                    }
                                                }
                                                else if (item.getType() == Material.GOLD_PICKAXE) {
                                                    if (diff < 200L) {
                                                        this.getAnticheat().failure(this, player, "Gold pickaxe, " + diff + " < 200ms", new String[0]);
                                                    }
                                                }
                                                else if (item.getType() == Material.WOOD_PICKAXE && diff < 300L) {
                                                    this.getAnticheat().failure(this, player, "Wood pickaxe, " + diff + " < 300ms", new String[0]);
                                                }
                                                data.setLastStoneBreak(System.currentTimeMillis());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
