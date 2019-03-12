package com.one.pos.service.gprint.aidl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.anlib.util.ToastUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.one.pos.service.print.aidl.AidlPrintService;
import com.one.pos.service.print.TscPrintData;

/**
 * @author zhumg
 */
public class GpAidlPrint extends AidlPrintService<TscPrintData> {

    public GpAidlPrint(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        try {
            if (aidlService != null) {
                Log.e("axa", "当前服务OK，不需要重新初始化");
                return;
            }
            Intent i = new Intent(this.context, GpPrintService.class);
            context.startService(i);
            Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
            if (context.bindService(intent, conn, Context.BIND_AUTO_CREATE)) {
                Log.e("axa", "绑定服務OK");
                registerBroadcast();
            } else {
                ToastUtils.showToast(context, "無法初始化打印服務，请尝试重启应用，code=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("axa", "無法初始化打印服務!");
            ToastUtils.showToast(context, "無法初始化打印服務，请尝试重启应用，code=2");
        }
    }

    @Override
    protected IInterface createService(IBinder iBinder) {
        return GpService.Stub.asInterface(iBinder);
    }

    @Override
    protected void callPrint(TscPrintData tscPrintData) {
        int count = tscPrintData.getCount();
        byte[] datas = tscPrintData.getDatas();
        for (int i = 0; i < count; i++) {
            //((GpService)aidlService).sendLabelCommand(0, tscPrintData.getCommand().toString())
        }
    }

    public static final String ACTION_CONNECT_STATUS = "action.connect.status";

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        this.context.registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d("ax", "connect status " + type + ", id = " + id);
                if (type == GpDevice.STATE_CONNECTING) {

                } else if (type == GpDevice.STATE_NONE) {

                } else if (type == GpDevice.STATE_VALID_PRINTER) {

                } else if (type == GpDevice.STATE_INVALID_PRINTER) {

                }
            }
        }
    };

}
