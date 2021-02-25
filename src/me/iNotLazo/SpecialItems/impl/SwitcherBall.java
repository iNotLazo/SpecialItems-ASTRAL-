package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class SwitcherBall
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    private String secondLore;
    
    public SwitcherBall(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&a&lSwitcher Ball");
        this.lore = ChatUtil.formatMessage("&7Hit a player with this ball to switch");
        this.secondLore = ChatUtil.formatMessage("&7positions with them. Must be within a 7 block radius.");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.SNOW_BALL).setAmount(quantity).setName(ChatUtil.formatMessage("&a&lSwitcher Ball")).setLore("&7Hit a player with this ball to switch", "&7positions with them. Must be within a 7 block radius.").build();
    }
    
    public ItemStack getItemStack() {
        return this.isSwitcherBall() ? new ItemMaker(Material.SNOW_BALL).setAmount(1).setName(this.name).setLore(this.lore, this.secondLore).build() : new ItemMaker(Material.SNOW_BALL).setAmount(1).build();
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
    
    public boolean isSwitcherBall() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && ((itemMeta.getLore().get(0).startsWith(this.lore) && itemMeta.getLore().get(1).startsWith(this.secondLore)) || (itemMeta.getLore().get(0).contains(this.lore) && itemMeta.getLore().get(1).contains(this.secondLore)) || (itemMeta.getLore().get(0).equals(this.lore) && itemMeta.getLore().get(1).equals(this.secondLore)));
    }
}
