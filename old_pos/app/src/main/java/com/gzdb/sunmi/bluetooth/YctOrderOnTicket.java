package com.gzdb.sunmi.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.vaservice.bean.YctRecordDetail;
import com.gzdb.yct.util.DateUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zxy on 2016/11/23.
 * <p>
 * 订单--打印小票
 */

public class YctOrderOnTicket {

    private static Context context;
    private static YctRecordDetail yctRecordDetail;
    public static App app;

    public static void intClass(Activity activity) {
        context = activity;
        app = (App) activity.getApplication();
    }

    public static void outcomeTacket(YctRecordDetail yctRecordDetail,int type) {
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {//请打开蓝牙!
            //商米开发测试请打开
            ToastUtil.showToast(context, "打开蓝牙后才能打印小票！");
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {//没有蓝牙，请确保有蓝牙
            ToastUtil.showToast(context, "打开蓝牙后才能打印小票！");
            return;
        }
        // 3: Generate a order data
        byte[] data = generateMockData(yctRecordDetail,type);
        // 4: Using InnerPrinter print data
        BluetoothSocket socket = null;
        try {
            socket = BluetoothUtil.getSocket(device);
            BluetoothUtil.sendData(data, socket);
        } catch (IOException e) {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // --------------------
    public static byte[] generateMockData(YctRecordDetail yct,int type) {
        try {

            String tag = "";
            if (yct.getOrderStatus() == 2) {
                tag = " *";
            }

            byte[] next2Line = ESCUtil.nextLine(2);//空两行
            byte[] boldOn = ESCUtil.boldOn();//加粗
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(2);//字体大小
            byte[] center = ESCUtil.alignCenter();//居中
            byte[] nextLine = ESCUtil.nextLine(1);//换行
            nextLine = ESCUtil.nextLine(1);//换一行
            next2Line = ESCUtil.nextLine(2);//换两行
            byte[] boldOff = ESCUtil.boldOff();//取消加粗
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(4);//字体取消倍宽倍高
            byte[] left = ESCUtil.alignLeft();//左对齐
            byte[] right = ESCUtil.alignRight();//右对齐
            byte[] positionRight = ESCUtil.set_HT_position((byte) 1);//右对齐
            boldOn = ESCUtil.boldOn();//加黑
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);//字体变大为标准的n倍
            boldOff = ESCUtil.boldOff();//取消加粗模式
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall((byte) 3);//字体取消倍宽倍高
            byte[] next4Line = ESCUtil.nextLine(4);//换4行
//            byte[] fontSize3Small = ESCUtil.fontSizeSetSmall();//字体取消倍宽倍高

            byte[] title = "羊城通充值凭单".getBytes("gb2312");
            if(type==2){
                title = "羊城通充值凭单(重打)".getBytes("gb2312");
            }
            byte[] card_name = "羊城通卡号：".getBytes("gb2312");
            byte[] merchantName = ("商户名称：" + app.currentUser.getShowName()).getBytes("gb2312");
            byte[] merchantNumber = ("商户编号：" + yct.getMerchantNumber()).getBytes("gb2312");
            byte[] terminalNumber = ("终端编号：" + yct.getTerminalNumber()).getBytes("gb2312");
            byte[] psamNumber = ("PSAM卡号：" + yct.getPsam()).getBytes("gb2312");
            byte[] tranType;
            if (yct.getType() == 1) {
                tranType = "交易类型：普通充值".getBytes("gb2312");
            } else {
                tranType = "交易类型：预存金充值".getBytes("gb2312");
            }
            byte[] cardNumber = yct.getCardNumber().getBytes("gb2312");
            byte[] tranTime = ("交易时间：" + DateUtil.stampToDetailDateJava(yct.getCreateTime())).getBytes("gb2312");
            byte[] sysNo = ("系统流水：" + yct.getFlowNumber()).getBytes("gb2312");
            byte[] oMoney = ("原 余 额：" + yct.getPrebalance()).getBytes("gb2312");
            byte[] money = ("充值金额：" + yct.getAmount()).getBytes("gb2312");
            byte[] balance = ("现 余 额：" + yct.getBalance() + tag).getBytes("gb2312");
            byte[] tagOne = "------------------------------------------------".getBytes("gb2312");

            List<byte[]> bs = new LinkedList<>();
            List<byte[]> titles = Arrays.asList(fontSize1Big, boldOn, center, title,
                    next2Line, left, fontSize1Small, nextLine, left, tagOne,
                    boldOff, fontSize1Small, nextLine, left, card_name,
                    boldOff, fontSize2Big, nextLine, center, cardNumber,
                    boldOff, fontSize1Small, nextLine, left, merchantName,
                    boldOff, fontSize1Small, nextLine, left, merchantNumber,
                    boldOff, fontSize1Small, nextLine, left, terminalNumber,
                    boldOff, fontSize1Small, nextLine, left, psamNumber,
                    boldOff, fontSize1Small, nextLine, left, tranType,
                    boldOff, fontSize1Small, nextLine, left, tranTime,
                    boldOff, fontSize1Small, nextLine, left, sysNo,
                    boldOff, fontSize1Small, nextLine, left, oMoney,
                    boldOff, fontSize1Small, nextLine, left, money,
                    boldOff, fontSize1Small, nextLine, left, balance);
            bs.addAll(titles);

            String store = "店铺：" + app.getCurrentUser().getShowName();
            byte[] storeName = store.getBytes("gb2312");
            byte[] ds_one = "2.简单、方便、快捷、高效".getBytes("gb2312");
            byte[] ds_two = "1.1号生活，智能云POS系统".getBytes("gb2312");
            byte[] ds_three = "3.欢迎加盟1号生活\n".getBytes("gb2312");
            byte[] phone = "  加盟热线：4000-598-008".getBytes("gb2312");

            List<byte[]> flooter = Arrays.asList(
//                    boldOff, fontSize1Small, nextLine, left, tagOne,
//                    fontSize1Small, nextLine, left, ToPriceName,
//                    fontSize1Small, nextLine, left, cardName,
//                    fontSize1Small, nextLine, left, TotalPriceName,
//                    fontSize1Small, nextLine, left, actuallyPaidName,
//                    fontSize1Small, nextLine, left, changeName,
//                    boldOff, fontSize1Small, nextLine, left, tagOne,
//                    fontSize1Small, nextLine, left, storeName,
//                    fontSize1Small, nextLine, left,locationName,
                    boldOff, fontSize1Small, nextLine, left, tagOne,
                    fontSize1Small, nextLine, left, ds_two,
                    fontSize1Small, nextLine, left, ds_one,
                    fontSize1Small, nextLine, left, ds_three,
                    fontSize1Small, nextLine, left, phone,
                    next4Line);
            bs.addAll(flooter);

            return ESCUtil.byteMerger(bs.toArray(new byte[][]{}));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
