package cc.solart.turbo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nongyd on 17/5/3.
 */


public abstract class BaseSinleChooseAdapter<T extends ChooseBean, VH extends BaseViewHolder> extends AbsTurboAdapter<T, BaseViewHolder> implements OnLoadMoreListener {

    protected static final String TAG = "BaseTurboAdapter";

    protected List<T> mData;
    private boolean mLoading = false;
    private boolean mEmptyEnable;
    private int lastChooseIndex = 0;
    private MAdapterOnClickListener<T> onClickListener;

    public BaseSinleChooseAdapter(Context context) {
        this(context, null,null);
    }

    /**
     * initialization
     *
     * @param context The context.
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public BaseSinleChooseAdapter(Context context, List<T> data,MAdapterOnClickListener<T> onClickListener) {
        super(context);
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.onClickListener=onClickListener;
    }

    public void chooseItem(int index,T item){

        if(lastChooseIndex>=getData().size()){
            Toast.makeText(mContext,"数据异常，请刷新重试!",Toast.LENGTH_LONG).show();
            return;
        }
        ChooseBean lastChooseBean = getData().get(lastChooseIndex);
        lastChooseBean.setSelected(false);
        notifyItemChanged(lastChooseIndex, lastChooseBean);
        lastChooseIndex = index;
        item.setSelected(true);
        notifyItemChanged(index, item);
        onClickListener.onItemClick(null,item,index);
    }

    public void chooseItem(int index){
        chooseItem(index,mData.get(index));
    }
    public void add(T item) {
        boolean isAdd = mData.add(item);
        if (isAdd)
            notifyItemInserted(mData.size() + getHeaderViewCount());
    }

    public void add(int position, T item) {
        if (position < 0 || position > mData.size()) {
            Log.e(TAG, "add position = " + position + ", IndexOutOfBounds, please check your code!");
            return;
        }
        mData.add(position, item);
        notifyItemInserted(position + getHeaderViewCount());
    }

    public void remove(T item) {
        int index = mData.indexOf(item);
        boolean isRemoved = mData.remove(item);
        if (isRemoved)
            notifyItemRemoved(index + getHeaderViewCount());
    }

    public void remove(int position) {
        if (position < 0 || position >= mData.size()) {
            Log.e(TAG, "remove position = " + position + ", IndexOutOfBounds, please check your code!");
            return;
        }
        mData.remove(position);
        notifyItemRemoved(position + getHeaderViewCount());
    }

    /**
     *
     * @param data additional data
     */
    public void addData(List<T> data) {
        if (data != null) {
            int pos = getItemCount();
            this.mData.addAll(data);
            notifyItemRangeInserted(pos, data.size() - 1);
        }
    }

    public void removeData(List<T> data) {
        if (data != null) {
            this.mData.removeAll(data);
            notifyDataSetChanged();
        }
    }

    public void resetData(List<T> data) {
        mData.removeAll(mData);
        addData(data);
    }


    public List<T> getData() {
        return mData;
    }


    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public T getItem(int position) {
        if (position < 0 || position >= mData.size()) {
            Log.e(TAG, "getItem position = " + position + ", IndexOutOfBounds, please check your code!");
            return null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected boolean isEmpty() {
        return getHeaderViewCount() + getFooterViewCount() + getData().size() == 0;
    }

    @Override
    public int getItemCount() {

        int count;
        if (mLoading) { //if loading ignore footer view
            count = mData.size() + 1 + getHeaderViewCount();
        } else {
            count = mData.size() + getHeaderViewCount() + getFooterViewCount();
        }
        mEmptyEnable = false;
        if (count == 0) {
            mEmptyEnable = true;
            count += getEmptyViewCount();
        }
        return count;
    }


    @Override
    public final int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER_VIEW;
        } else if (mEmptyView != null && getItemCount() == 1 && mEmptyEnable) {
            return TYPE_EMPTY_VIEW;
        } else if (position == mData.size() + getHeaderViewCount()) {
            if (mLoading) {
                return TYPE_LOADING_VIEW;
            } else if (mFooterView != null) {
                return TYPE_FOOTER_VIEW;
            }
        }
        return getDefItemViewType(position);
    }

    @Override
    protected final void bindHolder(final BaseViewHolder holder, final int position) {
        convert((VH) holder, mData.get(holder.getLayoutPosition() - getHeaderViewCount()),position);
        ((VH) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseItem(position,mData.get(holder.getLayoutPosition() - getHeaderViewCount()));
            }
        });
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    abstract protected void convert(VH holder, T item,int position);


    @Override
    public void onLoadingMore() {
        if (!mLoading) {
            mLoading = true;
            notifyItemChanged(getItemCount());
        }
    }

    void loadingMoreComplete(List<T> data) {
        mLoading = false;
        addData(data);
    }

}