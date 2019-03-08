package com.gzdb.supermarket.util;

import android.os.Build;
import android.text.InputType;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by Zxy on 2016/12/5.
 * 禁止Edittext弹出软件盘，光标依然正常显示。
 */

public class NotShowKey {

    public static void disableShowSoftInput(EditText et) {
        if (Build.VERSION.SDK_INT <= 10) {
            et.setInputType(InputType.TYPE_NULL);
        } else {



            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }
}
