package com.gzdb.sunmi.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gzdb.printer.PrintUtils;
import com.gzdb.supermarket.been.FinishOrderData;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

public class BluetoothUtils {
    private static final UUID PRINTER_UUID = UUID.fromString("f05d1320-e336-11e8-9c88-0235d2b38928");

    public static String Innerprinter_Address = "00:11:22:33:44:55";

    public static Context mContext;


    public static BluetoothAdapter getBTAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        BluetoothDevice innerprinter_device = null;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(Innerprinter_Address)) {
                innerprinter_device = device;
                break;
            }
        }
        return innerprinter_device;
    }

    public static BluetoothSocket getSocket(BluetoothDevice device) throws IOException {
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(PRINTER_UUID);
        //socket.connect();


        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            socket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                socket = (BluetoothSocket) m.invoke(device, 1);
                socket.connect();
            } catch (Exception e) {
                Log.e("BLUE",e.toString());
                try{
                    socket.close();
                }catch (IOException ie){}
            }

        }
        return socket;
    }

    public static void sendData(byte[] bytes, BluetoothSocket socket) throws IOException {
        try {
            OutputStream out = socket.getOutputStream();
            out.write(bytes, 0, bytes.length);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static byte[] convertVectorByteToBytes(Vector<Byte> data) {
        byte[] sendData = new byte[data.size()];
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); ++i) {
                sendData[i] = (Byte) data.get(i);
            }
        }

        return sendData;
    }

    public static void initPrint() {
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = getBTAdapter();
        if (btAdapter == null) {//请打开蓝牙!
            //商米开发测试请打开
            Toast.makeText(mContext, "请打开蓝牙打印小票!", Toast.LENGTH_LONG).show();
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = getDevice(btAdapter);
        if (device == null) {//没有蓝牙，请确保有蓝牙
            Toast.makeText(mContext, "请打开蓝牙打印小票!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 3: Generate a order data

        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;



        try {
            socket = getSocket(device);

            // socket.connect();
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
}
