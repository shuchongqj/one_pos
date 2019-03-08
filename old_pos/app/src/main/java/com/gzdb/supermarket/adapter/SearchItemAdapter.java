package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.util.ImageLoaders;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchItemAdapter extends BaseRecyclerAdapter<GoodBean> {

    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.tt)
    TextView tt;
    @Bind(R.id.text_buyMoney)
    TextView textBuyMoney;
    @Bind(R.id.sell)
    TextView sell;
    @Bind(R.id.money)
    TextView money;

    private Context mContext;

    public SearchItemAdapter(Context context, List<GoodBean> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.adapter_search_item;
    }

    @Override
    protected void covert(BaseViewHolder holder, GoodBean data, int position) {
        ButterKnife.bind(this, holder.getView());
        String url = data.getItemImg();
        ImageLoaders.display(mContext, image, url);
        name.setText(data.getItemName());
        money.setText(data.getSalesPrice() + "");
        textBuyMoney.setText(data.getStockPrice() + "");
    }
}
