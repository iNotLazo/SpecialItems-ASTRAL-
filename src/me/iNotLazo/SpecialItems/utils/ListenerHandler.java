package me.iNotLazo.SpecialItems.utils;

import org.bukkit.plugin.*;
import org.bukkit.event.*;
import java.util.*;

public enum ListenerHandler
{
    INSTANCE("INSTANCE", 0);
    
    private Plugin plugin;
    
    private ListenerHandler(final String s, final int n) {
    }
    
    public void initialize(final Plugin plugin) {
        this.plugin = plugin;
    }
    
    public void registerListenerFromPackage(final String packageName) {
        for (final Class<?> clazz : ClassUtil.getClassesInPackage(this.plugin, packageName)) {
            if (this.isListener(clazz)) {
                try {
                    this.registerListener((Listener)clazz.newInstance());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void registerListener(final Listener listener) {
        this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
    }
    
    public boolean isListener(final Class<?> clazz) {
        Class<?>[] interfaces;
        for (int length = (interfaces = clazz.getInterfaces()).length, i = 0; i < length; ++i) {
            final Class<?> interfaze = interfaces[i];
            if (interfaze == Listener.class) {
                return true;
            }
        }
        return false;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
}
