package com.gzdb.yct.socket.sdk.connection.abilities;

import android.content.Context;

import com.gzdb.yct.socket.sdk.ConnectionInfo;
import com.gzdb.yct.socket.sdk.SocketActionAdapter;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IDisConnectable {
    /**
     * 断开当前连接管理器的链接,并伴随着一个异常<br>
     * 该异常信息将回调在{@link SocketActionAdapter#onSocketDisconnection(Context, ConnectionInfo, String, Exception)}<br>
     *
     * @param e 断开时希望伴随的异常对象
     */
    void disconnect(Exception e);

    /**
     * 断开当前连接管理器的链接,,断开回调中的断开异常将是Null<br>
     */
    void disconnect();
}
