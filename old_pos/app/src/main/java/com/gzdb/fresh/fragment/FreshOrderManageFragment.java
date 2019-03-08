package com.gzdb.fresh.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.fresh.BadgeView;
import com.gzdb.fresh.activity.FreshActivity;
import com.gzdb.fresh.adapter.SimpleFragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import static com.gzdb.fresh.adapter.SimpleFragmentPagerAdapter.formatBadgeNumber;

public class FreshOrderManageFragment extends BaseFragment {
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private SimpleFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();
    private List<Integer> mBadgeCountList = new ArrayList<>();
    private List<BadgeView> mBadgeViews;


    public FreshOrderManageFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fresh_manage, container, false);
        ButterKnife.bind(this, view);
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
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
