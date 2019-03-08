package com.core.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    View mView ;
    public BaseViewHolder(View itemView) {
        super(itemView);
        this.mView=itemView;
    }

    public View getView(){
        return mView;
    }
}
