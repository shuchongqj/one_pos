package com.gzdb.supermarket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gzdb.printer.PrintUtils;
import com.gzdb.sunmi.Sunmi;

public class MyService  extends Service {
    private static final String TAG = "ServiceDemo" ;
    public static final String ACTION = "com.gzdb.supermarket.MyService";

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "ServiceDemo onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "ServiceDemo onCreate");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.v(TAG, "ServiceDemo onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "ServiceDemo onStartCommand");

        PrintUtils.initPrintParam(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }
}