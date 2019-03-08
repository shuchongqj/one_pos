package com.zhumg.anlib.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.zhumg.anlib.widget.dialog.TipClickListener;

import java.util.ArrayList;

/**
 * Created by zhumg on 2017/3/24 0024.
 */

public class PermissionUtils {

    public static final int SDK_PERMISSION_REQUEST = 127;

    //6.0系统以后部分权限要自动获取
    @TargetApi(23)
    public static void getPersimmions(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> permissions = new ArrayList<String>();
            // 位置权限
            if (addPermission(activity, permissions, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            }
            if (permissions.size() > 0) {
                activity.requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    public static boolean addPermission(Activity activity, ArrayList<String> permissionsList, String permission) {
        if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    public static void onPermissionsResult(final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            DialogUtils.createTipDialog(activity, "该功能需要定位权限,请您手动为本程序授权", new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if(!left) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}
