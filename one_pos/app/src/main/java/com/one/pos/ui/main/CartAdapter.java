package com.one.pos.ui.main;

import android.content.Context;
import android.view.View;

import com.anlib.refresh.AbstractListAdapter;
import com.anlib.refresh.BaseAdapterHolder;

import java.util.List;

/**
 *
 * 购物车
 *
 * @author zhumg
 *
 */
public class CartAdapter extends AbstractListAdapter<CartModel> {

    public CartAdapter(Context context, List<CartModel> datas) {
        super(context, datas);
    }

    @Override
    public int getViewResId(int position) {
        return 0;
    }

    @Override
    public void refreshData(BaseAdapterHolder<CartModel> holder, View view, CartModel cartModel, int viewType, int position) {

    }
}
