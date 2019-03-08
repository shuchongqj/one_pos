package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.SettlementResultBean;

import java.util.List;

/**
 * Created by zhumg on 8/5.
 */
public class SettlementAdapter extends BaseAdapter {
    private int mLayoutInflater;
    private Context mContext;
    private List<SettlementResultBean.DatasBean> listItem;

    public SettlementAdapter(Context context,int layout,List<SettlementResultBean.DatasBean> listitem){
        this.mContext=context;
        this.mLayoutInflater = layout;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (mViewHolder == null){
            mViewHolder = new ViewHolder();
            convertView=View.inflate(mContext, mLayoutInflater, null);
            convertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
            mViewHolder.ordreType = (TextView) convertView.findViewById(R.id.order_type);
            mViewHolder.orderCount = (TextView) convertView.findViewById(R.id.order_count);
            mViewHolder.orderTotal = (TextView) convertView.findViewById(R.id.order_total);
            mViewHolder.item_line =  convertView.findViewById(R.id.item_line);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        SettlementResultBean.DatasBean settlementOrder = listItem.get(position);

//        if(settlementOrder.getSoType() == 1) {
//            mViewHolder.ordreType.setText(settlementOrder.getQuantity());
//            mViewHolder.orderCount.setText("");
//            mViewHolder.orderTotal.setText("");
//            mViewHolder.item_line.setVisibility(View.VISIBLE);
//        }else {
            mViewHolder.ordreType.setText(settlementOrder.getPaymentTypeTitle());
            mViewHolder.orderCount.setText(settlementOrder.getCount()+"");
            mViewHolder.orderTotal.setText("￥" + settlementOrder.getAmount());
//        }
        return convertView;
    }


    public class ViewHolder{
        private TextView ordreType;
        private TextView orderCount;
        private TextView orderTotal;
        private View item_line;
    }
}
