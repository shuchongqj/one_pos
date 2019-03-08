package com.gzdb.yct.socket.impl.blockio.threads;

import android.content.Context;

import java.io.IOException;

import com.gzdb.yct.socket.impl.LoopThread;
import com.gzdb.yct.socket.impl.abilities.IReader;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.connection.interfacies.IAction;
import com.gzdb.yct.socket.utils.SL;

/**
 * Created by xuhao on 2017/5/17.
 */

public class DuplexReadThread extends LoopThread {
    private IStateSender mStateSender;

    private IReader mReader;

    public DuplexReadThread(Context context, IReader reader, IStateSender stateSender) {
        super(context, "duplex_read_thread");
        this.mStateSender = stateSender;
        this.mReader = reader;
    }

    @Override
    protected void beforeLoop() {
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        mReader.read();
    }

    @Override
    protected void loopFinish(Exception e) {
        if (e != null) {
            SL.e("duplex read error,thread is dead with exception:" + e.getMessage());
        }
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_SHUTDOWN, e);
    }
}
