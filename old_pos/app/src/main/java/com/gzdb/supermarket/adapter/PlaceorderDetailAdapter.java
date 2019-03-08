package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.entity.PlaceOderData;
import com.gzdb.supermarket.util.Arith;

import java.util.List;

/**
 * Created by dianba on 2016/5/11.
 * 商品显示列表
 */
public class PlaceorderDetailAdapter extends BaseAdapter {

    private int mLayoutInflater;
    private Context mContext;
    private List<PlaceOderData> listItem;

    public PlaceorderDetailAdapter(Context context, int layout, List<PlaceOderData> listitem) {
        this.mContext = context;
        this.mLayoutInflater = layout;
        this.listItem = listitem;
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
            mViewHolder = new ViewHolder();
            convertView = View.inflate(mContext, mLayoutInflater, null);
            mViewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            mViewHolder.mCommodityName = (TextView) convertView.findViewById(R.id.commodity_name);
            mViewHolder.mCommodityPrice = (TextView) convertView.findViewById(R.id.commodity_price);
            mViewHolder.mCommodityCount = (TextView) convertView.findViewById(R.id.commodity_count);
            mViewHolder.promotionPrice = (TextView) convertView.findViewById(R.id.promotionPrice);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        PlaceOderData mPlaceOderData = listItem.get(position);
        SpannableStringBuilder builderMoney = new SpannableStringBuilder("￥");
        builderMoney.setSpan(new AbsoluteSizeSpan((int) mContext.getResources().getDimension(R.dimen.font_14)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderMoney.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.font_6)), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder builderUnit = new SpannableStringBuilder(mPlaceOderData.getCount() + "/" + mPlaceOderData.getUnit());
        builderUnit.setSpan(new AbsoluteSizeSpan((int) mContext.getResources().getDimension(R.dimen.font_14)), mPlaceOderData.getCount().length(), builderUnit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builderUnit.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.font_6)), mPlaceOderData.getCount().length(), builderUnit.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mViewHolder.mCommodityCount.setText(builderUnit);
        mViewHolder.mCommodityName.setText(mPlaceOderData.getName());
        mViewHolder.mCommodityPrice.setText(builderMoney.replace(1, builderMoney.length(), mPlaceOderData.getPrice()));
        //促销
        if ("N".equals(mPlaceOderData.getSalesPromotion())) {
            //无网络状态时就为会null，此时本地计算价格
            String temp = mPlaceOderData.getPromotionPrice() == null ? "" + Arith.mul(mPlaceOderData.getCount(), mPlaceOderData.getPrice()) : mPlaceOderData.getPromotionPrice();
            mViewHolder.promotionPrice.setText(builderMoney.replace(1, builderMoney.length(), temp));
            String temp2 = mPlaceOderData.getDiscountMoney() == null ? "0" : mPlaceOderData.getDiscountMoney();
        } else {
            //无网络状态时就为会null，此时本地计算价格
            String temp = mPlaceOderData.getPromotionPrice() == null ? "" + Arith.mul(mPlaceOderData.getCount(), mPlaceOderData.getPrice()) : mPlaceOderData.getPromotionPrice();
            mViewHolder.promotionPrice.setText(builderMoney.replace(1, builderMoney.length(), temp));
            String temp2 = mPlaceOderData.getDiscountMoney() == null ? "0" : mPlaceOderData.getDiscountMoney();
        }

        if (position % 2 != 0) {
            mViewHolder.ll_item.setBackgroundColor(Color.parseColor("#f2f2f2"));
        } else {
            mViewHolder.ll_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    public class ViewHolder {
        private LinearLayout ll_item;
        private TextView mCommodityName;//商品名称
        private TextView mCommodityPrice;//商品单价
        private TextView mCommodityCount;//购买商品的数量
        private TextView promotionPrice;//优惠后金额
    }


}
