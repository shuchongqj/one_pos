package com.gzdb.yct.data;

import com.gzdb.yct.socket.sdk.bean.ISendable;

/**
 * Created by Tony on 2017/10/24.
 */

public class MsgDataBean implements ISendable {
    private byte[] mBytes;

    public MsgDataBean(byte[] bytes) {
        this.mBytes = bytes;
    }

    @Override
    public byte[] parse() {
        return mBytes;
    }
}
