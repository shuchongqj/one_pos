package com.anlib.refresh;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.one.anlib.R;


/**
 * @author zhumg
 */
public class DefaultRefreshLoadMoreUi implements RefreshLoaderUi {

    private View root;
    private Context context;
    private View pb_load;
    private TextView tv_message;
    private View.OnClickListener listener;

    public DefaultRefreshLoadMoreUi(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        if (root == null) {
            root = View.inflate(context, R.layout.widget_refresh_more_view, null);
            pb_load = root.findViewById(R.id.pb_load);
            tv_message = root.findViewById(R.id.tv_message);
            if (listener != null) {
                root.setOnClickListener(listener);
            }
        }
        return root;
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
        if (root != null) {
            root.setOnClickListener(listener);
        }
    }

    @Override
    public void showUi(RefreshLoaderUiTypeEnum typeEnum, String msg) {
        if (typeEnum == RefreshLoaderUiTypeEnum.LOAD) {
            root.setVisibility(View.VISIBLE);
            pb_load.setVisibility(View.VISIBLE);
            tv_message.setText(msg != null ? msg : "努力加载中");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.EMPTY) {
            root.setVisibility(View.VISIBLE);
            pb_load.setVisibility(View.GONE);
            tv_message.setText(msg != null ? msg : "没有更多数据了");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.ERROR) {
            root.setVisibility(View.VISIBLE);
            pb_load.setVisibility(View.GONE);
            tv_message.setText(msg != null ? msg : "加载失败，点击刷新");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.OVER) {
            root.setVisibility(View.VISIBLE);
            pb_load.setVisibility(View.GONE);
            tv_message.setText(msg != null ? msg : "已经加载全部数据");
        } else if (typeEnum == RefreshLoaderUiTypeEnum.HIDE || typeEnum == RefreshLoaderUiTypeEnum.DEFAULT) {
            root.setVisibility(View.GONE);
        }
    }


}
