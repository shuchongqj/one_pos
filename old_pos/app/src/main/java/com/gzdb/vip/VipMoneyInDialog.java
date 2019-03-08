package com.gzdb.vip;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.platform.comapi.map.E;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.GsonUtil;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

/**
 * @author zhumg
 */
public class VipMoneyInDialog extends Dialog implements View.OnClickListener {

    TextView tv_phone;
    EditText ev_money;
    TextView tv_tip;

    VipUserInfo userInfo;

    int inputMoney;
    String token;

    public VipMoneyInDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        setContentView(R.layout.vip_money_in_dialog);
        tv_phone = findViewById(R.id.tv_phone);
        tv_tip = findViewById(R.id.tv_tip);
        ev_money = findViewById(R.id.ev_money);
        findViewById(R.id.btn_in_money).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        setCanceledOnTouchOutside(false);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            if (this.userInfo != null) {
                VipUserInfoDialog userInfoDialog = new VipUserInfoDialog(getContext());
                userInfoDialog.setUserInfo(this.userInfo);
                userInfoDialog.show();
            }
            dismiss();
        } else if (id == R.id.btn_in_money) {
            String money = ev_money.getText().toString();
            if (money == null || money.trim().length() < 1) {
                ToastUtil.showEorr(this.getContext(), "请输入要充值的金额");
                return;
            }
            try {
                double moneyDouble = Double.parseDouble(money);
                inputMoney = (int) Arith.mul(moneyDouble, 100);
                if (token == null) {
                    gettoken();
                } else {
                    vipuserpay();
                }
            } catch (Exception e) {
                ToastUtil.showEorr(this.getContext(), "金额数值有误，请重新输入");
                return;
            }
        }
    }

    void gettoken() {
        OkGo.<NydResponse<Object>>post(Contonts.customer_base_gettoken)
                .params("passport_id", String.valueOf(this.userInfo.getPassport_id()))
                .execute(new JsonCallback<NydResponse<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        try {
                            Object obj = response.body().response;
                            if (obj == null) {
                                return;
                            }
                            String str = GsonUtil.toJson(obj);
                            JSONObject json = new JSONObject(str);
                            token = json.getString("token");
                            vipuserpay();
                            //创建充值订单
                        } catch (Exception e) {
                            token = null;
                            ToastUtil.showEorr(VipMoneyInDialog.this.getContext(), "接口返回参数异常");
                        }
                    }
                });
    }

    void vipuserpay() {
        OkGo.<NydResponse<Object>>post(Contonts.customer_vipuser_vipuserpay)
                .params("passport_id", String.valueOf(this.userInfo.getPassport_id()))
                .params("merchant_id", String.valueOf(App.getInstance().getCurrentUser().getPassportId()))
                .params("rechargeAmount", String.valueOf(inputMoney))
                .params("recharge_source", "offline")
                .execute(new JsonCallback<NydResponse<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        try {
                            //body:{"msg":"成功","code":0,"response":{"order_count":36,"refund_apply_count":14}}
                            Object obj = response.body().response;
                            if (obj == null) {
                                return;
                            }
                            String str = GsonUtil.toJson(obj);
                            JSONObject json = new JSONObject(str);
                            String pr_random_num = json.getString("pr_random_num");
                            String recharge_amount = json.getString("recharge_amount");
                            offline(pr_random_num, recharge_amount);
                            //创建充值订单
                        } catch (Exception e) {
                            ToastUtil.showEorr(VipMoneyInDialog.this.getContext(), "接口返回参数异常");
                        }
                    }

                });
    }

    void offline(String prRandomNum, String recharge_amount) {
        OkGo.<NydResponse<VipUserInfo>>post(Contonts.vipuserpaycallback_offline)
                .params("passport_id", String.valueOf(this.userInfo.getPassport_id()))
                .params("token", token)
                .params("recharge_amount", recharge_amount)
                .params("pr_random_num", prRandomNum)
                .execute(new JsonCallback<NydResponse<VipUserInfo>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<VipUserInfo>> response) {
                        try {
                            token = null;
                            VipUserInfo vipUserInfo = response.body().response;
                            VipUserInfoDialog userInfoDialog = new VipUserInfoDialog(getContext());
                            userInfoDialog.setUserInfo(vipUserInfo);
                            userInfoDialog.show();
                            dismiss();
                        } catch (Exception e) {
                            token = null;
                            ToastUtil.showEorr(VipMoneyInDialog.this.getContext(), "接口返回参数异常");
                        }
                    }

                    @Override
                    public void onError(Response<NydResponse<VipUserInfo>> response) {
                        token = null;
                        super.onError(response);
                    }
                });
    }

    public void setUserInfo(VipUserInfo userInfo) {
        this.userInfo = userInfo;
        this.tv_phone.setText("手机号码：" + VipUserInfoDialog.toStr(userInfo.getPhone_number()));
        this.tv_tip.setText(VipUserInfoDialog.toStr(userInfo.getRecharge_ordinance()));
    }
}
