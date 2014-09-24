package zmplayer2.app.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

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

        if (albumArt == null || albumArt.isEmpty() || !(new File(albumArt).exists())) {
            if (params.length >= 2 && params[1] != null && ! params[1].isEmpty()) {
                albumArt = new File(params[1]).getParent() + fileName;
            }
        }

        Log.d("yoba", albumArt);

        int size = 100;

        if (params.length >= 3 && params[2] != null && !params[2].isEmpty()) {
            size = Integer.valueOf(params[2]);
        }

//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        BitmapFactory.decodeFile(albumArt, options);
//
////        int imageHeight = options.outHeight;
////        int imageWidth = options.outWidth;
//
//        options.inSampleSize = calculateInSampleSize(options, size, size);
//
//        options.inJustDecodeBounds = false;
//
//        Bitmap albumCover = BitmapFactory.decodeFile(albumArt, options);

//        if (albumCover != null) {
//            albumCover = Bitmap.createScaledBitmap(albumCover, size, size, true);
//        }

//        if (listener != null) {
//            listener.onCompleted(albumCover);
//        }

        return decodeSampledBitmapFromFile(albumArt, size);
    }

    public static Bitmap decodeSampledBitmapFromFile(String albumArt, int size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(albumArt, options);

//        int imageHeight = options.outHeight;
//        int imageWidth = options.outWidth;

        options.inSampleSize = calculateInSampleSize(options, size, size);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(albumArt, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public interface GetBitmapListener {
        public void onCompleted(Bitmap bitmap);
    }
}
