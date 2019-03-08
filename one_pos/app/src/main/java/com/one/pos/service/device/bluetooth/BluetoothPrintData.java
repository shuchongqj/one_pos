package com.one.pos.service.device.bluetooth;

/**
 * 蓝牙打印通用数据
 *
 * @author zhumg
 */
public abstract class BluetoothPrintData {
    public abstract int getCount();
    protected abstract byte[] getDatas();
}
