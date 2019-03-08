package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.core.util.BaseUtils;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.NoticeResulBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by nongyd on 17/6/10.
 */


public class NoticeAdapter extends BaseTurboAdapter<NoticeResulBean.DatasBean,NoticeAdapter.ViewHolder > {


    public NoticeAdapter(Context context, List<NoticeResulBean.DatasBean> data) {
        super(context, data);
    }

    @Override
    protected void convert(ViewHolder holder, NoticeResulBean.DatasBean item) {
        holder.textTitle.setText(item.getTitle());
        holder.textDate.setText(BaseUtils.convertToStr(item.getUpdateTime()));
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateItemView(R.layout.dialog_activity_item, parent));
    }


    static class ViewHolder extends BaseViewHolder {
        @Bind(R.id.text_title)
        TextView textTitle;
        @Bind(R.id.text_date)
        TextView textDate;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}