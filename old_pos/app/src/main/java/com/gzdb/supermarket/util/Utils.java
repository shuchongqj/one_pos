package com.gzdb.supermarket.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.core.http.Http;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhumg on 9/7.
 */
public class Utils {

    //你好 , 在那里
    private static Context context;
    private static TaskRuner taskRuner;
    private static int screenHeight;
    private static int screenWidth;
    private static java.text.DecimalFormat df = new java.text.DecimalFormat(
            "0.00");

    public static void init(Application application) {

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        context = application;

        //初始化异步任务处理
        taskRuner = new TaskRuner();
        taskRuner.start();

        //系统key value保存控件
        SharedPreferencesUtil.init(context);

        //初始化HTTP
        Http.init(application);


        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：px）
        screenHeight = dm.heightPixels; // 屏幕高（像素，如：px）

        ImageLoaders.initImageLoader(application);

        //注册Exception监听
        CatchHander catchHander = CatchHander.getInstance();
        catchHander.init(application);
    }

    public static String toYuanStr(long price) {
        double v = (double) price / 100;
        return doubleToTowStr(v);
    }

    public static String doubleToTowStr(double price) {
        //截取2位double
        String vs = String.valueOf(price);
        StringBuilder sb = new StringBuilder();
        boolean point = false;
        int pointCount = 0;
        for (int i = 0; i < vs.length(); i++) {
            char c = vs.charAt(i);
            if (c == '.') {
                point = true;
                sb.append(c);
                continue;
            }
            if (point) {
                pointCount++;
                //只截2位
                if (pointCount == 3) {
                    break;
                }
            }
            sb.append(c);
        }
        //如果未够2位数
        if (pointCount != 2) {
            int c = 2 - pointCount;
            for (int i = 0; i < c; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static Context getAppContext() {
        return context;
    }

    public static TaskRuner getTaskRuner() {
        return taskRuner;
    }


    public static String doublePriceToStr(double price) {
        BigDecimal bd = new BigDecimal(price);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd + "";
    }

    public static String strPriceTo(String price) {
        return df.format(price);
    }

    /**
     * 应用程序退出
     */
    public static void appExit() {
        try {
            com.gzdb.supermarket.activity.ActivityManager.finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }

    }

    /**
     * 判断是否为合法手机号码
     *
     * @param mobiles
     * @return boolean
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 密码控制
     *
     * @param pwd
     * @return
     */
    public static boolean isPassWord(String pwd) {
        pwd.trim();
        if (pwd.length() < 6)
            return false;
        return true;
    }

    public static boolean isNull(CharSequence str) {
        return str == null || "".equals(str);
    }


    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static int[] input(String value) {
        String vs = value.trim();
        if (vs.length() < 1) return null;
        int[] v = new int[value.length()];
        for (int i = 0; i < vs.length(); i++) {
            char c = vs.charAt(i);
            int iv = 0;
            switch (c) {
                case '0':
                    iv = 0X30;
                    break;
                case '1':
                    iv = 0X31;
                    break;
                case '2':
                    iv = 0X32;
                    break;
                case '3':
                    iv = 0X33;
                    break;
                case '4':
                    iv = 0X34;
                    break;
                case '5':
                    iv = 0X35;
                    break;
                case '6':
                    iv = 0X36;
                    break;
                case '7':
                    iv = 0X37;
                    break;
                case '8':
                    iv = 0X38;
                    break;
                case '9':
                    iv = 0X39;
                    break;
                case '.':
                    iv = 0X2E;
                    break;
            }
            v[i] = iv;
        }
        return v;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;

    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        if (s == null || s.equals("") || s.equals("null") || s.equals("0")) {
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
