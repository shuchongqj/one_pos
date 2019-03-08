package com.gzdb.supermarket.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Even on 2016/8/31.
 * 全局监听 当发生系统异常后把异常信息发送到服务器
 */
public class CatchHander implements Thread.UncaughtExceptionHandler {
    private String exceptionlog = "logs";
    private static final String TAG = "CrashHandler";

    //系统默认的uncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHander;

    //CrashHander实例
    private static CatchHander instance = new CatchHander();

    //程序的context对象
    private Context mContext;

    //用来存储设备的异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    //用于格式化日期，作为异常文件的名称
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    //保证只有一个catchHander实例
    private CatchHander() {
    }

    //获取catchHander实例 （单例模式）
    public static CatchHander getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的suncaughtException处理器
        mDefaultHander = new Thread().getDefaultUncaughtExceptionHandler();

        //设置该catchHander为程序默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当异常发生时会调用该函数
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handlerExcetion(ex) && mDefaultHander != null) {
            //如果用户没处理就让系统默认的异常处理器处理
            mDefaultHander.uncaughtException(thread, ex);

        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义 错误处理，收集错误信息 发送错误报告 等操作均在这里完成
     *
     * @return
     */
    private boolean handlerExcetion(Throwable ex) {

        if (ex == null) {
            return false;
        }

        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                //Toast.makeText(mContext, "很抱歉，系统出现异常，即将退出...", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        //收集Log信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param
     */
    private void collectDeviceInfo(Context context) {

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";

                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {

        StringBuffer sb = new StringBuffer();
        sb.append("crash-" + formatter.format(new Date()) + ".log---------");
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + " \n ");
        }


        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();

        String result = writer.toString();
        sb.append(result);

        //开启线程上传数据
        exceptionlog = sb.toString();
        /*Thread serviceThread = new Thread(new ServiceThread());
        serviceThread.start();*/

        //将错误日志保存到SD卡中
        try {
            // long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + "/dianba_send/log/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 自定义类：ServiceThread线程类(内部类)
     */
    /*class ServiceThread implements Runnable {
        @Override
        public void run() {
            //将错误信息发送到服务器
            String url = Contonts.ExceptionLog_Upload;
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("exception", exceptionlog);

            HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1000 * 10);
            try {
                ResponseStream responseStream = http.sendSync(HttpMethod.POST, url, params);
                System.out.println("服务器返回信息：" + responseStream.readString());
            } catch (Exception e) {
                Log.e("catchHander向服务器发送错误日志 error", e.getMessage());
            }

        }
    }*/

}
