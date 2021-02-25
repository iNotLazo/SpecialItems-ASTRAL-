package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.player.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.entity.*;
import me.activated.core.plugin.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.event.*;

public class FleeceCostumeTimer extends PlayerTimer implements Listener
{
    public FleeceCostumeTimer() {
        super("FleeceCostume", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("FleeceCostume")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final FleeceCostume FleeceCostume = new FleeceCostume(player.getItemInHand());
        if (FleeceCostume.isFleeceCostume() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
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
	                    if (AquaCoreAPI.INSTANCE.getPlayerData(players.getUniqueId()).isVanished()) {
	                        continue;
	                    }
	                    players.hidePlayer(player);
	                    players.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
	                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                    if (player.getItemInHand().getAmount() > 1) {
	                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	                    }
	                    else {
	                        player.setItemInHand(new ItemStack(Material.AIR, 1));
	                    }
	                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 125, 0), true);
	                    players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.FIREWORKS_SPARK, 3);
	                    players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.FIREWORKS_SPARK, 3);
	                    players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 3);
	                    players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.FIREWORKS_SPARK, 5);
	                    players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.FIREWORKS_SPARK, 5);
	                    players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 5);
	                    event.setCancelled(true);
	                }
	                new BukkitRunnable() {
	                    public void run() {
	                        FleeceCostumeTimer.this.afterFiveSeconds(player);
	                    }
	                }.runTaskLater((Plugin)Main.getPlugin(), 125L);
	            }
            }
        }
    }
    
    public void afterFiveSeconds(final Player player) {
        for (final Player players : Bukkit.getServer().getOnlinePlayers()) {
            if (!AquaCoreAPI.INSTANCE.getPlayerData(players.getUniqueId()).isVanished()) {
                players.showPlayer(player);
            }
            players.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 5.0f, 5.0f);
            players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.FIREWORKS_SPARK, 3);
            players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.FIREWORKS_SPARK, 3);
            players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 3);
            players.playEffect(player.getLocation().add(0.0, 2.0, 0.0), Effect.FIREWORKS_SPARK, 5);
            players.playEffect(player.getLocation().add(0.0, 1.5, 0.0), Effect.FIREWORKS_SPARK, 5);
            players.playEffect(player.getLocation().add(0.0, 1.0, 0.0), Effect.FIREWORKS_SPARK, 5);
        }
    }
}
