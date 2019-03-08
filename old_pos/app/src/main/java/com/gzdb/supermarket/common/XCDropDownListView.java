package com.gzdb.supermarket.common;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzdb.basepos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Even on 2016/8/4.
 */
public class XCDropDownListView extends LinearLayout {

    private TextView editText;
    private ImageView imageView;
    private PopupWindow popupWindow = null;
    private ArrayList<XCDropDownItem> dataList = new ArrayList<XCDropDownItem>();
    private View mView;
    private XCDropDownListAdapter adapter;
    private View contentView;
    private ListView listView;
    //当前选中的
    private XCDropDownItem selectedItem;
    private SelectCallback selectCallback;
    private Activity mContext;

    public XCDropDownListView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public void setSelectCallback(SelectCallback selectCallback) {
        this.selectCallback = selectCallback;
    }

    public XCDropDownListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public XCDropDownListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView();
    }

    public void initView() {
        String infServie = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);

        layoutInflater = (LayoutInflater) getContext().getSystemService(infServie);
        contentView = layoutInflater.inflate(R.layout.dropdownlist_popupwindow, null, false);
        listView = (ListView) contentView.findViewById(R.id.listView);
        adapter = new XCDropDownListAdapter(getContext(), dataList);
        listView.setAdapter(adapter);
        View view = layoutInflater.inflate(R.layout.layout_up_or_down, this, true);
        editText = (TextView) view.findViewById(R.id.text);
        imageView = (ImageView) view.findViewById(R.id.btn);
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (popupWindow == null) {
                    showPopWindow();
                } else {
                    closePopWindow();
                }
            }
        });
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow() {
        // 加载popupWindow的布局文件
        popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(this);
    }


    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
        popupWindow = null;
    }

    public void addItem(XCDropDownItem string) {
        dataList.add(string);
        selectedItem = string;
        editText.setText(string.getXCDropDownItemText());
        adapter.notifyDataSetChanged();
    }

    public List<XCDropDownItem> getItems() {
        return this.dataList;
    }

    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    public void setShowIndex(int index) {
        selectedItem = dataList.get(index);
        editText.setText(selectedItem.getXCDropDownItemText());
    }

    public XCDropDownItem getSelectedItem() {
        return selectedItem;
    }

    /**
     * 数据适配器
     */
    class XCDropDownListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<XCDropDownItem> mData;
        LayoutInflater inflater;

        public XCDropDownListAdapter(Context ctx, ArrayList<XCDropDownItem> data) {
            mContext = ctx;
            mData = data;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            // 自定义视图
            ListItemView listItemView = null;
            if (convertView == null) {
                // 获取list_item布局文件的视图
                convertView = inflater.inflate(R.layout.dropdown_list_item, null);

                listItemView = new ListItemView();
                // 获取控件对象
                listItemView.tv = (TextView) convertView.findViewById(R.id.tv);

                listItemView.layout = (LinearLayout) convertView.findViewById(R.id.layout_container);

                listItemView.layout.setOnClickListener(listItemView);

                // 设置控件集到convertView
                convertView.setTag(listItemView);
            } else {
                listItemView = (ListItemView) convertView.getTag();
            }

            // 设置数据
            listItemView.selectItem = mData.get(position);
            listItemView.index = position;
            listItemView.tv.setText(listItemView.selectItem.getXCDropDownItemText());

            return convertView;
        }

    }

    class ListItemView implements OnClickListener {
        TextView tv;
        LinearLayout layout;
        XCDropDownItem selectItem;
        int index;

        @Override
        public void onClick(View v) {
            editText.setText(selectItem.getXCDropDownItemText());
            selectedItem = selectItem;
            closePopWindow();
            if (selectCallback != null) {
                selectCallback.selected(index);
            }
        }
    }

    public interface SelectCallback {
        public void selected(int index);
    }

    public static interface XCDropDownItem {
        public String getXCDropDownItemText();

        public int getXCDropDownItemType();
    }

    public static class XCDropDownItemStr implements XCDropDownItem {

        private String name;
        private int type;

        public XCDropDownItemStr(String name) {
            this.name = name;
        }

        public XCDropDownItemStr(String name, int type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public String getXCDropDownItemText() {
            return name;
        }

        public int getXCDropDownItemType() {
            return type;
        }
    }

    public void setListviewWH(int width, int height) {
        LayoutParams lp;
        lp = (LayoutParams) listView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        listView.setLayoutParams(lp);
    }


}
