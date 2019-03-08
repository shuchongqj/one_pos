package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.entity.PlaceOderData;

import java.util.List;

/**
 * Created by Even on 2016/6/1.
 *立即下单错误信息提示
 */
public class PlaceOrderAdapter extends BaseAdapter{
    private Context mContext;
    private List<PlaceOderData> listItem;

    public PlaceOrderAdapter(Context context, List<PlaceOderData> listitem){
        this.mContext=context;
        this.listItem=listitem;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (mViewHolder == null){
            mViewHolder = new ViewHolder();
            convertView=View.inflate(mContext, R.layout.layout_settlement_itemtip, null);
            mViewHolder.ordreType = (TextView) convertView.findViewById(R.id.order_type);
            mViewHolder.orderCount = (TextView) convertView.findViewById(R.id.order_count);
            mViewHolder.orderTotal = (TextView) convertView.findViewById(R.id.order_total);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        PlaceOderData placeOderData = listItem.get(position);
        mViewHolder.ordreType.setText(placeOderData.getName());
        mViewHolder.orderCount.setText("￥"+placeOderData.getPrice());
        mViewHolder.orderTotal.setText(placeOderData.getErrMsg());
        return convertView;
    }

    public class ViewHolder{
        private TextView ordreType;
        private TextView orderCount;
        private TextView orderTotal;
    }
}
