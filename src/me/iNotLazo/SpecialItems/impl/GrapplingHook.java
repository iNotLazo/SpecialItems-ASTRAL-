package me.iNotLazo.SpecialItems.impl;

import org.bukkit.inventory.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.meta.*;

public class GrapplingHook
{
    private ItemStack itemStack;
    private String name;
    private String lore;
    
    public GrapplingHook(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.name = ChatUtil.formatMessage("&6&lGrappling Hook");
        this.lore = ChatUtil.formatMessage("&7Hook and pull to the block that stuck the rod");
    }
    
    public static ItemStack getItem(final int quantity) {
        return new ItemMaker(Material.FISHING_ROD).setAmount(quantity).setData(54).setName(ChatUtil.formatMessage("&6&lGrappling Hook")).setLore(ChatUtil.formatMessage("&7Hook and pull to the block that stuck the rod")).build();
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
    
    public boolean isGrapplingHook() {
        if (this.itemStack == null || !this.itemStack.hasItemMeta()) {
            return false;
        }
        final ItemMeta itemMeta = this.itemStack.getItemMeta();
        return itemMeta.hasDisplayName() && itemMeta.hasLore() && itemMeta.getDisplayName().equals(this.name) && (itemMeta.getLore().get(0).startsWith(this.lore) || itemMeta.getLore().get(0).contains(this.lore) || itemMeta.getLore().get(0).equals(this.lore));
    }
}
