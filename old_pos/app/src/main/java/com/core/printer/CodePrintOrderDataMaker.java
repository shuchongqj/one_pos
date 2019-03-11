package com.core.printer;

import android.content.Context;

import com.core.printer.util.BarCodeUtil;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.been.PrintBean;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Utils;
import com.hss01248.dialog.Util;

import java.util.ArrayList;
import java.util.List;

public class CodePrintOrderDataMaker implements PrintDataMaker {
    private Context context;
    private String qr;
    private int width;
    private int height;
    private PrintBean mData;

    public CodePrintOrderDataMaker(Context context, String qr, int width, int height, PrintBean data) {
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
            printer.setAlignCenter();
            printer.print("对米生鲜到家");
            printer.setFontSize(0);
            printer.printLineFeed();
            printer.printLineFeed();
            printer.print("--------------------1号生活--------------------");
            printer.printLineFeed();
            if(!"".equals(mData.getRemarks())) {
                printer.setFontSize(1);
                printer.print("备注：" + mData.getRemarks());
                printer.printLineFeed();
                printer.setFontSize(0);
                printer.print("------------------------------------------------");

                printer.printLineFeed();
            }
            printer.setAlignLeft();
            printer.print("当日排号：" + mData.getDaySortNumber());
            printer.printLineFeed();
            printer.print("订单号：  " + mData.getOrderSequenceNumber());
            printer.printLineFeed();
            printer.print("下单时间：" + mData.getCreateTime());
            printer.printLineFeed();
            printer.print("商家名称：" + mData.getShowName());
            printer.printLineFeed();
            printer.setAlignCenter();
            printer.print("-------------------收货人信息-------------------");
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.print("收货人： " + mData.getReceiptNickName());
            printer.printLineFeed();
            printer.print("电   话：" + mData.getReceiptPhone());
            printer.printLineFeed();
            printer.print("地   址：" + mData.getDistributionAddress());
//            printer.printLineFeed();
//            printer.print("备注留言：" + mData.getRemarks());
            printer.printLineFeed();
            printer.setAlignCenter();
            printer.print("--------------------商品信息--------------------");
            printer.printLineFeed();
            printer.setAlignLeft();
            printer.printInOneLine2("商品", "单价", "数量", "小计", 0);
            printer.printLineFeed();
            for (int i = 0; i < mData.getDetail().size(); i++) {

                String str=mData.getDetail().get(i).getItem_name();
                if(str.length()>10){
                    str=str.substring(0,9)+"...";
                }

                printer.printInOneLine2(str,    Utils.toYuanStr(mData.getDetail().get(i).getGroup_price()) + "", mData.getDetail().get(i).getNormal_quantity() + "", Utils.toYuanStr(mData.getDetail().get(i).getGroup_price()*mData.getDetail().get(i).getNormal_quantity()), 0);
            }
            printer.setAlignLeft();
            printer.printLineFeed();
            printer.print("实付金额：" +Utils.toYuanStr( mData.getTotalPrice()));
            printer.printLineFeed();
            printer.printLineFeed();
            printer.setAlignCenter();
            printer.print("------------------收货确认信息------------------");

//            for(int i=0;i<mData.getItemSnapshots().size();i++){
//                ItemSnapshotsBean bean = mData.getItemSnapshots().get(i);
//                String itemName = bean.getItemName();
//                String itemPrice = bean.getNormalPrice() + "";
//                String itemCount = bean.getNormalQuantity() + "";
//                String itemTotalPrice = Arith.mul(bean.getNormalPrice(), bean.getNormalQuantity()) + "";
//                if (bean.getItemType() == 2) {
//                    itemName = itemName + " (" + Arith.div(bean.getNormalQuantity(), 100) + "千克)";
//                    itemCount = "1";
//                    itemTotalPrice = Arith.mul(bean.getNormalPrice(), Arith.div(bean.getNormalQuantity(), 100)) + "";
//                }
//                printer.printInOneLine(itemName, "", 0);
//                printer.printLineFeed();
//                printer.printInOneLine2("", itemCount, itemPrice, itemTotalPrice, 0);
//                printer.printLineFeed();
//            }

            printer.setAlignCenter();
            data.add(printer.getDataAndReset());
            data.addAll(printer.getImageByte(BarCodeUtil.createQRImage(mData.getQrCode(), 400, 400)));
            printer.setEmphasizedOff();
            printer.printLineFeed();
            printer.printLineFeed();
            printer.feedPaperCutPartial();
            data.add(printer.getDataAndClose());
            return data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
