package zmplayer2.app.net.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import zmplayer2.app.net.model.incoming.AlbumInfo;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class JsonParser {

    private static Gson gson = new GsonBuilder().create();

    public static AlbumInfo parseAlbumInfo(String s) {
        return gson.fromJson(s, AlbumInfo.class);
    }

    public static String toJson(Object jsonElement) {
        return gson.toJson(jsonElement);
    }
}
