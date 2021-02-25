package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.player.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.entity.*;
import java.util.*;

public class SwitcherBallTimer extends PlayerTimer implements Listener
{
    public SwitcherBallTimer() {
        super("Switcher", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("Switcher")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.GOLD;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final SwitcherBall Switcher = new SwitcherBall(player.getItemInHand());
        if (Switcher.isSwitcherBall() && action.equals(Action.RIGHT_CLICK_AIR)) {
            Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (factionAt instanceof CitadelFaction) {
    	        player.sendMessage(me.iNotLazo.HCF.utils.chat.Color.translate("&CYou can't use this item in a citadel event!"));
    			event.setCancelled(true);
            } else {
	            final long remaining = this.getRemaining(player);
	            if (remaining > 0L) {
	                ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
	                event.setCancelled(true);
	            }
            }
        }
    }
    
    @SuppressWarnings("unused")
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Snowball) {
            final Snowball snowball = (Snowball)event.getDamager();
            if (snowball.getShooter() instanceof Player) {
                final Player shooter = (Player)snowball.getShooter();
                final ItemStack itemStack = shooter.getItemInHand();
                final SwitcherBall SwitcherBall = new SwitcherBall(itemStack);
                if (snowball.hasMetadata("switcher") && event.getEntity() instanceof Player) {
                	final Player entity = this.getPlayerInSight(shooter, 9);
                    String hiddenAstrixedName = entity.hasPotionEffect(PotionEffectType.INVISIBILITY) ? "???" : entity.getName();
                    if (entity == null) {
                        ChatUtil.sendMessage(shooter, "&cFailed to swap places with " + hiddenAstrixedName + " as they're out of range.");
                        return;
                    }
	                    final Location playerLoc = shooter.getLocation().clone();
	                    final Location entityLoc = entity.getLocation().clone();
	                    final Vector playerLook = playerLoc.getDirection();
	                    final Vector playerVec = playerLoc.toVector();
	                    final Vector entityVec = entityLoc.toVector();
	                    final Vector toVec = playerVec.subtract(entityVec).normalize();
	                    entity.teleport(playerLoc.setDirection(playerLook.normalize()));
	                    shooter.teleport(entityLoc.setDirection(toVec));
	                    ChatUtil.sendMessage(shooter, "&aYou've swapped places with " + hiddenAstrixedName);
	                    entity.playSound(entity.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);
	                    shooter.playSound(shooter.getLocation(), Sound.ANVIL_BREAK, 2.0f, 2.0f);
                }
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onProjectileLaunch(final ProjectileLaunchEvent event) {
        final Projectile projectile = event.getEntity();
        if (projectile instanceof Snowball) {
            final Snowball snowball = (Snowball)projectile;
            final ProjectileSource source = snowball.getShooter();
            if (source instanceof Player) {
                final Player shooter = (Player)source;
                final ItemStack itemStack = shooter.getItemInHand();
                final SwitcherBall SwitcherBall = new SwitcherBall(itemStack);
                if (SwitcherBall.isSwitcherBall()) {
                    final long remaining = this.getRemaining(shooter);
                    if (remaining > 0L) {
                        event.setCancelled(true);
                        shooter.getInventory().addItem(new ItemStack[] { SwitcherBall.getItemStack() });
                        shooter.updateInventory();
                        ChatUtil.sendMessage(shooter, "&cYou cannot use &r" + this.getDisplayName(true) + " &cfor another " + DurationFormatter.getRemaining(remaining, true, false) + ".");
                        return;
                    }
                    snowball.setMetadata("switcher", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(), (Object)true));
                    this.setCooldown(shooter, shooter.getUniqueId(), this.defaultCooldown, true);
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
