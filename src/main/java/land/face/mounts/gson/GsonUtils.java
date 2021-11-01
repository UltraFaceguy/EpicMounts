package land.face.mounts.gson;

import com.tealcube.minecraft.bukkit.shade.google.gson.Gson;
import com.tealcube.minecraft.bukkit.shade.google.gson.GsonBuilder;
import com.tealcube.minecraft.bukkit.shade.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.lang.reflect.Type;

public class GsonUtils {

    private static final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

    public static void registerTypeAdapter(RuntimeTypeAdapterFactory<?> adapter) {
        gsonBuilder.registerTypeAdapterFactory(adapter);
    }
    public static void registerType(Type type, Object obj) {
        gsonBuilder.registerTypeAdapter(type, obj);
    }
    public static Gson getGson() {
        return gsonBuilder.create();
    }
}
