package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Tags {
    @SerializedName(JsonKeys.TAG)
    private ArrayList<Tag> tags;

    public ArrayList<Tag> getTags() {
        return tags;
    }
}
