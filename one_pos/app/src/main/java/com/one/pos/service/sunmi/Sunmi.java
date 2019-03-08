package com.one.pos.service.sunmi;

import android.content.Context;


import com.one.pos.service.sunmi.bluetooth.SunmiBluetoothPrint;

/**
 * 商米服务
 * 商米POS机，直接用本机的蓝牙协议连接本机打印机进行打印
 *
 * @author zhumg
 */
public class Sunmi {

    private static SunmiBluetoothPrint bPrintManager;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        bPrintManager = new SunmiBluetoothPrint(context);
    }

    /**
     * 退出
     */
    public static void onExit() {
        bPrintManager.close();
    }

    /**
     * 打印内容
     */
    public static void print(PrintTask task) {
        bPrintManager.sendDatas(task);
    }

    /**
     * 打开钱箱
     */
    public static void openMoneyBox() {

    }
}
