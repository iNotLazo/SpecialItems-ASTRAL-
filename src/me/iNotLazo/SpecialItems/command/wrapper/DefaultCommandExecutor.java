package me.iNotLazo.SpecialItems.command.wrapper;

import java.util.regex.*;
import org.apache.commons.lang3.*;
import org.bukkit.command.*;
import java.util.*;
import me.iNotLazo.SpecialItems.command.*;
import me.iNotLazo.SpecialItems.utils.*;

public abstract class DefaultCommandExecutor implements CommandExecutor, TabCompleter
{
    private Pattern usageReplacer;
    private String name;
    private String description;
    private String permission;
    private String usage;
    private String[] aliases;
    private String[] flags;
    
    public DefaultCommandExecutor(final String name) {
        this(name, null);
    }
    
    public DefaultCommandExecutor(final String name, final String description) {
        this(name, description, ArrayUtils.EMPTY_STRING_ARRAY);
    }
    
    public DefaultCommandExecutor(final String name, final String description, final String[] aliases) {
        this(name, description, null, aliases);
    }
    
    public DefaultCommandExecutor(final String name, final String description, final String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }
    
    public DefaultCommandExecutor(final String name, final String description, final String permission, final String[] aliases) {
        this.usageReplacer = Pattern.compile("(command)", 16);
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        if (this.permission != null && !commandSender.hasPermission(this.permission)) {
            ChatUtil.sendMessage(commandSender, Color.translate("&cNo Permission."));
            return false;
        }
        this.execute(commandSender, label, arguments);
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        return this.completer(commandSender, label, arguments);
    }
    
    public abstract boolean execute(final CommandSender p0, final String p1, final String[] p2);
    
    public List<String> completer(final CommandSender commandSender, final String flag, final String[] arguments) {
        return null;
    }
    
    public String getName() {
        return this.name;
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
    
    public String[] getFlags() {
        return this.flags;
    }
    
    protected void setFlags(final String[] flags) {
        this.flags = flags;
    }
    
    public String getUsage(final String label) {
        return ChatUtil.formatMessage(String.valueOf(Color.translate("&cUsage: ")) + this.usageReplacer.matcher(this.usage).replaceAll(label));
    }
    
    public String getUsage() {
        if (this.usage == null) {
            this.usage = "";
        }
        return this.usageReplacer.matcher(this.usage).replaceAll(this.name);
    }
    
    public void setUsage(final String usage) {
        this.usage = usage;
    }
    
    public void asyncSendingUsage(final CommandSender commandSender, final String usage) {
        TaskUtil.run(CommandHandler.INSTANCE.getPlugin(), () -> ChatUtil.sendMessage(commandSender, String.valueOf(Color.translate("&cUsage: ")) + usage));
    }
}
