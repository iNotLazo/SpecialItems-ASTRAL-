package me.iNotLazo.SpecialItems.utils;

import com.google.common.collect.*;
import java.util.concurrent.*;
import java.text.*;
import org.apache.commons.lang.time.*;
import com.google.common.base.*;
import java.util.stream.*;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import org.bukkit.event.entity.*;
import org.bukkit.projectiles.*;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.block.Block;
import org.bukkit.potion.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.*;

public class BukkitUtils
{
    public static String STRAIGHT_LINE_DEFAULT;
    private static ImmutableMap<Object, Object> CHAT_DYE_COLOUR_MAP;
    private static ImmutableSet<Object> DEBUFF_TYPES;
    private static int DEFAULT_COMPLETION_LIMIT;
    private static String STRAIGHT_LINE_TEMPLATE;
    
    static {
        BukkitUtils.DEFAULT_COMPLETION_LIMIT = 80;
        BukkitUtils.STRAIGHT_LINE_TEMPLATE = String.valueOf(ChatColor.STRIKETHROUGH.toString()) + Strings.repeat("-", 256);
        BukkitUtils.STRAIGHT_LINE_DEFAULT = BukkitUtils.STRAIGHT_LINE_TEMPLATE.substring(0, 55);
        BukkitUtils.CHAT_DYE_COLOUR_MAP = (ImmutableMap<Object, Object>)ImmutableMap.builder().put((Object)ChatColor.AQUA, (Object)DyeColor.LIGHT_BLUE).put((Object)ChatColor.BLACK, (Object)DyeColor.BLACK).put((Object)ChatColor.BLUE, (Object)DyeColor.LIGHT_BLUE).put((Object)ChatColor.DARK_AQUA, (Object)DyeColor.CYAN).put((Object)ChatColor.DARK_BLUE, (Object)DyeColor.BLUE).put((Object)ChatColor.DARK_GRAY, (Object)DyeColor.GRAY).put((Object)ChatColor.DARK_GREEN, (Object)DyeColor.GREEN).put((Object)ChatColor.DARK_PURPLE, (Object)DyeColor.PURPLE).put((Object)ChatColor.DARK_RED, (Object)DyeColor.RED).put((Object)ChatColor.GOLD, (Object)DyeColor.ORANGE).put((Object)ChatColor.GRAY, (Object)DyeColor.SILVER).put((Object)ChatColor.GREEN, (Object)DyeColor.LIME).put((Object)ChatColor.LIGHT_PURPLE, (Object)DyeColor.MAGENTA).put((Object)ChatColor.RED, (Object)DyeColor.RED).put((Object)ChatColor.WHITE, (Object)DyeColor.WHITE).put((Object)ChatColor.YELLOW, (Object)DyeColor.YELLOW).build();
        BukkitUtils.DEBUFF_TYPES = (ImmutableSet<Object>)ImmutableSet.builder().add((Object)PotionEffectType.BLINDNESS).add((Object)PotionEffectType.CONFUSION).add((Object)PotionEffectType.HARM).add((Object)PotionEffectType.HUNGER).add((Object)PotionEffectType.POISON).add((Object)PotionEffectType.SATURATION).add((Object)PotionEffectType.SLOW).add((Object)PotionEffectType.SLOW_DIGGING).add((Object)PotionEffectType.WEAKNESS).add((Object)PotionEffectType.WITHER).build();
    }
    
    public static String getRemaining(final long millis, final boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }
    
    public static String getRemaining(final long duration, final boolean milliseconds, final boolean trail) {
        if (milliseconds && duration < TimeUnit.MINUTES.toMillis(1L)) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, String.valueOf((duration >= TimeUnit.HOURS.toMillis(1L)) ? "HH:" : "") + "mm:ss");
    }
    
    public static String handleBardFormat(final long millis, final boolean trailingZero) {
        return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
    }
    
    public static String getRemainingSpawn(final long duration) {
        return DurationFormatUtils.formatDuration(duration, String.valueOf((duration >= TimeUnit.HOURS.toMillis(1L)) ? "HH:" : "") + "mm:ss");
    }
    
    public static int countColoursUsed(final String id, final boolean ignoreDuplicates) {
        final ChatColor[] values = ChatColor.values();
        final List<Character> charList = new ArrayList<Character>(values.length);
        ChatColor[] array;
        for (int length = (array = values).length, j = 0; j < length; ++j) {
            final ChatColor colour = array[j];
            charList.add(colour.getChar());
        }
        int count = 0;
        final Set<ChatColor> found = new HashSet<ChatColor>();
        for (int i = 1; i < id.length(); ++i) {
            if (charList.contains(id.charAt(i)) && id.charAt(i - 1) == '&') {
                final ChatColor colour2 = ChatColor.getByChar(id.charAt(i));
                if (found.add(colour2) || ignoreDuplicates) {
                    ++count;
                }
            }
        }
        return count;
    }
    
    public static List<String> getCompletions(final String[] args, final List<String> input) {
        return getCompletions(args, input, BukkitUtils.DEFAULT_COMPLETION_LIMIT);
    }
    
    public static List<String> getCompletions(final String[] args, final List<String> input, final int limit) {
        Preconditions.checkNotNull((Object)args);
        Preconditions.checkArgument(args.length != 0);
        final String argument = args[args.length - 1];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }
    
    public static String getDisplayName(final CommandSender sender) {
        Preconditions.checkNotNull((Object)sender);
        return (sender instanceof Player) ? ((Player)sender).getDisplayName() : sender.getName();
    }
    
    public static long getIdleTime(final Player player) {
        Preconditions.checkNotNull((Object)player);
        final long idleTime = ((CraftPlayer)player).getHandle().D();
        return (idleTime > 0L) ? (MinecraftServer.az() - idleTime) : 0L;
    }
    
    public static DyeColor toDyeColor(final ChatColor colour) {
        return (DyeColor)BukkitUtils.CHAT_DYE_COLOUR_MAP.get((Object)colour);
    }
    
    public static boolean hasMetaData(final Metadatable metadatable, final String input, final Plugin plugin) {
        return getMetaData(metadatable, input, plugin) != null;
    }
    
    public static MetadataValue getMetaData(final Metadatable metadatable, final String input, final Plugin plugin) {
        return (MetadataValue)metadatable.getMetadata(input);
    }
    
    public static Player getFinalAttacker(final EntityDamageEvent ede, final boolean ignoreSelf) {
        Player attacker = null;
        if (ede instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)ede;
            final Entity damager = event.getDamager();
            if (event.getDamager() instanceof Player) {
                attacker = (Player)damager;
            }
            else if (event.getDamager() instanceof Projectile) {
                final Projectile projectile = (Projectile)damager;
                final ProjectileSource shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    attacker = (Player)shooter;
                }
            }
            if (attacker != null && ignoreSelf && event.getEntity().equals(attacker)) {
                attacker = null;
            }
        }
        return attacker;
    }
    
    public static Player playerWithNameOrUUID(final String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getPlayer(UUID.fromString(string)) : Bukkit.getPlayer(string);
    }
    
    @Deprecated
    public static OfflinePlayer offlinePlayerWithNameOrUUID(final String string) {
        if (string == null) {
            return null;
        }
        return JavaUtils.isUUID(string) ? Bukkit.getOfflinePlayer(UUID.fromString(string)) : Bukkit.getOfflinePlayer(string);
    }
    
    public static boolean isWithinX(final Location location, final Location other, final double distance) {
        return location.getWorld().equals(other.getWorld()) && Math.abs(other.getX() - location.getX()) <= distance && Math.abs(other.getZ() - location.getZ()) <= distance;
    }
    
    public static Location getHighestLocation(final Location origin) {
        return getHighestLocation(origin, null);
    }
    
    public static Location getHighestLocation(final Location origin, final Location def) {
        Preconditions.checkNotNull((Object)origin, (Object)"The location cannot be null");
        final Location cloned = origin.clone();
        final World world = cloned.getWorld();
        final int x = cloned.getBlockX();
        int y = world.getMaxHeight();
        final int z = cloned.getBlockZ();
        while (y > origin.getBlockY()) {
            final Block block = world.getBlockAt(x, --y, z);
            if (!block.isEmpty()) {
                final Location next = block.getLocation();
                next.setPitch(origin.getPitch());
                next.setYaw(origin.getYaw());
                return next;
            }
        }
        return def;
    }
    
    public static boolean isDebuff(final PotionEffectType type) {
        return BukkitUtils.DEBUFF_TYPES.contains((Object)type);
    }
    
    public static boolean isDebuff(final PotionEffect potionEffect) {
        return isDebuff(potionEffect.getType());
    }
    
    public static boolean isDebuff(final ThrownPotion thrownPotion) {
        for (final PotionEffect effect : thrownPotion.getEffects()) {
            if (isDebuff(effect)) {
                return true;
            }
        }
        return false;
    }
}
