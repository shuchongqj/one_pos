package com.gzdb.sunmi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zxy on 2016/11/15.
 * 缓存工具类
 */

public class SharedPreferencesSunmiUtil {

    public static final String PREFERENCE_FILE_NAME = "obj_cache"; // 缓存文件名

    public  static void put(Context context, String key, long value){
        SharedPreferences spf = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(Context context, String key){
        try{
            SharedPreferences spf = context.getSharedPreferences(
                    PREFERENCE_FILE_NAME, 0);
            return  spf.getLong(key, -1);
        }catch (Exception e){
            return -1;
        }
    }
    /**
     * 清除某个key对应的缓存
     */
    public static void clearByKey(String key, Context context) {
        SharedPreferences spf = context.getSharedPreferences(
                PREFERENCE_FILE_NAME, 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString(key, "");
        editor.apply();
    }

}
