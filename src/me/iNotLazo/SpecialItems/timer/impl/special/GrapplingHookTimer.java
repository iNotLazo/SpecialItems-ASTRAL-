package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.util.*;

public class GrapplingHookTimer extends PlayerTimer implements Listener
{
    public GrapplingHookTimer() {
        super("GrapplingHook", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("GrapplingHook")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void fallDamage(final EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player)event.getEntity();
            final long remaining = this.getRemaining(player);
            if (remaining > 0L) {
                event.setDamage(event.getDamage() / 2.0);
            }
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final GrapplingHook grapplingHook = new GrapplingHook(player.getItemInHand());
        if (grapplingHook.isGrapplingHook() && action.equals(Action.RIGHT_CLICK_AIR)) {
            final long remaining = this.getRemaining(player);
            if (remaining > 0L) {
                ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void grappler(final PlayerFishEvent event) {
        final Player player = event.getPlayer();
        if (event.getState() != PlayerFishEvent.State.FISHING) {
            final GrapplingHook grapplingHook = new GrapplingHook(player.getItemInHand());
            if (grapplingHook.isGrapplingHook()) {
                Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
                if (factionAt instanceof CitadelFaction) {
        	        player.sendMessage(Color.translate("&CYou can't use this item in a citadel event!"));
        			event.setCancelled(true);
                } else {
	                final Location hookLocation = event.getHook().getLocation().clone().add(0.0, -1.0, 0.0);
	                if (hookLocation.getBlock().getType().isSolid()) {
	                    final Location playerLocation = player.getLocation();
	                    final Location location = event.getHook().getLocation();
	                    if (playerLocation.distance(location) < 3.0) {
	                        this.pullPlayerSlightly(player, location);
	                    }
	                    else {
	                        this.pullEntityToLocation((Entity)player, location);
	                        player.playSound(player.getLocation(), Sound.HORSE_WOOD, 10.0f, 1.0f);
	                        player.playEffect(player.getPlayer().getLocation(), Effect.FLAME, 100);
	                    }
	                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                }
                }
            }
        }
    }
    
    private void pullPlayerSlightly(final Player player, final Location location) {
        if (location.getY() > player.getLocation().getY()) {
            player.setVelocity(new Vector(0.0, 0.56, 0.0));
            return;
        }
        final Location playerLoc = player.getLocation();
        final Vector vector = location.toVector().subtract(playerLoc.toVector());
        player.setVelocity(vector);
    }
    
    private void pullEntityToLocation(final Entity entity, final Location location) {
        final Location entityLoc = entity.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5);
        entity.teleport(entityLoc);
        final double global = -0.08;
        final double t = location.distance(entityLoc);
        final double v_x = (1.0 + 0.07 * t) * (location.getX() - entityLoc.getX()) / t;
        final double v_y = (1.0 + 0.03 * t) * (location.getY() - entityLoc.getY()) / t - 0.5 * global * t;
        final double v_z = (1.0 + 0.07 * t) * (location.getZ() - entityLoc.getZ()) / t;
        final Vector vector = entity.getVelocity();
        vector.setX(v_x);
        vector.setY(v_y);
        vector.setZ(v_z);
        entity.setVelocity(vector);
    }
}
