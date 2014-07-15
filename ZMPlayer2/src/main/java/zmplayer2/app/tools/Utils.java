package zmplayer2.app.tools;

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
}
