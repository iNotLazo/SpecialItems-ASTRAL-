package me.iNotLazo.SpecialItems.timer.impl.special;

import me.iNotLazo.SpecialItems.timer.*;
import org.bukkit.block.*;
import java.util.concurrent.*;

import me.iNotLazo.HCF.HCFactions;
import me.iNotLazo.HCF.factions.Faction;
import me.iNotLazo.HCF.factions.utils.games.CitadelFaction;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.event.player.*;
import me.iNotLazo.SpecialItems.impl.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.inventory.*;
import org.bukkit.event.block.*;
import org.bukkit.event.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.*;

public class TrapAxeTimer extends PlayerTimer implements Listener
{
    private HashMap<Player, HashMap<Block, BlockFace>> blocks;
    
    public TrapAxeTimer() {
        super("TrapAxe", TimeUnit.SECONDS.toMillis(Main.getPlugin().getConfig().getInt("TrapAxe")));
        this.blocks = new HashMap<Player, HashMap<Block, BlockFace>>();
    }
    
    public ChatColor getScoreboardPrefix() {
        return ChatColor.YELLOW;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockClick(final PlayerInteractEvent e) {
        if (e.isCancelled()) {
            return;
        }
        final Player player = e.getPlayer();
        final long remaining = this.getRemaining(player);
        final TrapAxe TrapAxe = new TrapAxe(player.getItemInHand());
        if (remaining > 0L && TrapAxe.isTrapAxe() && (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR)) {
            ChatUtil.sendMessage(player, "&cYou can't use this for another &l" + Utils.getRemaining(remaining, true));
            e.setCancelled(true);
        }
        else if (remaining <= 0L && TrapAxe.isTrapAxe() && e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Faction factionAt = HCFactions.getInstance().getFactionManager().getFactionAt(player.getLocation());
            if (factionAt instanceof CitadelFaction) {
    	        player.sendMessage(me.iNotLazo.HCF.utils.chat.Color.translate("&CYou can't use this item in a citadel event!"));
    			e.setCancelled(true);
            } else {
	            final ItemStack item = player.getItemInHand();
	            final Block block = e.getClickedBlock();
	            final HashMap<Block, BlockFace> blockFace = new HashMap<Block, BlockFace>();
	            blockFace.put(block, e.getBlockFace());
	            this.blocks.put(player, blockFace);
	            player.playSound(player.getLocation(), Sound.BLAZE_HIT, 3.0f, 2.0f);
	            this.setCooldown(player, player.getUniqueId(), this.defaultCooldown, true);
	        }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlastBreak(final BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getBlock();
        final TrapAxe TrapAxe = new TrapAxe(player.getItemInHand());
        final ItemStack item = player.getItemInHand();
        if (TrapAxe.isTrapAxe()) {
            if (this.blocks.containsKey(player) && this.blocks.get(player).containsKey(block)) {
                final BlockFace face = this.blocks.get(player).get(block);
                this.blocks.remove(player);
                final HashMap<ItemStack, Integer> drops = new HashMap<ItemStack, Integer>();
                final int xp = 0;
                final Boolean damage = true;
                for (final Block b : this.getBlocks(block.getLocation(), face, 0)) {
                    if (b.getType() != Material.BEDROCK) {
                        final BlockBreakEvent event = new BlockBreakEvent(b, player);
                        Bukkit.getPluginManager().callEvent((Event)event);
                        if (event.isCancelled()) {
                            continue;
                        }
                        if (player.getGameMode() == GameMode.CREATIVE) {
                            b.setType(Material.AIR);
                        }
                        else {
                            boolean toggle = true;
                            Boolean hasSilkTouch = false;
                            if (item.getItemMeta().hasEnchants() && item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                                hasSilkTouch = true;
                                ItemStack drop;
                                if (b.getType() == Material.GLOWING_REDSTONE_ORE) {
                                    drop = new ItemStack(Material.REDSTONE_ORE, 1, (short)b.getData());
                                }
                                else {
                                    drop = new ItemStack(b.getType(), 1, (short)b.getData());
                                }
                                b.getWorld().dropItem(b.getLocation(), drop);
                            }
                            if (!hasSilkTouch) {
                                for (final ItemStack drop : b.getDrops()) {
                                    if (this.getItems().contains(b.getType()) && item.getItemMeta().hasEnchants() && item.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && randomPicker(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS), 3)) {
                                        drop.setAmount(drop.getAmount() + getRandomNumber(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)));
                                    }
                                    b.getWorld().dropItem(b.getLocation().add(0.5, 0.0, 0.5), drop);
                                    toggle = true;
                                    if (drop.getType() == Material.GLOWING_REDSTONE_ORE || drop.getType() == Material.REDSTONE_ORE || drop.getType() == Material.LAPIS_ORE) {
                                        break;
                                    }
                                    if (drop.getType() == Material.GLOWSTONE) {
                                        break;
                                    }
                                }
                            }
                            if (toggle) {
                                b.setType(Material.AIR);
                            }
                            else {
                                b.breakNaturally();
                            }
                            if (!damage) {
                                continue;
                            }
                            removeDurability(item, player);
                        }
                    }
                }
                if (!damage) {
                    removeDurability(item, player);
                }
                for (final ItemStack i : drops.keySet()) {
                    if (i.getType() == Material.INK_SACK) {
                        i.setType(new ItemStack(Material.INK_SACK, 1, (short)4).getType());
                    }
                    i.setAmount((int)drops.get(i));
                    if (isInvFull(player)) {
                        player.getWorld().dropItem(player.getLocation(), i);
                    }
                    else {
                        player.getInventory().addItem(new ItemStack[] { i });
                    }
                }
                if (xp > 0) {
                    final ExperienceOrb orb = (ExperienceOrb)block.getWorld().spawn(block.getLocation(), (Class)ExperienceOrb.class);
                    orb.setExperience(xp);
                }
            }
        }
    }
    
    public static boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    
    public static boolean isInvFull(final Player player) {
        return player.getInventory().firstEmpty() == -1;
    }
    
    public static Integer getRandomNumber(int range) {
        if (range == 0) {
            ++range;
        }
        return new Random().nextInt(range) + 1;
    }
    
    public static Integer getRandomNumber(final String range) {
        int number = 1;
        final String[] split = range.split("-");
        if (isInt(split[0]) && isInt(split[1])) {
            final int max = Integer.parseInt(split[1]) + 1;
            final int min = Integer.parseInt(split[0]);
            number = min + new Random().nextInt(max - min);
        }
        return number;
    }
    
    public static void removeDurability(final ItemStack item, final Player player) {
        if (item.hasItemMeta()) {
            try {
                if (item.getItemMeta().spigot().isUnbreakable()) {
                    return;
                }
            }
            catch (NoSuchMethodError noSuchMethodError) {}
            if (item.getItemMeta().hasEnchants() && item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                if (randomPicker(1, 1 + item.getEnchantmentLevel(Enchantment.DURABILITY))) {
                    if (item.getType().getMaxDurability() < item.getDurability()) {
                        player.getInventory().remove(item);
                    }
                    else {
                        item.setDurability((short)(item.getDurability() + 1));
                    }
                }
                return;
            }
        }
        if (item.getType().getMaxDurability() < item.getDurability()) {
            player.getInventory().remove(item);
        }
        else {
            item.setDurability((short)(item.getDurability() + 1));
        }
    }
    
    public static boolean randomPicker(final int max) {
        final Random number = new Random();
        if (max <= 0) {
            return true;
        }
        final int chance = 1 + number.nextInt(max);
        return chance == 1;
    }
    
    public static boolean randomPicker(final int min, final int max) {
        if (max == min || max <= min || max <= 0) {
            return true;
        }
        final Random number = new Random();
        final int chance = 1 + number.nextInt(max);
        return chance >= 1 && chance <= min;
    }
    
    public static Integer percentPick(final int max, final int min) {
        final Random i = new Random();
        if (max == min) {
            return max;
        }
        return min + i.nextInt(max - min);
    }
    
    private Boolean hasSilkTouch(final ItemStack item) {
        if (item.hasItemMeta()) {
            return item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
        }
        return false;
    }
    
    private List<Block> getBlocks(final Location loc, final BlockFace blockFace, final Integer depth) {
        final Location loc2 = loc.clone();
        switch (blockFace) {
            case SOUTH: {
                loc.add(-1.0, 1.0, (double)(-depth));
                loc2.add(1.0, -1.0, 0.0);
                break;
            }
            case WEST: {
                loc.add((double)depth, 1.0, -1.0);
                loc2.add(0.0, -1.0, 1.0);
                break;
            }
            case EAST: {
                loc.add((double)(-depth), 1.0, 1.0);
                loc2.add(0.0, -1.0, -1.0);
                break;
            }
            case NORTH: {
                loc.add(1.0, 1.0, (double)depth);
                loc2.add(-1.0, -1.0, 0.0);
                break;
            }
            case UP: {
                loc.add(-1.0, (double)(-depth), -1.0);
                loc2.add(1.0, 0.0, 1.0);
                break;
            }
            case DOWN: {
                loc.add(1.0, (double)depth, 1.0);
                loc2.add(-1.0, 0.0, -1.0);
                break;
            }
        }
        final List<Block> blocks = new ArrayList<Block>();
        final int topBlockX = (loc.getBlockX() < loc2.getBlockX()) ? loc2.getBlockX() : loc.getBlockX();
        final int bottomBlockX = (loc.getBlockX() > loc2.getBlockX()) ? loc2.getBlockX() : loc.getBlockX();
        final int topBlockY = (loc.getBlockY() < loc2.getBlockY()) ? loc2.getBlockY() : loc.getBlockY();
        final int bottomBlockY = (loc.getBlockY() > loc2.getBlockY()) ? loc2.getBlockY() : loc.getBlockY();
        final int topBlockZ = (loc.getBlockZ() < loc2.getBlockZ()) ? loc2.getBlockZ() : loc.getBlockZ();
        final int bottomBlockZ = (loc.getBlockZ() > loc2.getBlockZ()) ? loc2.getBlockZ() : loc.getBlockZ();
        for (int x = bottomBlockX; x <= topBlockX; ++x) {
            for (int z = bottomBlockZ; z <= topBlockZ; ++z) {
                for (int y = bottomBlockY; y <= topBlockY; ++y) {
                    final Block block = loc.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
    
    private HashMap<Material, ItemStack> getOres() {
        final HashMap<Material, ItemStack> ores = new HashMap<Material, ItemStack>();
        ores.put(Material.COAL_ORE, new ItemStack(Material.COAL));
        ores.put(Material.QUARTZ_ORE, new ItemStack(Material.QUARTZ));
        ores.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        ores.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        ores.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND));
        ores.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD));
        ores.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE));
        ores.put(Material.GLOWING_REDSTONE_ORE, new ItemStack(Material.REDSTONE));
        ores.put(Material.LAPIS_ORE, new ItemStack(Material.INK_SACK, 1, (short)4));
        return ores;
    }
    
    private HashMap<Material, ItemStack> getOres(final int amount) {
        final HashMap<Material, ItemStack> ores = new HashMap<Material, ItemStack>();
        ores.put(Material.COAL_ORE, new ItemStack(Material.COAL, amount));
        ores.put(Material.QUARTZ_ORE, new ItemStack(Material.QUARTZ, amount));
        ores.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT, amount));
        ores.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT, amount));
        ores.put(Material.DIAMOND_ORE, new ItemStack(Material.DIAMOND, amount));
        ores.put(Material.EMERALD_ORE, new ItemStack(Material.EMERALD, amount));
        ores.put(Material.REDSTONE_ORE, new ItemStack(Material.REDSTONE, amount));
        ores.put(Material.GLOWING_REDSTONE_ORE, new ItemStack(Material.REDSTONE, amount));
        ores.put(Material.LAPIS_ORE, new ItemStack(Material.INK_SACK, amount, (short)4));
        return ores;
    }
    
    private ArrayList<Material> getItems() {
        final ArrayList<Material> items = new ArrayList<Material>();
        items.add(Material.COAL_ORE);
        items.add(Material.QUARTZ_ORE);
        items.add(Material.DIAMOND_ORE);
        items.add(Material.EMERALD_ORE);
        items.add(Material.REDSTONE_ORE);
        items.add(Material.GLOWING_REDSTONE_ORE);
        items.add(Material.LAPIS_ORE);
        items.add(Material.LONG_GRASS);
        items.add(Material.NETHER_WARTS);
        items.add(Material.GLOWSTONE);
        items.add(Material.GRAVEL);
        items.add(Material.LEAVES);
        return items;
    }
}
