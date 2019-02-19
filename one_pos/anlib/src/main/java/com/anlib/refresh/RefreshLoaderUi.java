package com.anlib.refresh;

import android.view.View;

public interface RefreshLoaderUi {
    View getView();
    void setOnClickListener(View.OnClickListener listener);
    void showUi(RefreshLoaderUiTypeEnum typeEnum, String msg);
}