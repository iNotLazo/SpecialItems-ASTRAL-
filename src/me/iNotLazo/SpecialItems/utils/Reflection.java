package me.iNotLazo.SpecialItems.utils;

import java.text.*;
import org.bukkit.*;
import java.lang.reflect.*;

public class Reflection
{
    private final String name;
    private final String version;
    private final DecimalFormat format;
    private Object serverInstance;
    private Field tpsField;
    
    public Reflection() {
        this.name = Bukkit.getServer().getClass().getPackage().getName();
        this.version = this.name.substring(this.name.lastIndexOf(46) + 1);
        this.format = new DecimalFormat("##.##");
    }
    
    private Class<?> getNMSClass(final String className) {
        try {
            return Class.forName("net.minecraft.server." + this.version + "." + className);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void getTpsRun() {
        try {
            this.serverInstance = this.getNMSClass("MinecraftServer").getMethod("getServer", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            this.tpsField = this.serverInstance.getClass().getField("recentTps");
        }
        catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException ex2) {
            final Exception ex;
            final Exception e = ex2;
            e.printStackTrace();
        }
    }
    
    public String getTPS(final int time) {
        try {
            final double[] tps = (double[])this.tpsField.get(this.serverInstance);
            return this.format.format(tps[time]);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Long getLag() {
        return Math.round((1.0 - Double.parseDouble(this.getTPS(0).replace(",", ".")) / 20.0) * 100.0);
    }
}
