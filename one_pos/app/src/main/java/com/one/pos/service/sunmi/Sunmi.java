package com.one.pos.service.sunmi;

import android.content.Context;


import java.util.List;

/**
 * 商米服务
 *
 * @author zhumg
 */
public class Sunmi {

    private static PrintManager printManager;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        printManager = new PrintManager(context);
    }

    /**
     * 退出
     */
    public static void onExit() {
        printManager.exit();
    }

    /**
     * 打印内容
     */
    public static void print(PrintTask task) {
        printManager.print(task);
    }

    /**
     * 打开钱箱
     */
    public static void openMoneyBox() {

    }
}
