package zmplayer2.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import zmplayer2.app.Core;
import zmplayer2.app.player.MusicPlayer;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class RemoteControl extends BroadcastReceiver {

    public RemoteControl() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String action = intent.getAction();
        if ("android.intent.action.PHONE_STATE".equals(action)) {
            tmgr.listen(new ZMPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        }

        if ("android.intent.action.HEADSET_PLUG".equals(action)) {
            boolean connectedHeadphones = (intent.getIntExtra("state", 0) == 1);
            if (connectedHeadphones) {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().play(MusicPlayer.HEADPHONES);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.HEADPHONES);
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
