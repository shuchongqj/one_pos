package com.gzdb.supermarket.activity;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhumg on 7/11.
 */
public class ActivityManager {

    private static Stack<Activity> activityStack= new Stack<Activity>();


    /**
     * 添加Activity到栈
     */
    public static void addActivity(Activity activity) {
        try {
            activityStack.add(activity);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static void removeActivity(Activity activity) {
        try {
            activityStack.remove(activity);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public static Activity currentActivity() {
        try {
            if (activityStack == null || activityStack.isEmpty()) {
                return null;
            }
            Activity activity = activityStack.lastElement();
            return activity;
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 查找指定activity
     */
    public static Activity getActivity(Class<?> cls) {
        Activity activity = null;
        try {
            for (Activity aty : activityStack) {
                if (aty.getClass().equals(cls)) {
                    activity = aty;
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public static void finishActivity() {
        try {
            Activity activity = currentActivity();
            finishActivity(activity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 结束指定的Activity(重载)
     */
    public static void finishActivity(Activity activity) {
        try {
            if (activity != null) {
                activity.finish();
                activity = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public static void finishActivity(Class<?> cls) {
        try {
            Activity a_Activity = null;
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    a_Activity = activity;
                    break;
                }
            }
            if(a_Activity!=null){
                finishActivity(a_Activity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public static void finishOthersActivity(Class<?> cls) {
        try {
            List<Activity> activities = new ArrayList<Activity>();
            for (Activity activity : activityStack) {
                Log.e("zhumg", "存在：" + activity.getClass().getName());
                if (!(activity.getClass().equals(cls))) {
                    activities.add(activity);
                }
            }
            for (int i = 0; i < activities.size(); i++) {
                Log.e("zhumg", "释放：" + activities.get(i).getClass().getName());
                finishActivity(activities.get(i));
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
            List<Activity> activities = new ArrayList<Activity>();
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activities.add(activityStack.get(i));
                }
            }
            for (int i = 0; i < activities.size(); i++) {
                activities.get(i).finish();
            }
            activityStack.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 应用程序退出
     */
    public static void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
