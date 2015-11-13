package com.jsbn.mgr.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by chenjianjun on 14-11-20.
 * <p/>
 * Beyond their own, let the future
 */
public class PreferenceUtils {

    /**
     *
     */
    public static enum DataType {
        STRING,
        INT,
        LONG,
        FLOAT,
        BOOLEAN
    }

    /**
     * 存储程序信息
     */
    public static final String PREFERENCE_NAME = "bafei";// 存储文件名

    public static final String FIRST_LOGIN = "first_login"; //第一次登陆标记

    /**
     * JSESSIONID
     * token-id
     * ticket
     * 登录成功以后保存服务器返回的，后续请求需要带上
     */
    public static final String PREFERENCE_SESSION_ID = "session-id";
    public static final String PREFERENCE_TOKEN = "token";
    public static final String PREFERENCE_TICKET = "ticket";

    public static final String PREFERENCE_B_TOKEN = "B_TOKEN";

    public static <T> void saveValue(Context context, String key, DataType type, T value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        SharedPreferences.Editor edit = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE).edit();

        switch (type) {
            case STRING: {
                edit.putString(key, (String) value);
                break;
            }
            case INT: {
                edit.putInt(key, (Integer) value);
                break;
            }
            case LONG: {
                edit.putLong(key, (Long) value);
                break;
            }
            case FLOAT: {
                edit.putFloat(key, (Float) value);
                break;
            }
            case BOOLEAN: {
                edit.putBoolean(key, (Boolean) value);
                break;
            }
            default:
                break;
        }

        edit.commit();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Context context, String key, DataType valueType) {

        switch (valueType) {
            case STRING: {
                return (T) ((Object) context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getString(key, null));
            }
            case INT: {
                return (T) ((Object) context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getInt(key, -1));
            }
            case LONG: {
                return (T) ((Object) context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getLong(key, -1));
            }
            case FLOAT: {
                return (T) ((Object) context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getFloat(key, -1));
            }

            case BOOLEAN: {
                return (T) ((Object) context.getSharedPreferences(PREFERENCE_NAME,
                        Context.MODE_PRIVATE).getBoolean(key, true));
            }
            default:
                break;
        }

        return null;
    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查目录是否存在， 如果不存在则创建
     *
     * @param dirs
     */
    public static void checkDirsExists(String... dirs) {
        for (int i = 0; i < dirs.length; i++) {
            File dir = new File(dirs[i]);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    public static void delDirsExists(String... dirs) {
        for (int i = 0; i < dirs.length; i++) {
            File dir = new File(dirs[i]);
            if (dir.exists()) {
                dir.delete();
            }
        }
    }
}

