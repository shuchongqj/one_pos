package com.one.pos;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;

import java.util.List;

/**
 * @author zhumg
 */
public class TestAdapter extends AbstractListAdapter<String> {

    public TestAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    public int getViewType(int position) {
        return R.layout.adapter_test;
    }

    @Override
    public void refreshData(BaseAdapterHolder<String> holder, View view, String s, int viewType, int position) {
        holder.setText(R.id.tv_title, s);
    }

}
