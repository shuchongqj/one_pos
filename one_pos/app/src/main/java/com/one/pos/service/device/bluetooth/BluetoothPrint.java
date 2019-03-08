package com.one.pos.service.device.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.anlib.util.LogUtils;
import com.anlib.util.ToastUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 蓝牙打印，通用处理，只接受esc格式的 byte[] 进行打印
 *
 * @author zhumg
 */
public class BluetoothPrint implements Runnable {

    private List<BluetoothDevice> entities = new ArrayList<>();
    private LinkedBlockingQueue<BluetoothPrintData> queue = new LinkedBlockingQueue<BluetoothPrintData>();
    private Context context;
    private boolean runing;
    private Thread thread;

    private BluetoothSocket socket;
    private String targetMacName;
    private String passMacName;

    private BluetoothDevice nowDevice;

    private UUID printUuid;

    /**
     * @param context
     * @param targetMacName 指定连接的目标MAC地址
     * @param passMacName   指定需要过滤的目标MAC地址
     */
    public void init(Context context, UUID uuid, String targetMacName, String passMacName) {
        this.printUuid = uuid;
        this.context = context;
        this.targetMacName = targetMacName;
        this.passMacName = passMacName;
    }

    public void close() {
        runing = false;
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Exception ex) {

            }
        }
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public void sendDatas(BluetoothPrintData print) {
        //检测蓝牙之类
        if (!checkBlue()) {
            return;
        }
        //添加到列表
        queue.offer(print);
        //起动
        if (!runing) {
            runing = true;
            //启动
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        while (runing) {
            BluetoothPrintData printData = null;
            try {
                LogUtils.info("取数据");
                printData = queue.take();
            } catch (Throwable ex) {
                _close();
                LogUtils.info("退出线程");
                return;
            }
            byte[] datas = printData.getDatas();
            int count = printData.getCount();
            if (count < 1) {
                count = 1;
            }
            for (int i = 0; i < count; i++) {
                //打印
                try {
                    OutputStream out = socket.getOutputStream();
                    out.write(datas, 0, datas.length);
                    out.close();
                } catch (Exception ex) {
                    _close();
                    ToastUtils.show(context, "打印失败，重置蓝牙");
                    new Handler().postDelayed(new ResizeRunnable(printData), 100);
                }
            }
        }
    }

    class ResizeRunnable implements Runnable {

        private BluetoothPrintData datas;

        public ResizeRunnable(BluetoothPrintData datas) {
            this.datas = datas;
        }

        @Override
        public void run() {
            sendDatas(datas);
        }
    }

    void _close() {
        try {
            socket.close();
        } catch (Exception e) {

        }
        socket = null;
        runing = false;
        thread = null;
    }


    private boolean checkBlue() {
        if (socket != null && socket.isConnected()) {
            return true;
        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            ToastUtils.showToast(context, "打印需要蓝牙服务，请打开蓝牙！");
            return false;
        }
        //第二种打开方法 ，调用系统API去打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(intent);
            return false;
        }
        BluetoothDevice device = getDevice(adapter);
        if (device == null) {
            ToastUtils.showToast(context, "配对蓝牙失败 ！");
            return false;
        }
        try {
            socket = device.createRfcommSocketToServiceRecord(printUuid);
            socket.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtils.show(context, "蓝牙连接失败，请检查，code=1");
            try {
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                socket = (BluetoothSocket) m.invoke(device, 1);
                socket.connect();
            } catch (Exception e) {
                ToastUtils.show(context, "蓝牙连接失败，请检查，code=2");
                try {
                    socket.close();
                } catch (IOException ie) {
                    ToastUtils.show(context, "蓝牙连接失败，请检查，code=3");
                }
            }
            return false;
        }
        if (socket.isConnected()) {
            return true;
        }
        ToastUtils.show(context, "蓝牙连接失败，请检查！");
        socket = null;
        return false;
    }

    private BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        entities.clear();
        nowDevice = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices != null && devices.size() > 0) {
            if (targetMacName != null) {
                for (BluetoothDevice device : devices) {
                    if (device.getAddress().equals(targetMacName)) {
                        nowDevice = device;
                        entities.add(nowDevice);
                        break;
                    }
                }
            } else {
                for (BluetoothDevice device : devices) {
                    if (passMacName != null && device.getAddress().equals(passMacName)) {
                        continue;
                    }
                    if (nowDevice == null) {
                        nowDevice = device;//bluetoothAdapter.getRemoteDevice(device.getAddress());
                        entities.add(nowDevice);
                    } else {
                        entities.add(device);
                    }
                }
            }
        }
        return nowDevice;
    }

    //蓝牙监听需要添加的Action
    private IntentFilter makeFilters() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        return intentFilter;
    }

    /**
     * 返回
     *
     * @return
     */
    public List<DeviceEntity> getDevices() {
        return null;
    }
}
