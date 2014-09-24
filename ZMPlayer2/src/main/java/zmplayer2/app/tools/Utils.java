package zmplayer2.app.tools;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by Anton Prozorov on 09.06.14.
 */
public class Utils {
    public static String prettyTime(long time) {
        int sec  = (int)(time/ 1000) % 60 ;
        int min  = (int)(time/ (1000) / 60);
        return (String.format("%02d", min) + ":" + String.format("%02d", sec));
    }

    public static String properName(String s) {
        if (s != null) {
            return s.replace("/", "_").replace("?", "_").replace(":", "_").replace(";", "_").replace("*", "_").replace(">", "_").replace("<", "_").replace("|", "_");
        } else {
            return "null";
        }
    }

    public static Bitmap getBlurredBitmap(Bitmap src){
        //TODO: make normal blur
        int width = src.getWidth();
        int height = src.getHeight();

        float blurValue = 10f;

        BlurMaskFilter blurMaskFilter;
        Paint paintBlur = new Paint();

        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dest);

        blurMaskFilter = new BlurMaskFilter(blurValue, BlurMaskFilter.Blur.INNER);
        paintBlur.setMaskFilter(blurMaskFilter);
        canvas.drawBitmap(src, 0, 0, paintBlur);

        return dest;
    }

    public static GradientDrawable getDominantGradient(Bitmap bitmap) {
        if (null == bitmap) {
            int colors[] = {Color.TRANSPARENT};
            return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        }

        int redBucketLeft = 0;
        int greenBucketLeft = 0;
        int blueBucketLeft = 0;
        int alphaBucketLeft = 0;

        int redBucketRight = 0;
        int greenBucketRight = 0;
        int blueBucketRight = 0;
        int alphaBucketRight = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth()/2 * bitmap.getHeight();
        int[] pixelsLeft = new int[pixelCount];
        bitmap.getPixels(pixelsLeft, 0, bitmap.getWidth()/2, 0, 0, bitmap.getWidth()/2, bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth()/2; x < w; x++)
            {
                int color = pixelsLeft[x + y * w]; // x + y * width
                redBucketLeft += (color >> 16) & 0xFF; // Color.red
                greenBucketLeft += (color >> 8) & 0xFF; // Color.greed
                blueBucketLeft += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucketLeft += (color >>> 24); // Color.alpha
            }
        }

        int[] pixelsRight = new int[pixelCount];
        bitmap.getPixels(pixelsRight, 0, bitmap.getWidth()/2, bitmap.getWidth()/2, 0, bitmap.getWidth()/2, bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth()/2; x < w; x++)
            {
                int color = pixelsRight[x + y * w]; // x + y * width
                redBucketRight += (color >> 16) & 0xFF; // Color.red
                greenBucketRight += (color >> 8) & 0xFF; // Color.greed
                blueBucketRight += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucketRight += (color >>> 24); // Color.alpha
            }
        }

        int colorLeft = Color.argb(
                (hasAlpha) ? (alphaBucketLeft / pixelCount) : 255,
                redBucketLeft / pixelCount,
                greenBucketLeft / pixelCount,
                blueBucketLeft / pixelCount);

        int colorRight = Color.argb(
                (hasAlpha) ? (alphaBucketRight / pixelCount) : 255,
                redBucketRight / pixelCount,
                greenBucketRight / pixelCount,
                blueBucketRight / pixelCount);

        int colors[] = {colorLeft, colorRight};
        return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++)
        {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++)
            {
                int color = pixels[x + y * w]; // x + y * width
                redBucket += (color >> 16) & 0xFF; // Color.red
                greenBucket += (color >> 8) & 0xFF; // Color.greed
                blueBucket += (color & 0xFF); // Color.blue
                if (hasAlpha) alphaBucket += (color >>> 24); // Color.alpha
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }
}
