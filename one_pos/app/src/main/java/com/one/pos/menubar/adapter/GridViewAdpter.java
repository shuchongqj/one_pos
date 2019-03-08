package com.one.pos.menubar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.coorchice.library.SuperTextView;
import com.one.pos.R;
import com.one.pos.menubar.MenuDialog;

import java.util.List;

/**
 * Author: even
 * Date:   2019/3/4
 * Description:
 */
public class GridViewAdpter extends BaseAdapter {
    private Context context;
    private List<MenuDialog.MenuBean> lists;
    private int mIndex;
    private int mPargerSize;


    public GridViewAdpter(Context context, List<MenuDialog.MenuBean> lists,
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
        return lists.size() > (mIndex + 1) * mPargerSize ? mPargerSize : (lists.size() - mIndex * mPargerSize);
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

    class ViewHolder {
        private SuperTextView item;
    }
}
