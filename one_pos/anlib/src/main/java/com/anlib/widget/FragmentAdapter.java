package com.anlib.widget;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhumg on 2017/3/23 0023.
 */
public class FragmentAdapter<T, F extends Fragment> extends FragmentPagerAdapter {

    private List<F> fragments;

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).toString();
    }

    public FragmentAdapter(FragmentManager fm) {
        this(fm, new ArrayList<F>());
    }

    public FragmentAdapter(FragmentManager fm, List<F> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public List<F> getFragments() {
        return fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}