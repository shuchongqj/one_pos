package com.gzdb.yct.socket.impl.blockio.io;

import android.support.annotation.MainThread;

import java.io.InputStream;

import com.gzdb.yct.socket.impl.abilities.IReader;
import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;

/**
 * Created by Tony on 2017/12/26.
 */

public abstract class AbsReader implements IReader {

    protected OkSocketOptions mOkOptions;

    protected IStateSender mStateSender;

    protected InputStream mInputStream;

    public AbsReader(InputStream inputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mInputStream = inputStream;
    }

    @Override
    @MainThread
    public void setOption(OkSocketOptions option) {
        mOkOptions = option;
    }
}
