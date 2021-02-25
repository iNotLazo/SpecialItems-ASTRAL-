package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class AntiTrapStar
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public AntiTrapStar(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&6&lAnti-Trap Star");
        this.lore = ChatUtil.formatMessage("&7hit an enemy with this, and he will cannot place blocks");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.NETHER_STAR).setAmount(quantity).setName(ChatUtil.formatMessage("&6&lAnti-Trap Star")).setLore("&7hit an enemy with this, and he will cannot place blocks").build();
    }
    
    public ItemStack getItemStack() {
        return this.isAntiTrapStar() ? new ItemMaker(Material.NETHER_STAR).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.NETHER_STAR).setAmount(1).build();
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
    
    public boolean isAntiTrapStar() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
