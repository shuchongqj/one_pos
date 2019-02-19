package com.anlib.refresh;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

public class RefreshLoader {

    //是否允许下拉，默认为true
    protected boolean canRefresh = true;
    //是否能加载更多，默认不可以
    protected boolean canLoadMore = false;
    //是否在加载更多中
    protected boolean runLoadMore = false;

    void initAbsViewLoadMore(AbsListView listView, OnScrollBottomListener onScrollBottomListener) {
        listView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
        listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
    }

    protected interface OnScrollBottomListener {
        void onScorllBootom();
    }

    /**
     * 针对于电视 选择到了底部项的时候自动加载更多数据
     */
    private class ListViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private OnScrollBottomListener onScrollBottomListener;

        public ListViewOnItemSelectedListener(OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
            if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    /**
     * 滚动到底部自动加载更多数据
     */
    private static class ListViewOnScrollListener implements AbsListView.OnScrollListener {
        private OnScrollBottomListener onScrollBottomListener;

        public ListViewOnScrollListener(OnScrollBottomListener onScrollBottomListener) {
            super();
            this.onScrollBottomListener = onScrollBottomListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView listView, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
                if (onScrollBottomListener != null) {
                    onScrollBottomListener.onScorllBootom();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }
}
