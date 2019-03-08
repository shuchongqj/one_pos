package com.gzdb.sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaleTypeActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_type);
        ButterKnife.bind(this);
        textTitle.setText("活动类型");

        intent = new Intent(SaleTypeActivity.this, EditSaleActivity.class);
        intent.putExtra("method", "add");
        intent.putExtra("id", "");
    }

    @OnClick({R.id.img_back, R.id.item1, R.id.item2, R.id.item3, R.id.item4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                break;
            case R.id.item1:
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.item2:
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.item3:
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.item4:
                intent.putExtra("type", 4);
                startActivity(intent);
                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
