package com.gzdb.supermarket.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.common.DivisionEditText;

/**
 * Created by liubolin on 2018/5/17.
 */

public class StampSettingDialog extends Dialog implements View.OnClickListener {

    View btn_stamp_connect;

    DivisionEditText det_code;

    private Activity mContext;

    private StampSettingHost mHost;

    public interface StampSettingHost {
        void connectStamp(String code);
    }

    public void setHost(StampSettingHost host) {
        this.mHost = host;
    }

    public StampSettingDialog(@NonNull Activity context) {
        super(context, R.style.dialog);
        setContentView(R.layout.dialog_stamp_setting);
        setCanceledOnTouchOutside(true);
        this.mContext = context;

        det_code= (DivisionEditText) findViewById(R.id.det_code);

        btn_stamp_connect = findViewById(R.id.btn_stamp_connect);

        btn_stamp_connect.setOnClickListener(this);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        WindowManager m = mContext.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        p.height = (int) (d.getHeight() * 0.35); // 高度设置为屏幕的0.6
        p.width = (int) (d.getHeight() * 0.5); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

        String stampCode = SPUtil.getInstance().getString(context, "stamp_code");
        det_code.setText(stampCode);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_stamp_connect) {
            String code = det_code.getText().toString();
            if (null != mHost) {
                mHost.connectStamp(code);
            }
            dismiss();
        }
    }
}
