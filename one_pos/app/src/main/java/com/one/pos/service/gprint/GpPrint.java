package com.one.pos.service.gprint;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.anlib.util.LogUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zhumg
 */
public class GpPrint implements Runnable {

    private Context context;
    private GpService gpService;
    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private int printStatus = 0;

    private LinkedBlockingQueue<GpPrintData> queue = new LinkedBlockingQueue<GpPrintData>();
    private boolean runing;
    private Thread thread;

    private int printerId;
    private Handler handler;
    private PrintErrorRunnable runnable;


    public GpPrint(Context c) {
        this.context = c;
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(c);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(false);
        }
        this.handler = new Handler();
        this.runnable = new PrintErrorRunnable(c, null);
    }

    private BroadcastReceiver printerStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                printerId = id;
                if (type == GpDevice.STATE_CONNECTING) {
                    //setProgressBarIndeterminateVisibility(true);
                    mPortParam[id].setPortOpenState(false);
                    printStatus = 0;
                    LogUtils.info("Gprinter 连接中");
                } else if (type == GpDevice.STATE_NONE) {
                    //setProgressBarIndeterminateVisibility(false);
                    mPortParam[id].setPortOpenState(false);
                    printStatus = -1;
                    LogUtils.info("Gprinter 连接失败");
                    //isPrint = false;
                    //btn_stamp_connect.setText("打印机连接失败");
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    //setProgressBarIndeterminateVisibility(false);
                    mPortParam[id].setPortOpenState(true);
                    printStatus = 1;
                    LogUtils.info("Gprinter 连接成功");
                    //isPrint = true;
                    //btn_stamp_connect.setText("打印机已连接");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    //setProgressBarIndeterminateVisibility(false);
                    printStatus = -1;
                }
            }
        }
    };

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //连接成功
            gpService = GpService.Stub.asInterface(iBinder);
            //如果未起动线程，则起动并打印

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            exit();
        }
    };

    public boolean isConnect() {
        return printStatus == 1;
    }

    public boolean isConnectError() {
        return this.printStatus == -1;
    }

    public boolean isConnecting() {
        return this.printStatus == 0;
    }

    void connent() {

        Intent intent = new Intent(context, GpPrintService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        context.registerReceiver(printerStatusBroadcastReceiver, filter);
    }

    public void exit() {
        if (gpService == null) {
            return;
        }
        gpService = null;
        //解绑服务
        try {
            context.unbindService(conn);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            context.unregisterReceiver(printerStatusBroadcastReceiver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //打印内容
    public void print(GpPrintData task) {
        if (gpService == null) {
            //连接
            connent();
        }
        //添加到列表
        queue.offer(task);
        startThread();
    }

    void startThread() {
        //起动
        if (!runing) {
            runing = true;
            //启动
            thread = new Thread(this);
            thread.start();
        }
    }

    public void prints(List<GpPrintData> datas) {
        if (gpService == null) {
            //连接
            connent();
        }
        for (int i = 0; i < datas.size(); i++) {
            queue.offer(datas.get(i));
        }
        //起动
        startThread();
    }

    @Override
    public void run() {
        while (runing) {
            GpPrintData task = null;
            try {
                Log.e("ax", "取数据");
                task = queue.take();
            } catch (Throwable ex) {
                runing = false;
                Log.e("ax", "退出线程");
                return;
            }
            //去打印
            Vector<Byte> datas = task.getDatas();
            byte[] bytes = GpUtils.ByteTo_byte(datas);
            String str = Base64.encodeToString(bytes, Base64.DEFAULT);
            int rel;
            try {
                rel = gpService.sendLabelCommand(printerId, str);
                final GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    runnable.setError(GpCom.getErrorText(r));
                    handler.post(runnable);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    static class PrintErrorRunnable implements Runnable {

        String error;
        Context c;

        public PrintErrorRunnable(Context c, String error) {
            this.c = c;
            this.error = error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public void run() {
            Toast.makeText(c, error, Toast.LENGTH_SHORT).show();
        }
    }

    private static GpPrint gpPrint = null;

    public static void init(Context context) {
        gpPrint = new GpPrint(context);
    }

    public static GpPrint getGpPrint() {
        return gpPrint;
    }
}
