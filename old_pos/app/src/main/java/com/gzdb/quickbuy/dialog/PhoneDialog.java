package com.gzdb.quickbuy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.gzdb.basepos.R;

/**
 * Created by Zxy on 2016/12/13.
 */

public class PhoneDialog extends Dialog {

    TextView tv_phone;
    String phone;

    public PhoneDialog(Context context,String phone) {
        super(context, R.style.dialog);
        setContentView(R.layout.phone_dialog);
        setCanceledOnTouchOutside(true);
        this.phone = phone;
        initView();
    }
    void initView(){
        tv_phone = (TextView) this.findViewById(R.id.tv_phone);
        tv_phone.setText(phone);
    }
}
