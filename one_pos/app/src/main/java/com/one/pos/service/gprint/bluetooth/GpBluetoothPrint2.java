package com.one.pos.service.gprint.bluetooth;

import android.content.Context;

import com.gprinter.command.LabelCommand;
import com.one.pos.service.device.bluetooth.BluetoothPrint;
import com.one.pos.service.device.bluetooth.BluetoothPrintData;
import com.one.pos.service.sunmi.bluetooth.SunmiBluetoothPrint;

import java.util.UUID;
import java.util.Vector;

/**
 * @author zhumg
 */
public class GpBluetoothPrint2 extends BluetoothPrint {

    private static final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public GpBluetoothPrint2(Context context) {
        super.init(context, SERIAL_PORT_SERVICE_CLASS_UUID, null, SunmiBluetoothPrint.INNERPRINTER_ADDRESS);
    }

    public void print(int count, LabelCommand labelCommand) {
        byte[] sendData = labelCommandToBytes(labelCommand);
        super.sendDatas(new BluetoothPrintDataImpl(count, sendData));
    }

    public static byte[] labelCommandToBytes(LabelCommand labelCommand) {
        Vector<Byte> vector = labelCommand.getCommand();
        byte[] sendData = null;
        if (vector.size() > 0) {
            sendData = new byte[vector.size()];
            for (int i = 0; i < vector.size(); ++i) {
                sendData[i] = (Byte) vector.get(i);
            }
        }
        if (sendData == null) {
            return null;
        }
        return sendData;
    }

    class BluetoothPrintDataImpl extends BluetoothPrintData {

        int count;
        byte[] datas;

        public BluetoothPrintDataImpl(int count, byte[] datas) {
            this.count = count;
            this.datas = datas;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setDatas(byte[] datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        protected byte[] getDatas() {
            return this.datas;
        }
    }
}
