package com.gzdb.mall.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gzdb.basepos.R;
import com.gzdb.mall.bean.Nav;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import cc.solart.turbo.BaseSinleChooseAdapter;
import cc.solart.turbo.BaseViewHolder;
import cc.solart.turbo.MAdapterOnClickListener;

/**
 * Created by nongyd on 17/5/3.
 */

public class NavAdapter extends BaseSinleChooseAdapter<Nav, NavAdapter.ViewHolder> {


    public NavAdapter(Context context, List<Nav> data, MAdapterOnClickListener<Nav> listener) {
        super(context, data,listener);
    }

    @Override
    protected void convert(final ViewHolder holder, final Nav item, final int position) {
        if (item.isSelected()) {
            holder.view.setVisibility(View.VISIBLE);
            holder.text.setTextColor(ContextCompat.getColor(mContext, R.color.font_select));
        } else {
            holder.view.setVisibility(View.GONE);
            holder.text.setTextColor(ContextCompat.getColor(mContext, R.color.font_3));
        }
        holder.text.setText(item.getTitle());
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateItemView(R.layout.vas_pare_item_type, parent));
    }

    static class ViewHolder extends BaseViewHolder {
        @Bind(R.id.view)
        ImageView view;
        @Bind(R.id.text)
        TextView text;
        @Bind(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
