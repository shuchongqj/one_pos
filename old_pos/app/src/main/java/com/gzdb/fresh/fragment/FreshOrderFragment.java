package com.gzdb.fresh.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.core.base.BaseRecyclerAdapter;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.fresh.activity.FreshOrderDetailActivity;
import com.gzdb.fresh.adapter.FreshOrderAdapter;
import com.gzdb.fresh.bean.FreshOrder;
import com.gzdb.fresh.bean.FreshOrderDetail;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.mall.event.RefreshOrderEvent;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class FreshOrderFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.et_condition)
    XEditText etCondition;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private int type;
    private int page = 1;
    private String deliver_status = "";//发货状态

    private List<FreshOrder> list = new ArrayList<>();
    private FreshOrderAdapter adapter;

    private String orderType = "";
    private List<String> typeArr = new ArrayList<String>();
    private String  status;//订单状态 0创建订单（未支付） 1已支付 2取消订单 3 已成功退款 4 申请退款不通过  5发起退款

    public FreshOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mall_order, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        type = getArguments().getInt("type");
        Log.e("type=",type+"");
        typeArr.add("全部");
        typeArr.add("自提订单");
        typeArr.add("配送订单");
        ArrayAdapter<String> orderTypaAdapter = new ArrayAdapter<String>(mContext, R.layout.item_select_type, typeArr);
        spinner.setAdapter(orderTypaAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    orderType = "";
                } else {
                    orderType = i - 1 + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        refreshLayout.setDelegate(this);
        refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, false));
        refreshLayout.setIsShowLoadingMoreView(true);

        adapter = new FreshOrderAdapter(mContext, list);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvList.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, FreshOrderDetailActivity.class);
                intent.putExtra("orderid", list.get(position).getId() + "");
                startActivity(intent);
            }
        });

        getData();

        etCondition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sequence_number = etCondition.getText().toString();
                if (sequence_number.length() == 24) {
                    query();
                }
            }
        });
        return view;
    }

    private void getData() {
        if (type == 1) {
            deliver_status = "0";
            status = "1";
        } else if (type == 2) {
            deliver_status = "";
            status = "5";//申请退款中
        }
        String sequence_number = etCondition.getText().toString();
        OkGo.<NydResponse<BaseBean<FreshOrder>>>post(Contonts.FRESH_ORDER_LIST)
                .params("supplier_id", app.currentUser.getPassportId())
                .params("status",status)
                .params("deliver_status", deliver_status)//发货状态  0未发货（待发货） 1已发货
                .params("distribution_type", orderType)
                .params("sequence_number",sequence_number)
                .params("current", page)
                .execute(new JsonCallback<NydResponse<BaseBean<FreshOrder>>>() {
                    @Override
                    public void onSuccess(Response<NydResponse<BaseBean<FreshOrder>>> response) {
                        Log.e("size==",response.body().response.getList().size()+"");
                        List<FreshOrder> lit = new ArrayList<>();
                        for(int i = 0; i < response.body().response.getList().size(); i++){
                            if(response.body().response.getList().get(i).getStatus() != 0){
//                                list.addAll(response.body().response.getList());
                                lit.add(response.body().response.getList().get(i));
                            }
                        }
                        list.addAll(lit);
                        adapter.notifyDataSetChanged();
                        if (refreshLayout != null) {
                            refreshLayout.endLoadingMore();
                            refreshLayout.endRefreshing();
                        }
                    }
                });
    }

    private void query() {
        String sequence_number = etCondition.getText().toString();
        if (sequence_number.equals("")) {
            ToastUtil.showToast(mContext, "请输入正确的订单号");
            return;
        }
        OkGo.<NydResponse<FreshOrderDetail>>post(Contonts.FRESH_ORDER_LIST)
                .params("supplier_id",app.currentUser.getPassportId())
                .params("distribution_type", orderType)//配送方式 0到店自取  1快递配送
                .params("sequence_number", sequence_number)
                .execute(new JsonCallback<NydResponse<FreshOrderDetail>>() {

                    @Override
                    public void onSuccess(Response<NydResponse<FreshOrderDetail>> response) {
                        Intent intent = new Intent(mContext, FreshOrderDetailActivity.class);
                        intent.putExtra("orderid", response.body().response.getId() + "");
                        startActivity(intent);
                        etCondition.setText("");
                    }

                    @Override
                    public void onError(Response<NydResponse<FreshOrderDetail>> response) {
                        super.onError(response);
                        etCondition.setText("");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshOrderEvent(RefreshOrderEvent event) {
        etCondition.setText("");
        page = 1;
        list.clear();
        getData();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page ++;
        list.clear();
        getData();
    }

    //上啦刷新
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page++;
        Log.e("page----",page+"");
        getData();
        return true;
    }

    @OnClick({R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                String sequence_number = etCondition.getText().toString();
                if (sequence_number.length() == 24) {
                    query();
                } else {
                    page = 1;
                    list.clear();
                    getData();
                }
                break;
        }
    }
}
