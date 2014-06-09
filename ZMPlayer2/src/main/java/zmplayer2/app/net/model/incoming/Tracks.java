package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Tracks {
    @SerializedName(JsonKeys.TRACK)
    private ArrayList<Track> track;

    public ArrayList<Track> getTrack() {
        return track;
    }
}
