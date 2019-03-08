package com.gzdb.yct.socket.impl.blockio.threads;

import android.content.Context;

import java.io.IOException;

import com.gzdb.yct.socket.impl.LoopThread;
import com.gzdb.yct.socket.impl.abilities.IReader;
import com.gzdb.yct.socket.impl.abilities.IWriter;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.connection.interfacies.IAction;
import com.gzdb.yct.socket.utils.SL;

/**
 * Created by xuhao on 2017/5/17.
 */

public class SimplexIOThread extends LoopThread {
    private IStateSender mStateSender;


    private IReader mReader;

    private IWriter mWriter;

    private boolean isWrite = false;


    public SimplexIOThread(Context context, IReader reader,
                           IWriter writer, IStateSender stateSender) {
        super(context, "simplex_io_thread");
        this.mStateSender = stateSender;
        this.mReader = reader;
        this.mWriter = writer;
    }

    @Override
    protected void beforeLoop() throws IOException {
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_START);
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        isWrite = mWriter.write();
        if (isWrite) {
            isWrite = false;
            mReader.read();
        }
    }

    @Override
    protected void loopFinish(Exception e) {
        if (e != null) {
            SL.e("simplex error,thread is dead with exception:" + e.getMessage());
        }
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_SHUTDOWN, e);
        mStateSender.sendBroadcast(IAction.ACTION_READ_THREAD_SHUTDOWN, e);
    }
}
