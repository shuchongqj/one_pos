package com.gzdb.supermarket.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.GoodUnitBean;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Even on 2017/1/10.
 */

public class AddItemUnitActivity extends Activity implements View.OnClickListener{

    EditText unit;
    Button cancel;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_unit_dialog);
        initView();
    }

    private void initView() {
        unit = (EditText) findViewById(R.id.unit);
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        BaseUtils.setEditTextInhibitInputSpeChat(unit);
    }

    @Override
    public void onClick(View v) {
        if(v == cancel){
            finish();
            return;
        }
        if(v == save){
            String unit_str = unit.getText().toString().trim();
            if(unit_str.length() > 10) {
                ToastUtil.showToast(this,"分类名称长度请不要超过15个字!");
            }else if(unit_str.length() < 1) {
                ToastUtil.showToast(this,"分类名称一定要填写哦!");
            }else {
                addUnitClient();
            }
        }


    }

    private void addUnitClient() {


        final String newUnit = unit.getText().toString();

        OkGo.<NydResponse<GoodUnitBean>>post(Contonts.URL_ADD_GOODTYPE)
                .params("title",newUnit)
                .execute(new DialogCallback<NydResponse<GoodUnitBean>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodUnitBean>> response) {
                        Intent intent = new Intent();
                        intent.putExtra("name", new Gson().toJson(response.body().response));
                        setResult(109, intent);
                        finish();
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(Contonts.URL_ADD_GOODTYPE);
    }
}
