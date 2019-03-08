package com.gzdb.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.gzdb.mall.activity.MallOrderDetailActivity;
import com.gzdb.mall.activity.SysProductActivity;
import com.gzdb.mall.adapter.MallOrderAdapter;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.mall.bean.MallOrder;
import com.gzdb.mall.bean.MallOrderDetail;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MallOrderFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {


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
    private String deliver_status = "";

    private List<MallOrder> list = new ArrayList<>();
    private MallOrderAdapter adapter;

    private String orderType = "";
    private List<String> typeArr = new ArrayList<String>();

    public MallOrderFragment() {
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

        typeArr.add("全部");
        typeArr.add("上门自提");
        typeArr.add("快递配送");

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

        adapter = new MallOrderAdapter(mContext, list);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rvList.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(mContext, MallOrderDetailActivity.class);
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
        } else if (type == 2) {
            deliver_status = "1";
        }
        String sequence_number = etCondition.getText().toString();
        OkGo.<NydResponse<BaseBean<MallOrder>>>post(Contonts.MALL_ORDER_LIST)
                .params("passport_id", app.currentUser.getPassportId())
                .params("deliver_status", deliver_status)
                .params("distribution_type", orderType)
                .params("sequence_number", sequence_number)
                .params("current", page)
                .execute(new JsonCallback<NydResponse<BaseBean<MallOrder>>>() {
                    @Override
                    public void onSuccess(Response<NydResponse<BaseBean<MallOrder>>> response) {
                        list.addAll(response.body().response.getList());
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
        OkGo.<NydResponse<MallOrderDetail>>post(Contonts.MALL_ORDER_QUERY)
                .params("passport_id", app.currentUser.getPassportId())
                .params("distribution_type", orderType)
                .params("sequence_number", sequence_number)
                .execute(new JsonCallback<NydResponse<MallOrderDetail>>() {

                    @Override
                    public void onSuccess(Response<NydResponse<MallOrderDetail>> response) {
                        Intent intent = new Intent(mContext, MallOrderDetailActivity.class);
                        intent.putExtra("orderid", response.body().response.getId() + "");
                        startActivity(intent);
                        etCondition.setText("");
                    }

                    @Override
                    public void onError(Response<NydResponse<MallOrderDetail>> response) {
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
        page++;
        list.clear();
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page++;
        getData();
        return true;
    }

    @OnClick({R.id.btn_search, R.id.btn_product})
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
            case R.id.btn_product:
                startActivity(new Intent(mContext, SysProductActivity.class));
                break;
        }
    }
}
