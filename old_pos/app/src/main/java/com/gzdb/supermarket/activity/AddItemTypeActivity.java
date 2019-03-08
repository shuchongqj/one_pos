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
import com.gzdb.supermarket.been.GoodTypesBean;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 添加 商品类型
 * Created by zhumg on 8/16.
 */
public class AddItemTypeActivity extends Activity implements View.OnClickListener{

    private Button cancel;
    private Button save;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_type_dialog);
        initView();
    }

    private void initView() {
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);
        name = (EditText) findViewById(R.id.name);

        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        BaseUtils.setEditTextInhibitInputSpeChat(name);
    }

    @Override
    public void onClick(View v) {
        if (v == cancel) {
            finish();
            return;
        }
        if (v == save){
            String name_str = name.getText().toString().trim();
            if(name_str.length() > 15) {

                ToastUtil.showToast(this,"分类名称长度请不要超过15个字!");
            }else if(name_str.length() < 1) {
                ToastUtil.showToast(this,"分类名称一定要填写哦!");
            }else {
                addTypeClient();
            }
        }
    }

    private void addTypeClient() {

        final String newName = name.getText().toString();
        OkGo.<NydResponse<GoodTypesBean>>post(Contonts.URL_ADD_GOODTYPE)
                .params("title",newName)
                .execute(new DialogCallback<NydResponse<GoodTypesBean>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodTypesBean>> response) {
                        //成功，将分类 名返回
                        Intent intent = new Intent();
                        intent.putExtra("itemtype", new Gson().toJson(response.body().response));
                        setResult(102, intent);
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
