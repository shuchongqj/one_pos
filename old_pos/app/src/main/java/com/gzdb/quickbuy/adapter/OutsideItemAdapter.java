package com.gzdb.quickbuy.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.BuyOutsideBean;

import java.util.List;

/**
 * Created by Zxy on 2017/1/9.
 * 系统外建议采购--适配器
 */

public class OutsideItemAdapter extends BaseAdapter {

    Context cx;
    List<BuyOutsideBean> buyOutsideBeanlist;

    public OutsideItemAdapter(Context context, List lists) {
        this.cx = context;
        this.buyOutsideBeanlist = lists;
    }

    @Override
    public int getCount() {
        return buyOutsideBeanlist.size();
    }

    @Override
    public Object getItem(int position) {
        return buyOutsideBeanlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = View.inflate(cx, R.layout.outsidebuy_item, null);
            holder = new ViewHolder((ViewGroup) convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BuyOutsideBean buyOutsideBean = buyOutsideBeanlist.get(position);
        if (buyOutsideBean.getSelectItem() == null) {
            buyOutsideBean.setSelectItem(buyOutsideBean.getItems().get(0));
        }
        BuyOutsideBean.QuickItem selectItem = buyOutsideBean.getSelectItem();

        holder.out_name.setText(selectItem.getName());
        holder.out_sales_num.setText(selectItem.getDefaultPurchase()+"");
        holder.out_standard.setText(selectItem.getUnit());
        holder.out_proposalbuy.setText("建议采购" + selectItem.getDefaultPurchase());
        holder.out_proposalbuy.setTextColor(ContextCompat.getColor(cx, R.color.red));
        return convertView;
    }

    public class ViewHolder {
        TextView out_name, out_sales_num, out_standard, out_proposalbuy;

        public ViewHolder(ViewGroup vg) {
            out_name = (TextView) vg.findViewById(R.id.out_name);//名称
            out_standard = (TextView) vg.findViewById(R.id.out_standard);  //规格
            out_proposalbuy = (TextView) vg.findViewById(R.id.out_proposalbuy);//进货价毛利率
            out_sales_num = (TextView) vg.findViewById(R.id.out_sales_num);// 近三天销量
        }
    }
}
