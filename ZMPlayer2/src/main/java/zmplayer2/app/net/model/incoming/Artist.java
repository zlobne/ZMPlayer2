package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Artist {
    @SerializedName(JsonKeys.NAME)
    private String name;
    @SerializedName(JsonKeys.MBID)
    private String mbid;
    @SerializedName(JsonKeys.URL)
    private String url;

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }
}
