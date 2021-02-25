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

public class DisturbTimer extends PlayerTimer implements Listener {
    
    public DisturbTimer() {
        super("Disturb", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("Disturb")));
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
            final Disturb Disturb = new Disturb(damager.getItemInHand());
            final long remaining = this.getRemaining(damager);
            if (remaining <= 0L && Disturb.isDisturb()) {
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(damager.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        damager.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	            	String hiddenAstrixedName = damaged.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : event.getEntity().getName();
	                if (event.getEntity() instanceof Player && damaged == null) {
	                    damager.sendMessage(ChatColor.RED + "Failed to apply disturb to " + hiddenAstrixedName + ", because is out of range");
	                    return;
	                }
	                if (damager.getItemInHand().getAmount() > 1) {
	                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    damager.setItemInHand(new ItemStack(Material.AIR, 1));
	                }
	                
		            damaged.sendMessage(Color.translate("&cSomeone want mess up your hotbar!"));
		            new BukkitRunnable() {
		                public void run() {
		                    ItemStack iteminhand = damaged.getInventory().getItemInHand();
		                    
		                    damaged.getWorld().dropItemNaturally(damaged.getLocation(), iteminhand);
		                    damaged.setItemInHand(new ItemStack(Material.AIR, 1));
		                    damaged.updateInventory();
		                    
			                damager.sendMessage(Color.translate("&eYou have messed up &d" + hiddenAstrixedName + "'s &ehotbar!"));
		                }
		            }.runTaskLater((Plugin)Main.getPlugin(), 15L);
		                
		            this.setCooldown(damager, damager.getUniqueId(), this.defaultCooldown, true);
	            }
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final Disturb Disturb = new Disturb(player.getItemInHand());
        final long remaining = this.getRemaining(player);
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (Disturb.isDisturb() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && remaining > 0L) {
            ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
            event.setCancelled(true);
        }
    }
}
