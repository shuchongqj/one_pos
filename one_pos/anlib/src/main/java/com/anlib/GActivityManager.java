package com.anlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * @author zhumg
 */
public class GActivityManager {

    private static Map<Integer, GActivity> activityMap = new HashMap<>();

    /**
     * 添加Activity到栈
     */
    protected static void addActivity(GActivity activity) {
        try {
            activityMap.put(activity.getActivityId(), activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected static void removeActivity(GActivity activity) {
        activityMap.remove(activity.getActivityId());
    }

    /**
     * 判断某个activity是否显示中
     * @param cls
     * @return
     */
    public static boolean activityIsShow(Class<?> cls) {
        Iterator<GActivity> as = activityMap.values().iterator();
        while (as.hasNext()) {
            GActivity a = as.next();
            if (a.getClass().getName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束指定的Activity(重载)
     */
    public static void finishActivity(Class<?> cls) {
        try {
            List<Activity> as = new ArrayList<>();
            Iterator<GActivity> iterator = activityMap.values().iterator();
            while (iterator.hasNext()) {
                GActivity a = iterator.next();
                //相同，则添加
                if (a.getClass().getName().equals(cls.getName())) {
                    as.add(a);
                }
            }
            for (int i = 0; i < as.size(); i++) {
                as.get(i).finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public static void finishOtherActivity(Class<?> cls) {
        try {
            List<Activity> as = new ArrayList<>();
            Iterator<GActivity> iterator = activityMap.values().iterator();
            while (iterator.hasNext()) {
                GActivity a = iterator.next();
                //不相同，则添加
                if (!a.getClass().getName().equals(cls.getName())) {
                    as.add(a);
                }
            }
            for (int i = 0; i < as.size(); i++) {
                as.get(i).finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        try {
            Iterator<GActivity> iterator = activityMap.values().iterator();
            while (iterator.hasNext()) {
                GActivity a = iterator.next();
                a.finish();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 应用程序退出
     */
    public static void appExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public static void startActivityAndFinish(Activity activity, Class<?> cls) {
        activity.startActivity(new Intent(activity, cls));
        activity.finish();
    }

    public static void startActivity(Activity activity, Class<?> cls) {
        activity.startActivity(new Intent(activity, cls));
    }
}
