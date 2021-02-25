package me.iNotLazo.SpecialItems.command.impl;

import me.iNotLazo.SpecialItems.command.wrapper.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;
import org.bukkit.inventory.*;
import me.iNotLazo.SpecialItems.impl.*;

public class SpecialItemsCommand extends DefaultCommandExecutor
{
    public SpecialItemsCommand() {
        super("specialitems", "Give power items to players.");
        this.setPermission("command.poweritems");
    }
    
    @Override
    public boolean execute(final CommandSender commandSender, final String label, final String[] arguments) {
        if (!(commandSender instanceof Player)) {
            final int amount = Integer.parseInt(arguments[2]);
            final Player player = Bukkit.getPlayer(arguments[1]);
            if (arguments.length == 0) {
                this.asyncSendingUsage(commandSender, String.valueOf('/') + label + " <pocketbard|impaler|ninjabone|faithfish|disturb|armorthief|trapaxe|antipearl|fakepearl|antitrapstar|instanthome|spacerocket|viewchange|fastcarrot|superstand|fleececustome|stellarpearls|switcher|grapplinghook|immobilizer> <player> <amount>");
                return false;
            }
            if (Bukkit.getPlayer(arguments[1]) == null) {
                ChatUtil.sendMessage(commandSender, "&cInvalid Player");
                return false;
            }
            if (!isStringInteger(arguments[2])) {
                player.sendMessage(Color.translate("&cThis must be an integer!"));
                return false;
            }
            if (amount < 0) {
                player.sendMessage(Color.translate("&cThis must be an integer!"));
                return false;
            }
            if (arguments[0].equalsIgnoreCase("pocketbard") || arguments[0].equalsIgnoreCase("bard")) {
                player.getInventory().addItem(new ItemStack[] { PocketBard.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("impaler")) {
                player.getInventory().addItem(new ItemStack[] { Impaler.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("enderpearl") || arguments[0].equalsIgnoreCase("pearls") || arguments[0].equalsIgnoreCase("stellarpearls") || arguments[0].equalsIgnoreCase("stellarpearl")) {
                player.getInventory().addItem(new ItemStack[] { StellarPearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("faithfish")) {
                player.getInventory().addItem(new ItemStack[] { FaithFish.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("armorthief")) {
                player.getInventory().addItem(new ItemStack[] { ArmorThief.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("disturb")) {
                player.getInventory().addItem(new ItemStack[] { Disturb.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("trapaxe")) {
                player.getInventory().addItem(new ItemStack[] { TrapAxe.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("antipearl")) {
                player.getInventory().addItem(new ItemStack[] { AntiPearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fakepearl")) {
                player.getInventory().addItem(new ItemStack[] { FakePearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("antitrapstar")) {
                player.getInventory().addItem(new ItemStack[] { AntiTrapStar.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("instanthome") || arguments[0].equalsIgnoreCase("ihome")) {
                player.getInventory().addItem(new ItemStack[] { InstantHome.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("spacerocket") || arguments[0].equalsIgnoreCase("rocket")) {
                player.getInventory().addItem(new ItemStack[] { SpaceRocket.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fastcarrot") || arguments[0].equalsIgnoreCase("carrot")) {
                player.getInventory().addItem(new ItemStack[] { FastCarrot.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("superstand") || arguments[0].equalsIgnoreCase("potsstand")) {
                player.getInventory().addItem(new ItemStack[] { SuperStand.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("grappling") || arguments[0].equalsIgnoreCase("grapplinghook") || arguments[0].equalsIgnoreCase("gh")) {
                player.getInventory().addItem(new ItemStack[] { GrapplingHook.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fleececustome") || arguments[0].equalsIgnoreCase("fleece")) {
                player.getInventory().addItem(new ItemStack[] { FleeceCostume.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("switcher")) {
                player.getInventory().addItem(new ItemStack[] { SwitcherBall.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("viewchange") || arguments[0].equalsIgnoreCase("viewchanger") || arguments[0].equalsIgnoreCase("view")) {
                player.getInventory().addItem(new ItemStack[] { ViewChanger.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("immobilizer") || arguments[0].equalsIgnoreCase("immo")) {
                player.getInventory().addItem(new ItemStack[] { Immobilizer.getItem(amount) });
                return false;
            }
            return true;
        }
        else {
            if (arguments.length == 0) {
                this.asyncSendingUsage(commandSender, String.valueOf('/') + label + " <pocketbard|impaler|ninjabone|faithfish|disturb|armorthief|trapaxe|antipearl|fakepearl|antitrapstar|instanthome|spacerocket|viewchange|fastcarrot|superstand|fleececustome|stellarpearls|switcher|grapplinghook|immobilizer>");
                return false;
            }
            final int amount = Integer.parseInt(arguments[2]);
            final Player player = Bukkit.getPlayer(arguments[1]);
            if (arguments[0].equalsIgnoreCase("impaler")) {
                player.getInventory().addItem(new ItemStack[] { Impaler.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("pocketbard") || arguments[0].equalsIgnoreCase("bard")) {
                player.getInventory().addItem(new ItemStack[] { PocketBard.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("enderpearl") || arguments[0].equalsIgnoreCase("pearls") || arguments[0].equalsIgnoreCase("stellarpearls") || arguments[0].equalsIgnoreCase("stellarpearl")) {
                player.getInventory().addItem(new ItemStack[] { StellarPearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("faithfish")) {
                player.getInventory().addItem(new ItemStack[] { FaithFish.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("trapaxe")) {
                player.getInventory().addItem(new ItemStack[] { TrapAxe.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("disturb")) {
                player.getInventory().addItem(new ItemStack[] { Disturb.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("armorthief")) {
                player.getInventory().addItem(new ItemStack[] { ArmorThief.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("antipearl")) {
                player.getInventory().addItem(new ItemStack[] { AntiPearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fakepearl")) {
                player.getInventory().addItem(new ItemStack[] { FakePearl.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("antitrapstar")) {
                player.getInventory().addItem(new ItemStack[] { AntiTrapStar.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("instanthome") || arguments[0].equalsIgnoreCase("ihome")) {
                player.getInventory().addItem(new ItemStack[] { InstantHome.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("spacerocket") || arguments[0].equalsIgnoreCase("rocket")) {
                player.getInventory().addItem(new ItemStack[] { SpaceRocket.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fastcarrot") || arguments[0].equalsIgnoreCase("carrot")) {
                player.getInventory().addItem(new ItemStack[] { FastCarrot.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("superstand") || arguments[0].equalsIgnoreCase("potsstand")) {
                player.getInventory().addItem(new ItemStack[] { SuperStand.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("grappling") || arguments[0].equalsIgnoreCase("grapplinghook") || arguments[0].equalsIgnoreCase("gh")) {
                player.getInventory().addItem(new ItemStack[] { GrapplingHook.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("fleececustome") || arguments[0].equalsIgnoreCase("fleece")) {
                player.getInventory().addItem(new ItemStack[] { FleeceCostume.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("switcher")) {
                player.getInventory().addItem(new ItemStack[] { SwitcherBall.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("viewchange") || arguments[0].equalsIgnoreCase("viewchanger") || arguments[0].equalsIgnoreCase("view")) {
                player.getInventory().addItem(new ItemStack[] { ViewChanger.getItem(amount) });
                return false;
            }
            if (arguments[0].equalsIgnoreCase("immobilizer") || arguments[0].equalsIgnoreCase("immo")) {
                player.getInventory().addItem(new ItemStack[] { Immobilizer.getItem(amount) });
                return false;
            }
            return true;
        }
    }
    
    public static boolean isStringInteger(final String string) {
        try {
            Integer.parseInt(string);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
