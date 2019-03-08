package com.one.pos.service.sunmi.bluetooth;


/**
 * @author zhumg
 */
public class SunmiPrintStream {

    public static final byte ESC = 27;// 换码
    public static final byte FS = 28;// 文本分隔符
    public static final byte GS = 29;// 组分隔符
    public static final byte DLE = 16;// 数据连接换码
    public static final byte EOT = 4;// 传输结束
    public static final byte ENQ = 5;// 询问字符
    public static final byte SP = 32;// 空格
    public static final byte HT = 9;// 横向列表
    public static final byte LF = 10;// 打印并换行（水平定位）
    public static final byte CR = 13;// 归位键
    public static final byte FF = 12;// 走纸控制（打印并回到标准模式（在页模式下） ）
    public static final byte CAN = 24;// 作废（页模式下取消打印数据 ）

    private int index;
    private byte[] datas = new byte[1024];

    private void writeByte(byte b) {
        updateIndex(1);
        datas[index] = b;
        index += 1;
    }

    private void updateIndex(int dex) {
        int length = index + dex;
        if (datas.length < length) {
            //重新创建
            byte[] newDatas = new byte[datas.length + 1024];
            //拷贝
            System.arraycopy(datas, 0, newDatas, 0, index);
            //重新赋值
            datas = newDatas;
        }
    }

    public SunmiPrintStream underLine(int size) {
        writeByte(ESC);
        writeByte((byte) 45);
        writeByte((byte) size);
        return this;
    }


    public SunmiPrintStream clearUnderLine() {
        underLine(0);
        return this;
    }

    public SunmiPrintStream nullLine(int count) {
        for (int i = 0; i < count; i++) {
            writeByte(LF);
        }
        return this;
    }

    public SunmiPrintStream align(int align) {
        writeByte(ESC);
        writeByte((byte) 97);
        writeByte((byte) align);
        return this;
    }

    public SunmiPrintStream boldOn() {
        writeByte(ESC);
        writeByte((byte) 69);
        writeByte((byte) 0xF);
        return this;
    }

    public SunmiPrintStream boldOff() {
        writeByte(ESC);
        writeByte((byte) 69);
        writeByte((byte) 0);
        return this;
    }

    public SunmiPrintStream fontSize(int fontSize) {
        byte realSize = 0;
        if (fontSize == 1) {
            writeByte(ESC);
            writeByte((byte) 33);
            writeByte((byte) 0);
            return this;
        }
        switch (fontSize) {
            case 2:
                realSize = 17;
                break;
            case 3:
                realSize = 34;
                break;
            case 4:
                realSize = 51;
                break;
            case 5:
                realSize = 68;
                break;
            case 6:
                realSize = 85;
                break;
            case 7:
                realSize = 102;
                break;
            case 8:
                realSize = 119;
                break;
        }
        writeByte(ESC);
        writeByte((byte) 29);
        writeByte((byte) 33);
        writeByte(realSize);
        return this;
    }

    public SunmiPrintStream clip() {
        writeByte(GS);
        writeByte((byte) 86);
        writeByte((byte) 66);
        writeByte((byte) 0);
        return this;
    }

    public SunmiPrintStream writeBytes(byte[] ds) {
        updateIndex(ds.length);
        System.arraycopy(ds, 0, datas, index, ds.length);
        index += ds.length;
        return this;
    }

    public byte[] toDatas() {
        byte[] newDatas = new byte[index];
        System.arraycopy(datas, 0, newDatas, 0, index);
        return newDatas;
    }
}
