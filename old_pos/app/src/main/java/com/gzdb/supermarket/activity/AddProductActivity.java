package com.gzdb.supermarket.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.TabAdapter;
import com.gzdb.sale.fragment.SaleFragment;
import com.gzdb.supermarket.event.RefreshProductEvent;
import com.gzdb.supermarket.fragment.AddFreshFragment;
import com.gzdb.supermarket.fragment.AddProductFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddProductActivity extends BaseActivity {

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        tabNames.add("按件商品");
        tabNames.add("称重商品");

        fragments.add( new AddProductFragment());
        if(Build.MODEL.equals("S2")){
            fragments.add( new AddFreshFragment());
        }else{
            tablayout.setVisibility(View.INVISIBLE);
        }

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), tabNames, fragments);
        tablayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(tabAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProductEvent(RefreshProductEvent event){
        finish();
    }
}
