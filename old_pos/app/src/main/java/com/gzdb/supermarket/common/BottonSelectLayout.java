package com.gzdb.supermarket.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.BottonItem;

import java.util.ArrayList;

/**
 * Author: even
 * Date:   2019/3/1
 * Description:
 */
public class BottonSelectLayout extends LinearLayout {

    private ArrayList<BottonItem> bottonItems = getResource();
    private GridView mGridview;
    private BottonItem bottonItem;
    private MlayoutAdapter adapter;
    private mlayoutClickListener mListener;
    public interface mlayoutClickListener {
         void onItemClick(BottonItem mEntity);
    }

    public void setMLayoutClickListener(mlayoutClickListener listener) {
        mListener = listener;
    }

    private void init() {
        if (isInEditMode())
            return;

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_menu_gridview, this);
        mGridview = (GridView) view.findViewById(R.id.gridView);
        adapter = new MlayoutAdapter();
        mGridview.setAdapter(adapter);
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (bottonItem != null) {
                    bottonItem.setIsClick("false");
                }
                bottonItem = bottonItems.get(position);
                bottonItem.setIsClick("true");
                adapter.notifyDataSetChanged();
                if (mListener != null) {
                    mListener.onItemClick(bottonItem);
                }
            }
        });
    }
    public BottonSelectLayout(Context context) {
        super(context);
        init();
    }

    public BottonSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottonSelectLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    public class MlayoutAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bottonItems.size();
        }

        @Override
        public Object getItem(int position) {
            return bottonItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_botton_select, null);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BottonItem entity = (BottonItem) getItem(position);
            holder.textView.setText(entity.getName());
            //判断textview是否被点击,true点击，false没点击
            if ("true".equals(entity.getIsClick())) {
                holder.textView.setTextColor(getResources().getColor(R.color.blue));
                holder.textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.old_shape_fare_bg_press));
            } else {
                holder.textView.setTextColor(getResources().getColor(R.color.font_6));
                holder.textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.old_shape_fare_bg_normal));
            }
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }

    public ArrayList<BottonItem> getResource() {
        ArrayList<BottonItem> list = new ArrayList<BottonItem>();
        BottonItem entity = new BottonItem("老");
        entity.setIsClick("false");
        list.add(entity);

        BottonItem entity1 = new BottonItem("中");
        entity1.setIsClick("false");
        list.add(entity1);

        BottonItem entity2 = new BottonItem("青");
        entity1.setIsClick("false");
        list.add(entity2);
        return list;
    }

}
