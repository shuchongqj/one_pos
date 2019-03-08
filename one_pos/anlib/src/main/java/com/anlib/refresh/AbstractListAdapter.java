package com.anlib.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *
 * @author zhumg
 * @date 3/23
 */
public abstract class AbstractListAdapter<DATA> extends BaseAdapter implements AdapterView.OnItemClickListener {

    //数据索引起点，如果有设置 headView , 则需要数据索引+1
    protected int index_start = 0;
    //数据
    protected List<DATA> datas;

    protected Context context;

    protected AdapterClickListener<DATA> listener;

    public void setAdapterClickListener(AdapterClickListener<DATA> listener) {
        this.listener = listener;
    }

    public AbstractListAdapter(Context context, List<DATA> datas) {
        this.datas = datas;
        this.context = context;
    }

    public void haveHeadView() {
        this.index_start = 1;
    }

    public void addMore(List<DATA> more) {
        if (more != null && more.size() > 0) {
            this.datas.addAll(more);
        }
    }

    public void addMoreAndRefresh(List<DATA> more) {
        this.addMore(more);
        this.notifyDataSetChanged();
    }

    public void add(DATA data) {
        this.datas.add(data);
    }

    public void clearAndRefresh(List<DATA> models) {
        this.datas.clear();
        this.refresh(models);
        this.notifyDataSetChanged();
    }


    public void refresh(List<DATA> models) {
        if (models != null && models.size() > 0) {
            this.datas.addAll(models);
        }
    }

    public void remove(int position) {
        int p = position - index_start;
        this.datas.remove(p);
    }

    public void removeAll() {
        this.datas.clear();
    }

    public List<DATA> getDatas() {
        return this.datas;
    }

    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public DATA getItem(int position) {
        int p = position - index_start;
        if (p < 0) {
            p = 0;
        }
        return this.datas.get(p);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.listener != null) {
            int p = position - index_start;
            if (p < 0) {
                return;
            }
            this.listener.onItemClick(parent, this.datas.get(p), p);
        }
    }

    /**
     *
     * @param position
     * @return
     */
    public abstract int getViewResId(int position);

    /**
     *
     * @param view
     * @param data
     * @param viewType
     * @return
     */
    public BaseAdapterHolder<DATA> createHolder(View view, DATA data, int viewType) {
        return new BaseAdapterHolder(view, data, viewType);
    }

    /**
     *
     * @param holder
     * @param view          视图
     * @param data          数据
     * @param viewType      视图类型
     * @param position      位置
     */
    public abstract void refreshData(BaseAdapterHolder<DATA> holder, View view, DATA data, int viewType, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DATA data = getItem(position);
        int viewResId = getViewResId(position);
        BaseAdapterHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, viewResId, null);
            holder = createHolder(convertView, data, viewResId);
            convertView.setTag(holder);
        } else {
            holder = (BaseAdapterHolder) convertView.getTag();
        }
        int p = position - index_start;
        refreshData(holder, convertView, data, viewResId, p);
        return convertView;
    }

}
