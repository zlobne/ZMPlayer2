package zmplayer2.app.ui.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.R;
import zmplayer2.app.model.Item;
import zmplayer2.app.model.Library;
import zmplayer2.app.ui.views.MainActivity;

/**
 * Created by Anton Prozorov on 06.06.14.
 */

public class LibraryController extends Observable implements Observer {

    private ViewGroup viewGroup;
    private LayoutInflater inflater;

    private Item currentRoot;

    public LibraryController(ViewGroup viewGroup, LayoutInflater inflater, Context context) {
        this.viewGroup = viewGroup;
        this.inflater = inflater;

        currentRoot = Library.instance(context);

        subscribe(currentRoot);

    }

    public void draw() {
        viewGroup.removeAllViews();
        for (Item item : currentRoot.getChilds()) {
            item.draw(viewGroup, inflater);
        }

//        Button button = (Button) ((View)viewGroup.getParent()).findViewById(R.id.backBtn);
//
//        if (currentRoot == Library.instance(context)) {
//            button.setVisibility(View.GONE);
//        } else {
//            button.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data == null) {
            currentRoot = (Item) observable;
            subscribe(currentRoot);
            draw();
        } else {
            setChanged();
            notifyObservers(data);
        }
    }

    private void subscribe(Item currentItem) {
        for (Item item : currentItem.getChilds()) {
            item.addObserver(this);
        }
    }

    public void backPress() {
        if (currentRoot.getParent() != null) {
            currentRoot = currentRoot.getParent();
            draw();
        }
    }
}
