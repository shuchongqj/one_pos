package com.gzdb.mall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ApplyStatusActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.iv_status)
    ImageView ivStatus;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_status);
        ButterKnife.bind(this);
        textTitle.setText("申请状态");
        status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            tvStatus.setText("申请加盟团购商城未通过");
            ivStatus.setImageResource(R.mipmap.ic_examine);
        } else {
            tvStatus.setText("提交申请成功");
            ivStatus.setImageResource(R.mipmap.ic_success);
        }
    }

    @OnClick({R.id.img_back, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_apply:
                finish();
                break;
        }
    }
}
