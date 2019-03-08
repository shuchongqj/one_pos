package com.gzdb.vip;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.NoticeResulBean;
import com.gzdb.supermarket.event.VipPayEvent;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.GsonUtil;
import com.gzdb.supermarket.util.Utils;
import com.lidroid.xutils.view.annotation.event.EventBase;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * @author zhumg
 */
public class VipUserInfoDialog extends Dialog implements View.OnClickListener {

    TextView tv_user;
    TextView tv_phone;
    TextView tv_date;
    TextView tv_user_money;
    TextView tv_user_money_tip;

    String orderid;

    VipUserInfo userInfo;


    public VipUserInfoDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        setContentView(R.layout.vip_user_info_dialog);

        tv_user = findViewById(R.id.tv_user);
        tv_phone = findViewById(R.id.tv_phone);
        tv_date = findViewById(R.id.tv_date);
        tv_user_money = findViewById(R.id.tv_user_money);
        tv_user_money_tip = findViewById(R.id.tv_user_money_tip);

        findViewById(R.id.btn_in_money).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);

        if (VipCheckPhoneDialog.VIP_PAY_ORDER_ID == null) {
            findViewById(R.id.btn_pay).setVisibility(View.GONE);
        }

        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            dismiss();
        } else if (id == R.id.btn_in_money) {
            VipMoneyInDialog moneyInDialog = new VipMoneyInDialog(getContext());
            moneyInDialog.setUserInfo(this.userInfo);
            moneyInDialog.show();
            dismiss();
        } else if (id == R.id.btn_pay) {
            //获取会员卡编码，
            getPayCode();
        }
    }

    void getPayCode() {
        OkGo.<NydResponse<Object>>post(Contonts.customer_pay_getcode)
                .params("passport_id", String.valueOf(this.userInfo.getPassport_id()))
                .execute(new JsonCallback<NydResponse<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        //拿到code，继续去支付
                        try {
                            Object obj = response.body().response;
                            if (obj == null) {
                                return;
                            }
                            String str = GsonUtil.toJson(obj);
                            JSONObject json = new JSONObject(str);
                            String codeStr = json.getString("code");
                            EventBus.getDefault().post(new VipPayEvent(codeStr, userInfo));
                            dismiss();
                            //创建充值订单
                        } catch (Exception e) {
                            ToastUtil.showEorr(VipUserInfoDialog.this.getContext(), "接口返回参数异常");
                        }
                    }
                });
    }


    public void setUserInfo(VipUserInfo userInfo) {
        this.userInfo = userInfo;
        tv_user.setText("会员姓名：" + toStr(userInfo.getReal_name()) + "(" + ("0".equals(userInfo.getPay_member_type()) ? "普通会员" : "VIP会员") + ")");
        tv_phone.setText("手机号码：" + toStr(userInfo.getPhone_number()));
        tv_date.setText("到期日期：" + toStr(userInfo.getPay_member_time()));
        String balance = Utils.toYuanStr(userInfo.getBalance());
        tv_user_money.setText(balance + "元");
    }

    public static String toStr(String v) {
        if (v == null) {
            v = "";
        }
        return v;
    }
}
