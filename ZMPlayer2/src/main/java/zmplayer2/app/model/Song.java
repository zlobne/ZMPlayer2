package zmplayer2.app.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import zmplayer2.app.R;
import zmplayer2.app.tools.Utils;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Song extends Item {

    private long duration;
    private String source;

    public Song(String name, Item parent) {
        super(name, parent);
    }

    public Song(String name, String source, long duration, Item parent) {
        super(name, parent);
        this.duration = duration;
        this.source = source;
    }

    public long getDuration() {
        return duration;
    }

    public String getSource() {
        return source;
    }

    @Override
    public void draw(ViewGroup viewGroup, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.artist_item, null);
        ViewGroup tv = (ViewGroup) view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChanged();
                notifyObservers();
            }
        });
        TextView songName = (TextView) view.findViewById(R.id.artistName);
        songName.setText(getName());
        TextView duration = (TextView) view.findViewById(R.id.albumCount);
        duration.setText(Utils.prettyTime(getDuration()));
        viewGroup.addView(view);
    }
}
