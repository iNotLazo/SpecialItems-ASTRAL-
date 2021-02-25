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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;

import org.bukkit.event.*;

public class AntiPearlTimer extends PlayerTimer implements Listener
{
    public final Map<UUID, Long> enemycooldown;
    
    public AntiPearlTimer() {
        super("AntiPearl", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("AntiPearl")));
        this.enemycooldown = new HashMap<UUID, Long>();
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void noPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Long lastInteractTime = this.enemycooldown.get(player.getUniqueId());
        if (lastInteractTime != null && lastInteractTime - System.currentTimeMillis() > 0L) {
            event.getPlayer().sendMessage(Color.translate("&cYou can't break blocks right now!"));
            event.setCancelled(true);
        }
    }
    
    @SuppressWarnings("unused")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player damager = (Player)event.getDamager();
            final Player damaged = (Player)event.getEntity();
            final AntiPearl AntiPearl = new AntiPearl(damager.getItemInHand());
            final long remaining = this.getRemaining(damager);
            if (remaining <= 0L && AntiPearl.isAntiPearl()) {
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(damager.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        damager.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	            	String hiddenAstrixedName = damaged.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : event.getEntity().getName();
	                if (event.getEntity() instanceof Player && damaged == null) {
	                    damager.sendMessage(ChatColor.RED + "Failed to apply antipearl to " + hiddenAstrixedName + ", because is out of range");
	                    return;
	                }
	                damager.sendMessage(ChatColor.RED + "Now " + ChatColor.GOLD + hiddenAstrixedName + ChatColor.RED + " cannot use pearls " + ChatColor.GRAY + "(15 Seconds)");
	                damaged.sendMessage(Color.translate("&cNow you can't use pearls &7(15 seconds)"));
	                if (damager.getItemInHand().getAmount() > 1) {
	                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    damager.setItemInHand(new ItemStack(Material.AIR, 1));
	                }
	                this.cancelUsePearls(damaged.getUniqueId(), 15000L);
	                this.setCooldown(damager, damager.getUniqueId(), this.defaultCooldown, true);
                }
            }
        }
    }
    
    public void cancelUsePearls(final UUID uuid, final long delay) {
        this.enemycooldown.put(uuid, System.currentTimeMillis() + delay);
    }
    
    @EventHandler
    public void onPearlLand(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Long lastInteractTime = this.enemycooldown.get(player.getUniqueId());
        if (lastInteractTime != null && lastInteractTime - System.currentTimeMillis() > 0L && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final AntiPearl AntiPearl = new AntiPearl(player.getItemInHand());
        final long remaining = this.getRemaining(player);
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (AntiPearl.isAntiPearl() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && remaining > 0L) {
            ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
            event.setCancelled(true);
        }
        final Long lastInteractTime = this.enemycooldown.get(player.getUniqueId());
        if (event.hasItem() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && event.getItem().getType().equals((Object)Material.ENDER_PEARL) && lastInteractTime != null && lastInteractTime - System.currentTimeMillis() > 0L) {
            event.setUseItemInHand(Event.Result.DENY);
            player.sendMessage(Color.translate("&cYou can't use pearls right now!"));
        }
    }
}
