package com.gzdb.basepos;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/4/22.
 */

public class BaseFragment extends Fragment {

    protected Context mContext;
    protected Fragment instance;
    protected App app;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        mContext=getActivity();
        app= (App) getActivity().getApplication();
    }
}
