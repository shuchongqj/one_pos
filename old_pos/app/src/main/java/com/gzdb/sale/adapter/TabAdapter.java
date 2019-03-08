package com.gzdb.sale.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mayn on 2018/3/28.
 */

public class TabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> tabNames;
    private FragmentManager fragmentManager;

    public TabAdapter(FragmentManager fragmentManager, List<String> tabNames, List<Fragment> fragments){
        super(fragmentManager);
        this.fragmentManager=fragmentManager;
        this.fragments=fragments;
        this.tabNames=tabNames;
    }
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container,position);
        fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragments.get(position);
        fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }
}
