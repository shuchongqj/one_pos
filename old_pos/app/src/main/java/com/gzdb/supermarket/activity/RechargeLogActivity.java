package com.gzdb.supermarket.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.RechargeLogAdapter;
import com.gzdb.supermarket.been.DataBean;
import com.gzdb.supermarket.been.RechargeLogBean;
import com.gzdb.supermarket.been.UserRechargeBean;
import com.gzdb.supermarket.dialog.SupermarketRechargeDialog;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by liubolin on 2017/12/28.
 */

public class RechargeLogActivity extends BaseActivity {

    public static final int RECHARGE_SYN_TIME = 3 * 1000; // 3秒刷新时间
    public static final int HANDLER_RECHARGE_SYN_CODE = 9001; // 心跳访问付款成功

    @Bind(R.id.recharge_log_list)
    ListView recharge_log_list;

    @Bind(R.id.text_title)
    TextView textTitle;

    @Bind(R.id.tv_recharge_time)
    TextView tv_recharge_time;

    @Bind(R.id.btn_recharge)
    View btn_recharge;

    RechargeLogAdapter adapter;

    private Handler mHandler;

    private SupermarketRechargeDialog supermarketRechargeDialog;

    private long defaultDiffer = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_log);
        ButterKnife.bind(this);
        textTitle.setText("续费管理");

        adapter = new RechargeLogAdapter(this);
        recharge_log_list.setAdapter(adapter);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_RECHARGE_SYN_CODE:
                        mHandler.removeMessages(HANDLER_RECHARGE_SYN_CODE);
                        getRecharge(true);//加载过期付款
                        break;
                    default:
                        break;
                }
            }
        };

        getRecharge(false);//加载过期付款
        getRechargeLog();
    }

    private void getRechargeLog() {
        OkGo.<NydResponse<RechargeLog>>post(Contonts.URL_GET_RENEWLOG)
                .params("passportId", app.getCurrentUser().getPassportId())
                .execute(new DialogCallback<NydResponse<RechargeLog>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<RechargeLog>> response) {
                        adapter.setRechargeLogDatas(response.body().response.getRenewLog());
                    }
                });
    }

    public void getRecharge(final boolean isShow) {
        OkGo.<NydResponse<DataBean<UserRechargeBean>>>post(Contonts.URL_RECHARGE)
                .params(new HttpParams("passportId", app.getCurrentUser().getPassportId()))
                .execute(new JsonCallback<NydResponse<DataBean<UserRechargeBean>>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<DataBean<UserRechargeBean>>> response) {
                        if (null != response.body() && null != response.body().response.getDatas() && response.body().response.getDatas().size() > 0) {
                            try {
                                if (null == supermarketRechargeDialog) {
                                    supermarketRechargeDialog = new SupermarketRechargeDialog(RechargeLogActivity.this, response.body().response.getDatas());
                                    supermarketRechargeDialog.setCanceledOnTouchOutside(true);
                                    supermarketRechargeDialog.setCancelable(true);
                                    supermarketRechargeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            mHandler.removeMessages(HANDLER_RECHARGE_SYN_CODE);
                                        }
                                    });
                                }

                                supermarketRechargeDialog.setTime(response.body().response.getDatas().get(0).getRenewTime());
                                tv_recharge_time.setText("您的租用时间于 " + Utils.stampToDate(String.valueOf(response.body().response.getDatas().get(0).getRenewTime())) + " 到期");

                                if (isShow && !supermarketRechargeDialog.isShowing()) {
                                    supermarketRechargeDialog.show();
                                }

                                long differ = response.body().response.getDatas().get(0).getDiffer();

                                if (defaultDiffer == -1) {
                                    defaultDiffer = differ;
                                }

                                if (differ > defaultDiffer) {
                                    supermarketRechargeDialog.dismiss();
                                    ToastUtil.showToast(RechargeLogActivity.this, "续租付款成功！");
                                    defaultDiffer = differ;
                                    getRechargeLog();
                                }
                                if (isShow) {
                                    mHandler.sendEmptyMessageDelayed(HANDLER_RECHARGE_SYN_CODE, RECHARGE_SYN_TIME);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
        });
    }

    @OnClick({R.id.img_back, R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.finish();
                break;
            case R.id.btn_recharge:
                getRecharge(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    class RechargeLog {
        List<RechargeLogBean> renewLog;

        public List<RechargeLogBean> getRenewLog() {
            return renewLog;
        }

        public void setRenewLog(List<RechargeLogBean> renewLog) {
            this.renewLog = renewLog;
        }
    }
}
