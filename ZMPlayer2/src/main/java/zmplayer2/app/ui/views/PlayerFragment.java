package zmplayer2.app.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.Core;
import zmplayer2.app.PreferenceManager;
import zmplayer2.app.R;
import zmplayer2.app.async.GetBitmapTask;
import zmplayer2.app.model.Album;
import zmplayer2.app.net.DownloadTask;
import zmplayer2.app.tools.Utils;
import zmplayer2.app.ui.TickerTextView;
import zmplayer2.app.ui.controllers.PlayerController;

/**
 * Created by Anton Prozorov on 06.06.14.
 */
public class PlayerFragment extends Fragment implements Observer, DownloadTask.DownloadTaskListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PlayerController playerController;

    private ViewGroup artContainer;
    private TickerTextView titleBar1;
    private TextView titleBar2;
    private ImageButton prevBtn;
    private ImageButton playPauseBtn;
    private ImageButton nextBtn;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView fullTime;

    private View rootView;

    private boolean interrupted = false;
    private boolean paused = false;

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
        Core.instance().getPlayerService().getMusicPlayer().addObserver(this);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void updateImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Core.instance().getPlayerService().getMusicPlayer().getSong() != null) {
                    Bitmap albumCover = null;
                    if (Core.instance().getPlayerService().getMusicPlayer().getSong().getParent() != null
                            && Core.instance().getPlayerService().getMusicPlayer().getSong().getParent().getParent() != null) {
                        try {
                            albumCover = ((Album) Core.instance().getPlayerService().getMusicPlayer().getSong().getParent()).getAlbumCover();
                        } catch (Exception e) {
                            Log.d("ololo", e.getLocalizedMessage());
                        }
                    }
                    if (albumCover == null) {
                        File song = new File(Core.instance().getPlayerService().getMusicPlayer().getSong().getSource());

                        String coverName = "/cover";
//                if (Core.instance().getPlayerService().getMusicPlayer().getSong().getAlbumName() != null
//                        && Core.instance().getPlayerService().getMusicPlayer().getSong().getAlbumName().isEmpty()) {
//                    coverName += Utils.properName(Core.instance().getPlayerService().getMusicPlayer().getSong().getAlbumName());
//                }

                        coverName += ".jpg";

                        String filename = song.getParent() + coverName;
                        Log.d("ololo", "cover " + filename);
                        final File coverArt = new File(filename);

                        if (coverArt.exists()) {
                            albumCover = GetBitmapTask.decodeSampledBitmapFromFile(coverArt.getAbsolutePath(), 400);
                        } else if (PreferenceManager.instance().isDownloadingArt()) {
                            playerController.downloadFile(filename, PlayerFragment.this);
                        }
                    }

                    if (albumCover == null) {
                        albumCover = Bitmap.createScaledBitmap(Core.instance().getDefaultArt(), 400, 400, true);
                    }

                    final Bitmap coverToShow = albumCover;


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addImage(coverToShow);
                        }
                    });
                }
            }
        }).start();
    }

    private void updateView(boolean updateImage) {

        if (updateImage) {
            updateImage();
        }

        if (Core.instance().getPlayerService().getMusicPlayer().isPlaying()) {
            playPauseBtn.setImageResource(R.drawable.btn_pause);
        } else {
            playPauseBtn.setImageResource(R.drawable.btn_play);
        }

        if (Core.instance().getPlayerService().getMusicPlayer().getSong() != null) {
            titleBar1.setText(Core.instance().getPlayerService().getMusicPlayer().getSong().getName());
            titleBar2.setText(Core.instance().getPlayerService().getMusicPlayer().getSong().getArtistName() + " - "
                    + Core.instance().getPlayerService().getMusicPlayer().getSong().getAlbumName());
            currentTime.setText(Utils.prettyTime(Core.instance().getPlayerService().getMusicPlayer().getCurrentPosition()));
            fullTime.setText(Utils.prettyTime(Core.instance().getPlayerService().getMusicPlayer().getDuration()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!interrupted) {
                        if (Core.instance().getPlayerService().getMusicPlayer().isPlaying() && !paused) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fullTime.setText(Utils.prettyTime(Core.instance().getPlayerService().getMusicPlayer().getDuration()));
                                    currentTime.setText(Utils.prettyTime(Core.instance().getPlayerService().getMusicPlayer().getCurrentPosition()));
                                    seekBar.setProgress((int) ((float) Core.instance().getPlayerService().getMusicPlayer().getCurrentPosition() / (float) Core.instance().getPlayerService().getMusicPlayer().getDuration() * 100.0));
                                }
                            });
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private void initViews(View view) {
        artContainer = (ViewGroup) view.findViewById(R.id.artContainer);
        titleBar1 = (TickerTextView) view.findViewById(R.id.titleBar1);
        titleBar1.setSelected(true);
        titleBar2 = (TextView) view.findViewById(R.id.titleBar2);
        prevBtn = (ImageButton) view.findViewById(R.id.prevBtn);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.prevSong();
            }
        });
        playPauseBtn = (ImageButton) view.findViewById(R.id.playPauseBtn);

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerController.playPause();
                if (Core.instance().getPlayerService().getMusicPlayer().isPlaying()) {
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
        updateView(true);
    }

    private void addImage(Bitmap bitmap) {

        new AQuery(getActivity(), rootView).id(R.id.albumArt).animate(android.R.anim.fade_in).image(Utils.getBlurredBitmap(bitmap));

        if (artContainer.getBackground() == null) {
            artContainer.setBackgroundColor(Color.TRANSPARENT);
        }

        GradientDrawable background = Utils.getDominantGradient(bitmap);

        Drawable[] drawables = {artContainer.getBackground(), background};

        TransitionDrawable transitionDrawable = new TransitionDrawable(drawables);

        artContainer.setBackgroundDrawable(transitionDrawable);

        transitionDrawable.startTransition(1000);

    }

    @Override
    public void update(Observable observable, Object o) {
        boolean updateImage = false;
        if (o == null) {
            updateImage = true;
        }
        updateView(updateImage);
    }

    @Override
    public void onPause() {
        super.onPause();
        Core.instance().getPlayerService().getMusicPlayer().deleteObserver(this);
        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Core.instance().getPlayerService().getMusicPlayer().addObserver(this);
        initViews(rootView);
        updateView(false);
        paused = false;
        interrupted = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interrupted = true;
    }

    @Override
    public void onDownloadTaskComplete(DownloadTask.DownloadTaskState state) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    updateImage();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
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
