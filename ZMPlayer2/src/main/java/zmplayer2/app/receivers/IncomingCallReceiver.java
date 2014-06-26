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
public class IncomingCallReceiver extends BroadcastReceiver {

    private Context context;

    private TelephonyManager tmgr;

    public IncomingCallReceiver(Context context) {
        this.context = context;
        tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
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

    }

    private class ZMPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.CALL);
            }

            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                Core.instance().getPlayerService().getMusicPlayer().pause(MusicPlayer.CALL);
            }

            if (state == TelephonyManager.CALL_STATE_IDLE) {
                Core.instance().getPlayerService().getMusicPlayer().play(MusicPlayer.CALL);
            }
        }

    }
}
