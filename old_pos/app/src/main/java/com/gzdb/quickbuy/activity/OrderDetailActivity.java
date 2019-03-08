package com.gzdb.quickbuy.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.AddressAdapter;
import com.gzdb.quickbuy.bean.AddressBean;
import com.gzdb.quickbuy.bean.BalanceResultBean;
import com.gzdb.quickbuy.bean.CreateOrderResult;
import com.gzdb.quickbuy.bean.OrderDetailBean;
import com.gzdb.quickbuy.bean.OrderDetailItemBean;
import com.gzdb.quickbuy.bean.QuickBuyItem;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liubolin on 2018/2/24.
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {

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

    Button submit;
    Button clean;

    TextView item_state;

    LinearLayout order_item_layout;

    OrderDetailBean bean;

    private String payPrice;//应收金额
    private String deliveryPrice;//运费
    private String orderId;
    private Dialog payAd = null;
    private BalanceResultBean balanceBean;

    private String warehouseId;
    private String mPaySequenceNumber;
    private List<QuickBuyItem> buyItems;
    private CreateOrderResult createOrderResult;

    private AddressAdapter mAddressAdapter;
    private AddressBean selectAddressBean;
    private List<AddressBean> addressList = new ArrayList<>();

    boolean isPay = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
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
        order_item_layout = (LinearLayout) findViewById(R.id.order_item_layout);

        all_price = (TextView) findViewById(R.id.all_price);
        dispatching_price = (TextView) findViewById(R.id.dispatching_price);
        preferential_price = (TextView) findViewById(R.id.preferential_price);
        pey_price = (TextView) findViewById(R.id.pey_price);

        item_state = (TextView) findViewById(R.id.item_state);
        submit = (Button) findViewById(R.id.submit);
        clean = (Button) findViewById(R.id.clean);

        ic_back.setOnClickListener(this);
        submit.setOnClickListener(this);
        clean.setOnClickListener(this);
    }

    private void initData() {
        orderId = getIntent().getStringExtra("orderId");
        getOrderDetailData();
    }

    private void reData() {
        tv_user_name.setText(bean.getReceiptNickName());
        tv_user_phone.setText(bean.getReceiptPhone());
        tv_user_address.setText(bean.getReceiptAddress());
        tv_order_number.setText("订单号：" + bean.getOrderSequenceNumber());

        all_price.setText(String.valueOf(Utils.doubleToTowStr(bean.getTotalPrice())));
        dispatching_price.setText(String.valueOf(Utils.doubleToTowStr(bean.getDistributionFee())));
        preferential_price.setText(String.valueOf(Utils.doubleToTowStr(bean.getDiscountPrice())));
        pey_price.setText(String.valueOf(Utils.doubleToTowStr(bean.getActualPrice())));

        int status = bean.getStatus();
        if (status == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {
            // 未付款
            submit.setVisibility(View.VISIBLE);
            clean.setVisibility(View.VISIBLE);
            item_state.setVisibility(View.GONE);
            submit.setText("去支付");
            isPay = true;
        } else if (status == OrderStatusEnum.ORDER_STATUS_PAYMENY.getType()
                || status == OrderStatusEnum.ORDER_STATUS_ACCEPT.getType()
                || status == OrderStatusEnum.ORDER_STATUS_DELIVER.getType()) {
            // 拣货中
            clean.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            item_state.setVisibility(View.VISIBLE);
            item_state.setText(OrderStatusEnum.getOrderRoleTypeEnum(bean.getStatus()).getName());
        } else if (status == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getType()
                || status == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()
                || status == OrderStatusEnum.ORDER_STATUS_BATCH.getType()) {
            // 配送中
            clean.setVisibility(View.GONE);
            submit.setText("确认收货");
            item_state.setVisibility(View.GONE);
            clean.setVisibility(View.GONE);
            isPay = false;
        } else if (status == OrderStatusEnum.ORDER_STATUS_CANCEL.getType()
                || status == OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {
            // 已完成
            clean.setVisibility(View.GONE);
            clean.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            item_state.setVisibility(View.VISIBLE);
            item_state.setText(OrderStatusEnum.getOrderRoleTypeEnum(bean.getStatus()).getName());
        }

        reItemView();
    }

    private void getOrderDetailData() {
        HttpParams params = new HttpParams();
        params.put("orderId", orderId);
        params.put("convertRMBUnit",true);
        OkGo.<NydResponse<OrderDetailBean>>post(Contonts.ORDER_ALS)
                .params(params)
                .execute(new DialogCallback<NydResponse<OrderDetailBean>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<OrderDetailBean>> response) {
                        if (response.body().code == 0) {
                            bean = response.body().response;
                            reData();
                        } else {
                            ToastUtil.showToast(OrderDetailActivity.this, response.body().msg);
                        }
                    }
                });
    }

    public void reItemView() {
        order_item_layout.removeAllViews();
        for (int i = 0; i < bean.getItemSnapshots().size(); i++) {
            OrderDetailItemBean itemBean = bean.getItemSnapshots().get(i);
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
            item_number.setText(String.valueOf(itemBean.getNormalQuantity()));
            item_sub.setText(String.valueOf(Utils.doubleToTowStr(itemBean.getTotalPrice())));

            item_distribution.setText(String.valueOf(itemBean.getDistributionQuantity()));
            item_accomplish.setText(String.valueOf(itemBean.getReceiptQuantity()));
            item_not_quantity.setText(String.valueOf(itemBean.getShipmentQuantity()));
            order_item_layout.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ic_back) {
            finish();
        } else if (id == R.id.submit) {
            /*HttpParams params = new HttpParams();
            params.put("orderId", bean.getId());
            OkGo.post(Contonts.URL_CONFIRM_ORDER)
                    .params(params)
                    .execute(new DialogCallback<NydResponse<JSONObject>>(mContext) {
                        @Override
                        public void onSuccess(NydResponse<JSONObject> jsonObject, Call call, Response response) {
                            ToastUtil.showToast(mContext, jsonObject.msg);
                            if (jsonObject.code == 0) {
                                getOrderDetailData();
                            }
                        }
                    });*/

            if (isPay) {
                // 去支付
                Intent intent = new Intent();
                intent.putExtra("orderId", bean.getId());
                intent.putExtra("payPrice", String.valueOf(bean.getTotalPrice()));
                intent.putExtra("deliverPrice", String.valueOf(bean.getDistributionFee()));
                intent.putExtra("datas", (Serializable) bean.getItemSnapshots());
                intent.putExtra("isOrderToPay", true);
                intent.putExtra("paymentEnter", 2);
                intent.putExtra("sequenceNumber", bean.getSequenceNumber());
                intent.setClass(mContext, OrderBuyPayActivity.class);
                ((Activity) mContext).startActivityForResult(intent, SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
                finish();
            } else {
                // 确认收货
                HttpParams params = new HttpParams();
                params.put("orderId", bean.getId());
                OkGo.<NydResponse<JSONObject>>post(Contonts.URL_CONFIRM_ORDER)
                        .params(params)
                        .execute(new DialogCallback<NydResponse<JSONObject>>(mContext) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                                ToastUtil.showToast(mContext, response.body().msg);
                                if (response.body().code == 0) {
                                    getOrderDetailData();
                                }
                            }
                        });

            }

        } else if (id == R.id.clean) {
            // 取消订单
            HttpParams params = new HttpParams();
            params.put("orderId", bean.getId());
            params.put("beforeStatus", bean.getStatus());
            OkGo.<NydResponse<JSONObject>>post(Contonts.URL_CANCEL_ORDER)
                    .params(params)
                    .execute(new DialogCallback<NydResponse<JSONObject>>(mContext) {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                            ToastUtil.showToast(mContext, response.body().msg);
                            if (response.body().code == 0) {
                                finish();
                            }
                        }
                    });
        }
    }

    class OrderDetail {

        List<OrderDetailBean> datas;

        public List<OrderDetailBean> getOrderArray() {
            return datas;
        }

        public void setOrderArray(List<OrderDetailBean> datas) {
            this.datas = datas;
        }
    }
}