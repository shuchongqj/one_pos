package com.gzdb.yct.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtil {

    public static String getStampTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static long dateToStamp2(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        return date.getTime();
    }

    /* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String dateToDate(Date date) {
        DateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd"); //HH表示24小时制
        return dFormat.format(date);
    }

    public static String stampToMDHM(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDetailDateJava(long l) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(l);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDetailDateJava3(long l) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(l);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDetailDateJava2(long l) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(l);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDetailDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDetailDate2L(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampShortDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDay(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToMonth(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM");
        long lt = new Long(s) * 1000;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static boolean compareDate(String date1,String date2){
        try {
            return dateToStamp2(date1)<=dateToStamp2(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getTime() {
        return stampToDetailDateJava(System.currentTimeMillis());
    }

    public static String getTime2() {
        return stampToDetailDateJava2(System.currentTimeMillis());
    }
}
