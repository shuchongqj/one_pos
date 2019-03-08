package com.core.http;

import android.util.Log;

import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.supermarket.util.Utils;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhumg on 7/19.
 */
public abstract class HttpCallback<T> {

    protected abstract Object onAsyncCall(String str);

    protected abstract void onUiCall(Object obj);


    /**
     * 和服务器约定的，msg 数据异常
     */
    protected void onDataFailure(String msg) {
        if (msg != null) {
            ToastUtil.showToast(Utils.getAppContext(), msg);
        }
    }

    /**
     * 异常触发了
     */
    protected void onException(Exception ex) {
        onDataFailure(ex.getMessage());
        if (ex != null) {
            ex.printStackTrace();
        }
    }

    //根据老版本，判断json合法性
    protected final boolean checkJson(JSONObject json) {
        String success_str = json.optString("success", null);
        if (success_str != null) {
            return Boolean.parseBoolean(success_str);
        }
        //适应新的
        int code = json.optInt("code", -1);
        if (code != -1) {
            return code == 0;
        }
        return false;
    }

    //拿到泛型实现类
    protected final Type getT() {
        Type mySuperClass = this.getClass().getGenericSuperclass();
        Type type = ((ParameterizedType) mySuperClass).getActualTypeArguments()[0];
        return type;
    }

    //转最终需要的class，老协定，最终class包装在obj或者response节点中
    protected final T toT(JSONObject json) {
        String obj = json.optString("obj", null);
        if (obj == null) {
            obj = json.optString("response", null);
        }
        if (obj == null) {
            //什么也没有找到，不再进行转换，直接抛异常
            throw new IllegalStateException("无法找到节点进行对象转换!");
        }
        Type cls = getT();
        Gson gson = new Gson();
        T return_obj = gson.fromJson(obj, cls);
        return return_obj;
    }

    //转最终需要的class，老协定，最终class包装在obj或者response节点中，允许obj下带1级key
    protected final T toT(JSONObject json, String key) {
        JSONObject obj = null;
        try {
            obj = json.optJSONObject("obj");
        } catch (Exception ex) {
        }
        if (obj == null) {
            try {
                obj = json.optJSONObject("response");
            } catch (Exception ex) {
            }
        }
        if (obj == null) {
            //什么也没有找到，不再进行转换，直接抛异常
            throw new RuntimeException("无法找到 obj 节点进行对象转换!");
        }
        String key_json = obj.optString(key, null);
        if(key_json == null) {
            //什么也没有找到，不再进行转换，直接抛异常
            throw new RuntimeException("无法找到节点进行对象转换!" + key);
        }
        Type cls = getT();
        Log.e("zhumg", "cls = " + cls);
        Gson gson = new Gson();
        T return_obj = gson.fromJson(key_json, cls);
        return return_obj;
    }
}
