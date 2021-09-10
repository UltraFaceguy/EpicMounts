package land.face.mounts.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

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
