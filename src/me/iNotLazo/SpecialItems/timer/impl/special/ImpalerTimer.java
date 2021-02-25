package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;

import java.util.HashMap;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.iNotLazo.SpecialItems.impl.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.plugin.*;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.metadata.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class ImpalerTimer extends PlayerTimer implements Listener
{
	
    public ImpalerTimer() {
        super("Impaler", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("Impaler")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.GOLD;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final Impaler impaler = new Impaler(player.getItemInHand());
        if (impaler.isImpaler() && action.equals(Action.RIGHT_CLICK_AIR)) {
            Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (factionAt instanceof CitadelFaction) {
    	        player.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
    			event.setCancelled(true);
            } else {
	            final long remaining = this.getRemaining(player);
	            if (remaining > 0L) {
	                ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
	                event.setCancelled(true);
	            } else {
	            	this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	    	        if (player.getItemInHand().getAmount() > 1) {
	    	        	player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	    	        }
	    	        else {
	    	        	player.setItemInHand(new ItemStack(Material.AIR, 1));
	    	        }
	            	Snowball ball = player.launchProjectile(Snowball.class);
	            	ball.setShooter(player);
	            	ball.setMetadata("impaler", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(), (Object)true));
	            }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball) {
            final Snowball snowball = (Snowball)event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                final Player shooter = (Player)snowball.getShooter();
                if (snowball.hasMetadata("impaler") && event.getEntity() instanceof Player) {
                	final Player damaged = (Player) event.getEntity();
                	final Player entity = this.getPlayerInSight(shooter, 9);
                    if (entity == null) {
                    	String hiddenAstrixedName = damaged.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : event.getEntity().getName();
                        ChatUtil.sendMessage(shooter, "&cFailed to push " + hiddenAstrixedName + " as they're out of range.");
                        return;
                    }
	                entity.setVelocity(shooter.getLocation().getDirection().normalize().multiply(2.6f));
	                entity.playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);
	                shooter.playSound(shooter.getLocation(), Sound.ANVIL_BREAK, 2.0f, 2.0f);
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
