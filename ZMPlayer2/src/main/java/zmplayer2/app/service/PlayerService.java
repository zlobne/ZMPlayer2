package zmplayer2.app.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

import zmplayer2.app.R;
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

    @Override
    public void onCreate() {
        Log.d("trololo", "service started");
        musicPlayer = new MusicPlayer();

        receiver = new IncomingCallReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(receiver, intentFilter);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(false);
        builder.setTicker("ZMPlayer2 started");
        builder.setWhen(System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pIntent);

        builder.setContentTitle("ZMPlayer2");
        builder.setContentText("Service started");

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
}
