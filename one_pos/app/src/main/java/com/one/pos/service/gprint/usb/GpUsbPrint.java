package com.one.pos.service.gprint.usb;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

import com.anlib.util.ToastUtils;
import com.one.pos.event.UsbStatusChangeEvent;
import com.one.pos.service.print.PrintThread;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * USB 连接打印机 / 佳博打印机
 *
 * @author zhumg
 */
public class GpUsbPrint extends PrintThread {

    public static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";

    private static final int TIME_OUT = 100000;

    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;

    private UsbEndpoint printerEp;

    //当前连接中的打印机设备
    private UsbDevice nowUsbDevice;

    //状态，-1连接异常，0未连接，1监听中，2连接中，10连接成功，100权限被拒绝
    private int status;

    //初始化
    @Override
    public void init(Context context) {
        super.init(context);
    }

    @Override
    protected void onConnect() {
        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            if (device.getInterface(0).getInterfaceClass() == 7) {
                connect(device);
            }
        }
    }

    @Override
    protected void onClose() {
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
        nowUsbDevice = null;
        printerEp = null;
        closeThread();
        //解绑
        updateStatus(1);
    }

    @Override
    protected void onPrint(byte[] datas) {
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.bulkTransfer(printerEp, datas, 0, datas.length);
        } else {
            ToastUtils.showEorr(context, "打印异常");
        }
    }

    //启动服务
    public void start() {
        if (status != 0) {
            return;
        }
        updateStatus(1);
        //开启监听，监听USB的插入，拨出情况
        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        context.registerReceiver(mUsbDeviceReceiver, filter);
        //查询当前已插入的USB情况
        onConnect();
    }

    //释放
    public void destroy() {
        onClose();
        context.unregisterReceiver(mUsbDeviceReceiver);
        mUsbManager = null;
        mPermissionIntent = null;
        updateStatus(0);
    }

    private void connect(UsbDevice usbDevice) {
        //找到，直接连接
        updateStatus(2);
        UsbInterface usbInterface = usbDevice.getInterface(0);
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            UsbEndpoint ep = usbInterface.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    if (!mUsbManager.hasPermission(usbDevice)) {
                        mUsbManager.requestPermission(usbDevice, mPermissionIntent);
                    }
                    printerEp = ep;
                    mUsbDeviceConnection = mUsbManager.openDevice(usbDevice);
                    if (mUsbDeviceConnection != null) {
                        mUsbDeviceConnection.claimInterface(usbInterface, true);
                        nowUsbDevice = usbDevice;
                        updateStatus(10);
                        ToastUtils.showSuccess(context, "已成功连接USB打印机");
                        startThread();
                        return;
                    }
                }
            }
        }
        updateStatus(1);
    }

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice mUsbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            //如果是打印机，才监听
            if (mUsbDevice == null || mUsbDevice.getInterface(0).getInterfaceClass() != 7) {
                return;
            }
            //接受了权限
            if (ACTION_USB_PERMISSION.equals(action)) {
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                        && mUsbDevice != null) {
                    //如果当前是1，马上连接
                    if (nowUsbDevice == null || status <= 1) {
                        connect(mUsbDevice);
                    }
                } else {
                    updateStatus(100);
                    ToastUtils.showEorr(context, "USB打印机 权限请求被拒绝");
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                //设备拨出
                ToastUtils.showEorr(context, "USB打印机 被拔出");
                onClose();
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                //设备插入
                ToastUtils.showToast(context, "USB打印机 设备已插入");
                //如果没有权限，则申请权限
                if (!mUsbManager.hasPermission(mUsbDevice)) {
                    mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
                } else {
                    //直接连接
                    if (nowUsbDevice == null || status <= 1) {
                        connect(mUsbDevice);
                    }
                }
            }
        }
    };

    public int getStatus() {
        return this.status;
    }

    private void updateStatus(int targetStatus) {
        int s = this.status;
        this.status = targetStatus;
        //状态改变，通知
        EventBus.getDefault().post(new UsbStatusChangeEvent(s, status));
    }

}
