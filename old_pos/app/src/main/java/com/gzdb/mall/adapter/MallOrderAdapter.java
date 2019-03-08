package com.gzdb.mall.adapter;

import android.content.Context;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.mall.bean.MallOrder;
import com.gzdb.supermarket.util.Arith;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MallOrderAdapter extends BaseRecyclerAdapter<MallOrder> {


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

    public MallOrderAdapter(Context context, List<MallOrder> list) {
        super(context, list);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_mall_order;
    }

    @Override
    protected void covert(BaseViewHolder holder, MallOrder data, int position) {
        ButterKnife.bind(this, holder.getView());
        tvOrderId.setText("订单号：" + data.getOrder_sequence_number());
        if (data.getDeliver_status() == 0) {
            tvStatus.setText("待收货");
        } else {
            tvStatus.setText("已完成");
        }
        if(data.getDistribution_type()==0){
            tvType.setText("上门自提");
        }else{
            tvType.setText("快递配送");
        }
        tvItems.setText(data.getItem_names());
        tvTime.setText(data.getFormat_create_time());
        tvMoney.setText("共 " + Arith.div(data.getTotal_price(),100) + " 元");
    }


}
