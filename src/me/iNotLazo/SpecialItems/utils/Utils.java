package me.iNotLazo.SpecialItems.utils;

import java.util.concurrent.*;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.*;
import org.bukkit.World;

import java.text.*;
import org.apache.commons.lang.time.*;
import com.google.common.base.*;
import com.google.common.base.Optional;

import javax.annotation.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PlayerInventory;

import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_8_R3.inventory.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.*;
import java.lang.reflect.*;
import org.bukkit.block.*;
import org.bukkit.block.Block;

public class Utils
{
    private static long MINUTE;
    private static long HOUR;
    public static String STICK;
    private static Map<UUID, String> uuidToName;
    private static Map<String, UUID> nameToUUID;
    
    static {
        Utils.MINUTE = TimeUnit.MINUTES.toMillis(1L);
        Utils.HOUR = TimeUnit.HOURS.toMillis(1L);
        Utils.STICK = "\u2503";
        Utils.uuidToName = new ConcurrentHashMap<UUID, String>();
        Utils.nameToUUID = new ConcurrentHashMap<String, UUID>();
    }
    
    public static UUID uuid(final String name) {
        return Utils.nameToUUID.get(name.toLowerCase());
    }
    
    public static String name(final UUID uuid) {
        return Utils.uuidToName.get(uuid);
    }
    
    public static boolean ensure(final UUID uuid) {
        if (String.valueOf(name(uuid)).equals("null")) {
            Main.getPlugin().getLogger().warning(uuid + " didn't have a cached name.");
            return false;
        }
        return true;
    }
    
    public static Location destringifyLocation(final String string) {
        final String[] split = string.substring(1, string.length() - 2).split(",");
        final World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            return null;
        }
        final double x = Double.parseDouble(split[1]);
        final double y = Double.parseDouble(split[2]);
        final double z = Double.parseDouble(split[3]);
        final float yaw = Float.parseFloat(split[4]);
        final float pitch = Float.parseFloat(split[5]);
        final Location loc = new Location(world, x, y, z);
        loc.setYaw(yaw);
        loc.setPitch(pitch);
        return loc;
    }
    
    public static List<String> getCompletions(final String[] args, final List<String> input) {
        return getCompletions(args, input);
    }
    
    public static String stringifyLocation(final Location location) {
        return "[" + location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch() + "]";
    }
    
    public static String getRemaining(final long millis, final boolean milliseconds) {
        return getRemaining(millis, milliseconds, true);
    }
    
    public static String getRemainingg(final long millis, final boolean milliseconds) {
        return getRemainingg(millis, milliseconds, true);
    }
    
    public static String getRemaining(final long duration, final boolean milliseconds, final boolean trail) {
        if (milliseconds && duration < Utils.MINUTE) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, String.valueOf((duration >= Utils.HOUR) ? "HH:" : "") + "mm:ss");
    }
    
    public static String getRemainingg(final long duration, final boolean milliseconds, final boolean trail) {
        if (milliseconds && duration < Utils.MINUTE) {
            return String.valueOf((trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(duration * 0.001)) + 's';
        }
        return DurationFormatUtils.formatDuration(duration, String.valueOf((duration >= Utils.HOUR) ? "H:" : "") + "mm:ss");
    }
    
    public static String TimerFormat(final double data) {
        final int minutes = (int)(data / 60.0);
        final int seconds = (int)(data % 60.0);
        final String str = String.format("%02d:%02d", minutes, seconds);
        return str;
    }
    
    public static String formatSecondsToHours(final double d) {
        final int i = (int)(d / 3600.0);
        final int j = (int)(d % 3600.0 / 60.0);
        final int k = (int)(d % 60.0);
        final String str = String.format("%02d:%02d:%02d", i, j, k);
        return str;
    }
    
    public static <T extends Enum<T>> Optional<T> getIfPresent(final Class<T> enumClass, final String value) {
        Preconditions.checkNotNull((Object)enumClass);
        Preconditions.checkNotNull((Object)value);
        try {
            return (Optional)Optional.of((Object)Enum.valueOf(enumClass, value));
        }
        catch (IllegalArgumentException iae) {
            return (Optional)Optional.absent();
        }
    }
    
    public static <T> T firstNonNull(@Nullable final T first, @Nullable final T second) {
        return (T)((first != null) ? first : Preconditions.checkNotNull((Object)second));
    }
    
    public static void resendHeldItemPacket(final Player player) {
        sendItemPacketAtHeldSlot(player, getCleanHeldItem(player));
    }
    
    public static void sendItemPacketAtHeldSlot(final Player player, final net.minecraft.server.v1_8_R3.ItemStack stack) {
        sendItemPacketAtSlot(player, stack, player.getInventory().getHeldItemSlot());
    }
    
    public static void sendItemPacketAtSlot(final Player player, final net.minecraft.server.v1_8_R3.ItemStack stack, final int index) {
        sendItemPacketAtSlot(player, stack, index, ((CraftPlayer)player).getHandle().defaultContainer.windowId);
    }
    
    public static void sendItemPacketAtSlot(final Player player, final net.minecraft.server.v1_8_R3.ItemStack stack, int index, final int windowID) {
        final EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        if (entityPlayer.playerConnection != null) {
            if (index < PlayerInventory.getHotbarSize()) {
                index += 36;
            }
            else if (index > 35) {
                index = 8 - (index - 36);
            }
            entityPlayer.playerConnection.sendPacket((Packet)new PacketPlayOutSetSlot(windowID, index, stack));
        }
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getCleanItem(final Inventory inventory, final int slot) {
        return ((CraftInventory)inventory).getInventory().getItem(slot);
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getCleanItem(final Player player, final int slot) {
        return getCleanItem((Inventory)player.getInventory(), slot);
    }
    
    public static net.minecraft.server.v1_8_R3.ItemStack getCleanHeldItem(final Player player) {
        return getCleanItem(player, player.getInventory().getHeldItemSlot());
    }
    
    public static boolean isOnline(final CommandSender sender, final Player player) {
        return player != null && (!(sender instanceof Player) || ((Player)sender).canSee(player));
    }
    
    public static int getPing(final Player player) {
        final CraftPlayer craft = (CraftPlayer)player;
        final int ping = craft.getHandle().ping - 20;
        return (ping > 0) ? ping : 0;
    }
    
    public static List<Entity> getNearby(final Location loc, final int distance) {
        final List<Entity> list = new ArrayList<Entity>();
        for (final Entity e : loc.getWorld().getEntities()) {
            if (e instanceof Player) {
                continue;
            }
            if (!e.getType().isAlive()) {
                continue;
            }
            if (loc.distance(e.getLocation()) > distance) {
                continue;
            }
            list.add(e);
        }
        for (final Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online.getWorld() == loc.getWorld() && loc.distance(online.getLocation()) <= distance) {
                list.add((Entity)online);
            }
        }
        return list;
    }
    
    public static void setMaxPlayers(final int amount) throws ReflectiveOperationException {
        final String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        final Object playerlist = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".CraftServer").getDeclaredMethod("getHandle", (Class<?>[])null).invoke(Bukkit.getServer(), (Object[])null);
        final Field maxplayers = playerlist.getClass().getSuperclass().getDeclaredField("maxPlayers");
        maxplayers.setAccessible(true);
        maxplayers.set(playerlist, amount);
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
}
