package me.iNotLazo.SpecialItems.command.wrapper;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import me.iNotLazo.SpecialItems.utils.*;
import java.util.*;
import java.io.*;

public class CommandWrapper implements CommandExecutor, TabCompleter
{
    private Collection<CommandArgument> commandArguments;
    
    public CommandWrapper(final Collection<CommandArgument> arguments) {
        this.commandArguments = arguments;
    }
    
    public static void printUsage(final CommandSender commandSender, final String label, final Collection<CommandArgument> arguments) {
        ChatUtil.sendMessage(commandSender, ChatColor.GRAY + ChatUtil.CHAT_BAR, "&6&l" + ChatUtil.capitalizeFullyWords(label) + " &6help");
        int amount = 0;
        for (final CommandArgument commandArgument : arguments) {
            final String permission = commandArgument.getPermission();
            if (permission != null && !commandSender.hasPermission(permission)) {
                continue;
            }
            ChatUtil.sendMessage(commandSender, "&e" + commandArgument.getUsage(label) + "&7, &f" + commandArgument.getDescription());
            amount = Integer.valueOf(amount + 1);
        }
        if (amount > 0) {
            ChatUtil.sendMessage(commandSender, ChatColor.GRAY + ChatUtil.CHAT_BAR);
        }
    }
    
    public static CommandArgument matchArgument(final String id, final CommandSender sender, final Collection<CommandArgument> arguments) {
        for (final CommandArgument commandArgument : arguments) {
            final String permission = commandArgument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                if (!commandArgument.getName().equalsIgnoreCase(id) && !Arrays.asList(commandArgument.getAliases()).contains(id)) {
                    continue;
                }
                return commandArgument;
            }
        }
        return null;
    }
    
    public static List<String> getAccessibleArgumentNames(final CommandSender sender, final Collection<CommandArgument> arguments) {
        final List<String> result = new ArrayList<String>();
        for (final CommandArgument commandArgument : arguments) {
            final String permission = commandArgument.getPermission();
            if (permission != null && !sender.hasPermission(permission)) {
                continue;
            }
            result.add(commandArgument.getName());
        }
        return result;
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        if (arguments.length < 1) {
            printUsage(commandSender, label, this.commandArguments);
            return true;
        }
        final CommandArgument commandArgument = matchArgument(arguments[0], commandSender, this.commandArguments);
        if (commandArgument == null) {
            printUsage(commandSender, label, this.commandArguments);
            return true;
        }
        return commandArgument.onCommand(commandSender, command, label, arguments);
    }
    
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        List<String> result = new ArrayList<String>();
        if (!(commandSender instanceof Player)) {
            return Collections.emptyList();
        }
        if (arguments.length == 1) {
            result = getAccessibleArgumentNames(commandSender, this.commandArguments);
        }
        else {
            final CommandArgument commandArgument = matchArgument(arguments[0], commandSender, this.commandArguments);
            if (commandArgument == null) {
                return Collections.emptyList();
            }
            result = commandArgument.onTabComplete(commandSender, command, label, arguments);
            if (result == null) {
                return null;
            }
        }
        return CollectionUtil.getCompletions(arguments, result);
    }
    
    public static class ArgumentComparator implements Comparator<CommandArgument>, Serializable
    {
        private static final long serialVersionUID = -2000275320912413407L;
        
        @Override
        public int compare(final CommandArgument primaryArgument, final CommandArgument secondaryArgument) {
            return secondaryArgument.getName().compareTo(primaryArgument.getName());
        }
    }
}
