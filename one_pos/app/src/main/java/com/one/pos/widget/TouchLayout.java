package com.one.pos.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.anlib.http.Http;
import com.anlib.util.DensityUtils;
import com.anlib.util.ToastUtils;

/**
 * Created by Even on 2016/6/29.
 * 主页的拖动界面
 */
public class TouchLayout extends FrameLayout {

    private boolean intercept = false;
    private boolean init_xy = false;
    private int tx;
    private int ty;
    private View drapview;
    private View srcView;
    private View layout;
    private boolean movebool;
    private boolean upbool;

    //private GoodBean item;
    //private SupermarketShopView supermarketShopView;

    //App app;
    public TouchLayout(Context context) {
        super(context);
        //app= (App) context.getApplicationContext();
    }

    public TouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrapView(View drapview, View srcView, View layout) {
        this.drapview = drapview;
        this.srcView = srcView;
        this.layout = layout;
    }

    //public void setShopView(SupermarketShopView view) {
    //    this.supermarketShopView = view;
    //}

    public boolean isCanCallCancel() {
        return upbool && !movebool;
    }

    //public GoodBean getItem() {
    //    return this.item;
    //}

    //public void setItem(GoodBean item) {
    //    this.item = item;
    //}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 如果需要开户拦截
        if (this.intercept) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                upbool = true;
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                movebool = true;
            }

            return true;
        }
        return false;
    }

    public void setInterceptTouch(boolean touch) {
        this.intercept = touch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drapview == null || !intercept) {
            return super.onTouchEvent(event);
        }
        if (!init_xy) {
            init_xy = true;
            tx = (int) event.getX();
            ty = (int) event.getY();
        }
        switch (event.getAction()) {

            case MotionEvent.ACTION_MOVE:

                int tpx = (int) (tx - event.getX());
                int tpy = (int) (ty - event.getY());
                //调整位置
                LayoutParams flParams = (LayoutParams) drapview.getLayoutParams();
                int lm = flParams.leftMargin - tpx;
                int rm = flParams.topMargin - tpy;

                flParams.leftMargin = lm;
                flParams.topMargin = rm;

                drapview.setLayoutParams(flParams);

                tx = (int) event.getX();
                ty = (int) event.getY();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //拖动放开后，恢复原来位置
                touchCancel();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void touchCancel() {

        movebool = false;
        upbool = false;

        Log.e("zhumg", "touchCancel 1");

        if (drapview == null || !intercept) {
            return;
        }

        Log.e("zhumg", "touchCancel 2");

        if (drapview.getLeft() > 200 && drapview.getTop() < 200) {
            Log.e("zhumg", "touchCancel 3");
            up();
            return;
        }

        if (drapview.getLeft() > 200 && drapview.getBottom() > DensityUtils.getScreenHeight() - 200) {
            Log.e("zhumg", "touchCancel 4");
            down();
            return;
        }

        clearDrag();
    }

    public void clearDrag() {
        init_xy = false;
        drapview.setVisibility(View.GONE);
        srcView.setVisibility(View.VISIBLE);
        layout.setVisibility(View.GONE);
        setInterceptTouch(false);
    }

    //上架
    private void up() {
        clearDrag();
        //如果购物车有内容,不允许上下架,以防有下架商品又进行购物
//        if (ShopCart.nowShopCart.getAllCount() > 0) {
//            ToastUtil.showWrning(getContext(), "请清空购物车后再进行商品上下架");
//            return;
//        }
        //上架
        if (Http.getNetWorkConnectionType(getContext()) != -1) {

//            Intent intent = new Intent(getContext(), DrapUpActivity.class);
//            intent.putExtra("itemId", item.getId());
//            ((Activity) getContext()).startActivityForResult(intent, SupermarketIndexActivity.requestCode_UPDATE_CENTER_ITEMS);

        } else {
            ToastUtils.showWrning(getContext(), "当前无法联网，无法修改商品信息");
        }
    }

    //下架
    private void down() {
        clearDrag();
        //如果购物车有内容,不允许上下架,以防有下架商品又进行购物
//        if (ShopCart.nowShopCart.getAllCount() > 0) {
//            ToastUtil.showWrning(getContext(), "请清空购物车后再进行商品上下架");
//            return;
//        }
        //下架
        if (Http.getNetWorkConnectionType(getContext()) != -1) {
            //网络进行

//            HttpParams httpParams = new HttpParams();
//            httpParams.put("id", item.getId());
//            httpParams.put("barcode", item.getBarcode());
//            httpParams.put("isShelve", "N");
//
//            OkGo.post(Contonts.URL_EDIT_GOOD)
//                    .params(httpParams)
//                    .execute(new DialogCallback<NydResponse<GoodBean>>(getContext()) {
//                        @Override
//                        public void onSuccess(NydResponse<GoodBean> emptyBeanNydResponse, Call call, Response response) {
//                            ToastUtils.showSuccess(getContext(), "下架成功！");
//                            app.getDaoInstant().getGoodBeanDao().insertOrReplace(emptyBeanNydResponse.response);
//                            supermarketShopView.refreshCahce();
//                        }
//
//                        @Override
//                        public void onError(Call call, Response response, Exception e) {
//                            super.onError(call, response, e);
//                        }
//                    });
        } else {
            ToastUtils.showWrning(getContext(), "当前无法联网，无法修改商品信息");
        }
    }
}
