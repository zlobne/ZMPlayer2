package zmplayer2.app.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;

import zmplayer2.app.Core;
import zmplayer2.app.R;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Library extends Item {

    private static Library self;
    private Context context;

    public Library(String name, Item parent, Context context) {
        super(name, parent);
        this.context = context;
    }

    public void createLibrary(Context context) {
        this.addChild(new Item(context.getString(R.string.artists), this));
        this.addChild(new Item(context.getString(R.string.albums), this));
        this.addChild(new Item(context.getString(R.string.tracks), this));
        createArtists(context);

//        for (Item item : getChilds().get(0).getChilds()) {
//            for (Item album : item.getChilds()) {
//                getChilds().get(1).addChild(album);
//            }
//        }
//
//        for (Item item : getChilds().get(1).getChilds()) {
//            for (Item song : item.getChilds()) {
//                getChilds().get(2).addChild(song);
//            }
//        }
        createAlbums(context, null);
        createTracks(context, null);
    }

    public static Library instance(Context context) {
        if (self == null) {
            self = new Library("Library root", null, context);
            self.createLibrary(context);
        }
        return self;
    }

    public static Library instance() {
        return self;
    }

    public boolean haveSongs() {
        return (!getChilds().get(2).getChilds().isEmpty());
    }

    public Item getArtists() {
        return getChilds().get(0);
    }

    public Item getAlbums() {
        return getChilds().get(1);
    }

    public Item getTracks() {
        return getChilds().get(2);
    }

    public Song findSongByPath(String path) {
        for (Item item : getChilds().get(2).getChilds()) {
            if (((Song) item).getSource().equals(path)) {
                return (Song) item;
            }
        }
        return null;
    }

    public Song getFirstSong() {
        if (!getChilds().get(2).getChilds().isEmpty()) {
            return (Song) getChilds().get(2).getChilds().get(0);
        }
        return null;
    }

    public void rescan() {
        self = new Library("Library root", null, context);
        self.createLibrary(context);
    }

    private void createArtists(Context context) {
        ContentResolver contentResolver;
        Uri uri;
        Cursor cursor;
        // Library

        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        cursor = contentResolver.query(uri, null, null, null, MediaStore.Audio.Artists.ARTIST);
        if (cursor == null) {
            Log.d("ololo", "cursor == null");
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            Log.d("ololo", "no media");
            // no media on the device
        } else {
            int titleColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            do {
                this.getChilds().get(0).addChild(new Artist(cursor.getString(titleColumn), this.getChilds().get(0)));
                // ...process entry...
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        for (Item item : this.getChilds().get(0).getChilds()) {
            createAlbums(context, item);
        }

    }

    private void createAlbums(Context context, Item artist) {
        ContentResolver contentResolver;
        Uri uri;
        Cursor cursor;
        String where = null;
        if (artist != null) {
            where = MediaStore.Audio.Albums.ARTIST + " = '" + artist.getName().replace("'", "''") + "'";
        }
        Log.d("ololo", "albums");
        // Library

        contentResolver = context.getContentResolver();
        uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        cursor = contentResolver.query(uri, null, where, null, MediaStore.Audio.Albums.ARTIST + ", " + MediaStore.Audio.Albums.ALBUM);

        if (cursor == null) {
            Log.d("ololo", "cursor == null");
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            Log.d("ololo", "no media");
            // no media on the device
        } else {
            Log.d("ololo", "ololo");
            int titleColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int artColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int artistColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            do {
                if (artist != null) {
                    Album album = new Album(cursor.getString(titleColumn), cursor.getString(artColumn), artist);
                    album.setArtistName(cursor.getString(artistColumn));
                    artist.addChild(album);
                } else {
                    Album album = new Album(cursor.getString(titleColumn), cursor.getString(artColumn), this.getChilds().get(1));
                    album.setArtistName(cursor.getString(artistColumn));
                    this.getChilds().get(1).addChild(album);
                }
                // ...process entry...
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        if (artist != null) {
            for (Item item : artist.getChilds()) {
                createTracks(context, (Album) item);
//                ((Album) item).findAlbumCover();
            }
        } else {
            for (Item item : this.getChilds().get(1).getChilds()) {
                createTracks(context, (Album) item);
//                ((Album) item).findAlbumCover();
            }
        }
    }

    private void createTracks(Context context, Album album) {
        ContentResolver contentResolver;
        Uri uri;
        Cursor cursor;
        String where = null;
        if (album != null) {
            where = MediaStore.Audio.Media.ALBUM + " = '" + album.getName().replace("'", "''")
                    + "' and " + MediaStore.Audio.Media.ARTIST + " = '" + album.getArtistName().replace("'","''") + "'";
        }
        Log.d("ololo", "tracks");
        // Library

        contentResolver = context.getContentResolver();
        uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sort = null;
        if (album != null) {
            sort = MediaStore.Audio.Media.TRACK;
        }
        cursor = contentResolver.query(uri, null, where, null, sort);
        if (cursor == null) {
            Log.d("ololo", "cursor == null");
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            Log.d("ololo", "no media");
            // no media on the device
        } else {
            int titleColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE);
            int durationColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION);
            int sourceColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA);
            int artistColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM);
            do {
                if (album != null) {
                    album.addChild(new Song(cursor.getString(titleColumn), cursor.getString(albumColumn), cursor.getString(artistColumn), cursor.getString(sourceColumn), cursor.getLong(durationColumn), album));
                } else {
                    this.getChilds().get(2).addChild(new Song(cursor.getString(titleColumn), cursor.getString(albumColumn), cursor.getString(artistColumn), cursor.getString(sourceColumn), cursor.getLong(durationColumn), this.getChilds().get(2)));
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}
