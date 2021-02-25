package me.iNotLazo.SpecialItems.utils;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import java.io.*;

public class Config extends YamlConfiguration
{
    private String fileName;
    private JavaPlugin plugin;
    
    public Config(final JavaPlugin plugin, final String fileName) {
        this(plugin, fileName, ".yml");
    }
    
    public Config(final JavaPlugin plugin, final String fileName, final String fileExtension) {
        this.plugin = plugin;
        this.fileName = String.valueOf(fileName) + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    private void createFile() {
        final File folder = this.plugin.getDataFolder();
        try {
            final File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                }
                else {
                    this.save(file);
                }
                this.load(file);
            }
            else {
                this.load(file);
                this.save(file);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        final File folder = this.plugin.getDataFolder();
        try {
            this.save(new File(folder, this.fileName));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
