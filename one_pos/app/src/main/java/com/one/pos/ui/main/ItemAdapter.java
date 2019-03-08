package com.one.pos.ui.main;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;
import com.one.pos.R;
import com.one.pos.db.Item;

import java.util.List;


/**
 * @author zhumg
 */
public class ItemAdapter extends AbstractListAdapter<Item> {

    public ItemAdapter(Context context, List<Item> datas) {
        super(context, datas);
    }

    @Override
    public int getViewResId(int position) {
        return R.layout.main_adapter_item;
    }

    @Override
    public void refreshData(BaseAdapterHolder<Item> holder, View view, Item itemModel, int viewType, int position) {
        holder.setText(R.id.tv_name, itemModel.getName());
        holder.setText(R.id.tv_vip_price, "会员价格：" + itemModel.getVipPrice());
        holder.setText(R.id.tv_price, "原价：" + itemModel.getPrice());
        holder.setText(R.id.tv_storage, "所属货架：" + itemModel.getStorage());
        holder.setText(R.id.tv_stock, "剩余库存：" + itemModel.getStock());
    }

}