package me.iNotLazo.SpecialItems;

import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public interface IPowerItem
{
    void register(final Plugin p0);
    
    String getName(final Player p0);
    
    ItemStack getItem(final Player p0);
}
