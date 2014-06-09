package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class AlbumArt {
    @SerializedName(JsonKeys.TEXT)
    private String text;
    @SerializedName(JsonKeys.SIZE)
    private String size;

    public String getText() {
        return text;
    }

    public String getSize() {
        return size;
    }
}
