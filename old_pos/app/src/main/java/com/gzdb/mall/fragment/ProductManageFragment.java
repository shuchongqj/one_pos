package com.gzdb.mall.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.mall.activity.SysProductActivity;
import com.gzdb.mall.adapter.MallProductAdapter;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.mall.bean.MallProduct;
import com.gzdb.supermarket.event.RefreshProductEvent;
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
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductManageFragment extends BaseFragment {


    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.et_condition)
    XEditText etCondition;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private MallProductAdapter adapter;
    private List<MallProduct> list = new ArrayList<>();
    private int page = 1;
    private String status = "";
    private List<String> typeArr = new ArrayList<String>();

    public ProductManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_manage, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        adapter = new MallProductAdapter(mContext, list);
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        rvList.setAdapter(adapter);

        typeArr.add("全部");
        typeArr.add("上架");
        typeArr.add("下架");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.item_select_type, typeArr);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    status = "";
                } else {
                    status = i + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getProduct("");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_search, R.id.btn_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                getProduct(etCondition.getText().toString());
                break;
            case R.id.btn_product:
                startActivity(new Intent(mContext, SysProductActivity.class));
                break;
        }
    }

    private void getProduct(String search_key) {
        OkGo.<NydResponse<BaseBean<MallProduct>>>post(Contonts.MALL_PRODUCT_LIST)
                .params("passport_id", app.currentUser.getPassportId())
                .params("current", page)
                .params("item_type_id", "")
                .params("status", status)
                .params("search_key", search_key)
                .execute(new DialogCallback<NydResponse<BaseBean<MallProduct>>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<BaseBean<MallProduct>>> response) {
                        list.clear();
                        list.addAll(response.body().response.getList());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProductEvent(RefreshProductEvent event){
        etCondition.setText("");
        getProduct("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
