package zmplayer2.app.ui.controllers;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.model.Item;

/**
 * Created by Anton Prozorov on 06.06.14.
 */
public class LibraryController implements Observer {

    private ViewGroup viewGroup;
    private LayoutInflater inflater;

    private final Item libraryRoot = new Item("Library root", null);
    private Item currentRoot;

    public LibraryController(ViewGroup viewGroup, LayoutInflater inflater) {
        this.viewGroup = viewGroup;
        this.inflater = inflater;
        libraryRoot.addChild(new Item("Artists", libraryRoot));
        libraryRoot.addChild(new Item("Albums", libraryRoot));
        libraryRoot.addChild(new Item("Tracks", libraryRoot));

        currentRoot = libraryRoot;

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
