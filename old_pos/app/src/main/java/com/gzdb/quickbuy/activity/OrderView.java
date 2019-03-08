package com.gzdb.quickbuy.activity;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.QuickBuyOrderAdapter;
import com.gzdb.quickbuy.bean.OrderDetailBean;
import com.gzdb.quickbuy.bean.OrderListResultBean;
import com.gzdb.quickbuy.util.OneBuyRightViewLogic;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.ViewPagerAdapter;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Zxy on 2017/1/6.
 * 采购订单
 */

public class OrderView implements OneBuyRightViewLogic, View.OnClickListener {

    View rootOrderView;
    ImageView close_btnO;
    ViewPager vp_content;
    List<View> mList = new ArrayList<View>();
    TextView not_order, done_order, dispatching_order, accomplish_order, tv_order_att;
    View l_1, l_2, l_3, l_4;
    ListView lv_notaccomplish, lv_accomplish, lv_dispatching, lv_done;

    View dispatchingView, accomplishView, notAccomplishView, doneView;//未完成订单和已完成订单

    List<OrderDetailBean> notAccomplishBeanList;
    List<OrderDetailBean> accomplishBeanList;
    List<OrderDetailBean> dispatchingBeenList;
    List<OrderDetailBean> doneBeanList;

    QuickBuyOrderAdapter notAccomplishAdapter;
    QuickBuyOrderAdapter dispatchingAdapter;
    QuickBuyOrderAdapter accomplishAdapter;
    QuickBuyOrderAdapter doneAdapter;

    Dialog qvDialog;
    QuickBuyActivity quickBuyActivity;
    LayoutInflater inflater;


    public OrderView(QuickBuyActivity quickBuyActivity) {
        rootOrderView = View.inflate(quickBuyActivity, R.layout.quickbuy_order, null);
        this.quickBuyActivity = quickBuyActivity;
        inflater = LayoutInflater.from(quickBuyActivity);
        qvDialog = DialogUtil.loadingDialog(quickBuyActivity, "加载中...");
        notAccomplishBeanList = new ArrayList<>();
        accomplishBeanList = new ArrayList<>();
        dispatchingBeenList = new ArrayList<>();
        doneBeanList = new ArrayList<>();
        notAccomplishAdapter = new QuickBuyOrderAdapter(quickBuyActivity, this, notAccomplishBeanList);
        doneAdapter = new QuickBuyOrderAdapter(quickBuyActivity, this, doneBeanList);
        dispatchingAdapter = new QuickBuyOrderAdapter(quickBuyActivity, this, dispatchingBeenList);
        accomplishAdapter = new QuickBuyOrderAdapter(quickBuyActivity, this, accomplishBeanList);

        //初始化
        initOrderID();
        onClickOrder();
    }

    @Override
    public View getView() {
        return rootOrderView;
    }

    @Override
    public void onVisible() {
    }

    @Override
    public void unVisible() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.not_order:
                orderAtt(false);
                getPager(0);
                break;
            case R.id.done_order:
                orderAtt(false);
                getPager(1);
                break;
            case R.id.dispatching_order:
                orderAtt(false);
                getPager(2);
                break;
            case R.id.accomplish_order:
                orderAtt(false);
                getPager(3);
                break;
        }
    }

    void onClickOrder() {
        not_order.setOnClickListener(this);
        done_order.setOnClickListener(this);
        dispatching_order.setOnClickListener(this);
        accomplish_order.setOnClickListener(this);
    }

    //采购订单
    public void initOrderID() {
        close_btnO = (ImageView) rootOrderView.findViewById(R.id.close_btno);
        vp_content = (ViewPager) rootOrderView.findViewById(R.id.vp_content);

        not_order = (TextView) rootOrderView.findViewById(R.id.not_order);
        done_order = (TextView) rootOrderView.findViewById(R.id.done_order);
        dispatching_order = (TextView) rootOrderView.findViewById(R.id.dispatching_order);
        accomplish_order = (TextView) rootOrderView.findViewById(R.id.accomplish_order);
        tv_order_att = (TextView) rootOrderView.findViewById(R.id.tv_order_att);
        l_1 = rootOrderView.findViewById(R.id.l_1);
        l_2 = rootOrderView.findViewById(R.id.l_2);
        l_3 = rootOrderView.findViewById(R.id.l_3);
        l_4 = rootOrderView.findViewById(R.id.l_4);

        accomplishView = inflater.inflate(R.layout.qu_notaccomplish_view, null);//完成
        notAccomplishView = inflater.inflate(R.layout.qu_notaccomplish_view, null);//未完成
        dispatchingView = inflater.inflate(R.layout.qu_notaccomplish_view, null);//未完成
        doneView = inflater.inflate(R.layout.qu_notaccomplish_view, null);//未完成

        lv_notaccomplish = (ListView) notAccomplishView.findViewById(R.id.lv_notaccomplish); //未完成订单
        lv_accomplish = (ListView) accomplishView.findViewById(R.id.lv_notaccomplish);        //已完成订单
        lv_done = (ListView) doneView.findViewById(R.id.lv_notaccomplish);
        lv_dispatching = (ListView) dispatchingView.findViewById(R.id.lv_notaccomplish);

        lv_notaccomplish.setAdapter(notAccomplishAdapter);
        lv_accomplish.setAdapter(accomplishAdapter);
        lv_dispatching.setAdapter(dispatchingAdapter);
        lv_done.setAdapter(doneAdapter);

        mList.add(notAccomplishView);//未完成订单
        mList.add(doneView); // 拣货中
        mList.add(dispatchingView); // 配送中
        mList.add(accomplishView);//已完成订单
        ViewPagerAdapter apAdap = new ViewPagerAdapter(mList);
        vp_content.setAdapter(apAdap);

        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                getPager(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void getPager(int Num) {
        switch (Num) {
            case 0:
                initPaterColor();
                not_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                l_1.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                vp_content.setCurrentItem(Num);
                getOrders(1);
                break;
            case 1:
                initPaterColor();
                done_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                l_2.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                vp_content.setCurrentItem(Num);
                getOrders(2);
                break;
            case 2:
                initPaterColor();
                dispatching_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                l_3.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                vp_content.setCurrentItem(Num);
                getOrders(3);
                break;

            case 3:
                initPaterColor();
                accomplish_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                l_4.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.blue));
                vp_content.setCurrentItem(Num);
                getOrders(4);
                break;

        }
    }

    public void initPaterColor() {
        not_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.black));
        done_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.black));
        dispatching_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.black));
        accomplish_order.setTextColor(ContextCompat.getColor(quickBuyActivity, R.color.black));

        l_1.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.white));
        l_2.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.white));
        l_3.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.white));
        l_4.setBackgroundColor(ContextCompat.getColor(quickBuyActivity, R.color.white));
    }

    //得到订单列表
    public void getOrders(final int type) {
        OkGo.<NydResponse<OrderListResultBean>>post(Contonts.URL_SUPPLY_ORDER_STATUS_LIST)
                .params("orderType", 1)
                .params("pageNum", 1)
                .params("pageSize", 1000)
                .params("statusEnter", type)
                .execute(new DialogCallback<NydResponse<OrderListResultBean>>(quickBuyActivity) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<OrderListResultBean>> response) {
                        if (response.body().response.getDatas().size() > 0 ) {//显示是否为空的数据
                            tv_order_att.setVisibility(View.GONE);
                        } else {
                            tv_order_att.setVisibility(View.VISIBLE);
                        }
                        if (type == 1) {
                            notAccomplishBeanList.clear();
                            notAccomplishBeanList.addAll(response.body().response.getDatas());
                            notAccomplishAdapter.notifyDataSetChanged();
                        } else if (type == 2) {
                            doneBeanList.clear();
                            doneBeanList.addAll(response.body().response.getDatas());
                            doneAdapter.notifyDataSetChanged();
                        } else if (type == 3) {
                            dispatchingBeenList.clear();
                            dispatchingBeenList.addAll(response.body().response.getDatas());
                            dispatchingAdapter.notifyDataSetChanged();
                        } else if (type == 4) {
                            accomplishBeanList.clear();
                            accomplishBeanList.addAll(response.body().response.getDatas());
                            accomplishAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    //得到订单列表
    public void getNotAccomplishOrder(final int status) {
        OkGo.<NydResponse<OrderListResultBean>>post(Contonts.URL_SUPPLY_ORDER_LIST)
                .params("orderType", 1)
                .params("pageNum", 1)
                .params("pageSize", 1000)
                .params("orderStatus", status)
                .execute(new DialogCallback<NydResponse<OrderListResultBean>>(quickBuyActivity) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<OrderListResultBean>> response) {
                        if (response.body().response.getDatas().size() > 0) {//显示是否为空的数据
                            tv_order_att.setVisibility(View.GONE);
                        } else {
                            tv_order_att.setVisibility(View.VISIBLE);
                        }
                        if (status == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {
                            notAccomplishBeanList.clear();
                            notAccomplishBeanList.addAll(response.body().response.getDatas());
                            notAccomplishAdapter.notifyDataSetChanged();
                        } else if (status == OrderStatusEnum.ORDER_STATUS_DELIVER.getType()) {
                            doneBeanList.clear();
                            doneBeanList.addAll(response.body().response.getDatas());
                            doneAdapter.notifyDataSetChanged();
                        } else if (status == OrderStatusEnum.ORDER_STATUS_DISTRIBUTION.getType()) {
                            dispatchingBeenList.clear();
                            dispatchingBeenList.addAll(response.body().response.getDatas());
                            dispatchingAdapter.notifyDataSetChanged();
                        } else if (status == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()) {
                            accomplishBeanList.clear();
                            accomplishBeanList.addAll(response.body().response.getDatas());
                            accomplishAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void orderAtt(boolean mode) {
        if (mode) {
            tv_order_att.setVisibility(View.VISIBLE);
        } else {
            tv_order_att.setVisibility(View.GONE);
        }
    }

}

