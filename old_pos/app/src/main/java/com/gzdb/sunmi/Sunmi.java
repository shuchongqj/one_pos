package com.gzdb.sunmi;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.present.ImageDisplay;
import com.gzdb.screen.present.ImageMenuDisplay;
import com.gzdb.sunmi.data.DataModel;
import com.gzdb.sunmi.data.UPacketFactory;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.sunmi.util.SharedPreferencesSunmiUtil;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.cache.ShopCart;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.IConnectionCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.data.DataPacket;
/**
 * Created by Zxy on 2016/11/22.
 * 商米 工具类
 */

public class Sunmi {

    private static DSKernel mDSKernel;//商米 SDK核心类
    private static Activity context;
    private static long welcomeTaskId = -1;
    public static final String WELCOME_IMG_ID = "WELCOEME";
    public static final String PAY_CODE_IMG_ID = "PAY_CODE_IMG";
    public static final String POS_AD_ID = "POS_AD_ID";

    public static String welcomeImgName = "welcome.png";
    public static String welcomeImgPath = Environment.getExternalStorageDirectory().getPath() + "/" + welcomeImgName;//默认图片


    //pos机是否有副屏
    public static boolean viceScreenMode = false;

    //初始化dsk
    public static void initSDK(Activity activity) {
        try {
            context = activity;
            writeToSD(welcomeImgName);
            mDSKernel = DSKernel.newInstance();
            mDSKernel.init(context.getApplicationContext(), mConnCallback);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static DSKernel getDSKernel() {
        return mDSKernel;
    }

    // SDK链接状态回调
    private static IConnectionCallback mConnCallback = new IConnectionCallback() {
        @Override
        public void onDisConnect() {
            //判断连接
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(context, "连接断开了，请尝试重连", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onConnected(final ConnState state) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (state) {
                        case AIDL_CONN: //与本地service的连接畅通
//                            Toast.makeText(context, "与本地service的连接畅通", Toast.LENGTH_LONG).show();
                            break;
                        case VICE_SERVICE_CONN:
                            viceScreenMode = true;//Pos有副屏
                            Toast.makeText(context, "与副屏连接畅通", Toast.LENGTH_LONG).show();
                            //与副屏service连接畅通
                            long fileId = SharedPreferencesSunmiUtil.getLong(context, WELCOME_IMG_ID);
                            checkImg(fileId, WELCOME_IMG_ID, welcomeImgName);
                            break;
                        case VICE_APP_CONN:
                            //与副屏app连接畅通
                            break;
                    }

                }
            });
        }
    };

    /**
     * 检测某张图片，如果存在，则什么事也不发生，如果不存在，则重新写到sdcard
     */
    private static void checkImg(final long fileId, final String key, final String imgName) {
        mDSKernel.checkFileExist(fileId, new ICheckFileCallback() {
            @Override
            public void onCheckFail() {
                writeToSD(imgName);
            }

            @Override
            public void onResult(boolean b) {
                if (!b) {
                    SharedPreferencesSunmiUtil.put(context, key, -1);
                    if (writeToSD(imgName)) {
                        showWelcomeImage(welcomeImgPath);
                    }
                } else {
                    showWelcomeImage(welcomeImgPath);
                }
            }
        });
    }

    //将图片写到SD卡
    public static boolean writeToSD(final String imgName) {
        InputStream inputStream;
        String ppppp = Environment.getExternalStorageDirectory().getPath() + "/" + imgName;
        File file = new File(ppppp);
        try {
            if (file.exists()) {
//                ToastUtil.showToast(context, "图片在SD卡");
                return true;
            }
            inputStream = context.getClass().getClassLoader().getResourceAsStream("assets/" + imgName);
            FileOutputStream fos = new FileOutputStream(new File(ppppp));

            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            inputStream.close();

//            ToastUtil.showToast(context, "写图片OK");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showToast(context, e.toString());
            return false;
        }
    }


    //结算总金额
    public static void showAllPrice(String title) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket packet = UPacketFactory.buildShowText(
                DSKernel.getDSDPackageName(), jsonObject.toString(), callback);
        mDSKernel.sendData(packet);//调用sendData方法发送文本
    }

    /**
     * 发送消息的回调
     */
    public static ISendCallback callback = new ISendCallback() {

        @Override
        public void onSendFail(int arg0, String arg1) {
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //商米开发测试请打开
                    if (viceScreenMode) {
                        Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onSendProcess(long arg0, long arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSendSuccess(long arg0) {
            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
              /*      //商米开发测试请打开
                    Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();*/
                }
            });
        }
    };


    //根据模型，显示图到副屏
    private static void showImageTo(long taskId, DataModel model) {
        String json1 = UPacketFactory.createJson(model, "");
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json1, taskId, null);
    }

    //根据模型，显示图和数据 到副屏
    private static void showImageTo(String json, long taskId, DataModel model) {
        String json1 = UPacketFactory.createJson(model, json);
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json1, taskId, null);
    }

    //显示购物车图片，目前与欢迎图一致
    public static void showCardImg(double coupon,double vipSaleMoney,ImageMenuDisplay imageMenuDisplay) {
        String json = ShopCart.nowShopCart.rebuildData(coupon,vipSaleMoney);
        if (Build.MODEL.equals("S2")) {
            try {
                if (imageMenuDisplay != null && !imageMenuDisplay.isShow) {
                    imageMenuDisplay.show();
                    imageMenuDisplay.update(json);
                } else if (null != imageMenuDisplay) {
                    imageMenuDisplay.update(json);
                }
            }catch (Exception e){
            }

            return;
        }
        json = UPacketFactory.createJson(DataModel.SHOW_IMG_LIST, json);
        long taskId = SPUtil.getInstance().getLong(App.getInstance(), PAY_CODE_IMG_ID);
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskId, null);
    }

    public static void showScreenAd(ImageDisplay imageDisplay) {
        if(Build.MODEL.equals("S2")){
            try {
                imageDisplay.show();
            }catch (Exception e){
            }
        }
        Long imgId = SPUtil.getInstance().getLong(context, POS_AD_ID, 0);
        ScreenUtil.getInstance().showPicture(imgId);
    }

    //显示欢迎界面
    public static void showWelcomeImage(String sdCardImgPath) {
        welcomeTaskId = SharedPreferencesSunmiUtil.getLong(context, WELCOME_IMG_ID);
        if (welcomeTaskId != -1) {
            showImageTo(welcomeTaskId, DataModel.SHOW_IMG_WELCOME);
            return;
        }
        welcomeTaskId = mDSKernel.sendFile(DSKernel.getDSDPackageName(), sdCardImgPath, new ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                welcomeTaskId = l;
                SharedPreferencesSunmiUtil.put(context, WELCOME_IMG_ID, welcomeTaskId);
                showImageTo(welcomeTaskId, DataModel.SHOW_IMG_WELCOME);
            }

            @Override
            public void onSendFail(int i, String s) {
            }

            @Override
            public void onSendProcess(long l, long l1) {
            }
        });
    }

    public static void waitShowImages() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
//                    showScrollImgs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 6000);
    } //支付成功界面显示6秒跳到欢迎界面


    //副屏付款界面
    public static void payActvity(FinishOrderData o) {
        if (o.getChange() <= 0) {
            Sunmi.showAllPrice("应收：￥" + o.getTotalPrice()
                    + "      实收：￥" + o.getActualPrice());
        } else {
            //在副屏显示
            Sunmi.showAllPrice("应收：￥" + o.getTotalPrice()
                    + "      实收：￥" + o.getActualPrice()
                    + "      找零：￥" + o.getChange());
        }
    }


}
