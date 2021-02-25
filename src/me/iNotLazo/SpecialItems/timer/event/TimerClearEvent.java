package me.iNotLazo.SpecialItems.timer.event;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import me.iNotLazo.SpecialItems.timer.*;
import me.iNotLazo.SpecialItems.timer.Timer;

import java.util.*;
import org.bukkit.*;

public class TimerClearEvent extends Event
{
    private static HandlerList handlers;
    private Optional<Player> player;
    private Optional<UUID> userUUID;
    private Timer timer;
    
    static {
        TimerClearEvent.handlers = new HandlerList();
    }
    
    public TimerClearEvent(final Timer timer) {
        this.userUUID = Optional.empty();
        this.timer = timer;
    }
    
    public TimerClearEvent(final UUID userUUID, final Timer timer) {
        this.userUUID = Optional.of(userUUID);
        this.timer = timer;
    }
    
    public TimerClearEvent(final Player player, final Timer timer) {
        Objects.requireNonNull(player);
        this.player = Optional.of(player);
        this.userUUID = Optional.of(player.getUniqueId());
        this.timer = timer;
    }
    
    public Optional<Player> getPlayer() {
        if (this.player == null) {
            this.player = (this.userUUID.isPresent() ? Optional.of(Bukkit.getPlayer((UUID)this.userUUID.get())) : Optional.empty());
        }
        return this.player;
    }
    
    public Optional<UUID> getUserUUID() {
        return this.userUUID;
    }
    
    public Timer getTimer() {
        return this.timer;
    }
    
    public static HandlerList getHandlerList() {
        return TimerClearEvent.handlers;
    }
    
    public HandlerList getHandlers() {
        return TimerClearEvent.handlers;
    }
}
