package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.SupermarketShopView;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.dialog.ShopCarNumDialog;
import com.gzdb.supermarket.util.Arith;

/**
 * Created by zhumg on 7/21.
 */
public class ShopCartAdapter extends BaseAdapter {

    private Context context;
    private SupermarketShopView supermarketShopView;
    private ShopCarNumDialog shopCarNumDialog;

    public ShopCartAdapter(Context context, SupermarketShopView supermarketShopView) {
        this.context = context;
        this.supermarketShopView = supermarketShopView;
    }

    @Override
    public int getCount() {
        return ShopCart.nowShopCart.gets().size();
    }

    @Override
    public Object getItem(int position) {
        return ShopCart.nowShopCart.gets().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_shop_cart_menu_item, null);
            holder.add_menu_img = (ImageView) convertView.findViewById(R.id.add_menu_img);
            holder.deleted_menu_img = (ImageView) convertView.findViewById(R.id.deleted_menu_img);
            holder.menu_count = (TextView) convertView.findViewById(R.id.menu_count);
            holder.menu_name = (TextView) convertView.findViewById(R.id.menu_name);
            holder.menu_price = (TextView) convertView.findViewById(R.id.menu_price);
            holder.sales_icon = (ImageView) convertView.findViewById(R.id.sales_icon);
            holder.sales_price = (TextView) convertView.findViewById(R.id.sales_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShopCart.ShopCartItem shopCartItem = ShopCart.nowShopCart.gets().get(position);
        holder.shopCartItem = shopCartItem;
        LogUtils.e(shopCartItem.item);

        if (shopCartItem.type == 2) {
            holder.menu_name.setText(shopCartItem.item.getItemName()+ "(" + shopCartItem.weight + "kg)");
            holder.add_menu_img.setVisibility(View.GONE);
            holder.menu_count.setVisibility(View.GONE);
            holder.menu_price.setText("￥ " + Arith.mul(Arith.mul(shopCartItem.item.getSalesPrice(), shopCartItem.weight),shopCartItem.discount));
        } else {
            holder.menu_name.setText(shopCartItem.item.getItemName());
            holder.add_menu_img.setVisibility(View.VISIBLE);
            holder.menu_count.setVisibility(View.VISIBLE);
            holder.menu_price.setText("￥ " + shopCartItem.item.getSalesPrice());
        }

        //非促销
        if (shopCartItem.item.getPromotionPrice() <= 0) {
            holder.sales_icon.setVisibility(View.GONE);
            holder.sales_price.setVisibility(View.GONE);
            holder.menu_count.setText(shopCartItem.count + "");
        } else {
            holder.sales_icon.setVisibility(View.VISIBLE);
            holder.sales_price.setVisibility(View.VISIBLE);
            holder.sales_price.setText("￥" + shopCartItem.item.getPromotionPrice());//促销价格
            holder.menu_name.setText(shopCartItem.item.getItemName());
            holder.menu_count.setText(shopCartItem.count + "");
            holder.menu_price.setText("￥ " + shopCartItem.item.getSalesPrice());
            holder.menu_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中间加一横
        }
        holder.add_menu_img.setOnClickListener(holder);
        holder.deleted_menu_img.setOnClickListener(holder);
        holder.menu_count.setOnClickListener(holder);

        return convertView;
    }

    private class ViewHolder implements View.OnClickListener {

        private ImageView deleted_menu_img;
        private ImageView add_menu_img;
        private TextView menu_count;
        private TextView menu_name;
        private TextView menu_price;
        private ShopCart.ShopCartItem shopCartItem;
        private ImageView sales_icon;
        private TextView sales_price;


        @Override
        public void onClick(View v) {
            if (v == add_menu_img) {
                if (!shopCartItem.add()) {
                    ToastUtil.showToast(context, "库存不足");
                    return;
                }
            } else if (v == deleted_menu_img) {
                if (!shopCartItem.del()) {
                    return;
                }
            } else if (v == menu_count) {
                shopCarNumDialog = new ShopCarNumDialog(context, new ShopCarNumDialog.PretermissionCarNum() {
                    @Override
                    public void pretermission(String quantity) {
                        int quantityAll = Integer.parseInt(quantity);
                        shopCartItem.EditorQuantity(quantityAll);
                        notifyDataSetChanged();
                        supermarketShopView.refreshShopCart();
                    }
                });
                shopCarNumDialog.show();
            }
            //刷新
            supermarketShopView.refreshShopCart();
        }
    }
}
