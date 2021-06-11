package land.face.mounts.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final GsonBuilder gsonBuilder = new GsonBuilder()
            .setPrettyPrinting();

    public static Gson getGson() {
        return gsonBuilder.create();
    }
}
