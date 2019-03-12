package com.one.pos;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.anlib.GActivity;
import com.anlib.refresh.PtrLoader;
import com.anlib.refresh.RefreshLoaderListener;
import com.anlib.util.Arith;
import com.anlib.util.LogUtils;
import com.anlib.util.Utils;
import com.anlib.widget.GridViewWithHeaderAndFooter;
import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.one.pos.event.UsbStatusChangeEvent;
import com.one.pos.service.gprint.usb.GpUsbPrint;
import com.one.pos.service.print.TscPrintData;
import com.one.pos.service.print.bluetooth.DeviceEntity;
import com.one.pos.service.gprint.bluetooth.GpBluetoothPrint;
import com.one.pos.service.sunmi.PrintComm;
import com.one.pos.service.print.EscPrintData;
import com.one.pos.service.sunmi.aidl.SunmiAidlPrint;
import com.one.pos.service.sunmi.fscene.ScreenManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.util.PtrCLog;

/**
 * @author zhumg
 */
public class TestActivity extends GActivity {

    List<String> items;
    TestAdapter adapter;

    PtrClassicFrameLayout ptr;
    ListView listView;
    GridViewWithHeaderAndFooter gridView;

    PtrLoader ptrLoader;

    int itemIndex = 0;

    GpBluetoothPrint bluetoothPrint = null;

    List<DeviceEntity> entities;

    GpUsbPrint gpUsbPrint;

    TextView tvMessage;

    @Override
    protected int getContentViewId() {
        return R.layout.ptr_gridview;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView(View view) {

        EventBus.getDefault().register(this);

        PtrCLog.setLogLevel(PtrCLog.LEVEL_FATAL + 1);

        ptr = view.findViewById(R.id.l_ptr);
        //listView = view.findViewById(R.id.l_list_view);
        gridView = view.findViewById(R.id.l_grid_view);

        items = new ArrayList<>();
        adapter = new TestAdapter(this, items);

        ptrLoader = new PtrLoader(view, ptr, new RefreshLoaderListener() {
            @Override
            public void onLoading(boolean isLoadMore) {
                if (isLoadMore) {
                    getMoreDatas();
                } else {
                    getDatas();
                }
            }
        });

        //listView.setAdapter(adapter);
        gridView.setAdapter(adapter);

        bluetoothPrint = new GpBluetoothPrint();
        bluetoothPrint.init(this);

        View v = View.inflate(this, R.layout.test_head, null);
        v.findViewById(R.id.tv_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //商米打印
                SunmiAidlPrint aidlPrint = new SunmiAidlPrint(TestActivity.this);
                List<PrintComm> comms = new ArrayList<>();
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 18, 1, "门店订单 132123123123"));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 24, 0, "订单编号：23423423"));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 22, 1, "订单编号：23423423"));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 20, 0, "支付方式：1号生活扫码"));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 26, 0, "支付方式：1号生活扫码"));
                comms.add(new PrintComm(PrintComm.COMM_NULL_LINE, 2, 1, null));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 26, 0, "订单时间：2018-12-12 12:12:12"));
                comms.add(new PrintComm(PrintComm.COMM_TEXT, 26, 1, "--------------------"));
                aidlPrint.print(new EscPrintData(comms));

                ScreenManager screenManager = ScreenManager.getInstance();
                screenManager.init(TestActivity.this);

            }
        });
        v.findViewById(R.id.tv_message_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //蓝牙打印
                bluetoothPrint.print(new TscPrintData(1, sendLabelPos("商品名", "5234", 1, 4.3, 3.2, "1", "2.3", 3.3)));
            }
        });

        tvMessage = v.findViewById(R.id.tv_message_2);
        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (gpUsbPrint.getStatus() != 10) {
//                    //重新折腾
//                    if (gpUsbPrint.getStatus() == 100) {
//                        gpUsbPrint.checkNowUsbDevice();
//                        return;
//                    }
//                } else {
//                    LabelCommand command = sendLabelPos("商品名", "5234", 1, 4.3, 3.2, "1", "2.3", 3.3);
//                    gpUsbPrint.write(TscPrintData.labelCommandToBytes(command));
//                }
            }
        });
        gridView.addHeaderView(v);

        ptrLoader.showLoading();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                gpUsbPrint = new GpUsbPrint();
//                gpUsbPrint.init(TestActivity.this);
//                gpUsbPrint.start();
//            }
//        }, 1000);
    }


    boolean error = false;

    void getDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemIndex = 0;
                if (error) {
                    adapter.clearAndRefresh(null);
                    ptrLoader.refreshComplete(false, false, true);
                    return;
                }
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    datas.add("项 " + (itemIndex++));
                }
                adapter.clearAndRefresh(datas);
                ptrLoader.refreshComplete(true, false, false);
                error = true;
            }
        }, Utils.random(100, 500));
    }

    void getMoreDatas() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utils.random(0, 10) > 5) {
                    ptrLoader.refreshComplete(true, false, true);
                    return;
                }
                int count = Utils.random(0, 10) > 5 ? 20 : 5;
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    datas.add("项 " + (itemIndex++));
                }
                boolean hasMore = count == 20;
                LogUtils.info("hasMore: " + hasMore + ", " + count);
                adapter.addMoreAndRefresh(datas);
                ptrLoader.refreshComplete(hasMore, false, false);
            }
        }, Utils.random(100, 500));
    }


    LabelCommand sendLabelPos(String name, String code, long itemId, double price, double vipPrice, String weight, String totalPrice, double discount) {
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
                "自在城市花园");

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
        String barcode = "13213124215123";

        tsc.add1DBarcode(10, 160, LabelCommand.BARCODETYPE.CODE128, 30, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, barcode);

        tsc.addPrint(1, 1);
        // 打印标签
        tsc.addSound(2, 100);
        // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        return tsc;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void usbStatusChange(UsbStatusChangeEvent event) {
        String msg = "";
        int status = event.getStatus();
        if (status == 0) {
            msg = "未连接";
        } else if (status == 1) {
            msg = "监听中";
        } else if (status == 2) {
            msg = "连接中";
        } else if (status == 10) {
            msg = "已连接";
        } else {
            msg = "连接异常";
        }
        final String targetMsg = msg;
        tvMessage.post(new Runnable() {
            @Override
            public void run() {
                tvMessage.setText(targetMsg);
            }
        });

    }

}
