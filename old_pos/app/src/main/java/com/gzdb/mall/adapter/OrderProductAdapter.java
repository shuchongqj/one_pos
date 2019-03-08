package com.gzdb.mall.adapter;

import android.content.Context;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.mall.bean.MallOrderDetail;
import com.gzdb.supermarket.util.Arith;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderProductAdapter extends BaseRecyclerAdapter<MallOrderDetail.ListBean> {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_price)
    TextView tvPrice;

    public OrderProductAdapter(Context context, List<MallOrderDetail.ListBean> list) {
        super(context, list);
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_order_product;
    }

    @Override
    protected void covert(BaseViewHolder holder, MallOrderDetail.ListBean data, int position) {
        ButterKnife.bind(this, holder.getView());
        tvName.setText(data.getItem_name());
        tvNum.setText("×"+data.getNormal_quantity());
        tvPrice.setText("¥ "+ Arith.div(data.getGroup_price(),100));
    }
}
