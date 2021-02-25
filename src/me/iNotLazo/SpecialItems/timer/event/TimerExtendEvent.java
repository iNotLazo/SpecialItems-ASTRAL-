package me.iNotLazo.SpecialItems.timer.event;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import java.util.*;
import me.iNotLazo.SpecialItems.timer.*;
import me.iNotLazo.SpecialItems.timer.Timer;

import javax.annotation.*;

public class TimerExtendEvent extends Event implements Cancellable
{
    private static HandlerList handlers;
    private boolean cancelled;
    private Optional<Player> player;
    private Optional<UUID> userUUID;
    private Timer timer;
    private long previousDuration;
    private long newDuration;
    
    static {
        TimerExtendEvent.handlers = new HandlerList();
    }
    
    public TimerExtendEvent(final Timer timer, final long previousDuration, final long newDuration) {
        this.player = Optional.empty();
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.previousDuration = previousDuration;
        this.newDuration = newDuration;
    }
    
    public TimerExtendEvent(@Nullable final Player player, final UUID uniqueId, final Timer timer, final long previousDuration, final long newDuration) {
        this.player = Optional.ofNullable(player);
        this.userUUID = Optional.ofNullable(uniqueId);
        this.timer = timer;
        this.previousDuration = previousDuration;
        this.newDuration = newDuration;
    }
    
    public Optional<Player> getPlayer() {
        return this.player;
    }
    
    public Optional<UUID> getUserUUID() {
        return this.userUUID;
    }
    
    public Timer getTimer() {
        return this.timer;
    }
    
    public long getPreviousDuration() {
        return this.previousDuration;
    }
    
    public long getNewDuration() {
        return this.newDuration;
    }
    
    public void setNewDuration(final long newDuration) {
        this.newDuration = newDuration;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public static HandlerList getHandlerList() {
        return TimerExtendEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return TimerExtendEvent.handlers;
    }
}
