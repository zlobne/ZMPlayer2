package zmplayer2.app.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;

import zmplayer2.app.model.Song;

/**
 * Created by ololoev on 15.07.14.
 */
public class GetBitmapTask extends AsyncTask<String, Void, Bitmap>{

    private GetBitmapListener listener;

    public GetBitmapTask() {
        super();
    }

    public GetBitmapTask(GetBitmapListener listener) {
        super();
        this.listener = listener;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (listener != null) {
            listener.onCompleted(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String albumArt = params[0];

        String fileName = "/cover";
//        if (params.length >= 4 && params[3] != null && !params[3].isEmpty()) {
//            fileName += params[3];
//        }
        fileName += ".jpg";

        if (albumArt == null || albumArt.isEmpty()) {
            if (params.length >= 2 && params[1] != null && ! params[1].isEmpty()) {
                albumArt = new File(params[1]).getParent() + fileName;
            }
        }

        int size = 100;

        if (params.length >= 3 && params[2] != null && !params[2].isEmpty()) {
            size = Integer.valueOf(params[2]);
        }

        Bitmap albumCover = BitmapFactory.decodeFile(albumArt);
        if (albumCover != null) {
            albumCover = Bitmap.createScaledBitmap(albumCover, size, size, true);
        }

//        if (listener != null) {
//            listener.onCompleted(albumCover);
//        }

        return albumCover;
    }

    public interface GetBitmapListener {
        public void onCompleted(Bitmap bitmap);
    }
}
