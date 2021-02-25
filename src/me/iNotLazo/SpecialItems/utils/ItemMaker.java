package me.iNotLazo.SpecialItems.utils;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.apache.commons.lang.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

public class ItemMaker implements Cloneable
{
    private Material type;
    private int data;
    private int amount;
    private String title;
    private List<String> lore;
    private org.bukkit.Color color;
    private HashMap<Enchantment, Integer> enchantments;
    private boolean unbreakable;
    
    public ItemMaker(final Material type) {
        this(type, 1);
    }
    
    public ItemMaker(final Material type, final int amount) {
        this(type, amount, 0);
    }
    
    public ItemMaker(final Material type, final int amount, final int data) {
        this.lore = new ArrayList<String>();
        this.type = type;
        this.amount = amount;
        this.data = data;
        this.enchantments = new HashMap<Enchantment, Integer>();
    }
    
    public ItemMaker(final ItemStack itemStack) {
        Validate.notNull((Object)itemStack, "ItemStack cannot be null");
        this.lore = new ArrayList<String>();
        this.enchantments = new HashMap<Enchantment, Integer>();
        this.type = itemStack.getType();
        this.data = itemStack.getDurability();
        this.amount = itemStack.getAmount();
        if (itemStack.hasItemMeta()) {
            if (itemStack.getItemMeta().hasDisplayName()) {
                this.title = itemStack.getItemMeta().getDisplayName();
            }
            if (itemStack.getItemMeta().hasLore()) {
                this.lore = (List<String>)itemStack.getItemMeta().getLore();
            }
        }
        if (itemStack.getEnchantments() != null) {
            this.enchantments.putAll(itemStack.getEnchantments());
        }
        if (itemStack.getType().toString().toLowerCase().contains("leather") && itemStack.getItemMeta() instanceof LeatherArmorMeta) {
            final LeatherArmorMeta lam = (LeatherArmorMeta)itemStack.getItemMeta();
            this.color = lam.getColor();
        }
    }
    
    public ItemMaker(final ItemMaker itemMaker) {
        this(itemMaker.build());
    }
    
    public ItemMaker setUnbreakable(final boolean flag) {
        this.unbreakable = flag;
        return this;
    }
    
    public ItemMaker addLore(final String... lores) {
        for (final String lore : lores) {
            this.lore.add(ChatUtil.formatMessage(lore));
        }
        return this;
    }
    
    public ItemMaker setData(final int data) {
        this.data = data;
        return this;
    }
    
    public ItemMaker setAmount(final int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemMaker setName(final String title) {
        this.title = ChatUtil.formatMessage(title);
        return this;
    }
    
    public ItemMaker setLore(final String... lore) {
        this.lore = ChatUtil.formatMessage(Arrays.asList(lore));
        return this;
    }
    
    public ItemMaker setSkullType(final SkullType skullType) {
        Validate.notNull((Object)skullType, "SkullType cannot be null");
        this.setData(skullType.data);
        return this;
    }
    
    public List<String> getLore() {
        return this.lore;
    }
    
    public ItemMaker setLore(final List<String> list) {
        this.lore = ChatUtil.formatMessage(list);
        return this;
    }
    
    public Material getType() {
        return this.type;
    }
    
    public ItemMaker setType(final Material type) {
        this.type = type;
        return this;
    }
    
    public ItemMaker addEnchantment(final Enchantment enchantment, final int level) {
        this.enchantments.put(enchantment, level);
        return this;
    }
    
    public ItemMaker setColor(final org.bukkit.Color color) {
        if (!this.type.toString().toLowerCase().contains("leather")) {
            throw new RuntimeException("Cannot set color of non-leather items.");
        }
        this.color = color;
        return this;
    }
    
    public ItemStack build() {
        Validate.noNullElements(new Object[] { this.type, this.data, this.amount });
        final ItemStack itemStack = new ItemStack(this.type, this.amount, (short)this.data);
        final ItemMeta itemMeta = itemStack.getItemMeta();
        if (this.title != null && this.title != "") {
            itemMeta.setDisplayName(this.title);
        }
        if (this.lore != null && !this.lore.isEmpty()) {
            itemMeta.setLore((List)this.lore);
        }
        if (this.color != null && this.type.toString().toLowerCase().contains("leather")) {
            ((LeatherArmorMeta)itemMeta).setColor(this.color);
        }
        itemStack.setItemMeta(itemMeta);
        if (this.enchantments != null && !this.enchantments.isEmpty()) {
            itemStack.addUnsafeEnchantments((Map)this.enchantments);
        }
        if (this.unbreakable) {
            final ItemMeta meta = itemStack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }
    
    public ItemMaker clone() {
        return new ItemMaker(this);
    }
    
    public enum SkullType
    {
        SKELETON("SKELETON", 0, "SKELETON", 0, 0), 
        WITHER_SKELETON("WITHER_SKELETON", 1, "WITHER_SKELETON", 1, 1), 
        ZOMBIE("ZOMBIE", 2, "ZOMBIE", 2, 2), 
        PLAYER("PLAYER", 3, "PLAYER", 3, 3), 
        CREEPER("CREEPER", 4, "CREEPER", 4, 4);
        
        private int data;
        
        private SkullType(final String s2, final int n2, final String s, final int n, final int data) {
            this.data = data;
        }
        
        public int getData() {
            return this.data;
        }
    }
}
