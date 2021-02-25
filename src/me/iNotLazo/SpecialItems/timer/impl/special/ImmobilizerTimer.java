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
import org.bukkit.potion.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class ImmobilizerTimer extends PlayerTimer implements Listener
{
    public ImmobilizerTimer() {
        super("Immobilizer", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("Immobilizer")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.DARK_RED;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final Immobilizer immo = new Immobilizer(player.getItemInHand());
        if (immo.isImmobilizer() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            final long remaining = this.getRemaining(player);
            if (remaining > 0L) {
                ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
                event.setCancelled(true);
            }
        }
    }
    
    @SuppressWarnings("unused")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onClick(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getItemInHand();
        final Immobilizer immobilizer = new Immobilizer(itemStack);
        final Action action = event.getAction();
        if (immobilizer.isImmobilizer() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (factionAt instanceof CitadelFaction) {
    	        player.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
    			event.setCancelled(true);
            } else {
	        	final Player entity = this.getPlayerInSight(player, 7);
	            if (entity instanceof Player) {
		            if (entity == null) {
		            	ChatUtil.sendMessage(player, "&cThere is no one 6 block near of you!");
		            } else {
			            /*player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));
			            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 10));
			            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 200));*/
			            entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 200));
			            entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 10));
			            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10));
			            entity.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 120));
			            String hiddenAstrixedName = player.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : player.getName();
			            ChatUtil.sendMessage(entity, "&cYou've been immobilized by " + hiddenAstrixedName);
			            ChatUtil.sendMessage(player, "&aYou've immobilized all players that are in a range of 6 blocks.");
			            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
			            if (player.getItemInHand().getAmount() > 1) {
			                player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
			            }
			            else {
			                player.setItemInHand(new ItemStack(Material.AIR, 1));
			            }
		            }
	            }
            }
        }
    }
    
    public Player getPlayerInSight(final Player player, final int distance) {
        final Location playerLoc = player.getLocation();
        final Vector3D playerDirection = new Vector3D(playerLoc.getDirection());
        final Vector3D start = new Vector3D(playerLoc);
        final Vector3D end = start.add(playerDirection.multiply(distance));
        Player inSight = null;
        for (final Entity nearbyEntity : player.getNearbyEntities((double)distance, (double)distance, (double)distance)) {
            if (nearbyEntity.getType() == EntityType.PLAYER) {
                final Vector3D nearbyLoc = new Vector3D(nearbyEntity.getLocation());
                final Vector3D min = nearbyLoc.subtract(0.5, 1.6, 0.5);
                final Vector3D max = nearbyLoc.add(0.5, 0.3, 0.5);
                if (this.hasIntersection(start, end, min, max) && (inSight == null || inSight.getLocation().distanceSquared(playerLoc) > nearbyEntity.getLocation().distanceSquared(playerLoc))) {
                    inSight = (Player)nearbyEntity;
                    return inSight;
                }
                continue;
            }
        }
        return inSight;
    }
    
    private boolean hasIntersection(final Vector3D start, final Vector3D end, final Vector3D min, final Vector3D max) {
        final Vector3D vectorD = end.subtract(start).multiply(0.5);
        final Vector3D vectorE = max.subtract(min).multiply(0.5);
        final Vector3D vectorC = start.add(vectorD).subtract(min.add(max).multiply(0.5));
        final Vector3D vectorAbs = vectorD.abs();
        return Math.abs(vectorC.getX()) <= vectorE.getX() + vectorAbs.getX() && Math.abs(vectorC.getY()) <= vectorE.getY() + vectorAbs.getY() && Math.abs(vectorC.getZ()) <= vectorE.getX() + vectorAbs.getZ() && Math.abs(vectorD.getY() * vectorC.getZ() - vectorD.getZ() * vectorC.getY()) <= vectorE.getY() * vectorAbs.getZ() + vectorE.getZ() * vectorAbs.getY() + 9.999999747378752E-5 && Math.abs(vectorD.getZ() * vectorC.getX() - vectorD.getX() * vectorC.getZ()) <= vectorE.getZ() * vectorAbs.getX() + vectorE.getX() * vectorAbs.getZ() + 9.999999747378752E-5 && Math.abs(vectorD.getX() * vectorC.getY() - vectorD.getY() * vectorC.getX()) <= vectorE.getX() * vectorAbs.getY() + vectorE.getY() * vectorAbs.getX() + 9.999999747378752E-5;
    }
}
