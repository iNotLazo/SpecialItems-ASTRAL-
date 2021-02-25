package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.inventory.*;
import org.bukkit.material.Cauldron;
import org.bukkit.material.MaterialData;

import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;

import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.block.*;
import org.bukkit.scheduler.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class SuperStandTimer extends PlayerTimer implements Listener
{
    public SuperStandTimer() {
        super("SuperStand", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("SuperStand")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void totalBreak(final BlockBreakEvent e) {
        final Block b = e.getBlock();
        if (b.getType() == Material.BREWING_STAND && b.hasMetadata("superstand")) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void totalBreak(final BlockPlaceEvent e) {
        final Block b = e.getBlock();
        final SuperStand SuperStand = new SuperStand(e.getPlayer().getItemInHand());
        if (b.getType() == Material.BREWING_STAND && SuperStand.isSuperStand()) {
            e.setCancelled(true);
        }
    }
    
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            if(block.getType() == Material.BREWING_STAND && block.hasMetadata("superstand")) {
            	event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlockPlaced();
        final SuperStand SuperStand = new SuperStand(player.getItemInHand());
        if (SuperStand.isSuperStand()) {
            event.setCancelled(true);
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
                final Location cord = new Location(player.getLocation().getWorld(), block.getLocation().getX(), block.getLocation().getY() + 1.0, block.getLocation().getZ());
                final Block block2 = cord.getBlock();
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        player.sendMessage(me.iNotLazo.HCF.utils.chat.Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	                player.sendMessage(Color.translate("&aYour potions will be ready in 3 seconds!!"));
	                if (!block2.getType().isSolid()) {
	                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                    block2.setType(Material.BREWING_STAND);
	                    block2.setMetadata("superstand", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(), (Object)true));
	                    final BrewingStand chest = (BrewingStand)block2.getState();
	                    final ItemStack health = new ItemStack(Material.POTION, 10, (short)16421);
	                    final ItemStack speed = new ItemStack(Material.POTION, 3, (short)16418);
	                    final ItemStack invis = new ItemStack(Material.POTION, 3, (short)16430);
	                    chest.getInventory().setItem(0, health);
	                    chest.getInventory().setItem(1, speed);
	                    chest.getInventory().setItem(2, invis);
	                    event.setCancelled(true);
	                }
	                event.setCancelled(true);
	                new BukkitRunnable() {
	                    public void run() {
	                        player.playSound(block2.getLocation(), Sound.NOTE_PLING, 3.0f, 3.0f);
	                        player.playEffect(block2.getLocation().add(0.0, 1.0, 0.0), Effect.LAVA_POP, 5);
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 15L);
	                new BukkitRunnable() {
	                    public void run() {
	                        player.playSound(block2.getLocation(), Sound.NOTE_PLING, 4.0f, 4.0f);
	                        player.playEffect(block2.getLocation().add(0.0, 1.0, 0.0), Effect.LAVA_POP, 5);
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 35L);
	                new BukkitRunnable() {
	                    public void run() {
	                        player.playSound(block2.getLocation(), Sound.NOTE_PLING, 5.0f, 5.0f);
	                        player.playEffect(block2.getLocation().add(0.0, 1.0, 0.0), Effect.LAVA_POP, 5);
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 50L);
	                new BukkitRunnable() {
	                    public void run() {
	                        player.playSound(block2.getLocation(), Sound.EXPLODE, 3.0f, 3.0f);
	                        player.playEffect(block2.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 5);
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 62L);
	                new BukkitRunnable() {
	                    public void run() {
	                        if (block2.getType() == Material.BREWING_STAND) {
	                            block2.setType(Material.AIR);
	                            player.sendMessage(Color.translate("&a&lTime is up &l:)"));
	                        }
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 60L);
	            }
            }
        }
    }
}
