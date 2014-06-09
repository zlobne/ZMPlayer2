package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Tag {
    @SerializedName(JsonKeys.NAME)
    private String name;
    @SerializedName(JsonKeys.URL)
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
