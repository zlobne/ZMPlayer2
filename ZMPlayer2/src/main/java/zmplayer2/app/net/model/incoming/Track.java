package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Track {
    @SerializedName(JsonKeys.NAME)
    private String name;
    @SerializedName(JsonKeys.DURATION)
    private String duration;
    @SerializedName(JsonKeys.MBID)
    private String mbid;
    @SerializedName(JsonKeys.URL)
    private String url;
    @SerializedName(JsonKeys.STREAMABLE)
    private Streamable streamable;
    @SerializedName(JsonKeys.ARTIST)
    private Artist artist;
    @SerializedName(JsonKeys.ATTR)
    private Attr attr;

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public Artist getArtist() {
        return artist;
    }

    public Attr getAttr() {
        return attr;
    }
}
