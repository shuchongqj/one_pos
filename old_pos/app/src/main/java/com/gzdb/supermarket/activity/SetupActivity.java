package com.gzdb.supermarket.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.supermarket.fragment.BannerFragment;
import com.gzdb.supermarket.fragment.ScaleFragment;
import com.gzdb.supermarket.fragment.ScreenFragment;
import com.gzdb.supermarket.fragment.SysSetupFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetupActivity extends BaseActivity {

    @Bind(R.id.tv_system_setting)
    TextView tvSystemSetting;
    @Bind(R.id.tv_mainimg_setting)
    TextView tvMainimgSetting;
    @Bind(R.id.tv_banner_setting)
    TextView tvBannerSetting;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_scale_setting)
    TextView tvScaleSetting;

    private Fragment[] mFragments;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        ButterKnife.bind(this);
        textTitle.setText("系统设置");

        if(Build.MODEL.equals("S2")){}

        initFragment();
    }

    @OnClick({R.id.tv_system_setting, R.id.tv_mainimg_setting, R.id.tv_banner_setting, R.id.tv_scale_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_system_setting:
                setIndexSelected(0);
                break;
            case R.id.tv_mainimg_setting:
                setIndexSelected(1);
                break;
            case R.id.tv_banner_setting:
                setIndexSelected(2);
                break;
            case R.id.tv_scale_setting:
                setIndexSelected(3);
                break;
        }
    }

    private void tagBg(int index) {
        switch (index) {
            case 0:
                tvSystemSetting.setBackgroundColor(getResources().getColor(R.color.white));
                tvMainimgSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvBannerSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvScaleSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                break;
            case 1:
                tvSystemSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvMainimgSetting.setBackgroundColor(getResources().getColor(R.color.white));
                tvBannerSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvScaleSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                break;
            case 2:
                tvSystemSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvMainimgSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvBannerSetting.setBackgroundColor(getResources().getColor(R.color.white));
                tvScaleSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                break;
            case 3:
                tvSystemSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvMainimgSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvBannerSetting.setBackgroundColor(getResources().getColor(R.color.ly_background));
                tvScaleSetting.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void initFragment() {

        SysSetupFragment sysSetupFragment = new SysSetupFragment();
        ScreenFragment screenFragment = new ScreenFragment();
        BannerFragment bannerFragment = new BannerFragment();
        ScaleFragment scaleFragment = new ScaleFragment();

        //添加到数组
        mFragments = new Fragment[]{sysSetupFragment, screenFragment, bannerFragment,scaleFragment};
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.content, sysSetupFragment).commit();
        //默认设置为第0个
        setIndexSelected(0);

    }

    private void setIndexSelected(int index) {
//        if (index != 0 && !Sunmi.viceScreenMode) {
//            ToastUtil.showToast(this, "仅支持商米分屏Pos~");
//            return;
//        }
        tagBg(index);
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.content, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }

//        ft.commit();
        ft.commitAllowingStateLoss();
        //再次赋值
        mIndex = index;

    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
