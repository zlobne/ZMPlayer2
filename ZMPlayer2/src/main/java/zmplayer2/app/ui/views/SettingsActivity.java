package zmplayer2.app.ui.views;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by prozorov on 13.08.14.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
