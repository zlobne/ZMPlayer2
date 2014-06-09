package zmplayer2.app.ui.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.R;
import zmplayer2.app.model.Item;
import zmplayer2.app.model.Library;

/**
 * Created by Anton Prozorov on 06.06.14.
 */

public class LibraryController implements Observer {

    private Context context;

    private ViewGroup viewGroup;
    private LayoutInflater inflater;

    private Item currentRoot;

    public LibraryController(ViewGroup viewGroup, LayoutInflater inflater, Context context) {
        this.viewGroup = viewGroup;
        this.inflater = inflater;
        this.context = context;

        currentRoot = Library.instance(context);

        subscribe(currentRoot);
    }

    public void draw() {
        for (Item item : currentRoot.getChilds()) {
            item.draw(viewGroup, inflater);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        currentRoot = (Item) observable;
        subscribe(currentRoot);
        viewGroup.removeAllViews();
        draw();

    }

    private void subscribe(Item currentItem) {
        for (Item item : currentItem.getChilds()) {
            item.addObserver(this);
        }
    }
}
