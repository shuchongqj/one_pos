package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.cache.ShopCart;

import java.util.List;

/**
 * Created by Even on 2016/6/1.
 */
public class SingleLeftAdapter extends BaseAdapter{
    private int clickPossition=0;

    private Context mContext;
    private List<ShopCart> listItem;
    private SingleRightAdapter rightAdapter;

    public SingleLeftAdapter(Context context, List<ShopCart> listitem){
        this.mContext=context;
        this.listItem=listitem;
    }

    public void setSingleRightAdapter(SingleRightAdapter adapter) {
        this.rightAdapter = adapter;
    }

    public void setClickPossition(int clickPossition){
        this.clickPossition=clickPossition;
        notifyDataSetChanged();
    }
    public int getClickPossition(){
        return clickPossition;
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
            convertView=View.inflate(mContext, R.layout.layout_settlement_item, null);
            mViewHolder.ordreType = (TextView) convertView.findViewById(R.id.order_type);
            mViewHolder.orderCount = (TextView) convertView.findViewById(R.id.order_count);
            mViewHolder.orderTotal = (TextView) convertView.findViewById(R.id.order_total);
            mViewHolder.settlementLayout =  convertView.findViewById(R.id.settlement_layout);
            convertView.setOnClickListener(mViewHolder);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        ShopCart car = listItem.get(position);
        if(position == clickPossition){
            mViewHolder.settlementLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
        }else{
            mViewHolder.settlementLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white2));
        }
        mViewHolder.position = position;
        mViewHolder.ordreType.setText(car.getTime());
        mViewHolder.orderCount.setText(car.getAllPrice()+"");
        mViewHolder.orderTotal.setText(car.getUsername());
        mViewHolder.orderTotal.setVisibility(View.GONE);
        return convertView;
    }


    public class ViewHolder implements View.OnClickListener {
        private TextView ordreType;
        private TextView orderCount;
        private TextView orderTotal;
        private View settlementLayout;
        public int position;
        @Override
        public void onClick(View v) {
            clickPossition = position;
            //取数据
            List<ShopCart.ShopCartItem> items = listItem.get(clickPossition).gets();
            rightAdapter.setList(items);
            notifyDataSetChanged();
        }
    }
}
