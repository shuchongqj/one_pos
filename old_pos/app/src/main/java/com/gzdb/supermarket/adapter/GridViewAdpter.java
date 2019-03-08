package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coorchice.library.SuperTextView;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.MenuBean;

import java.util.List;

public class GridViewAdpter extends BaseAdapter {

    private Context context;
    private List<MenuBean> lists;
    private int mIndex;
    private int mPargerSize;


    public GridViewAdpter(Context context, List<MenuBean> lists,
                          int mIndex, int mPargerSize) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size() > (mIndex + 1) * mPargerSize ?
                mPargerSize : (lists.size() - mIndex * mPargerSize);
    }

    @Override
    public MenuBean getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0 + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0 + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_menu, null);
            holder.item = (SuperTextView) convertView.findViewById(R.id.item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //重新确定position因为拿到的总是数据源，数据源是分页加载到每页的GridView上的
        final int pos = position + mIndex * mPargerSize;
        //假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
        holder.item.setText(lists.get(pos).getName());
        holder.item.setSolid(Color.parseColor(lists.get(pos).getColor()));
        holder.item.setDrawable(lists.get(pos).getImgge());
        return convertView;
    }

    static class ViewHolder {
        private SuperTextView item;
    }
}
