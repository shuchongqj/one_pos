package com.gzdb.yct.socket.impl.abilities;


import com.gzdb.yct.socket.sdk.ConnectionInfo;
import com.gzdb.yct.socket.sdk.connection.IConnectionManager;

/**
 * Created by xuhao on 2017/6/30.
 */

public interface IConnectionSwitchListener {
    void onSwitchConnectionInfo(IConnectionManager manager, ConnectionInfo oldInfo, ConnectionInfo newInfo);
}
