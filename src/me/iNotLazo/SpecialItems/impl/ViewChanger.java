package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class ViewChanger
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public ViewChanger(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&5&lView Changer");
        this.lore = ChatUtil.formatMessage("&7Hit with this to change the view position of the enemy");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.BLAZE_ROD).setAmount(quantity).setName(ChatUtil.formatMessage("&5&lView Changer")).setLore("&7Hit with this to change the view position of the enemy").build();
    }
    
    public ItemStack getItemStack() {
        return this.isViewChanger() ? new ItemMaker(Material.BLAZE_ROD).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.BLAZE_ROD).setAmount(1).build();
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
    
    public boolean isViewChanger() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
