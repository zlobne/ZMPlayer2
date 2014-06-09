package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Album {
//    @SerializedName(JsonKeys.NAME)
//    private String name;
//    @SerializedName(JsonKeys.ARTIST)
//    private String artist;
//    @SerializedName(JsonKeys.ID)
//    private String id;
//    @SerializedName(JsonKeys.MBID)
//    private String mbid;
//    @SerializedName(JsonKeys.URL)
//    private String url;
//    @SerializedName(JsonKeys.RELEASE_DATE)
//    private String releasedate;
    @SerializedName(JsonKeys.IMAGE)
    private ArrayList<AlbumArt> images;
//    @SerializedName(JsonKeys.LISTENERS)
//    private String listeners;
//    @SerializedName(JsonKeys.PLAYCOUNT)
//    private String playcount;
//    @SerializedName(JsonKeys.TRACKS)
//    private Tracks tracks;
//    @SerializedName(JsonKeys.TOPTAGS)
//    private String tags;
//    @SerializedName(JsonKeys.WIKI)
//    private Wiki wiki;

//    public String getName() {
//        return name;
//    }
//
//    public String getArtist() {
//        return artist;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getMbid() {
//        return mbid;
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public String getReleasedate() {
//        return releasedate;
//    }

    public ArrayList<AlbumArt> getImages() {
        return images;
    }

//    public String getListeners() {
//        return listeners;
//    }
//
//    public String getPlaycount() {
//        return playcount;
//    }
//
//    public Tracks getTracks() {
//        return tracks;
//    }

//    public String getTags() {
//        return tags;
//    }
//
//    public Wiki getWiki() {
//        return wiki;
//    }
}
