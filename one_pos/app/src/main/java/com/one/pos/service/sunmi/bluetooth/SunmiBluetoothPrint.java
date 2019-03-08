package com.one.pos.service.sunmi.bluetooth;

import android.content.Context;

import com.one.pos.service.device.bluetooth.BluetoothPrint;

import java.util.UUID;

/**
 *
 * 商米蓝牙打印
 * esc 协议打印
 *
 * @author zhumg
 */
public class SunmiBluetoothPrint extends BluetoothPrint {

    private static final UUID SUNMI_UUID = UUID.fromString("00001125-0000-1000-8000-00805F9B34FB");
    /** 商米POS打印机默认地址，名称 InnerPrinter */
    public static final String INNERPRINTER_ADDRESS = "00:11:22:33:44:55";

    public SunmiBluetoothPrint(Context context) {
        super.init(context, SUNMI_UUID, INNERPRINTER_ADDRESS, null);
    }

}
