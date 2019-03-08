package com.anlib.permission.requestresult;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * 類：RequestPermissionsResult 處理權限申請的結果，如果未允許，不做處理
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class RequestPermissionsResult implements IRequestPermissionsResult {
    private static RequestPermissionsResult requestPermissionsResult;
    public static RequestPermissionsResult getInstance(){
        if(requestPermissionsResult == null){
            requestPermissionsResult = new RequestPermissionsResult();
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
        else {
            //什麼也不做
        }
        return false;
    }
}
