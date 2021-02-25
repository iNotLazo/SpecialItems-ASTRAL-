package me.iNotLazo.SpecialItems.utils;

import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;

public class TaskUtil
{
    public static void run(final Plugin plugin, final Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }
    
    public static void runTimer(final Plugin plugin, final Runnable runnable, final long delay, final long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
    }
    
    public static void runTimer(final Plugin plugin, final BukkitRunnable runnable, final long delay, final long timer) {
        runnable.runTaskTimer(plugin, delay, timer);
    }
    
    public static void runLater(final Plugin plugin, final Runnable runnable, final long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }
    
    public static void runAsync(final Plugin plugin, final Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }
}
