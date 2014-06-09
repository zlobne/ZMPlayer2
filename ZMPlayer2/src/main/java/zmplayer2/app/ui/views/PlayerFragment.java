package zmplayer2.app.ui.views;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.R;
import zmplayer2.app.model.Album;
import zmplayer2.app.model.Song;
import zmplayer2.app.net.DownloadTask;
import zmplayer2.app.player.MusicPlayer;
import zmplayer2.app.tools.Utils;
import zmplayer2.app.ui.controllers.PlayerController;

/**
 * Created by Anton Prozorov on 06.06.14.
 */
public class PlayerFragment extends Fragment implements Observer, DownloadTask.DownloadTaskListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Song song;
    private PlayerController playerController;

    private ViewGroup albumArt;
    private TextView titleBar1;
    private TextView titleBar2;
    private ImageButton prevBtn;
    private ImageButton playPauseBtn;
    private ImageButton nextBtn;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView fullTime;

    private View rootView;

    private boolean interrupted = false;

    public static PlayerFragment newInstance(int sectionNumber) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        this.rootView = rootView;
        playerController = new PlayerController();
        MusicPlayer.instance().addObserver(this);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void initViews(View view) {
        albumArt = (ViewGroup) view.findViewById(R.id.albumArt);
        albumArt.removeAllViews();
        if (MusicPlayer.instance().getSong() != null) {
            Bitmap albumCover = null;
            if (MusicPlayer.instance().getSong().getParent() != null
                    && MusicPlayer.instance().getSong().getParent().getParent() != null) {
                try {
                    albumCover = ((Album) MusicPlayer.instance().getSong().getParent()).getAlbumCover();
                } catch (Exception e) {
                    Log.d("ololo", e.getLocalizedMessage());
                }
            }
            if (albumCover == null) {
                File song = new File(MusicPlayer.instance().getSong().getSource());
                String filename = song.getParent() + "/cover.jpg";
                Log.d("ololo", "cover " + filename);
                final File coverArt = new File(filename);

                if (coverArt.exists()) {
                    albumCover = BitmapFactory.decodeFile(coverArt.getAbsolutePath());
                } else {
                    playerController.downloadFile(filename, this);
                }
            }

            if (albumCover != null) {
                addImage(albumCover);
            }
        }
        titleBar1 = (TextView) view.findViewById(R.id.titleBar1);
        titleBar2 = (TextView) view.findViewById(R.id.titleBar2);
        prevBtn = (ImageButton) view.findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.prevSong();
            }
        });
        playPauseBtn = (ImageButton) view.findViewById(R.id.playPauseBtn);
        if (MusicPlayer.instance().isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.btn_pause);
        }
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.playPause();
                if (MusicPlayer.instance().isPlaying()) {
                    playPauseBtn.setImageResource(R.drawable.btn_pause);
                } else {
                    playPauseBtn.setImageResource(R.drawable.btn_play);
                }
            }
        });
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.nextSong();
            }
        });
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    playerController.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        currentTime = (TextView) view.findViewById(R.id.current);
        fullTime = (TextView) view.findViewById(R.id.full);

        if (MusicPlayer.instance().getSong() != null) {
            titleBar1.setText(MusicPlayer.instance().getSong().getName());
            titleBar2.setText(MusicPlayer.instance().getSong().getArtistName() + " - "
                    + MusicPlayer.instance().getSong().getAlbumName());
            currentTime.setText(Utils.prettyTime(MusicPlayer.instance().getCurrentPosition()));
            fullTime.setText(Utils.prettyTime(MusicPlayer.instance().getDuration()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!interrupted) {
                        if (MusicPlayer.instance().isPlaying()) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fullTime.setText(Utils.prettyTime(MusicPlayer.instance().getDuration()));
                                    currentTime.setText(Utils.prettyTime(MusicPlayer.instance().getCurrentPosition()));
                                    seekBar.setProgress((int) ((float) MusicPlayer.instance().getCurrentPosition() / (float) MusicPlayer.instance().getDuration() * 100.0));
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    private void addImage(Bitmap bitmap) {

        ImageView imageView = new ImageView(getActivity());
        imageView.setMinimumHeight(albumArt.getHeight());
        imageView.setMinimumWidth(albumArt.getWidth());
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(bitmap);
        albumArt.addView(imageView);
    }

    @Override
    public void update(Observable observable, Object o) {
        initViews(rootView);
    }

    @Override
    public void onPause() {
        super.onPause();
        MusicPlayer.instance().deleteObserver(this);
        interrupted = true;
    }

    @Override
    public void onDownloadTaskComplete(DownloadTask.DownloadTaskState state) {
        final String filename = state.getNameFile();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addImage(BitmapFactory.decodeFile(filename));
            }
        });
    }

    @Override
    public void onDownloadTaskError(DownloadTask.DownloadTaskState state) {

    }

    @Override
    public void onProgressUpdate(DownloadTask.DownloadTaskState state) {

    }
}
