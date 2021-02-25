package me.iNotLazo.SpecialItems.command.wrapper;

import org.apache.commons.lang.*;
import org.bukkit.command.*;
import org.bukkit.*;
import net.md_5.bungee.api.chat.*;
import me.iNotLazo.SpecialItems.command.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.utils.Color;

import com.google.common.collect.*;
import java.util.*;

public abstract class ArgumentExecutor implements CommandExecutor, TabCompleter
{
    protected List<CommandArgument> commandArguments;
    protected String label;
    protected String description;
    protected String permission;
    protected String[] aliases;
    
    public ArgumentExecutor(final String name) {
        this(name, null);
    }
    
    public ArgumentExecutor(final String name, final String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }
    
    public ArgumentExecutor(final String label, final String description, final String[] aliases) {
        this.commandArguments = new ArrayList<CommandArgument>();
        this.label = label;
        this.description = description;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        if (this.permission != null && !commandSender.hasPermission(this.permission)) {
            ChatUtil.sendMessage(commandSender, Color.translate("&cNo Permission."));
            return false;
        }
        if (arguments.length < 1) {
            ChatUtil.sendMessage(commandSender, ChatColor.GRAY + ChatUtil.CHAT_BAR, "&6&l" + ChatUtil.capitalizeFullyWords(label) + ' ' + "&6Help" + ' ' + "&f(Page 1 out of 1)", " ");
            for (final CommandArgument commandArgument : this.commandArguments) {
                ChatUtil.sendMessage(commandSender, new ComponentBuilder(ChatUtil.formatMessage("&e" + commandArgument.getUsage(command.getName()) + "&7, " + "&r" + commandArgument.getDescription())).event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, commandArgument.getUsage(label))).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatUtil.formatMessage("&eClick to suggest " + commandArgument.getUsage(label))))).create());
            }
            ChatUtil.sendMessage(commandSender, ChatColor.GRAY + ChatUtil.CHAT_BAR);
            return false;
        }
        CommandArgument commandArgument = this.getCommandArgument(arguments[0]);
        final String permission = (commandArgument == null) ? null : commandArgument.getPermission();
        if (commandArgument == null) {
            ChatUtil.sendMessage(commandSender, Color.translate("&c{LABEL} subcommand '{NAME}' not found.").replace("{LABEL}", ChatUtil.capitalizeFullyWords(this.label)).replace("{NAME}", arguments[0]));
            return false;
        }
        if (permission != null && !commandSender.hasPermission(permission)) {
            ChatUtil.sendMessage(commandSender, Color.translate("&cNo Permission."));
            return false;
        }
        commandArgument.onCommand(commandSender, command, label, arguments);
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        List<String> result = new ArrayList<String>();
        if (arguments.length < 2) {
            for (final CommandArgument commandArgument : this.commandArguments) {
                final String permission = commandArgument.getPermission();
                if (permission != null && !commandSender.hasPermission(permission)) {
                    continue;
                }
                result.add(commandArgument.getName());
            }
        }
        else {
            final CommandArgument commandArgument = this.getCommandArgument(arguments[0]);
            if (commandArgument == null) {
                return result;
            }
            final String permission2 = commandArgument.getPermission();
            if (permission2 == null || commandSender.hasPermission(permission2)) {
                result = commandArgument.onTabComplete(commandSender, command, label, arguments);
                if (result == null) {
                    return null;
                }
            }
        }
        return CollectionUtil.getCompletions(arguments, result);
    }
    
    public void asyncSendingUsage(final CommandSender commandSender, final String usage) {
        TaskUtil.run(CommandHandler.INSTANCE.getPlugin(), () -> ChatUtil.sendMessage(commandSender, String.valueOf(Color.translate("&cUsage: ")) + usage));
    }
    
    public boolean containsCommandArgument(final CommandArgument commandArgument) {
        return this.commandArguments.contains(commandArgument);
    }
    
    public void addCommandArgument(final CommandArgument commandArgument) {
        this.commandArguments.add(commandArgument);
    }
    
    public void removeCommandArgument(final CommandArgument commandArgument) {
        this.commandArguments.remove(commandArgument);
    }
    
    public CommandArgument getCommandArgument(final String name) {
        return this.commandArguments.stream().filter(commandArgument -> commandArgument.getName().equalsIgnoreCase(name) || Arrays.asList(commandArgument.getAliases()).contains(name.toLowerCase())).findFirst().orElse(null);
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public void setPermission(final String permission) {
        this.permission = permission;
    }
    
    public String[] getAliases() {
        if (this.aliases == null) {
            this.aliases = ArrayUtils.EMPTY_STRING_ARRAY;
        }
        return Arrays.copyOf(this.aliases, this.aliases.length);
    }
    
    public void setAliases(final String[] aliases) {
        this.aliases = aliases;
    }
    
    public List<CommandArgument> getCommandArguments() {
        return (List<CommandArgument>)ImmutableList.copyOf((Collection)this.commandArguments);
    }
}
