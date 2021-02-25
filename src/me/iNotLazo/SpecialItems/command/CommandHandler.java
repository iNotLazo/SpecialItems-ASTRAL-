package me.iNotLazo.SpecialItems.command;

import java.util.*;
import org.bukkit.command.*;
import me.iNotLazo.SpecialItems.command.wrapper.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import java.lang.reflect.*;

public enum CommandHandler
{
    INSTANCE("INSTANCE", 0);
    
    private Plugin plugin;
    private PluginCommand pluginCommand;
    private CommandMap commandMap;
    
    private CommandHandler(final String s, final int n) {
    }
    
    public void initialize(final Plugin plugin) {
        this.plugin = plugin;
        this.commandMap = this.getCommandMap();
    }
    
    public void registerArgumentExecutor(final ArgumentExecutor argumentExecutor) {
        this.pluginCommand = this.getCommand(argumentExecutor.getLabel());
        if (argumentExecutor.getAliases() != null) {
            this.pluginCommand.setAliases((List)Arrays.asList(argumentExecutor.getAliases()));
        }
        if (argumentExecutor.getDescription() != null) {
            this.pluginCommand.setDescription(argumentExecutor.getDescription());
        }
        this.pluginCommand.setExecutor((CommandExecutor)argumentExecutor);
        this.pluginCommand.setTabCompleter((TabCompleter)argumentExecutor);
        this.commandMap.register(this.plugin.getDescription().getName(), (Command)this.pluginCommand);
    }
    
    public void registerDefaultCommandExecutor(final DefaultCommandExecutor defaultCommandExecutor) {
        this.pluginCommand = this.getCommand(defaultCommandExecutor.getName());
        if (defaultCommandExecutor.getAliases() != null) {
            this.pluginCommand.setAliases((List)Arrays.asList(defaultCommandExecutor.getAliases()));
        }
        if (defaultCommandExecutor.getDescription() != null) {
            this.pluginCommand.setDescription(defaultCommandExecutor.getDescription());
        }
        this.pluginCommand.setExecutor((CommandExecutor)defaultCommandExecutor);
        this.pluginCommand.setTabCompleter((TabCompleter)defaultCommandExecutor);
        this.commandMap.register(this.plugin.getDescription().getName(), (Command)this.pluginCommand);
    }
    
    private PluginCommand getCommand(final String name) {
        PluginCommand command = null;
        try {
            final Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);
            command = constructor.newInstance(name, this.plugin);
        }
        catch (SecurityException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | IllegalArgumentException ex2) {
            final Exception e = ex2;
            e.printStackTrace();
        }
        return command;
    }
    
    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                final Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                commandMap = (CommandMap)field.get(Bukkit.getPluginManager());
            }
        }
        catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException ex2) {
            final Exception e = ex2;
            e.printStackTrace();
        }
        return commandMap;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
}
