package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.commands.arguments.essentials.chat.FocusCommand;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.type.PlayerFaction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.playerdata.PlayerData;
import me.iNotLazo.HCF.scoreboard.PlayerScoreboard;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.entity.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;

import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.event.*;

public class PocketBardTimer extends PlayerTimer implements Listener
{
    
	private static double TEAMMATE_NEARBY_RADIUS = 30;
	private Inventory inv;
	private HashMap<Player, Location> lastLoc = new HashMap<Player, Location>();
	private ItemStack Glass = new ItemBuilder(Material.STAINED_GLASS_PANE, "", 1, (byte) 1).getItem();
	
    public PocketBardTimer() {
        super("PocketBard", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("PocketBard")));
        this.inv = Bukkit.createInventory(null, 27, Color.translate("&6&lPocket Bard"));
    }
    
    public org.bukkit.ChatColor getScoreboardPrefix() {
        return org.bukkit.ChatColor.YELLOW;
    }
	
	public Inventory openMainInventory(Player player) {

		for(int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, this.Glass);
		}
		
		inv.setItem(10, new ItemBuilder(Material.BLAZE_POWDER, Color.translate("&eStrength II"), 1).getItem());
		inv.setItem(11, new ItemBuilder(Material.SUGAR, Color.translate("&eSpeed III"), 1).getItem());
		inv.setItem(12, new ItemBuilder(Material.IRON_INGOT, Color.translate("&eResistance III"), 1).getItem());
		inv.setItem(13, new ItemBuilder(Material.GHAST_TEAR, Color.translate("&eRegeneration III"), 1).getItem());
		inv.setItem(14, new ItemBuilder(Material.MAGMA_CREAM, Color.translate("&eFire Resistance 1"), 1).getItem());
		inv.setItem(15, new ItemBuilder(Material.FERMENTED_SPIDER_EYE, Color.translate("&eInvisibility I"), 1).getItem());
		inv.setItem(16, new ItemBuilder(Material.FEATHER, Color.translate("&eJump Boost X"), 1).getItem());
		player.openInventory(inv);
		
		return inv;
	}
	
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final int chance = (int) (Math.random() * 100);
        final PocketBard PocketBard = new PocketBard(player.getItemInHand());
        if (PocketBard.isPocketBard() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (factionAt instanceof CitadelFaction) {
    	        player.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
    			event.setCancelled(true);
            } else {
	            final long remaining = this.getRemaining(player);
	            if (remaining > 0L) {
	                ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
	                event.setCancelled(true);
	            }
	            else {
	                if (player.getItemInHand().getAmount() > 1) {
	                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    player.setItemInHand(new ItemStack(Material.AIR, 1));
	                }
	                
	                openMainInventory(player);
	            }
            }
        }
    }
    
    @EventHandler
	public void onInventoryClick(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		Inventory inventory = event.getInventory();
		final long remaining = this.getRemaining(player);
		if(!inventory.getName().equals(Color.translate("&6&lPocket Bard"))) return;
		if(inventory.getName().equals(Color.translate("&6&lPocket Bard")) && !(remaining > 0L)) {
			World world = player.getWorld();
	        Map<Integer, ItemStack> excess = player.getInventory().addItem(new ItemStack[] { PocketBard.getItem(1) });
	        for (Map.Entry<Integer, ItemStack> excessItemStack : excess.entrySet()) {
	            world.dropItemNaturally(player.getLocation(), excessItemStack.getValue());
	        }
        }
    }
    
    @SuppressWarnings("unused")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		Inventory inventory = event.getInventory();

		/*EffectData bardEffect = this.bardEffects.get(item.getType());
		if (bardEffect == null || bardEffect.clickable == null) {
			event.setCancelled(true);
		}*/
		
		if(!inventory.getName().equals(Color.translate("&6&lPocket Bard"))) return;
		if(event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
		if(item.getType().equals(Material.AIR)) return;

		PlayerFaction playerFaction = HCFactions.getInstance().getFactionManager().getPlayerFaction(player.getUniqueId());
		if(event.getInventory().getTitle().equals(ChatColor.translateAlternateColorCodes('&', Color.translate("&6&lPocket Bard"))) && event.getCurrentItem() != null) {
			event.setCancelled(true);
			if(item.getType() == Material.BLAZE_POWDER) {
				if (playerFaction == null) {
					Main.getPlugin().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
	                this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                player.closeInventory();
	                PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
	    			data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
	    			HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
					}
			} else {
				for (Entity nearby : player.getNearbyEntities(TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS)) {
					if (nearby instanceof Player) {
						if (nearby != null) {
							Player nearbyPlayer = (Player) nearby;
		                    PlayerFaction nearbyFaction = HCFactions.getInstance().getFactionManager().getPlayerFaction(nearbyPlayer.getUniqueId());
		                    if (playerFaction != nearbyFaction) {
								continue;
		                    }
		                        
		                    Main.getPlugin().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
		                    Main.getPlugin().getEffectRestorer().setRestoreEffect(nearbyPlayer, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
		                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
		                    player.closeInventory();
		                    PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
		                    data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
		    				HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
						} else {
							Main.getPlugin().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
			                this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
			                player.closeInventory();
			                PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
			    			data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
			    			HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
						}
					}
				}
			}
		}
    }
}
			/*if (playerFaction != null && !HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
				if (item.getType() == Material.BLAZE_POWDER) {
					for (Entity nearby : player.getNearbyEntities(TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS)) {
                        if (nearby instanceof Player) {
                            Player nearbyPlayer = (Player) nearby;
                            PlayerFaction nearbyFaction = HCFactions.getInstance().getFactionManager().getPlayerFaction(nearbyPlayer.getUniqueId());
                            if (playerFaction != nearbyFaction) {
								continue;
                            }
                            
                            HCFactions.getInstance().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                            HCFactions.getInstance().getEffectRestorer().setRestoreEffect(nearbyPlayer, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
                            new BukkitRunnable() {
			                    public void run() {
			                    	player.closeInventory();
			                    }
			                }.runTaskLater((Plugin)HCFactions.getInstance(), 2L);
                            PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
        					data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
        					HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
                        }
					}
				} if (item.getType() == Material.FERMENTED_SPIDER_EYE) {
					for (Entity nearby : player.getNearbyEntities(TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS)) {
                        if (nearby instanceof Player) {
                            Player nearbyPlayer = (Player) nearby;
                            PlayerFaction nearbyFaction = HCFactions.getInstance().getFactionManager().getPlayerFaction(nearbyPlayer.getUniqueId());
                            if (playerFaction != nearbyFaction) {
								continue;
                            }
                            HCFactions.getInstance().getEffectRestorer().setRestoreEffect(nearbyPlayer, new PotionEffect(PotionEffectType.INVISIBILITY, 900, 0));
                            HCFactions.getInstance().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INVISIBILITY, 900, 0));
                            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
                            new BukkitRunnable() {
			                    public void run() {
			                    	player.closeInventory();
			                    }
			                }.runTaskLater((Plugin)HCFactions.getInstance(), 2L);
                            PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
        					data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
        					HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
                        	new BukkitRunnable() {
                        	@SuppressWarnings("deprecation")
                    			public void run() {
                    				for(PlayerScoreboard board : HCFactions.getInstance().getScoreboardTagEvents().getSBData().values()) {
                    					board.addUpdate(player);
                    				}

                    				HCFactions.getInstance().getScoreboardTagEvents().getSBData().get(player.getUniqueId()).addUpdate(player);
                    				
                    				PlayerFaction faction = HCFactions.getInstance().getFactionManager().getPlayerFaction(player);
                    				
                    				if(FocusCommand.focus.containsKey(faction)) {
                    		        	Player target = Bukkit.getPlayer(FocusCommand.focus.get(faction));
                    		        	if(target == null) {
                    		        		return;
                    		        	}
                    		        }
                    			}
                    		}.runTaskLaterAsynchronously(HCFactions.getInstance(), 5L);
						}
					}
				}
			}
			
			if (playerFaction == null && !HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation()).isSafezone()) {
				if (item.getType() != Material.FERMENTED_SPIDER_EYE) {
					for (Entity nearby : player.getNearbyEntities(TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS)) {
                        if (nearby instanceof Player) {
                            
                        	HCFactions.getInstance().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 1));
                            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
                            new BukkitRunnable() {
			                    public void run() {
			                    	player.closeInventory();
			                    }
			                }.runTaskLater((Plugin)HCFactions.getInstance(), 2L);
                            PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
        					data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
        					HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
                        }
					}
				} if (item.getType() == Material.FERMENTED_SPIDER_EYE) {
					for (Entity nearby : player.getNearbyEntities(TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS, TEAMMATE_NEARBY_RADIUS)) {
                        if (nearby instanceof Player) {
                        	HCFactions.getInstance().getEffectRestorer().setRestoreEffect(player, new PotionEffect(PotionEffectType.INVISIBILITY, 900, 0));
                            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
                            new BukkitRunnable() {
			                    public void run() {
			                    	player.closeInventory();
			                    }
			                }.runTaskLater((Plugin)HCFactions.getInstance(), 2L);
                            PlayerData data = HCFactions.getInstance().getPlayerData().getPlayer(player);
        					data.setSpawnTagCooldown(HCFactions.getInstance().getTimerManager().getSpawnTagHandler().getTime());
        					HCFactions.getInstance().getTimerManager().getSpawnTagHandler().setCooldown(player, player.getUniqueId());
                            
                        	new BukkitRunnable() {
                        	@SuppressWarnings("deprecation")
                    			public void run() {
                    				for(PlayerScoreboard board : HCFactions.getInstance().getScoreboardTagEvents().getSBData().values()) {
                    					board.addUpdate(player);
                    				}

                    				HCFactions.getInstance().getScoreboardTagEvents().getSBData().get(player.getUniqueId()).addUpdate(player);
                    			}
                    		}.runTaskLaterAsynchronously(HCFactions.getInstance(), 5L);
						}
					}
				}
			}*/