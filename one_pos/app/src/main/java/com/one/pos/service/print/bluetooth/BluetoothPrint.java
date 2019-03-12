package com.one.pos.service.print.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anlib.util.LogUtils;
import com.anlib.util.ToastUtils;
import com.one.pos.event.BluetoothStatusChangeEvent;
import com.one.pos.event.UsbStatusChangeEvent;
import com.one.pos.service.print.PrintThread;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 蓝牙打印，通用处理，进行打印
 *
 * @author zhumg
 */
public abstract class BluetoothPrint extends PrintThread {

    private BluetoothSocket socket;
    private UUID printUuid;
    private BluetoothDevice connDevice;
    //状态，-1连接异常，0未连接，1监听中，2连接中，10连接成功，100权限被拒绝
    private int status;
    private OutputStream out = null;

    private Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                socket = connDevice.createRfcommSocketToServiceRecord(printUuid);
                socket.connect();
                out = socket.getOutputStream();
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    Method m = connDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    socket = (BluetoothSocket) m.invoke(connDevice, 1);
                    socket.connect();
                    out = socket.getOutputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        updateStatus(0, 1);
                        onClose();
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }
                    return;
                }
            }
            if (socket.isConnected()) {
                startThread();
                return;
            }
        }
    };

    /**
     * @param context
     */
    public void init(Context context, UUID uuid) {
        super.init(context);
        this.printUuid = uuid;
    }

    protected abstract BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter);

    @Override
    protected void onConnect() {
        if (status != 0) {
            return;
        }
        //如果已连接跳出
        if (socket != null && socket.isConnected()) {
            return;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            ToastUtils.showEorr(context, "请打开蓝牙！");
            return;
        }
        //第二种打开方法 ，调用系统API去打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
            ToastUtils.showEorr(context, "请打开蓝牙！");
            return;
        }
        final BluetoothDevice device = getDevice(adapter);
        if (device == null) {
            ToastUtils.showEorr(context, "配对蓝牙失败！");
            return;
        }
        ToastUtils.showSuccess(context, "正在连接蓝牙：" + device.getAddress());
        updateStatus(1, 0);
        connDevice = device;
        //开启线程连接
        new Thread(this.connRunnable).start();
    }

    @Override
    protected void onClose() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            out = null;
        }
        closeThread();
    }

    @Override
    protected void onPrint(byte[] datas) {
        try {
            out.write(datas, 0, datas.length);
        } catch (Exception e) {
            e.printStackTrace();
            onClose();
        }
    }

    public int getStatus() {
        return this.status;
    }

    private void updateStatus(int targetStatus, int code) {
        this.status = targetStatus;
        //状态改变，通知
        EventBus.getDefault().post(new BluetoothStatusChangeEvent(this.status, code));
    }
}
