package com.one.pos.service.sunmi.aidl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;

import com.anlib.util.ApkUtils;
import com.anlib.util.QrCodeUtils;
import com.anlib.util.ToastUtils;
import com.one.pos.service.print.aidl.AidlPrintService;
import com.one.pos.service.sunmi.PrintComm;
import com.one.pos.service.print.EscPrintData;
import com.one.pos.service.sunmi.bluetooth.SunmiPrintStream;

import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * 商米机, 以 aidl 方式打印
 * 需要在 src/aidl 文件夹下 放 woyou/aidlservice/jiuiv5
 *
 * @author zhumg
 */
public class SunmiAidlPrint extends AidlPrintService<EscPrintData> {

    public SunmiAidlPrint(Context context) {
        super(context);
    }

    @Override
    protected void callPrint(EscPrintData printTask) {
        try {
            print((IWoyouService) aidlService, callback, printTask);
        } catch (Exception e) {
        }
    }

    private ICallback callback = new ICallback.Stub() {

        @Override
        public void onRunResult(boolean success) {
        }

        @Override
        public void onReturnString(final String value) {
        }

        @Override
        public void onRaiseException(int code, final String msg) {
        }

        @Override
        public void onPrintResult(int code, String msg) throws RemoteException {

        }
    };

    @Override
    protected void init() {
        try {
            if (aidlService != null) {
                Log.e("axa", "当前服务OK，不需要重新初始化");
                return;
            }
            Intent intent = new Intent();
            intent.setPackage("woyou.aidlservice.jiuiv5");
            intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
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
        return IWoyouService.Stub.asInterface(iBinder);
    }

    private void print(IWoyouService service, ICallback callback, EscPrintData entity) throws Exception {
        //次数
        int count = entity.getCount();
        for (int i = 0; i < count; i++) {
            List<PrintComm> printComms = entity.getPrintComms();
            for (int ik = 0; ik < printComms.size(); ik++) {
                PrintComm printData = printComms.get(ik);
                printData(service, callback, printData);
            }
            //切纸
            byte[] datas = new byte[4];
            datas[0] = SunmiPrintStream.GS;
            datas[1] = 86;
            datas[2] = 66;
            datas[3] = 0;
            service.sendRAWData(datas, callback);
        }
    }

    private void printData(IWoyouService service, ICallback callback, PrintComm printData) throws Exception {
        int align = printData.getAlign();
        service.setAlignment(align, callback);
        switch (printData.getCommType()) {
            case PrintComm.COMM_NULL_LINE:
                service.lineWrap(printData.getFontSize(), callback);
                break;
            case PrintComm.COMM_LOGO:
//                if (printLogo == null) {
//                    printLogo = SunmiPrintManager.getBitmap(context, R.drawable.food4u_print_logo);
//                }
//                if (printLogo != null) {
//                    service.printBitmap(printLogo, callback);
//                    service.lineWrap(1, callback);
//                }
                break;
            case PrintComm.COMM_VER:
                String ver = ApkUtils.getVersionName();
                service.printTextWithFont(ver, "", printData.getFontSize(),
                        callback);
                service.lineWrap(1, callback);
                break;
            case PrintComm.COMM_CODE:
                Bitmap bitmap = QrCodeUtils.createQRImage(printData.getText(), printData.getFontSize(), printData.getFontSize());
                if (bitmap != null) {
                    service.printBitmap(bitmap, callback);
                    service.lineWrap(1, callback);
                    bitmap.recycle();
                    bitmap = null;
                }
                break;
            case PrintComm.COMM_TEXT:
                service.printTextWithFont(printData.getText(), "", printData.getFontSize(),
                        callback);
                service.lineWrap(1, callback);
                break;
            default:
                break;
        }
    }

    public IWoyouService getWoyouService() {
        return (IWoyouService)this.aidlService;
    }

}
