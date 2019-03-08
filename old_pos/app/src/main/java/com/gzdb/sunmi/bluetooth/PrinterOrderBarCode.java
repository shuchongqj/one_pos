package com.gzdb.sunmi.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import com.core.printer.CodePrintOrderDataMaker;
import com.core.printer.PrinterWriter80mm;
import com.gzdb.basepos.App;
import com.gzdb.supermarket.been.PrintBean;

import java.io.IOException;

public class PrinterOrderBarCode {

    private static Context context;
    private static App app;

    public static void intClass(Activity activity) {
        context = activity;
        app = (App) activity.getApplication();
    }

    public static void outcomeTacket(PrintBean o) {
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {//请打开蓝牙!
            //商米开发测试请打开
            Toast.makeText(context, "请打开蓝牙打印小票!", Toast.LENGTH_LONG).show();
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {//没有蓝牙，请确保有蓝牙
            try {
                Toast.makeText(context, "请打开蓝牙打印小票!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        // 3: Generate a order data
        byte[] data = generateMockData(o);
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(data, socket);
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // --------------------
    public static byte[] generateMockData(PrintBean bean) {
        return ESCUtil.byteMerger(new CodePrintOrderDataMaker(context, "", 500, 255, bean).getPrintData(PrinterWriter80mm.TYPE_80).toArray(new byte[][]{}));
    }

}
