package me.iNotLazo.SpecialItems.utils;

import org.bukkit.enchantments.*;
import org.bukkit.*;
import com.google.common.base.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import java.util.*;

public class InventoryUtils
{
    public static String InventoryToString(final Inventory invInventory) {
        final StringBuilder serialization = new StringBuilder(String.valueOf(invInventory.getSize()) + ";");
        for (int i = 0; i < invInventory.getSize(); ++i) {
            final ItemStack is = invInventory.getItem(i);
            if (is != null) {
                final StringBuilder serializedItemStack = new StringBuilder();
                final String isType = String.valueOf(is.getType().toString());
                serializedItemStack.append("t@").append(isType);
                if (is.getDurability() != 0) {
                    final String isDurability = String.valueOf(is.getDurability());
                    serializedItemStack.append(":d@").append(isDurability);
                }
                if (is.getAmount() != 1) {
                    final String isAmount = String.valueOf(is.getAmount());
                    serializedItemStack.append(":a@").append(isAmount);
                }
                final Map<Enchantment, Integer> isEnch = (Map<Enchantment, Integer>)is.getEnchantments();
                if (isEnch.size() > 0) {
                    for (final Map.Entry<Enchantment, Integer> ench : isEnch.entrySet()) {
                        serializedItemStack.append(":e@").append(ench.getKey().getName()).append("@").append(ench.getValue());
                    }
                }
                serialization.append(i).append("#").append((CharSequence)serializedItemStack).append(";");
            }
        }
        return serialization.toString();
    }
    
    public static Inventory StringToInventory(final String invString) {
        final String[] serializedBlocks = invString.split(";");
        final String invInfo = serializedBlocks[0];
        final Inventory deserializedInventory = Bukkit.getServer().createInventory((InventoryHolder)null, (int)Integer.valueOf(invInfo));
        for (int i = 1; i < serializedBlocks.length; ++i) {
            final String[] serializedBlock = serializedBlocks[i].split("#");
            final int stackPosition = Integer.valueOf(serializedBlock[0]);
            if (stackPosition < deserializedInventory.getSize()) {
                ItemStack is = null;
                Boolean createdItemStack = false;
                final String[] serializedItemStack = serializedBlock[1].split(":");
                String[] array;
                for (int length = (array = serializedItemStack).length, j = 0; j < length; ++j) {
                    final String itemInfo = array[j];
                    final String[] itemAttribute = itemInfo.split("@");
                    if (itemAttribute[0].equals("t")) {
                        is = new ItemStack(Material.getMaterial(itemAttribute[1]));
                        createdItemStack = true;
                    }
                    else if (itemAttribute[0].equals("d") && createdItemStack) {
                        is.setDurability((short)Short.valueOf(itemAttribute[1]));
                    }
                    else if (itemAttribute[0].equals("a") && createdItemStack) {
                        is.setAmount((int)Integer.valueOf(itemAttribute[1]));
                    }
                    else if (itemAttribute[0].equals("e") && createdItemStack) {
                        is.addEnchantment(Enchantment.getByName(itemAttribute[1]), (int)Integer.valueOf(itemAttribute[2]));
                    }
                }
                deserializedInventory.setItem(stackPosition, is);
            }
        }
        return deserializedInventory;
    }
    
    public static ItemStack[] deepClone(final ItemStack[] origin) {
        Preconditions.checkNotNull((Object)origin, (Object)"Origin cannot be null");
        final ItemStack[] cloned = new ItemStack[origin.length];
        for (int i = 0; i < origin.length; ++i) {
            final ItemStack next = origin[i];
            cloned[i] = ((next == null) ? null : next.clone());
        }
        return cloned;
    }
    
    public static int getSafestInventorySize(final int initialSize) {
        return (initialSize + 8) / 9 * 9;
    }
    
    public static void removeItem(final Inventory inventory, final Material type, final short data, final int quantity) {
        final ItemStack[] contents = inventory.getContents();
        final boolean compareDamage = type.getMaxDurability() == 0;
        for (int i = quantity; i > 0; --i) {
            final ItemStack[] array = contents;
            final int length = array.length;
            int j = 0;
            while (j < length) {
                final ItemStack content = array[j];
                if (content != null && content.getType() == type && (!compareDamage || content.getData().getData() == data)) {
                    if (content.getAmount() <= 1) {
                        inventory.removeItem(new ItemStack[] { content });
                        break;
                    }
                    content.setAmount(content.getAmount() - 1);
                    break;
                }
                else {
                    ++j;
                }
            }
        }
    }
    
    public static int countAmount(final Inventory inventory, final Material type, final short data) {
        final ItemStack[] contents = inventory.getContents();
        final boolean compareDamage = type.getMaxDurability() == 0;
        int counter = 0;
        ItemStack[] array;
        for (int length = (array = contents).length, i = 0; i < length; ++i) {
            final ItemStack item = array[i];
            if (item != null && item.getType() == type && (!compareDamage || item.getData().getData() == data)) {
                counter += item.getAmount();
            }
        }
        return counter;
    }
    
    public static boolean isEmpty(final Inventory inventory) {
        return isEmpty(inventory, true);
    }
    
    public static boolean isEmpty(final Inventory inventory, final boolean checkArmour) {
        boolean result = true;
        final ItemStack[] contents = inventory.getContents();
        ItemStack[] array;
        for (int length = (array = contents).length, i = 0; i < length; ++i) {
            final ItemStack content = array[i];
            if (content != null && content.getType() != Material.AIR) {
                result = false;
                break;
            }
        }
        if (!result) {
            return false;
        }
        if (checkArmour && inventory instanceof PlayerInventory) {
            final ItemStack[] armorContents = ((PlayerInventory)inventory).getArmorContents();
            ItemStack[] array2;
            for (int length2 = (array2 = armorContents).length, j = 0; j < length2; ++j) {
                final ItemStack content2 = array2[j];
                if (content2 != null && content2.getType() != Material.AIR) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }
    
    public static boolean clickedTopInventory(final InventoryDragEvent event) {
        final InventoryView view = event.getView();
        final Inventory topInventory = view.getTopInventory();
        if (topInventory == null) {
            return false;
        }
        boolean result = false;
        final Set<Map.Entry<Integer, ItemStack>> entrySet = event.getNewItems().entrySet();
        final int size = topInventory.getSize();
        for (final Map.Entry<Integer, ItemStack> entry : entrySet) {
            if (entry.getKey() < size) {
                result = true;
                break;
            }
        }
        return result;
    }
}
