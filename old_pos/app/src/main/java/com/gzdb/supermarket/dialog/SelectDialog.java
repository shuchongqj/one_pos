package com.gzdb.supermarket.dialog;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzdb.basepos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubolin on 2017/6/1.
 */

public class SelectDialog extends PopupWindow {

    private Activity mContext;

    private List<String> mListData = new ArrayList<>();

    private ListView mListView;

    private SelectAdapter mAdapter;

    private SelectDialogHost mHost;

    public interface SelectDialogHost {
        void selectDialog(int position);
    }

    public SelectDialog(@NonNull Activity context, SelectDialogHost host) {
        super(context);
        mContext = context;
        mHost = host;
        init();
    }

    public void setListData(List<String> data) {
        if (null == data || data.size() == 0) {
            return;
        }
        mListData.clear();
        mListData.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_layout, null);
        mListView = (ListView) view.findViewById(R.id.select_list);
        mAdapter = new SelectAdapter();

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mHost) {
                    mHost.selectDialog(position);
                }
                dismiss();
            }
        });
        setContentView(view);

        // 点击外部消失
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void showViewSize(View view) {
        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        setHeight((int) (d.getHeight() * 0.5));
        setWidth(view.getWidth());
    }

    class SelectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_select_item, null);

            }
            TextView item = (TextView) convertView.findViewById(R.id.select_item);
            item.setText(getItem(position).toString());
            return convertView;
        }
    }

}
