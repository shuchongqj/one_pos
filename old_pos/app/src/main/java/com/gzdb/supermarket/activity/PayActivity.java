package com.gzdb.supermarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.ScreenManager;
import com.gzdb.screen.present.ImageDisplay;
import com.gzdb.screen.present.TextDisplay;
import com.gzdb.sunmi.bluetooth.Printer;
import com.gzdb.sunmi.util.OpenDrawers;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.been.EmptyBean;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.SettlementResultBean;
import com.gzdb.supermarket.event.PrintEvent;
import com.gzdb.supermarket.event.StatisticsEvent;
import com.gzdb.supermarket.event.VipPayEvent;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.PrintUtil;
import com.gzdb.vip.VipCheckPhoneDialog;
import com.gzdb.vip.VipUserInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hdx.HdxUtil;


/**
 * 支付界面
 * Created by Even on 2016/6/29.
 * UNKNOWN(9999, "UNKNOWN", "未知"),
 * BALANCE(10000, "BALANCE", "余额"),
 * VIP_BALANCE(11000, "VIP_BALANCE", "会员余额"),
 * ALIPAY(20000, "ALIPAY", "支付宝"),
 * WEIXIN_NATIVE(30000, "WEIXIN_NATIVE", "微信"),
 * WEIXIN_JS(31000, "WEIXIN_JS", "微信公众号"),
 * DRAW_CASH('鱀', "DRAW_CASH", "提现");
 */
public class PayActivity extends BaseActivity implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener {

    public static final int BALANCE_PAY = 0;
    public static final int WX_PAY = 1;
    public static final int ALI_PAY = 2;
    public static final int CASH_PAY = 3;
    public static final int VIP_PAY = 5;
    public static final int UNIT_PAY = 6;

    private ImageView delete;
    private ImageView imageIcon;
    private TextView content;
    private LinearLayout layout;
    private Button btn_join_vip;

    private ScanGunKeyEventHelper scanGunKeyEventHelper = null;//扫码

    public SettlementResultBean bean;//如果是从结算页面跳转过来的就有

    //支付类型
    private int payType;//0=1号生活支付 1=微信收款 2=支付宝收款
    //订单ID
    private String orderId;
    //是否是一键采购，一键采购，微信和支付宝走的接口不一样
    private boolean supplyPay = false;
    private int paymentEnter = 2;
    private String sequenceNumber = "";

    private boolean payVoice = false;

    private String couponId = "";

    private double actualPrice = 0;
    private double totalPrice = 0;
    private double totaldiscountPrice = 0;
    private double discountPercent = 0;

    private boolean isPay = false;

    private String phoneNumber;
    private ScreenManager screenManager = ScreenManager.getInstance();
    private TextDisplay textDisplay = null;
    private ImageDisplay imageDisplay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_dialog2);
        EventBus.getDefault().register(this);
        payVoice = SPUtil.getInstance().getBoolean(this, "payVoice", true);

        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        initView();
        initScreen();
    }

    private void initView() {
        layout = findViewById(R.id.common_dialog_layout);
        delete = findViewById(R.id.delete);
        imageIcon = findViewById(R.id.image_icon);
        content = findViewById(R.id.content);
        btn_join_vip = findViewById(R.id.btn_join_vip);
        btn_join_vip.setOnClickListener(this);

        delete.setOnClickListener(this);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        payType = getIntent().getIntExtra("payType", 0);
        orderId = getIntent().getStringExtra("orderId");
        if (getIntent().getStringExtra("settlementBean") != null) {
            bean = new Gson().fromJson(getIntent().getStringExtra("settlementBean"), SettlementResultBean.class);
        }

        if (getIntent().getStringExtra("couponId") != null) {
            couponId = getIntent().getStringExtra("couponId");
        }

        try {
            sequenceNumber = getIntent().getStringExtra("sequenceNumber");
        } catch (Exception e) {
        }
        try {
            paymentEnter = getIntent().getIntExtra("paymentEnter", 2);
        } catch (Exception e) {
        }
        try {
            supplyPay = getIntent().getBooleanExtra("supply", false);
        } catch (Exception e) {
        }

        try {
            actualPrice = getIntent().getDoubleExtra("actualPrice", 0);
        } catch (Exception e) {
        }

        try {
            totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        } catch (Exception e) {
        }

        try {
            totaldiscountPrice = getIntent().getDoubleExtra("totaldiscountPrice", 0);
        } catch (Exception e) {
        }

        try {
            discountPercent = getIntent().getDoubleExtra("discountPercent", 0);
        } catch (Exception e) {
        }
        btn_join_vip.setVisibility(View.GONE);
        if (payType == 0) {
            layout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            pay("", "BALANCE");
        } else if (payType == 1) {
            imageIcon.setImageResource(R.mipmap.pay_icon_wechat);
            layout.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            content.setText("客户的微信付款码");
        } else if (payType == 2) {
            imageIcon.setImageResource(R.mipmap.pay_icon_alipay);
            layout.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            content.setText("客户的支付宝付款码");
        } else if (payType == 3) {
            layout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            pay("", "CASH");
        } else if (payType == 4) {
            layout.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            pay("", "VIP_BALANCE");
        } else if (payType == 5) {
            imageIcon.setImageResource(R.mipmap.ic_vip_card_big);
            layout.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            btn_join_vip.setVisibility(View.VISIBLE);
            content.setText("客户的VIP付款码");
        } else if (payType == 6) {
            imageIcon.setImageResource(R.mipmap.bg_unit_pay);
            layout.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            content.setText("客户的支付宝/微信付款码");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.delete) {
            finish();
        } else if (id == R.id.btn_join_vip) {
            VipCheckPhoneDialog.showVipCheckPhoneDialog(this, orderId, phoneNumber);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void vipPayEvent(VipPayEvent event) {
        vipPay(event.getCode(), event.getVipUserInfo());
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (barcode.equals("")) {
            return;
        }
        if (isPay) {
            return;
        }
        //扫码后处理
        if (payType == WX_PAY) {//微信扫码
            pay(barcode, "WEIXIN_NATIVE");
        } else if (payType == ALI_PAY) {//支付宝扫码
            pay(barcode, "ALIPAY");
        } else if (payType == VIP_PAY) {
            vipPay(barcode, null);
        } else if (payType == UNIT_PAY) {
            pay(barcode, "UNIT_PAY");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        scanGunKeyEventHelper.analysisKeyEvent(event);
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanGunKeyEventHelper.onDestroy();
        EventBus.getDefault().unregister(this);
        OkGo.getInstance().cancelAll();
    }

    private void vipPay(String barcode, final VipUserInfo vipUserInfo) {
        OkGo.<NydResponse<FinishOrderData>>post(Contonts.VPI_CARD_PAY)
                .params("orderid", orderId)
                .params("code", barcode)
                .params("couponId", couponId)
                .execute(new DialogCallback<NydResponse<FinishOrderData>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                        try {
                            if (response == null) {
                                SupermarketIndexActivity.speak("支付失败");
                                return;
                            }
                            if (response.body() == null) {
                                SupermarketIndexActivity.speak("支付失败");
                                return;
                            }

                            isPay = true;

                            FinishOrderData finishOrderData = response.body().response;
                            if (discountPercent > 0) {
                                EventBus.getDefault().post(new StatisticsEvent(orderId, actualPrice, totalPrice, totaldiscountPrice, discountPercent));
                            }
                            if (payVoice) {
                                SupermarketIndexActivity.speak("支付成功,收款" + finishOrderData.getActualPrice() + "元");
                            }
                            View toastLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_my_taost, null);
                            if (!BaseUtils.isEmpty(finishOrderData.getRewardAmount()) && Double.valueOf(finishOrderData.getRewardAmount()) > 0) {
                                ((TextView) toastLayout.findViewById(R.id.text)).setText("支付成功，本单奖励" + finishOrderData.getRewardAmount() + "元");
                            } else if (!BaseUtils.isEmpty(finishOrderData.getRewardAmount()) && Double.valueOf(finishOrderData.getRewardAmount()) < 0) {
                                ((TextView) toastLayout.findViewById(R.id.text)).setText("支付成功，您的账号已被风控，无法获得奖励！");
                            }
                            ToastUtil.custom(mContext, toastLayout, Toast.LENGTH_LONG).show();
                            try {
                                Printer.outcomeTacket(finishOrderData, vipUserInfo);//打印小票
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                ScreenUtil.getInstance().payActvity(finishOrderData, textDisplay);
                                ScreenUtil.getInstance().waitShowScreen(imageDisplay);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent();
                            intent.putExtra("data", new Gson().toJson(finishOrderData));
                            setResult(SupermarketIndexActivity.requestCode_PAY_RESULT, intent);
                            finish();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                        super.onError(response);
                        finish();
                    }
                });
    }

    public void pay(String barCode, final String payType) {
        try {
            String url = Contonts.URL_PAY;
            if (supplyPay) {
                url = Contonts.URL_PAY2;
            }
            HttpParams httpParams = new HttpParams();
            httpParams.put("paymentTypeKey", payType);
            if (orderId != null) {//结算页面过来的是没有orderId的
                httpParams.put("orderId", orderId);
            }
            httpParams.put("authCode", barCode);
            if (payType.equals("BALANCE") || payType.equals("VIP_BALANCE")) {//一键采购的支付如果是余额支付要加上支付密码
                httpParams.put("paymentPassword", getIntent().getStringExtra("paymentPassword"));
            }
            if (supplyPay) {
                httpParams.put("paymentEnter", paymentEnter);
                httpParams.put("sequenceNumber", sequenceNumber);
            }
            httpParams.put("couponId", couponId);
            if (bean != null) {//结算页面的支付
                settlementPay(httpParams);
            } else {
                OkGo.<NydResponse<FinishOrderData>>post(url)
                        .params(httpParams)
                        .execute(new DialogCallback<NydResponse<FinishOrderData>>(this) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                                try {
                                    isPay = true;
                                    if (response == null) {
                                        SupermarketIndexActivity.speak("支付失败");
                                        return;
                                    }
                                    if (response.body() == null) {
                                        SupermarketIndexActivity.speak("支付失败");
                                        return;
                                    }
                                    FinishOrderData finishOrderData = response.body().response;
                                    //                        SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_PAYSUCCESS,1, 1, 0, 0, 1);
                                    if (discountPercent > 0) {
                                        EventBus.getDefault().post(new StatisticsEvent(orderId, actualPrice, totalPrice, totaldiscountPrice, discountPercent));
                                    }
                                    if (payType.equals("CASH")) {
                                        if (finishOrderData.getChange() > 0) {
                                            if (payVoice) {
                                                SupermarketIndexActivity.speak("现金收款" + finishOrderData.getActualPrice() + "元，找零" + finishOrderData.getChange() + "元");
                                            }
                                        } else {
                                            if (payVoice) {
                                                SupermarketIndexActivity.speak("现金收款" + finishOrderData.getActualPrice() + "元");
                                            }
                                        }
                                    } else if (supplyPay) {//一键采购的订单只提示支付成功；
                                        if (payVoice) {
                                            SupermarketIndexActivity.speak("支付成功");
                                        }
                                    } else {
                                        if (payVoice) {
                                            SupermarketIndexActivity.speak("支付成功,收款" + finishOrderData.getActualPrice() + "元");
                                        }
                                    }
                                    View toastLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_my_taost, null);
                                    if (!BaseUtils.isEmpty(finishOrderData.getRewardAmount()) && Double.valueOf(finishOrderData.getRewardAmount()) > 0) {
                                        ((TextView) toastLayout.findViewById(R.id.text)).setText("支付成功，本单奖励" + finishOrderData.getRewardAmount() + "元");
                                    } else if (!BaseUtils.isEmpty(finishOrderData.getRewardAmount()) && Double.valueOf(finishOrderData.getRewardAmount()) < 0) {
                                        ((TextView) toastLayout.findViewById(R.id.text)).setText("支付成功，您的账号已被风控，无法获得奖励！");
                                    }
                                    ToastUtil.custom(mContext, toastLayout, Toast.LENGTH_LONG).show();
                                    try {
                                        Printer.outcomeTacket(finishOrderData);//打印小票
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        ScreenUtil.getInstance().payActvity(finishOrderData, textDisplay);
                                        ScreenUtil.getInstance().waitShowScreen(imageDisplay);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (payType.equals("CASH")) {
                                        //钱箱
                                        try {
                                            HdxUtil.SetV12Power(0);
                                            HdxUtil.SetV12Power(1);
                                        } catch (Throwable ex) {
                                            ex.printStackTrace();
                                        }
                                        try {
                                            OpenDrawers.openMoneyBox();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (!supplyPay) {
                                        //关掉自己
                                        Intent intent = new Intent();
                                        intent.putExtra("data", new Gson().toJson(finishOrderData));
                                        setResult(SupermarketIndexActivity.requestCode_PAY_RESULT, intent);
                                        finish();
                                    } else {
                                        setResult(SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
                                        finish();
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                                super.onError(response);
                                //支付超时关闭窗口
                                try {
                                    if (response.getException().getMessage().equals("支付超时，请重新支付")) {
                                        SupermarketIndexActivity.speak("支付超时，请重新支付");
                                        finish();
                                    } else {
                                        SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_PAYFAULIE, 1, 1, 0, 0, 1);
                                        if (payType.equals("CASH") || payType.equals("BALANCE") || payType.equals("VIP_BALANCE")) {
                                            finish();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settlementPay(HttpParams params) {
        params.put("cashAmount", getIntent().getStringExtra("cashAmount"));
        OkGo.<NydResponse<EmptyBean>>post(Contonts.URL_SETTLEMENT_PAY)
                .params(params)
                .execute(new DialogCallback<NydResponse<EmptyBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                        try {
                            if (payVoice) {
                                SupermarketIndexActivity.speak("支付成功！");
                            }
                            Intent intent = new Intent();
                            intent.putExtra("isPay", true);
                            setResult(SupermarketIndexActivity.requestCode_PAY_RESULT, intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                        super.onError(response);
                        try {
                            if (payVoice) {
                                SupermarketIndexActivity.speak("支付失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    private void initScreen() {
        screenManager.init(this);
        Display[] displays = screenManager.getDisplays();
        if (displays.length > 1) {
            textDisplay = new TextDisplay(this, displays[1]);
            imageDisplay = new ImageDisplay(this, displays[1]);
        }
    }
}
