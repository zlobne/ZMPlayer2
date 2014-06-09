package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class Wiki {
    @SerializedName(JsonKeys.PUBLISHED)
    private String published;
    @SerializedName(JsonKeys.SUMMARY)
    private String summary;

    public String getPublished() {
        return published;
    }

    public String getSummary() {
        return summary;
    }
}
