package com.core.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by nongyd on 17/7/31.
 */

public class MTextWatch implements TextWatcher {

    protected EditText editText;

    public MTextWatch(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
// TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
// TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
// TODO Auto-generated method stub
//这部分是处理如果输入框内小数点后有俩位，那么舍弃最后一位赋值，光标移动到最后
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {

                editText.setText(s.toString().subSequence(0,
                        s.toString().indexOf(".") + 2));

                editText.setSelection(s.toString().trim().length() - 1
                );
            }
        }
//这部分是处理如果用户输入以.开头，在前面加上0
        if (s.toString().trim().substring(0).equals(".")) {

            editText.setText("0" + s);
            editText.setSelection(2);
        }
        //这里处理用户 多次输入.的处理 比如输入 1..6的形式，是不可以的
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                editText.setText(s.subSequence(0, 1));
                editText.setSelection(1);
                return;
            }
        }
    }
}
