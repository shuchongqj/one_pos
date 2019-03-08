package com.one.pos.menubar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.anlib.http.Http;
import com.anlib.http.HttpCallback;
import com.anlib.util.ToastUtils;
import com.google.gson.Gson;
import com.one.pos.Contonts;
import com.one.pos.R;
import com.one.pos.menubar.activity.FreshActivity;
import com.one.pos.menubar.activity.VASActivity;
import com.one.pos.menubar.bean.FreshOrderNumber;
import com.one.pos.util.DialogUtil;
import com.one.pos.menubar.adapter.GridViewAdpter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: even
 * Date:   2019/3/4
 * Description:菜单栏
 */
public class MenuDialog extends DialogFragment implements View.OnClickListener {

    private ViewPager viewpager;
    private LinearLayout points;
    private ImageView img_close;
    private Dialog dialog;
    private List<MenuIconBean> miblist = null;
    private List<MenuBean> listDatas;
    private int totalPage;
    private int mPageSize = 8;
    private List<View> viewPagerList;
    private ImageView[] ivPoints;

    private int OverhangNum;
    private int RefundNum;
    private FreshOrderNumber orderNumber;


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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_menu, container, false);
        dialog = DialogUtil.loadingDialog(getContext(), "正在发请求，请稍候...");
        miblist = new ArrayList<>();
        initId(view);
        initMenu();
        initList();
        initData();
        getAcceptOrderCount();
        return view;
    }

    private void initId(View view) {
        viewpager = view.findViewById(R.id.viewpager);
        points = view.findViewById(R.id.points);
        img_close = view.findViewById(R.id.img_close);
        img_close.setOnClickListener(this);
    }

    private void initMenu() {
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
        mib.setProName("生鲜系统");
        mib.setBgColors("#00CC00");
        mib.setImages(R.mipmap.menu_fresh);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("增值服务");
        mib.setBgColors("#e08f05");
        mib.setImages(R.mipmap.menu_vas);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("续期管理");
        mib.setBgColors("#EE6AA7");
        mib.setImages(R.mipmap.menu_renewal);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("商品管理");
        mib.setBgColors("#7D26CD");
        mib.setImages(R.mipmap.menu_add);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("结算交班");
        mib.setBgColors("#d81e06");
        mib.setImages(R.mipmap.menu_conut);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("拍照打卡");
        mib.setBgColors("#d81e06");
        mib.setImages(R.mipmap.menu_conut);
        miblist.add(mib);

        mib = new MenuIconBean();
        mib.setProName("折扣优惠");
        mib.setBgColors("#4DB361");
        mib.setImages(R.mipmap.menu_discount);
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
        mib.setProName("会员管理");
        mib.setBgColors("#e08f05");
        mib.setImages(R.mipmap.menu_member);
        miblist.add(mib);


    }

    public void initList() {
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
            final GridView gridView = (GridView) View.inflate(getContext(), R.layout.menu_item_gridview, null);
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
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    Intent intent = new Intent(getActivity(), AddProductActivity.class);
//                                    getActivity().startActivityForResult(intent, SupermarketIndexActivity.requestCode_UPDATE_ALL);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，无法商品入库");
                                }
                                dismiss();
                                break;
                            case "一键采购":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
                                    //无码收银
//                                    Intent intent = new Intent(getActivity(), QuickBuyActivity.class);
//                                    startActivity(intent);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，无法一键采购");
                                }
                                dismiss();
                                break;

                            case "生鲜系统":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
                                    getFreshSystem();
                                } else {
                                    ToastUtils.show(getActivity(), "当前没联网，无法查看生鲜系统");
                                }
                                break;

                            case "增值服务":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
                                    Intent intentvas = new Intent(getActivity(), VASActivity.class);
                                    startActivity(intentvas);
                                    dismiss();
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                break;

                            case "续期管理":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    Intent intenta = new Intent(getActivity(), RechargeLogActivity.class);
//                                    startActivity(intenta);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，无法查看续期管理");
                                }
                                dismiss();
                                break;

                            case "商品管理":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    Intent intentm = new Intent(getActivity(), ItemManageActivity.class);
//                                    startActivityForResult(intentm, SupermarketIndexActivity.requestCode_UPDATE_LEFT_ITEMTYPE);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                dismiss();
                                break;

                            case "结算交班":
//                                Intent intentr = new Intent(getActivity(), SettlementActivity.class);
//                                startActivity(intentr);
                                break;

                            case "拍照打卡":

                                break;

                            case "折扣优惠":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    Intent sale = new Intent(getActivity(), SaleActivity.class);
//                                    startActivity(sale);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                dismiss();
                                break;

                            case "报表":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    Intent intent = new Intent(getActivity(), ReportActivity.class);
//                                    startActivity(intent);
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，无法查看报表");
                                }
                                dismiss();
                                break;

                            case "优惠活动":
                                if (Http.getNetWorkConnectionType(getActivity()) != -1) {
//                                    ActivityDialogo dialogo = new ActivityDialogo();
//                                    dialogo.show(getActivity().getSupportFragmentManager(), "actitivity");
                                    dismiss();
                                } else {
                                    ToastUtils.showToast(getActivity(), "当前没联网，此功能无法使用");
                                }
                                break;

                            case "设置":
                                //系统设置
//                                Intent intent = new Intent(getActivity(), SetupActivity.class);
//                                startActivity(intent);
                                dismiss();
                                break;

                            case "会员管理":

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

    //生鲜系统
    private void getFreshSystem() {
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("supplier_id", "1929556");//App.getInstance().currentUser.getPassportId()
        map.put("status", "1");
        map.put("deliver_status", "0");
        Http.post(getContext(), map, Contonts.FRESH_ORDER_LIST, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                dialog.dismiss();
//                if (response.body().code == 0) {
                    Intent intent = new Intent(getContext(), FreshActivity.class);
                    intent.putExtra("OverhangNum", OverhangNum);
                    intent.putExtra("RefundNum", RefundNum);
                    startActivity(intent);
//                }


            }
        });
    }


    //获取生鲜系统的订单数
    void getAcceptOrderCount() {
        Map<String, String> map = new HashMap<>();
        map.put("supplier_id", "1929556");//App.getInstance().currentUser.getPassportId()
        map.put("order_type", "-2");//订单类型：-2 pos/生鲜订单 0普通订单
        Http.post(getContext(), map, Contonts.FRESH_ORDER_NUMBER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                Gson gson = new Gson();
                orderNumber = gson.fromJson(data.toString(), FreshOrderNumber.class);
                OverhangNum = orderNumber.getOrder_count();
                RefundNum = orderNumber.getRefund_apply_count();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;

        }

    }

    public class MenuIconBean {

        private String proName;
        private String bgColors;
        private int images;

        public String getProName() {
            return proName;
        }

        public void setProName(String proName) {
            this.proName = proName;
        }

        public String getBgColors() {
            return bgColors;
        }

        public void setBgColors(String bgColors) {
            this.bgColors = bgColors;
        }

        public int getImages() {
            return images;
        }

        public void setImages(int images) {
            this.images = images;
        }
    }

    public class MenuBean {
        private String name;
        private int imgge;
        private String color;

        public MenuBean(String name, int imgge, String color) {
            this.name = name;
            this.imgge = imgge;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getImgge() {
            return imgge;
        }

        public void setImgge(int imgge) {
            this.imgge = imgge;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {

        private List<View> viewList;


        public ViewPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList != null ? viewList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        /**
         * 将当前的View添加到ViewGroup容器中
         * 这个方法，return一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPage上
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }

}
