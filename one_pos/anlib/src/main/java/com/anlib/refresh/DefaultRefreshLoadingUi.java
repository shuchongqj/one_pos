package com.anlib.refresh;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.one.anlib.R;


/**
 * @author zhumg
 */
public class DefaultRefreshLoadingUi implements RefreshLoaderUi {

    private View root;
    private View loadingView;
    private View otherView;
    private Context context;

    private TextView tv_message;
    private ImageView iv_img;

    public DefaultRefreshLoadingUi(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        if (root == null) {
            root = View.inflate(this.context, R.layout.widget_refresh_loading_view, null);
            loadingView = root.findViewById(R.id.l_loading_v);
            otherView = root.findViewById(R.id.l_other_v);
            tv_message = root.findViewById(R.id.tv_message);
            iv_img = root.findViewById(R.id.iv_img);
            root.setVisibility(View.GONE);
        }
        return root;
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        otherView.setOnClickListener(listener);
    }

    @Override
    public void showUi(RefreshLoaderUiTypeEnum typeEnum, String msg) {
        if (typeEnum == RefreshLoaderUiTypeEnum.LOAD) {
            root.setVisibility(View.VISIBLE);
            otherView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        } else if (typeEnum == RefreshLoaderUiTypeEnum.EMPTY) {
            root.setVisibility(View.VISIBLE);
            otherView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            tv_message.setText(msg != null ? msg : "没有更多数据了");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.ERROR) {
            root.setVisibility(View.VISIBLE);
            otherView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            tv_message.setText(msg != null ? msg : "加载异常，点击刷新");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.HIDE) {
            root.setVisibility(View.GONE);
        }
    }

}
