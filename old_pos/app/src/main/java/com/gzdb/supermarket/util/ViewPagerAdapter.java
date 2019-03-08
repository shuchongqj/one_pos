package com.gzdb.supermarket.util;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Zxy on 2016/12/5.
 */

public class ViewPagerAdapter extends PagerAdapter {

    public List<View> mView;

    public ViewPagerAdapter(List<View> lists){
        this.mView = lists;
    }

    @Override
    public int getCount() {
        return mView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mView.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mView.get(position));
        return mView.get(position);
    }
}
