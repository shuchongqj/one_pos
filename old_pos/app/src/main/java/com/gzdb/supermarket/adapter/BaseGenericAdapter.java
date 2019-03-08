package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhumg on 7/16.
 */
public abstract class BaseGenericAdapter<T> extends BaseAdapter {

    public final String TAG = getClass().getSimpleName();
    protected List<T> list;
    protected Context context;
    protected LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局

    public BaseGenericAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.isEmpty()?0:list.size();
    }

    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup viewGroup);

    public void clearList() {
        list.clear();
    }

    public void setList(List<T> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    public void delete(int pos) {
        list.remove(pos);
    }

    public List<T> getList() {
        return list;
    }

    public void addList(List<T> lists) {
        list.addAll(lists);
    }

    public void add(T t) {
        list.add(t);
    }

}
