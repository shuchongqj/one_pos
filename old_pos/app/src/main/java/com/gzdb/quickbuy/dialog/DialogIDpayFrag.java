package com.gzdb.quickbuy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.core.base.MBaseAdapter;
import com.core.base.MRecyclerView;
import com.google.gson.Gson;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.activity.OrderBuyPayActivity;
import com.gzdb.quickbuy.bean.BalanceResultBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by nongyd on 17/6/21.
 */

public class DialogIDpayFrag extends DialogFragment {

    @Bind(R.id.text_content)
    TextView textContent;
    @Bind(R.id.text_goodPrice)
    TextView textGoodPrice;

    @Bind(R.id.text_totalPrice)
    TextView textTotalPrice;
    @Bind(R.id.text_cancel)
    TextView textCancel;
    @Bind(R.id.text_commit)
    TextView textCommit;
    @Bind(R.id.recyclerView)
    MRecyclerView recyclerView;

    private BalanceResultBean bean;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.4), (int) (dm.heightPixels * 0.7));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quickbuy_idpay_dialog, container, false);
        ButterKnife.bind(this, view);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        return view;
    }

    public void init() {
        bean = new Gson().fromJson(getArguments().getString("bean"), BalanceResultBean.class);
        textContent.setText(bean.getContent());
        textTotalPrice.setText(bean.getPayAmount()+"");
        MAdapter adapter=new MAdapter(getActivity(),bean.getItems());
        textGoodPrice.setText(getArguments().getString("goodPrice"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.text_cancel, R.id.text_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_cancel:
                dismiss();
                break;
            case R.id.text_commit:
                ((OrderBuyPayActivity) getActivity()).creaditLinePay();
                break;
        }
    }

    static class MAdapter extends MBaseAdapter<BalanceResultBean.ItemsBean, MAdapter.ViewHolder> {
        public MAdapter(Context context, List<BalanceResultBean.ItemsBean> data) {
            super(context, data);
        }

        @Override
        protected void convert(final ViewHolder holder, final BalanceResultBean.ItemsBean item) {
            holder.textName.setText(item.getName());
            holder.textGoodPrice.setText(item.getFees()+"");
        }


        @Override
        protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(inflateItemView(R.layout.quickbuy_idpay_dialog_item, parent));
        }


        static class ViewHolder extends BaseViewHolder {
            @Bind(R.id.text_name)
            TextView textName;
            @Bind(R.id.text_goodPrice)
            TextView textGoodPrice;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

    }
}
