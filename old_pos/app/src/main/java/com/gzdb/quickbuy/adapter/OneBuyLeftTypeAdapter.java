package com.gzdb.quickbuy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.OneBuyTypeBean;
import com.gzdb.supermarket.adapter.BaseGenericAdapter;

import java.util.List;

import static com.gzdb.basepos.R.id.select_line;
import static com.gzdb.basepos.R.id.txt_menu_name;

/**
 * Created by Zxy on 2017/1/10.
 * 一键采购---左边类型
 */

public class OneBuyLeftTypeAdapter extends BaseGenericAdapter implements View.OnClickListener {

    //当前选中的
    private int select_position = 0;
    private Runnable runnable;

    public OneBuyLeftTypeAdapter(Context context, List list, Runnable runnable) {
        super(context, list);
        this.runnable = runnable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (holder == null) {
            convertView = mInflater.inflate(R.layout.layout_menu_type_item, null);
            holder = new ViewHolder();
            holder.select_line = convertView.findViewById(select_line);
            holder.txt_menu_name = (TextView) convertView.findViewById(txt_menu_name);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OneBuyTypeBean oneBuyTypeBean = (OneBuyTypeBean) list.get(position);
        if (oneBuyTypeBean.isSelect()) {
            holder.select_line.setBackgroundResource(R.mipmap.select);
            holder.txt_menu_name.setTextColor(context.getResources().getColor(R.color.theme));
        } else {
            holder.select_line.setBackgroundColor(context.getResources().getColor(R.color.white_color));
            holder.txt_menu_name.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.txt_menu_name.setText(oneBuyTypeBean.getTitle());
        holder.position = position;

        return convertView;
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag();
        this.select_position = holder.position;
        Log.e("zhang", "点击了" + select_position);
        if (runnable != null) {
            runnable.run();
        }
        notifyDataSetChanged();//刷新
    }

    public int getSelectPosition() {
        return select_position;
    }

    public static class ViewHolder {
        private View select_line;
        private TextView txt_menu_name;
        private int position;
    }
}
