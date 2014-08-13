package zmplayer2.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import zmplayer2.app.Core;
import zmplayer2.app.PreferenceManager;
import zmplayer2.app.player.MusicPlayer;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class RemoteControl extends BroadcastReceiver {

    private final static String TAG = "ZMPRemoteControl";

    public RemoteControl() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String action = intent.getAction();
//        if (action.equals("android.media.AUDIO_BECOMING_NOISY")) {
//            Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.HEADPHONES);
//        }
        if ("android.intent.action.PHONE_STATE".equals(action)) {
            tmgr.listen(new ZMPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }

        if (action.equals(Intent.ACTION_HEADSET_PLUG) && PreferenceManager.instance().isHeadphonesPauseEnabled()) {
            boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);
            if (connectedHeadphones) {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().play(MusicPlayer.HEADPHONES);
                    Log.d(TAG, "headset plugged");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.HEADPHONES);
                    Log.d(TAG, "headset unplugged");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        if ("zmp.playPause".equals(action)) {
            Core.instance().getPlayerService().getMusicPlayer().playPause();
        }

        if ("zmp.nextSong".equals(action)) {
            Core.instance().getPlayerService().getMusicPlayer().nextSong(Core.instance().getPlayerService().getMusicPlayer().isPlaying());
        }

    }

    private class ZMPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {

            if (!PreferenceManager.instance().isCallPauseEnabled()) {
                return;
            }

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.CALL);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.CALL);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            if (state == TelephonyManager.CALL_STATE_IDLE) {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().play(MusicPlayer.CALL);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
