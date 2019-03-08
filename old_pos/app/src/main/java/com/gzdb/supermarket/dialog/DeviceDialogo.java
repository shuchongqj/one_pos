package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.http.HttpCallback;
import com.core.okgo.NydResponse;
import com.core.util.GsonUtils;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.mall.activity.ApplyStatusActivity;
import com.gzdb.mall.activity.ClauseActivity;
import com.gzdb.mall.activity.MallActivity;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.GlobalData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zhumg.anlib.AfainalDialogFragment;

import java.util.HashMap;
import java.util.Map;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceDialogo extends AfainalDialogFragment {


    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.img_close)
    ImageView imgClose;
    @Bind(R.id.edit_device_money)
    EditText edit_device_money;
    @Bind(R.id.btn_submit)
    TextView btnSubmit;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout(1073, 680);
        }
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


    @Override
    public int getContentViewId() {
        return R.layout.dialog_device;
    }

    @Override
    protected void initViewData(View view) {

        edit_device_money.setText(GlobalData.money);
        edit_device_money.setSelection(edit_device_money.getText().length());//将光标移至文字末尾
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        if (edit_device_money.getText().toString().length() == 0) {
            return;
        }
        Integer money = Integer.parseInt(edit_device_money.getText().toString());
        if (money == 0) {
            return;
        }


        OkGo.<String>post(Contonts.setbusinesssendfee)
                .params("passport_id", App.getInstance().currentUser.getPassportId())
                .params("send_fee", (money*100)+"")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {

                        dismiss();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        dismiss();
                    }
                });

    }
}