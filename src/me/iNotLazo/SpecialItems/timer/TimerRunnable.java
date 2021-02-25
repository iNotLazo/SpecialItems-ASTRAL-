package me.iNotLazo.SpecialItems.timer;

import java.util.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import me.iNotLazo.SpecialItems.timer.event.*;
import org.bukkit.event.*;
import me.iNotLazo.SpecialItems.*;
import org.bukkit.plugin.*;

public class TimerRunnable
{
    private BukkitTask eventNotificationTask;
    private Timer timer;
    private UUID owner;
    private long expiryMillis;
    private long pauseMillis;
    private boolean cancelled;
    
    public TimerRunnable(final Timer timer, final long duration) {
        this.cancelled = false;
        this.owner = null;
        this.timer = timer;
        this.setRemaining(duration);
    }
    
    public TimerRunnable(final Timer timer, final UUID playerUUID, final long duration) {
        this.cancelled = false;
        this.timer = timer;
        this.owner = playerUUID;
        this.setRemaining(duration);
    }
    
    public Timer getTimer() {
        return this.timer;
    }
    
    public long getRemaining() {
        return this.getRemaining(false);
    }
    
    public long getRemaining(final boolean ignorePaused) {
        if (!ignorePaused && this.pauseMillis != 0L) {
            return this.pauseMillis;
        }
        return this.expiryMillis - System.currentTimeMillis();
    }
    
    public long getExpiryMillis() {
        return this.expiryMillis;
    }
    
    public void setRemaining(final long remaining) {
        this.setExpiryMillis(remaining);
    }
    
    private void setExpiryMillis(final long remainingMillis) {
        final long expiryMillis = System.currentTimeMillis() + remainingMillis;
        if (expiryMillis == this.expiryMillis) {
            return;
        }
        this.expiryMillis = expiryMillis;
        if (remainingMillis > 0L) {
            if (this.eventNotificationTask != null) {
                this.eventNotificationTask.cancel();
            }
            this.eventNotificationTask = new BukkitRunnable() {
                public void run() {
                    Bukkit.getPluginManager().callEvent((Event)new TimerExpireEvent(TimerRunnable.this.owner, TimerRunnable.this.timer));
                }
            }.runTaskLater((Plugin)Main.getPlugin(), remainingMillis / 50L);
        }
    }
    
    public long getPauseMillis() {
        return this.pauseMillis;
    }
    
    public void setPauseMillis(final long pauseMillis) {
        this.pauseMillis = pauseMillis;
    }
    
    public boolean isPaused() {
        return this.pauseMillis != 0L;
    }
    
    public void setPaused(final boolean paused) {
        if (paused != this.isPaused()) {
            if (paused) {
                this.pauseMillis = this.getRemaining(true);
                this.cancel();
            }
            else {
                this.setExpiryMillis(this.pauseMillis);
                this.pauseMillis = 0L;
            }
        }
    }
    
    public long getRemaining(final boolean ignorePaused, final long now) {
        if (!ignorePaused && this.pauseMillis != 0L) {
            return this.pauseMillis;
        }
        return this.expiryMillis - now;
    }
    
    public boolean check(final long now) {
        if (this.cancelled) {
            return true;
        }
        if (this.getRemaining(false, now) <= 0L) {
            final TimerExpireEvent expireEvent = new TimerExpireEvent(this.owner, this.timer);
            Bukkit.getPluginManager().callEvent((Event)expireEvent);
            return true;
        }
        return false;
    }
    
    public boolean cancel() {
        if (this.eventNotificationTask != null) {
            this.eventNotificationTask.cancel();
            return true;
        }
        return false;
    }
}
