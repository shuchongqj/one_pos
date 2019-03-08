package com.one.pos.service.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.anlib.http.Http;
import com.anlib.http.HttpCallback;
import com.anlib.http.HttpLife;
import com.anlib.util.LogUtils;
import com.one.pos.TestActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 前台服务
 *
 * @author zhumg
 */
public class ForegroundService extends Service implements Runnable, HttpLife {

    private PowerManager.WakeLock wakeLock = null;
    private boolean runing;
    //10秒刷新1次
    private int refreshTime = 3000;
    private Thread httpThread;

    @Override
    public void onCreate() {
        super.onCreate();
        this.acquireWakeLock();
        LogUtils.info("ForegroundService onCreate");
        this.runing = true;
        this.httpThread = new Thread(this);
        this.httpThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void acquireWakeLock() {
        if (this.wakeLock == null) {
            PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
            this.wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
            if (this.wakeLock != null) {
                Log.i("-wakeLock-", "wakelock acquireWakeLock");
                this.wakeLock.acquire();
            }
        }
    }

    private void releaseWakeLock() {
        if (this.wakeLock != null && this.wakeLock.isHeld()) {
            Log.i("-releaseWakeLock-", "wakelock releaseWakeLock");
            this.wakeLock.release();
            this.wakeLock = null;
        }
    }

    @Override
    public void onDestroy() {
        //Log.d("GpPrintService", "-Service onDestory-");
        //this.unregisterReceiver(this.PortOperateBroadcastReceiver);
        runing = false;
        this.releaseWakeLock();
        super.onDestroy();
    }

    public static final String merchantGroupOrderSize = "https://p.food4u.cc/market/saas/order/openapi/merchantGroupOrderSize.do";

    private boolean httpHandler = false;

    @Override
    public void run() {
        while (runing) {
            if (!httpHandler) {
                httpHandler = true;
                LogUtils.info("http 处理数据 post");
                Map<String, String> params = new HashMap<>();
                params.put("passportId", "100000");
                params.put("marketId", "100000");
                Http.post(this, params, merchantGroupOrderSize, new HttpCallback<OrderSize>() {
                    @Override
                    public void onSuccess(OrderSize data) {
                        //广播出去
                        LogUtils.info("http 处理数据 成功");
                        httpHandler = false;
                    }
                });
            }
            try {
                Thread.sleep(refreshTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isActive() {
        return runing;
    }
}
