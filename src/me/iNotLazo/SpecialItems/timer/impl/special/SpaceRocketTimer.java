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
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.inventory.meta.*;

public class SpaceRocketTimer extends PlayerTimer implements Listener
{
    public SpaceRocketTimer() {
        super("SpaceRocket", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("SpaceRocket")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final SpaceRocket SpaceRocket = new SpaceRocket(player.getItemInHand());
        if (SpaceRocket.isSpaceRocket() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
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
	            else {
	                if (player.getItemInHand().getAmount() > 1) {
	                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	                }
	                else {
	                    player.setItemInHand(new ItemStack(Material.AIR, 1));
	                }
	                if (player.getVehicle() != null) {
	                    player.getVehicle().remove();
	                    player.eject();
	                }
	                event.setCancelled(true);
	                player.setVelocity(player.getLocation().getDirection().normalize().setY(2.5f));
	                player.setVelocity(player.getLocation().getDirection().normalize().multiply(2.6f));
	                this.firework(player);
	                this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                event.setCancelled(true);
	            }
	        }
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void fallDamage(final EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            final Player player = (Player)event.getEntity();
            final long remaining = this.getRemaining(player);
            if (remaining > this.defaultCooldown / 2L) {
                player.setFallDistance(0.0f);
                event.setDamage(event.getDamage() / 2.0);
                event.setCancelled(true);
            }
        }
    }
    
    public void firework(final Player player) {
        final Firework fw = (Firework)player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        final FireworkMeta fwmeta = fw.getFireworkMeta();
        final FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withTrail();
        builder.withFlicker();
        builder.withColor(Color.FUCHSIA);
        builder.withColor(Color.GREEN);
        builder.withColor(Color.LIME);
        builder.withColor(Color.BLUE);
        builder.withColor(Color.MAROON);
        builder.withColor(Color.ORANGE);
        builder.withColor(Color.PURPLE);
        builder.withColor(Color.WHITE);
        builder.with(FireworkEffect.Type.BALL_LARGE);
        fwmeta.addEffects(new FireworkEffect[] { builder.build() });
        fwmeta.setPower(0);
        fw.setFireworkMeta(fwmeta);
    }
}
