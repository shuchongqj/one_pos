package com.gzdb.yct.socket.impl;

import android.content.Context;

import com.gzdb.yct.socket.sdk.ConnectionInfo;
import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.SocketActionAdapter;
import com.gzdb.yct.socket.sdk.connection.IConnectionManager;
import com.gzdb.yct.socket.sdk.connection.abilities.IRegister;

/**
 * Created by xuhao on 2017/5/18.
 */

public class SocketActionHandler extends SocketActionAdapter {
    private IConnectionManager mManager;

    private OkSocketOptions.IOThreadMode mCurrentThreadMode;

    private boolean iOThreadIsCalledDisconnect = false;

    protected SocketActionHandler() {

    }

    public void attach(IConnectionManager manager, IRegister register) {
        this.mManager = manager;
        register.registerReceiver(this);
    }

    public void detach(IRegister register) {
        register.unRegisterReceiver(this);
    }

    @Override
    public void onSocketIOThreadStart(Context context, String action) {
        if (mManager.getOption().getIOThreadMode() != mCurrentThreadMode) {
            mCurrentThreadMode = mManager.getOption().getIOThreadMode();
        }
        iOThreadIsCalledDisconnect = false;
    }

    @Override
    public void onSocketIOThreadShutdown(Context context, String action, Exception e) {
        if (mCurrentThreadMode != mManager.getOption().getIOThreadMode()) {//切换线程模式,不需要断开连接
            //do nothing
        } else {//多工模式
            if (!iOThreadIsCalledDisconnect) {//保证只调用一次,多工多线程,会调用两次
                iOThreadIsCalledDisconnect = true;
                mManager.disconnect(e);
            }
        }
    }

    @Override
    public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
        mManager.disconnect(e);
    }
}
