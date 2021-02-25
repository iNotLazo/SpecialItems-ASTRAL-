package me.iNotLazo.SpecialItems.utils;

import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import org.apache.commons.lang.*;
import org.bukkit.inventory.meta.*;

public class ItemNames
{
    private static final Map<String, String> map = null;
      
    public static String lookup(final ItemStack stack) {
        if (stack.hasItemMeta()) {
            final ItemMeta meta = stack.getItemMeta();
            if (meta.getDisplayName() != null) {
                return meta.getDisplayName();
            }
            if (meta instanceof BookMeta) {
                return ((BookMeta)meta).getTitle();
            }
        }
        final String key = Integer.toString(stack.getTypeId());
        final Material mat = stack.getType();
        String result;
        if ((mat == Material.WOOL || mat == Material.CARPET) && stack.getDurability() == 0) {
            result = ItemNames.map.get(key);
        }
        else if (mat == Material.WOOL || mat == Material.CARPET || mat == Material.STAINED_CLAY || mat == Material.STAINED_GLASS || mat == Material.STAINED_GLASS_PANE) {
            final DyeColor dc = DyeColor.getByWoolData((byte)stack.getDurability());
            result = ((dc == null) ? ItemNames.map.get(key) : (String.valueOf(WordUtils.capitalizeFully(dc.toString().replace("_", " "))) + " " + ItemNames.map.get(key)));
        }
        else if (mat == Material.LEATHER_HELMET || mat == Material.LEATHER_CHESTPLATE || mat == Material.LEATHER_LEGGINGS || mat == Material.LEATHER_BOOTS) {
            final LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta)stack.getItemMeta();
            final DyeColor dc2 = DyeColor.getByColor(leatherArmorMeta.getColor());
            result = ((dc2 == null) ? ItemNames.map.get(key) : (String.valueOf(WordUtils.capitalizeFully(dc2.toString()).replace("_", " ")) + " " + ItemNames.map.get(key)));
        }
        else if (stack.getDurability() != 0) {
            result = ItemNames.map.get(String.valueOf(key) + ":" + stack.getDurability());
            if (result == null) {
                result = ItemNames.map.get(key);
            }
        }
        else {
            result = (ItemNames.map.containsKey(key) ? ItemNames.map.get(key) : stack.getType().toString());
        }
        return result;
    }
    
    public static String lookupWithAmount(final ItemStack stack) {
        final String s = lookup(stack);
        return String.valueOf(stack.getAmount()) + " x " + s;
    }
}
