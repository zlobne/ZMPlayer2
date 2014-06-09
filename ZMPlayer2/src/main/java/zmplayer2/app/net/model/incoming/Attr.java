package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Attr {
    @SerializedName(JsonKeys.RANK)
    private String rank;

    public String getRank() {
        return rank;
    }
}
