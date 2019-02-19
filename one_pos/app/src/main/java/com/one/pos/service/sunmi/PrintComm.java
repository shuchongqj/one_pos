package com.one.pos.service.sunmi;

/**
 * 打印指令
 *
 * @author zhumg
 */
public class PrintComm {

    public static final int COMM_NULL_LINE = 0;
    public static final int COMM_LOGO = 1;
    public static final int COMM_VER = 2;
    public static final int COMM_CODE = 3;
    public static final int COMM_TEXT = 4;
    public static final int COMM_UNDER_LINE = 5;

    //指令类型
    int commType;
    //字体大小，如果为负数，则代表粗体，如果是换行，则代表换行数
    int fontSize;
    //对齐方式
    int align;
    //打印的文本内容
    String text;

    public PrintComm() {
    }

    public PrintComm(int commType, int fontSize, int align, String text) {
        this.commType = commType;
        this.fontSize = fontSize;
        this.align = align;
        this.text = text;
    }

    public int getCommType() {
        return commType;
    }

    public void setCommType(int commType) {
        this.commType = commType;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
