package com.gzdb.quickbuy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.AddressAdapter;
import com.gzdb.quickbuy.bean.AddressBean;
import com.gzdb.quickbuy.bean.BalanceResultBean;
import com.gzdb.quickbuy.bean.OrderDetailItemBean;
import com.gzdb.quickbuy.bean.QuickBuyItem;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.activity.PayActivity;
import com.gzdb.supermarket.been.EmptyBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.config.ConfigBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liubolin on 2018/3/2.
 */

public class OrderBuyPayActivity extends BaseActivity implements View.OnClickListener {

    View ic_back;

    TextView tv_user_name;
    TextView tv_user_phone;
    TextView tv_user_address;
    TextView tv_order_number;
    TextView tv_log;

    TextView all_price; // 总金额
    TextView dispatching_price; // 运费
    TextView preferential_price; // 支付优惠
    TextView pey_price; // 付款金额
    View btn_change_address;
    EditText et_log;

    TextView text_balance;// 商家余额
    TextView text_vip_balance;// 会员余额

    RadioButton rb_balance;
    RadioButton rb_vip_balance;
    RadioButton rb_wxpay;
    RadioButton rb_alipay;

    View layout_balance;
    View layout_vip_balance;
    View layout_wxpay;
    View layout_alipay;

    View ll_pay_button;

    Button btn_submit;
    Button btn_clean;

    LinearLayout order_item_layout;
    PopupWindow window;
    private String payPrice;//应收金额
    private String deliveryPrice;//运费
    private String orderId;
    private Dialog payAd = null;
    private BalanceResultBean balanceBean;
    private int paymentEnter = 2;
    private String sequenceNumber;

    private List<OrderDetailItemBean> orderItems = new ArrayList<>();
    private List<QuickBuyItem> buyItems = new ArrayList<>();
    private AddressAdapter mAddressAdapter;
    private List<AddressBean> addressList = new ArrayList<>();

    CustomNestRadioGroup ll_pay_way;

    int payType = 0; // 0余额支付 1 信用支付 2 微信支付 3 支付宝支付

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_buy_pay);
        paymentEnter = getIntent().getIntExtra("paymentEnter", 2);
        initView();

        initData();
    }

    private void initView() {
        ic_back = findViewById(R.id.ic_back);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_user_address = (TextView) findViewById(R.id.tv_user_address);
        tv_order_number = (TextView) findViewById(R.id.tv_order_number);
        tv_log = (TextView) findViewById(R.id.tv_log);
        et_log = (EditText) findViewById(R.id.et_log);
        btn_change_address = findViewById(R.id.btn_change_address);
        order_item_layout = (LinearLayout) findViewById(R.id.order_item_layout);

        all_price = (TextView) findViewById(R.id.all_price);
        dispatching_price = (TextView) findViewById(R.id.dispatching_price);
        preferential_price = (TextView) findViewById(R.id.preferential_price);
        pey_price = (TextView) findViewById(R.id.pey_price);

        text_balance = (TextView) findViewById(R.id.text_balance);
        text_vip_balance = (TextView) findViewById(R.id.text_vip_balance);

        rb_balance = (RadioButton) findViewById(R.id.rb_balance);
        rb_vip_balance = (RadioButton) findViewById(R.id.rb_vip_balance);
        rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
        rb_wxpay = (RadioButton) findViewById(R.id.rb_wxpay);

        layout_balance = findViewById(R.id.layout_balance);
        layout_vip_balance = findViewById(R.id.layout_vip_balance);
        layout_alipay = findViewById(R.id.layout_alipay);
        layout_wxpay = findViewById(R.id.layout_wxpay);

        ll_pay_way = (CustomNestRadioGroup) findViewById(R.id.ll_pay_way);
        ll_pay_button = findViewById(R.id.ll_pay_button);

        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_clean = (Button) findViewById(R.id.btn_clean);

        ic_back.setOnClickListener(this);
        btn_change_address.setOnClickListener(this);

        layout_balance.setOnClickListener(this);
        layout_vip_balance.setOnClickListener(this);
        layout_wxpay.setOnClickListener(this);
        layout_alipay.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        ll_pay_way.setOnCheckedChangeListener(new CustomNestRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CustomNestRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_balance:
                        payType = 0;
                        break;
                    case R.id.rb_vip_balance:
                        payType = 1;
                        break;
                    case R.id.rb_wxpay:
                        payType = 2;
                        break;
                    case R.id.rb_alipay:
                        payType = 3;
                        break;
                }
            }
        });

    }

    private void initSelectAddress() {
        View selectAddress = LayoutInflater.from(this).inflate(R.layout.qu_notaccomplish_view, null);
        ListView addressListView = (ListView) selectAddress.findViewById(R.id.lv_notaccomplish);
        mAddressAdapter = new AddressAdapter(this, addressList, true);
        addressListView.setAdapter(mAddressAdapter);

        window = new PopupWindow(selectAddress, 800, 600);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();

        addressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressBean bean = addressList.get(position);
                tv_user_name.setText(bean.getName());
                tv_user_phone.setText(bean.getPhoneNumber());
                tv_user_address.setText(bean.getAddressAlias() + " " + bean.getAddress());
                window.dismiss();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(1.0f);
            }
        });

        getAddressList();
    }

    private void getAddressList() {
        OkGo.<NydResponse<AddressView.Datas>>post(Contonts.URL_ADDRESS_LIST)
                .params("passportId", ((App) this.getApplication()).getCurrentUser().getPassportId())
                .execute(new DialogCallback<NydResponse<AddressView.Datas>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<AddressView.Datas>> response) {
                        mAddressAdapter.setDatas(response.body().response.getDatas());
                        if (addressList.size() > 0) {
                            tv_user_name.setText(addressList.get(0).getName());
                            tv_user_phone.setText(addressList.get(0).getPhoneNumber());
                            tv_user_address.setText(addressList.get(0).getAddressAlias() + " " + addressList.get(0).getAddress());
                        }
                    }
                });
    }

    private void initData() {
        LogUtils.e(getIntent().getStringExtra("jsonData"));
        orderId = getIntent().getStringExtra("orderId");
        sequenceNumber=getIntent().getStringExtra("sequenceNumber");

        getBalance();
        deliveryPrice = getIntent().getStringExtra("deliverPrice");
        payPrice = getIntent().getStringExtra("payPrice");
        boolean isOrderToPay = getIntent().getBooleanExtra("isOrderToPay", false);
        String sequenceNumber = getIntent().getStringExtra("sequenceNumber");
        tv_order_number.setText("订单编号：" + sequenceNumber);
        if (isOrderToPay) {
            orderItems.clear();
            orderItems.addAll((List<OrderDetailItemBean>) getIntent().getSerializableExtra("datas"));
            reOrderItemView();
        } else {
            buyItems.clear();
            buyItems.addAll((List<QuickBuyItem>) getIntent().getSerializableExtra("datas"));
            reItemView();
        }

        initSelectAddress();
        dispatching_price.setText(deliveryPrice);
        all_price.setText(payPrice);
        pey_price.setText(payPrice);
        text_balance.setText("");
    }

    void showDialogImpl(final int type) {

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
                        ToastUtil.showToast(OrderBuyPayActivity.this, "请输入付款密码！");
                        return;
                    } else if (passStr.length() != 6) {
                        ToastUtil.showToast(OrderBuyPayActivity.this, "付款密码是6位！");
                        return;
                    } else {
                        ToastUtil.showToast(OrderBuyPayActivity.this, "发送到付款！");

                        InputMethodManager imm = (InputMethodManager) OrderBuyPayActivity.this
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(
                                    balance_pay_pass_et.getWindowToken(), 0);
                        }
                        payAd.dismiss();
                        pay(type, passStr);
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

    public void pay(int payType, String secreat) {
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("payType", payType);
        intent.putExtra("orderId", orderId);
        intent.putExtra("paymentPassword", secreat);
        intent.putExtra("supply", true);
        intent.putExtra("paymentEnter",paymentEnter);
        intent.putExtra("sequenceNumber",sequenceNumber);
        startActivityForResult(intent, SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
    }

    public void getBalance() {
        OkGo.<NydResponse<BalanceResultBean>>post(Contonts.URL_GET_MERCHANT_BALANCE)
                .params("orderId", getIntent().getStringExtra("orderId"))
                .execute(new DialogCallback<NydResponse<BalanceResultBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<BalanceResultBean>> response) {
                        balanceBean = response.body().response;
                        for (BalanceResultBean.DatasBean bean : response.body().response.getDatas()) {
                            if (bean.getType().equals("2")) {
                                text_vip_balance.setText(bean.getCurrentAmount() + "");
                            }
                            if (bean.getType().equals("1")) {
                                text_balance.setText(bean.getCurrentAmount() + "");
                                if (bean.getCurrentAmount() >= Double.parseDouble(payPrice)) {
                                    rb_balance.setChecked(true);
                                } else {
                                    rb_wxpay.setChecked(true);
                                }
                            }
                        }
                    }
                });
    }

    public void creaditLinePay() {
        ViewGroup customView = (ViewGroup) View.inflate(this, R.layout.dialog_input_password, null);
        final QuickBuyPayActivity.ViewHolder holder = new QuickBuyPayActivity.ViewHolder(customView);
        final ConfigBean configBean = StyledDialog.buildCustom(customView, Gravity.CENTER);
        holder.balancePayValue.setText("¥：" + balanceBean.getPayAmount());
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
                if (BaseUtils.isEmpty(holder.balancePayPassEt.getText().toString())) {
                    ToastUtil.showWrning(mContext, "请输入支付密码！");
                    return;
                }
                OkGo.<NydResponse<EmptyBean>>post(Contonts.URL_CREADIT_LINE_PAY)
                        .params("orderId", orderId)
                        .params("paymentPassword", holder.balancePayPassEt.getText().toString())
                        .execute(new DialogCallback<NydResponse<EmptyBean>>(mContext) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                                ToastUtil.showSuccess(mContext, "支付成功！");
                                SupermarketIndexActivity.speak("支付成功！");
                                dialog.cancel();
                                finish();
                            }

                            @Override
                            public void onError(com.lzy.okgo.model.Response<NydResponse<EmptyBean>> response) {
                                super.onError(response);
                                SupermarketIndexActivity.speak("支付失败！");
                                DialogUtil.OkAndCancle(mContext, response.message()).show();
                            }
                        });
            }
        });

    }

    // 支付前修改收货地址
    void modifyOrderAddress() {
        HttpParams params = new HttpParams();
        params.put("orderId", orderId);
        params.put("receiptAddress", tv_user_address.getText().toString());
        params.put("receiptNickName", tv_user_name.getText().toString());
        params.put("receiptPhone", tv_user_phone.getText().toString());
        OkGo.<NydResponse<JSONObject>>post(Contonts.URL_SUPPLY_MODIFY_ORDER_ADDRESS)
                .params(params)
                .execute(new DialogCallback<NydResponse<JSONObject>>(this) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                        if (response.body().code == 0) {
                            // 修改成功 去支付
                            toPay();
                        } else {
                            ToastUtil.showToast(OrderBuyPayActivity.this, response.body().msg);
                        }
                    }
                });
    }

    void toPay() {
        switch (payType) {
            case 0:
                if (Double.valueOf(payPrice) > Double.valueOf(text_balance.getText().toString())) {
                    ToastUtil.showWrning(mContext, "商家余额不足，请使用其它付款方式！");
                    return;
                }
                showDialogImpl(0);
                break;
            case 1:
                /*if (Double.valueOf(text_creaditLine.getText().toString()) <= 0) {
                    ToastUtil.showWrning(mContext, "您的信用额度为0，请联系客服申请!");
                    return;
                } else if (Double.valueOf(payPrice) < 300) {
                    ToastUtil.showWrning(mContext, "金额小于300的订单不支持货到付款!");
                    return;
                } else if (Double.valueOf(payPrice) > Double.valueOf(text_useCreaditLine.getText().toString())) {
                    ToastUtil.showWrning(mContext, "可用额度不足，请使用其它付款方式！");
                    return;
                }
                DialogIDpayFrag dialogIDpayFrag=new DialogIDpayFrag();
                Bundle bundle=new Bundle();
                bundle.putString("bean",new Gson().toJson(balanceBean));
                bundle.putString("goodPrice",getIntent().getStringExtra("payPrice"));
                dialogIDpayFrag.setArguments(bundle);
                dialogIDpayFrag.show(getSupportFragmentManager(),DialogIDpayFrag.class.getName());*/

                if (Double.valueOf(payPrice) > Double.valueOf(text_vip_balance.getText().toString())) {
                    ToastUtil.showWrning(mContext, "会员余额不足，请使用其它付款方式！");
                    return;
                }

                showDialogImpl(4);
                break;
            case 2:
                pay(1, "");
                break;
            case 3:
                pay(2, "");
                break;
        }
    }

    private void reItemView() {
        order_item_layout.removeAllViews();
        for (int i = 0; i < buyItems.size(); i++) {
            QuickBuyItem itemBean = buyItems.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.widget_quickbuy_item, null);
            TextView item_name = (TextView) view.findViewById(R.id.item_name);
            TextView item_number = (TextView) view.findViewById(R.id.item_number);
            TextView item_sub = (TextView) view.findViewById(R.id.item_sub);
            TextView item_accomplish = (TextView) view.findViewById(R.id.item_accomplish);
            TextView item_distribution = (TextView) view.findViewById(R.id.item_distribution);
            TextView item_not_quantity = (TextView) view.findViewById(R.id.item_not_quantity);

            View view_1 = view.findViewById(R.id.view_1);
            view_1.setVisibility(View.VISIBLE);
            item_accomplish.setVisibility(View.GONE);
            item_distribution.setVisibility(View.GONE);
            item_not_quantity.setVisibility(View.GONE);

            item_name.setText(itemBean.getItems().get(0).getName());
            item_number.setText(String.valueOf("x" + itemBean.getCount()));
            item_sub.setText(String.valueOf(new DecimalFormat("0.00").format(Double.parseDouble(itemBean.getItems().get(0).getPrice()) * itemBean.getCount())));

            order_item_layout.addView(view);
        }
    }

    private void reOrderItemView() {
        order_item_layout.removeAllViews();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderDetailItemBean itemBean = orderItems.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.widget_quickbuy_item, null);
            TextView item_name = (TextView) view.findViewById(R.id.item_name);
            TextView item_number = (TextView) view.findViewById(R.id.item_number);
            TextView item_sub = (TextView) view.findViewById(R.id.item_sub);
            TextView item_accomplish = (TextView) view.findViewById(R.id.item_accomplish);
            TextView item_distribution = (TextView) view.findViewById(R.id.item_distribution);
            TextView item_not_quantity = (TextView) view.findViewById(R.id.item_not_quantity);

            View view_1 = view.findViewById(R.id.view_1);
            view_1.setVisibility(View.VISIBLE);
            item_accomplish.setVisibility(View.GONE);
            item_distribution.setVisibility(View.GONE);
            item_not_quantity.setVisibility(View.GONE);

            item_name.setText(itemBean.getItemName());
            item_number.setText(String.valueOf("x" + itemBean.getNormalQuantity()));
            item_sub.setText(String.valueOf(itemBean.getTotalPrice()));

            order_item_layout.addView(view);
        }
    }

    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ic_back) {
            finish();
        } else if (id == R.id.btn_change_address) {
            // 更换收货地址
            if (null != window) {
                window.showAsDropDown(btn_change_address, 0, 20);
                setBackgroundAlpha(0.5f);
            }
        } else if (id == R.id.layout_balance) {
            rb_balance.setChecked(true);
        } else if (id == R.id.layout_alipay) {
            rb_alipay.setChecked(true);
        } else if (id == R.id.layout_wxpay) {
            rb_wxpay.setChecked(true);
        } else if (id == R.id.layout_vip_balance) {
            rb_vip_balance.setChecked(true);
        } else if (id == R.id.btn_clean) {
            finish();
        } else if (id == R.id.btn_submit) {
            if (TextUtils.isEmpty(orderId)) {
                ToastUtil.showToast(this, "支付异常！");
                return;
            }

            modifyOrderAddress();
        }
    }
}
