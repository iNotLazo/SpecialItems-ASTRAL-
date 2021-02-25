package me.iNotLazo.SpecialItems.utils.config;

import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import java.util.*;

public class ConfigFile
{
    private File file;
    private YamlConfiguration configuration;
    
    public ConfigFile(final JavaPlugin plugin, final String name) {
        this.file = new File(plugin.getDataFolder(), String.valueOf(name) + ".yml");
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdir();
        }
        plugin.saveResource(String.valueOf(name) + ".yml", true);
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public double getDouble(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getDouble(path);
        }
        return 0.0;
    }
    
    public int getInt(final String path) {
        if (this.configuration.contains(path)) {
            return this.configuration.getInt(path);
        }
        return 0;
    }
    
    public boolean getBoolean(final String path) {
        return this.configuration.contains(path) && this.configuration.getBoolean(path);
    }
    
    public String getString(final String path) {
        if (this.configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', this.configuration.getString(path));
        }
        return "ERROR: STRING NOT FOUND";
    }
    
    public List<String> getReversedStringList(final String path) {
        final List<String> list = this.getStringList(path);
        if (list != null) {
            final int size = list.size();
            final List<String> toReturn = new ArrayList<String>();
            for (int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }
            return toReturn;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
    
    public List<String> getStringList(final String path) {
        if (this.configuration.contains(path)) {
            final ArrayList<String> strings = new ArrayList<String>();
            for (final String string : this.configuration.getStringList(path)) {
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return strings;
        }
        return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
    }
}
