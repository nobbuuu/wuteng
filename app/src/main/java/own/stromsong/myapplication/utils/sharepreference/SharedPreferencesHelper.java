package own.stromsong.myapplication.utils.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 当前类注释:当前为SharedPerferences进行封装基本的方法,SharedPerferences已经封装成单例模式
 * 可以通过SharedPreferences sp=SharedPreferencesHelper.getInstances(Application.getInstance())进行获取当前对象
 * sp.putStringValue(key,value)进行使用
 */
public class SharedPreferencesHelper {
    private static final String SHARED_PATH = "dlf_shared";
    private static SharedPreferencesHelper instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesHelper getInstance(Context context) {
        if (instance == null && context != null) {
            instance = new SharedPreferencesHelper(context);
        }
        return instance;
    }

    private SharedPreferencesHelper(Context context) {
        sp = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public long getLongValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public String getStringValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getString(key, "");
        }
        return "";
    }

    public int getIntValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public int getIntValueByDefault(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public boolean getBooleanValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getBoolean(key, false);
        }
        return true;
    }

    public float getFloatValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public void putStringValue(String key, String value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public void putIntValue(String key, int value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public void putBooleanValue(String key, boolean value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public void putLongValue(String key, long value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public void putFloatValue(String key, Float value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }
}
