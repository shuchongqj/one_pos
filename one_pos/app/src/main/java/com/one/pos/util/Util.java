package com.one.pos.util;

import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

import com.anlib.util.ToastUtils;

import java.lang.reflect.Method;

/**
 * Author: even
 * Date:   2019/3/6
 * Description:
 */
public class Util {

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        if (Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }

    public static boolean isEmpty(String str){
        if(TextUtils.isEmpty(str)){
            return true;
        }
        if(null==str){
            return true;
        }
        if("null".equals(str)){
            return true;
        }
        if("".equals(str)){
            return true;
        }
        return false;
    }



}
