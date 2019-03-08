package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.ReportResultBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReportAdapter extends BaseRecyclerAdapter<ReportResultBean.DatasBean> {

    @Bind(R.id.tv_index)
    TextView tvIndex;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.tv_classify)
    TextView tvClassify;
    @Bind(R.id.tv_sale_num)
    TextView tvSaleNum;
    @Bind(R.id.tv_purchase_price)
    TextView tvPurchasePrice;
    @Bind(R.id.tv_sale_price)
    TextView tvSalePrice;
    @Bind(R.id.tv_profit)
    TextView tvProfit;
    @Bind(R.id.tv_rate)
    TextView tvRate;
    @Bind(R.id.tv_all_sale_money)
    TextView tvAllSaleMoney;

    private int mType;


    public ReportAdapter(Context context, List<ReportResultBean.DatasBean> list, int type) {
        super(context, list);
        this.mType = type;
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_report;
    }

    @Override
    protected void covert(BaseViewHolder holder, ReportResultBean.DatasBean data, int position) {
        ButterKnife.bind(this, holder.getView());
        tvIndex.setText(position + 1 + "");
        tvName.setText(data.getItemName());
        tvCode.setText(data.getItemBarcode());
        tvClassify.setText(data.getItTitle());
        tvPurchasePrice.setText(data.getCostPrice());
        tvSalePrice.setText(data.getNormalPrice());
        if (mType == 1) {
            tvSaleNum.setText((int) data.getSumCount() + "");
        } else {
            tvSaleNum.setText(data.getSumCount() + "");
        }
        tvAllSaleMoney.setText(data.getTotalMoney() + "");
        tvProfit.setText(data.getMarginMoney() + "");
        tvRate.setText(data.getGrossMargin());
    }
}
