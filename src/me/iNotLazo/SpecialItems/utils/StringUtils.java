package me.iNotLazo.SpecialItems.utils;

public class StringUtils
{
    public static String join(final String[] args) {
        return join(args, 0);
    }
    
    public static String join(final String[] args, final int start) {
        final StringBuilder ret = new StringBuilder();
        for (int i = start; i < args.length; ++i) {
            ret.append(args[i]).append(" ");
        }
        return ret.toString();
    }
}
