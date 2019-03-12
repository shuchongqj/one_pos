package com.one.pos.service.print.bluetooth;

/**
 * 蓝牙打印通用数据
 *
 * @author zhumg
 */
public abstract class PrintData {
    public abstract int getCount();
    public abstract byte[] getDatas();
}
