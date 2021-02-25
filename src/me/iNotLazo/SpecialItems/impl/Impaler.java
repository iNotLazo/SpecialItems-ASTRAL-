package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class Impaler
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    private String secondLore;
    
    public Impaler(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&b&liNotLazo's Impaler");
        this.lore = ChatUtil.formatMessage("&7right click to throw a snowball that will impulse");
        this.secondLore = ChatUtil.formatMessage("&7the enemies to throw them into a trap");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.GOLD_AXE).setAmount(quantity).setName(ChatUtil.formatMessage("&b&liNotLazo's Impaler")).setLore("&7right click to throw a snowball that will impulse", "&7the enemies to throw them into a trap").build();
    }
    
    public ItemStack getItemStack() {
        return this.isImpaler() ? new ItemMaker(Material.GOLD_AXE).setAmount(1).setName(this.name).setLore(this.lore, this.secondLore).build() : new ItemMaker(Material.GOLD_AXE).setAmount(1).build();
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
    
    public boolean isImpaler() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && ((itemMeta.getLore().get(0).startsWith(this.lore) && itemMeta.getLore().get(1).startsWith(this.secondLore)) || (itemMeta.getLore().get(0).contains(this.lore) && itemMeta.getLore().get(1).contains(this.secondLore)) || (itemMeta.getLore().get(0).equals(this.lore) && itemMeta.getLore().get(1).equals(this.secondLore)));
    }
}
