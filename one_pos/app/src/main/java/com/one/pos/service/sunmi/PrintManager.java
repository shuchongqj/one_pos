package com.one.pos.service.sunmi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.anlib.util.ToastUtils;

import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhumg
 */
public class PrintManager implements Runnable {

    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String Innerprinter_Address = "00:11:22:33:44:55";

    private Context context;
    private LinkedBlockingQueue<PrintTask> queue = new LinkedBlockingQueue<PrintTask>();
    private boolean runing;
    private Thread thread;

    private BluetoothSocket socket;

    public PrintManager(Context c) {
        this.context = c;
    }

    protected void print(PrintTask task) {
        //检测蓝牙之类
        if (!checkBlue()) {
            return;
        }
        //添加到列表
        queue.offer(task);
        //起动
        if (!runing) {
            runing = true;
            //启动
            thread = new Thread(this);
            thread.start();
        }
    }

    protected void exit() {
        runing = false;
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Exception ex) {

            }
        }
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
        BluetoothDevice device = getDevice(adapter);
        try {
            socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
            socket.connect();
        } catch (Exception ex) {
            ex.printStackTrace();
            ToastUtils.show(context, "蓝牙连接失败，请检查！");
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
        BluetoothDevice innerprinterDevice = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(Innerprinter_Address)) {
                innerprinterDevice = device;
                break;
            }
        }
        return innerprinterDevice;
    }

    @Override
    public void run() {
        while (runing) {
            PrintTask task = null;
            try {
                Log.e("ax", "取数据");
                task = queue.take();
            } catch (Throwable ex) {
                _close();
                Log.e("ax", "退出线程");
                return;
            }
            byte[] datas = task.getPrintDatas();
            int count = task.getCount();
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
                    ToastUtils.show(context, "打印失败");
                }
            }
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
}
