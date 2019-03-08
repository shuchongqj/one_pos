package com.gzdb.quickbuy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.OrderDetailItemBean;

import java.util.List;

/**
 * Created by Zxy on 2016/12/13.
 * 订单详情适配器
 */

public class OrderDetailAdapter extends BaseAdapter {

    Context cx;
    List<OrderDetailItemBean> lists;

    public OrderDetailAdapter(Context cx, List<OrderDetailItemBean> listBean) {
        this.cx = cx;
        this.lists = listBean;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            convertView = View.inflate(cx, R.layout.order_detail_item, null);
            viewHolder = new ViewHolder((ViewGroup)convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrderDetailItemBean itemsBean = lists.get(position);
        viewHolder.itemName.setText(itemsBean.getItemName()+"");
//        viewHolder.itemSun.setText("x"+itemsBean.getQuantity());
        viewHolder.itemMoney.setText("￥"+itemsBean.getTotalPrice());
        viewHolder.itemReceiptQuantity.setText("已收货："+itemsBean.getReceiptQuantity());
        viewHolder.itemDeliveryQuantity.setText("配送中："+itemsBean.getDistributionQuantity());
//        viewHolder.itemNotDeliveryQuantity.setText("未配送："+itemsBean.getNotDeliveryQuantity());
        return convertView;
    }

    public class ViewHolder {

        TextView itemName;
        TextView itemSun;
        TextView itemMoney;
        TextView itemReceiptQuantity;
        TextView itemDeliveryQuantity;
        TextView itemNotDeliveryQuantity;

        public ViewHolder(ViewGroup vg){
            itemName = (TextView) vg.findViewById(R.id.item_name);
            itemSun = (TextView) vg.findViewById(R.id.item_sun);
            itemMoney = (TextView) vg.findViewById(R.id.item_money);
            itemReceiptQuantity = (TextView) vg.findViewById(R.id.item_receiptQuantity);
            itemDeliveryQuantity = (TextView) vg.findViewById(R.id.item_deliveryQuantity);
            itemNotDeliveryQuantity = (TextView) vg.findViewById(R.id.item_notDeliveryQuantity);
        }
    }
}
