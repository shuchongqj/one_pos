package com.gzdb.supermarket.util;

import com.core.util.ShareCodeUtils;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gzdb.basepos.App;
import com.gzdb.printer.PrintUtils;

/**
 * @author zhumg
 */
public class PrintUtil {

    public static void prints(String name, String code, double price, double vipPrice, String weight, String totalPrice, double discount) {
        double totalVipPrice = Arith.mul(vipPrice, Double.valueOf(weight));
        if (vipPrice <= 0) {
            vipPrice = price;
            totalVipPrice = Double.valueOf(totalPrice);
        }
        String flag = "";
        if (discount > 0 && discount < 1) {
            flag = "(" + (int) Arith.mul(discount, 10) + "折)";
        }
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文

        tsc.addText(5, 25, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
                name + flag);
        tsc.addText(5, 75, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "价格:" + totalPrice);
        tsc.addText(135, 75, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "会员价:" + totalVipPrice);
//        tsc.addText(230, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                weight);
//        tsc.addText(120, 165, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                price + "");
//        tsc.addText(420, 165, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                vipPrice + "");
//        tsc.addText(120, 240, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                Arith.mul(Double.valueOf(totalPrice), discount) + "");
//        tsc.addText(120, 315, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                Arith.mul(totalVipPrice, discount) + "");

        tsc.add1DBarcode(10, 110, LabelCommand.BARCODETYPE.CODE128, 85, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, code + "|" + Arith.mul(Double.valueOf(weight), 100) + "|" + discount);

//        tsc.addBitmap(280, 110, LabelCommand.BITMAP_MODE.OVERWRITE, 240, bitmap);
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        PrintUtils.sendLabelPos(tsc);
    }

    /**
     * 称重打印
     *
     * @param name
     * @param code
     * @param itemId
     * @param price
     * @param vipPrice
     * @param weight
     * @param totalPrice
     * @param discount
     */
    public static void print(String name, String code, long itemId, double price, double vipPrice, String weight, String totalPrice, double discount) {

        if (!PrintUtils.isPrint) {
            PrintUtils.initPrintParam(App.getContext());
        }
        double totalVipPrice = Arith.mul(vipPrice, Double.valueOf(weight));
        if (vipPrice <= 0) {
            vipPrice = price;
            totalVipPrice = Double.valueOf(totalPrice);
        }

        String flag = "";
        if (discount > 0 && discount < 1) {
            flag = "(" + (int) Arith.mul(discount, 10) + "折)";
        }

        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(40, 30);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(25);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
        // 设置坐标原点
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();

        int x = 300;
        int y = 10;

        int gap = 25;

        //打印店名
        tsc.addText(x, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                App.getInstance().getCurrentUser().getShowName());

        tsc.addText(x - gap, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                name);

        tsc.addText(x - gap * 3 + 10, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "单价(元)");

        tsc.addText(x - gap * 4 + 10, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                String.valueOf(price) + "/千克");

        tsc.addText(x - gap * 6 + 20, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "重量：" + weight);

        tsc.addText(x - gap * 8 + 30, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "总价：");

        tsc.addText(x - gap * 9 + 30, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                String.valueOf(Arith.mul(Double.valueOf(totalPrice), discount)));

        tsc.addText(x - gap * 11 + 30, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "会员价：");

        tsc.addText(x - gap * 12 + 30, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                String.valueOf(Arith.mul(Double.valueOf(totalVipPrice), discount)) + flag);

        //称重条码生成规则，重新计算
        String barcode = ShareCodeUtils.createPrintItemBarcode(String.valueOf(itemId), weight, discount);

        tsc.add1DBarcode(0, 160, LabelCommand.BARCODETYPE.CODE128, 30, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, barcode);

        tsc.addPrint(1, 1);
        // 打印标签
        tsc.addSound(2, 100);
        // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        // tsc.addCutter(EscCommand.ENABLE.ON);
        PrintUtils.sendLabelPos(tsc);

    }


//    public static void print(String name, String code, double price, double vipPrice, String weight, String totalPrice, double discount) {
//        if (!PrintUtils.isPrint) {
//         //   PrintUtils.disConn();
//            PrintUtils.initPrintParam(App.getContext());
//        }
//        double totalVipPrice = Arith.mul(vipPrice, Double.valueOf(weight));
//        if (vipPrice <= 0) {
//            vipPrice = price;
//            totalVipPrice = Double.valueOf(totalPrice);
//        }
//        String flag = "";
//        if (discount > 0 && discount < 1) {
//            flag = "(" + (int) Arith.mul(discount, 10) + "折)";
//        }
//        LabelCommand tsc = new LabelCommand();
//        // 设置标签尺寸，按照实际尺寸设置
//        tsc.addSize(40, 30);
//        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
//        tsc.addGap(5);
//        // 设置打印方向
//        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
//        // 设置坐标原点
//        tsc.addReference(0, 0);
//        // 撕纸模式开启
//        tsc.addTear(EscCommand.ENABLE.ON);
//        // 清除打印缓冲区
//        tsc.addCls();
//
//        tsc.addText(290, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "对米");
//        tsc.addText(260, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_90, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "花园店");
//
//        tsc.addSize(80, 50); // 设置标签尺寸，按照实际尺寸设置
//        tsc.addGap(25); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
//        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
//        tsc.addReference(0, 0);// 设置原点坐标
//        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
//        tsc.addCls();// 清除打印缓冲区
//        // 绘制简体中文
//        tsc.addText(230, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                name + flag);
//        tsc.addText(230, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                weight);
//        tsc.addText(120, 165, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                price + "");
//        tsc.addText(420, 165, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                vipPrice + "");
//        tsc.addText(120, 240, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                Arith.mul(Double.valueOf(totalPrice), discount) + "");
//        tsc.addText(120, 315, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
//                Arith.mul(totalVipPrice, discount) + "");
//        tsc.addQRCode(400, 220, LabelCommand.EEC.LEVEL_L, 6,
//                LabelCommand.ROTATION.ROTATION_0,
//                code + "_" + Arith.mul(Double.valueOf(weight), 100) + "_" + discount);
//        tsc.addBitmap(280, 110, LabelCommand.BITMAP_MODE.OVERWRITE, 240, bitmap);
//        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
//        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
//
//        PrintUtils.sendLabelPos(tsc);
//    }
}
