package zmplayer2.app.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import zmplayer2.app.Core;
import zmplayer2.app.PreferenceManager;
import zmplayer2.app.PreferencesConsts;
import zmplayer2.app.R;
import zmplayer2.app.model.Album;
import zmplayer2.app.model.Library;
import zmplayer2.app.model.Song;
import zmplayer2.app.player.MusicPlayer;
import zmplayer2.app.receivers.IncomingCallReceiver;
import zmplayer2.app.ui.views.MainActivity;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class PlayerService extends Service {

    private PlayerBinder binder = new PlayerBinder();

    private MusicPlayer musicPlayer;

    private IncomingCallReceiver receiver;

    private RemoteViews notificationView;

    @Override
    public void onCreate() {
        Log.d("trololo", "service started");
        Core.instance(this);
        musicPlayer = new MusicPlayer();

        String lastSong = PreferenceManager.instance().getString(PreferencesConsts.LAST_SONG, "");
        Log.d("Search last song", lastSong);
        Song song = Library.instance(this).findSongByPath(lastSong);

        if (song != null) {
            Log.d("Search result", "" + song.toString());
            musicPlayer.setSong(song);
        } else {
            musicPlayer.setSong(Library.instance(this).getFirstSong());
        }

        initReceivers();
        initWidgets();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_notif);
        builder.setAutoCancel(false);
        builder.setTicker("ZMPlayer2 started");
        builder.setWhen(System.currentTimeMillis());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pIntent);

        builder.setContent(notificationView);
//
//        builder.setPriority(Notification.PRIORITY_MAX);
//        builder.setContentTitle("ZMPlayer2");
//        builder.setContentText("Service started");

        Notification notif = builder.build();

        startForeground(666, notif);
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

    public class PlayerBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }

    }

    private void initReceivers() {
        receiver = new IncomingCallReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(receiver, intentFilter);
    }

    private void initWidgets() {
        notificationView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_view);
        notificationView.setImageViewBitmap(R.id.cover, BitmapFactory.decodeResource(getResources(), R.drawable.default_art));
//        notificationView.setImageViewBitmap(R.id.cover, ((Album) getMusicPlayer().getSong().getParent()).getAlbumCover());
    }
}
