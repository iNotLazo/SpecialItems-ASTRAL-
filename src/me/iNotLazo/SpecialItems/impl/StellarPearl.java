package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class StellarPearl
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public StellarPearl(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&6&lStellar Pearl");
        this.lore = ChatUtil.formatMessage("&7Obtain shorter time when you use the pearl.");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.ENDER_PEARL).setAmount(quantity).setName(ChatUtil.formatMessage("&6&lStellar Pearl")).setLore(ChatUtil.formatMessage("&7Obtain shorter time when you use the pearl.")).build();
    }
    
    public ItemStack getItemStack() {
        return this.isStellarPearl() ? new ItemMaker(Material.ENDER_PEARL).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.ENDER_PEARL).setAmount(1).build();
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getLore() {
        return this.lore;
    }
    
    public void setLore(final String lore) {
        this.lore = lore;
    }
    
    public boolean isStellarPearl() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
