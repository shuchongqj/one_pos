package com.one.pos.service.gprint.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.anlib.util.LogUtils;
import com.gprinter.io.BluetoothPort;
import com.one.pos.service.print.bluetooth.BluetoothPrint;
import com.one.pos.service.sunmi.bluetooth.SunmiBluetoothPrint;

import java.util.Set;
import java.util.UUID;

/**
 * @author zhumg
 */
public class GpBluetoothPrint extends BluetoothPrint {

    //当前连接中的
    private BluetoothDevice nowDevice;
    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public void init(Context context) {
        super.init(context, SERIAL_PORT_SERVICE_CLASS_UUID);
    }

    @Override
    protected BluetoothDevice getDevice(BluetoothAdapter bluetoothAdapter) {
        nowDevice = null;
        BluetoothDevice targetDevice = null;
        Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        String localMac = "DC:1D:30:41:DD:22";
        for (BluetoothDevice device : deviceSet) {
            LogUtils.info("------> " + device.getAddress());
            //跳过商米打印机
            if (device.getAddress().equals(SunmiBluetoothPrint.INNERPRINTER_ADDRESS)) {
                continue;
            }
            //如果是本地保存过的地址，则直接连接
            if (localMac.equals(device.getAddress())) {
                nowDevice = device;
                return nowDevice;
            } else {
                if (targetDevice == null) {
                    targetDevice = device;
                }
            }
        }
        nowDevice = targetDevice;
        return nowDevice;
    }

    public BluetoothDevice getNowDevice() {
        return nowDevice;
    }
}
