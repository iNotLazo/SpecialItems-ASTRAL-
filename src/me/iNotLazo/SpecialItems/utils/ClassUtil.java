package me.iNotLazo.SpecialItems.utils;

import org.bukkit.plugin.*;
import java.io.*;
import java.util.jar.*;
import com.google.common.collect.*;
import java.security.*;
import java.net.*;
import java.util.*;

public final class ClassUtil
{
    private ClassUtil() {
        throw new RuntimeException("Cannot instantiate a utility class.");
    }
    
    public static Collection<Class<?>> getClassesInPackage(final Plugin plugin, final String packageName) {
        final Collection<Class<?>> classes = new ArrayList<Class<?>>();
        final CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        final URL resource = codeSource.getLocation();
        final String relPath = packageName.replace('.', '/');
        final String resPath = resource.getPath().replace("%20", " ");
        final String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        }
        catch (IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", e);
        }
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }
            if (className != null) {
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(className);
                }
                catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
                if (clazz == null) {
                    continue;
                }
                classes.add(clazz);
            }
        }
        try {
            jarFile.close();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
        return (Collection<Class<?>>)ImmutableSet.copyOf((Collection)classes);
    }
}
