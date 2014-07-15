package zmplayer2.app.ui.views;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.R;
import zmplayer2.app.model.Item;
import zmplayer2.app.ui.controllers.LibraryController;

/**
 * Created by Anton Prozorov on 06.06.14.
 */
public class LibraryFragment extends Fragment implements Observer {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private LayoutInflater inflater;

    private ViewGroup viewGroup;
    private Button backBtn;

    private LibraryController libraryController;

    public static LibraryFragment newInstance(int sectionNumber) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LibraryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        initViews(rootView);
        libraryController = new LibraryController(viewGroup, inflater, getActivity().getApplicationContext());
        libraryController.addObserver(this);
        libraryController.draw();
        return rootView;
    }

    private void initViews(View rootView) {
        viewGroup = (ViewGroup) rootView.findViewById(R.id.vg);
        backBtn = (Button) rootView.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                libraryController.backPress();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (libraryController != null) {
            libraryController.deleteObserver(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (libraryController != null) {
            libraryController.addObserver(this);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o != null) {
            ((MainActivity) getActivity()).onNavigationDrawerItemSelected(2);
        }
    }
}
