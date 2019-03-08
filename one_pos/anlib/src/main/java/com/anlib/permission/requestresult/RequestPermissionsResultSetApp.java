package com.anlib.permission.requestresult;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.anlib.permission.PermissionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 類：RequestPermissionsResult
 * 處理權限申請的結果，如果未允許，提示用戶,並跳轉至設置APP權限頁面
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class RequestPermissionsResultSetApp implements IRequestPermissionsResult {
    private static RequestPermissionsResultSetApp requestPermissionsResult;

    public static RequestPermissionsResultSetApp getInstance() {
        if (requestPermissionsResult == null) {
            requestPermissionsResult = new RequestPermissionsResultSetApp();
        }
        return requestPermissionsResult;
    }

    @Override
    public boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean isAllGranted = true;
        // 判斷是否所有的權限都已經授予了
        for (int grant : grantResults) {
            if (grant != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }

        //已全部授權
        if (isAllGranted) {
            return true;
        }
        //引導用戶去授權
        else {
            List<String> deniedPermission = new ArrayList<>();
            //如果選擇了“不再詢問”，則彈出“權限指導對話框”
            for (int i = 0; i < permissions.length; i++) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i])) {
                    deniedPermission.add(permissions[i]);
                }
            }
            if (deniedPermission.size() > 0) {
                String name = PermissionUtils.getInstance().getPermissionNames(deniedPermission);
                SetPermissions.openAppDetails(activity, name);
            }
        }
        return false;
    }
}
