package com.gzdb.yct.socket.impl.blockio.threads;

import android.content.Context;

import java.io.IOException;

import com.gzdb.yct.socket.impl.LoopThread;
import com.gzdb.yct.socket.impl.abilities.IWriter;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.connection.interfacies.IAction;
import com.gzdb.yct.socket.utils.SL;

/**
 * Created by xuhao on 2017/5/17.
 */

public class DuplexWriteThread extends LoopThread {
    private IStateSender mStateSender;

    private IWriter mWriter;

    public DuplexWriteThread(Context context, IWriter writer,
                             IStateSender stateSender) {
        super(context, "duplex_write_thread");
        this.mStateSender = stateSender;
        this.mWriter = writer;
    }

    @Override
    protected void beforeLoop() {
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_START);
    }

    @Override
    protected void runInLoopThread() throws IOException {
        mWriter.write();
    }

    @Override
    protected void loopFinish(Exception e) {
        if (e != null) {
            SL.e("duplex write error,thread is dead with exception:" + e.getMessage());
        }
        mStateSender.sendBroadcast(IAction.ACTION_WRITE_THREAD_SHUTDOWN, e);
    }

    public boolean isNeedSend() {
        if (mWriter != null) {
            int size = mWriter.queueSize();
            if (size >= 3) {//需要输出了
                return true;
            }
        }
        return false;
    }
}
