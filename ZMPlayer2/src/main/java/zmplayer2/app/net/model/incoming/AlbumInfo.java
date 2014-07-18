package zmplayer2.app.net.model.incoming;

import com.google.gson.annotations.SerializedName;

import zmplayer2.app.net.model.JsonKeys;

/**
 * Created by Anton Prozorov on 25.03.14.
 */
public class AlbumInfo {

    @SerializedName(JsonKeys.ALBUM)
    private AlbumObject albumObject;

    public AlbumObject getAlbumObject() {
        return albumObject;
    }

}
