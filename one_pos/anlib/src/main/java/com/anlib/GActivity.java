package com.anlib;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.anlib.http.HttpLife;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhumg
 */
public abstract class GActivity extends FragmentActivity implements HttpLife {

    //静态ID创建
    private static int ACTIVITY_ID = 0;

    //activityId
    private int activity_id;

    protected View rootView;

    boolean active = true;

    protected abstract int getContentViewId();

    protected abstract void initView(View view);

    protected List<GActivityOnTouchListener> onTouchListeners = new ArrayList<>();

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity_id = (ACTIVITY_ID++);
        GActivityManager.addActivity(this);
        int viewid = getContentViewId();
        if (viewid != 0) {
            rootView = View.inflate(this, viewid, null);
            setContentView(rootView);
            initView(rootView);
        }
    }

    public int getActivityId() {
        return this.activity_id;
    }

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        super.onDestroy();
    }

    @Override
    public void finish() {
        active = false;
        GActivityManager.removeActivity(this);
        super.finish();
    }

    //activity触屏事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (GActivityOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(GActivityOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(GActivityOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }
    //Activity.onCreate->Activity.onResume->View.onMeasure->View.onLayout->onGlobalLayoutListener->Activity.onWidnowFocusChanged
}
