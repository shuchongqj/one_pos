package com.gzdb.vip;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

/**
 * @author zhumg
 */
public class VipCheckPhoneDialog extends Dialog implements View.OnClickListener {

    //检测支付时的订单号
    public static String VIP_PAY_ORDER_ID = "";

    EditText ev_phone;


    public VipCheckPhoneDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        setContentView(R.layout.vip_check_phone_dialog);
        ev_phone = findViewById(R.id.ev_phone);
        findViewById(R.id.btn_check).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        setCanceledOnTouchOutside(false);
    }

    public void setPhoneNumber(String phoneNumber) {
        String phone = VipUserInfoDialog.toStr(phoneNumber);
        ev_phone.setText(VipUserInfoDialog.toStr(phoneNumber));
        ev_phone.setSelection(phone.length());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            dismiss();
        } else if (id == R.id.btn_check) {
            final String phone = ev_phone.getText().toString();
            if (phone == null || phone.equals("")) {
                ToastUtil.showToast(this.getContext(), "请输入手机号");
                return;
            }
            OkGo.<NydResponse<VipUserInfo>>post(Contonts.offlinemember_getmemberinfo)
                    .params("phone_number", phone)
                    .execute(new JsonCallback<NydResponse<VipUserInfo>>() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<NydResponse<VipUserInfo>> response) {
                            VipUserInfo vipUserInfo = response.body().response;
                            if (vipUserInfo.getCode() == 10003) {
                                //未注册
                                VipJoinDialog joinDialog = new VipJoinDialog(VipCheckPhoneDialog.this.getContext());
                                joinDialog.setPhone(phone);
                                joinDialog.show();
                                dismiss();
                            } else {
                                //获取会员信息，则弹出会员信息
                                VipUserInfoDialog userInfoDialog = new VipUserInfoDialog(VipCheckPhoneDialog.this.getContext());
                                userInfoDialog.setUserInfo(response.body().response);
                                userInfoDialog.show();
                                dismiss();
                            }
                        }
                    });
        }
//        else if (id == R.id.btn_join_vip) {
//            VipJoinDialog joinDialog = new VipJoinDialog(getContext());
//            joinDialog.show();
//            dismiss();
//        }
    }

    public static void showVipCheckPhoneDialog(Context context, String orderId, String phoneNumber) {
        //跳去VIP
        VipCheckPhoneDialog.VIP_PAY_ORDER_ID = orderId;
        VipCheckPhoneDialog checkPhoneDialog = new VipCheckPhoneDialog(context);
        checkPhoneDialog.setPhoneNumber(phoneNumber);
        checkPhoneDialog.show();
    }


}
