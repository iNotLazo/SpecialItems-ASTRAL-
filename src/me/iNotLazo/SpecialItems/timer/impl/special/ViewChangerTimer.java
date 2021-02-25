package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import java.util.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.entity.*;
import me.iNotLazo.SpecialItems.impl.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.util.*;
import org.bukkit.util.Vector;
import org.bukkit.event.*;

public class ViewChangerTimer extends PlayerTimer implements Listener
{
    private ArrayList<String> cooldown;
    public static final HashMap<Player, Integer> countdown;
    
    static {
        countdown = new HashMap<Player, Integer>();
    }
    
    public ViewChangerTimer() {
        super("ViewChanger", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("ViewChanger")));
        this.cooldown = new ArrayList<String>();
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler
    public void onHangingDamageByEntity(final EntityDamageByEntityEvent event) {
        final Entity damaged = event.getEntity();
        final Player player = BukkitUtils.getFinalAttacker((EntityDamageEvent)event, false);
        if (damaged instanceof Player && player instanceof Player) {
            final ViewChanger ViewChanger = new ViewChanger(player.getItemInHand());
            
            if (!ViewChanger.isViewChanger()) {
            	return;
            }
            
            if (ViewChanger.isViewChanger() && player != null && player instanceof Player && damaged instanceof Player) {
                final long remaining = this.getRemaining(player);
                if (remaining > 0L) {
                    ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
                }
                else {
                    Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
                    if (factionAt instanceof CitadelFaction) {
            	        player.sendMessage(me.iNotLazo.HCF.utils.chat.Color.translate("&CYou can't use this item in a citadel event!"));
            			event.setCancelled(true);
                    } else {
	                    if (player.getItemInHand().getAmount() > 1) {
	                        player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
	                    }
	                    else {
	                        player.setItemInHand(new ItemStack(Material.AIR, 1));
	                    }
	                    final Location playerLoc = player.getLocation().clone();
	                    final Location entityLoc = damaged.getLocation().clone();
	                    final Vector playerLook = playerLoc.getDirection();
	                    final Vector playerVec = playerLoc.toVector();
	                    final Vector entityVec = entityLoc.toVector();
	                    final Vector toVec = playerVec.subtract(entityVec).normalize();
	                    damaged.teleport(entityLoc.setDirection(playerLook.normalize()));
	                    this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	                }
                }
            }
        }
    }
}
