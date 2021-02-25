package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.SpecialItems.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;

public class ArmorThiefTimer extends PlayerTimer implements Listener {
    
    public ArmorThiefTimer() {
        super("ArmorThief", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("ArmorThief")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @SuppressWarnings("unused")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player damager = (Player)event.getDamager();
            final Player damaged = (Player)event.getEntity();
            final ArmorThief ArmorThief = new ArmorThief(damager.getItemInHand());
            final long remaining = this.getRemaining(damager);
            final int chance = (int) (Math.random() * 100);
            if (remaining <= 0L && ArmorThief.isArmorThief()) {
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(damager.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        damager.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	            	String hiddenAstrixedName = damaged.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : event.getEntity().getName();
	                if (event.getEntity() instanceof Player && damaged == null) {
	                    damager.sendMessage(ChatColor.RED + "Failed to apply armor thief to " + hiddenAstrixedName + ", because is out of range");
	                    return;
	                }
	                /*if (damager.getItemInHand().getAmount() > 1) {
	                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    damager.setItemInHand(new ItemStack(Material.AIR, 1));
	                }*/
	                
	                //if (chance > 65) {
	                	damaged.sendMessage(Color.translate("&7&m---------------------------"));
		                damaged.sendMessage(Color.translate("&eSomeone want thief your &d&ohelmet!"));
		                damaged.sendMessage(Color.translate("&7&m---------------------------"));
		                new BukkitRunnable() {
		                    public void run() {
		                    	ItemStack helmet = damaged.getInventory().getHelmet();
		                    	if (helmet.getType() == Material.DIAMOND_HELMET) {
			                        World world = damaged.getWorld();
			                        Map<Integer, ItemStack> excess = damaged.getInventory().addItem(helmet);
			                        for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
			                            world.dropItemNaturally(damaged.getLocation(), excessItemStack.getValue());
			                        }
			                        damaged.getInventory().setHelmet(new ItemStack(Material.AIR, 1));
			                        damaged.updateInventory();
			                        damager.sendMessage(Color.translate("&7&m---------------------------"));
			                        damager.sendMessage(Color.translate("&eYou have thefted &d" + hiddenAstrixedName + "'s &ehelmet!"));
			                        damager.sendMessage(Color.translate("&7&m---------------------------"));
			                        setCooldown(damager, damager.getUniqueId(), defaultCooldown, true);
			                    } else {
			                    	damager.sendMessage(Color.translate("&cYou only can thief diamond helmets!!"));
			                    }
		                	}
		                }.runTaskLater((Plugin)Main.getPlugin(), 15L);
		                
		            /*} else {
		            	damaged.sendMessage(Color.translate("&7&m---------------------------"));
		            	damaged.sendMessage(Color.translate("&eSomeone want thief your &d&oboots!"));
		            	damaged.sendMessage(Color.translate("&7&m---------------------------"));
		                new BukkitRunnable() {
		                    public void run() {
		                    	ItemStack boots = damaged.getInventory().getBoots();
		                    	if (boots.getType() == Material.DIAMOND_BOOTS) {
			                        World world = damaged.getWorld();
			                        Map<Integer, ItemStack> excess = damaged.getInventory().addItem(boots);
			                        for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
			                            world.dropItemNaturally(damaged.getLocation(), excessItemStack.getValue());
			                        }
			                        damaged.getInventory().setBoots(new ItemStack(Material.AIR, 1));
			                        damaged.updateInventory();
			                        damager.sendMessage(Color.translate("&7&m---------------------------"));
			                        damager.sendMessage(Color.translate("&eYou have thefted &d" + damaged.getName() + "'s &eboots!"));
			                        damager.sendMessage(Color.translate("&7&m---------------------------"));
			                        setCooldown(damager, damager.getUniqueId(), defaultCooldown, true);
			                    } else {
			                    	damager.sendMessage(Color.translate("&cYou only can thief diamond boots!!"));
			                    }
		                    }
		                }.runTaskLater((Plugin)Main.getPlugin(), 15L);*/
		            //}
	            }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final ArmorThief ArmorThief = new ArmorThief(player.getItemInHand());
        final long remaining = this.getRemaining(player);
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (ArmorThief.isArmorThief() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && remaining > 0L) {
            ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
            event.setCancelled(true);
        }
    }
}
