package com.anlib;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.one.anlib.R;


/**
 * @author zhumg
 */
public abstract class GFragment extends Fragment {

    protected View rootView;

    protected abstract int getContentViewId();

    protected abstract void initView(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            int viewId = getContentViewId();
            if (viewId == 0) {
                viewId = R.layout.widget_null_fragment;
            }
            System.err.println("---> GFragment createView "+getClass().getName());
            rootView = inflater.inflate(viewId, null);
            initView(rootView);

        }
        return rootView;
    }
}
