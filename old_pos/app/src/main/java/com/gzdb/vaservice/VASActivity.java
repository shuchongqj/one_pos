package com.gzdb.vaservice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.vaservice.adapter.VasTypeAdapter;
import com.gzdb.vaservice.bean.VasTypeBean;
import com.gzdb.vaservice.event.FinishVasEvent;
import com.gzdb.vaservice.fragment.HfFragment;
import com.gzdb.vaservice.fragment.LlFragment;
import com.gzdb.vaservice.fragment.YctFragment;
import com.gzdb.yct.util.RxCountDown;
import com.gzdb.yct.util.TUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.MAdapterOnClickListener;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * 增值服务主activity
 * Created by nongyd on 2017/4/27.
 */

public class VASActivity extends BaseActivity implements MAdapterOnClickListener<VasTypeBean> {

    @Bind(R.id.content)
    FrameLayout content;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.rv_tag)
    RecyclerView rvTag;

    private List<VasTypeBean> beanList;

    private List<Fragment> fragments = new ArrayList<>();

    private int mIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vas);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        textTitle.setText("增值服务");
//        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.frag_container,new VASFragment());
//        ft.commit();

        beanList = new ArrayList<>();
        VasTypeBean bean1 = new VasTypeBean();
        bean1.setTitle("话费充值");
        bean1.setType("1");
        beanList.add(bean1);
        VasTypeBean bean2 = new VasTypeBean();
        bean2.setTitle("流量充值");
        bean2.setType("2");
        beanList.add(bean2);
        if (SPUtil.getInstance().getInt(mContext, "openYct") == 1) {
            VasTypeBean bean3 = new VasTypeBean();
            bean3.setTitle("羊城通充值");
            bean3.setType("3");
            beanList.add(bean3);
        }

        VasTypeAdapter adapter = new VasTypeAdapter(this, beanList, this);
        adapter.chooseItem(0, bean1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvTag.setLayoutManager(layoutManager);
        rvTag.setAdapter(adapter);

        fragments.add(new HfFragment());
        fragments.add(new LlFragment());
        if (SPUtil.getInstance().getInt(mContext, "openYct") == 1) {
            fragments.add(new YctFragment());
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragments.get(0));
        transaction.commit();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, VasTypeBean item, int position) {
        setIndexSelected(position);
    }

    private void setIndexSelected(int index) {

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
//        ft.commitAllowingStateLoss();
        //再次赋值
        mIndex = index;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finishEvent(FinishVasEvent event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
