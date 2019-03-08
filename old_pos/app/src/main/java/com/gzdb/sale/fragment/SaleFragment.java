package com.gzdb.sale.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.SaleAdapter;
import com.gzdb.sale.bean.Sale;
import com.gzdb.sale.event.RefreshSaleEvent;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaleFragment extends BaseFragment {

    @Bind(R.id.rv_list)
    RecyclerView rvList;

    private List<Sale> sales = new ArrayList<>();
    private SaleAdapter adapter;

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    Timer timer = new Timer();//实例化Timer类

    private int type;

    public SaleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        type = getArguments().getInt("type");

        adapter = new SaleAdapter(getActivity(), sales, type);
        rvList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvList.setAdapter(adapter);

        getData();
        return view;
    }

    private void getData() {
        OkGo.<NydResponse<List<Sale>>>post(Contonts.SALE_LIST)
                .tag(type)
                .params("type", type)
                .execute(new DialogCallback<NydResponse<List<Sale>>>(mContext) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<List<Sale>>> response) {
                        sales.clear();
                        sales.addAll(response.body().response);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshSaleEvent(RefreshSaleEvent event) {
        timer.schedule(new TimerTask() {
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                });
                this.cancel();
            }
        }, 1000);//五百毫秒

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        timer.cancel();
    }
}
