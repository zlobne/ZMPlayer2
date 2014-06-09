package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Streamable {
    @SerializedName(JsonKeys.TEXT)
    private String text;
    @SerializedName(JsonKeys.FULLTRACK)
    private String fulltrack;

    public String getText() {
        return text;
    }

    public String getFulltrack() {
        return fulltrack;
    }
}
