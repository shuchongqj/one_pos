package com.one.pos.menubar.activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.anlib.GActivity;
import com.anlib.util.SPUtil;
import com.one.pos.R;
import com.one.pos.menubar.adapter.VasTypeAdapter;
import com.one.pos.menubar.bean.VasTypeBean;
import com.one.pos.menubar.fragment.FlowFragment;
import com.one.pos.menubar.fragment.PhoneFragment;
import com.one.pos.menubar.fragment.YctFragment;

import java.util.ArrayList;
import java.util.List;

import cc.solart.turbo.MAdapterOnClickListener;


/**
 * Author: even
 * Date:   2019/3/4
 * Description:增值服务
 */
public class VASActivity extends GActivity  implements MAdapterOnClickListener<VasTypeBean>{
    private ImageView img_back;
    private TextView common_title;
    private RecyclerView rv_tag;
    private FrameLayout content;
    private List<VasTypeBean> beanList;
    private int mIndex = 0;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected int getContentViewId() {
        return R.layout.activity_vas;
    }

    @Override
    protected void initView(View view) {
        initId(view);
        common_title.setText("增值服务");
        beanList = new ArrayList<>();
        VasTypeBean bean1 = new VasTypeBean();
        bean1.setTitle("话费充值");
        bean1.setType("1");
        bean1.setSelected(false);
        beanList.add(bean1);
        VasTypeBean bean2 = new VasTypeBean();
        bean2.setTitle("流量充值");
        bean2.setType("2");
        bean2.setSelected(false);
        beanList.add(bean2);

//        if (SPUtil.getInstance().getInt(this, "openYct") == 1) {
            VasTypeBean bean3 = new VasTypeBean();
            bean3.setTitle("羊城通充值");
            bean3.setType("3");
            bean3.setSelected(false);
            beanList.add(bean3);
//        }
        VasTypeAdapter  adapter = new VasTypeAdapter(this, beanList,  this);
        adapter.chooseItem(0, bean1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_tag.setLayoutManager(layoutManager);
        rv_tag.setAdapter(adapter);
//
        fragments.add(new PhoneFragment());//话费充值
        fragments.add(new FlowFragment());//流量充值
//        if (SPUtil.getInstance().getInt(this, "openYct") == 1) {
            fragments.add(new YctFragment());//羊城通充值
//        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, fragments.get(0));
        transaction.commit();

    }

    private void initId(View view) {
        img_back = view.findViewById(R.id.img_back);
        common_title = view.findViewById(R.id.common_title);
        rv_tag = view.findViewById(R.id.rv_tag);
        content = view.findViewById(R.id.content);
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
}
