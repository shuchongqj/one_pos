package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gzdb.basepos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscountDialog extends Dialog {

    @Bind(R.id.et_number1)
    EditText etNumber1;
    @Bind(R.id.et_number2)
    EditText etNumber2;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_commit)
    Button btnCommit;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_desc)
    TextView tvDesc;

    private PriorityListener mListener;

    private double mDiscount;
    private String mTitle;
    private String mDesc;

    public DiscountDialog(@NonNull Context context, String title, String desc, double discount, PriorityListener listener) {
        super(context, R.style.Dialog);
        this.mListener = listener;
        this.mDiscount = discount;
        this.mTitle = title;
        this.mDesc = desc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_discount);
        ButterKnife.bind(this);

        tvTitle.setText(mTitle);
        if(!mDesc.equals("")){
            tvDesc.setVisibility(View.VISIBLE);
            tvDesc.setText(mDesc);
        }

        if (mDiscount > 0) {
            String[] strs = String.valueOf(mDiscount).split("\\.");
            String number1 = strs[0];
            String number2 = strs[1];
            etNumber1.setText(number1);
            etNumber2.setText(number2);
            etNumber2.requestFocus();
            etNumber2.setSelection(1);
        }

        etNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etNumber1.getText().toString().equals("")) {
                    etNumber1.clearFocus();
                    etNumber2.requestFocus();
                    if (!etNumber2.getText().toString().equals("")) {
                        etNumber2.setSelection(1);
                    }
                }
            }
        });

        etNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etNumber2.getText().toString().equals("")) {
                    etNumber2.clearFocus();
                    etNumber1.requestFocus();
                    if (!etNumber1.getText().toString().equals("")) {
                        etNumber1.setSelection(1);
                    }
                }
            }
        });

        btnCommit.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mumber1 = etNumber1.getText().toString();
                String mumber2 = etNumber2.getText().toString();
                if (mumber1.equals("")) {
                    mumber1 = "0";
                }
                if (mumber2.equals("")) {
                    mumber2 = "0";
                }
                String discountStr = mumber1 + "." + mumber2;
                double discount = Double.valueOf(discountStr);
                mListener.refreshPriorityUI(discount);
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(etNumber1.getWindowToken(), 0);
                }
                dismiss();
            }
        });
    }

    @OnClick(R.id.btn_cancel)
    public void onViewClicked() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(etNumber1.getWindowToken(), 0);
        }
        dismiss();
    }

    /**
     * 自定义Dialog监听器
     */
    public interface PriorityListener {
        /**
         * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
         */
        void refreshPriorityUI(double discount);
    }
}
