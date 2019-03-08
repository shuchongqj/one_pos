package com.gzdb.mall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClauseActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.fr_webview)
    WebView frWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clause);
        ButterKnife.bind(this);
        textTitle.setText("申请入驻");

        WebSettings webSettings = frWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);

        frWebview.loadUrl("http://sc.0085.com/h5/agreement/index.html");
    }

    private void apply() {
        try {
            OkGo.<NydResponse<Object>>post(Contonts.APPLY_JOIN)
                    .params("passport_id", app.currentUser.getPassportId())
                    .execute(new JsonCallback<NydResponse<Object>>() {
                @Override
                public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                    try {
                        if (response.body().code == 0) {
                            Intent intent = new Intent(mContext, ApplyStatusActivity.class);
                            intent.putExtra("status", 0);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClick({R.id.img_back, R.id.btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_apply:
                apply();
                break;
        }
    }
}
