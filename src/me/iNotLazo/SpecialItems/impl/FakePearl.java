package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class FakePearl
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public FakePearl(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&4&lHerpionG's Fake Pearl");
        this.lore = ChatUtil.formatMessage("&7This pearl will not teleport you, use it to make escape plan");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.ENDER_PEARL).setAmount(quantity).setName(ChatUtil.formatMessage("&4&lHerpionG's Fake Pearl")).setLore("&7This pearl will not teleport you, use it to make escape plan").build();
    }
    
    public ItemStack getItemStack() {
        return this.isFakePearl() ? new ItemMaker(Material.ENDER_PEARL).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.ENDER_PEARL).setAmount(1).build();
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
    
    public boolean isFakePearl() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
