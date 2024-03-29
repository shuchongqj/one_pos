package com.one.pos.service.print;

import com.anlib.util.LogUtils;
import com.anlib.util.StringUtils;
import com.one.pos.service.print.bluetooth.PrintData;
import com.one.pos.service.sunmi.PrintComm;
import com.one.pos.service.sunmi.bluetooth.SunmiPrintStream;

import java.util.List;

/**
 *
 * 服务器打印对象
 * 即，服务器发送该对象过来客户端进行打印
 * 目前是esc协议形式打印
 *
 * @author zhumg
 */
public class EscPrintData extends PrintData {

    //打印次数
    private int count;
    //打印指令列表
    private List<PrintComm> printComms;

    //数据
    private byte[] datas;

    public EscPrintData(List<PrintComm> printComms) {
        this.printComms = printComms;
        this.count = 1;
    }

    public EscPrintData(List<PrintComm> printComms, int count) {
        this.printComms = printComms;
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public byte[] getDatas() {
        if (datas == null) {
            datas = changeToData(printComms);
        }
        return datas;
    }

    public List<PrintComm> getPrintComms() {
        return printComms;
    }


    public static byte[] changeToData(List<PrintComm> printComms) {
//
//        byte[] next2Line = ESCUtil.nextLine(2);//空两行
//        byte[] boldOn = ESCUtil.boldOn();//加粗
//        byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);//字体大小
//        byte[] center = ESCUtil.alignCenter();//居中
//        byte[] nextLine = ESCUtil.nextLine(1);//换行
//        nextLine = ESCUtil.nextLine(1);//换一行
//        next2Line = ESCUtil.nextLine(2);//换两行
//        byte[] boldOff = ESCUtil.boldOff();//取消加粗
//        byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);//字体取消倍宽倍高
//        byte[] left = ESCUtil.alignLeft();//左对齐
//        byte[] right = ESCUtil.alignRight();//右对齐
//        byte[] positionRight = ESCUtil.set_HT_position((byte) 1);//右对齐
//        boldOn = ESCUtil.boldOn();//加黑
//        byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);//字体变大为标准的n倍
//        boldOff = ESCUtil.boldOff();//取消加粗模式
//        byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);//字体取消倍宽倍高
//        byte[] next4Line = ESCUtil.nextLine(4);//换4行
//            byte[] breakPartial = ESCUtil.feedPaperCutPartial();//订单切完后再留一点


//        try {
//            String title = "门店订单" + "  1550195636666";
//            byte[] titleName = title.getBytes("gb2312");
//            String order = "订单编号：2323232323232";
//            byte[] orderId = order.getBytes("gb2312");
//            String paymodes = "支付方式：1号生活扫码";
//            byte[] payModeName = paymodes.getBytes("gb2312");
//            String time = "订单时间：2018-12-12 10:12:12";//订单时间
//            byte[] orderTimeName = time.getBytes("gb2312");
//            byte[] tagOne = "------------------------------------------------".getBytes("gb2312");
//            byte[] itemTag = "商品明细                 数量     单价      小计".getBytes("gb2312");
//
//            List<byte[]> bs = new LinkedList<>();
//            List<byte[]> titles = Arrays.asList(fontSize1Big, boldOn, center, titleName,
//                    next2Line, left, boldOff, fontSize2Small, orderId,
//                    boldOff, fontSize1Small, nextLine, left, payModeName,
//                    boldOff, fontSize1Small, nextLine, left, orderTimeName,
//                    boldOff, fontSize1Small, nextLine, left, tagOne,
//                    boldOff, fontSize1Small, nextLine, left, itemTag);
//            bs.addAll(titles);
//            return ESCUtil.byteMerger(bs.toArray(new byte[][]{}));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }

        SunmiPrintStream outputStream = new SunmiPrintStream();

        //打印列表
        for (int i = 0; i < printComms.size(); i++) {
            PrintComm printComm = printComms.get(i);

            int _commType = printComm.getCommType();

            if (_commType == PrintComm.COMM_TEXT) {
                String text = printComm.getText();

                if (StringUtils.isEmpty(text)) {
                    LogUtils.info("打印异常：字符串为空，当前行为：" + i);
                    continue;
                }

                int _fontSize = Math.abs(printComm.getFontSize());
                outputStream.fontSize(_fontSize);

                //如果加粗有变，则添加
                boolean _bold = printComm.getFontSize() < 0;
                if (_bold) {
                    outputStream.boldOn();
                } else {
                    outputStream.boldOff();
                }

                //如果对齐方式有变，则添加
                outputStream.align(printComm.getAlign());

                //添加打印字符串
                try {
                    byte[] ds = text.getBytes("gb2312");
                    outputStream.writeBytes(ds);
                } catch (Exception ex) {
                    LogUtils.info("打印异常：字符串无法转换为 gb2312，当前行：" + i + "，字符串：" + text);
                }
                //打印完字符串，自动换一行
                outputStream.nullLine(1);
            } else if (_commType == PrintComm.COMM_NULL_LINE) {
                //强制空行
                outputStream.nullLine(printComm.getFontSize());
            } else if (_commType == PrintComm.COMM_CODE) {
                //打印二维码
            } else if (_commType == PrintComm.COMM_UNDER_LINE) {
                //点型行
                outputStream.underLine(printComm.getFontSize());
            }
        }

        //清除下划线
        outputStream.clearUnderLine();
        //空4行
        outputStream.nullLine(4);
        //最后切纸
        outputStream.clip();

        return outputStream.toDatas();
    }
}
