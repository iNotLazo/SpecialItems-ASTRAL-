package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.entity.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;

import org.bukkit.event.*;

public class FaithFishTimer extends PlayerTimer implements Listener {
	
    public FaithFishTimer() {
        super("FaithFish", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("FaithFish")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final FaithFish FaithFish = new FaithFish(player.getItemInHand());
        if (FaithFish.isFaithFish() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
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
	                for (final Player players : Bukkit.getServer().getOnlinePlayers()) {
	                    players.playSound(player.getLocation(), Sound.EAT, 1.0f, 1.0f);
	                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                    if (player.getItemInHand().getAmount() > 1) {
	                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	                    }
	                    else {
	                        player.setItemInHand(new ItemStack(Material.AIR, 1));
	                    }
	                    player.setHealth(20);
	                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 125, 2), true);
	                    players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.HEART, 3);
	                    players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.HEART, 3);
	                    players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.HEART, 3);
	                    players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.HEART, 5);
	                    players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.HEART, 5);
	                    players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.HEART, 5);
	                    event.setCancelled(true);
	                }
	            }
            }
        }
    }
}
