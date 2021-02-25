package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.HCF.utils.chat.Color;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import me.iNotLazo.SpecialItems.impl.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class FakePearlTimer extends PlayerTimer implements Listener
{
    public final ArrayList<UUID> falsepearlList;
    
    public FakePearlTimer() {
        super("FakePearl", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("FakePearl")));
        this.falsepearlList = new ArrayList<UUID>();
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.RED;
    }
    
    @EventHandler
    public void onPearlLaunch(final ProjectileLaunchEvent event) {
        final Player player = (Player)event.getEntity().getShooter();
        if (!(event.getEntity() instanceof EnderPearl) || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final FakePearl fpearl = new FakePearl(player.getItemInHand());
        if (fpearl.isFakePearl()) {
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
	                this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                this.falsepearlList.add(player.getUniqueId());
	                event.getEntity().setMetadata("fakepearl", (MetadataValue)new FixedMetadataValue((Plugin)Main.getPlugin(), (Object)true));
	            }
            }
        }
    }
    
    @EventHandler
    public void onPearlLand(final PlayerTeleportEvent event) {
        if (this.falsepearlList.contains(event.getPlayer().getUniqueId()) && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            this.falsepearlList.remove(event.getPlayer().getUniqueId());
            event.setCancelled(true);
        }
    }
}
