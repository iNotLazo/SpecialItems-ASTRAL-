package me.iNotLazo.SpecialItems.utils.cuboid;

import java.util.*;
import org.bukkit.block.*;
import org.bukkit.*;

public class CuboidBlockIterator implements Iterator<Block>
{
    private World world;
    private int baseX;
    private int baseY;
    private int baseZ;
    private int sizeX;
    private int sizeY;
    private int sizeZ;
    private int x;
    private int y;
    private int z;
    
    public CuboidBlockIterator(final World world, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        this.world = world;
        this.baseX = x1;
        this.baseY = y1;
        this.baseZ = z1;
        this.sizeX = Math.abs(x2 - x1) + 1;
        this.sizeY = Math.abs(y2 - y1) + 1;
        this.sizeZ = Math.abs(z2 - z1) + 1;
        this.z = 0;
        this.y = 0;
        this.x = 0;
    }
    
    @Override
    public boolean hasNext() {
        return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
    }
    
    @Override
    public Block next() {
        final Block block = this.world.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
        if (++this.x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
                this.y = 0;
                ++this.z;
            }
        }
        return block;
    }
    
    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}