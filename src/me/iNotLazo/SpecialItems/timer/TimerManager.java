package me.iNotLazo.SpecialItems.timer;

import org.bukkit.event.*;
import me.iNotLazo.SpecialItems.timer.impl.special.*;
import me.iNotLazo.SpecialItems.utils.*;
import me.iNotLazo.SpecialItems.*;
import java.util.*;

public class TimerManager implements Listener
{
    private Set<Timer> timers;
    private Config config;
    @SuppressWarnings("unused")
	private GrapplingHookTimer grapplingHookTimer;
    @SuppressWarnings("unused")
    private ImmobilizerTimer immobilizerTimer;
    @SuppressWarnings("unused")
    private SwitcherBallTimer SwitcherBallTimer;
    @SuppressWarnings("unused")
    private FleeceCostumeTimer fleeceCostumeTimer;
    @SuppressWarnings("unused")
    private FastCarrotTimer fastcarrotTimer;
    @SuppressWarnings("unused")
    private ViewChangerTimer viewchangerTimer;
    @SuppressWarnings("unused")
    private SuperStandTimer superstandTimer;
    @SuppressWarnings("unused")
    private SpaceRocketTimer spacerocketTimer;
    @SuppressWarnings("unused")
    private AntiTrapStarTimer antitrapstarTimer;
    @SuppressWarnings("unused")
    private FakePearlTimer fakepearlTimer;
    @SuppressWarnings("unused")
    private AntiPearlTimer antipearlTimer;
    @SuppressWarnings("unused")
    private TrapAxeTimer trapaxeTimer;
    @SuppressWarnings("unused")
    private DisturbTimer disturbTimer;
    @SuppressWarnings("unused")
    private ArmorThiefTimer armorthiefTimer;
    @SuppressWarnings("unused")
    private FaithFishTimer faithfishTimer;
    @SuppressWarnings("unused")
    private PocketBardTimer pocketbardTimer;
    @SuppressWarnings("unused")
	private ImpalerTimer impalerTimer;
    
    public TimerManager() {
        this.timers = new LinkedHashSet<Timer>();
        this.registerTimer(this.impalerTimer = new ImpalerTimer());
        this.registerTimer(this.pocketbardTimer = new PocketBardTimer());
        this.registerTimer(this.faithfishTimer = new FaithFishTimer());
        this.registerTimer(this.disturbTimer = new DisturbTimer());
        this.registerTimer(this.trapaxeTimer = new TrapAxeTimer());
        this.registerTimer(this.antipearlTimer = new AntiPearlTimer());
        this.registerTimer(this.armorthiefTimer = new ArmorThiefTimer());
        this.registerTimer(this.fakepearlTimer = new FakePearlTimer());
        this.registerTimer(this.antitrapstarTimer = new AntiTrapStarTimer());
        this.registerTimer(this.spacerocketTimer = new SpaceRocketTimer());
        this.registerTimer(this.superstandTimer = new SuperStandTimer());
        this.registerTimer(this.viewchangerTimer = new ViewChangerTimer());
        this.registerTimer(this.fastcarrotTimer = new FastCarrotTimer());
        this.registerTimer(this.grapplingHookTimer = new GrapplingHookTimer());
        this.registerTimer(this.fleeceCostumeTimer = new FleeceCostumeTimer());
        this.registerTimer(this.immobilizerTimer = new ImmobilizerTimer());
        this.registerTimer(this.SwitcherBallTimer = new SwitcherBallTimer());
        this.reloadTimerData();
    }
    
    public void registerTimer(final Timer timer) {
        this.timers.add(timer);
        if (timer instanceof Listener) {
            ListenerHandler.INSTANCE.registerListener((Listener)timer);
        }
    }
    
    public void unregisterTimer(final Timer timer) {
        this.timers.remove(timer);
    }
    
    public void reloadTimerData() {
        this.config = new Config(Main.getPlugin(), "timers");
        for (final Timer timer : this.timers) {
            timer.load(this.config);
        }
    }
    
    public void saveTimerData() {
        for (final Timer timer : this.timers) {
            timer.onDisable(this.config);
        }
        this.config.save();
    }
    
    public Set<Timer> getTimers() {
        return this.timers;
    }
    
    public Config getConfig() {
        return this.config;
    }
}
