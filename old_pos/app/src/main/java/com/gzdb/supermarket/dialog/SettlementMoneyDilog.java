package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.SettlementActivity;
import com.gzdb.supermarket.been.SettlementResultBean;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created Zxy Even on 2016/8/11.
 *                         清点现金
 */
public class SettlementMoneyDilog extends Dialog implements ScanGunKeyEventHelper.OnScanSuccessListener{
    private Context context;


    protected SettlementMoneyDilog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public SettlementMoneyDilog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public SettlementMoneyDilog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void onScanSuccess(String barcode) {

    }

    //点击搜索框以外的地方键盘消失
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    public static class Builder {
        private Context context;
        private EditText paidIn;
        private Button cancel;
        private Button submit;
      //  private BaseClient baseClient;

        public Builder(Context context) {
            this.context = context;
        }

        public SettlementMoneyDilog show() {
            SettlementMoneyDilog dialog = create();
            dialog.show();
            return dialog;
        }

        public SettlementMoneyDilog create() {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            baseClient = new BaseClient();

            // instantiate the dialog with the custom Theme
            final SettlementMoneyDilog moneydialog = new SettlementMoneyDilog(context, R.style.Dialog);
            moneydialog.setCanceledOnTouchOutside(true);

            View layout = inflater.inflate(R.layout.settlement_money_dialog, null);
            paidIn = (EditText) layout.findViewById(R.id.paid_in);
            cancel = (Button) layout.findViewById(R.id.cancel);
            submit = (Button) layout.findViewById(R.id.submit);


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moneydialog.dismiss();

                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    cashAmount
                    String p = paidIn.getText().toString();
                    if(p == null || p.trim().length() < 1) {
                        ToastUtil.showToast(context, "请输入结算金额！");
                        return;
                    }


                    OkGo.<NydResponse<SettlementResultBean>>post(Contonts.URL_SETTLEMENT_GET_DATA)
                            .params("cashAmount",p)
                            .execute(new DialogCallback<NydResponse<SettlementResultBean>>(context) {
                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<NydResponse<SettlementResultBean>> response) {
                                    if(response.body().response.getDatas().size()>0){
                                        //直接跳去结算
                                        Intent intent = new Intent(context, SettlementActivity.class);
                                        intent.putExtra("bean",new Gson().toJson(response.body().response));
                                        context.startActivity(intent);
                                        moneydialog.dismiss();

                                    }else{
                                        ToastUtil.showWrning(context,"暂无订单数据！");
                                    }
                                }
                            });
                }
            });

            moneydialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            moneydialog.setContentView(layout);
            return moneydialog;

        }


    }
}
