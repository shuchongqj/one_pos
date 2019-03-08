package com.one.pos.service.gprint.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import com.anlib.util.ToastUtils;
import com.gprinter.aidl.GpService;
import com.gprinter.service.GpPrintService;
import com.one.pos.service.device.aidl.AidlPrintService;
import com.one.pos.service.sunmi.PrintTask;

/**
 * @author zhumg
 */
public class GpAidlPrint extends AidlPrintService<PrintTask> {

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
    protected void callPrint(PrintTask printTask) {

    }

}
