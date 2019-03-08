package com.gzdb.quickbuy.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.event.QuickBuyEvent;
import com.gzdb.quickbuy.util.OneBuyRightViewLogic;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.util.DialogUtil;
import com.util.baidu.BaiduLocationManager;
import com.util.baidu.EditAddress;
import com.util.baidu.LocationAddress;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.gzdb.basepos.R.id.all_count_txt;
import static com.gzdb.basepos.R.id.all_price_txt;
import static com.gzdb.basepos.R.id.tv_quickbuy;

/**
 * Created by Zxy on 2017/1/5.
 * <p>
 * <主界面>一键采购
 */

public class QuickBuyActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.tv_quickbuy)
    TextView tvQuickbuy;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.tv_quickbuy_order)
    TextView tvQuickbuyOrder;
    @Bind(all_count_txt)
    TextView allCountTxt;
    @Bind(R.id.ly_totalNum)
    LinearLayout lyTotalNum;
    @Bind(R.id.ly_close)
    LinearLayout lyClose;
    @Bind(R.id.tv_service_money)
    TextView tvServiceMoney;
    @Bind(R.id.lyservice)
    LinearLayout lyservice;
    @Bind(R.id.all_price_show)
    TextView allPriceShow;
    @Bind(R.id.submit)
    Button submit;
    @Bind(all_price_txt)
    TextView allPriceTxt;
    ViewGroup rightView;
    OneBuyRightViewLogic nowRightView;//当前选中的
    LayoutInflater inflater;
    Dialog dialog;
    //一键采购
    OnebuyRightView2 onebuyRightView;
    //采购订单
    OrderView orderView;
    // 收货地址
    AddressView addressView;
    private int REQUEST_CODE_SETTING = 101;
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                // TODO 相应代码。
                //这里是定位，
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAddress();
                    }
                }, 2000);
            } else if (requestCode == 101) {
                // TODO 相应代码。
                BaiduLocationManager.init(mContext.getApplicationContext());
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickbuy);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

        getPersimmions();
    }

    //获取定位相关权限
    public void getPersimmions() {
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, listener);
    }

    public void loadAddress() {
        BaiduLocationManager.startLocation(new BaiduLocationManager.BaiduLocationNotify() {
            @Override
            public void callNotify(LocationAddress address) {
                //设置位置
                try {
                    onebuyRightView.setSginLocation(address);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        onebuyRightView = new OnebuyRightView2(this);//一键采购
        orderView = new OrderView(this);            //采购订单
        addressView = new AddressView(this); // 收货地址
        rightView = (ViewGroup) findViewById(R.id.rightView);
        inflater = LayoutInflater.from(this);
        dialog = DialogUtil.loadingDialog(this, "加载中...");
//        dialog.show();
        initOnclick();
        change(0);
    }

    public void change(int type) {
        if (nowRightView != null) {
            rightView.removeView(nowRightView.getView());
            nowRightView.onVisible();
        }
        if (type == 0) {
            nowRightView = onebuyRightView;
        } else if (type == 1) {
            nowRightView = orderView;
        } else if (type == 2) {
            nowRightView = addressView;
        }
        rightView.addView(nowRightView.getView());
        nowRightView.onVisible();
        //刷新界面
        rightView.requestLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_close:
                this.finish();
                break;
            case tv_quickbuy://一键采购
                change(0);
                getPagerLeft(0);
                //onebuyRightView.changeLeft(-1);
                break;
            case R.id.tv_quickbuy_order://采购订单
                change(1);
                getPagerLeft(1);
                orderView.getOrders(1);
                break;
            case R.id.tv_address:
                change(2);
                getPagerLeft(2);
                break;
            case R.id.submit://立即下单
                if (addressView.getAddressListSize() != 0) {
//                    ToastUtil.showToast(this, "立即下单");
                    onebuyRightView.submitBuy();
                } else {
                    ToastUtil.showToast(this, "请先设置收货地址");
                    change(2);
                    getPagerLeft(2);
                }
                break;
        }
    }

    public void initOnclick() {
        lyClose.setOnClickListener(this);
        tvQuickbuy.setOnClickListener(this);
        tvQuickbuyOrder.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    //改变左边类型背景颜色
    public void getPagerLeft(int select) {
        if (select == 0) {
            tvQuickbuy.setBackgroundResource(R.color.blue);
            tvQuickbuy.setTextColor(this.getResources().getColor(R.color.white));
            tvQuickbuyOrder.setTextColor(this.getResources().getColor(R.color.black));
            tvQuickbuyOrder.setBackgroundResource(R.color.white);
            tv_address.setBackgroundResource(R.color.white);
            tv_address.setTextColor(this.getResources().getColor(R.color.black));
        } else if (select == 1) {
            tvQuickbuy.setBackgroundResource(R.color.white);
            tvQuickbuyOrder.setBackgroundResource(R.color.blue);
            tvQuickbuy.setTextColor(this.getResources().getColor(R.color.black));
            tvQuickbuyOrder.setTextColor(this.getResources().getColor(R.color.white));
            tv_address.setBackgroundResource(R.color.white);
            tv_address.setTextColor(this.getResources().getColor(R.color.black));
        } else if (select == 2) {
            tvQuickbuyOrder.setBackgroundResource(R.color.white);
            tvQuickbuyOrder.setTextColor(this.getResources().getColor(R.color.black));
            tvQuickbuy.setBackgroundResource(R.color.white);
            tvQuickbuy.setTextColor(this.getResources().getColor(R.color.black));
            tv_address.setBackgroundResource(R.color.blue);
            tv_address.setTextColor(this.getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == AddressView.requestCode_BaiduAddressActivity) {
            Object o = data.getSerializableExtra("address");
            if (o != null) {
                EditAddress baiduAddress = (EditAddress) o;
                addressView.setAddress(baiduAddress);
            }
        } else if (requestCode == SupermarketIndexActivity.requestCode_PAY_RESULT) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(QuickBuyEvent event) {
        allCountTxt.setText(event.getNum() + "");
        allPriceTxt.setText("¥ " + event.getMoney());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
