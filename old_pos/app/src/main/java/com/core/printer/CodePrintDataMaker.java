package com.core.printer;

import android.content.Context;

import com.core.printer.util.BarCodeUtil;
import com.gzdb.supermarket.been.GoodBean;

import java.util.ArrayList;
import java.util.List;
/**
 * 测试数据生成器
 * Created by Alex on 2016/11/10.
 */

public class CodePrintDataMaker implements PrintDataMaker {

    private Context context;
    private String qr;
    private int width;
    private int height;
    private GoodBean mData;

    public CodePrintDataMaker(Context context, String qr, int width, int height, GoodBean data) {
        this.context = context;
        this.qr = qr;
        this.width = width;
        this.height = height;
        this.mData = data;
    }

    @Override
    public List<byte[]> getPrintData(int type) {
        ArrayList<byte[]> data = new ArrayList<>();
        try {
            PrinterWriter printer;
            printer = type == PrinterWriter58mm.TYPE_58 ? new PrinterWriter58mm(height, width) : new PrinterWriter80mm(height, width);
            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            printer.setAlignCenter();
            printer.setEmphasizedOn();
            printer.setFontSize(1);
            printer.print(mData.getItemName());
            printer.setFontSize(0);
            printer.setEmphasizedOff();
            data.add(printer.getDataAndReset());
            printer.printLineFeed();
            printer.printLineFeed();
            printer.printLineFeed();
            data.addAll(printer.getImageByte(BarCodeUtil.creatBarcode(mData.getBarcode(),560,200)));
            printer.feedPaperCutPartial();
            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
