package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.SettingMainImgBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.solart.turbo.BaseSinleChooseAdapter;
import cc.solart.turbo.BaseViewHolder;
import cc.solart.turbo.MAdapterOnClickListener;

/**
 * Created by nongyd on 17/9/2.
 */

public class SettingMainImgAdapter extends BaseSinleChooseAdapter<SettingMainImgBean, SettingMainImgAdapter.ViewHolder> {


    public SettingMainImgAdapter(Context context, List<SettingMainImgBean> data, MAdapterOnClickListener<SettingMainImgBean> onClickListener) {
        super(context, data, onClickListener);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateItemView(R.layout.item_setting_main_img, parent));
    }

    @Override
    protected void convert(SettingMainImgAdapter.ViewHolder holder, SettingMainImgBean item, int position) {
        int width=mContext.getResources().getDimensionPixelSize(R.dimen.dp300);
        Glide.with(mContext).load(item.getImgUrl()).apply(new RequestOptions().fitCenter().override(width,width)).into(holder.imgMain);
        if(item.isSelected()){
            holder.imgStatus.setVisibility(View.VISIBLE);
        }else{
            holder.imgStatus.setVisibility(View.GONE);
        }
    }


    static class ViewHolder extends BaseViewHolder {
        @Bind(R.id.img_main)
        ImageView imgMain;
        @Bind(R.id.img_status)
        ImageView imgStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
