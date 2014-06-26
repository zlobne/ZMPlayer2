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

        Notification notif = new Notification(R.drawable.ic_launcher, "ZPlayer2 started",
                System.currentTimeMillis());

        // 3-я часть
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 2-я часть
        notif.setLatestEventInfo(this, "ZMPlayer2", "Service started", pIntent);

        // ставим флаг, чтобы уведомление пропало после нажатия
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
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
