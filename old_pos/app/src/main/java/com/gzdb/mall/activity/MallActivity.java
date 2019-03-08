package com.gzdb.mall.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.mall.fragment.OrderManageFragment;
import com.gzdb.mall.fragment.ProductManageFragment;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MallActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.tv_product)
    TextView tvProduct;
    @Bind(R.id.ll_nav)
    LinearLayout llNav;

    private List<Fragment> fragments = new ArrayList<>();
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        ButterKnife.bind(this);

        textTitle.setText("团购商城");

        fragments.add(new OrderManageFragment());
        fragments.add(new ProductManageFragment());

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

        for(int i=0;i<llNav.getChildCount();i++){
            TextView child= (TextView) llNav.getChildAt(i);
            child.setBackgroundColor(getResources().getColor(R.color.grey));
        }

        view.setBackgroundColor(getResources().getColor(R.color.csc_title_bg));
    }

    @OnClick({R.id.img_back, R.id.tv_order, R.id.tv_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_order:
                setIndexSelected(view,0);
                break;
            case R.id.tv_product:
//                ToastUtil.showToast(mContext,"暂未开放");
                setIndexSelected(view,1);
                break;
        }
    }
}
