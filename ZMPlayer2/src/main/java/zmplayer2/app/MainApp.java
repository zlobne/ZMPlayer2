package zmplayer2.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import zmplayer2.app.model.Library;

/**
 * Created by Anton Prozorov on 26.06.14.
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        Crashlytics.start(this);
        Core.instance(this).startPlayerService();
        Library.instance(this);
    }
}
