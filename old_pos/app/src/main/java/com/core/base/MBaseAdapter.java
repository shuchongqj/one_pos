package com.core.base;

import android.content.Context;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by nongyd on 2017/4/27.
 */

public abstract class MBaseAdapter<T,VH extends BaseViewHolder> extends BaseTurboAdapter<T,VH> {


    public MBaseAdapter(Context context) {
        super(context);
    }

    public MBaseAdapter(Context context, List<T> data) {
        super(context, data);
    }
}
