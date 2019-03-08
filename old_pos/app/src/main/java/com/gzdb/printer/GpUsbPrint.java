package com.gzdb.printer;

import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * USB 连接打印机 / 佳博打印机
 *
 * @author zhumg
 */
public class GpUsbPrint {

    public static final String ACTION_USB_PERMISSION = "com.usb.printer.USB_PERMISSION";

    private static GpUsbPrint mInstance;
    public static boolean use = false;

    private Context mContext;
    private PendingIntent mPermissionIntent;
    private UsbManager mUsbManager;
    private UsbDeviceConnection mUsbDeviceConnection;

    private UsbEndpoint ep, printerEp;
    private UsbInterface usbInterface;

    private static final int TIME_OUT = 100000;

    public static GpUsbPrint getInstance() {
        if (mInstance == null) {
            synchronized (GpUsbPrint.class) {
                if (mInstance == null) {
                    mInstance = new GpUsbPrint();
                }
            }
        }
        return mInstance;
    }

    public static void initPrinter(Context context) {
        getInstance().mContext = context;
    }

    public static void connPrinter() {
        if (getInstance().mUsbManager == null) {
            getInstance().initConn();
        }
        //直接马上连接
        List<DeviceEntity> devices = getInstance().getUsbDeviceEntitys();
        if (devices.size() > 0) {
            getInstance().connectUsbPrinter(devices.get(0));
        } else {
            ToastUtil.showEorr(getInstance().mContext, "无法找到USB打印设备");
        }
    }

    public static void closePrinter() {
        getInstance().close();
    }

    void initConn() {
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        mContext.registerReceiver(mUsbDeviceReceiver, filter);
    }

    //拿到所有 USB 硬件
    public List<DeviceEntity> getUsbDeviceEntitys() {

        List<DeviceEntity> deviceEntities = new ArrayList<>();

        // 列出所有的USB设备，并且都请求获取USB权限
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();

        StringBuilder sb = new StringBuilder();

        for (UsbDevice device : deviceList.values()) {
            usbInterface = device.getInterface(0);
            if (usbInterface.getInterfaceClass() == 7) {
                sb.delete(0, sb.length());
                DeviceEntity deviceEntity = new DeviceEntity(device.getProductName(), device.getManufacturerName());
                deviceEntities.add(deviceEntity);
                deviceEntity.setSrcDevice(device);
                sb.append("VendorId=").append(device.getVendorId()).append(", ")
                        .append("ProductId=").append(device.getProductId())
                        .append("DeviceId=").append(device.getDeviceId());
                deviceEntity.setInfo(sb.toString());

                Log.d("device", device.getProductName() + "     " + device.getManufacturerName());
                Log.d("device", device.getVendorId() + "     " + device.getProductId() + "      " + device.getDeviceId());
                Log.d("device", usbInterface.getInterfaceClass() + "");
            }
        }

        return deviceEntities;
    }

    private final BroadcastReceiver mUsbDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("action", action);
            UsbDevice mUsbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false) && mUsbDevice != null) {
                        Log.d("receiver", action);
                        _connectUsbPrinter(mUsbDevice);
                    } else {
                        ToastUtil.showToast(context, "USB设备请求被拒绝");
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                if (mUsbDevice != null) {
                    ToastUtil.showToast(context, "有设备拔出");
                }
            } else if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                ToastUtil.showToast(context, "有设备插入");
                if (mUsbDevice != null) {
                    if (!mUsbManager.hasPermission(mUsbDevice)) {
                        mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
                    }
                }
            }
        }
    };

    public void close() {
        if (mUsbDeviceConnection != null) {
            mUsbDeviceConnection.close();
            mUsbDeviceConnection = null;
        }
        mContext.unregisterReceiver(mUsbDeviceReceiver);
        mUsbManager = null;
    }

    public void connectUsbPrinter(DeviceEntity deviceEntity) {
        UsbDevice mUsbDevice = (UsbDevice) deviceEntity.getSrcDevice();
        //先变为0
        if (_connectUsbPrinter(mUsbDevice)) {
            deviceEntity.setStatus(10);
            return;
        }
        deviceEntity.setStatus(0);
    }

    private boolean _connectUsbPrinter(UsbDevice usbDevice) {
        for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
            ep = usbInterface.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    if (!mUsbManager.hasPermission(usbDevice)) {
                        mUsbManager.requestPermission(usbDevice, mPermissionIntent);
                    }
                    mUsbDeviceConnection = mUsbManager.openDevice(usbDevice);
                    printerEp = ep;
                    if (mUsbDeviceConnection != null) {
                        mUsbDeviceConnection.claimInterface(usbInterface, true);
                        ToastUtil.showToast(mContext, "设备已连接");
                        return true;
                    }
                }
            }
        }
        ToastUtil.showToast(mContext, "连接打印机失败");
        return false;
    }

    public void write(byte[] bytes) {
        if (mUsbDeviceConnection != null) {
            int b = mUsbDeviceConnection.bulkTransfer(printerEp, bytes, bytes.length, TIME_OUT);
            //ToastUtil.showToast(this.mContext, "成功打印：" + b + "字符");
        } else {
            handler.sendEmptyMessage(0);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToastUtil.showToast(mContext, "未发现可用的打印机");
        }
    };

}
