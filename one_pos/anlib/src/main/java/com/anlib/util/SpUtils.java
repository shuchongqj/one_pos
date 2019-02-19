package com.anlib.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 * @author zhumg
 * @date 2017/3/23 0023
 */

public class SpUtils {

    private SharedPreferences appShared;// 用于保存应用内部数据
    private SharedPreferences.Editor appEditor; // 编辑appShared中的数据

    public void init(Context context, String name) {
        appShared = context.getSharedPreferences(name, MODE_PRIVATE);
        appEditor = appShared.edit();
    }

    public void saveValue(String key, String value) {
        appEditor.putString(key, value);
        appEditor.commit();
    }

    public String loadValue(String key) {
        return appShared.getString(key, "");
    }

    public void saveJson(String key, String json) {
        appEditor.putString(key, json);
        appEditor.commit();
    }

    public <T> T loadJson(String key, Class<T> cls) {
        String json = loadValue(key);
        if (json.length() > 0) {
            return JsonUtils.fromJson(json, cls);
        }
        return null;
    }
}
