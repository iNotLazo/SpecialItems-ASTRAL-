package me.iNotLazo.SpecialItems.timer.event;

import org.bukkit.event.*;
import java.util.*;
import me.iNotLazo.SpecialItems.timer.*;
import me.iNotLazo.SpecialItems.timer.Timer;

public class TimerPauseEvent extends Event implements Cancellable
{
    private static HandlerList handlers;
    private boolean cancelled;
    private boolean paused;
    private Optional<UUID> userUUID;
    private Timer timer;
    
    static {
        TimerPauseEvent.handlers = new HandlerList();
    }
    
    public TimerPauseEvent(final Timer timer, final boolean paused) {
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.paused = paused;
    }
    
    public TimerPauseEvent(final UUID userUUID, final Timer timer, final boolean paused) {
        this.userUUID = Optional.ofNullable(userUUID);
        this.timer = timer;
        this.paused = paused;
    }
    
    public Optional<UUID> getUserUUID() {
        return this.userUUID;
    }
    
    public Timer getTimer() {
        return this.timer;
    }
    
    public static HandlerList getHandlerList() {
        return TimerPauseEvent.handlers;
    }
    
    public boolean isPaused() {
        return this.paused;
    }
    
    public HandlerList getHandlers() {
        return TimerPauseEvent.handlers;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
