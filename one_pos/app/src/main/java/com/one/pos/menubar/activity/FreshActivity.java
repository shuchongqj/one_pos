package com.one.pos.menubar.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anlib.GActivity;
import com.one.pos.R;
import com.one.pos.menubar.fragment.FreshOrderManageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: even
 * Date:   2019/3/6
 * Description:
 */
public class FreshActivity extends GActivity implements View.OnClickListener {
    private FrameLayout content;
    private LinearLayout comebace_layout;
    private TextView common_title;
    private TextView flash_order;


    public static int OverhangNum;
    public static  int RefundNum;
    private List<Fragment> fragments = new ArrayList<>();




    @Override
    protected int getContentViewId() {
        return R.layout.activity_fresh;
    }

    @Override
    protected void initView(View view) {
        initId(view);
        OverhangNum = getIntent().getIntExtra("OverhangNum", 0);
        RefundNum = getIntent().getIntExtra("RefundNum", 0);
        common_title.setText("生鲜系统");

        fragments.add(new FreshOrderManageFragment());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragments.get(0));
        transaction.commit();
    }

    private void initId(View view) {
        comebace_layout = view.findViewById(R.id.comebace_layout);
        content = view.findViewById(R.id.content);
        common_title = view.findViewById(R.id.common_title);
        flash_order = view.findViewById(R.id.flash_order);
        comebace_layout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.comebace_layout:
                finish();
                break;
        }

    }
}
