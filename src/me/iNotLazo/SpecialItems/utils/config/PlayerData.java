package me.iNotLazo.SpecialItems.utils.config;

import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.plugin.*;

public class PlayerData
{
    static PlayerData instance;
    Plugin p;
    FileConfiguration Data;
    File Datafile;
    
    static {
        PlayerData.instance = new PlayerData();
    }
    
    public static PlayerData getInstance() {
        return PlayerData.instance;
    }
    
    public void setup(final Plugin p) {
        this.p = p;
        this.Datafile = new File(p.getDataFolder(), "playerdata.yml");
        this.Data = (FileConfiguration)YamlConfiguration.loadConfiguration(this.Datafile);
        if (!this.Datafile.exists()) {
            try {
                this.Datafile.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
    
    public FileConfiguration getConfig() {
        return this.Data;
    }
    
    public void saveConfig() {
        try {
            this.Data.save(this.Datafile);
        }
        catch (IOException ex) {}
    }
    
    public PluginDescriptionFile getDescription() {
        return this.p.getDescription();
    }
    
    public void reloadConfig() {
        this.Data = (FileConfiguration)YamlConfiguration.loadConfiguration(this.Datafile);
    }
}
