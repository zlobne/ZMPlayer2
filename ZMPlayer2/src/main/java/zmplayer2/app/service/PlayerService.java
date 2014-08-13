package zmplayer2.app.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.Core;
import zmplayer2.app.PreferenceManager;
import zmplayer2.app.R;
import zmplayer2.app.model.Album;
import zmplayer2.app.model.Library;
import zmplayer2.app.model.Song;
import zmplayer2.app.player.MusicPlayer;
import zmplayer2.app.receivers.RemoteControl;
import zmplayer2.app.tools.Utils;
import zmplayer2.app.ui.views.MainActivity;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class PlayerService extends Service implements Observer {

    private PlayerBinder binder = new PlayerBinder();

    private MusicPlayer musicPlayer;

    private RemoteControl receiver;

    @Override
    public void onCreate() {
        Log.d("trololo", "service started");
        Core.instance(this);
        musicPlayer = new MusicPlayer();
        musicPlayer.addObserver(this);

        String lastSong = PreferenceManager.instance().getLastSong();
        Log.d("Search last song", lastSong);
        Song song = Library.instance(this).findSongByPath(lastSong);

        if (song != null) {
            Log.d("Search result", "" + song.toString());
            musicPlayer.setSong(song);
        } else {
            musicPlayer.setSong(Library.instance(this).getFirstSong());
        }

        initReceivers();
        sendNotif();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public MusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    @Override
    public void update(Observable observable, Object data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendNotif();
            }
        }).start();
    }

    public class PlayerBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }

    }

    private void initReceivers() {
        receiver = new RemoteControl();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction("zmp.playPause");
        intentFilter.addAction("zmp.nextSong");
        registerReceiver(receiver, intentFilter);
    }

    private RemoteViews getNotification() {
        RemoteViews notificationView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_view);
        notificationView.setOnClickPendingIntent(R.id.playPauseBtn, PendingIntent.getBroadcast(this, 0, new Intent("zmp.playPause"), PendingIntent.FLAG_UPDATE_CURRENT));
        notificationView.setOnClickPendingIntent(R.id.nextBtn, PendingIntent.getBroadcast(this, 0, new Intent("zmp.nextSong"), PendingIntent.FLAG_UPDATE_CURRENT));
        notificationView.setCharSequence(R.id.artistName, "setText", getMusicPlayer().getSong().getArtistName());
        notificationView.setCharSequence(R.id.songName, "setText", getMusicPlayer().getSong().getName());

        if (getMusicPlayer().isPlaying()) {
            notificationView.setImageViewBitmap(R.id.playPauseBtn, BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_pause));
        } else {
            notificationView.setImageViewBitmap(R.id.playPauseBtn, BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_play));
        }

        Bitmap albumCover = getAlbumArt();
        if (albumCover == null) {
            albumCover = Bitmap.createScaledBitmap(Core.instance().getDefaultArt(), 100, 100, true);
        }
        notificationView.setImageViewBitmap(R.id.cover, albumCover);
        return notificationView;
    }

    private void sendNotif() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_notif);
        builder.setAutoCancel(false);
        builder.setTicker("ZMPlayer2 started");
        builder.setWhen(System.currentTimeMillis());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setPriority(Notification.PRIORITY_MAX);

        if (getMusicPlayer().getSong() == null) {
            builder.setContentIntent(pIntent);
            builder.setContentTitle("ZMPlayer2");
            builder.setContentText("Service started");
        } else {
            builder.setContent(getNotification());
        }

        Notification notif = builder.build();

        startForeground(666, notif);
    }

    private Bitmap getAlbumArt() {
        Bitmap albumCover = null;
        if (getMusicPlayer().getSong() != null) {
            if (getMusicPlayer().getSong().getParent() != null
                    && getMusicPlayer().getSong().getParent().getParent() != null) {
                try {
                    albumCover = ((Album) getMusicPlayer().getSong().getParent()).getAlbumCover();
                } catch (Exception e) {
                    Log.d("ololo", e.getLocalizedMessage());
                }
            }
            if (albumCover == null) {
                File song = new File(getMusicPlayer().getSong().getSource());

                String coverName = "/cover";
                if (getMusicPlayer().getSong().getAlbumName() != null
                        && getMusicPlayer().getSong().getAlbumName().isEmpty()) {
                    coverName += Utils.properName(getMusicPlayer().getSong().getAlbumName());
                }

                coverName += ".jpg";

                String filename = song.getParent() + coverName;
                Log.d("ololo", "cover " + filename);
                final File coverArt = new File(filename);

                if (coverArt.exists()) {
                    albumCover = BitmapFactory.decodeFile(coverArt.getAbsolutePath());
                }
            }

            if (albumCover == null) {
                albumCover = Bitmap.createScaledBitmap(Core.instance().getDefaultArt(), 100, 100, true);
            }
        }
        return albumCover;
    }
}
