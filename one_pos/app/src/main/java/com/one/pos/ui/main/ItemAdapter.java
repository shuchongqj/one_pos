package com.one.pos.ui.main;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;

import java.util.List;

public class ItemAdapter extends AbstractListAdapter<ItemModel> {

    public ItemAdapter(Context context, List<ItemModel> datas) {
        super(context, datas);
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }

    @Override
    public void refreshData(BaseAdapterHolder<ItemModel> holder, View view, ItemModel itemModel, int viewType, int position) {

    }
}
