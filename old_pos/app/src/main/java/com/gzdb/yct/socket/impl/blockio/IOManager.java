package com.gzdb.yct.socket.impl.blockio;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.InputStream;
import java.io.OutputStream;

import com.gzdb.yct.socket.impl.LoopThread;
import com.gzdb.yct.socket.impl.abilities.IIOManager;
import com.gzdb.yct.socket.impl.abilities.IReader;
import com.gzdb.yct.socket.impl.abilities.IWriter;
import com.gzdb.yct.socket.impl.blockio.io.ReaderImpl;
import com.gzdb.yct.socket.impl.blockio.io.WriterImpl;
import com.gzdb.yct.socket.impl.blockio.threads.DuplexReadThread;
import com.gzdb.yct.socket.impl.blockio.threads.DuplexWriteThread;
import com.gzdb.yct.socket.impl.blockio.threads.SimplexIOThread;
import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.bean.ISendable;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.protocol.IHeaderProtocol;
import com.gzdb.yct.socket.utils.SL;

/**
 * Created by xuhao on 2017/5/31.
 */

public class IOManager implements IIOManager {

    private Context mContext;

    private InputStream mInputStream;

    private OutputStream mOutputStream;

    private OkSocketOptions mOkOptions;

    private IStateSender mSender;

    private IReader mReader;

    private IWriter mWriter;

    private LoopThread mSimplexThread;

    private DuplexReadThread mDuplexReadThread;

    private DuplexWriteThread mDuplexWriteThread;

    private OkSocketOptions.IOThreadMode mCurrentThreadMode;

    public IOManager(@NonNull Context context,
                     @NonNull InputStream inputStream,
                     @NonNull OutputStream outputStream,
                     @NonNull OkSocketOptions okOptions,
                     @NonNull IStateSender stateSender) {
        mContext = context;
        mInputStream = inputStream;
        mOutputStream = outputStream;
        mOkOptions = okOptions;
        mSender = stateSender;
        initIO();
    }

    private void initIO() {
        assertHeaderProtocolNotEmpty();
        mReader = new ReaderImpl(mInputStream, mSender);
        mWriter = new WriterImpl(mOutputStream, mSender);
    }

    @Override
    public void resolve() {
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
        //初始化读写工具类
        mReader.setOption(mOkOptions);
        mWriter.setOption(mOkOptions);
        switch (mOkOptions.getIOThreadMode()) {
            case DUPLEX:
                SL.e("DUPLEX is processing");
                duplex();
                break;
            case SIMPLEX:
                SL.e("SIMPLEX is processing");
                simplex();
                break;
            default:
                throw new RuntimeException("未定义的线程模式");
        }
    }

    private void duplex() {
        shutdownAllThread();
        mDuplexWriteThread = new DuplexWriteThread(mContext, mWriter, mSender);
        mDuplexReadThread = new DuplexReadThread(mContext, mReader, mSender);
        mDuplexWriteThread.start();
        mDuplexReadThread.start();
    }

    private void simplex() {
        shutdownAllThread();
        mSimplexThread = new SimplexIOThread(mContext, mReader, mWriter, mSender);
        mSimplexThread.start();
    }

    private void shutdownAllThread() {
        if (mSimplexThread != null) {
            mSimplexThread.shutdown();
            mSimplexThread = null;
        }
        if (mDuplexReadThread != null) {
            mDuplexReadThread.shutdown();
            mDuplexReadThread = null;
        }
        if (mDuplexWriteThread != null) {
            mDuplexWriteThread.shutdown();
            mDuplexWriteThread = null;
        }
    }

    @Override
    public void setOkOptions(OkSocketOptions options) {
        mOkOptions = options;
        if (mCurrentThreadMode == null) {
            mCurrentThreadMode = mOkOptions.getIOThreadMode();
        }
        assertTheThreadModeNotChanged();
        assertHeaderProtocolNotEmpty();

        mWriter.setOption(mOkOptions);
        mReader.setOption(mOkOptions);
    }

    @Override
    public void send(ISendable sendable) {
        mWriter.offer(sendable);
    }

    @Override
    public void close() {
        shutdownAllThread();
        mCurrentThreadMode = null;
    }

    private void assertHeaderProtocolNotEmpty() {
        IHeaderProtocol protocol = mOkOptions.getHeaderProtocol();
        if (protocol == null) {
            throw new IllegalArgumentException("The header protocol can not be Null.");
        }

        if (protocol.getHeaderLength() == 0) {
//            throw new IllegalArgumentException("The header length can not be zero.");
        }
    }

    private void assertTheThreadModeNotChanged() {
        if (mOkOptions.getIOThreadMode() != mCurrentThreadMode) {
            throw new IllegalArgumentException("can't hot change iothread mode from " + mCurrentThreadMode + " to "
                    + mOkOptions.getIOThreadMode() + " in blocking io manager");
        }
    }

}
