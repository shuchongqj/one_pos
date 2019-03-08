package com.gzdb.sale.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gzdb.basepos.R;

/**
 * Created by yunshi on 2017/11/20.
 */

public class ConfirmDialog extends Dialog {

    private Context mContext;
    private String str;
    private View.OnClickListener mListener;
    public ConfirmDialog(@NonNull Context context, String str, View.OnClickListener listener) {
        super(context, R.style.dialog);
        this.mContext=context;
        this.str=str;
        this.mListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        findViewById(R.id.btn_commit).setOnClickListener(mListener);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TextView content= (TextView) findViewById(R.id.tv_content);
        content.setText(str);
    }
}
