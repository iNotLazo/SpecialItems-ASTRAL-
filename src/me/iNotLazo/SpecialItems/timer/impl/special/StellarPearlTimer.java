package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import me.iNotLazo.SpecialItems.impl.*;
import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.*;
import org.bukkit.inventory.*;

public class StellarPearlTimer extends PlayerTimer implements Listener
{
    public StellarPearlTimer() {
        super("StellarPearl", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("StellarPearl")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void onPlayerInteract(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlockPlaced();
        final StellarPearl StellarPearl = new StellarPearl(player.getItemInHand());
        if (StellarPearl.isStellarPearl()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final StellarPearl StellarPearl = new StellarPearl(player.getItemInHand());
        if (StellarPearl.isStellarPearl() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
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
