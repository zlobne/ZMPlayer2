package zmplayer2.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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

    protected Core(Context context) {
        this.context = context;
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

    public boolean startPlayerService() {
        Intent intent = new Intent(context, PlayerService.class);
        context.startService(intent);
        context.bindService(intent, sConn, 0);
        return serviceBound;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }
}
