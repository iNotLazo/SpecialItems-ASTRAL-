package me.iNotLazo.SpecialItems.utils;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.iNotLazo.SpecialItems.Main;
import me.joeleoli.ragespigot.event.potion.PotionEffectExpireEvent;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class EffectRestorer implements Listener {

	private Table<UUID, PotionEffectType, PotionEffect> restores = HashBasedTable.create();

	public EffectRestorer(Main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void setRestoreEffect(Player player, PotionEffect effect) {
		boolean shouldCancel = true;
				
		Collection<PotionEffect> activeList = player.getActivePotionEffects();
		for (PotionEffect active : activeList) {
			
			if(!active.getType().equals(effect.getType())) continue;

			// If the current potion effect has a higher amplifier, ignore this one.
			if (effect.getAmplifier() < active.getAmplifier()) {
				return;
			} else if (effect.getAmplifier() == active.getAmplifier()) {
				// If the current potion effect has a longer duration, ignore this one.
				if (effect.getDuration() < active.getDuration()) {
					return;
				}
			}

			restores.put(player.getUniqueId(), active.getType(), active);
			shouldCancel = false;
			break;
		}

		// Cancel the previous restore.
		player.addPotionEffect(effect, true);
		if (shouldCancel) {
			restores.remove(player.getUniqueId(), effect.getType());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPotionEffectExpire(PotionEffectExpireEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if ((livingEntity instanceof Player)) {
			final Player player = (Player) livingEntity;
			final PotionEffect previous = (PotionEffect) this.restores.remove(player.getUniqueId(),
					event.getEffect().getType());
			if (previous != null) {
				event.setCancelled(true);
				new BukkitRunnable() {
					public void run() {
						player.addPotionEffect(previous, true);
					}
				}.runTask(Main.getPlugin());
			}
		}
	}
}