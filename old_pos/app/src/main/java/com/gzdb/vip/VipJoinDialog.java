package com.gzdb.vip;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.NoticeResulBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.GsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.zhumg.anlib.utils.ViewUtils;

import org.json.JSONObject;

/**
 * @author zhumg
 */
public class VipJoinDialog extends Dialog implements View.OnClickListener, Runnable {

    EditText ev_phone;
    EditText ev_name;
    EditText ev_code;

    TextView btn_code;

    boolean canGetCode = true;
    int mTotalTime;
    Handler handler;


    public VipJoinDialog(@NonNull Context context) {
        super(context, R.style.dialog_style);
        setContentView(R.layout.vip_join_dialog);
        ev_phone = findViewById(R.id.ev_phone);
        ev_name = findViewById(R.id.ev_name);
        ev_code = findViewById(R.id.ev_code);
        btn_code = findViewById(R.id.btn_code);
        btn_code.setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_check).setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        handler = new Handler();
    }

    @Override
    public void run() {
        mTotalTime--;
        if (mTotalTime == 0) {
            //停止
            canGetCode = true;
            handler.removeCallbacks(this);
            btn_code.setText("获取验证码");
            btn_code.setBackgroundResource(R.drawable.bg_corner_orange);
        } else {
            btn_code.setBackgroundResource(R.drawable.bg_corner_gray);
            btn_code.setText("重新获取(" + mTotalTime + ")");
            handler.postDelayed(this, 1000);
        }
    }

    @Override
    public void dismiss() {
        if (handler != null) {
            handler.removeCallbacks(this);
            handler = null;
        }
        super.dismiss();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_back) {
            dismiss();
        } else if (id == R.id.btn_code) {
            if (!canGetCode) {
                return;
            }
            //获取验证码
            String phone = ev_phone.getText().toString();
            if (phone == null || phone.trim().equals("")) {
                ToastUtil.showToast(this.getContext(), "请输入手机号");
                return;
            }

            canGetCode = false;
            mTotalTime = 59;
            btn_code.setText("重新获取(" + mTotalTime + ")");
            btn_code.setBackgroundResource(R.drawable.bg_corner_gray);
            handler.postDelayed(this, 1000);

            OkGo.<NydResponse<Object>>post(Contonts.offlinemember_sendcode)
                    .params("phone_number", phone)
                    .execute(new JsonCallback<NydResponse<Object>>() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                            String msg = response.body().msg;
                            if (msg == null) {
                                msg = "获取验证码成功";
                            }
                            ToastUtil.showToast(VipJoinDialog.this.getContext(), msg);
                        }
                    });

        } else if (id == R.id.btn_check) {
            String phone = ev_phone.getText().toString();
            if (phone == null || phone.trim().equals("")) {
                ToastUtil.showToast(this.getContext(), "请输入手机号");
                return;
            }
            String code = ev_code.getText().toString();
            if (code == null || code.trim().equals("")) {
                ToastUtil.showToast(this.getContext(), "请输入验证码");
                return;
            }
            String name = ev_name.getText().toString();
            if (name == null) {
                name = "";
            }
            //注册
            OkGo.<NydResponse<VipUserInfo>>post(Contonts.offlinemember_reg)
                    .params("phone_number", phone)
                    .params("code", code)
                    .params("real_name", name)
                    .execute(new JsonCallback<NydResponse<VipUserInfo>>() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<NydResponse<VipUserInfo>> response) {
                            try {
                                VipUserInfo userInfo = response.body().response;
                                VipUserInfoDialog userInfoDialog = new VipUserInfoDialog(VipJoinDialog.this.getContext());
                                userInfoDialog.setUserInfo(userInfo);
                                userInfoDialog.show();
                                dismiss();
                            } catch (Exception e) {
                                ToastUtil.showEorr(VipJoinDialog.this.getContext(), "接口返回参数异常");
                            }
                        }
                    });
        }
    }

    void setPhone(String phone) {
        ev_phone.setText(phone);
    }
}
