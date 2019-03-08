package com.core.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import cc.solart.turbo.TurboRecyclerView;

/**
 * Created by nongyd on 2017/4/27.
 */

public class MRecyclerView extends TurboRecyclerView {
    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
