package com.gzdb.yct.socket.impl.abilities;


import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IIOManager {
    void resolve();

    void setOkOptions(OkSocketOptions options);

    void send(ISendable sendable);

    void close();

}
