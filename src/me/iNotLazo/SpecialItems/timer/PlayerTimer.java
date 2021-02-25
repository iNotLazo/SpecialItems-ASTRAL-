package me.iNotLazo.SpecialItems.timer;

import java.util.concurrent.*;
import org.bukkit.entity.*;
import javax.annotation.*;
import org.bukkit.*;
import org.bukkit.event.*;
import java.util.function.*;
import me.iNotLazo.SpecialItems.timer.event.*;
import me.iNotLazo.SpecialItems.utils.*;
import org.bukkit.configuration.*;
import java.util.*;

public abstract class PlayerTimer extends Timer
{
    protected boolean persistable;
    protected Map<UUID, TimerRunnable> cooldowns;
    private static String COOLDOWN_PATH;
    private static String PAUSE_PATH;
    
    static {
        PlayerTimer.COOLDOWN_PATH = "timer-cooldowns";
        PlayerTimer.PAUSE_PATH = "timer-pauses";
    }
    
    public PlayerTimer(final String name, final long defaultCooldown) {
        this(name, defaultCooldown, true);
    }
    
    public PlayerTimer(final String name, final long defaultCooldown, final boolean persistable) {
        super(name, defaultCooldown);
        this.cooldowns = new ConcurrentHashMap<UUID, TimerRunnable>();
        this.persistable = persistable;
    }
    
    public void onExpire(final UUID userUUID) {
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTimerExpireLoadReduce(final TimerExpireEvent event) {
        if (event.getTimer().equals(this)) {
            final Optional<UUID> optionalUserUUID = event.getUserUUID();
            if (optionalUserUUID.isPresent()) {
                final UUID userUUID = optionalUserUUID.get();
                this.onExpire(userUUID);
                this.clearCooldown(userUUID);
            }
        }
    }
    
    public TimerRunnable clearCooldown(final UUID uuid) {
        return this.clearCooldown(null, uuid);
    }
    
    public TimerRunnable clearCooldown(final Player player) {
        Objects.requireNonNull(player);
        return this.clearCooldown(player, player.getUniqueId());
    }
    
    public TimerRunnable clearCooldown(@Nullable final Player player, final UUID playerUUID) {
        final TimerRunnable runnable = this.cooldowns.remove(playerUUID);
        if (runnable != null) {
            runnable.cancel();
            if (player == null) {
                Bukkit.getPluginManager().callEvent((Event)new TimerClearEvent(playerUUID, this));
            }
            else {
                Bukkit.getPluginManager().callEvent((Event)new TimerClearEvent(player, this));
            }
        }
        return runnable;
    }
    
    public boolean isPaused(final Player player) {
        return this.isPaused(player.getUniqueId());
    }
    
    public boolean isPaused(final UUID playerUUID) {
        final TimerRunnable runnable = this.cooldowns.get(playerUUID);
        return runnable != null && runnable.isPaused();
    }
    
    public void setPaused(final UUID playerUUID, final boolean paused) {
        final TimerRunnable runnable = this.cooldowns.get(playerUUID);
        if (runnable != null && runnable.isPaused() != paused) {
            final TimerPauseEvent event = new TimerPauseEvent(playerUUID, this, paused);
            Bukkit.getPluginManager().callEvent((Event)event);
            if (!event.isCancelled()) {
                runnable.setPaused(paused);
            }
        }
    }
    
    public long getRemaining(final Player player) {
        return this.getRemaining(player.getUniqueId());
    }
    
    public long getRemaining(final UUID playerUUID) {
        final TimerRunnable runnable = this.cooldowns.get(playerUUID);
        return (runnable == null) ? 0L : runnable.getRemaining();
    }
    
    public boolean setCooldown(@Nullable final Player player, final UUID playerUUID) {
        return this.setCooldown(player, playerUUID, this.defaultCooldown, false);
    }
    
    public boolean setCooldown(@Nullable final Player player, final UUID playerUUID, final long duration, final boolean overwrite) {
        return this.setCooldown(player, playerUUID, duration, overwrite, null);
    }
    
    public boolean setCooldown(@Nullable final Player player, final UUID playerUUID, final long duration, final boolean overwrite, @Nullable final Predicate<Long> currentCooldownPredicate) {
        TimerRunnable runnable = (duration > 0L) ? this.cooldowns.get(playerUUID) : this.clearCooldown(player, playerUUID);
        if (runnable == null) {
            Bukkit.getPluginManager().callEvent((Event)new TimerStartEvent(player, playerUUID, this, duration));
            runnable = new TimerRunnable(this, playerUUID, duration);
            this.cooldowns.put(playerUUID, runnable);
            return true;
        }
        final long remaining = runnable.getRemaining();
        if (!overwrite && remaining > 0L && duration <= remaining) {
            return false;
        }
        final TimerExtendEvent event = new TimerExtendEvent(player, playerUUID, this, remaining, duration);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return false;
        }
        boolean flag = true;
        if (currentCooldownPredicate != null) {
            flag = currentCooldownPredicate.test(remaining);
        }
        if (flag) {
            runnable.setRemaining(duration);
        }
        return flag;
    }
    
    @Override
    public void load(final Config config) {
        if (!this.persistable) {
            return;
        }
        String path = String.valueOf(PlayerTimer.COOLDOWN_PATH) + '.' + this.name;
        Object object = config.get(path);
        if (object instanceof MemorySection) {
            final MemorySection section = (MemorySection)object;
            final long millis = System.currentTimeMillis();
            for (final String id : section.getKeys(false)) {
                final long remaining = config.getLong(String.valueOf(section.getCurrentPath()) + '.' + id) - millis;
                if (remaining > 0L) {
                    this.setCooldown(null, UUID.fromString(id), remaining, true, null);
                }
            }
        }
        path = String.valueOf(PlayerTimer.PAUSE_PATH) + '.' + this.name;
        if ((object = config.get(path)) instanceof MemorySection) {
            final MemorySection section = (MemorySection)object;
            for (final String id2 : section.getKeys(false)) {
                final TimerRunnable timerCooldown = this.cooldowns.get(UUID.fromString(id2));
                if (timerCooldown == null) {
                    continue;
                }
                timerCooldown.setPauseMillis(config.getLong(String.valueOf(path) + '.' + id2));
            }
        }
    }
    
    @Override
    public void onDisable(final Config config) {
        if (this.persistable) {
            final Set<Map.Entry<UUID, TimerRunnable>> entrySet = this.cooldowns.entrySet();
            final Map<String, Long> pauseSavemap = new LinkedHashMap<String, Long>(entrySet.size());
            final Map<String, Long> cooldownSavemap = new LinkedHashMap<String, Long>(entrySet.size());
            for (final Map.Entry<UUID, TimerRunnable> entry : entrySet) {
                final String id = entry.getKey().toString();
                final TimerRunnable runnable = entry.getValue();
                pauseSavemap.put(id, runnable.getPauseMillis());
                cooldownSavemap.put(id, runnable.getExpiryMillis());
            }
            config.set("timer-pauses." + this.name, (Object)pauseSavemap);
            config.set("timer-cooldowns." + this.name, (Object)cooldownSavemap);
        }
    }
}
