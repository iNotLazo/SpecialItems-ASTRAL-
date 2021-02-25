package me.iNotLazo.SpecialItems.utils;

import org.bukkit.inventory.*;
import com.google.gson.reflect.*;
import java.io.*;
import org.bukkit.*;

public class InventorySerialisation
{
    public static String itemStackArrayToJson(final ItemStack[] items) throws IllegalStateException {
        return GsonFactory.getCompactGson().toJson((Object)items);
    }
    
    public static ItemStack[] itemStackArrayFromJson(final String data) throws IOException {
        return (ItemStack[])GsonFactory.getCompactGson().fromJson(data, new TypeToken<ItemStack[]>() {}.getType());
    }
    
    public static boolean isHelmet(final ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_HELMET || itemStack.getType() == Material.LEATHER_HELMET || itemStack.getType() == Material.DIAMOND_HELMET || itemStack.getType() == Material.CHAINMAIL_HELMET || itemStack.getType() == Material.GOLD_HELMET;
    }
    
    public static boolean isChestplate(final ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_CHESTPLATE || itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.DIAMOND_CHESTPLATE || itemStack.getType() == Material.CHAINMAIL_CHESTPLATE || itemStack.getType() == Material.GOLD_CHESTPLATE;
    }
    
    public static boolean isLeggings(final ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_LEGGINGS || itemStack.getType() == Material.LEATHER_LEGGINGS || itemStack.getType() == Material.DIAMOND_LEGGINGS || itemStack.getType() == Material.CHAINMAIL_LEGGINGS || itemStack.getType() == Material.GOLD_LEGGINGS;
    }
    
    public static boolean isBoots(final ItemStack itemStack) {
        return itemStack.getType() == Material.IRON_BOOTS || itemStack.getType() == Material.LEATHER_BOOTS || itemStack.getType() == Material.DIAMOND_BOOTS || itemStack.getType() == Material.CHAINMAIL_BOOTS || itemStack.getType() == Material.GOLD_BOOTS;
    }
}
