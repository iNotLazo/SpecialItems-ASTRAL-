package me.iNotLazo.SpecialItems.utils;

import org.bukkit.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.*;
import org.bukkit.configuration.serialization.*;
import org.bukkit.craftbukkit.v1_8_R3.inventory.*;
import java.io.*;
import com.google.gson.stream.*;
import com.google.gson.reflect.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import org.bukkit.craftbukkit.v1_8_R3.util.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.potion.*;
import com.google.gson.*;
import com.google.gson.annotations.*;
import java.lang.annotation.*;

public class GsonFactory
{
    private static Gson g;
    private static final String CLASS_KEY = "SERIAL-ADAPTER-CLASS-KEY";
    private static Gson prettyGson;
    private static Gson compactGson;
    
    static {
        GsonFactory.g = new Gson();
    }
    
    public static Gson getPrettyGson() {
        if (GsonFactory.prettyGson == null) {
            GsonFactory.prettyGson = new GsonBuilder().addSerializationExclusionStrategy(new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter((Class)ItemStack.class, (Object)new ItemStackGsonAdapter()).registerTypeAdapter((Type)PotionEffect.class, (Object)new PotionEffectGsonAdapter()).registerTypeAdapter((Type)Location.class, (Object)new LocationGsonAdapter()).registerTypeAdapter((Type)Date.class, (Object)new DateGsonAdapter()).setPrettyPrinting().disableHtmlEscaping().create();
        }
        return GsonFactory.prettyGson;
    }
    
    public static Gson getCompactGson() {
        if (GsonFactory.compactGson == null) {
            GsonFactory.compactGson = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter((Class)ItemStack.class, (Object)new ItemStackGsonAdapter()).registerTypeAdapter((Type)PotionEffect.class, (Object)new PotionEffectGsonAdapter()).registerTypeAdapter((Type)Location.class, (Object)new LocationGsonAdapter()).registerTypeAdapter((Type)Date.class, (Object)new DateGsonAdapter()).disableHtmlEscaping().create();
        }
        return GsonFactory.compactGson;
    }
    
    public static Gson getNewGson(final boolean prettyPrinting) {
        final GsonBuilder builder = new GsonBuilder().addSerializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).addDeserializationExclusionStrategy((ExclusionStrategy)new ExposeExlusion()).registerTypeHierarchyAdapter((Class)ItemStack.class, (Object)new NewItemStackAdapter()).disableHtmlEscaping();
        if (prettyPrinting) {
            builder.setPrettyPrinting();
        }
        return builder.create();
    }
    
    private static Map<String, Object> recursiveSerialization(final ConfigurationSerializable o) {
        final Map<String, Object> originalMap = (Map<String, Object>)o.serialize();
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final Map.Entry<String, Object> entry : originalMap.entrySet()) {
            final Object o2 = entry.getValue();
            if (o2 instanceof ConfigurationSerializable) {
                final ConfigurationSerializable serializable = (ConfigurationSerializable)o2;
                final Map<String, Object> newMap = recursiveSerialization(serializable);
                newMap.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias((Class)serializable.getClass()));
                map.put(entry.getKey(), newMap);
            }
        }
        map.put("SERIAL-ADAPTER-CLASS-KEY", ConfigurationSerialization.getAlias((Class)o.getClass()));
        return map;
    }
    
    private static Map<String, Object> recursiveDoubleToInteger(final Map<String, Object> originalMap) {
        final Map<String, Object> map = new HashMap<String, Object>();
        for (final Map.Entry<String, Object> entry : originalMap.entrySet()) {
            final Object o = entry.getValue();
            if (o instanceof Double) {
                final Double d = (Double)o;
                final Integer i = (int)(Object)d;
                map.put(entry.getKey(), i);
            }
            else if (o instanceof Map) {
                final Map<String, Object> subMap = (Map<String, Object>)o;
                map.put(entry.getKey(), recursiveDoubleToInteger(subMap));
            }
            else {
                map.put(entry.getKey(), o);
            }
        }
        return map;
    }
    
    private static String nbtToString(final NBTTagCompound base) {
        return base.toString().replace(",}", "}").replace(",]", "]").replaceAll("[0-9]+\\:", "");
    }
    
    private static net.minecraft.server.v1_8_R3.ItemStack removeSlot(final ItemStack item) {
        if (item == null) {
            return null;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
        if (nmsi == null) {
            return null;
        }
        final NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        return nmsi;
    }
    
    private static ItemStack removeSlotNBT(final ItemStack item) {
        if (item == null) {
            return null;
        }
        final net.minecraft.server.v1_8_R3.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
        if (nmsi == null) {
            return null;
        }
        final NBTTagCompound nbtt = nmsi.getTag();
        if (nbtt != null) {
            nbtt.remove("Slot");
            nmsi.setTag(nbtt);
        }
        return CraftItemStack.asBukkitCopy(nmsi);
    }
    
    private static class DateGsonAdapter extends TypeAdapter<Date>
    {
        public void write(final JsonWriter jsonWriter, final Date date) throws IOException {
            if (date == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(date.getTime());
        }
        
        public Date read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return new Date(jsonReader.nextLong());
        }
    }
    
    private static class ItemStackGsonAdapter extends TypeAdapter<ItemStack>
    {
        private static Type seriType;
        
        static {
            ItemStackGsonAdapter.seriType = new TypeToken<Map<String, Object>>() {}.getType();
        }
        
        public void write(final JsonWriter jsonWriter, final ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(removeSlotNBT(itemStack)));
        }
        
        public ItemStack read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }
        
        private String getRaw(final ItemStack item) {
            if (item == null) {
                return null;
            }
            final Map<String, Object> serial = (Map<String, Object>)item.serialize();
            if (serial.containsKey("damage") && item.getType().getMaxDurability() > 0) {
                serial.put("max-damage", item.getType().getMaxDurability());
            }
            if (!item.getItemMeta().hasDisplayName()) {
                serial.put("displayname", ItemNames.lookup(item));
            }
            serial.put("id", item.getTypeId());
            if (serial.get("meta") != null) {
                final ItemMeta itemMeta = item.getItemMeta();
                final Map<String, Object> originalMeta = (Map<String, Object>)itemMeta.serialize();
                final Map<String, Object> meta = new HashMap<String, Object>();
                for (final Map.Entry<String, Object> entry : originalMeta.entrySet()) {
                    meta.put(entry.getKey(), entry.getValue());
                }
                for (final Map.Entry<String, Object> entry2 : meta.entrySet()) {
                    final Object o = entry2.getValue();
                    if (o instanceof ConfigurationSerializable) {
                        final ConfigurationSerializable serializable = (ConfigurationSerializable)o;
                        final Map<String, Object> serialized = recursiveSerialization(serializable);
                        meta.put(entry2.getKey(), serialized);
                    }
                }
                serial.put("meta", meta);
            }
            return GsonFactory.g.toJson((Object)serial);
        }
        
        private ItemStack fromRaw(final String raw) {
            final Map<String, Object> keys = (Map<String, Object>)GsonFactory.g.fromJson(raw, ItemStackGsonAdapter.seriType);
            if (keys.get("amount") != null) {
                final Double d = (Double) keys.get("amount");
                final Integer i = (int)(Object)d;
                keys.put("amount", i);
            }
            ItemStack item;
            try {
                item = ItemStack.deserialize((Map)keys);
            }
            catch (Exception e) {
                return null;
            }
            if (item == null) {
                return null;
            }
            if (keys.containsKey("meta")) {
                Map<String, Object> itemmeta = (Map<String, Object>) keys.get("meta");
                itemmeta.remove("max-damage");
                if (itemmeta.containsKey("displayname") && itemmeta.get("displayname").equals(ItemNames.lookup(item))) {
                    itemmeta.remove("displayname");
                }
                itemmeta.remove("id");
                itemmeta = recursiveDoubleToInteger(itemmeta);
                final ItemMeta meta = (ItemMeta)ConfigurationSerialization.deserializeObject((Map)itemmeta, ConfigurationSerialization.getClassByAlias("ItemMeta"));
                item.setItemMeta(meta);
            }
            return item;
        }
    }
    
    private static class LocationGsonAdapter extends TypeAdapter<Location>
    {
        private static Type seriType;
        private static String UUID;
        private static String X;
        private static String Y;
        private static String Z;
        private static String YAW;
        private static String PITCH;
        
        static {
            LocationGsonAdapter.seriType = new TypeToken<Map<String, Object>>() {}.getType();
            LocationGsonAdapter.UUID = "uuid";
            LocationGsonAdapter.X = "x";
            LocationGsonAdapter.Y = "y";
            LocationGsonAdapter.Z = "z";
            LocationGsonAdapter.YAW = "yaw";
            LocationGsonAdapter.PITCH = "pitch";
        }
        
        public void write(final JsonWriter jsonWriter, final Location location) throws IOException {
            if (location == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(location));
        }
        
        public Location read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }
        
        private String getRaw(final Location location) {
            final Map<String, Object> serial = new HashMap<String, Object>();
            serial.put(LocationGsonAdapter.UUID, location.getWorld().getUID().toString());
            serial.put(LocationGsonAdapter.X, Double.toString(location.getX()));
            serial.put(LocationGsonAdapter.Y, Double.toString(location.getY()));
            serial.put(LocationGsonAdapter.Z, Double.toString(location.getZ()));
            serial.put(LocationGsonAdapter.YAW, Float.toString(location.getYaw()));
            serial.put(LocationGsonAdapter.PITCH, Float.toString(location.getPitch()));
            return GsonFactory.g.toJson((Object)serial);
        }
        
        private Location fromRaw(final String raw) {
            final Map<String, Object> keys = (Map<String, Object>)GsonFactory.g.fromJson(raw, LocationGsonAdapter.seriType);
            final World w = Bukkit.getWorld(java.util.UUID.fromString((String) keys.get(LocationGsonAdapter.UUID)));
            return new Location(w, Double.parseDouble((String) keys.get(LocationGsonAdapter.X)), Double.parseDouble((String) keys.get(LocationGsonAdapter.Y)), Double.parseDouble((String) keys.get(LocationGsonAdapter.Z)), Float.parseFloat((String) keys.get(LocationGsonAdapter.YAW)), Float.parseFloat((String) keys.get(LocationGsonAdapter.PITCH)));
        }
    }
    
    private static class NewItemStackAdapter extends TypeAdapter<ItemStack>
    {
        public void write(final JsonWriter jsonWriter, final ItemStack itemStack) throws IOException {
            if (itemStack == null) {
                jsonWriter.nullValue();
                return;
            }
            final net.minecraft.server.v1_8_R3.ItemStack item = removeSlot(itemStack);
            if (item == null) {
                jsonWriter.nullValue();
                return;
            }
            try {
                jsonWriter.beginObject();
                jsonWriter.name("type");
                jsonWriter.value(itemStack.getType().toString());
                jsonWriter.name("amount");
                jsonWriter.value((long)itemStack.getAmount());
                jsonWriter.name("data");
                jsonWriter.value((long)itemStack.getDurability());
                jsonWriter.name("tag");
                if (item != null && item.getTag() != null) {
                    jsonWriter.value(nbtToString(item.getTag()));
                }
                else {
                    jsonWriter.value("");
                }
                jsonWriter.endObject();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        public ItemStack read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                return null;
            }
            jsonReader.beginObject();
            jsonReader.nextName();
            final Material type = Material.getMaterial(jsonReader.nextString());
            jsonReader.nextName();
            final int amount = jsonReader.nextInt();
            jsonReader.nextName();
            final int data = jsonReader.nextInt();
            final net.minecraft.server.v1_8_R3.ItemStack item = new net.minecraft.server.v1_8_R3.ItemStack(CraftMagicNumbers.getItem(type), amount, data);
            jsonReader.nextName();
            final String next = jsonReader.nextString();
            if (next.startsWith("{")) {
                NBTTagCompound compound = null;
                try {
                    compound = MojangsonParser.parse(ChatColor.translateAlternateColorCodes('&', next));
                }
                catch (MojangsonParseException e) {
                    e.printStackTrace();
                }
                item.setTag(compound);
            }
            jsonReader.endObject();
            return CraftItemStack.asBukkitCopy(item);
        }
    }
    
    private static class PotionEffectGsonAdapter extends TypeAdapter<PotionEffect>
    {
        private static Type seriType;
        private static String TYPE;
        private static String DURATION;
        private static String AMPLIFIER;
        private static String AMBIENT;
        
        static {
            PotionEffectGsonAdapter.seriType = new TypeToken<Map<String, Object>>() {}.getType();
            PotionEffectGsonAdapter.TYPE = "effect";
            PotionEffectGsonAdapter.DURATION = "duration";
            PotionEffectGsonAdapter.AMPLIFIER = "amplifier";
            PotionEffectGsonAdapter.AMBIENT = "ambient";
        }
        
        public void write(final JsonWriter jsonWriter, final PotionEffect potionEffect) throws IOException {
            if (potionEffect == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.getRaw(potionEffect));
        }
        
        public PotionEffect read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return this.fromRaw(jsonReader.nextString());
        }
        
        private String getRaw(final PotionEffect potion) {
            final Map<String, Object> serial = (Map<String, Object>)potion.serialize();
            return GsonFactory.g.toJson((Object)serial);
        }
        
        private PotionEffect fromRaw(final String raw) {
            final Map<String, Object> keys = (Map<String, Object>)GsonFactory.g.fromJson(raw, PotionEffectGsonAdapter.seriType);
            return new PotionEffect(PotionEffectType.getById((int)(Object)keys.get(PotionEffectGsonAdapter.TYPE)), (int)(Object)keys.get(PotionEffectGsonAdapter.DURATION), (int)(Object)keys.get(PotionEffectGsonAdapter.AMPLIFIER), (boolean)keys.get(PotionEffectGsonAdapter.AMBIENT));
        }
    }
    
    private static class ExposeExlusion implements ExclusionStrategy
    {
        public boolean shouldSkipField(final FieldAttributes fieldAttributes) {
            final Ignore ignore = (Ignore)fieldAttributes.getAnnotation((Class)Ignore.class);
            if (ignore != null) {
                return true;
            }
            final Expose expose = (Expose)fieldAttributes.getAnnotation((Class)Expose.class);
            return expose != null && (!expose.serialize() || !expose.deserialize());
        }
        
        public boolean shouldSkipClass(final Class<?> aClass) {
            return false;
        }
    }
    
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    public @interface Ignore {
    }
}
