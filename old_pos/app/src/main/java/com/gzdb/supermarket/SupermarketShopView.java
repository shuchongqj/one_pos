package com.gzdb.supermarket;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.coorchice.library.SuperTextView;
import com.core.base.MRecyclerView;
import com.core.http.Http;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.supermarket.adapter.CenterItemAdapter;
import com.gzdb.supermarket.adapter.MainGoodTypeAdapter;
import com.gzdb.supermarket.adapter.ShopCartAdapter;
import com.gzdb.supermarket.been.FreePriceGoodBean;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.GoodListBean;
import com.gzdb.supermarket.been.GoodTypeResultBean;
import com.gzdb.supermarket.been.GoodTypesBean;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.dialog.SingleDialog;
import com.gzdb.supermarket.event.SearchOnEvent;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.PrintUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.solart.turbo.MAdapterOnClickListener;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhumg on 7/16.
 */
public class SupermarketShopView implements MAdapterOnClickListener<GoodTypesBean> {


    CenterItemAdapter centerItemAdapter;
    ShopCartAdapter shopCartAdapter;
    MainGoodTypeAdapter typeAdapter;

    View rootView;
    SupermarketIndexActivity activity;
    SingleDialog singleDialog;
    @Bind(R.id.recyclerView)
    MRecyclerView typeRecyclerView;
    @Bind(R.id.menu_gridView)
    GridView centerListView;
    @Bind(R.id.shop_car_tip)
    LinearLayout shopCarTip;
    @Bind(R.id.shop_cart_list_menu)
    ListView shopCartListView;
    @Bind(R.id.btn_createOrder)
    TextView btnCreateOrder;
    @Bind(R.id.btn_kuadan)
    Button btnKuadan;
    @Bind(R.id.btn_clear)
    Button btnClear;
    @Bind(R.id.text_cout)
    TextView textCout;
    @Bind(R.id.text_price)
    TextView textPrice;


    //拼音搜索前选中的类型
    String select_item_type_id;
    @Bind(R.id.edit_freePrice)
    XEditText editFreePrice;
    @Bind(R.id.btn_addFreeGood)
    SuperTextView btnAddFreeGood;
    private int curentTypeIndex;
    private App app;
    private Handler myHandler;

    public SupermarketShopView(SupermarketIndexActivity activity) {
        this.activity = activity;
        app = (App) activity.getApplication();
    }

    public View getView() {
        if (rootView == null) {
            rootView = View.inflate(activity, R.layout.layout_supermarket_fragment, null);
            ButterKnife.bind(this, rootView);

            typeAdapter = new MainGoodTypeAdapter(activity, new ArrayList<GoodTypesBean>(), this);
            typeRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            typeRecyclerView.setAdapter(typeAdapter);

            centerItemAdapter = new CenterItemAdapter(activity, new ArrayList<GoodBean>());
            centerListView.setAdapter(centerItemAdapter);

            shopCartAdapter = new ShopCartAdapter(activity, this);
            shopCartListView.setAdapter(shopCartAdapter);

            editFreePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        EventBus.getDefault().post(new SearchOnEvent(true));
                    } else {
                        EventBus.getDefault().post(new SearchOnEvent(false));
                    }
                }
            });

            centerItemAdapter.setListener(new CenterItemAdapter.ClickListener() {
                @Override
                public void onClick(GoodBean bean) {
                    activity.showFreshDialog(bean);
                }
            });

        }
        return rootView;
    }


    public void loadData() {

        if (!Http.isNetworkConnected(activity)) {
            //从本地加载
            refreshCahce();
        } else {
            //从网络加载
            refreshFromNet();
        }
    }

    public void refreshFromNet() {
        SupermarketIndexActivity.showLoadDialog();
        app.getDaoInstant().getGoodBeanDao().deleteAll();
        app.getDaoInstant().getGoodUnitBeanDao().deleteAll();
        app.getDaoInstant().getGoodTypesBeanDao().deleteAll();
        getType();
    }

    /***
     * 刷新数据
     * 这个要记录当前所选的分类，只需刷新当前界面就可以了
     */
    public void refreshCahce() {
        typeAdapter.removeData(typeAdapter.getData());
        typeAdapter.addData(app.getDaoInstant().getGoodTypesBeanDao().loadAll());
        if (typeAdapter.getItem(curentTypeIndex) != null) {
            typeAdapter.chooseItem(curentTypeIndex, typeAdapter.getItem(curentTypeIndex));
        }
        SupermarketIndexActivity.closeDialog();
    }

    public void getType() {
        // SupermarketIndexActivity.setDialogMsg("数据加载中" + "10%...");
        OkGo.<NydResponse<GoodTypeResultBean>>post(Contonts.URL_GET_GOODTYPELIST)
                .execute(new JsonCallback<NydResponse<GoodTypeResultBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodTypeResultBean>> response) {
                        try {
                            //  SupermarketIndexActivity.setDialogMsg("数据加载中" + "50%...");
                            app.getDaoInstant().getGoodTypesBeanDao().insertOrReplaceInTx(response.body().response.getItemTypes());
                            app.getDaoInstant().getGoodUnitBeanDao().insertOrReplaceInTx(response.body().response.getItemUnitList());
                            typeAdapter.removeData(typeAdapter.getData());
                            typeAdapter.addData(response.body().response.getItemTypes());


                            //  if (response.body().response.getItemTypes().size() != 0) {
                            for (int i = 0; i < response.body().response.getItemTypes().size(); i++) {
                                final String id = response.body().response.getItemTypes().get(i).getItemTypeId();
                                App.getUIHandler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getGoodList(id);//传BBBBB表示要加载自由价商品
                                    }
                                }, 1000);
                                // Thread.sleep(1000);
                            }

                            //  } else {
                            //防止商家端删除分类，分类被删除则清空该分类下的商品
//                                centerItemAdapter.clearList();
//                                centerItemAdapter.notifyDataSort();
//                                SupermarketIndexActivity.closeDialog();
                            getGoodList("B");//传BBBBB表示要加载自由价商品

                            //  }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodTypeResultBean>> response) {
                        super.onError(response);
                        SupermarketIndexActivity.closeDialog();
                    }
                });
    }

    public void getGoodList(final String typeId) {
        HttpParams params = new HttpParams();
        params.put("itemTypeId", typeId);
        //  SupermarketIndexActivity.setDialogMsg("数据加载中" + "70%...");
        OkGo.<NydResponse<GoodListBean>>post(Contonts.URL_GET_GOODLIST)
                .params(params)
                .execute(new JsonCallback<NydResponse<GoodListBean>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        try {
                            //    SupermarketIndexActivity.setDialogMsg("数据加载中" + "90%...");
//                            if (response.body().response.getDatas().size() == 0) {
//                                centerItemAdapter.clearList();
//                                centerItemAdapter.notifyDataSort();
//                                app.getDaoInstant().getGoodBeanDao().deleteAll();
//                            } else {
                            app.getDaoInstant().getGoodBeanDao().insertOrReplaceInTx(response.body().response.getDatas());
                            if (!"BBBBB".equals(typeId)) {//如果是自由价商品这不选择
                                typeAdapter.chooseItem(0, typeAdapter.getItem(0));
                            }
//                            }
                            SupermarketIndexActivity.closeDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        super.onError(response);
                        SupermarketIndexActivity.closeDialog();
                    }
                });


    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, GoodTypesBean item, int position) {
        curentTypeIndex = position;
        centerItemAdapter.clearList();
        centerItemAdapter.addList(app.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.ItemTypeId.eq(item.getItemTypeId())).list());
        centerItemAdapter.notifyDataSort();
    }

    //拼音搜索
    public void pinyinSearch(String key) {
        List<GoodBean> items = app.getDaoInstant().getGoodBeanDao().queryBuilder()
                .whereOr(GoodBeanDao.Properties.Barcode.like("%" + key + "%"), GoodBeanDao.Properties.ItemName.like("%" + key + "%")).list();
        if (items != null) {
            centerItemAdapter.setList(items);
        } else {
            centerItemAdapter.clearList();
        }
        centerItemAdapter.notifyDataSort();
    }

    //刷新左边数据
    public void refreshLeftAdapter() {
        typeAdapter.removeData(typeAdapter.getData());
        typeAdapter.addData(app.getDaoInstant().getGoodTypesBeanDao().loadAll());
    }

    //刷新购物车
    public void refreshShopCart() {
        activity.btnSpread.setText("推广会员");
        //如果购物车有内容
        if (ShopCart.nowShopCart.getAllCount() > 0) {
            shopCartListView.setVisibility(View.VISIBLE);
            shopCarTip.setVisibility(View.GONE);
            //向副屏发送当前的item数据
            Sunmi.showCardImg(0, 0, activity.imageMenuDisplay);
        } else {
            shopCartListView.setVisibility(View.GONE);
            shopCarTip.setVisibility(View.VISIBLE);
//            ScreenUtil.getInstance().showScreen();
        }
        //刷新 合计数量
        textCout.setText(ShopCart.nowShopCart.getAllCount() + "");
        //刷新 合计价格
        textPrice.setText(ShopCart.nowShopCart.getAllPrice() + "");
        //刷新 购物车 适配器
        shopCartAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelAll();
    }

    @OnClick({R.id.btn_addFreeGood, R.id.btn_createOrder, R.id.btn_kuadan, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_addFreeGood://添加自由价商品
                try {
                    Double.valueOf(editFreePrice.getText().toString());
                } catch (Exception e) {
                    ToastUtil.showEorr(activity, "价格有误，请重新输入!");
                    return;
                }
                if (Double.valueOf(editFreePrice.getText().toString()) > 1000) {
                    ToastUtil.showEorr(activity, "单笔价格不能超过1000块！");
                    return;
                }
                ShopCart.nowShopCart.addItem(new FreePriceGoodBean(editFreePrice.getText().toString()));
                editFreePrice.setText("");
                refreshShopCart();
                break;
            case R.id.btn_createOrder:

                SupermarketIndexActivity.showLoadDialog();
                if (ShopCart.nowShopCart.getAllCount() < 1) {
                    ToastUtil.showToast(activity, "购物车为空");
                    SupermarketIndexActivity.closeDialog();
                    return;
                }
                if (textPrice.getText().toString().equals("0.0")) {
                    ToastUtil.showToast(activity, "无法购买价格为0的商品");
                    SupermarketIndexActivity.closeDialog();
                    return;
                }
                if (SPUtil.getInstance().getBoolean(activity, "orderVoice", true)) {
                    SupermarketIndexActivity.speak("共计" + ShopCart.nowShopCart.getAllCount() + "件商品");
                }
                activity.btnSpread.setText("推广会员");
                Sunmi.showCardImg(0, 0, activity.imageMenuDisplay);
                Intent intent = new Intent(activity, PlaceOrderActivity.class);
                activity.startActivityForResult(intent, 101);
                break;
            case R.id.btn_kuadan:
                if (!Sunmi.viceScreenMode) {
                    try {
                        app.mSerialPortOperaion.WriteData(0xC);
                    } catch (Exception e) {
                    }
                }

                if (ShopCart.nowShopCart.getAllCount() < 1) {
                    //有挂单
                    ShopCart.inputAll(SupermarketShopView.this.activity);
                    if (ShopCart.shopCarts.size() == 0) {
                        ToastUtil.showToast(activity, "无挂单数据,无法取单");
                    } else {
                        if (singleDialog == null) {
                            singleDialog = new SingleDialog(SupermarketShopView.this.activity);
                            singleDialog.setSupermarketShopView(this);
                        }
                        singleDialog.show();
                    }
                    return;
                }
                //挂单
                if (ShopCart.guai(activity)) {
                    //提示
                    ToastUtil.showToast(activity, "挂单成功");
                } else {
                    ToastUtil.showToast(activity, "挂单失败");
                }
                //刷新
                refreshShopCart();
                break;
            case R.id.btn_clear:
                if (!Sunmi.viceScreenMode) {
                    try {
                        app.mSerialPortOperaion.WriteData(0xC);
                    } catch (Exception e) {
                    }
                }
                ShopCart.nowShopCart.clear();
                //刷新
                refreshShopCart();
                ScreenUtil.getInstance().showScreen(activity.imageDisplay);
                break;
        }

    }

}
