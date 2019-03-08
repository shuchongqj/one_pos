package com.gzdb.supermarket.util;

import android.content.Context;

import java.math.BigDecimal;

/**
 * 数据转换用的工具类
 * Created by nongyd on 2017/4/21.
 */

public class FormatUtils {
    /***
     * 这个是用来格式化金额的，返回最多两们小数，4舍5入
     * @param
     * @return
     */

    public static double formatFloat(float f){
        BigDecimal b  =   new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double formatFloat(String s){
        BigDecimal b  =   new BigDecimal(s);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static double formatFloat(double f){
        BigDecimal b  =   new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }



    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
