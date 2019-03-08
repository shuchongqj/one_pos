package com.one.pos.ui.main;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;
import com.one.pos.R;
import com.one.pos.db.ItemType;

import java.util.List;

/**
 * @author zhumg
 */
public class ItemTypeAdapter extends AbstractListAdapter<ItemType> {

    public ItemTypeAdapter(Context context, List<ItemType> datas) {
        super(context, datas);
    }

    @Override
    public int getViewResId(int position) {
        return R.layout.main_adapter_item_type;
    }

    @Override
    public void refreshData(BaseAdapterHolder<ItemType> holder, View view, ItemType itemTypeModel, int viewType, int position) {
        holder.setText(R.id.tv_name, itemTypeModel.getName());
        if(itemTypeModel.isSelect()) {
            holder.setVisibility(R.id.iv_select, View.VISIBLE);
        } else {
            holder.setVisibility(R.id.iv_select, View.INVISIBLE);
        }
    }
}
