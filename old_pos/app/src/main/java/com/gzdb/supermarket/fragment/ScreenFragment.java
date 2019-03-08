package com.gzdb.supermarket.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.util.SharedPreferencesSunmiUtil;
import com.gzdb.supermarket.adapter.SettingMainImgAdapter;
import com.gzdb.supermarket.been.ScreenResultBean;
import com.gzdb.supermarket.been.SettingMainImgBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.MAdapterOnClickListener;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Response;
import sunmi.ds.DSKernel;
import sunmi.ds.callback.ISendCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScreenFragment extends Fragment implements MAdapterOnClickListener<SettingMainImgBean> {


    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.btn_setup)
    Button btnSetup;

    private SettingMainImgAdapter mainImgAdapter;
    private String chooseImgUrl;
    private Dialog dialog;

    DSKernel mDSKernel;

    public ScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_screen, container, false);
        ButterKnife.bind(this, view);
        mDSKernel = Sunmi.getDSKernel();
        dialog = DialogUtil.loadingDialog(getContext(), "正在处理，请稍候...");
        getData();
        return view;
    }

    private void getData() {
        OkGo.<NydResponse<ScreenResultBean>>post(Contonts.URL_SCREEN)
                .execute(new DialogCallback<NydResponse<ScreenResultBean>>(getActivity()) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<ScreenResultBean>> response) {
                        mainImgAdapter = new SettingMainImgAdapter(getContext(), response.body().response.getDatas(), ScreenFragment.this);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        recyclerView.setAdapter(mainImgAdapter);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_setup)
    public void onViewClicked() {
        if (BaseUtils.isEmpty(chooseImgUrl)) {
            ToastUtil.showEorr(getContext(), "请先选择图片！");
            return;
        }
        dialog.show();
        String localPath = Environment.getExternalStorageDirectory().getPath() + "/download/";
        final String fileName = chooseImgUrl.substring(chooseImgUrl.lastIndexOf("/") + 1);
        File file = new File(localPath + fileName);
        //判断本地是否存在图片
        if (file.exists()) {
            dealMainImg(localPath + fileName, fileName);
        } else {
            OkGo.<File>get(chooseImgUrl)
                    .execute(new FileCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<File> response) {
                            dealMainImg(response.body().getAbsolutePath(), response.body().getName());
                        }
                    });
        }
    }

    public void dealMainImg(final String fileDir, final String fileName) {

        long welcomeTaskId = SharedPreferencesSunmiUtil.getLong(getContext(), fileName);
        //判断是否有保存过这张图片的ID。如果有刚直接通过ID显示，如果没有则重新传到副屏页面
        if (welcomeTaskId == -1) {
            mDSKernel.sendFile(DSKernel.getDSDPackageName(), fileDir, new ISendCallback() {
                @Override
                public void onSendSuccess(long l) {
                    dialog.cancel();
                    SharedPreferencesSunmiUtil.put(getContext(), Sunmi.WELCOME_IMG_ID, l);
                    Sunmi.showWelcomeImage(fileDir);
                    Looper.prepare();
                    Toasty.success(getContext(), "操作成功！").show();
                    Looper.loop();
                }

                @Override
                public void onSendFail(int i, String s) {
                    Looper.prepare();
                    Toasty.success(getContext(), "操作失败，请退出重试！").show();
                    Looper.loop();
                    dialog.cancel();
                }

                @Override
                public void onSendProcess(long l, long l1) {
                }
            });
        } else {
            SharedPreferencesSunmiUtil.put(getContext(), Sunmi.WELCOME_IMG_ID, welcomeTaskId);
            Sunmi.showWelcomeImage(fileDir);
            Toasty.success(getContext(), "操作成功！").show();
            dialog.cancel();
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, SettingMainImgBean item, int position) {
        chooseImgUrl = item.getImgUrl();
    }
}
