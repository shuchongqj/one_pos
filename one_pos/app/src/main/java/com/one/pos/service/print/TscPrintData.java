package com.one.pos.service.print;

import com.gprinter.command.LabelCommand;
import com.one.pos.service.print.bluetooth.PrintData;

import java.util.Vector;

/**
 * Tsc 协议形式打印
 *
 * @author zhumg
 */
public class TscPrintData extends PrintData {

    private int count;
    private LabelCommand command;
    private byte[] datas;

    public TscPrintData(int count, LabelCommand command) {
        this.command = command;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    public LabelCommand getCommand() {
        return command;
    }

    @Override
    public byte[] getDatas() {
        if (datas == null) {
            datas = labelCommandToBytes(this.command);
        }
        return datas;
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
}
