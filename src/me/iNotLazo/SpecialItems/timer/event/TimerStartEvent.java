package me.iNotLazo.SpecialItems.timer.event;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import java.util.*;
import me.iNotLazo.SpecialItems.timer.*;
import me.iNotLazo.SpecialItems.timer.Timer;

import javax.annotation.*;

public class TimerStartEvent extends Event
{
    private static HandlerList handlers;
    private Optional<Player> player;
    private Optional<UUID> userUUID;
    private Timer timer;
    private long duration;
    
    static {
        TimerStartEvent.handlers = new HandlerList();
    }
    
    public TimerStartEvent(final Timer timer, final long duration) {
        this.player = Optional.empty();
        this.userUUID = Optional.empty();
        this.timer = timer;
        this.duration = duration;
    }
    
    public TimerStartEvent(@Nullable final Player player, final UUID uniqueId, final Timer timer, final long duration) {
        this.player = Optional.ofNullable(player);
        this.userUUID = Optional.ofNullable(uniqueId);
        this.timer = timer;
        this.duration = duration;
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
    
    public long getDuration() {
        return this.duration;
    }
    
    public static HandlerList getHandlerList() {
        return TimerStartEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return TimerStartEvent.handlers;
    }
}
