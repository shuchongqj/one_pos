package com.gzdb.sunmi.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.entity.PlaceOderData;
import com.gzdb.supermarket.util.Arith;

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

public class OrderOnTicket {

    private static Context context;
    private static List<ItemSnapshotsBean> listItem;
    private static PlaceOderData placeOderData;
    public static App app;

    public static void intClass(Activity activity) {
        context = activity;
        app = (App) activity.getApplication();
    }

    public static void outcomeTacket(FinishOrderData o) {
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
    public static byte[] generateMockData(FinishOrderData o) {
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
            byte[] positionRight = ESCUtil.set_HT_position((byte) 1);//右对齐
            boldOn = ESCUtil.boldOn();//加黑
            byte[] fontSize1Big = ESCUtil.fontSizeSetBig(2);//字体变大为标准的n倍
            boldOff = ESCUtil.boldOff();//取消加粗模式
            byte[] fontSize1Small = ESCUtil.fontSizeSetSmall(2);//字体取消倍宽倍高
            byte[] next4Line = ESCUtil.nextLine(4);//换4行
//            byte[] breakPartial = ESCUtil.feedPaperCutPartial();//订单切完后再留一点

            String orderMode = null;
//            if ("1".equals( o.getSaleType() )){
//                o.takeOrder();
//                orderMode = "外卖订单";
//            }else if ("2".equals(o.getSaleType())){
//                orderMode = "门店订单" ;
//            }
//            String orderNum =  o.getOrderNum();//订单排号
            String title = "门店订单" + "  " + o.getPaymentTime() / 1000;
            byte[] titleName = title.getBytes("gb2312");
            String order = "订单编号：" + o.getSequenceNumber();
            byte[] orderId = order.getBytes("gb2312");
            String paymodes = "支付方式：" + o.getTransType();
            byte[] payModeName = paymodes.getBytes("gb2312");
            String time = "订单时间：" + BaseUtils.convertToStrSS(o.getCreateTime());//订单时间
            byte[] orderTimeName = time.getBytes("gb2312");
            byte[] tagOne = "------------------------------------------------".getBytes("gb2312");
            byte[] itemTag = "商品明细\t\t数量  \t单价  \t小计".getBytes("gb2312");

            List<byte[]> bs = new LinkedList<>();
            List<byte[]> titles = Arrays.asList(fontSize1Big, boldOn, center, titleName,
                    next2Line, left, boldOff, fontSize2Small, orderId,
                    boldOff, fontSize1Small, nextLine, left, payModeName,
                    boldOff, fontSize1Small, nextLine, left, orderTimeName,
                    boldOff, fontSize1Small, nextLine, left, tagOne,
                    boldOff, fontSize1Small, nextLine, left, itemTag);
            bs.addAll(titles);

            listItem = o.getItemSnapshots();

            List<byte[]> itmes = new LinkedList<>();
            for (int i = 0; i < listItem.size(); i++) {
                ItemSnapshotsBean bean = listItem.get(i);
                String itemName = bean.getItemName();
                String itemPrice = bean.getNormalPrice() + "";
                String itemCount = bean.getNormalQuantity() + "";
                String itemTotalPrice = Arith.mul(bean.getNormalPrice(), bean.getNormalQuantity()) + "";
                if (bean.getItemType() == 2) {
                    itemName = itemName + " (" + Arith.div(bean.getNormalQuantity(), 100) + "千克)";
                    itemCount = "1";
                    itemTotalPrice = Arith.mul(bean.getNormalPrice(), Arith.div(bean.getNormalQuantity(), 100)) + "";
                }
                byte[] itemTagName = itemName.getBytes("gb2312");
                String itemSunPrice = itemCount + "  \t" + itemPrice + "  \t" + itemTotalPrice;
                byte[] itemSunPriceName = itemSunPrice.getBytes("gb2312");
                itmes.addAll(Arrays.asList(
                        fontSize1Small, nextLine, left, itemTagName,
                        fontSize1Small, nextLine, right, itemSunPriceName));
            }
            bs.addAll(itmes);

            String ToPrice = "原价合计：                               " + Arith.add(o.getTotalPrice(), o.getDiscountPrice());
            byte[] ToPriceName = ToPrice.getBytes("gb2312");
            String actuallyPaid = "实收金额：                               " + o.getActualPrice();
            byte[] actuallyPaidName = actuallyPaid.getBytes("gb2312");
            String card = "优惠合计：                               " + o.getDiscountPrice();
            byte[] cardName = card.getBytes("gb2312");
            String TotalPrice = "应收金额：                               " + o.getTotalPrice();
            byte[] TotalPriceName = TotalPrice.getBytes("gb2312");
            String change = "现金找零：                               " + o.getChange();
            byte[] changeName = change.getBytes("gb2312");


//            String merchantMobile = o.getMerchantMobile();//店铺电话
            String store = "店铺：" + app.getCurrentUser().getShowName();
            byte[] storeName = store.getBytes("gb2312");
//            String location = "地址："+o.getMerchantAddress();
//            byte[] locationName = location.getBytes("gb2312");
            byte[] ds_one = "2.简单、方便、快捷、高效".getBytes("gb2312");
            byte[] ds_two = "1.1号生活，智能云POS系统".getBytes("gb2312");
            byte[] ds_three = "3.欢迎加盟1号生活\n".getBytes("gb2312");
            byte[] phone = "  加盟热线：4000-598-008".getBytes("gb2312");

            List<byte[]> flooter = Arrays.asList(
                    boldOff, fontSize1Small, nextLine, left, tagOne,
                    fontSize1Small, nextLine, left, ToPriceName,
                    fontSize1Small, nextLine, left, cardName,
                    fontSize1Small, nextLine, left, TotalPriceName,
                    fontSize1Small, nextLine, left, actuallyPaidName,
                    fontSize1Small, nextLine, left, changeName,
                    boldOff, fontSize1Small, nextLine, left, tagOne,
                    fontSize1Small, nextLine, left, storeName,
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
