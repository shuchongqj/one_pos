package com.gzdb.yct.socket.impl.blockio.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.gzdb.basepos.App;
import com.gzdb.yct.socket.impl.exceptions.ReadException;
import com.gzdb.yct.socket.sdk.OkSocketOptions;
import com.gzdb.yct.socket.sdk.bean.OriginalData;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.connection.interfacies.IAction;
import com.gzdb.yct.socket.sdk.protocol.IHeaderProtocol;
import com.gzdb.yct.socket.utils.BytesUtils;
import com.gzdb.yct.socket.utils.SL;

/**
 * Created by xuhao on 2017/5/31.
 */

public class ReaderImpl extends AbsReader {

    private ByteBuffer mRemainingBuf;

    public ReaderImpl(InputStream inputStream, IStateSender stateSender) {
        super(inputStream, stateSender);
    }

    @Override
    public void read() throws RuntimeException {
        OriginalData originalData = new OriginalData();
        IHeaderProtocol headerProtocol = mOkOptions.getHeaderProtocol();
        ByteBuffer headBuf = ByteBuffer.allocate(0);
        headBuf.order(mOkOptions.getReadByteOrder());
        try {
            if (mRemainingBuf != null) {
                mRemainingBuf.flip();
                int length = Math.min(mRemainingBuf.remaining(), headerProtocol.getHeaderLength());
                headBuf.put(mRemainingBuf.array(), 0, length);
                if (length < 0) {
                    //there are no data left
                    mRemainingBuf = null;
                    for (int i = 0; i < 0 - length; i++) {
                        headBuf.put((byte) mInputStream.read());
                    }
                } else {
                    mRemainingBuf.position(0);
                }
            } else {
                for (int i = 0; i < headBuf.capacity(); i++) {
                    headBuf.put((byte) mInputStream.read());
                }
            }
            originalData.setHeadBytes(headBuf.array());
            if (OkSocketOptions.isDebug()) {
                SL.i("read head: " + BytesUtils.toHexStringForLog(headBuf.array()));
            }
//            int bodyLength = headerProtocol.getBodyLength(originalData.getHeadBytes(), mOkOptions.getReadByteOrder());

            int bodyLength = App.getInstance().getLength();
            SL.e("need read body length: " + bodyLength);
//            SL.e("need read body length: " + bodyLength2);
            if (OkSocketOptions.isDebug()) {
                SL.i("need read body length: " + bodyLength);
            }

            if (bodyLength > 0) {
                if (bodyLength > mOkOptions.getMaxReadDataMB() * 1024 * 1024) {
                    throw new ReadException("Need to follow the transmission protocol.\r\n" +
                            "Please check the client/server code.\r\n" +
                            "According to the packet header data in the transport protocol, the package length is " + bodyLength + " Bytes.");
                }
                ByteBuffer byteBuffer = ByteBuffer.allocate(bodyLength);
                byteBuffer.order(mOkOptions.getReadByteOrder());
                if (mRemainingBuf != null) {
                    int bodyStartPosition = mRemainingBuf.position();
                    int length = Math.min(mRemainingBuf.remaining(), bodyLength);
                    byteBuffer.put(mRemainingBuf.array(), bodyStartPosition, length);
                    mRemainingBuf.position(bodyStartPosition + length);
                    if (length == bodyLength) {
                        if (mRemainingBuf.remaining() > 0) {//there are data left
                            ByteBuffer temp = ByteBuffer.allocate(mRemainingBuf.remaining());
                            temp.order(mOkOptions.getReadByteOrder());
                            temp.put(mRemainingBuf.array(), mRemainingBuf.position(), mRemainingBuf.remaining());
                            mRemainingBuf = temp;
                        } else {//there are no data left
                            mRemainingBuf = null;
                        }
                        //cause this time data from remaining buffer not from channel.
                        originalData.setBodyBytes(byteBuffer.array());
                        mStateSender.sendBroadcast(IAction.ACTION_READ_COMPLETE, originalData);
                        return;
                    } else {//there are no data left in buffer and some data pieces in channel
                        mRemainingBuf = null;
                    }
                }
                readBodyFromChannel(byteBuffer);
                originalData.setBodyBytes(byteBuffer.array());
            } else if (bodyLength == 0) {
                originalData.setBodyBytes(new byte[0]);
                if (mRemainingBuf != null) {
                    //the body is empty so header remaining buf need set null
                    if (mRemainingBuf.hasRemaining()) {
                        ByteBuffer temp = ByteBuffer.allocate(mRemainingBuf.remaining());
                        temp.order(mOkOptions.getReadByteOrder());
                        temp.put(mRemainingBuf.array(), mRemainingBuf.position(), mRemainingBuf.remaining());
                        mRemainingBuf = temp;
                    } else {
                        mRemainingBuf = null;
                    }
                }
            } else if (bodyLength < 0) {
                throw new ReadException(
                        "this socket input stream is end of file read " + bodyLength + " ,we'll disconnect");
            }
            mStateSender.sendBroadcast(IAction.ACTION_READ_COMPLETE, originalData);
        } catch (Exception e) {
            ReadException readException = new ReadException(e);
            throw readException;
        }
    }

    private void readBodyFromChannel(ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.hasRemaining()) {
            try {
                byte[] bufArray = new byte[mOkOptions.getReadPackageBytes()];
                int len = mInputStream.read(bufArray);
                if (len < 0) {
                    break;
                }
                int remaining = byteBuffer.remaining();
                if (len > remaining) {
                    byteBuffer.put(bufArray, 0, remaining);
                    mRemainingBuf = ByteBuffer.allocate(len - remaining);
                    mRemainingBuf.order(mOkOptions.getReadByteOrder());
                    mRemainingBuf.put(bufArray, remaining, len - remaining);
                } else {
                    byteBuffer.put(bufArray, 0, len);
                }
            } catch (Exception e) {
                throw e;
            }
        }
        if (OkSocketOptions.isDebug()) {
            SL.e("read total bytes: " + BytesUtils.toHexStringForLog(byteBuffer.array()));
            SL.e("read total length:" + (byteBuffer.capacity() - byteBuffer.remaining()));
        }
        if((byteBuffer.capacity() - byteBuffer.remaining())==0){
            throw new ReadException(IAction.ACTION_SERVER_RETURN_NULL);
        }
    }

}
