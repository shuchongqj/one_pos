package com.core.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Created by zhumg on 7/12.
 */
public class ToastUtil {


    public static void showToast(Context context, String s) {
        showNormal(context, s);
    }

    public static void showToast(Context context, int resId) {
        showToast(context.getApplicationContext(), context.getString(resId));
    }

    public static void showSuccess(Context context, String msg) {
        Toasty.success(context.getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void showEorr(Context context, String msg) {
        Toasty.error(context.getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void showWrning(Context context, String msg) {
        Toasty.warning(context.getApplicationContext(), msg, Toast.LENGTH_SHORT, true).show();
    }

    public static void showNormal(Context context, String msg) {
        Toasty.normal(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static Toast custom(@NonNull Context context, View view, int duration) {
        Toast currentToast = new Toast(context);
        currentToast.setView(view);
        currentToast.setDuration(duration);
        return currentToast;
    }

    public static void showToastBig(Context context, String info) {
        Toast toast = Toast.makeText(context.getApplicationContext(), info, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextSize(24);
        toast.setText(info);
        toast.show();
    }
//    public static Toast customBigSize(@NonNull Context context, String msg ,int duration){
//        Toast currentToast = new Toast(context);
//        View view=LayoutInflater.from(context).inflate(,null);
//        currentToast.setView(view);
//        ((SuperTextView)view.findViewById(R.id.text)).setText(msg);
//        currentToast.setDuration(duration);
//        return currentToast;
//    }
}
