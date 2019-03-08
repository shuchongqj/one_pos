package com.anlib.permission.requestresult;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 類：IRequestPermissionsResult
 * 作者： qxc
 * 日期：2018/2/8.
 */
public interface IRequestPermissionsResult {
    /**
     * 處理權限請求結果
     * @param activity
     * @param permissions 請求的權限數組
     * @param grantResults 權限請求結果數組
     * @return 處理權限結果如果全部通過，返回true；否則，引導用戶去授權頁面
     */
    boolean doRequestPermissionsResult(Activity activity, @NonNull String[] permissions, @NonNull int[] grantResults);
}
