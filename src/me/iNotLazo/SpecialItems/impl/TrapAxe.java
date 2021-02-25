package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class TrapAxe
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public TrapAxe(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&b&lTrap Axe");
        this.lore = ChatUtil.formatMessage("&7break 1x3 blocks radius, excellent for make traps!");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.GOLD_AXE).setAmount(quantity).setName(ChatUtil.formatMessage("&b&lTrap Axe")).setLore("&7break 1x3 blocks radius, excellent for make traps!").build();
    }
    
    public ItemStack getItemStack() {
        return this.isTrapAxe() ? new ItemMaker(Material.GOLD_AXE).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.GOLD_AXE).setAmount(1).build();
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
    
    public boolean isTrapAxe() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
