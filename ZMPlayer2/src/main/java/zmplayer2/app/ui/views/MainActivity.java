package zmplayer2.app.ui.views;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import zmplayer2.app.R;
import zmplayer2.app.model.Library;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MusicPlayer.instance(this);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0: {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, LibraryFragment.newInstance(position + 1))
                        .commit();
                break;
            }
//            case 1: {
//                fragmentManager.beginTransaction()
//                        .replace(R.id.container, FilesFragment.newInstance(position + 1))
//                        .commit();
//                break;
//            }
            case 1: {
                if (Library.instance().haveSongs()) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, PlayerFragment.newInstance(position + 1))
                            .commit();
                } else {
                    Toast.makeText(this, "ololo", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_library);
                break;
//            case 2:
//                mTitle = getString(R.string.title_files);
//                break;
            case 2:
                mTitle = getString(R.string.title_player);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            }

            case R.id.action_rescan: {
                Library.instance(this).rescan();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
