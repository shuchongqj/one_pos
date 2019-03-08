package com.gzdb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gzdb.basepos.R;
import com.gzdb.printer.DeviceConnFactoryManager;
import com.gzdb.printer.PrintUtils;

import java.util.Vector;

/**
 * Created by Administrator
 *
 * @author 猿史森林
 * Date: 2017/8/2
 * Class description:
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private int id = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()");
        setContentView(R.layout.activity_main);


    }


    /**
     * 发送标签
     */
    void sendLabel() {
        LabelCommand tsc = new LabelCommand();
        // 设置标签尺寸，按照实际尺寸设置
        tsc.addSize(60, 60);
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(0);
        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        // 设置原点坐标
        tsc.addReference(0, 0);
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON);
        // 清除打印缓冲区
        tsc.addCls();
        // 绘制简体中文
        tsc.addText(10, 0, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "Welcome to use SMARNET printer!");
        // 绘制图片

        tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        // 打印标签
        tsc.addPrint(1, 1);
        // 打印标签后 蜂鸣器响

        tsc.addSound(2, 100);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        PrintUtils.sendLabelPos(tsc);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }


    public void btnDisConn(View view) {
        sendLabel();
      //  mHandler.obtainMessage(CONN_STATE_DISCONN).sendToTarget();
    }





//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.e(TAG, "onDestroy()");
//        DeviceConnFactoryManager.closeAllPort();
//
//    }



}