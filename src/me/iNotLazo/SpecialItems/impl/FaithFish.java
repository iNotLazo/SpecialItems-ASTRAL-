package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class FaithFish
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public FaithFish(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&6&lEdu's Faith Fish");
        this.lore = ChatUtil.formatMessage("&7Right click to obtain regeneration III during 5 seconds");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.RAW_FISH).setAmount(quantity).setName(ChatUtil.formatMessage("&6&lEdu's Faith Fish")).setLore("&7Right click to obtain regeneration III during 5 seconds").build();
    }
    
    public ItemStack getItemStack() {
        return this.isFaithFish() ? new ItemMaker(Material.RAW_FISH).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.RAW_FISH).setAmount(1).build();
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
    
    public boolean isFaithFish() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
