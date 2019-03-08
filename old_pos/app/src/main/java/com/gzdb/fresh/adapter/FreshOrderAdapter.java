package com.gzdb.fresh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.fresh.bean.FreshOrder;
import com.gzdb.supermarket.util.Arith;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class FreshOrderAdapter extends BaseRecyclerAdapter<FreshOrder> {


    @Bind(R.id.tv_order_id)
    TextView tvOrderId;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_items)
    TextView tvItems;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_money)
    TextView tvMoney;

    public FreshOrderAdapter(Context context, List<FreshOrder> list) {
        super(context, list);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_mall_order;
    }


    @Override
    protected void covert(BaseViewHolder holder, FreshOrder data, int position) {
        ButterKnife.bind(this, holder.getView());


            tvOrderId.setText("订单号：" + data.getOrder_sequence_number());
            //发货状态 0未发货（待发货） 1已发货
            if (data.getDeliver_status() == 0) {
                tvStatus.setText("待收货");
            } else {
                tvStatus.setText("已完成");
            }
            //配送方式 0 到店自取 1 快递配送
            if (data.getDistribution_type() == 1) {
                tvType.setText("配送订单");
            } else {
                tvType.setText("自提订单");
            }
            //订单状态 0创建订单（未支付） 1已支付 2取消订单 3 已成功退款 4 申请退款不通过 5发起退款申请
            if (data.getStatus() == 5) {
                tvStatus.setText("退款申请中");
            }

            tvItems.setText(data.getItem_names());
            tvTime.setText(data.getFormat_create_time());
            tvMoney.setText("共 " + Arith.div(data.getTotal_price(), 100) + " 元");

    }





}
