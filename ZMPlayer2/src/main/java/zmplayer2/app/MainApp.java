package zmplayer2.app;

import android.app.Application;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;

import zmplayer2.app.model.Library;
import zmplayer2.app.service.PlayerService;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        Crashlytics.start(this);
        Intent intent = new Intent(this, PlayerService.class);
        Library.instance(this);
        startService(intent);
    }
}
