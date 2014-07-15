package zmplayer2.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import zmplayer2.app.service.PlayerService;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class Core {
    private static Core self;

    private Context context;

    private ServiceConnection sConn;
    private PlayerService playerService;
    private boolean serviceBound;

    private Bitmap defaultArt;

    protected Core(Context context) {
        this.context = context;
        PreferenceManager.instance(context);
        sConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playerService = ((PlayerService.PlayerBinder) service).getService();
                serviceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                serviceBound = false;
            }
        };
        context.bindService(new Intent(context, PlayerService.class), sConn, 0);
        defaultArt = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_art);
    }

    public static Core instance(Context context) {
        if (self == null) {
            self = new Core(context);
        }
        return self;
    }

    public static Core instance() {
        return self;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    public Bitmap getDefaultArt() {
        return defaultArt;
    }
}
