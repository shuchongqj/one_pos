package com.gzdb.mall.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.mall.adapter.NavAdapter;
import com.gzdb.mall.adapter.SysProductAdapter;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.mall.bean.Nav;
import com.gzdb.mall.bean.ProductType;
import com.gzdb.mall.bean.SysProduct;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xw.repo.XEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.MAdapterOnClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

public class SysProductActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.et_condition)
    XEditText etCondition;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @Bind(R.id.rv_nav)
    RecyclerView rvNav;

    private SysProductAdapter adapter;
    private List<SysProduct> list = new ArrayList<>();
    private NavAdapter navAdapter;
    private List<Nav> navs = new ArrayList<>();

    private int page = 1;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_product);
        ButterKnife.bind(this);
        textTitle.setText("商品模板");

        adapter = new SysProductAdapter(mContext, list);
        rvList.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvList.setAdapter(adapter);

        getProductType();
    }

    private void getProductType() {
        OkGo.<NydResponse<BaseBean<ProductType>>>get(Contonts.MALL_PRODUCT_TYPE)
                .execute(new JsonCallback<NydResponse<BaseBean<ProductType>>>() {
                    @Override
                    public void onSuccess(Response<NydResponse<BaseBean<ProductType>>> response) {
                        try {
                            List<ProductType> list = response.body().response.getList();
                            for (int i = 0; i < list.size(); i++) {
                                Nav nav = new Nav();
                                nav.setTitle(list.get(i).getTitle());
                                nav.setType(list.get(i).getId() + "");
                                navs.add(nav);
                            }
                            if (navs.size() == 0) {
                                return;
                            }
                            navAdapter = new NavAdapter(mContext, navs, new MAdapterOnClickListener<Nav>() {
                                @Override
                                public void onItemClick(RecyclerView.ViewHolder vh, Nav item, int position) {
                                    try {
                                        page = 1;
                                        id = item.getType();
                                        getProducts("");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            rvNav.setLayoutManager(new LinearLayoutManager(mContext));
                            rvNav.setAdapter(navAdapter);
                            navAdapter.chooseItem(0, navs.get(0));
                            navAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getProducts(String search_key) {
        list.clear();
        OkGo.<NydResponse<BaseBean<SysProduct>>>get(Contonts.MALL_SYS_PRODUCT_LIST)
                .params("passport_id", app.currentUser.getPassportId())
                .params("item_type_id", id)
                .params("search_key", search_key)
                .params("current", page)
                .execute(new DialogCallback<NydResponse<BaseBean<SysProduct>>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<BaseBean<SysProduct>>> response) {
                        try {
                            list.addAll(response.body().response.getList());
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @OnClick({R.id.img_back, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_search:
                getProducts(etCondition.getText().toString());
                break;
        }
    }
}
