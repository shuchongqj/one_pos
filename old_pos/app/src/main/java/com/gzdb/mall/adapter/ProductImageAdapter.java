package com.gzdb.mall.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductImageAdapter extends BaseRecyclerAdapter<String> {

    @Bind(R.id.iv_image)
    ImageView ivImage;
    private Context mContext;

    public ProductImageAdapter(Context context, List<String> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_product_image;
    }

    @Override
    protected void covert(BaseViewHolder holder, String data, int position) {
        ButterKnife.bind(this, holder.getView());
        Glide.with(mContext).load(data).into(ivImage);
    }
}
