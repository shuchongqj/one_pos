package com.gzdb.sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.TabAdapter;
import com.gzdb.sale.fragment.SaleFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaleActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        ButterKnife.bind(this);
        textTitle.setText("折扣优惠");

        tabNames.add("进行中");
        tabNames.add("未开始");
        tabNames.add("已结束");

        for (int i = 1; i <= 3; i++) {
            SaleFragment fragment = new SaleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", i);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), tabNames, fragments);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(tabAdapter);
    }

    @OnClick({R.id.img_back, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_add:
                startActivity(new Intent(mContext, SaleTypeActivity.class));
                break;
        }
    }
}
