package me.iNotLazo.SpecialItems;

import org.bukkit.plugin.java.*;
import me.iNotLazo.SpecialItems.timer.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.plugin.*;
import me.iNotLazo.SpecialItems.command.*;
import me.iNotLazo.SpecialItems.command.impl.*;
import me.iNotLazo.SpecialItems.command.wrapper.*;

public class Main extends JavaPlugin
{
    private TimerManager timerManager;
    private EffectRestorer effectRestorer;
    
    public void onEnable() {
        this.saveDefaultConfig();
        this.effectRestorer = new EffectRestorer(this);
        ListenerHandler.INSTANCE.initialize((Plugin)this);
        CommandHandler.INSTANCE.initialize((Plugin)this);
        this.timerManager = new TimerManager();
        CommandHandler.INSTANCE.registerDefaultCommandExecutor(new SpecialItemsCommand());
    }
    
    public void onDisable() {
        this.timerManager.saveTimerData();
    }
    
    public static Main getPlugin() {
        return (Main)JavaPlugin.getPlugin((Class)Main.class);
    }
    
    public EffectRestorer getEffectRestorer() {
        return this.effectRestorer;
    }
    
    public TimerManager getTimerManager() {
        return this.timerManager;
    }
}
