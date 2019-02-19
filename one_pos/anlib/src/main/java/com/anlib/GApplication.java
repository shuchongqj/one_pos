package com.anlib;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.anlib.http.Http;


public class GApplication extends Application {

    private static Context context;
    private static DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        displayMetrics = getResources().getDisplayMetrics();

        //初始化http
        Http.init(this);

    }

    public static Context getContext() {
        if (context == null) {
            throw new RuntimeException("context is null");
        }
        return context;
    }

    public static DisplayMetrics getDisplayMetrics() {
        if(displayMetrics == null) {
            throw new RuntimeException("displayMetrics is null");
        }
        return displayMetrics;
    }
}
