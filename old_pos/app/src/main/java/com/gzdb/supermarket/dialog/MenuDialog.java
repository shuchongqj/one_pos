package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.core.http.Http;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.GsonUtils;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.fresh.activity.FreshActivity;
import com.gzdb.fresh.bean.FreshOrder;
import com.gzdb.fresh.bean.OrderNumber;
import com.gzdb.mall.activity.ApplyStatusActivity;
import com.gzdb.mall.activity.ClauseActivity;
import com.gzdb.mall.activity.MallActivity;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.quickbuy.activity.QuickBuyActivity;
import com.gzdb.sale.activity.SaleActivity;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.SettlementActivity;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.activity.AddItemActivity;
import com.gzdb.supermarket.activity.AddProductActivity;
import com.gzdb.supermarket.activity.ItemManageActivity;
import com.gzdb.supermarket.activity.RechargeLogActivity;
import com.gzdb.supermarket.activity.ReportActivity;
import com.gzdb.supermarket.activity.SetupActivity;
import com.gzdb.supermarket.adapter.GridViewAdpter;
import com.gzdb.supermarket.adapter.ViewPagerAdapter;
import com.gzdb.supermarket.been.MenuBean;
import com.gzdb.supermarket.been.MenuIconBean;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.GlobalData;
import com.gzdb.supermarket.util.LogUtils;
import com.gzdb.vaservice.VASActivity;
import com.gzdb.vip.VipCheckPhoneDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class MenuDialog extends DialogFragment {

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.points)
    LinearLayout points;

    private ImageView[] ivPoints;
    private int totalPage;
    private int mPageSize = 8;
    private List<MenuBean> listDatas;
    private List<View> viewPagerList;


    private String[] proName = {"商品入库", "一键采购", "团购商城", "增值服务", "续期管理", "商品管理",     //, "团购商城"
            "结算", "折扣优惠", "报表", "优惠活动", "设置", "起送价配置"};

    private String[] colors = {"#1296db", "#13227a", "#e03636", "#e08f05", "#EE6AA7", "#7D26CD",           //, "#e03636"
            "#d81e06", "#4DB361", "#d4237a", "#5f0c04", "#707070", "#f04848"};

    private int[] images = {R.mipmap.menu_add, R.mipmap.menu_buy, R.mipmap.ic_mall, R.mipmap.menu_vas, R.mipmap.ic_recharge_log, R.mipmap.menu_add,   //, R.mipmap.ic_mall
            R.mipmap.menu_conut, R.mipmap.ic_save, R.mipmap.menu_report, R.mipmap.menu_activity, R.mipmap.menu_seting, R.mipmap.nav_icon_device};

    private Dialog dialog;
    List<MenuIconBean> miblist = null;
    int OverhangNum;
    int RefundNum;
    private OrderNumber orderNumber;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.8), (int) (dm.heightPixels * 0.8));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_menu, container, false);
        ButterKnife.bind(this, view);
        dialog = DialogUtil.loadingDialog(getContext(), "正在发请求，请稍候...");
        miblist = new ArrayList<>();
        initMenu();
        initVie();
        initData();
        submit();
        getAcceptOrderCount();
        return view;
    }
    private  void initMenu(){
        miblist.clear();
        MenuIconBean mib = new MenuIconBean();
        mib.setProName("商品入库");
        mib.setBgColors("#1296db");
        mib.setImages(R.mipmap.menu_add);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("一键采购");
        mib.setBgColors("#13227a");
        mib.setImages(R.mipmap.menu_buy);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("团购商城");
        mib.setBgColors("#e03636");
        mib.setImages(R.mipmap.ic_mall);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("增值服务");
        mib.setBgColors("#e08f05");
        mib.setImages(R.mipmap.menu_vas);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("续期管理");
        mib.setBgColors("#EE6AA7");
        mib.setImages(R.mipmap.ic_recharge_log);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("商品管理");
        mib.setBgColors("#7D26CD");
        mib.setImages(R.mipmap.menu_add);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("结算");
        mib.setBgColors("#d81e06");
        mib.setImages(R.mipmap.menu_conut);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("折扣优惠");
        mib.setBgColors("#4DB361");
        mib.setImages(R.mipmap.ic_save);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("报表");
        mib.setBgColors("#d4237a");
        mib.setImages(R.mipmap.menu_report);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("优惠活动");
        mib.setBgColors("#5f0c04");
        mib.setImages(R.mipmap.menu_activity);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("设置");
        mib.setBgColors("#707070");
        mib.setImages(R.mipmap.menu_seting);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("生鲜系统");
        mib.setBgColors("#00CC00");
        mib.setImages(R.mipmap.menu_fresh);//menu_fresh
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("会员管理");
        mib.setBgColors("#e08f05");
        mib.setImages(R.mipmap.member);//menu_fresh
        miblist.add(mib);
    }

    public void initVie() {
        // TODO Auto-generated method stub
        listDatas = new ArrayList<MenuBean>();
        for (int i = 0; i < miblist.size(); i++) {
            listDatas.add(new MenuBean(miblist.get(i).getProName(), miblist.get(i).getImages(), miblist.get(i).getBgColors()));
        }
    }

    public void initData() {
        // TODO Auto-generated method stub
        //总的页数向上取整
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(getContext(), R.layout.item_menu_gridview, null);
            gridView.setAdapter(new GridViewAdpter(getContext(), listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    Object obj = gridView.getItemAtPosition(position);
                    if (obj != null && obj instanceof MenuBean) {
                        System.out.println(obj);
                        switch (((MenuBean) obj).getName()) {
                            case "商品入库":
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent intent = new Intent(getActivity(), AddProductActivity.class);
                                    getActivity().startActivityForResult(intent, SupermarketIndexActivity.requestCode_UPDATE_ALL);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，无法商品入库");
                                }
                                dismiss();
                                break;
                            case "一键采购":
                                if (Http.isNetworkConnected(getActivity())) {
                                    //无码收银
                                    Intent intent = new Intent(getActivity(), QuickBuyActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，无法一键采购");
                                }
                                dismiss();
                                break;
                            case "增值服务":
                                //增值服务
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent intentvas = new Intent(getActivity(), VASActivity.class);
                                    startActivity(intentvas);
                                    dismiss();
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }

                                break;
                            case "报表":
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent intent = new Intent(getActivity(), ReportActivity.class);
                                    startActivity(intent);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，无法查看报表");
                                }
                                dismiss();
                                break;
                            case "结算":
                                Intent intentr = new Intent(getActivity(), SettlementActivity.class);
                                startActivity(intentr);
                                break;
                            case "设置":
                                //系统设置
                                Intent intent = new Intent(getActivity(), SetupActivity.class);
                                startActivity(intent);
                                dismiss();
                                break;
                            case "优惠活动":
                                if (Http.isNetworkConnected(getActivity())) {
                                    ActivityDialogo dialogo = new ActivityDialogo();
                                    dialogo.show(getActivity().getSupportFragmentManager(), "actitivity");
                                    dismiss();
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                break;
                            case "续期管理":
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent intenta = new Intent(getActivity(), RechargeLogActivity.class);
                                    startActivity(intenta);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，无法查看续期管理");
                                }
                                dismiss();
                                break;
                            case "折扣优惠":
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent sale = new Intent(getActivity(), SaleActivity.class);
                                    startActivity(sale);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                dismiss();
                                break;
                            case "商品管理":
                                if (Http.isNetworkConnected(getActivity())) {
                                    Intent intentm = new Intent(getActivity(), ItemManageActivity.class);
                                    startActivityForResult(intentm, SupermarketIndexActivity.requestCode_UPDATE_LEFT_ITEMTYPE);
                                } else {
                                    ToastUtil.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                dismiss();
                                break;
                            case "团购商城":
                                getMallStatus();
                                break;
                            case "起送价配置":
                                DeviceDialogo dialogo = new DeviceDialogo();
                                dialogo.show(getActivity().getSupportFragmentManager(), "device");
                                dismiss();
                                break;
                            case "生鲜系统":
                                getFreshSystem();
                                break;
                            case "会员管理":
                                VipCheckPhoneDialog.showVipCheckPhoneDialog(MenuDialog.this.getContext(), null, "");
                                break;
                        }
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewpager.setAdapter(new ViewPagerAdapter(viewPagerList));

        //添加小圆点
        ivPoints = new ImageView[totalPage];
        points.removeAllViews();
        for (int i = 0; i < totalPage; i++) {
            //循坏加入点点图片组
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = 40;
            params.width = 40;
            imageView.setLayoutParams(params);
            ivPoints[i] = imageView;
            if (i == 0) {
                ivPoints[i].setImageResource(R.mipmap.ic_point_on);
            } else {
                ivPoints[i].setImageResource(R.mipmap.ic_point);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            points.addView(ivPoints[i]);
        }
        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //currentPage = position;
                for (int i = 0; i < totalPage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.mipmap.ic_point_on);
                    } else {
                        ivPoints[i].setImageResource(R.mipmap.ic_point);
                    }
                }
            }
        });
    }

    private void submit() {


        try {
            OkGo.<String>post(Contonts.getbusinesssendfee)
                    .params("passport_id", App.getInstance().currentUser.getPassportId())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            try {
                                String str = response.body();
                                JSONObject json = new JSONObject(str);
                                if (json == null) {
                                    return;
                                }

                                if (json.optInt("code") == 0) {
                                    JSONObject jsonObject = json.getJSONObject("response");
                                    if (jsonObject != null) {
                                        double d = jsonObject.optDouble("send_fee") / 100;
                                        BigDecimal bg = new BigDecimal(d);
                                        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                        GlobalData.money =(int)f1 + "";
                                    }
                                    initMenu();
                                    MenuIconBean mib = new MenuIconBean();
                                    mib.setProName("起送价配置");
                                    mib.setBgColors("#f04848");
                                    mib.setImages(R.mipmap.nav_icon_device);
                                    miblist.add(mib);
                                    initVie();
                                    initData();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<String> response) {
                            super.onError(response);
                            try {
                                ToastUtil.showToast(getContext(), response.body().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        dismiss();
    }

    //团购商城
    private void getMallStatus() {
        dialog.show();
        OkGo.<String>post(Contonts.MALL_ORDER_LIST)
                .params("passport_id", App.getInstance().currentUser.getPassportId())
                .params("deliver_status", 0)
                .params("current", 1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        dialog.dismiss();
                        NydResponse nydResponse = GsonUtils.convertEntity(response.body(), NydResponse.class);
                        Log.e("哈哈哈--------",nydResponse.response.toString());
                        if (nydResponse.code == 0) {
                            startActivity(new Intent(getContext(), MallActivity.class));
                        } else if (nydResponse.code == 10002) {
                            startActivity(new Intent(getContext(), ClauseActivity.class));
                        } else if (nydResponse.code == 10003) {
                            Intent intent = new Intent(getContext(), ApplyStatusActivity.class);
                            intent.putExtra("status", 1);
                            startActivity(intent);
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        dialog.dismiss();
                    }
                });
    }

    //生鲜系统
    private void getFreshSystem(){
        dialog.show();
        OkGo.<NydResponse<Object>>post(Contonts.FRESH_ORDER_LIST)
                .params("supplier_id", App.getInstance().currentUser.getPassportId())
                .params("status",1)
                .params("deliver_status", 0)
                .execute(new JsonCallback<NydResponse<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        try {
                            dialog.dismiss();
                            if (response.body().code == 0) {
                                Intent intent = new Intent(getContext(), FreshActivity.class);
                                intent.putExtra("OverhangNum", OverhangNum);
                                intent.putExtra("RefundNum", RefundNum);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //获取生鲜系统的订单数
    void getAcceptOrderCount(){
        OkGo.<NydResponse<OrderNumber>>post(Contonts.FRESH_ORDER_NUMBER)
                .params("supplier_id", App.getInstance().currentUser.getPassportId())
                .params("order_type",-2)////订单类型：-2 pos/生鲜订单 0普通订单
                .execute(new JsonCallback<NydResponse<OrderNumber>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<OrderNumber>> response) {
                        if (response.body().code == 0 ) {
                            orderNumber = response.body().response;
                            Log.e("getOrder_count",orderNumber.getOrder_count()+"");
                            OverhangNum = orderNumber.getOrder_count();
                            RefundNum = orderNumber.getRefund_apply_count();

                        }

                    }
                });

    }



}
