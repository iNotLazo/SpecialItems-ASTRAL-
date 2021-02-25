package me.iNotLazo.SpecialItems.timer;

import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;

public abstract class Timer
{
    protected final String name;
    protected final long defaultCooldown;
    
    public Timer(final String name, final long defaultCooldown) {
        this.name = name;
        this.defaultCooldown = defaultCooldown;
    }
    
    public abstract ChatColor getScoreboardPrefix();
    
    public String getDisplayName() {
        return String.valueOf(this.getScoreboardPrefix().toString()) + this.name;
    }
    
    public String getDisplayName(final boolean bold) {
        return String.valueOf(this.getScoreboardPrefix().toString()) + (bold ? ChatColor.BOLD.toString() : null) + this.name;
    }
    
    public void load(final Config config) {
    }
    
    public void onDisable(final Config config) {
    }
}
