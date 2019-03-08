package com.gzdb.basepos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.basepos.greendao.DaoMaster;
import com.gzdb.basepos.greendao.DaoSession;
import com.gzdb.sunmi.util.OpenDrawers;
import com.gzdb.supermarket.been.UserBean;
import com.gzdb.supermarket.util.SharedPreferencesUtil;
import com.gzdb.supermarket.util.Utils;
import com.gzdb.yct.socket.sdk.OkSocket;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;
import com.hss01248.dialog.MyActyManager;
import com.hss01248.dialog.StyledDialog;
import com.lzy.okgo.OkGo;
import com.tencent.bugly.Bugly;
import com.util.baidu.BaiduLocationManager;

import java.util.logging.Level;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zhumg on 7/14.
 */
public class App extends MultiDexApplication {

    public static SerialPortOperaion mSerialPortOperaion = null;
    public static App instance;
    public double latitude;
    public double lontitude;
    private SharedPreferences courierLocation;
    public   UserBean currentUser;
    private static DaoSession daoSession;

    private static Handler sUIHandler = new Handler(Looper.getMainLooper());

    public static Handler getUIHandler() {
        return sUIHandler;
    }

    private int length;
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志；.
        JPushInterface.init(this); // 初始化 JPush
        if (BuildConfig.APP_DEBUG) {
            LogUtils.getLogConfig().configAllowLog(true);
            Bugly.init(getApplicationContext(), "190bca6585", BuildConfig.APP_DEBUG);
        } else {
            MCrashHandler.getInstance().init(this); //全局捕捉异常.崩溃后提示直接退出应用，一定要放在Bugly前面
            //腾讯Bugly
            Bugly.init(getApplicationContext(), "9909e6805f", BuildConfig.APP_DEBUG);
            LogUtils.getLogConfig().configAllowLog(false);
        }

        OkGo.getInstance().init(this);

        BaiduLocationManager.init(this);
        //全局对话框
        StyledDialog.init(this);
        registCallback();

        Utils.init(this);
        OpenDrawers.init(this);

        setupDatabase();

        SharedPreferencesUtil.init(this);

        try {
            mSerialPortOperaion = new SerialPortOperaion(null, new SerialParam(2400, "/dev/ttyS3", 0));
            mSerialPortOperaion.StartSerial();
        } catch (Throwable e) {
            //e.printStackTrace();
        }
        //百度地图
        courierLocation = this.getSharedPreferences("location", MODE_PRIVATE);
        //配置数据库
        debugMode(BuildConfig.APP_DEBUG);

        OkSocket.initialize(this, true);
    }


    public static App getInstance() {
        return instance;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void debugMode(boolean isDebug) {
        if (isDebug) {
//            L.e("debug");
            Bugly.setAppChannel(this, "测试");
            Bugly.setIsDevelopmentDevice(this, isDebug);
          //   OkGo.getInstance().debug("okgo", Level.INFO, true);
            /**
             * 这是非必须的
             * 如果你什么也不做,输出Log时默认按如下配置初始化
             */
            LogUtils.getLogConfig()
                    .configAllowLog(true);
        } else {
            LogUtils.getLogConfig()
                    .configAllowLog(false);
        }
    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "g2.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }


    public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    //全局对话框
    private void registCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                MyActyManager.getInstance().setCurrentActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


}
