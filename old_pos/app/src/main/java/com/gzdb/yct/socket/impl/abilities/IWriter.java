package com.gzdb.yct.socket.impl.abilities;


import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IWriter {
    boolean write() throws RuntimeException;

    void setOption(OkSocketOptions option);

    void offer(ISendable sendable);

    int queueSize();

}
