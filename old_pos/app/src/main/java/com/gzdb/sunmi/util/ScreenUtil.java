package com.gzdb.sunmi.util;

import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.basepos.App;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.ScreenManager;
import com.gzdb.screen.present.ImageDisplay;
import com.gzdb.screen.present.TextDisplay;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.data.DataModel;
import com.gzdb.sunmi.data.UPacketFactory;
import com.gzdb.supermarket.been.FinishOrderData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import sunmi.ds.DSKernel;
import sunmi.ds.callback.ICheckFileCallback;
import sunmi.ds.callback.ISendCallback;
import sunmi.ds.callback.ISendFilesCallback;
import sunmi.ds.data.DataPacket;

public class ScreenUtil {

    public static String SCREEN_TYPE = "SCREEN_TYPE";         //0 图集 1视频集
    public static String IMAGES = "IMAGES";
    public static String VIDEOS = "VIDEOS";

    private static DSKernel mDSKernel;//商米 SDK核心类

    private static ScreenUtil instance;

    public static ScreenUtil getInstance() {
        if (instance == null) {
            instance = new ScreenUtil();
            if (mDSKernel == null) {
                mDSKernel = DSKernel.newInstance();
            }
        }
        return instance;
    }

    public void setPicture(String filePath) {
        mDSKernel.sendFile(DSKernel.getDSDPackageName(), filePath, new ISendCallback() {

            @Override
            public void onSendSuccess(long taskId) {
                showPicture(taskId);
            }

            @Override
            public void onSendFail(int errorId, String errorInfo) {
            }

            @Override
            public void onSendProcess(long totle, long sended) {
            }
        });
    }

    public void showPicture(long taskId) {
        try {
            JSONObject json = new JSONObject();
            json.put("dataModel", "SHOW_IMG_WELCOME");
            json.put("data", "default");
            mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json.toString(), taskId, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPictures(int time, List<String> filePaths) {
        JSONObject json = new JSONObject();
        try {
            json.put("rotation_time", time); //幻灯片的切换时间，用毫秒计算，如果不传默认是10000毫秒
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDSKernel.sendFiles(DSKernel.getDSDPackageName(), json.toString(),
                filePaths, new ISendFilesCallback() {
                    public void onAllSendSuccess(long fileId) {
                        SPUtil.getInstance().putLong(App.getInstance(), IMAGES, fileId);
                        showPictures(fileId);
                    }

                    public void onSendSuccess(final String s, final long l) {
                    }

                    public void onSendFaile(int errorId, String errorInfo) {
                    }

                    public void onSendFileFaile(String path, int errorId, String errorInfo) {
                    }

                    public void onSendProcess(String path, long total, long sended) {
                    }
                });
    }

    public void showPictures(long fileId) {
        String json = UPacketFactory.createJson(DataModel.IMAGES, "");
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, fileId, null);
    }

    public void setVideo(String filePath) {
        mDSKernel.sendFile(DSKernel.getDSDPackageName(), filePath, new sunmi.ds.callback.ISendCallback() {
            @Override
            public void onSendSuccess(long l) {
                playVideo(l);
            }

            @Override
            public void onSendFail(int i, String s) {
            }

            @Override
            public void onSendProcess(final long l, final long l1) {
            }
        });
    }

    public void playVideo(long taskID) {
        String json = UPacketFactory.createJson(DataModel.VIDEO, "true"); //"true"视频续播；false视频从头播放
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskID, null);
    }

    public void setVideos(List<String> filePaths) {
        mDSKernel.sendFiles(DSKernel.getDSDPackageName(), "", filePaths, new ISendFilesCallback() {

            @Override
            public void onAllSendSuccess(long fileid) {
                SPUtil.getInstance().putLong(App.getInstance(), VIDEOS, fileid);
                playVideos(fileid);
            }

            @Override
            public void onSendSuccess(String path, long taskId) {
            }

            @Override
            public void onSendFaile(int errorId, String errorInfo) {
            }

            @Override
            public void onSendFileFaile(String path, int errorId, String errorInfo) {
            }

            @Override
            public void onSendProcess(String path, long totle, long sended) {
            }
        });
    }

    public void playVideos(long taskID) {
        String json = UPacketFactory.createJson(DataModel.VIDEOS, "true"); //第二个参数true：视频续播 false：视频从头播放
        mDSKernel.sendCMD(DSKernel.getDSDPackageName(), json, taskID, null);
    }

    public void showScreen(ImageDisplay imageDisplay) {
        if (Build.MODEL.equals("S2") && imageDisplay != null) {
            try{
                imageDisplay.show();
            }catch (Exception e){
            }
        }
        int type = SPUtil.getInstance().getInt(App.getInstance(), SCREEN_TYPE, 0);
        if (type == 0) {
            long taskId = SPUtil.getInstance().getLong(App.getInstance(), IMAGES);
            showPictures(taskId);
        } else if (type == 1) {
            long taskId = SPUtil.getInstance().getLong(App.getInstance(), VIDEOS);
            playVideos(taskId);
        }
    }

    public void waitShowScreen(final ImageDisplay imageDisplay) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                showScreen(imageDisplay);
            }
        }, 3000);
    }

    public void showAllPrice(String title) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DataPacket packet = UPacketFactory.buildShowText(
                DSKernel.getDSDPackageName(), jsonObject.toString(), null);
        mDSKernel.sendData(packet);//调用sendData方法发送文本
    }

    public void payActvity(FinishOrderData o, TextDisplay textDisplay) {
        String str;
        if (o.getChange() <= 0) {
            str = "应收：￥" + o.getTotalPrice()
                    + "      实收：￥" + o.getActualPrice();
        } else {
            //在副屏显示
            str = "应收：￥" + o.getTotalPrice()
                    + "      实收：￥" + o.getActualPrice()
                    + "      找零：￥" + o.getChange();
        }
        if (Build.MODEL.equals("S2") && textDisplay != null) {
            try{
                textDisplay.show();
                textDisplay.update(str);
            }catch (Exception e){

            }
            return;
        }
        showAllPrice(str);

    }
}
