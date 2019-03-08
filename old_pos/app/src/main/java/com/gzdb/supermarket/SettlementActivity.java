package com.gzdb.supermarket;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.MTextWatch;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.bluetooth.BluetoothUtil;
import com.gzdb.sunmi.bluetooth.SettlementOnTicket;
import com.gzdb.supermarket.activity.PayActivity;
import com.gzdb.supermarket.adapter.SettlementAdapter;
import com.gzdb.supermarket.been.EmptyBean;
import com.gzdb.supermarket.been.SettlementResultBean;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created by zhumg on 8/5.
 */
public class SettlementActivity extends BaseActivity {


    public static int payType;//0 1号生活 1 微信  2 支付宝
    @Bind(R.id.shop_type)
    TextView shopType;
    @Bind(R.id.shop_time)
    TextView shopTime;
    @Bind(R.id.commodity_name)
    TextView commodityName;
    @Bind(R.id.commodity_price)
    TextView commodityPrice;
    @Bind(R.id.commodity_count)
    TextView commodityCount;
    @Bind(R.id.placeorder_detail)
    ListView placeorderDetail;
    @Bind(R.id.qingdian)
    TextView qingdian;
    @Bind(R.id.qingdian_cash)
    TextView qingdianCash;
    @Bind(R.id.all_name)
    TextView allName;
    @Bind(R.id.order_number)
    TextView orderNumber;
    @Bind(R.id.order_total)
    TextView orderTotal;
    @Bind(R.id.tip)
    TextView tip;
    @Bind(R.id.finish_pay)
    TextView finishPay;
    @Bind(R.id.tip_text)
    TextView tipText;
    @Bind(R.id.edit_cash)
    EditText editCash;
    @Bind(R.id.layout_payNotice)
    LinearLayout layoutPayNotice;
    @Bind(R.id.line)
    TextView line;
    @Bind(R.id.Alipay)
    Button Alipay;
    @Bind(R.id.Wechat)
    Button Wechat;
    @Bind(R.id.life_pay)
    Button lifePay;
    @Bind(R.id.change_pay)
    Button changePay;
    @Bind(R.id.text_payPrice)
    TextView textPayPrice;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.layuot_onlinePay)
    LinearLayout layuotOnlinePay;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.text_title)
    TextView textTitle;


    private Dialog dialog;

    private SettlementAdapter adapter;
    private SettlementResultBean bean;

    List<SettlementResultBean.DatasBean> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement2);
        ButterKnife.bind(this);
        editCash.addTextChangedListener(new MTextWatch(editCash));
        textTitle.setText("营业额交班结算清单");
        post();
    }

    public void post() {
        OkGo.<NydResponse<SettlementResultBean>>post(Contonts.URL_SETTLEMENT_GET_DATA)
                .execute(new DialogCallback<NydResponse<SettlementResultBean>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<SettlementResultBean>> response) {
                        initView(response.body().response);
                    }

                });
    }

    private void initView(SettlementResultBean bean) {
        this.bean = bean;
        checkbox.setChecked(true);
        if (bean.getIsDirectStore() == 0) {
            lifePay.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            tip.setText("请按照交班结算程序核对清单后再交班");
            layuotOnlinePay.setVisibility(View.GONE);
            changePay.setVisibility(View.VISIBLE);
            layoutPayNotice.setVisibility(View.GONE);

        } else if (bean.getIsDirectStore() == 1) {
            Log.e("--", "直营店");
            layuotOnlinePay.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            tip.setText("请按照交班结算程序核对清单后再交班");
            changePay.setVisibility(View.GONE);
            layoutPayNotice.setVisibility(View.VISIBLE);
//            }
        } else {
            changePay.setVisibility(View.VISIBLE);
        }
        orders.addAll(bean.getDatas());
        adapter = new SettlementAdapter(SettlementActivity.this, R.layout.layout_settlement_item, orders);
        if (app.currentUser != null) {
            shopType.setText(app.getCurrentUser().getAccountTypeName() + ":" + app.getCurrentUser().getRealName());
        }
        orderTotal.setText("" + bean.getTotalAmount());
        shopTime.setText("结算时间：" + BaseUtils.convertToStrSS(System.currentTimeMillis()));
        int sum = 0;
        for (int i = 0; i < bean.getDatas().size(); i++) {
            sum += bean.getDatas().get(i).getCount();
        }
        orderNumber.setText("" + sum);
        bean.setTotalNum(sum);
        placeorderDetail.setAdapter(adapter);
    }


    View.OnClickListener setComeBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int id = v.getId();

            if (id == R.id.Alipay) {

                return;
            }

            if (id == R.id.Wechat) {

                return;
            }

//            if (id==R.id.life_pay) {

//            }
//
            if (v == changePay) {

            }
        }
    };

    void orderIdNullPay() {
        if (bean.getDatas().size() < 1) {
            Toasty.error(mContext, "暂无订单数据，无需结算！").show();
            return;
        }
        OkGo.<NydResponse<EmptyBean>>post(Contonts.URL_SETTLEMENT_COUNT)
                .params("cashAmount", editCash.getText().toString())
                .execute(new DialogCallback<NydResponse<EmptyBean>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<EmptyBean>> response) {
                        SupermarketIndexActivity.speak("结算成功！");
                        Toasty.success(mContext, "结算成功！").show();
                        if (checkbox.isChecked()) {
                            try {
                                SettlementOnTicket.outcomeTacket(bean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!Sunmi.viceScreenMode) {
                            try {
                                App.mSerialPortOperaion.WriteData(0xC);
                            } catch (Exception e) {
                            }
                        }
                        finish();
                    }

                    @Override
                    public void onError(Response<NydResponse<EmptyBean>> response) {
                        super.onError(response);
                        Toasty.success(mContext, "结算失败，请重试！").show();
                        SupermarketIndexActivity.speak("结算失败！");
                    }
                });
    }

    @OnClick({R.id.img_back, R.id.Alipay, R.id.Wechat, R.id.life_pay, R.id.change_pay})
    public void onViewClicked(View view) {
        if (view.getId() != R.id.img_back) {
            if (checkbox.isChecked()) {
                BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
                if (btAdapter == null) {//请打开蓝牙!
                    //商米开发测试请打开
                    Toasty.warning(mContext, "打开蓝牙后才能打印小票！").show();
                    return;
                }
            }
            if (!BaseUtils.isEmpty(editCash.getText().toString())) {
                bean.setCashAmount(Double.valueOf(editCash.getText().toString()));
            }
        }
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.Alipay:
                if (bean.getDatas().size() < 1) {
                    Toasty.error(mContext, "暂无订单数据，无需结算！").show();
                    return;
                }
                if (BaseUtils.isEmpty(editCash.getText().toString())) {
                    Toasty.error(mContext, "请输入结算现金！").show();
                    return;
                }
                payType = 2;
                Intent intent = new Intent(SettlementActivity.this, PayActivity.class);
                intent.putExtra("payType", payType);
                intent.putExtra("cashAmount", editCash.getText().toString());
                intent.putExtra("settlementBean", new Gson().toJson(bean));
                startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
                break;
            case R.id.Wechat:
                if (bean.getDatas().size() < 1) {
                    Toasty.error(mContext, "暂无订单数据，无需结算！").show();
                    return;
                }
                if (BaseUtils.isEmpty(editCash.getText().toString())) {
                    Toasty.error(mContext, "请输入结算现金！").show();
                    return;
                }
                payType = 1;
                Intent intent2 = new Intent(SettlementActivity.this, PayActivity.class);
                intent2.putExtra("payType", payType);
                intent2.putExtra("cashAmount", editCash.getText().toString());
                intent2.putExtra("settlementBean", new Gson().toJson(bean));
                startActivityForResult(intent2, SupermarketIndexActivity.requestCode_PAY_RESULT);
                break;
            case R.id.life_pay:
                //                ToastUtil.showToast(SettlementActivity.this, "即将上线，敬请期待");
//                return;
                break;
            case R.id.change_pay:
                orderIdNullPay();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getBooleanExtra("isPay", false)) {
            if (checkbox.isChecked()) {
                try {
                    SettlementOnTicket.outcomeTacket(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!Sunmi.viceScreenMode) {
                try {
                    App.mSerialPortOperaion.WriteData(0xC);
                } catch (Exception e) {
                }
            }
            finish();
        }
    }
}
