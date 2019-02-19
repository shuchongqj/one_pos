package com.anlib.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;


import com.anlib.util.LogUtils;
import com.anlib.widget.GridViewWithHeaderAndFooter;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * @author zhumg
 */
public class PtrLoader extends RefreshLoader implements RefreshLoader.OnScrollBottomListener {

    public static final int LOAD_TYPE_DEFAULT = 0;
    public static final int LOAD_TYPE_LOADING = 1;
    public static final int LOAD_TYPE_REFRESH = 2;
    public static final int LOAD_TYPE_LOADMORE = 3;

    Context context;
    PtrFrameLayout ptr;
    RefreshLoaderListener loaderListener;

    RefreshLoaderUi loadingView;
    RefreshLoaderUi loadmoreView;
    RefreshLoaderUiTypeEnum loadmoreUiTypeEnum;

    boolean isHasMore;

    //当前是刷新，还是加载更多
    int loadType = LOAD_TYPE_DEFAULT;

    //listview面板是否有headview，如果有，在refresh时，失败的时候，仅仅是弹窗提示
    boolean haveHeadView;

    /**
     * @param parent
     * @param ptr
     * @param loaderListener
     */
    public PtrLoader(View parent, PtrFrameLayout ptr, RefreshLoaderListener loaderListener) {
        this(parent, ptr, loaderListener, new DefaultRefreshLoadingUi(parent.getContext()),
                new DefaultRefreshLoadMoreUi(parent.getContext()));
    }

    public PtrLoader(View parent, PtrFrameLayout ptr, RefreshLoaderListener listener,
                     final RefreshLoaderUi loadingView, RefreshLoaderUi loadMoreView) {
        this.ptr = ptr;
        this.context = parent.getContext();
        this.loaderListener = listener;

        this.loadingView = loadingView;
        this.loadmoreView = loadMoreView;

        //添加加载页面
        if (loadingView != null) {
            ((ViewGroup) parent).addView(this.loadingView.getView());
        }

        //根据类型进行
        View listView = ptr.getContentView();
        if (listView instanceof ListView) {
            if (loadMoreView != null) {
                View footerView = loadMoreView.getView();
                ((ListView) listView).addFooterView(footerView);
            }
            initAbsViewLoadMore((AbsListView) listView, this);
        } else if (listView instanceof GridViewWithHeaderAndFooter) {
            if (loadMoreView != null) {
                View footerView = loadMoreView.getView();
                ((GridViewWithHeaderAndFooter) listView).addFooterView(footerView);
            }
            initAbsViewLoadMore((AbsListView) listView, this);
        } else if (listView instanceof RecyclerView) {

        }

        this.ptr.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadType = LOAD_TYPE_REFRESH;
                loaderListener.onLoading(false);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                //加载更多中
                if (loadType != LOAD_TYPE_DEFAULT) {
                    LogUtils.info("加载中，加载类型： " + loadType);
                    return false;
                }
                //不允许下拉刷新
                if (!canRefresh) {
                    LogUtils.info("canRefresh 不允许刷新");
                    return false;
                }
                if (!checkCanDoRefreshImpl()) {
                    LogUtils.info("checkCanDoRefreshImpl 不允许刷新");
                    return false;
                }
                return checkContentCanBePulledDown(frame, content, header);
            }
        });

        //如果有加载更多，则设置事件
        if (this.loadmoreView != null) {
            this.loadmoreView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onScorllBootom();
                }
            });
        }

        //加载界面
        if (this.loadingView != null) {
            this.loadingView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLoading();
                }
            });
        }
    }

    public void setHaveHeadView(boolean have) {
        this.haveHeadView = have;
    }

    public boolean checkCanDoRefreshImpl() {
        return true;
    }

    public void showLoading() {
        //如果内容不为空，则直接加载
        if (loadingView != null) {
            ptr.setVisibility(View.GONE);
            loadingView.showUi(RefreshLoaderUiTypeEnum.LOAD, null);
        }
        loadType = LOAD_TYPE_LOADING;
        loaderListener.onLoading(false);
    }

    public void showRefresh() {
        ptr.autoRefresh();
        loadType = LOAD_TYPE_REFRESH;
        loaderListener.onLoading(false);
    }

    public void refreshComplete(boolean hasMore, boolean hasEmpty, boolean hasError) {
        refreshComplete(hasMore, hasEmpty, hasError, null);
    }

    /**
     * 加载完成
     *
     * @param hasMore
     * @param hasEmpty
     * @param hasError
     */
    public void refreshComplete(boolean hasMore, boolean hasEmpty, boolean hasError, String msg) {
        ptr.refreshComplete();
        isHasMore = hasMore;

        //当前是加载更多的操作
        if (loadType == LOAD_TYPE_LOADMORE) {
            if (hasError) {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.ERROR, msg);
            } else if (hasEmpty) {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.EMPTY, msg);
            } else if (isHasMore) {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.DEFAULT, msg);
            } else {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.OVER, msg);
            }
        } else {
            //当前是刷新的操作
            if (hasError) {
                if (loadingView != null) {
                    loadingView.showUi(RefreshLoaderUiTypeEnum.ERROR, msg);
                    ptr.setVisibility(View.GONE);
                    return;
                }
            } else if (hasEmpty) {
                if (loadingView != null) {
                    loadingView.showUi(RefreshLoaderUiTypeEnum.EMPTY, msg);
                    ptr.setVisibility(View.GONE);
                    return;
                }
            } else {
                if (loadType == LOAD_TYPE_LOADING) {
                    ptr.setVisibility(View.VISIBLE);
                    loadingView.showUi(RefreshLoaderUiTypeEnum.HIDE, msg);
                }
            }

            if (isHasMore) {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.DEFAULT, msg);
            } else {
                loadmoreShowUi(RefreshLoaderUiTypeEnum.OVER, msg);
            }
        }

        loadType = LOAD_TYPE_DEFAULT;
    }

    @Override
    public void onScorllBootom() {
        if (!isHasMore) {
            loadmoreShowUi(RefreshLoaderUiTypeEnum.OVER, null);
            return;
        }
        if (loadType != 0) {
            LogUtils.info("加载中，加载类型： " + loadType);
            return;
        }
        loadType = LOAD_TYPE_LOADMORE;
        loadmoreShowUi(RefreshLoaderUiTypeEnum.LOAD, null);
        loaderListener.onLoading(true);
    }

    void loadmoreShowUi(RefreshLoaderUiTypeEnum typeEnum, String msg) {
        this.loadmoreUiTypeEnum = typeEnum;
        if (loadmoreView != null) {
            loadmoreView.showUi(typeEnum, msg);
        }
    }

}