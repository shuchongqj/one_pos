package com.gzdb.supermarket.util;

/**
 * Created by zhumg on 7/16.
 */
public abstract class Task implements Runnable {
    public abstract void onAsyncRun();

    public void onUiRun() {

    }

    public void run() {
        onUiRun();
    }
}
