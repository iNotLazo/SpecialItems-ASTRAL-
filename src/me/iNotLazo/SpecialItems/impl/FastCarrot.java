package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class FastCarrot
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public FastCarrot(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&e&lFast Carrot");
        this.lore = ChatUtil.formatMessage("&7Right click to get speed 4 for 4 seconds");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.GOLDEN_CARROT).setAmount(quantity).setName(ChatUtil.formatMessage("&e&lFast Carrot")).setLore("&7Right click to get speed 4 for 4 seconds").build();
    }
    
    public ItemStack getItemStack() {
        return this.isFastCarrot() ? new ItemMaker(Material.GOLDEN_CARROT).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.GOLDEN_CARROT).setAmount(1).build();
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
    
    public boolean isFastCarrot() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
