package com.gzdb.sunmi.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


/**
 * Created by Zxy on 2016/12/12.
 *   商米钱箱
 */

public class OpenDrawers {

    private static IWoyouService iws;

    public static void init(Context context) {
        try {
            Intent intent = new Intent();
            intent.setPackage("woyou.aidlservice.jiuiv5");
            intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
            context.startService(intent);
            context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("zhumg", "无法初始化钱箱!");
            return;
        }
    }

    private static ServiceConnection conn = new ServiceConnection() {

        //当服务断开
        @Override
        public void onServiceDisconnected(ComponentName name) {
            iws = null;
        }

        //服务连上
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iws = IWoyouService.Stub.asInterface(service);
            Log.e("xinyuan", "onServiceConnected");
        }
    };

    private static ICallback callback = new ICallback.Stub(){

        @Override
        public void onRunResult(boolean isSuccess, int code, String msg) throws RemoteException {

        }
    };

    public static void openMoneyBox() {
        if(iws!= null){
            try {
                iws.openDrawer(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
