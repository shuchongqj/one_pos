package com.gzdb.printer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.bluetooth.BluetoothUtils;

import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;
import static com.gzdb.printer.Constant.ACTION_USB_PERMISSION;
import static com.gzdb.printer.DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE;
import static com.gzdb.printer.DeviceConnFactoryManager.CONN_STATE_FAILED;

public class PrintUtils {
    static ArrayList<String> per = new ArrayList<>();
    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;


    /**
     * ESC查询打印机实时状态指令
     */
    private static byte[] esc = {0x10, 0x04, 0x02};
    /**
     * CPCL查询打印机实时状态指令
     */
    private static byte[] cpcl = {0x1b, 0x68};
    /**
     * TSC查询打印机状态指令
     */
    private static byte[] tsc = {0x1b, '!', '?'};
    private static final int CONN_PRINTER = 0x12;
    private static String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    public static int id = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private static Context mContext;

    public static void initPrintParam(Context mContext) {
        PrintUtils.mContext = mContext;
        checkPermission();
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DEVICE_DETACHED);
        filter.addAction(ACTION_QUERY_PRINTER_STATE);
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        mContext.registerReceiver(receiver, filter);
        initStatus();
    }

    private static BluetoothAdapter mBluetoothAdapter;

    private static String getPrintCode() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            for (final BluetoothDevice device : pairedDevices) {

                if (device.getName().indexOf("InnerPrinter") == -1) {
                    return device.getAddress();
                }
                //  mPairedDevicesArrayAdapter.add(device.getName() + "\n"+ device.getAddress());
            }
        }
        return "";
    }

    /**
     * 蓝牙连接
     */
    public static void printCode() {

        String stampCode = SPUtil.getInstance().getString(mContext, "stamp_code");
        if (stampCode == null || stampCode.length() == 0) {
            if (getPrintCode() == null || getPrintCode().length() == 0) {
                //  ToastUtil.showEorr(mContext, "请选择打印机");
                return;
            } else {
                // stampCode = getPrintCode();
            }
        }

        String macAddress = stampCode;// "DC:1D:30:31:8D:5D";
        //初始化话DeviceConnFactoryManager
        new DeviceConnFactoryManager.Build()
                .setId(id)
                //设置连接方式
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                //设置连接的蓝牙mac地址
                .setMacAddress(macAddress)
                .build();
        //打开端口
        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();

    }

    private static void initStatus() {
        //    disConn();
        Handler myHandler = new Handler();
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isPrint) {
                        printCode();
                    }
                    //   LogUtil.d("ttttttt","88888888888888");
                    initStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10000);
    }


    private static void checkPermission() {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mContext, permission)) {
                per.add(permission);
            }
        }
    }


    public static void disConn() {
        try {
            mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendLabelPos(LabelCommand tsc) {
        tsc.addCutter(EscCommand.ENABLE.ON);
        Vector<Byte> vector = tsc.getCommand();
        if (vector.size() < 1) {
            return;
        }
        //如果使用USB打印
        if (GpUsbPrint.use) {
            byte[] sendData = null;
            sendData = new byte[vector.size()];
            for (int i = 0; i < vector.size(); ++i) {
                sendData[i] = (Byte) vector.get(i);
            }
            GpUsbPrint.getInstance().write(sendData);
        } else {
            // 发送数据
            if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null) {
                return;
            }
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(vector);
        }
    }


    public static boolean isPrint = false;

    private static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {

                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (id == deviceId) {
                                isPrint = false;
                                //  initStatus();
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            isPrint = true;
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            isPrint = true;
                            break;
                        case CONN_STATE_FAILED:
                            isPrint = false;
                            //  initStatus();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN:
                    if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                    }
                    break;
                case PRINTER_COMMAND_ERROR:
                    //     Utils.toast(MainActivity.this, getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER:
                    //   Utils.toast(MainActivity.this, getString(R.string.str_cann_printer));
                    break;

                default:
                    break;
            }
        }
    };


    public static void onDestroy() {
        try {
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
            DeviceConnFactoryManager.closeAllPort();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
