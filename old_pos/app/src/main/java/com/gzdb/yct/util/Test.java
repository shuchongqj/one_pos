package com.gzdb.yct.util;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.basepos.App;
import com.gzdb.vaservice.bean.Record;

import java.io.UnsupportedEncodingException;

import rx.Subscriber;
import rx.functions.Action0;

public class Test {

    public static void main(String[] args) {
//        Integer x = 666;
//        String hex = x.toHexString(x);
//        String sub = "";
//        for (int i = 0; i < 16 - hex.length(); i++) {
//            sub += "0";
//        }
//        System.out.println(sub + hex);
//
//        String hex2 = "0000004c56";
//        Integer x2 = Integer.parseInt(hex2, 16);
//        System.out.println(x2);
//
//        String str=TUtil.convertHexToString("BFA8D7B4CCACB4EDCEF3");
//        System.out.println(str);
//
        String string = "D0C5CFA2B4EDCEF3A3ACC7EBD6D8D0C2B6C1BFA8";
        byte[] bytes = new byte[string.length() / 2];
        for(int i = 0; i < bytes.length; i ++){
            byte high = Byte.parseByte(string.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(string.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        try {
            System.out.println(new String(bytes, "gbk"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String hex="BDE7250089006555A12007135EDFF8215100000401136882AB1F199F0000000001FFFFFFFFFFFF030000000000FFFFFFFF5100000403020113688236B88CB825121512040381210000052B000140A296B90003E80000035300001805174546000156000000A9FFFFFF5600000000FF00FF00000000000000300200000000FFCFFD0000000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF45461400000120000503180517153819020000000000";
        System.out.println(hex.length());

        int timeOut = 2000;
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
            timeOut--;
            System.out.println(timeOut+"");
        } while (true && timeOut > 0);
        if (timeOut == 0) {
            System.out.println("哈哈儿啦");
        }
    }

    private static String getMoney(double m) {
        Integer money = (int) (m * 100);
        String hex = money.toHexString(money);
        String sub = "";
        for (int i = 0; i < 8 - hex.length(); i++) {
            sub += "0";
        }
        return sub + hex;
    }
}
