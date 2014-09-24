package zmplayer2.app.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import zmplayer2.app.Core;
import zmplayer2.app.R;
import zmplayer2.app.async.GetBitmapTask;
import zmplayer2.app.tools.Utils;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Album extends Item {

    private String albumArt;
    private String artistName;

    public Album(String name, Item parent) {
        super(name, parent);
    }

    public Album(String name, String albumArt, Item parent) {
        super(name, parent);
        this.albumArt = albumArt;
    }

    public Bitmap getAlbumCover(int size) {
        GetBitmapTask task = new GetBitmapTask();
        try {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, albumArt, ((Song) getChilds().get(0)).getSource(), String.valueOf(size), Utils.properName(getName())).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getAlbumCover() {
        return getAlbumCover(400);
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
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
        final ImageView cover = (ImageView) view.findViewById(R.id.cover);

        cover.setImageBitmap(Bitmap.createScaledBitmap(Core.instance().getDefaultArt(), 100, 100, true));

        new GetBitmapTask(new GetBitmapTask.GetBitmapListener() {
            @Override
            public void onCompleted(Bitmap bitmap) {
                if (bitmap != null) {
                    cover.setImageBitmap(bitmap);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, albumArt, ((Song) getChilds().get(0)).getSource(), "100", Utils.properName(getName()));

        viewGroup.addView(view);
    }
}
