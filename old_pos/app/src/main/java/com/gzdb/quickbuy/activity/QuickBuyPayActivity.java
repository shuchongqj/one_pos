package com.gzdb.quickbuy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.BalanceResultBean;
import com.gzdb.quickbuy.dialog.DialogIDpayFrag;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.activity.PayActivity;
import com.gzdb.supermarket.been.EmptyBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.config.ConfigBean;
import com.lzy.okgo.OkGo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhumg on 8/16.
 */
public class QuickBuyPayActivity extends BaseActivity {


    @Bind(R.id.text_payPrice)
    TextView textPayPrice;
    @Bind(R.id.text_deliverPrice)
    TextView textDeliverPrice;
    @Bind(R.id.text_balance)
    TextView textBalance;
    @Bind(R.id.layout_balance)
    LinearLayout layoutBalance;
    @Bind(R.id.layout_idCarPay)
    LinearLayout layoutIdCarPay;
    @Bind(R.id.layout_wxpay)
    LinearLayout layoutWxpay;
    @Bind(R.id.layout_alipay)
    LinearLayout layoutAlipay;
    @Bind(R.id.close_btn)
    ImageView closeBtn;
    @Bind(R.id.text_creaditLine)
    TextView textCreaditLine;
    @Bind(R.id.text_useCreaditLine)
    TextView textUseCreaditLine;

    private String payPrice;//应收金额
    private String deliveryPrice;//运费
    private String orderId;
    private Dialog payAd = null;
    private BalanceResultBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quickbuy_pay);
        ButterKnife.bind(this);
        initView();
        getBalance();
    }

    private void initView() {

        deliveryPrice = getIntent().getStringExtra("deliverPrice");
        payPrice = getIntent().getStringExtra("payPrice");
        orderId = getIntent().getStringExtra("orderId");
        textDeliverPrice.setText(deliveryPrice);
        textPayPrice.setText(payPrice);
        textBalance.setText("");
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    void showDialogImpl() {

        if (payAd == null) {

            LayoutInflater inflater = LayoutInflater.from(this);
            // final AlertDialog ad = new AlertDialog.Builder(activity).create();

            payAd = new Dialog(this, R.style.Dialog);
            // dialog.setContentView(R.layout.dialog_balance_pay_layout);

            // ad.setCancelable(false);
            View view = inflater.inflate(R.layout.dialog_input_password, null);
            // ad.setView(view);
            payAd.setContentView(view);

            // 弹出输入法
            TextView balance_pay_value = (TextView) view
                    .findViewById(R.id.balance_pay_value);
            balance_pay_value.setText("¥：" + payPrice);
            final EditText balance_pay_pass_et = (EditText) view
                    .findViewById(R.id.balance_pay_pass_et);
            balance_pay_pass_et.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            balance_pay_pass_et.setFocusable(true);
            balance_pay_pass_et.requestFocus();
            balance_pay_pass_et.setFocusableInTouchMode(true);
            //
            // 自动弹出输入法
            payAd.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            payAd.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            payAd.setCanceledOnTouchOutside(false);
            payAd.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
                    if (arg1 == KeyEvent.KEYCODE_BACK && arg2.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            View balance_pay_cancle = view
                    .findViewById(R.id.balance_pay_cancle);
            View balance_pay_btn = view
                    .findViewById(R.id.balance_pay_btn);
            balance_pay_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    payAd.dismiss();
                }
            });
            balance_pay_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String passStr = balance_pay_pass_et.getText().toString()
                            .trim();
                    if (TextUtils.isEmpty(passStr)) {
                        ToastUtil.showToast(QuickBuyPayActivity.this, "请输入付款密码！");
                        return;
                    } else if (passStr.length() != 6) {
                        ToastUtil.showToast(QuickBuyPayActivity.this, "付款密码是6位！");
                        return;
                    } else {
                        ToastUtil.showToast(QuickBuyPayActivity.this, "发送到付款！");

                        InputMethodManager imm = (InputMethodManager) QuickBuyPayActivity.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(
                                    balance_pay_pass_et.getWindowToken(), 0);
                        }
                        payAd.dismiss();
                        pay(0, passStr);
                        return;
                    }
                }
            });
        } else {
            // 弹出输入法
            TextView balance_pay_value = (TextView) payAd
                    .findViewById(R.id.balance_pay_value);
            balance_pay_value.setText("¥：" + payPrice);

            EditText balance_pay_pass_et = (EditText) payAd
                    .findViewById(R.id.balance_pay_pass_et);

            balance_pay_pass_et.setText("");
        }

        payAd.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT) {
            //针对选中的商品进行添加库存
            setResult(SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
            finish();
        }
    }

    @OnClick({R.id.layout_balance, R.id.layout_idCarPay, R.id.layout_wxpay, R.id.layout_alipay, R.id.close_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_balance:
                if (Double.valueOf(payPrice) > Double.valueOf(textBalance.getText().toString())) {
                    ToastUtil.showWrning(mContext, "余额不足，请使用其它付款方式！");
                    return;
                }
                showDialogImpl();
                break;
            case R.id.layout_idCarPay:
                if (Double.valueOf(textCreaditLine.getText().toString()) <= 0) {
                    ToastUtil.showWrning(mContext, "您的信用额度为0，请联系客服申请!");
                    return;
                } else if (Double.valueOf(payPrice) < 300) {
                    ToastUtil.showWrning(mContext, "金额小于300的订单不支持货到付款!");
                    return;
                } else if (Double.valueOf(payPrice) > Double.valueOf(textUseCreaditLine.getText().toString())) {
                    ToastUtil.showWrning(mContext, "可用额度不足，请使用其它付款方式！");
                    return;
                }
                DialogIDpayFrag dialogIDpayFrag=new DialogIDpayFrag();
                Bundle bundle=new Bundle();
                bundle.putString("bean",new Gson().toJson(bean));
                bundle.putString("goodPrice",getIntent().getStringExtra("payPrice"));
                dialogIDpayFrag.setArguments(bundle);
                dialogIDpayFrag.show(getSupportFragmentManager(),DialogIDpayFrag.class.getName());
                break;
            case R.id.layout_wxpay:
                pay(1, "");
                break;
            case R.id.layout_alipay:
                pay(2, "");
                break;
            case R.id.close_btn:
                finish();
                break;
        }
    }

    public void pay(int payType, String secreat) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("payType", payType);
        intent.putExtra("orderId", orderId);
        intent.putExtra("paymentPassword", secreat);
        intent.putExtra("supply", true);
        startActivityForResult(intent, SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
    }

    public void getBalance() {
        OkGo.<NydResponse<BalanceResultBean>>post(Contonts.URL_GET_BALANCE)
                .params("orderId",getIntent().getStringExtra("orderId"))
                .execute(new DialogCallback<NydResponse<BalanceResultBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<BalanceResultBean>> response) {
                        bean=response.body().response;
                        for (BalanceResultBean.DatasBean bean : response.body().response.getDatas()) {
                            if (bean.getType().equals("2")) {
                                textBalance.setText(bean.getCurrentAmount() + "");
                            }
                        }
                        textCreaditLine.setText(response.body().response.getNowQuota() + "");
                        textUseCreaditLine.setText(response.body().response.getSurplusQuota() + "");
                    }
                });
    }

    public void creaditLinePay() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.dialog_input_password, null);
        final ViewHolder holder=new ViewHolder(customView);
        final ConfigBean configBean = StyledDialog.buildCustom(customView, Gravity.CENTER);
        holder.balancePayValue.setText("¥：" + bean.getPayAmount());
        final Dialog dialog = configBean.show();
        holder.balancePayCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });



        holder.balancePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BaseUtils.isEmpty(holder.balancePayPassEt.getText().toString())){
                    ToastUtil.showWrning(mContext,"请输入支付密码！");
                    return;
                }
                OkGo.<NydResponse<EmptyBean>>post(Contonts.URL_CREADIT_LINE_PAY)
                        .params("orderId",orderId)
                        .params("paymentPassword",holder.balancePayPassEt.getText().toString())
                        .execute(new DialogCallback<NydResponse<EmptyBean>>(mContext) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                                ToastUtil.showSuccess(mContext,"支付成功！");
                                SupermarketIndexActivity.speak("支付成功！");
                                dialog.cancel();
                                finish();
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                                super.onError(response);
                                SupermarketIndexActivity.speak("支付失败！");
                                DialogUtil.OkAndCancle(mContext,response.message()).show();
                            }
                        });
            }
        });

    }

    static class ViewHolder {
        @Bind(R.id.balance_pay_value)
        TextView balancePayValue;
        @Bind(R.id.balance_pay_pass_et)
        EditText balancePayPassEt;
        @Bind(R.id.balance_pay_cancle)
        TextView balancePayCancle;
        @Bind(R.id.balance_pay_btn)
        TextView balancePayBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
