package com.gzdb.supermarket;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gzdb.printer.DeviceConnFactoryManager;
import com.gzdb.printer.PrintUtils;

import static com.gzdb.printer.DeviceConnFactoryManager.CONN_STATE_FAILED;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {

            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[PrintUtils.id] != null) {
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[PrintUtils.id].closePort(PrintUtils.id);
                }
                break;
            case DeviceConnFactoryManager.ACTION_CONN_STATE:
                int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        if (deviceId == PrintUtils.id) {
                            PrintUtils.isPrint = false;
                        }
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        //>连接状态：连接中 tvConnState.setText(getString(R.string.str_conn_state_connecting));
                        PrintUtils.isPrint = true;
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        //Utils.toast(mContext, "连接成功");
                        //已连接  tvConnState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                        PrintUtils.isPrint = true;
                        break;
                    case CONN_STATE_FAILED:
                        //  Utils.toast(MainActivity.this, getString(R.string.str_conn_fail));
                        //连接失败 tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                        PrintUtils.isPrint = false;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
