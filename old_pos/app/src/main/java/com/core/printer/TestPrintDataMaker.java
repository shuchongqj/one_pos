package com.core.printer;

import android.content.Context;

import com.core.printer.util.FileUtils;
import com.core.printer.util.QRCodeUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Utils;
import com.gzdb.vip.VipCheckPhoneDialog;
import com.gzdb.vip.VipUserInfo;
import com.gzdb.vip.VipUserInfoDialog;
import com.zhumg.anlib.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 测试数据生成器
 * Created by Alex on 2016/11/10.
 */

public class TestPrintDataMaker implements PrintDataMaker {

    private Context context;
    private String qr;
    private int width;
    private int height;
    private FinishOrderData mData;
    private VipUserInfo vipUserInfo;

    public TestPrintDataMaker(Context context, String qr, int width, int height, FinishOrderData data) {
        this(context, qr, width, height, data, null);
    }

    public TestPrintDataMaker(Context context, String qr, int width, int height, FinishOrderData data, VipUserInfo vipUserInfo) {
        this.context = context;
        this.qr = qr;
        this.width = width;
        this.height = height;
        this.mData = data;
        this.vipUserInfo = vipUserInfo;
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
            printer.print("门店订单 " + System.currentTimeMillis());
            printer.printLineFeed();
            printer.setFontSize(0);
            printer.setEmphasizedOff();

            printer.setAlignLeft();
            printer.printLineFeed();

            printer.print("订单编号：" + mData.getSequenceNumber());
            printer.printLineFeed();

            printer.print("支付方式：" + mData.getTransType());
            printer.printLineFeed();

            printer.print("订单时间：" +
                    new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.getDefault())
                            .format(new Date(System.currentTimeMillis())));
            printer.printLineFeed();

            printer.printLine();
            printer.printLineFeed();

            printer.printInOneLine2("商品明细", "数量", "单价", "小计", 0);
            printer.printLineFeed();

            for (int i = 0; i < mData.getItemSnapshots().size(); i++) {
                ItemSnapshotsBean bean = mData.getItemSnapshots().get(i);
                String itemName = bean.getItemName();
                String itemPrice = bean.getNormalPrice() + "";
                String itemCount = bean.getNormalQuantity() + "";
                String itemTotalPrice = Arith.mul(bean.getNormalPrice(), bean.getNormalQuantity()) + "";
                if (bean.getItemType() == 2) {
                    itemName = itemName + " (" + Arith.div(bean.getNormalQuantity(), 100) + "千克)";
                    itemCount = "1";
                    itemTotalPrice = Arith.mul(bean.getNormalPrice(), Arith.div(bean.getNormalQuantity(), 100)) + "";
                }
                printer.printInOneLine(itemName, "", 0);
                printer.printLineFeed();
                printer.printInOneLine2("", itemCount, itemPrice, itemTotalPrice, 0);
                printer.printLineFeed();
            }

            printer.printLine();
            printer.printLineFeed();

            printer.printInOneLine("原价合计：", Arith.add(mData.getTotalPrice(), mData.getDiscountPrice()) + "", 0);
            printer.printLineFeed();
            printer.printInOneLine("优惠合计：", mData.getDiscountPrice() + "", 0);
            printer.printLineFeed();
            printer.printInOneLine("应收金额：", mData.getTotalPrice() + "", 0);
            printer.printLineFeed();
            printer.printInOneLine("实收金额：", mData.getActualPrice() + "", 0);
            printer.printLineFeed();
            printer.printInOneLine("现金找零：", mData.getChange() + "", 0);
            printer.printLineFeed();
            printer.printLine();
            printer.printLineFeed();
            printer.print("店铺：" + App.getInstance().getCurrentUser().getShowName());
            printer.printLineFeed();
            printer.printLine();
            //1代表 VIP 会员
            if (vipUserInfo != null && "1".equals(vipUserInfo.getPay_member_type())) {
                printer.printLineFeed();
                String phone = VipUserInfoDialog.toStr(vipUserInfo.getPhone_number());
                if (phone.length() == 11) {
                    String p = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
                    printer.print("手机号：" + p);
                } else {
                    printer.print("手机号：" + phone);
                }
                printer.printLineFeed();
                int balance = vipUserInfo.getBalance() - (int) Arith.mul(mData.getActualPrice(), 100);
                printer.print("余额：" + Utils.toYuanStr(balance));
                printer.printLineFeed();
                printer.print("会员到期时间：" + VipUserInfoDialog.toStr(vipUserInfo.getPay_member_time()));
                printer.printLineFeed();
            }

            printer.printLineFeed();
            printer.print("1.1号生活，智能云POS系统");
            printer.printLineFeed();
            printer.print("2.简单、方便、快捷、高效");
            printer.printLineFeed();
            printer.print("3.欢迎加盟1号生活");
            printer.printLineFeed();
            printer.printLineFeed();

            printer.setAlignCenter();
            printer.print("加盟热线：4000-598-008");
            printer.printLineFeed();

            data.add(printer.getDataAndReset());

            printer.printLineFeed();
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
