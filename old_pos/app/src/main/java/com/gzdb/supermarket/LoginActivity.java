package com.gzdb.supermarket;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.QRCodeUtil;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.jpush.TagAliasOperatorHelper;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.supermarket.been.LoginCodeBean;
import com.gzdb.supermarket.been.UserBean;
import com.gzdb.supermarket.entity.QRcodeLoginEvent;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.SharedPreferencesUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.tencent.bugly.Bugly;
import com.xw.repo.XEditText;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhumg on 7/15.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.phone_number)
    XEditText phoneNumber;
    @Bind(R.id.login_password)
    XEditText loginPassword;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.version)
    TextView version;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.tv_password_login)
    TextView tvPasswordLogin;
    @Bind(R.id.tv_scan_login)
    TextView tvScanLogin;
    @Bind(R.id.ll_password_login)
    LinearLayout llPasswordLogin;
    @Bind(R.id.ll_scan_login)
    RelativeLayout llScanLogin;
    @Bind(R.id.iv_qrcode)
    ImageView ivQrcode;

    private boolean canRefresh = true;

    Timer timer = new Timer();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Configuration config = getResources().getConfiguration();
        LogUtils.e("屏幕适配：" + config.smallestScreenWidthDp);
        //本地数据库调试地址
        LogUtils.e(DebugDB.getAddressLog());
        Sunmi.initSDK(LoginActivity.this);//初始化商米
        checkbox.setChecked(true);
        String username = SharedPreferencesUtil.getData("login_username");
        phoneNumber.setText(username);
        if (!username.equals("")) {
            phoneNumber.setSelection(username.length());
        }
        loginPassword.setText(SharedPreferencesUtil.getData("login_pwd"));
        getPersimmions();
        try {
            version.setText("版本号：" + getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            startService(new Intent(LoginActivity.this,MyService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //    startActivity(new Intent(LoginActivity.this, MyService.class));

    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName + "   " + versionName + "  " + versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //进入主界面
    private void enterMainActivity() {
        //直接跳去主activity
        Intent intent = new Intent(mContext, SupermarketIndexActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        verify();
    }


    public void verify() {
        if (TextUtils.isEmpty(phoneNumber.getText())) {
            ToastUtil.showToast(LoginActivity.this, "账号不能为空！");
            return;
        } else if (TextUtils.isEmpty(loginPassword.getText())) {
            ToastUtil.showToast(LoginActivity.this, "密码不能为空！");
            return;
        } else {
            login();
        }
    }

    public void login() {
        try {
            OkGo.<NydResponse<UserBean>>post(Contonts.URL_LOGIN)
                    .params("username", phoneNumber.getText().toString())
                    .params("password", loginPassword.getText().toString())
                    .params("clientType", 2)
                    .params("deviceType", 1)
                    .params("versionIndex", BaseUtils.getVersionCode(mContext))
                    .cacheMode(CacheMode.NO_CACHE)//2.0.6开始屏蔽掉离线登录功能
                    .execute(new DialogCallback<NydResponse<UserBean>>(mContext) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<NydResponse<UserBean>> response) {
                            try {
                                loginHandel(response.body().response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCacheSuccess(com.lzy.okgo.model.Response<NydResponse<UserBean>> response) {
                            super.onCacheSuccess(response);
                            app.setCurrentUser(response.body().response);
                            Bugly.setUserId(mContext, app.getCurrentUser().getPassportId());
                            ToastUtil.showToast(mContext, "无网络，启动离线登录！");
                            enterMainActivity();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginHandel(UserBean userBean) {
        try {
            app.setCurrentUser(userBean);
            Bugly.setUserId(mContext, app.getCurrentUser().getPassportId());
            HttpParams params = new HttpParams();
            params.put("passportId", app.getCurrentUser().getPassportId());
            params.put("merchantId", app.getCurrentUser().getPassportId());

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put("Authorization", app.getCurrentUser().getTokenType() + " " + app.getCurrentUser().getAccessToken());
            OkGo.getInstance().addCommonParams(params);
            OkGo.getInstance().addCommonHeaders(httpHeaders);
            TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
            tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
            tagAliasBean.alias = app.getCurrentUser().getPassportId();
            tagAliasBean.isAliasAction = true;
            TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), TagAliasOperatorHelper.sequence++, tagAliasBean);
            ToastUtil.showToast(mContext, "登录成功！");
            SPUtil.getInstance().putInt(mContext, "isCode", userBean.getIsCode());
            SPUtil.getInstance().putInt(mContext, "couponState", userBean.getCouponState());
            SPUtil.getInstance().putString(mContext, "pay_code", userBean.getCode());
            SPUtil.getInstance().putInt(mContext, "account_type", userBean.getAccountType());
            if (checkbox != null) {
                if (checkbox.isChecked()) {
                    SharedPreferencesUtil.saveData("login_username", phoneNumber.getText().toString());
                    SharedPreferencesUtil.saveData("login_pwd", loginPassword.getText().toString());
                } else {
                    SharedPreferencesUtil.saveData("login_username", "");
                    SharedPreferencesUtil.saveData("login_pwd", "");
                }
            }
            if (timer != null) {
                timer.cancel();
            }
            enterMainActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void qRcodeLoginEvent(QRcodeLoginEvent event) {
        LogUtils.e(event.getUserBean());
        loginHandel(event.getUserBean());
    }

    private final int REQUEST_CODE_SETTING = 101;


    public void getPersimmions() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                // TODO 相应代码。
                getPermisionAfter();
            } else if (requestCode == 101) {
                // TODO 相应代码。
                getPermisionAfter();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            AndPermission.defaultSettingDialog((Activity) mContext, REQUEST_CODE_SETTING)
                    .setTitle("权限申请失败")
                    .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                    .setPositiveButton("好，去设置")
                    .show();
        }
    };

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    public void getPermisionAfter() {
        LogUtils.e("取得权限");
        //初始化商米
        initAssets();
        initGalleryFinal();
    }

    private void initGalleryFinal() {
        // 设置主题
        ThemeConfig theme = new ThemeConfig.Builder().build();
        // 配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        // 配置ImageLoader
        ImageLoader imageloader = new PicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    @OnClick({R.id.tv_password_login, R.id.tv_scan_login, R.id.iv_qrcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_password_login:
                tvScanLogin.setBackgroundColor(getResources().getColor(R.color.img_full_opaque));
                tvPasswordLogin.setBackgroundResource(R.drawable.bg_login_select);
                tvScanLogin.setTextColor(getResources().getColor(R.color.gray2));
                tvPasswordLogin.setTextColor(getResources().getColor(R.color.deep_blue));
                llPasswordLogin.setVisibility(View.VISIBLE);
                llScanLogin.setVisibility(View.GONE);
                break;
            case R.id.tv_scan_login:
                getLoginCode();
                tvPasswordLogin.setBackgroundColor(getResources().getColor(R.color.img_full_opaque));
                tvScanLogin.setBackgroundResource(R.drawable.bg_login_select);
                tvPasswordLogin.setTextColor(getResources().getColor(R.color.gray2));
                tvScanLogin.setTextColor(getResources().getColor(R.color.deep_blue));
                llPasswordLogin.setVisibility(View.GONE);
                llScanLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_qrcode:
                getLoginCode();
                break;
        }
    }


    public static class PicassoImageLoader implements ImageLoader {

        private Bitmap.Config mConfig;

        public PicassoImageLoader() {
            this(Bitmap.Config.RGB_565);
        }

        public PicassoImageLoader(Bitmap.Config config) {
            this.mConfig = config;
        }

        @Override
        public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
            Glide.with(activity)
                    .load(new File(path))
                    .apply(new RequestOptions().placeholder(defaultDrawable).error(defaultDrawable).override(width, height).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {
            // TODO
        }
    }

    private void initAssets() {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            String fileNames[] = assetManager.list("custom_img");
            File file = new File(Environment.getExternalStorageDirectory().getPath());
            String sdFileNames[] = file.list();
            for (int i = 0; i < fileNames.length; i++) {
                boolean hasFile = false;
                for (String fileName : sdFileNames) {
                    if (fileName.equals(fileNames[i])) {
                        hasFile = true;
                        break;
                    }
                }
                if (hasFile) continue;
                inputStream = getClass().getClassLoader().getResourceAsStream("assets/custom_img/" + fileNames[i]);
                fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/" + fileNames[i]));
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                inputStream.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (timer != null) {
            timer.cancel();
        }
    }

    private void getLoginCode() {
        if (!canRefresh) {
            return;
        }
        OkGo.<NydResponse<LoginCodeBean>>post(Contonts.EQUIPMENT_NUMBER)
                .params("codeTypeKey", "10000").execute(new DialogCallback<NydResponse<LoginCodeBean>>(mContext) {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<LoginCodeBean>> response) {
                String keyStr = Contonts.BASE_URL + "?codetype=10000&conent=" + response.body().response.getKey();
                Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(keyStr, 1000, 1000);
                ivQrcode.setImageBitmap(bitmap);
                TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
                tagAliasBean.alias = response.body().response.getKey();
                tagAliasBean.isAliasAction = true;
                TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), TagAliasOperatorHelper.sequence++, tagAliasBean);

                timeOut();
                canRefresh = false;
            }
        });

    }

    private void timeOut() {
        timer.schedule(new TimerTask() {
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ivQrcode.setImageResource(R.mipmap.img_invalid);
                        canRefresh = true;
                    }
                });

            }
        }, 60000);
    }

}
