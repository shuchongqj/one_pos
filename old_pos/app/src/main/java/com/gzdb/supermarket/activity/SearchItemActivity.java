package com.gzdb.supermarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.core.base.BaseRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.SearchItemAdapter;
import com.gzdb.supermarket.been.GoodBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhumg on 8/17.
 */
public class SearchItemActivity extends Activity {

    @Bind(R.id.rv_list)
    RecyclerView rvList;
    private List<GoodBean> goodBeanList;

    private SearchItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        ButterKnife.bind(this);
        goodBeanList = new Gson().fromJson(getIntent().getStringExtra("beanList"), new TypeToken<List<GoodBean>>() {
        }.getType());

        adapter=new SearchItemAdapter(this,goodBeanList);
        rvList.setLayoutManager(new GridLayoutManager(this,3));
        rvList.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                GoodBean bean = goodBeanList.get(position);
                Intent intent = new Intent();
                intent.putExtra("bean", new Gson().toJson(bean));
                setResult(101, intent);
                finish();
            }
        });
    }

    @OnClick(R.id.ll_add)
    public void onViewClicked() {
        setResult(100);
        finish();
    }
}
