package com.gzdb.fresh.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.fresh.fragment.FreshOrderManageFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FreshActivity extends BaseActivity {
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.flash_order)
    TextView flashOrder;
    @Bind(R.id.flash_nav)
    LinearLayout flashNav;

    private int mIndex = 0;
    private List<Fragment> fragments = new ArrayList<>();
    public static int OverhangNum;
    public static  int RefundNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh);
        ButterKnife.bind(this);
        OverhangNum = getIntent().getIntExtra("OverhangNum", 0);
        RefundNum = getIntent().getIntExtra("RefundNum", 0);



        Log.e("OverhangNum----", FreshActivity.OverhangNum + "");
        Log.e("RefundNum----", FreshActivity.RefundNum + "");


        textTitle.setText("生鲜系统");

        fragments.add(new FreshOrderManageFragment());


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragments.get(0));
        transaction.commit();

    }

    private void setIndexSelected(View view, int index) {

        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(fragments.get(mIndex));
        //判断是否添加
        if (!fragments.get(index).isAdded()) {
            ft.add(R.id.content, fragments.get(index)).show(fragments.get(index));
        } else {
            ft.show(fragments.get(index));
        }

        ft.commit();
        //再次赋值
        mIndex = index;

        for(int i=0;i<flashNav.getChildCount();i++){
            TextView child= (TextView) flashNav.getChildAt(i);
            child.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        view.setBackgroundColor(getResources().getColor(R.color.csc_title_bg));
    }

    @OnClick({R.id.img_back, R.id.flash_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.flash_order:
                setIndexSelected(view,0);
                break;

        }
    }



}
