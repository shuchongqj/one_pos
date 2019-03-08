package com.zhumg.anlib;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.GlobalDataParam;
import com.zhumg.anlib.utils.ToastUtil;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by zhumg on 3/15.
 */
public abstract class AfinalActivity extends FragmentActivity {

    public static int top_bg_color_resid = 0;

    public abstract int getContentViewId();

    public abstract void initView(View view);

    protected Dialog loadingDialog;
    protected Context mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        mContent=AfinalActivity.this;
        int viewid = getContentViewId();
        View rootView = null;
        if (viewid != 0) {
            rootView = View.inflate(this, viewid, null);
            setContentView(rootView);
            ButterKnife.bind(this);
            initView(rootView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
    public void httpGets(Map map, String url, HttpCallback callback) {
        Http.get(this, map, url, callback, loadingDialog);
    }
    @Override
    public void finish() {
        super.finish();
        ActivityManager.removeActivity(this);
    }

    /**
     * 设置状态栏背景状态
     */
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        int resid = top_bg_color_resid;
        if (resid != 0) {
            tintManager.setStatusBarTintResource(resid);// 状态栏无背景
        }
    }

    //网络请求
    public void httpGet(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        map.put("platform","2");
        map.put("version","2.0");
        Http.get(this, map, url, callback, loadingDialog);
    }

    //POS
    public void httpPost(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
      //  ToastUtil.showToast(mContent, GlobalDataParam.accessToken);
        map.put("accessToken", GlobalDataParam.accessToken);
        map.put("passportId", GlobalDataParam.passportId);
        map.put("roleValue", GlobalDataParam.roleValue);
        map.put("showName", GlobalDataParam.showName);
        map.put("version", GlobalDataParam.versionstr);
        map.put("platform","2");
        map.put("device",DEVICE_ID);
        map.put("channel","0");
        Http.post(this, map, url, callback, loadingDialog);

    }

    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.createLoadingDialog(this, true, false);
        }

        loadingDialog.show();
    }

    protected void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
    protected  void setRight(View.OnClickListener clickListener){
        LinearLayout ll_right=(LinearLayout)findViewById(R.id.ll_right);
        if(ll_right!=null){
            ll_right.setVisibility(View.VISIBLE);
            ll_right.setOnClickListener(clickListener);
        }
    }
    protected  void  setBack(View.OnClickListener clickListener){
        LinearLayout ll_left=(LinearLayout)findViewById(R.id.ll_left);
        if(ll_left!=null){
            ll_left.setVisibility(View.VISIBLE);
            ll_left.setOnClickListener(clickListener);
        }

    }
    protected  void  setTitle(String str){
        TextView tv_title=(TextView)findViewById(R.id.tv_title);
        if(tv_title!=null){
            tv_title.setText(str);
        }
    }
    protected  void  setBack(View.OnClickListener clickListener, Drawable dw){
        ImageView img=(ImageView)findViewById(R.id.img_left);

        if(img!=null){
            img.setImageDrawable(dw);

        }
        LinearLayout ll_left=(LinearLayout)findViewById(R.id.ll_left);

        if(ll_left!=null){
            ll_left.setVisibility(View.VISIBLE);
            ll_left.setOnClickListener(clickListener);
        }

    }
}
