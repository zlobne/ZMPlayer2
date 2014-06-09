package zmplayer2.app.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import zmplayer2.app.R;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Artist extends Item {

    public Artist(String name, Item parent) {
        super(name, parent);
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
        TextView artistName = (TextView) view.findViewById(R.id.artistName);
        artistName.setText(getName());
        TextView albumCount = (TextView) view.findViewById(R.id.albumCount);
        albumCount.setText(getChilds().size() + " " + inflater.getContext().getString(R.string.album_count));
        viewGroup.addView(view);
    }
}
