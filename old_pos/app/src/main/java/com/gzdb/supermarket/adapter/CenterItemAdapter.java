package com.gzdb.supermarket.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhumg on 7/16.
 */
public class CenterItemAdapter extends BaseGenericAdapter<GoodBean> {

    private LayoutInflater mLayoutInflater;
    private SupermarketIndexActivity indexActivity;
    private FrameLayout.LayoutParams flParams;

    private ClickListener listener = null;

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        void onClick(GoodBean bean);
    }

    public CenterItemAdapter(Context context, List<GoodBean> list) {
        super(context, list);
        this.indexActivity = (SupermarketIndexActivity) context;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void notifyDataSort() {

        if (list.size() < 1) {
            super.notifyDataSetChanged();
            return;
        }

        List<GoodBean> items = new ArrayList<>();

        //将原来的-1位置的内容先不加进去
        for (int i = 0; i < list.size(); i++) {
            GoodBean item = getItem(i);
            if (-1 == item.getSortId()) {
                continue;
            }
            if ("Y".equals(item.getIsShelve())) {
                items.add(item);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            GoodBean item = getItem(i);
            if (-1 == item.getSortId()) {
                continue;
            }
            if ("N".equals(item.getIsShelve())) {
                items.add(item);
            }
        }

        List<GoodBean> mos = new ArrayList<>();

        int rowCount = -1;
        boolean addb = false;
        for (int i = 0; i < items.size(); i++) {
            GoodBean mo = items.get(i);
            rowCount++;
            if (rowCount == 4) {
                rowCount = 0;
            }
            if ("N".equals(mo.getIsShelve())) {
                if (!addb && rowCount > 0) {
                    int num = 4 - rowCount;
                    if (num > 0) {
                        for (int y = 0; y < num; y++) {
                            GoodBean mot = new GoodBean();
                            mot.setSortId(-1);
                            mos.add(mot);
                        }
                        rowCount = 0;
                    }
                }
                addb = true;
                mos.add(mo);
            } else {
                if ("Y".equals(mo.getIsShelve())) {
                    mos.add(mo);
                }
            }
        }
        list.clear();
        list.addAll(mos);

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder mViewHolder = null;
        GoodBean item = getItem(position);
        if (mViewHolder == null) {
            mViewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_supermarket_gridview_item, null);
            mViewHolder.supermarket_shoplayout = convertView;
            mViewHolder.mShopName = (TextView) convertView.findViewById(R.id.supermarket_shopname);
            mViewHolder.mShopPrice = (TextView) convertView.findViewById(R.id.supermarket_shopprice);
            mViewHolder.mKu = (TextView) convertView.findViewById(R.id.ku);
            mViewHolder.vip_price = (TextView) convertView.findViewById(R.id.vip_price);
            mViewHolder.iv_sale1 = (ImageView) convertView.findViewById(R.id.iv_sale1);
            mViewHolder.iv_sale2 = (ImageView) convertView.findViewById(R.id.iv_sale2);
            mViewHolder.ll_vip_price = (LinearLayout) convertView.findViewById(R.id.ll_vip_price);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        if (-1 == item.getSortId()) {
            mViewHolder.supermarket_shoplayout.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            mViewHolder.mShopName.setText("");
            mViewHolder.mShopPrice.setText("");
            mViewHolder.mKu.setText("");
            return convertView;
        }

        mViewHolder.item = item;
//        if (User.nowLoginUser.getCashierType() == 2) {//店长
        mViewHolder.supermarket_shoplayout.setOnLongClickListener(mViewHolder);
        mViewHolder.supermarket_shoplayout.setOnTouchListener(mViewHolder);
//        }

        //上下架背景色
        if ("N".equals(item.getIsShelve())) {
            mViewHolder.supermarket_shoplayout.setBackgroundColor(context.getResources().getColor(R.color.gray2));
        }
        if (item.getMembershipPrice() > 0) {
            mViewHolder.iv_sale1.setVisibility(View.VISIBLE);
            mViewHolder.ll_vip_price.setVisibility(View.VISIBLE);
            mViewHolder.vip_price.setText("¥ " + item.getMembershipPrice());
        } else {
            mViewHolder.iv_sale1.setVisibility(View.INVISIBLE);
        }
        mViewHolder.mShopName.setText(item.getItemName());
        mViewHolder.mShopPrice.setText("¥ " + item.getSalesPrice());
        mViewHolder.mShopPrice.setTextColor(ContextCompat.getColor(context, R.color.orange2));
        if (item.getItemType() == 2) {
            mViewHolder.mShopPrice.setText("¥ " + item.getSalesPrice() + "/千克");
            mViewHolder.mKu.setVisibility(View.INVISIBLE);
        } else {
            mViewHolder.mShopPrice.setText("¥ " + item.getSalesPrice());
            mViewHolder.mKu.setVisibility(View.VISIBLE);
            mViewHolder.mKu.setText("剩余库存:" + item.getRepertory());
        }

        if (item.getActivityType() > 0 && item.getState() == 1) {
            mViewHolder.iv_sale2.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.iv_sale2.setVisibility(View.INVISIBLE);
        }
        convertView.setOnClickListener(mViewHolder);
        return convertView;
    }

    private View ak_gridView = null;

    private class ViewHolder implements View.OnLongClickListener, View.OnTouchListener, View.OnClickListener {

        private TextView mShopName;//商品名
        private TextView mShopPrice;//商品价格
        private TextView mKu;//剩余库存
        private TextView vip_price;//促销价格
        private View supermarket_shoplayout;
        private ImageView iv_sale1;
        private ImageView iv_sale2;
        private GoodBean item;
        private LinearLayout ll_vip_price;

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public boolean onLongClick(View v) {
            Log.e("onLongClick", "onLongClick");
            //商品拖动
            if (flParams == null) {
                ak_gridView = View.inflate(indexActivity, R.layout.layout_supermarket_gridview_item, null);
                flParams = new FrameLayout.LayoutParams(150, LinearLayout.LayoutParams.WRAP_CONTENT);
                indexActivity.getDrapLayout().addView(ak_gridView, flParams);
            }
            indexActivity.getTouchLayout().setItem(item);

            indexActivity.getTouchLayout().setDrapView(ak_gridView, v, indexActivity.getDrapLayout());

            int location[] = new int[2];
            v.getLocationOnScreen(location);

            flParams.leftMargin = location[0];
            flParams.topMargin = location[1] - (int) BaseUtils.dp2px(context, 48);

            ak_gridView.setLayoutParams(flParams);
            ak_gridView.setVisibility(View.VISIBLE);
            indexActivity.getTouchLayout().bringChildToFront(ak_gridView);//置顶显示

            TextView mShopName1 = (TextView) ak_gridView.findViewById(R.id.supermarket_shopname);
            TextView mShopPrice1 = (TextView) ak_gridView.findViewById(R.id.supermarket_shopprice);
            TextView mKu1 = (TextView) ak_gridView.findViewById(R.id.ku);
            TextView vip_price = (TextView) ak_gridView.findViewById(R.id.vip_price);
            ImageView iv_sale1 = (ImageView) ak_gridView.findViewById(R.id.iv_sale1);
            ImageView iv_sale2 = (ImageView) ak_gridView.findViewById(R.id.iv_sale2);

            //假如促销
            vip_price.setVisibility(View.INVISIBLE);
            iv_sale1.setVisibility(View.INVISIBLE);
            iv_sale2.setVisibility(View.INVISIBLE);
            mShopName1.setText(mShopName.getText());
            mShopPrice1.setText(mShopPrice.getText());
            mShopPrice1.setTextColor(ContextCompat.getColor(context, R.color.orange2));
            mKu1.setText(mKu.getText());

            indexActivity.getDrapLayout().setVisibility(View.VISIBLE);

            //隐藏当前长按的对象
            v.setVisibility(View.INVISIBLE);

            //显示需要被拖动的view
            indexActivity.getTouchLayout().setVisibility(View.VISIBLE);

            //设置开始拦截事件
            indexActivity.getTouchLayout().setInterceptTouch(true);

            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                if (indexActivity.getTouchLayout().isCanCallCancel()) {
                    indexActivity.getTouchLayout().touchCancel();
                }
            }
            return false;
        }

        @Override
        public void onClick(View view) {
            if ("N".equals(item.getIsShelve())) {
                ToastUtil.showToast(context, item.getItemName() + "已下架");
            } else {

                if (item.getItemType() == 2) {
                    listener.onClick(item);
                } else {
                    if (!ShopCart.nowShopCart.addItem(item)) {
                        ToastUtil.showToast(CenterItemAdapter.this.context, "库存不足");
                        return;
                    }
                    if (!Sunmi.viceScreenMode && App.mSerialPortOperaion != null) {
                        try {
                            App.mSerialPortOperaion.WriteData(0xC);
                            String str = item.getSalesPrice() + "";
                            App.mSerialPortOperaion.WriteData(27, 81, 65);
                            App.mSerialPortOperaion.WriteData(Utils.input(str));
                            App.mSerialPortOperaion.WriteData(13);
                            App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X31);//单价
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //刷新购物车
                    indexActivity.getSupermarketShopView().refreshShopCart();
                }
            }
        }
    }

}
