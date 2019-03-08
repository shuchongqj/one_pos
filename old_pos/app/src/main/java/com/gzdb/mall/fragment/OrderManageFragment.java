package com.gzdb.mall.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.TabAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderManageFragment extends BaseFragment {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    public OrderManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_manage, container, false);
        ButterKnife.bind(this, view);

        tabNames.add("全部订单");
        tabNames.add("待发货");
        tabNames.add("已完成");

        for (int i = 0; i < 3; i++) {
            MallOrderFragment fragment = new MallOrderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabNames, fragments);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(tabAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
