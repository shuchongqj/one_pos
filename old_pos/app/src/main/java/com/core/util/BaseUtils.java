package com.core.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nongyd on 17/4/29.
 */

public class BaseUtils {

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static boolean isEmpty(String str){
        if(TextUtils.isEmpty(str)){
            return true;
        }
        if(null==str){
            return true;
        }
        if("null".equals(str)){
            return true;
        }
        if("".equals(str)){
            return true;
        }
        return false;
    }


    /*将时间戳转为字符串*/
    public static String convertToStr(long mill){
        Date date=new Date(mill);
        String strs="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
            strs=sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    /*将时间戳转为字符串*/
    public static String convertToStrSS(long mill){
        Date date=new Date(mill);
        String strs="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            strs=sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    /*将时间戳转为字符串*/
    public static String convertToStrHHSS(long mill){
        Date date=new Date(mill);
        String strs="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
            strs=sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
    /*将字符串转为时间戳*/
    public static long getStringToDate(String time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try{
            date = df.parse(time);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    //打电话
    public static void callDial(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ phoneNumber)));
    }

    /**
     * 判断文件是否为图片
     *
     * @param pInput 文件名
     * @param pImgeFlag 判断具体文件类型
     * @return 检查后的结果
     * @throws Exception
     */
    public static boolean isPicture(String  pInput,
                                    String pImgeFlag) throws Exception{
        // 文件名称为空的场合
        if(isEmpty(pInput)){
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1,
                pInput.length());
        // 声明图片后缀名数组
        String imgeArray [][] = {
                {"bmp", "0"}, {"dib", "1"}, {"gif", "2"},
                {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"},
                {"jpg", "6"}, {"png", "7"} ,{"tif", "8"},
                {"tiff", "9"}, {"ico", "10"}
        };
        // 遍历名称数组
        for(int i = 0; i<imgeArray.length;i++){
            // 判断单个类型文件的场合
            if(!isEmpty(pImgeFlag)
                    && imgeArray [i][0].equals(tmpName.toLowerCase())
                    && imgeArray [i][1].equals(pImgeFlag)){
                return true;
            }
            // 判断符合全部类型的场合
            if(isEmpty(pImgeFlag)
                    && imgeArray [i][0].equals(tmpName.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public static float dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static float px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }
}
