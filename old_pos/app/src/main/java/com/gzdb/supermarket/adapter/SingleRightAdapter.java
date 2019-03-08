package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.cache.ShopCart;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Even on 2016/6/1.
 */
public class SingleRightAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopCart.ShopCartItem> listItem = new ArrayList<>();

    public SingleRightAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<ShopCart.ShopCartItem> listItem) {
        this.listItem.clear();
        if (listItem != null) {
            this.listItem.addAll(listItem);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (mViewHolder == null) {
            convertView = View.inflate(mContext, R.layout.layout_settlement_item, null);
            mViewHolder = new ViewHolder(convertView);
            //设置item字体和背景颜色
            convertView.setBackgroundColor(Color.parseColor("#d7f0ff"));
            mViewHolder.orderType.setTextColor(Color.parseColor("#0d6396"));
            mViewHolder.orderCount.setTextColor(Color.parseColor("#0d6396"));
            mViewHolder.orderTotal.setTextColor(Color.parseColor("#0d6396"));
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ShopCart.ShopCartItem carItem = listItem.get(position);
        mViewHolder.orderType.setText(carItem.item.getItemName()+"");
        mViewHolder.orderCount.setText(carItem.item.getSalesPrice() + "");//商品价格
        mViewHolder.orderTotal.setText(carItem.count + "");//商品数量
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.item_line)
        View itemLine;
        @Bind(R.id.order_type)
        TextView orderType;
        @Bind(R.id.order_count)
        TextView orderCount;
        @Bind(R.id.order_total)
        TextView orderTotal;
        @Bind(R.id.settlement_layout)
        LinearLayout settlementLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
