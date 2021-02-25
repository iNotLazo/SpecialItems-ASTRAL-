package me.iNotLazo.SpecialItems.command.wrapper;

import org.apache.commons.lang.*;
import me.iNotLazo.SpecialItems.command.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.command.*;
import java.util.*;

public abstract class CommandArgument
{
    private String name;
    private String description;
    private String permission;
    protected String[] aliases;
    
    public CommandArgument(final String name, final String description) {
        this(name, description, (String)null);
    }
    
    public CommandArgument(final String name, final String description, final String permission) {
        this(name, description, permission, ArrayUtils.EMPTY_STRING_ARRAY);
    }
    
    public CommandArgument(final String name, final String description, final String[] aliases) {
        this(name, description, null, aliases);
    }
    
    public CommandArgument(final String name, final String description, final String permission, final String[] aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = Arrays.copyOf(aliases, aliases.length);
    }
    
    public String getUsage(final String label) {
        return null;
    }
    
    public void asyncSendingUsage(final CommandSender commandSender, final String usage) {
        TaskUtil.run(CommandHandler.INSTANCE.getPlugin(), () -> ChatUtil.sendMessage(commandSender, String.valueOf(Color.translate("&cUsage: ")) + usage));
    }
    
    public abstract boolean onCommand(final CommandSender p0, final Command p1, final String p2, final String[] p3);
    
    public List<String> onTabComplete(final CommandSender commandSender, final Command command, final String label, final String[] arguments) {
        return Collections.emptyList();
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
}
