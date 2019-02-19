package com.one.pos.ui.main;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;

import java.util.List;

public class ItemTypeAdapter extends AbstractListAdapter<ItemTypeModel> {

    public ItemTypeAdapter(Context context, List<ItemTypeModel> datas) {
        super(context, datas);
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public void refreshData(BaseAdapterHolder<ItemTypeModel> holder, View view, ItemTypeModel itemTypeModel, int viewType, int position) {

    }
}
