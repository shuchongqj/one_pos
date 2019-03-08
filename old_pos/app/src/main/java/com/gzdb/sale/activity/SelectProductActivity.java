package com.gzdb.sale.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.SelectProductAdapter;
import com.gzdb.sale.bean.SelectProduct;
import com.gzdb.sale.event.SelectProductEvent;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

public class SelectProductActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate ,ScanGunKeyEventHelper.OnScanSuccessListener {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.et_condition)
    EditText etCondition;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;

    private int page = 1;
    private int size = 20;

    private SelectProductAdapter adapter;
    private List<SelectProduct> products = new ArrayList<>();
    private List<SelectProduct> selectProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product);
        ButterKnife.bind(this);
        textTitle.setText("选择商品");

//        adapter = new SelectProductAdapter(this, products, selectProducts);
//        rvList.setLayoutManager(new LinearLayoutManager(this));
//        rvList.setAdapter(adapter);

        LimitsEditEnter(etCondition);

        refreshLayout.setDelegate(this);
        refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        refreshLayout.setIsShowLoadingMoreView(true);

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
                page = 1;
                getData();
            }
        });
    }

    @OnClick({R.id.img_back, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                back();
                break;
            case R.id.btn_search:
                page = 1;
                getData();
                break;
        }
    }

    private void getData() {
        String condition = etCondition.getText().toString().trim();
        OkGo.<NydResponse<List<SelectProduct>>>post(Contonts.SEARCH_PRODUCT_LIST)
                .tag(getClass().getSimpleName())
                .params("condition", condition)
                .params("page", page)
                .params("rows", size)
                .execute(new JsonCallback<NydResponse<List<SelectProduct>>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<List<SelectProduct>>> response) {
                        if (page == 1) {
                            products.clear();
                            adapter = new SelectProductAdapter(mContext, products, selectProducts);
                            rvList.setLayoutManager(new LinearLayoutManager(mContext));
                            rvList.setAdapter(adapter);
                        }
                        for (int i = 0; i < response.body().response.size(); i++) {
                            if (response.body().response.get(i).getChecked() == 1) {
                                selectProducts.add(response.body().response.get(i));
                            }
                        }
                        products.addAll(response.body().response);
                        adapter.notifyDataSetChanged();
                        if (refreshLayout != null) {
                            refreshLayout.endLoadingMore();
                            refreshLayout.endRefreshing();
                        }
                    }
                });
    }

    private void back() {
        if (adapter == null) {
            finish();
            return;
        }
        List<SelectProduct> productList = adapter.getmProducts();
        List<SelectProduct> selectProducts2 = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getChecked() == 1) {
                boolean isSelect = false;
                for (int j = 0; j < selectProducts.size(); j++) {
                    if (productList.get(i).getItemId() == selectProducts.get(j).getItemId()) {
                        isSelect = true;
                        break;
                    }
                }
                if (!isSelect) {
                    selectProducts2.add(productList.get(i));
                }
            }
        }
        EventBus.getDefault().post(new SelectProductEvent(selectProducts2));
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            back();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page++;
        getData();
        return true;
    }

    /**
     * 限制回车换行
     *
     * @param et
     */
    public static void LimitsEditEnter(EditText et) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

    @Override
    public void onScanSuccess(String barcode) {
        String code = barcode.replace(" ", "");
        etCondition.setText(code);
    }
}
