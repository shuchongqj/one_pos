package com.gzdb.supermarket.util;

import android.os.Handler;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zhumg on 7/16.
 */
public class TaskRuner extends Thread {

    // 等待中列表
    private LinkedBlockingQueue<Task> waitTasks = new LinkedBlockingQueue<Task>();

    private boolean runing = false;
    private Handler handler = new Handler();

    public void run() {
        runing = true;
        while(runing) {
            Task runnable = null;
            try {
                runnable = waitTasks.take();
            } catch (Exception ie) {
                ie.printStackTrace();
            }
            if(runnable == null) {
                continue;
            }
            try {
                runnable.onAsyncRun();
                handler.post(runnable);
            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }
    }

    public void close() {
        runing = false;
        add(new Task() {
            @Override
            public void onAsyncRun() {

            }

            @Override
            public void run() {

            }
        });
    }

    public void add(Task runnable) {
        waitTasks.offer(runnable);
    }
}
