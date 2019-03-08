package com.anlib.permission.request;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 類：CheckPermission 檢測權限
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class CheckPermission {
    /**
     * 檢查是否擁有指定的所有權限
     * @param context 上下文
     * @param permissions 權限數組
     * @return 只要有一個權限沒有被授予, 則直接返回 false，否則，返回true!
     */
    public static boolean checkPermissionAllGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 檢查未允許的權限集合
     * @param context 上下文
     * @param permissions 權限集合
     * @return 未允許的權限集合
     */
    public static List<String> checkPermissionDenied(Context context, String[] permissions){
        List<String> lstPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                lstPermissions.add(permission);
            }
        }
        return lstPermissions;
    }
}
