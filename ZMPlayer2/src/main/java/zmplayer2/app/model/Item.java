package zmplayer2.app.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.R;

/**
 * Created by Anton Prozorov on 06.06.14.
 */
public class Item extends Observable{
    private String name;
    private Item parent;
    private ArrayList<Item> childs = new ArrayList<Item>();

    public Item(String name, Item parent) {
        this.name = name;
        this.parent = parent;
    }

    public void addChild(Item child) {
        childs.add(child);
    }

    public ArrayList<Item> getChilds() {
        return childs;
    }

    public Item getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void draw(ViewGroup viewGroup, LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.library_item, null);
        Button tv = (Button) view.findViewById(R.id.tv);
        tv.setText(getName());
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChanged();
                notifyObservers();
            }
        });
        viewGroup.addView(view);
    }
}
