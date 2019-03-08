package com.core.http;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.AbsCallback;
import com.lzy.okhttputils.request.BaseRequest;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by zhumg on 7/11.
 */
public final class Http {

    private static boolean init_bool = false;
    private static String sessionKey = null;

    /**
     * 初始化
     *
     * @param application
     */
    public static void init(Application application) {
        if (init_bool) return;
        init_bool = true;
        //必须调用初始化
        OkHttpUtils.init(application);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("db_http")                                              //是否打开调试
                .setWriteTimeOut(15000)
                .setReadTimeOut(15000)
                .setConnectTimeout(15000)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);                 //全局的写入超时时间
    }

    /**
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private static AbsCallback createAbsCallback(final HttpCallback callback) {
        return new AbsCallback<Object>() {
            @Override
            public Object parseNetworkResponse(Response response) throws Exception {
                String str = response.body().string();
                return callback.onAsyncCall(str);
            }

            @Override
            public void onSuccess(Object o, Call call, Response response) {
                callback.onUiCall(o);
            }

            /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
            public void onError(Call call, Response response, Exception e) {
                e.printStackTrace();
                callback.onException(e);
            }
            public void parseNetworkFail(Call call, IOException e) {
                System.err.println("网络异常！！！！！");
            }

        };
    }

    private static boolean checkNet(Context context, final HttpCallback callback) {
        if (!isNetworkConnected(context)) {
            //直接提示网络异常
            callback.onException(null);
            return false;
        }
        return true;
    }

    /**
     * http get 处理
     *
     * @param url
     */
    public static <T> void get(Context context, String url, final HttpCallback callback) {
        if (!checkNet(context, callback)) {
            return;
        }
        OkHttpUtils.get(url).tag(context).execute(createAbsCallback(callback));
    }

    /**
     * http get 处理
     *
     * @param activity
     * @param url
     * @param pres
     */
    public static void get(Activity activity, String url, Map<String, String> pres, final HttpCallback callback) {
        if (!checkNet(activity, callback)) {
            return;
        }
        BaseRequest br = OkHttpUtils.get(url).tag(activity);
        if (pres != null) {
            Iterator<String> it = pres.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = pres.get(key);
                br.params(key, String.valueOf(value));
            }
        }
        br.execute(createAbsCallback(callback));
    }

    /**
     * http post 处理
     *
     * @param activity
     * @param url
     * @param pres
     */
    public static void post(Activity activity, String url, Map<String, String> pres, HttpCallback callback) {
        if (pres == null || pres.size() < 0) {
            throw new IllegalStateException("post 的参数 pres 必须有内容!");
        }
        if (!checkNet(activity, callback)) {
            return;
        }
    }


    /**
     * http session get  处理
     * <p>
     * 原后端1.5的接口需要用到，访问时要带session的
     *
     * @param context
     * @param url
     */
    public static <T> void sessionGet(final Context context, final String url, final HttpCallback callback) {
        if (!checkNet(context, callback)) {
            return;
        }

        GetSessionTask task = new GetSessionTask();
        task.context = context;
        task.url = url;
        task.callback = callback;

        task.run();
    }

    /**
     * http session get  处理
     * <p>
     * 原后端1.5的接口需要用到，访问时要带session的
     *
     * @param context
     * @param url
     * @param pres
     */
    public static void sessionGet(final Context context, final String url, final Map<String, String> pres, final HttpCallback callback) {
        if (!checkNet(context, callback)) {
            return;
        }
        GetSessionTask task = new GetSessionTask();
        task.context = context;
        task.url = url;
        task.pres = pres;
        task.callback = callback;
        LogUtils.e(pres.toString());
        task.run();
    }

    /**
     * http session get  处理
     * <p>
     * 原后端1.5的接口需要用到，访问时要带session的
     *
     * @param context
     * @param url
     * @param pres
     */
    public static void sessionPost(final Context context, final String url, final Map<String, String> pres, final HttpCallback callback) {
        if (!checkNet(context, callback)) {
            return;
        }
        GetSessionTask task = new GetSessionTask();
        task.context = context;
        task.url = url;
        task.pres = pres;
        task.callback = callback;
        task.post = true;

        task.run();
    }

    private static class GetSessionTask extends AbsCallback {

        //请求类型
        boolean post = false;
        Context context;
        String url;
        Map<String, String> pres;
        HttpCallback callback;
        //控制每个请求，如果没有sessionkey，只访问拿1次
        boolean sessionKeyGetBool = false;
        
        public void run() {
            if(sessionKey == null) {
                //直接获取
                if(!sessionKeyGetBool) {
                    sessionKeyGetBool = true;
                    getSessionKeyRun();
                }else {
                    callback.onException(null);
                }
                return;
            }
            BaseRequest br = null;
            if(post) {
                br = OkHttpUtils.post(url).tag(context);
            }else {
                br = OkHttpUtils.get(url).tag(context);
            }
            if (pres != null && pres.size() > 0) {
                if (pres != null) {
                    Iterator<String> it = pres.keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        Object value = pres.get(key);
                        br.params(key, String.valueOf(value));
                    }
                }
            }
            br.params("sessionkey", sessionKey);
            br.execute(this);
        }

        public void getSessionKeyRun() {
            BaseRequest br = OkHttpUtils.get(Contonts.SESSION_KEY_LOGIN);//TODO 直接写死，移Contonts再改
            br.params("id", "getAccess");
            br.params("login_name", "fshdskjf");
            br.params("password", "1367jhd");
            br.execute(new AbsCallback() {
                @Override
                public Object parseNetworkResponse(Response response) throws Exception {
                    String str = response.body().string();
                    JSONObject json = new JSONObject(str);
                    sessionKey = json.getString("sessionkey");
                    return null;
                }

                @Override
                public void onSuccess(Object o, Call call, Response response) {
                    run();
                }

                public void onError(Call call, Response response, Exception e) {
                    //弹出异常
                    callback.onException(e);
                }
            });
        }


        @Override
        public Object parseNetworkResponse(Response response) throws Exception {
            //直接转JSON，如果是session失效，直接重新获取
            String str = response.body().string();
            try {
                JSONObject json = new JSONObject(str);
                if (json.optString("state").equals("success")) {
                    return callback.onAsyncCall(str);
                }
                //如果需要再次获取 sessionkey
                if(!sessionKeyGetBool) {
                    sessionKeyGetBool = true;
                    getSessionKeyRun();
                }else {
                    callback.onException(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onSuccess(Object o, Call call, Response response) {
            if(o != null) {
                callback.onUiCall(o);
            } else {
                callback.onException(null);
            }
        }

        public void onError(Call call, Response response, Exception e) {
            //弹出异常
            callback.onException(e);
        }
    }
}
