package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.base.MRecyclerView;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.NoticeAdapter;
import com.gzdb.supermarket.been.NoticeResulBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.OnItemClickListener;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by nongyd on 17/6/10.
 * 活动通告
 */


public class ActivityDialogo extends DialogFragment {


    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.img_close)
    ImageView imgClose;
    @Bind(R.id.recyclerView)
    MRecyclerView recyclerView;


    private NoticeAdapter adapter;
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.6), (int) (dm.heightPixels * 0.8));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_activity, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }
    public void init(){
        adapter=new NoticeAdapter(getActivity(), new ArrayList<NoticeResulBean.DatasBean>());
        adapter.addOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                DialogUtil.OkAndCancle(getActivity(),adapter.getItem(position).getTitle(),adapter.getItem(position).getContent()).show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        getNotice();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        dismiss();
    }

    public void getNotice(){
        OkGo.<NydResponse<NoticeResulBean>>post(Contonts.URL_NOTICE_LIST)
                .execute(new JsonCallback<NydResponse<NoticeResulBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<NoticeResulBean>> response) {
                        adapter.removeData(adapter.getData());
                        adapter.addData(response.body().response.getDatas());
                    }
                });
    }
}