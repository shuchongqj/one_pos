package com.gzdb.supermarket.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Zxy on 2016/12/15.
 */

public class SharedPreferencesUtil {

    private static SharedPreferences appShared;//用于保存应用内部数据
    private static SharedPreferences.Editor appEditor;//编辑appShared中的数据

    public static void init(Context context) {
        appShared = context.getSharedPreferences("appShared", Context.MODE_PRIVATE);
        appEditor = appShared.edit();

    }

    //将数据保存到appShared中，用于应用内部数据操作（注：key尽量保持唯一，不然会覆盖已保存的数据）
    public static void saveData(String key, String value) {
        appEditor.putString(key, value);
        appEditor.commit();
    }

    //根据key 获取以保存到appShared的数据
    public static String getData(String key) {
        return appShared.getString(key, "");
    }

}
