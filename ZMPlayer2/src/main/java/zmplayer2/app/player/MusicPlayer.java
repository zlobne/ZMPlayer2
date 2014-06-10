package zmplayer2.app.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Observable;

import zmplayer2.app.model.Item;
import zmplayer2.app.model.Song;
import zmplayer2.app.net.model.JsonParser;
import zmplayer2.app.net.model.incoming.AlbumInfo;
import zmplayer2.app.net.DownloadTask;

/**
 * Created by zlobne on 09.06.14.
 */
public class MusicPlayer extends Observable {

    private static MusicPlayer self;

    private MediaPlayer mediaPlayer;

    private Song song;
    private Item album;
    private Item artist;

    protected MusicPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                nextSong(true);
            }
        });
    }

    public static MusicPlayer instance(Context context) {
        if (self == null) {
            self = new MusicPlayer();
        }
        return self;
    }

    public static MusicPlayer instance() {
        return self;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void setProgress(int msec) {
        mediaPlayer.seekTo(msec);
    }

    public long getDuration() {
        return song.getDuration();
    }

    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
        this.album = song.getParent();
        if (album != null) {
            this.artist = album.getParent();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.getSource());
            mediaPlayer.prepare();
            setChanged();
            notifyObservers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        mediaPlayer.start();
    }

    public void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void nextSong(boolean forcePlay) {
        if (album.getChilds().indexOf(song) < album.getChilds().size() - 1) {
            setSong((Song) album.getChilds().get(album.getChilds().indexOf(song) + 1));
        } else {
            if (artist != null) {
                nextAlbum(forcePlay);
            }
            return;
        }
        if (forcePlay) {
            play();
        }
    }

    private void nextAlbum(boolean forcePlay) {
        if (artist.getChilds().indexOf(album) < artist.getChilds().size() - 1) {
            setSong((Song) artist.getChilds().get(artist.getChilds().indexOf(album) + 1).getChilds().get(0));
        } else {
            if (artist.getParent() != null) {
                nextArtist(forcePlay);
            }
            return;
        }
        if (forcePlay) {
            play();
        }
    }

    private void nextArtist(boolean forcePlay) {
        if (artist.getParent().getChilds().indexOf(artist) < artist.getParent().getChilds().size() - 1) {
            setSong((Song) artist.getParent().getChilds().get(artist.getParent().getChilds().indexOf(artist) + 1).getChilds().get(0).getChilds().get(0));
        } else {
            setSong((Song) artist.getParent().getChilds().get(0).getChilds().get(0).getChilds().get(0));
        }
        if (forcePlay) {
            play();
        }
    }

    public void prevSong(boolean forcePlay) {
        if (album.getChilds().indexOf(song) > 0) {
            setSong((Song) album.getChilds().get(album.getChilds().indexOf(song) - 1));
        } else {
            if (artist != null) {
                prevAlbum(forcePlay);
            }
            return;
        }
        if (forcePlay) {
            play();
        }
    }

    private void prevAlbum(boolean forcePlay) {
        if (artist.getChilds().indexOf(album) > 0) {
            setSong((Song) artist.getChilds().get(artist.getChilds().indexOf(album) - 1) //prev album
                    .getChilds().get(artist.getChilds().get(artist.getChilds().indexOf(album) - 1).getChilds().size() - 1)); //last song
        } else {
            if (artist.getParent() != null) {
                prevArtist(forcePlay);
            }
            return;
        }
        if (forcePlay) {
            play();
        }
    }

    private void prevArtist(boolean forcePlay) {
        if (artist.getParent().getChilds().indexOf(artist) > 0) {
            int prevArtist = artist.getParent().getChilds().indexOf(artist) - 1;
            int prevAlbum = artist.getParent().getChilds().get(prevArtist).getChilds().size() - 1;
            int prevTrack = artist.getParent().getChilds().get(prevArtist).getChilds().get(prevAlbum).getChilds().size() - 1;
            setSong((Song) artist.getParent().getChilds().get(prevArtist)
                    .getChilds().get(prevAlbum)
                    .getChilds().get(prevTrack));
        } else {
            int lastArtist = artist.getParent().getChilds().size() - 1;
            int lastAlbum = artist.getParent().getChilds().get(lastArtist).getChilds().size() - 1;
            int lastSong = artist.getParent().getChilds().get(lastArtist).getChilds().get(lastAlbum).getChilds().size() - 1;
            setSong((Song) artist.getParent().getChilds().get(lastArtist).getChilds().get(lastAlbum).getChilds().get(lastSong));
        }
        if (forcePlay) {
            play();
        }
    }

    public void downloadCoverArt(final String filename, final DownloadTask.DownloadTaskListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpGet = new HttpGet("http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=89a9f43ab1afa3b4dffb4cafd62bb1b5&artist="
                        + getSong().getArtistName().replace(" ", "%20").replace("<", "").replace(">", "") + "&album="
                        + getSong().getAlbumName().replace(" ", "%20").replace("<", "").replace(">", "") + "&format=json");
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                    Log.d("trololoq", result);
                    AlbumInfo info = JsonParser.parseAlbumInfo(result);
                    new DownloadTask(listener).execute(info.getAlbum().getImages().get(3).getText(), filename);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}