package com.one.pos.menubar.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.anlib.GFragment;
import com.one.pos.R;
import com.one.pos.menubar.BadgeView;
import com.one.pos.menubar.activity.FreshActivity;
import com.one.pos.menubar.adapter.SimpleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.one.pos.menubar.adapter.SimpleFragmentPagerAdapter.formatBadgeNumber;

/**
 * Author: even
 * Date:   2019/3/7
 * Description:
 */
public class FreshOrderManageFragment extends GFragment {
    private TabLayout tablayout;
    private ViewPager viewpager;
    private SimpleFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();
    private List<Integer> mBadgeCountList = new ArrayList<>();
    private List<BadgeView> mBadgeViews;

    public FreshOrderManageFragment() {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_fresh_manage;
    }

    @Override
    protected void initView(View view) {
        initId(view);
        tabNames.add("全部订单");
        tabNames.add("待发货");
        tabNames.add("退款申请中");
        mBadgeCountList.add(0);
        mBadgeCountList.add(FreshActivity.OverhangNum);
        mBadgeCountList.add(FreshActivity.RefundNum);
        for (int i = 0; i < 3; i++) {
            FreshOrderFragment fragment = new FreshOrderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        mPagerAdapter = new SimpleFragmentPagerAdapter(getFragmentManager(), fragments, tabNames, mBadgeCountList);
        viewpager.setAdapter(mPagerAdapter);
        tablayout.setupWithViewPager(viewpager);
        initBadgeViews();
        setUpTabBadge();
    }

    private void initId(View view) {
        tablayout = view.findViewById(R.id.tablayout);
        viewpager = view.findViewById(R.id.viewpager);
    }

    private void initBadgeViews() {
        if (mBadgeViews == null) {
            mBadgeViews = new ArrayList<BadgeView>();
            for (int i = 0; i < fragments.size(); i++) {
                BadgeView tmp = new BadgeView(getContext());
                //setBadgePosition
                tmp.setBadgeGravity(Gravity.RIGHT);
                tmp.setBadgeMargin(0, 0, 30, 0);
                tmp.setTextSize(10);
                mBadgeViews.add(tmp);
            }
        }
    }

    private void setUpTabBadge() {
        // 1. 最简单
        for (int i = 0; i < fragments.size(); i++) {
            mBadgeViews.get(i).setTargetView(((ViewGroup) tablayout.getChildAt(0)).getChildAt(i));
            mBadgeViews.get(i).setText(formatBadgeNumber(mBadgeCountList.get(i)));
        }
    }


}
