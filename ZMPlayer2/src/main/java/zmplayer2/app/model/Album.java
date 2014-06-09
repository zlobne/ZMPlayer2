package zmplayer2.app.model;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import zmplayer2.app.R;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Album extends Item {

    private String albumArt;

    public Album(String name, Item parent) {
        super(name, parent);
    }

    public Album(String name, String albumArt, Item parent) {
        super(name, parent);
        this.albumArt = albumArt;
    }

    @Override
    public void draw(ViewGroup viewGroup, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.album_item, null);
        ViewGroup tv = (ViewGroup) view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChanged();
                notifyObservers();
            }
        });
        TextView artistName = (TextView) view.findViewById(R.id.albumName);
        artistName.setText(getName());
        TextView albumCount = (TextView) view.findViewById(R.id.tracksCount);
        albumCount.setText(getChilds().size() + " " + inflater.getContext().getString(R.string.tracks_count));
        ImageView cover = (ImageView) view.findViewById(R.id.cover);
        if (albumArt != null &&
                !albumArt.isEmpty()) {
            cover.setImageBitmap(BitmapFactory.decodeFile(albumArt));
        }
        viewGroup.addView(view);
    }
}
