package zmplayer2.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by ololoev on 09.07.14.
 */
public class PreferenceManager {

    private static PreferenceManager self;

    private final static String name = "ZMP2";

    private SharedPreferences sharedPreferences;

    protected PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static PreferenceManager instance() {
        return self;
    }

    public static PreferenceManager instance(Context context) {
        if (self == null) {
            self = new PreferenceManager(context);
        }
        return self;
    }

    public void putInt(String key, int value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public void putBoolean(String key, boolean value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public void putFloat(String key, float value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    public void putLong(String key, long value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public void putString(String key, String value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void putStringSet(String key, Set<String> value) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(key, value);
            editor.commit();
        }
    }

    public int getInt(String key, int defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, defValue);
        }
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, defValue);
        }
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getFloat(key, defValue);
        }
        return defValue;
    }

    public long getLong(String key, long defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, defValue);
        }
        return defValue;
    }

    public String getString(String key, String defValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, defValue);
        }
        return defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        if (sharedPreferences != null) {
            return sharedPreferences.getStringSet(key, defValues);
        }
        return defValues;
    }
}
