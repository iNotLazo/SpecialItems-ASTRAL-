package me.iNotLazo.SpecialItems;

import org.bukkit.plugin.*;
import java.util.*;

public class PowerItemManager
{
    private Plugin plugin;
    private Set<IPowerItem> powerItemsSet;
    private IPowerItem powerItem;
    
    public PowerItemManager(final Plugin plugin) {
        this.powerItemsSet = new HashSet<IPowerItem>();
        this.plugin = plugin;
        for (final IPowerItem powerItem : this.powerItemsSet) {
            powerItem.register(this.plugin);
        }
    }
    
    public void addPowerItems(final IPowerItem powerItem) {
        if (!this.powerItemsSet.contains(powerItem)) {
            this.powerItemsSet.add(powerItem);
        }
    }
    
    public Set<IPowerItem> getPowerItemsSet() {
        return this.powerItemsSet;
    }
    
    public IPowerItem getPowerItem() {
        return this.powerItem;
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
}
