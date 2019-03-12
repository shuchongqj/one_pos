package com.one.pos.service.print;

import android.content.Context;

import com.anlib.util.LogUtils;
import com.anlib.util.ToastUtils;
import com.one.pos.service.print.bluetooth.PrintData;

import java.io.OutputStream;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhumg
 */
public abstract class PrintThread implements Runnable {

    private LinkedBlockingQueue<PrintData> queue = new LinkedBlockingQueue<PrintData>();
    private boolean runing;
    private Thread thread;
    protected Context context;

    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (runing) {
            PrintData printData = null;
            try {
                LogUtils.info("取数据");
                printData = queue.take();
            } catch (Throwable ex) {
                onClose();
                LogUtils.info("退出线程");
                return;
            }
            byte[] datas = printData.getDatas();
            int count = printData.getCount();
            if (count < 1) {
                count = 1;
            }
            for (int i = 0; i < count; i++) {
                //打印
                try {
                    onPrint(datas);
                } catch (Exception ex) {
                    onClose();
                    ToastUtils.show(context, "打印异常，请检查");
                }
            }
        }
    }

    /**
     * 调用打印
     *
     * @param printData
     */
    public void print(PrintData printData) {
        //运行中，则可以打印
        queue.offer(printData);
        if (!runing) {
            //连接
            onConnect();
        }
    }

    protected void startThread() {
        //起动
        if (!runing) {
            runing = true;
            //启动
            thread = new Thread(this);
            thread.start();
        }
    }

    protected void closeThread() {
        runing = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    protected abstract void onConnect();

    protected abstract void onClose();

    protected abstract void onPrint(byte[] datas);
}
