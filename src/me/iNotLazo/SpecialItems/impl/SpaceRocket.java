package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class SpaceRocket
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public SpaceRocket(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&c&lSpace Rocket");
        this.lore = ChatUtil.formatMessage("&7be driven by the force of a rocket!");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.FIREWORK).setAmount(quantity).setName(ChatUtil.formatMessage("&c&lSpace Rocket")).setLore("&7be driven by the force of a rocket!").build();
    }
    
    public ItemStack getItemStack() {
        return this.isSpaceRocket() ? new ItemMaker(Material.FIREWORK).setAmount(1).setName(this.name).setLore(this.lore).build() : new ItemMaker(Material.FIREWORK).setAmount(1).build();
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
    
    public boolean isSpaceRocket() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
