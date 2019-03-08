package com.gzdb.quickbuy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.activity.OrderBuyPayActivity;
import com.gzdb.quickbuy.activity.OrderDetailActivity;
import com.gzdb.quickbuy.activity.OrderView;
import com.gzdb.quickbuy.bean.OrderDetailBean;
import com.gzdb.quickbuy.bean.OrderDetailItemBean;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liubolin on 2018/2/23.
 */

public class QuickBuyOrderAdapter extends BaseAdapter {

    List<OrderDetailBean> datas;
    Context mContext;
    OrderView orderView;

    public QuickBuyOrderAdapter(Context context, OrderView orderView, List<OrderDetailBean> datas) {
        this.datas = datas;
        mContext = context;
        this.orderView = orderView;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.widget_quickbuy_order_item, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        OrderDetailBean bean = datas.get(position);
        holder.bean = bean;

        holder.qu_orderNum.setText("订单号：" + bean.getOrderSequenceNumber());
        holder.qu_orderTime.setText("创建时间：" + BaseUtils.convertToStrSS(bean.getCreateTime()));

        int status = bean.getStatus();
        if (status == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {
            // 未付款
            holder.reItemView(false);
            holder.tv_amount.setText("合计：" + String.valueOf(bean.getTotalPrice()));
            holder.submit.setVisibility(View.VISIBLE);
            holder.clean.setVisibility(View.VISIBLE);
            holder.item_state.setVisibility(View.GONE);
            holder.view_1.setVisibility(View.VISIBLE);
            holder.ll_item_status.setVisibility(View.GONE);
            holder.isPay = true;
            holder.submit.setText("去支付");
        } else if (status == OrderStatusEnum.ORDER_STATUS_PAYMENY.getType()
                || status == OrderStatusEnum.ORDER_STATUS_ACCEPT.getType()
                ||status == OrderStatusEnum.ORDER_STATUS_DELIVER.getType()) {
            // 拣货中
            holder.reItemView(true);
            holder.clean.setVisibility(View.GONE);
            holder.tv_amount.setVisibility(View.GONE);
            holder.clean.setVisibility(View.GONE);
            holder.submit.setVisibility(View.GONE);
            holder.item_state.setVisibility(View.VISIBLE);
            holder.view_1.setVisibility(View.GONE);
            holder.ll_item_status.setVisibility(View.VISIBLE);
            holder.item_state.setText(OrderStatusEnum.getOrderRoleTypeEnum(bean.getStatus()).getName());
        } else if (status == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getType()
                || status == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()
                || status == OrderStatusEnum.ORDER_STATUS_BATCH.getType()) {
            // 配送中
            holder.reItemView(true);
            holder.submit.setVisibility(View.VISIBLE);
            holder.clean.setVisibility(View.GONE);
            holder.isPay = false;
            holder.submit.setText("确认收货");
            holder.tv_amount.setVisibility(View.GONE);
            holder.item_state.setVisibility(View.GONE);
            holder.clean.setVisibility(View.GONE);
            holder.view_1.setVisibility(View.GONE);
            holder.ll_item_status.setVisibility(View.VISIBLE);
        } else if (status == OrderStatusEnum.ORDER_STATUS_CANCEL.getType()
                || status == OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {
            // 已完成
            holder.reItemView(true);
            holder.clean.setVisibility(View.GONE);
            holder.tv_amount.setVisibility(View.GONE);
            holder.clean.setVisibility(View.GONE);
            holder.submit.setVisibility(View.GONE);
            holder.item_state.setVisibility(View.VISIBLE);
            holder.view_1.setVisibility(View.GONE);
            holder.ll_item_status.setVisibility(View.VISIBLE);
            holder.item_state.setText(OrderStatusEnum.getOrderRoleTypeEnum(bean.getStatus()).getName());
        }else{
            holder.reItemView(true);
            holder.clean.setVisibility(View.GONE);
            holder.tv_amount.setVisibility(View.GONE);
            holder.clean.setVisibility(View.GONE);
            holder.submit.setVisibility(View.GONE);
            holder.item_state.setVisibility(View.VISIBLE);
            holder.view_1.setVisibility(View.GONE);
            holder.ll_item_status.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class Holder implements View.OnClickListener {
        OrderDetailBean bean;

        TextView qu_orderNum;
        TextView qu_orderTime;
        TextView tv_amount;
        TextView item_state;
        View view_1;
        View ll_item_status;

        Button submit;
        Button clean;

        LinearLayout order_item_layout;

        View parentView;

        boolean isPay = true;

        public Holder(View view) {
            parentView = view.findViewById(R.id.parent_view);
            qu_orderNum = (TextView) view.findViewById(R.id.qu_orderNum);
            qu_orderTime = (TextView) view.findViewById(R.id.qu_orderTime);
            tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            item_state = (TextView) view.findViewById(R.id.item_state);
            view_1 = view.findViewById(R.id.view_1);
            ll_item_status = view.findViewById(R.id.ll_item_status);

            submit = (Button) view.findViewById(R.id.submit);
            clean = (Button) view.findViewById(R.id.clean);
            submit.setOnClickListener(this);
            clean.setOnClickListener(this);
            parentView.setOnClickListener(this);

            order_item_layout = (LinearLayout) view.findViewById(R.id.order_item_layout);
        }

        public void reItemView(boolean isShow) {
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
                if (!isShow) {
                    view_1.setVisibility(View.VISIBLE);
                    item_accomplish.setVisibility(View.GONE);
                    item_distribution.setVisibility(View.GONE);
                    item_not_quantity.setVisibility(View.GONE);
                } else {
                    view_1.setVisibility(View.GONE);
                    item_accomplish.setVisibility(View.VISIBLE);
                    item_distribution.setVisibility(View.VISIBLE);
                    item_not_quantity.setVisibility(View.VISIBLE);
                }

                item_name.setText(itemBean.getItemName());
                item_number.setText(String.valueOf(itemBean.getNormalQuantity()));
                item_sub.setText(String.valueOf(itemBean.getTotalPrice()));

                item_distribution.setText(String.valueOf(itemBean.getDistributionQuantity()));
                item_accomplish.setText(String.valueOf(itemBean.getReceiptQuantity()));
                item_not_quantity.setText(String.valueOf(itemBean.getUnReceiptQuantity()));
                order_item_layout.addView(view);
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.submit) {
                if (isPay) {
                    // 去支付
                    Intent intent = new Intent();
                    intent.putExtra("orderId", bean.getId());
                    intent.putExtra("sequenceNumber", bean.getSequenceNumber());
                    intent.putExtra("payPrice", String.valueOf(bean.getTotalPrice()));
                    intent.putExtra("deliverPrice", String.valueOf(bean.getDistributionFee()));
                    intent.putExtra("datas", (Serializable) bean.getItemSnapshots());
                    intent.putExtra("isOrderToPay", true);
                    intent.putExtra("paymentEnter",2);
                    intent.putExtra("sequenceNumber",bean.getSequenceNumber());
                    intent.setClass(mContext, OrderBuyPayActivity.class);
                    ((Activity)mContext).startActivityForResult(intent, SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
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
                                    if (response.body().code == 0){
                                        //orderView.getNotAccomplishOrder(orderStatusEnum.getType());
                                        orderView.getOrders(3);
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
                                if (response.body().code == 0){
                                    //orderView.getNotAccomplishOrder(orderStatusEnum.getType());
                                    orderView.getOrders(1);
                                }
                            }
                        });
            } else if (v.equals(parentView)) {
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                intent.putExtra("orderId", bean.getId());
                mContext.startActivity(intent);
            }
        }
    }
}
