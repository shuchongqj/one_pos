package com.gzdb.yct.socket.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;
import java.util.HashMap;

import com.gzdb.yct.socket.sdk.ConnectionInfo;
import com.gzdb.yct.socket.sdk.bean.IPulseSendable;
import com.gzdb.yct.socket.sdk.bean.ISendable;
import com.gzdb.yct.socket.sdk.bean.OriginalData;
import com.gzdb.yct.socket.sdk.connection.abilities.IRegister;
import com.gzdb.yct.socket.sdk.connection.abilities.IStateSender;
import com.gzdb.yct.socket.sdk.connection.interfacies.ISocketActionListener;
import com.gzdb.yct.socket.utils.SocketBroadcastManager;

import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_CONNECTION_FAILED;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_CONNECTION_SUCCESS;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_DATA;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_DISCONNECTION;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_PULSE_REQUEST;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_READ_COMPLETE;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_READ_THREAD_SHUTDOWN;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_READ_THREAD_START;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_WRITE_COMPLETE;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_WRITE_THREAD_SHUTDOWN;
import static com.gzdb.yct.socket.sdk.connection.interfacies.IAction.ACTION_WRITE_THREAD_START;


/**
 * 状态机
 * Created by didi on 2018/4/19.
 */
public class ActionDispatcher implements IRegister, IStateSender {
    /**
     * 每个连接一个广播管理器不会串
     */
    private SocketBroadcastManager mSocketBroadcastManager;
    /**
     * 除了广播还支持回调
     */
    private HashMap<ISocketActionListener, BroadcastReceiver> mResponseHandlerMap = new HashMap<>();
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 连接信息
     */
    private ConnectionInfo mConnectionInfo;


    public ActionDispatcher(Context context, ConnectionInfo info) {
        mContext = context.getApplicationContext();
        mConnectionInfo = info;
        mSocketBroadcastManager = new SocketBroadcastManager(mContext);
    }

    @Override
    public void registerReceiver(BroadcastReceiver broadcastReceiver, String... action) {
        IntentFilter intentFilter = new IntentFilter();
        if (action != null) {
            for (int i = 0; i < action.length; i++) {
                intentFilter.addAction(action[i]);
            }
        }
        mSocketBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void registerReceiver(final ISocketActionListener socketResponseHandler) {
        if (socketResponseHandler != null) {
            if (!mResponseHandlerMap.containsKey(socketResponseHandler)) {
                BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        dispatchActionToListener(context, intent, socketResponseHandler);
                    }
                };
                registerReceiver(broadcastReceiver,
                        ACTION_CONNECTION_FAILED,
                        ACTION_CONNECTION_SUCCESS,
                        ACTION_DISCONNECTION,
                        ACTION_READ_COMPLETE,
                        ACTION_READ_THREAD_SHUTDOWN,
                        ACTION_READ_THREAD_START,
                        ACTION_WRITE_COMPLETE,
                        ACTION_WRITE_THREAD_SHUTDOWN,
                        ACTION_WRITE_THREAD_START,
                        ACTION_PULSE_REQUEST);
                synchronized (mResponseHandlerMap) {
                    mResponseHandlerMap.put(socketResponseHandler, broadcastReceiver);
                }
            }
        }
    }

    @Override
    public void unRegisterReceiver(BroadcastReceiver broadcastReceiver) {
        mSocketBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void unRegisterReceiver(ISocketActionListener socketResponseHandler) {
        synchronized (mResponseHandlerMap) {
            BroadcastReceiver broadcastReceiver = mResponseHandlerMap.get(socketResponseHandler);
            mResponseHandlerMap.remove(socketResponseHandler);
            unRegisterReceiver(broadcastReceiver);
        }
    }

    /**
     * 分发收到的响应
     *
     * @param context
     * @param intent
     * @param responseHandler
     */
    private void dispatchActionToListener(Context context, Intent intent, ISocketActionListener responseHandler) {
        String action = intent.getAction();
        switch (action) {
            case ACTION_CONNECTION_SUCCESS: {
                try {
                    responseHandler.onSocketConnectionSuccess(context, mConnectionInfo, action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_CONNECTION_FAILED: {
                try {
                    Exception exception = (Exception) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onSocketConnectionFailed(context, mConnectionInfo, action, exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_DISCONNECTION: {
                try {
                    Exception exception = (Exception) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onSocketDisconnection(context, mConnectionInfo, action, exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_READ_COMPLETE: {
                try {
                    OriginalData data = (OriginalData) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onSocketReadResponse(context, mConnectionInfo, action, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_READ_THREAD_START:
            case ACTION_WRITE_THREAD_START: {
                try {
                    responseHandler.onSocketIOThreadStart(context, action);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_WRITE_COMPLETE: {
                try {
                    ISendable sendable = (ISendable) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onSocketWriteResponse(context, mConnectionInfo, action, sendable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_WRITE_THREAD_SHUTDOWN:
            case ACTION_READ_THREAD_SHUTDOWN: {
                try {
                    Exception exception = (Exception) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onSocketIOThreadShutdown(context, action, exception);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ACTION_PULSE_REQUEST: {
                try {
                    IPulseSendable sendable = (IPulseSendable) intent.getSerializableExtra(ACTION_DATA);
                    responseHandler.onPulseSend(context, mConnectionInfo, sendable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void sendBroadcast(String action, Serializable serializable) {
        Intent intent = new Intent(action);
        intent.putExtra(ACTION_DATA, serializable);
        mSocketBroadcastManager.sendBroadcast(intent);
    }

    @Override
    public void sendBroadcast(String action) {
        sendBroadcast(action, null);
    }
}
