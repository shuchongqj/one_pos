package com.core.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author zhumg
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    Context mContext;
    List<T> mList;
    private OnItemClickListener mOnItemClickListener;

    public BaseRecyclerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(getContentView(viewType), parent,false);

        return new BaseViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个地方一定要判断 不然你没有注册点击事件的时候，点击Item的时候会报错
                if(mOnItemClickListener==null){
                    return;
                }
                mOnItemClickListener.onItemClick(position);
            }
        });
        covert(holder, mList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    protected abstract int getContentView(int viewType);

    protected abstract void covert(BaseViewHolder holder, T data, int position);


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


}