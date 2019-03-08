package com.gzdb.basepos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.gzdb.supermarket.activity.ActivityManager;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by nongyd on 2017/4/22.
 */

public class BaseActivity extends FragmentActivity {

    protected Activity mContext;
    protected App app;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        mContext = this;
        app = (App) getApplication();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.removeActivity(this);

    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }
}
