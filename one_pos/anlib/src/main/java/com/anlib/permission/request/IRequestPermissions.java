package com.anlib.permission.request;

import android.app.Activity;

/**
 * 類：IRequestPermissions 申請權限
 * 作者： qxc
 * 日期：2018/2/8.
 */
public interface IRequestPermissions {
    /**
     * 請求權限
     * @param activity 上下文
     * @param permissions 權限集合
     * @param resultCode 請求碼
     * @return 如果權限已全部允許，返回true; 反之，請求權限，在
     */
    boolean requestPermissions(Activity activity, String[] permissions, int resultCode);
}
