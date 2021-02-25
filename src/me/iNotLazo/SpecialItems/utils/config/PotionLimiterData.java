package me.iNotLazo.SpecialItems.utils.config;

import org.bukkit.configuration.file.*;
import java.io.*;
import org.bukkit.plugin.*;

public class PotionLimiterData
{
    static PotionLimiterData instance;
    Plugin p;
    FileConfiguration Data;
    File Datafile;
    
    static {
        PotionLimiterData.instance = new PotionLimiterData();
    }
    
    public static PotionLimiterData getInstance() {
        return PotionLimiterData.instance;
    }
    
    public void setup(final Plugin p) {
        this.p = p;
        this.Datafile = new File(p.getDataFolder(), "potion-limiter.yml");
        this.Data = (FileConfiguration)YamlConfiguration.loadConfiguration(this.Datafile);
        if (!this.Datafile.exists()) {
            try {
                this.Datafile.createNewFile();
                final PrintWriter writer = new PrintWriter(new FileWriter(this.Datafile));
                writer.println("#See http://minecraft.gamepedia.com/Potion\n#And http://minecraft.gamepedia.com/Splash_Potion\ndisabled-potions:\n  - 8195\n  - 8201\n  - 8265\n  - 16393\n  - 16457\n  - 16387\n  - 16451");
                writer.flush();
                writer.close();
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
