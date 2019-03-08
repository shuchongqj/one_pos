package com.anlib.permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 類：PermissionUtils
 * 作者： qxc
 * 日期：2018/2/8.
 */
public class PermissionUtils {

    public static int ResultCode1 = 100;//權限請求碼
    public static int ResultCode2 = 200;//權限請求碼
    public static int ResultCode3 = 300;//權限請求碼
    public static String PermissionTip1 = "親愛的用戶 \n\n軟件部分功能需要請求您的手機權限，請允許以下權限：\n\n";//權限提醒
    public static String PermissionTip2 = "\n請到 “應用信息 -> 權限” 中授予！";//權限提醒
    public static String PermissionDialogPositiveButton = "去手動授權";
    public static String PermissionDialogNegativeButton = "取消";

    private static PermissionUtils permissionUtils;
    public static PermissionUtils getInstance(){
        if(permissionUtils == null){
            permissionUtils = new PermissionUtils();
        }
        return permissionUtils;
    }

    private HashMap<String,String> permissions;
    public HashMap<String,String> getPermissions(){
        if(permissions == null){
            permissions = new HashMap<>();
            initPermissions();
        }
        return permissions;
    }

    private void initPermissions(){
        //聯系人/通訊錄權限
        permissions.put("android.permission.WRITE_CONTACTS","--通訊錄/聯系人");
        permissions.put("android.permission.GET_ACCOUNTS","--通訊錄/聯系人");
        permissions.put("android.permission.READ_CONTACTS","--通訊錄/聯系人");
        //電話權限
        permissions.put("android.permission.READ_CALL_LOG","--電話");
        permissions.put("android.permission.READ_PHONE_STATE","--電話");
        permissions.put("android.permission.CALL_PHONE","--電話");
        permissions.put("android.permission.WRITE_CALL_LOG","--電話");
        permissions.put("android.permission.USE_SIP","--電話");
        permissions.put("android.permission.PROCESS_OUTGOING_CALLS","--電話");
        permissions.put("com.android.voicemail.permission.ADD_VOICEMAIL","--電話");
        //日曆權限
        permissions.put("android.permission.READ_CALENDAR","--日曆");
        permissions.put("android.permission.WRITE_CALENDAR","--日曆");
        //相機拍照權限
        permissions.put("android.permission.CAMERA","--相機/拍照");
        //傳感器權限
        permissions.put("android.permission.BODY_SENSORS","--傳感器");
        //定位權限
        permissions.put("android.permission.ACCESS_FINE_LOCATION","--定位");
        permissions.put("android.permission.ACCESS_COARSE_LOCATION","--定位");
        //文件存取
        permissions.put("android.permission.READ_EXTERNAL_STORAGE","--文件存儲");
        permissions.put("android.permission.WRITE_EXTERNAL_STORAGE","--文件存儲");
        //音視頻、錄音權限
        permissions.put("android.permission.RECORD_AUDIO","--音視頻/錄音");
        //短信權限
        permissions.put("android.permission.READ_SMS","--短信");
        permissions.put("android.permission.RECEIVE_WAP_PUSH","--短信");
        permissions.put("android.permission.RECEIVE_MMS","--短信");
        permissions.put("android.permission.RECEIVE_SMS","--短信");
        permissions.put("android.permission.SEND_SMS","--短信");
        permissions.put("android.permission.READ_CELL_BROADCASTS","--短信");
    }

    /**
     * 獲得權限名稱集合（去重）
     * @param permission 權限數組
     * @return 權限名稱
     */
    public String getPermissionNames(List<String> permission){
        if(permission==null || permission.size()==0){
            return "\n";
        }
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>();
        HashMap<String,String> permissions = getPermissions();
        for(int i=0; i<permission.size(); i++){
            String name = permissions.get(permission.get(i));
            if(name!=null && !list.contains(name)){
                list.add(name);
                sb.append(name);
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
