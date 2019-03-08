package com.gzdb.sunmi.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import com.core.util.BaseUtils;
import com.gzdb.basepos.App;
import com.gzdb.supermarket.been.SettlementResultBean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Zxy on 2016/11/23.
 *
 *  结算--打印小票
 */

public class SettlementOnTicket {

    private static Context context;
    private static App app;
    public static void intClass(Activity activity){
        context = activity;
        app= (App) activity.getApplication();
    }

    public static void outcomeTacket(SettlementResultBean o){
        // 1: Get BluetoothAdapter
        BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
        if (btAdapter == null) {//请打开蓝牙!
            //商米开发测试请打开
            Toast.makeText(context, "请打开蓝牙打印小票!", Toast.LENGTH_LONG).show();
            return;
        }
        // 2: Get Sunmi's InnerPrinter BluetoothDevice
        BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
        if (device == null) {//没有蓝牙，请确保有蓝牙
            Toast.makeText(context, "请打开蓝牙打印小票!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 3: Generate a order data
        byte[] data = generateMockData(o);
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
    public static byte[] generateMockData(SettlementResultBean bean) {
        try {

            byte[] next2Line = ESCUtil.nextLine(2);//空两行
            byte[] boldOn = ESCUtil.boldOn();//加粗
            byte[] fontSize2Big = ESCUtil.fontSizeSetBig(3);//字体大小
            byte[] center = ESCUtil.alignCenter();//居中
            byte[] nextLine = ESCUtil.nextLine(1);//换行
            nextLine = ESCUtil.nextLine(1);//换一行
            next2Line = ESCUtil.nextLine(2);//换两行
            byte[] boldOff = ESCUtil.boldOff();//取消加粗
            byte[] fontSize2Small = ESCUtil.fontSizeSetSmall(3);//字体取消倍宽倍高
            byte[] left = ESCUtil.alignLeft();//左对齐
            byte[] right = ESCUtil.alignRight();//右对齐
            byte[] positionRight = ESCUtil.set_HT_position((byte)1);//右对齐
            boldOn = ESCUtil.boldOn();//加黑
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);//字体变大为标准的n倍
            boldOff = ESCUtil.boldOff();//取消加粗模式
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);//字体取消倍宽倍高
            byte[] next4Line = ESCUtil.nextLine(5);//换4行
//            byte[] breakPartial = ESCUtil.feedPaperCutPartial();//订单切完后再留一点


            String title = app.getCurrentUser().getShowName();
            byte[] titleName = title.getBytes("gb2312");
            String tag = "订单类型\t\t"+"订单数量\t"+"收入";
            byte[] tagName = tag.getBytes("gb2312");
            byte[] tagOne = "------------------------------------------------".getBytes("gb2312");

            List<byte[]> bs = new LinkedList<>();
            List<byte[]> titles = Arrays.asList(fontSize1Big,boldOn,center,titleName,
                    boldOff,fontSize1Small, nextLine,
                    boldOff,fontSize1Small, nextLine, left,tagOne,
                    boldOff,fontSize1Small, nextLine,
                    boldOff,fontSize1Small, nextLine, left,tagName,
                    boldOff,fontSize1Small, nextLine
            );

            bs.addAll(titles);



                List<byte[]> itmes  =  new LinkedList<>();
                for (int i = 0; i < bean.getDatas().size();i++){
                    SettlementResultBean.DatasBean datasBean = bean.getDatas().get(i);

                    String itemName =datasBean.getPaymentTypeTitle();
                    String Quantity = datasBean.getCount()+"";//订单数量
                    String Income = datasBean.getAmount()+"";//订单金额


                    String  text = itemName+"\t\t\t"+Quantity+"\t\t"+Income;
                    byte[] itemTagName = text.getBytes("gb2312");
                    itmes.addAll( Arrays.asList(
                            fontSize1Small, nextLine, left,itemTagName));
                };
                bs.addAll(itmes);


            String ToPrice = "合计：  \t\t "+bean.getTotalNum()+"            ￥"+bean.getTotalAmount();
            byte[] ToPriceName = ToPrice.getBytes("gb2312");

            String CashMoney = "清点现金：\t\t\t\t￥"+bean.getCashAmount();
            byte[] CashMoneyName = CashMoney.getBytes("gb2312");

            String cashierName = "营业员名字："+app.getCurrentUser().getRealName();
            byte[] cashierNameName = cashierName.getBytes("gb2312");

            String endTime = "结算时间： "+ BaseUtils.convertToStrSS(System.currentTimeMillis());
            byte[] endTimeName = endTime.getBytes("gb2312");

            String desc = "备注： ";
            byte[] descName = desc.getBytes("gb2312");

            List<byte[]> flooter = Arrays.asList(
                    boldOff,fontSize1Small, nextLine, left,tagOne,
                    boldOff,fontSize1Small, nextLine, left, ToPriceName,
                    boldOff,fontSize1Small, nextLine, left,CashMoneyName,
                    boldOff,fontSize1Small, nextLine, left,tagOne,
                    boldOff,fontSize1Small, nextLine,
                    boldOff,fontSize1Small, nextLine, left,cashierNameName,
                    boldOff,fontSize1Small, nextLine, left,endTimeName,
                    boldOff,fontSize1Small, nextLine, left,descName,
                    next4Line);

            bs.addAll(flooter);

            return ESCUtil.byteMerger(bs.toArray(new byte[][]{}));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
