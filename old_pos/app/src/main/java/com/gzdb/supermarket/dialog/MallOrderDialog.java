package com.gzdb.supermarket.dialog;

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

public class MallOrderDialog extends Dialog {

    private View.OnClickListener mListener;
    public MallOrderDialog(@NonNull Context context, View.OnClickListener listener) {
        super(context, R.style.dialog);
        this.mListener=listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mall_order);
        findViewById(R.id.btn_commit).setOnClickListener(mListener);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TextView content= (TextView) findViewById(R.id.tv_content);
    }
}
