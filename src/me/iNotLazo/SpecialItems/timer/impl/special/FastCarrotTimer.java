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
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class FastCarrotTimer extends PlayerTimer implements Listener
{
    public FastCarrotTimer() {
        super("FastCarrot", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("FastCarrot")));
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final FastCarrot FastCarrot = new FastCarrot(player.getItemInHand());
        if (FastCarrot.isFastCarrot() && (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
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
	                this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 75, 3), true);
	                event.setCancelled(true);
	            }
	        }
        }
    }
}
