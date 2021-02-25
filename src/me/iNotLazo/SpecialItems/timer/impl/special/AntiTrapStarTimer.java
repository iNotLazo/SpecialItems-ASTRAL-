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
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;

public class AntiTrapStarTimer extends PlayerTimer implements Listener
{
    public final Map<UUID, Long> enemycooldown;
    
    public AntiTrapStarTimer() {
        super("AntiTrapStar", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("AntiTrapStar")));
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
            event.getPlayer().sendMessage(Color.translate("&cYou can't Place blocks right now!"));
            event.setCancelled(true);
        }
    }
    
    @SuppressWarnings("unused")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player damager = (Player)event.getDamager();
            final Player damaged = (Player)event.getEntity();
            final AntiTrapStar AntiTrapStar = new AntiTrapStar(damager.getItemInHand());
            final long remaining = this.getRemaining(damager);
            if (remaining <= 0L && AntiTrapStar.isAntiTrapStar()) {
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(damager.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        damager.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	            	String hiddenAstrixedName = damaged.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : event.getEntity().getName();
	                if (event.getEntity() instanceof Player && damaged == null) {
	                    damager.sendMessage(ChatColor.RED + "Failed to apply antitrap to " + hiddenAstrixedName + ", because is out of range");
	                    return;
	                }
	                damager.sendMessage(ChatColor.RED + "Now " + ChatColor.GOLD + hiddenAstrixedName + ChatColor.RED + " cannot place blocks " + ChatColor.GRAY + "(45 Seconds)");
	                damaged.sendMessage(Color.translate("&cNow you can't place blocks &7(45 seconds)"));
	                if (damager.getItemInHand().getAmount() > 1) {
	                    damager.getItemInHand().setAmount(damager.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    damager.setItemInHand(new ItemStack(Material.AIR, 1));
	                }
	                this.cancelPlaceBlocks(damaged.getUniqueId(), 45000L);
	                this.setCooldown(damager, damager.getUniqueId(), this.defaultCooldown, true);
                }
            }
        }
    }
    
    public void cancelPlaceBlocks(final UUID uuid, final long delay) {
        this.enemycooldown.put(uuid, System.currentTimeMillis() + delay);
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final Long lastInteractTime = this.enemycooldown.get(player.getUniqueId());
        if (lastInteractTime != null && lastInteractTime - System.currentTimeMillis() > 0L 
        		&& (event.getClickedBlock().getType() == Material.ACACIA_FENCE_GATE
        		|| event.getClickedBlock().getType() == Material.BIRCH_FENCE_GATE
        		|| event.getClickedBlock().getType() == Material.DARK_OAK_FENCE_GATE
        		|| event.getClickedBlock().getType() == Material.FENCE_GATE
        		|| event.getClickedBlock().getType() == Material.JUNGLE_FENCE_GATE
        		|| event.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE)) {
            event.getPlayer().sendMessage(Color.translate("&cYou can't open fences right now!"));
            event.setCancelled(true);
        }
        final AntiTrapStar AntiTrapStar = new AntiTrapStar(player.getItemInHand());
        final long remaining = this.getRemaining(player);
        if (player.getGameMode().equals((Object)GameMode.CREATIVE)) {
            return;
        }
        if (AntiTrapStar.isAntiTrapStar() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) && remaining > 0L) {
            ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
            event.setCancelled(true);
        }
    }
}
