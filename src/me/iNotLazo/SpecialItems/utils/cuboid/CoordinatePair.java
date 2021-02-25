package me.iNotLazo.SpecialItems.utils.cuboid;

import org.bukkit.block.*;
import org.bukkit.*;

public class CoordinatePair
{
    private String worldName;
    private int x;
    private int z;
    
    public CoordinatePair(final Block block) {
        this(block.getWorld(), block.getX(), block.getZ());
    }
    
    public CoordinatePair(final World world, final int x, final int z) {
        this.worldName = world.getName();
        this.x = x;
        this.z = z;
    }
    
    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }
    
    public String getWorldName() {
        return this.worldName;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
}
