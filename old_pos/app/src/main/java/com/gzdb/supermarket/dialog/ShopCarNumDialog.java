package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.core.util.ToastUtil;
import com.gzdb.basepos.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zxy on 2017/1/4.
 * 购物车输入购买数量
 */

public class ShopCarNumDialog extends Dialog implements View.OnClickListener{

    Context cx;
    @Bind(R.id.et_quantity)
    EditText etQuantity;
    @Bind(R.id.definite)
    Button definite;
    @Bind(R.id.clos)
    Button clos;
    PretermissionCarNum pretermissionCarNum;
    String quantity;

    public ShopCarNumDialog(Context context,PretermissionCarNum pretermissionCarNum) {
        super(context, R.style.dialog);
        setContentView(R.layout.shopcarnumdialog);
        this.cx = context;
        this.pretermissionCarNum = pretermissionCarNum;
        ButterKnife.bind(this);
        etQuantity.setText("");
        initOnclick();
        textLength();

    }

    public void textLength(){
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int testSun = s.toString().length();
                if (s.length() >= 3){
                    Toast.makeText(cx, "最多只能输入三位",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initOnclick() {
        definite.setOnClickListener(this);
        clos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clos:
                this.dismiss();
                closeKey();
                break;
            case R.id.definite:
                quantity = etQuantity.getText().toString();
                if (quantity.equals("")){
                    ToastUtil.showToast(cx,"数量不能为空");
                    break;
                }
                pretermissionCarNum.pretermission(quantity);
                this.dismiss();
                closeKey();
                break;
        }

    }


    public static interface PretermissionCarNum{
        public void pretermission(String quantity);
    }

    public void closeKey(){
        /**隐藏软键盘**/
        InputMethodManager inputMgr = (InputMethodManager) cx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
    }
}
