package zmplayer2.app.ui.controllers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.Observable;
import java.util.Observer;

import zmplayer2.app.net.DownloadTask;
import zmplayer2.app.player.MusicPlayer;

/**
 * Created by zlobne on 09.06.14.
 */
public class PlayerController {
    public void playPause() {
        MusicPlayer.instance().playPause();
    }

    public void nextSong() {
        MusicPlayer.instance().nextSong(MusicPlayer.instance().isPlaying());
    }

    public void prevSong() {
        MusicPlayer.instance().prevSong(MusicPlayer.instance().isPlaying());
    }

    public void seekTo(int progress) {
        MusicPlayer.instance().setProgress((int) (MusicPlayer.instance().getDuration() * (float) progress / 100f));
    }

    public void downloadFile(String filename, DownloadTask.DownloadTaskListener listener) {
        MusicPlayer.instance().downloadCoverArt(filename, listener);
    }
}
