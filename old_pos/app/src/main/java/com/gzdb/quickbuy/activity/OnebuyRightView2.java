package com.gzdb.quickbuy.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.OneBuyLeftTypeAdapter;
import com.gzdb.quickbuy.adapter.OutsideItemAdapter;
import com.gzdb.quickbuy.adapter.QuickBuyAdapter;
import com.gzdb.quickbuy.bean.BuyOutsideBean;
import com.gzdb.quickbuy.bean.CreateOrderItem;
import com.gzdb.quickbuy.bean.CreateOrderResult;
import com.gzdb.quickbuy.bean.MainResultBean;
import com.gzdb.quickbuy.bean.OneBuyTypeBean;
import com.gzdb.quickbuy.bean.QuickBuyItem;
import com.gzdb.quickbuy.util.AddAndEditorAddr;
import com.gzdb.quickbuy.util.OneBuyRightViewLogic;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.adapter.BaseGenericAdapter;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.util.baidu.LocationAddress;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

import static com.gzdb.basepos.R.mipmap.select;

/**
 * Created by Zxy on 2017/1/6.
 * 一键采购
 */

public class OnebuyRightView2 implements OneBuyRightViewLogic, View.OnClickListener {

    static TextView tv_dbonebuy, tv_outsideonebuy, internal_item_all;
    static View select_line;
    static QuickBuyActivity quickBuyActivity;
    final Map<Integer, List<QuickBuyItem>> itemListMap = new HashMap<>();       //系统内
    final Map<Integer, List<BuyOutsideBean>> outsideItemMap = new HashMap<>();  //系统外
    View rootOneView;
    ListView lv_item_type;
    //系统外建议采购
    LinearLayout ild_onebuy, ild_outside;//不一样的tile
    ImageView all_select_btn;
    TextView all_select_txt;
    ListView buy_listview;
    TextView no_info_txt;
    TextView btn_search;
    EditText et_item_search;
    RecyclerView rvList;
    NestedScrollView nv_list;
    double all_price = 0;
    //List<QuickBuyItem> adapterBuyItems = new ArrayList<>();
    Dialog OrDialog;
    boolean all_select = true;
    Map<String, String> itemMap;//装所以采购单位
    List<QuickBuyItem> buyItems = new ArrayList<>();  //系统内
    BuyItemsAdapter adapter;
    TextView tv_local_adr; // 当前地址
    List<OneBuyTypeBean> adapterBeanList = new ArrayList<>();//显示商品类型
    List<OneBuyTypeBean> oneBuyTypeBeenList = new ArrayList<>();//商品类型
    OneBuyLeftTypeAdapter oneBuyLeftTypeAdapter;
    List<BuyOutsideBean> buyOutsideBeanList = new ArrayList<>();//系统外建议采购
    List<OneBuyTypeBean> outsideBuyTypeBeenList = new ArrayList<>();//系统外 商品类型
    OutsideItemAdapter outsideItemAdapter;
    boolean outside_show = true;
    int leftSelectIndex = -1;
    int rightSelectIndex = -1;
    private String warehouseId;
    private String searchText;
    private LocationAddress mLocationAddress = null; //定位信息

    private QuickBuyAdapter quickBuyAdapter;

    public OnebuyRightView2(QuickBuyActivity quickBuyActivity) {
        rootOneView = View.inflate(quickBuyActivity, R.layout.quickbuy_item2, null);
        this.quickBuyActivity = quickBuyActivity;
        //初始化
        obrView();
        initOnclickObr();

        setTypeData();
        setInternalAdapter();
        getDataOutside();

        getOneBuyAllData();//总数据
    }

    //类型样式
    public static void allItemStyle(boolean allItem) {
        if (allItem) {
            select_line.setBackgroundResource(select);
            internal_item_all.setTextColor(quickBuyActivity.getResources().getColor(R.color.blue_color));
        } else {
            select_line.setBackgroundColor(quickBuyActivity.getResources().getColor(R.color.white_color));
            internal_item_all.setTextColor(quickBuyActivity.getResources().getColor(R.color.black));
        }
    }

    //拿到一键采购总数据
    public void getOneBuyAllData() {
        if (OrDialog == null) {
            OrDialog = DialogUtil.loadingDialog(quickBuyActivity, "定位中...");
        }
        OrDialog.show();
        //显示数据

//        setSelected(true);
    }

    public void setSginLocation(LocationAddress location) {
        if (location == null) {
            tv_local_adr.setText("定位失败！");
        } else {
            tv_local_adr.setText(location.getAddress());
            mLocationAddress = location;
        }
        OrDialog.dismiss();
        loadAllData();
    }

    //把所有的类型装入到容器里
    void setTypeData() {
        oneBuyLeftTypeAdapter = new OneBuyLeftTypeAdapter(quickBuyActivity, adapterBeanList, new Runnable() {
            @Override
            public void run() {
                clickType(oneBuyLeftTypeAdapter.getSelectPosition(), outside_show);
                //changeLeft(oneBuyLeftTypeAdapter.getSelectPosition());
            }
        });
        lv_item_type.setAdapter(oneBuyLeftTypeAdapter);
    }

    //把1号生活优惠采购装入到容器里
    public void setInternalAdapter() {
        adapter = new BuyItemsAdapter(quickBuyActivity, buyItems);
        buy_listview.setAdapter(adapter);
    }

    //系统外建议采购全部数据装入到容器
    public void getDataOutside() {
        outsideItemAdapter = new OutsideItemAdapter(quickBuyActivity, buyOutsideBeanList);
        buy_listview.setAdapter(outsideItemAdapter);

    }

    @Override
    public View getView() {
        return rootOneView;
    }

    @Override
    public void onVisible() {
    }

    @Override
    public void unVisible() {
    }

    void obrView() {

        nv_list = (NestedScrollView) rootOneView.findViewById(R.id.nv_list);
        rvList = (RecyclerView) rootOneView.findViewById(R.id.rv_list);
        tv_dbonebuy = (TextView) rootOneView.findViewById(R.id.tv_dbonebuy);
        tv_outsideonebuy = (TextView) rootOneView.findViewById(R.id.tv_outsideonebuy);
        internal_item_all = (TextView) rootOneView.findViewById(R.id.internal_item_all);
        select_line = rootOneView.findViewById(R.id.select_line);
        lv_item_type = (ListView) rootOneView.findViewById(R.id.lv_item_type);
        buy_listview = (ListView) rootOneView.findViewById(R.id.buy_listview);
        ild_onebuy = (LinearLayout) rootOneView.findViewById(R.id.ild_onebuy);
        ild_outside = (LinearLayout) rootOneView.findViewById(R.id.ild_outside);
        all_select_btn = (ImageView) rootOneView.findViewById(R.id.all_select_btn);
        tv_local_adr = (TextView) rootOneView.findViewById(R.id.tv_local_adr);
        btn_search = (TextView) rootOneView.findViewById(R.id.btn_search);
        et_item_search = (EditText) rootOneView.findViewById(R.id.et_item_search);
        //---
        all_select_txt = (TextView) rootOneView.findViewById(R.id.all_select_txt);
   /*     all_count_txt = (TextView) rootOneView.findViewById(R.id.all_count_txt);   //共几件
        all_price_txt = (TextView) rootOneView.findViewById(R.id.all_price_txt);    //一共多少钱*/
        no_info_txt = (TextView) rootOneView.findViewById(R.id.no_info_txt);  //显示文字
        tv_local_adr.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        quickBuyAdapter = new QuickBuyAdapter(buyItems, quickBuyActivity);
        rvList.setLayoutManager(new LinearLayoutManager(quickBuyActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvList.setAdapter(quickBuyAdapter);

        LimitsEditEnter(et_item_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dbonebuy://1号生活优惠采购
                nv_list.setVisibility(View.VISIBLE);
                buy_listview.setVisibility(View.GONE);
                et_item_search.setText("");
                searchText = "";
                getpagerObr(0);

                break;
            case R.id.tv_outsideonebuy://系统外建议采购
                nv_list.setVisibility(View.GONE);
                buy_listview.setVisibility(View.VISIBLE);
                et_item_search.setText("");
                searchText = "";
                getpagerObr(1);
                break;
            case R.id.all_select_txt: //全选
//                setSelected(!all_select);
                refreshBuyItemSelect();
                break;

            case R.id.internal_item_all:
                clickType(-1, outside_show);
                break;

            case R.id.tv_local_adr:
                quickBuyActivity.loadAddress();
                break;
            case R.id.btn_search:
                String search = et_item_search.getText().toString();
                search = search.replace(" ", "");
                if (TextUtils.isEmpty(search)) {
                    searchText = "";
                } else {
                    searchText = search;
                }
                clickType(-1, outside_show);
                break;
        }
    }

    void initOnclickObr() {
        tv_dbonebuy.setOnClickListener(this);
        tv_outsideonebuy.setOnClickListener(this);
        internal_item_all.setOnClickListener(this);
        all_select_txt.setOnClickListener(this);
    }

    void getpagerObr(int select) {
        if (select == 0) {//1号生活

            tv_dbonebuy.setTextColor(quickBuyActivity.getResources().getColor(R.color.blue));
            tv_dbonebuy.setBackgroundResource(R.color.onebuytile);
            ild_onebuy.setVisibility(View.VISIBLE);
            ild_outside.setVisibility(View.GONE);

            tv_outsideonebuy.setTextColor(quickBuyActivity.getResources().getColor(R.color.black));
            tv_outsideonebuy.setBackgroundResource(R.color.white);

            //ClearLists(0);
            //outside_show = false;
            //changeLeft(leftSelectIndex);
            clickType(leftSelectIndex, false);

        } else if (select == 1) {//系统外
            tv_dbonebuy.setTextColor(quickBuyActivity.getResources().getColor(R.color.black));
            tv_dbonebuy.setBackgroundResource(R.color.white);
            ild_onebuy.setVisibility(View.GONE);
            ild_outside.setVisibility(View.VISIBLE);

            tv_outsideonebuy.setTextColor(quickBuyActivity.getResources().getColor(R.color.blue));
            tv_outsideonebuy.setBackgroundResource(R.color.onebuytile);

            //ClearLists(1);
            //outside_show = true;
            //changeLeft(leftSelectIndex);

            clickType(rightSelectIndex, true);
        }
    }

    void setSelected(boolean s) {
        all_select = s;
        if (s) {
            all_select_btn.setImageResource(R.mipmap.btn_blue);
        } else {
            all_select_btn.setImageResource(R.mipmap.btn_normal);
        }
    }

    //一键采购的全部数据
    void loadAllData() {
        HttpParams param = new HttpParams();
        param.put("passportId", ((App) quickBuyActivity.getApplication()).getCurrentUser().getPassportId());
        if (null != mLocationAddress) {
            param.put("latitude", String.valueOf(mLocationAddress.getLatitude()));
            param.put("longitude", String.valueOf(mLocationAddress.getLongtitude()));
        }

        OkGo.<NydResponse<MainResultBean>>post(Contonts.ULR_SUPPLY_GET_ALL_DATA_1)
                .params(param)
                .execute(new DialogCallback<NydResponse<MainResultBean>>(quickBuyActivity) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<MainResultBean>> response) {
                        itemListMap.clear();
                        warehouseId = response.body().response.getWarehouseId();
                        List<QuickBuyItem> qbItems = response.body().response.getPreferentialList();
                        if (qbItems != null && qbItems.size() > 0) {

                            for (int i = 0; i < qbItems.size(); i++) {
                                QuickBuyItem qbi = qbItems.get(i);
                                int menuTypeId = Integer.parseInt(qbi.getMenuTypeId());
                                List<QuickBuyItem> maplist = itemListMap.get(menuTypeId);
                                if (maplist == null) {
                                    maplist = new ArrayList<QuickBuyItem>();
                                    itemListMap.put(menuTypeId, maplist);
                                }
                                maplist.add(qbi);
                            }
                        }

                        List<BuyOutsideBean> qbTypes0 = response.body().response.getExternalList();
                        if (qbTypes0 != null) {
                            for (int i = 0; i < qbTypes0.size(); i++) {

                                BuyOutsideBean qbi = qbTypes0.get(i);
                                int menuTypeId = 0;
                                try {
                                    menuTypeId = Integer.parseInt(qbi.getMenuTypeId());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    System.err.println("-------------->" + qbi.getBarcode());
                                    continue;
                                }
                                List<BuyOutsideBean> maplist = outsideItemMap.get(menuTypeId);
                                if (maplist == null) {
                                    maplist = new ArrayList<BuyOutsideBean>();
                                    outsideItemMap.put(menuTypeId, maplist);
                                }
                                maplist.add(qbi);
                            }
                        }
                        //商品类型
                        List<OneBuyTypeBean> qbTypesT = response.body().response.getMenutypeList();
                        List<OneBuyTypeBean> newQbt = new ArrayList<OneBuyTypeBean>();
                        if (qbTypesT != null) {
                            for (int i = 0; i < qbTypesT.size(); i++) {
                                OneBuyTypeBean bean = qbTypesT.get(i);
                                List<QuickBuyItem> maplist1 = itemListMap.get(bean.getId());
                                boolean a1 = maplist1 != null && maplist1.size() > 0;
                                if (a1) {
                                    newQbt.add(bean);
                                }
                            }
                            oneBuyTypeBeenList.addAll(newQbt);

                            newQbt.clear();
                            for (int i = 0; i < qbTypesT.size(); i++) {
                                OneBuyTypeBean bean = qbTypesT.get(i);
                                List<BuyOutsideBean> maplist2 = outsideItemMap.get(bean.getId());
                                boolean a2 = maplist2 != null && maplist2.size() > 0;
                                if (a2) {
                                    newQbt.add(bean);
                                }
                            }
                        }
                        outsideBuyTypeBeenList.addAll(newQbt);
                        clickType(-1, false);
                    }
                });
    }

    void refreshBuyItemSelect() {
        all_select = !all_select;
        //设置所有选中
        for (int i = 0; i < buyItems.size(); i++) {
            QuickBuyItem item = buyItems.get(i);
            item.setSelect(all_select);
            buyItems.set(i, item);
        }
        quickBuyAdapter.notifyDataSetChanged();
    }

    void submitBuy() {

        final List<QuickBuyItem> toItems = new ArrayList<>();
        final List<CreateOrderItem> items = new ArrayList<>();
        for (int i = 0; i < buyItems.size(); i++) {
            QuickBuyItem buyItem = buyItems.get(i);
            QuickBuyItem.QuickItem qi = buyItem.getSelectItem();
            if (qi == null || !buyItem.isSelect() || buyItem.getCount() < 1) {
                continue;
            }
            CreateOrderItem item = new CreateOrderItem();
            item.setCount(buyItem.getCount());
            item.setId(qi.getId());
            items.add(item);

            toItems.add(buyItem);
        }
        if (items.size() < 1) {
            ToastUtil.showToast(quickBuyActivity, "你还没有选择商品！");
            return;
        }

        HttpParams params = new HttpParams();
        params.put("warehouseId", warehouseId);
        params.put("itemSets", new Gson().toJson(items));

        final String jsonData = new Gson().toJson(items);

        OkGo.<NydResponse<CreateOrderResult>>post(Contonts.URL_SUPPLY_CREATE_ORDER)
                .params(params)
                .execute(new DialogCallback<NydResponse<CreateOrderResult>>(quickBuyActivity) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<CreateOrderResult>> response) {
                        Intent intent = new Intent();
                        intent.putExtra("orderId", response.body().response.getId());
                        intent.putExtra("payPrice", response.body().response.getTotalPrice());
                        intent.putExtra("deliverPrice", response.body().response.getDistributionFee());
                        intent.putExtra("sequenceNumber", response.body().response.getSequenceNumber());
                        intent.putExtra("isOrderToPay", false);
                        intent.putExtra("datas", (Serializable) toItems);
                        intent.putExtra("jsonData", jsonData);
                        intent.putExtra("paymentEnter", 1);
                        intent.putExtra("sequenceNumber", response.body().response.getSequenceNumber());
                        intent.setClass(quickBuyActivity, OrderBuyPayActivity.class);
                        quickBuyActivity.startActivityForResult(intent, SupermarketIndexActivity.requestCode_SUPPLY_PAY_RESULT);
                    }
                });

    }

    void clearBtn() {
        //
        allItemStyle(false);
        //
        if (outside_show) {
            if (rightSelectIndex >= 0 && rightSelectIndex < adapterBeanList.size()) {
                adapterBeanList.get(rightSelectIndex).setSelect(false);
            }
        } else {
            if (leftSelectIndex >= 0 && leftSelectIndex < adapterBeanList.size()) {
                adapterBeanList.get(leftSelectIndex).setSelect(false);
            }
        }
    }

    void clickLeftType(int index) {
        leftSelectIndex = index;
        if (index == -1) {
            buyItems.clear();
            buy_listview.setAdapter(adapter);
            Iterator<List<QuickBuyItem>> it = itemListMap.values().iterator();
            while (it.hasNext()) {
                List<QuickBuyItem> bobs = it.next();
                bobs = findQuickItemName(searchText, bobs);
                buyItems.addAll(bobs);
            }
//            if (buyItems.size() < 1) {
//                no_info_txt.setVisibility(View.VISIBLE);
//                buy_listview.setVisibility(View.GONE);
//            } else {
//                no_info_txt.setVisibility(View.GONE);
//                buy_listview.setVisibility(View.VISIBLE);
//            }
            adapter.notifyDataSetChanged();
            quickBuyAdapter.notifyDataSetChanged();
            allItemStyle(true);
        } else {
            int typeId = oneBuyTypeBeenList.get(leftSelectIndex).getId();
            adapterBeanList.get(leftSelectIndex).setSelect(true);
            buyItems.clear();
            buy_listview.setAdapter(adapter);
            List<QuickBuyItem> bobs = itemListMap.get(typeId);
            bobs = findQuickItemName(searchText, bobs);
            if (bobs != null) {
                buyItems.addAll(bobs);
                no_info_txt.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                quickBuyAdapter.notifyDataSetChanged();
            } else {
                no_info_txt.setVisibility(View.VISIBLE);
                no_info_txt.setText("该分类没有商品");
            }
        }
    }

    void clickRightType(int index) {
        rightSelectIndex = index;
        if (index == -1) {
            buyOutsideBeanList.clear();
            buy_listview.setAdapter(outsideItemAdapter);
            Iterator<List<BuyOutsideBean>> it = outsideItemMap.values().iterator();
            while (it.hasNext()) {
                List<BuyOutsideBean> bobs = it.next();
                bobs = findItemName(searchText, bobs);
                buyOutsideBeanList.addAll(bobs);
            }
            if (buyOutsideBeanList.size() < 1) {
                no_info_txt.setVisibility(View.VISIBLE);
                buy_listview.setVisibility(View.GONE);
            } else {
                no_info_txt.setVisibility(View.GONE);
                buy_listview.setVisibility(View.VISIBLE);
            }
            outsideItemAdapter.notifyDataSetChanged();
            allItemStyle(true);
        } else {
            int typeId = outsideBuyTypeBeenList.get(rightSelectIndex).getId();
            adapterBeanList.get(rightSelectIndex).setSelect(true);
            oneBuyLeftTypeAdapter.notifyDataSetChanged();
            buyOutsideBeanList.clear();
            buy_listview.setAdapter(outsideItemAdapter);
            List<BuyOutsideBean> bobs = outsideItemMap.get(typeId);
            bobs = findItemName(searchText, bobs);
            if (bobs != null) {
                buyOutsideBeanList.addAll(bobs);
                no_info_txt.setVisibility(View.GONE);
                outsideItemAdapter.notifyDataSetChanged();
            } else {
                no_info_txt.setVisibility(View.VISIBLE);
                no_info_txt.setText("该分类没有商品");
            }
        }
    }

    public List<QuickBuyItem> findQuickItemName(String searchTest, List<QuickBuyItem> list) {
        if (TextUtils.isEmpty(searchTest)) {
            return list;
        }
        List<QuickBuyItem> searchList = new ArrayList<>();

        Pattern pattern = Pattern.compile(searchTest);

        for (int i = 0; i < list.size(); i++) {
            QuickBuyItem quickBuyItem = list.get(i);
            List<QuickBuyItem.QuickItem> quickItems = new ArrayList<>();
            for (int j = 0; j < quickBuyItem.getItems().size(); j++) {
                QuickBuyItem.QuickItem quickItem = quickBuyItem.getItems().get(j);
                Matcher matcher = pattern.matcher(quickItem.getName());
                if (matcher.find()) {
                    quickItems.add(quickBuyItem.getItems().get(j));
                }
            }
            if (quickItems.size() == 0) {
                continue;
            }
            quickBuyItem.setItems(quickItems);
            searchList.add(quickBuyItem);
        }
        return searchList;
    }

    public List<BuyOutsideBean> findItemName(String searchTest, List<BuyOutsideBean> list) {
        if (TextUtils.isEmpty(searchTest)) {
            return list;
        }
        List<BuyOutsideBean> searchList = new ArrayList<>();

        Pattern pattern = Pattern.compile(searchTest);

        for (int i = 0; i < list.size(); i++) {
            BuyOutsideBean buyOutsideBean = list.get(i);
            List<BuyOutsideBean.QuickItem> quickItems = new ArrayList<>();
            for (int j = 0; j < buyOutsideBean.getItems().size(); j++) {
                BuyOutsideBean.QuickItem quickItem = buyOutsideBean.getItems().get(j);
                Matcher matcher = pattern.matcher(quickItem.getName());
                if (matcher.find()) {
                    quickItems.add(buyOutsideBean.getItems().get(j));
                }
            }
            if (quickItems.size() == 0) {
                continue;
            }
            buyOutsideBean.setItems(quickItems);
            searchList.add(buyOutsideBean);
        }
        return searchList;
    }

    void clickType(int index, boolean outside) {
        clearBtn();
        if (outside != outside_show) {
            if (outside) {
                adapterBeanList.clear();
                adapterBeanList.addAll(outsideBuyTypeBeenList);
                //oneBuyLeftTypeAdapter.notifyDataSetChanged();
            } else {
                adapterBeanList.clear();
                adapterBeanList.addAll(oneBuyTypeBeenList);
                //oneBuyLeftTypeAdapter.notifyDataSetChanged();
            }
        }
        outside_show = outside;
        if (outside_show) {
            clickRightType(index);
        } else {
            clickLeftType(index);
        }
        oneBuyLeftTypeAdapter.notifyDataSetChanged();
    }

    class BuyItemsAdapter extends BaseGenericAdapter {

        public BuyItemsAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            BuyItemView buyItemView = null;
            if (convertView == null) {
                convertView = View.inflate(quickBuyActivity, R.layout.adapter_quickbuy, null);
                buyItemView = new BuyItemView((ViewGroup) convertView);
                convertView.setTag(buyItemView);
            } else {
                buyItemView = (BuyItemView) convertView.getTag();
            }

            QuickBuyItem qbi = buyItems.get(position);
            buyItemView.buyAdapter = adapter;
            buyItemView.refresh(qbi);

            return convertView;
        }

        public void selectNotifyChanged() {
            boolean s = true;
            for (int i = 0; i < buyItems.size(); i++) {
                if (!buyItems.get(i).isSelect()) {
                    s = false;
                }
            }
            //全部选中
            setSelected(s);
            refreshData();
        }

        public void refreshData() {
            int count = 0;
            double price = 0;
            for (int i = 0; i < buyItems.size(); i++) {
                QuickBuyItem bi = buyItems.get(i);
                if (bi.isSelect()) {
                    if (bi.getSelectItem() == null) {
                        bi.setSelectItem(bi.getItems().get(0));
                    }
                    count += bi.getCount();
                    price += (bi.getCount() * Double.parseDouble(bi.getSelectItem().getPrice()));
                }
            }
           /*   old
           all_count_txt.setText(count + "");
            all_price_txt.setText("¥ " + Utils.doublePriceToStr(price));*/
            quickBuyActivity.allCountTxt.setText(count + "");
            quickBuyActivity.allPriceTxt.setText("¥ " + Utils.doublePriceToStr(price));

            all_price = price;
            super.notifyDataSetChanged();
        }
    }

    class BuyItemView implements View.OnClickListener, TextWatcher {

        View item_select_btn;
        ImageView item_select_img;
        TextView item_code;
        TextView item_name;
        TextView item_inventory;
        TextView item_price;
        XCDropDownListView item_unit;
        //TextView textSellPersent;
        //TextView textStockPersent;
        //TextView item_standard;
        TextView tv_hint;
        View count_del_btn;
        EditText count_edittext;
        View count_add_btn;
        TextView item_total_price;
        TextView sales_num;
        //选中的
        QuickBuyItem oneBuyItem;
        //
        int selectIndex;

        boolean update_edit = false;

        BuyItemsAdapter buyAdapter;

        public BuyItemView(ViewGroup vg) {
            item_select_btn = vg.findViewById(R.id.item_select_btn);
            sales_num = (TextView) vg.findViewById(R.id.sales_num);
            item_select_img = (ImageView) vg.findViewById(R.id.item_select_img);
            item_code = (TextView) vg.findViewById(R.id.item_code);
            item_name = (TextView) vg.findViewById(R.id.item_name);
            item_inventory = (TextView) vg.findViewById(R.id.item_inventory);
            item_price = (TextView) vg.findViewById(R.id.item_price);
            item_unit = (XCDropDownListView) vg.findViewById(R.id.item_unit);
            //item_standard = (TextView) vg.findViewById(R.id.item_standard);
            tv_hint = (TextView) vg.findViewById(R.id.tv_hint);
            count_del_btn = vg.findViewById(R.id.count_del_btn);
            count_edittext = (EditText) vg.findViewById(R.id.count_edittext);
            count_add_btn = vg.findViewById(R.id.count_add_btn);
            item_total_price = (TextView) vg.findViewById(R.id.item_total_price);
            //textStockPersent = (TextView) vg.findViewById(R.id.text_stockPersent);
            //textSellPersent = (TextView) vg.findViewById(R.id.text_sellPersent);

         /*item使用Edittext的时候
          NotShowKey.disableShowSoftInput(count_edittext);

            count_edittext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count_edittext.setSelection(count_edittext.getText().length());
                }
            });*/
            count_edittext.addTextChangedListener(this);
            count_del_btn.setOnClickListener(this);
            count_add_btn.setOnClickListener(this);
            item_select_btn.setOnClickListener(this);

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (update_edit) {
                update_edit = false;
                return;
            }
            if (s.length() < 1) {
                update_edit = true;
                count_edittext.setText("0");
                return;
            }
            int v = 0;
            try {
                v = Integer.parseInt(s.toString());
            } catch (Exception e) {
                e.printStackTrace();
                update_edit = true;
                count_edittext.setText("0");
                return;
            }
            if (v < 0) {
                v = 0;
            } else if (v > oneBuyItem.getSelectItem().getStock()) {
                ToastUtil.showToast(quickBuyActivity, "供应链可购买库存" + oneBuyItem.getSelectItem().getStock());
                update_edit = true;
                count_edittext.setText("" + oneBuyItem.getSelectItem().getDefaultPurchase());
                return;
            } else if (v > 10000) {
                ToastUtil.showToast(quickBuyActivity, "超出最大购买数量10000了");
                update_edit = true;
                count_edittext.setText("" + oneBuyItem.getSelectItem().getDefaultPurchase());
                return;
            }
            update_edit = true;
            count_edittext.setText("" + v);
            oneBuyItem.setCount(v);
            //刷新价格
            item_total_price.setText(Utils.doublePriceToStr(oneBuyItem.getCount() * Double.parseDouble(oneBuyItem.getSelectItem().getPrice())));
            adapter.refreshData();
            count_edittext.requestFocus();
            item_inventory.setText("" + oneBuyItem.getRepertory());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }


        public void refresh(final QuickBuyItem bi) {

            this.oneBuyItem = bi;
            if (this.oneBuyItem.getSelectItem() == null) {
                this.oneBuyItem.setSelectItem(this.oneBuyItem.getItems().get(0));
            }

            QuickBuyItem.QuickItem selectItem = this.oneBuyItem.getSelectItem();

            if (this.oneBuyItem.isSelect()) {
                item_select_img.setImageResource(R.mipmap.btn_blue);
            } else {
                item_select_img.setImageResource(R.mipmap.btn_normal);
            }
//            item_code.setText(oneBuyItem.getBarcode());  隐藏商品条码
            item_name.setText(selectItem.getName());
            sales_num.setText(String.valueOf(oneBuyItem.getStandardInventory()));
            item_inventory.setText(oneBuyItem.getRepertory());
            item_price.setText(selectItem.getPrice() + "");
            //textStockPersent.setText(selectItem.getBuyRate());
            //textSellPersent.setText(selectItem.getSaleRate());
            //将所有的单位装进去
            final List<XCDropDownListView.XCDropDownItem> list = item_unit.getItems();
            item_unit.setListviewWH(170, 150);
            list.clear();
            for (int i = 0; i < oneBuyItem.getItems().size(); i++) {
                list.add(new XCDropDownListView.XCDropDownItemStr(oneBuyItem.getItems().get(i).getUnit()));
            }
            item_unit.setShowIndex(selectIndex);

            //item_standard.setText(selectItem.getStandard());
            item_total_price.setText((Double.parseDouble(selectItem.getPrice()) * oneBuyItem.getCount()) + "");
            count_edittext.setText(oneBuyItem.getCount() + "");
            AddAndEditorAddr.hintNum(tv_hint, oneBuyItem);//显示周销量和采购值
            //当你选中就改变数据
            item_unit.setSelectCallback(new XCDropDownListView.SelectCallback() {
                @Override
                public void selected(int index) {
                    QuickBuyItem.QuickItem qi = oneBuyItem.getItems().get(index);
                    oneBuyItem.setSelectItem(qi);
                    selectIndex = index;
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.count_add_btn) {
                this.oneBuyItem.add();
                AddAndEditorAddr.hintNum(tv_hint, oneBuyItem);//显示周销量和采购值
                if (this.oneBuyItem.getCount() > 10000) {
                    this.oneBuyItem.setCount(10000);
                    ToastUtil.showToast(quickBuyActivity, "超出最大购买数量10000了");
                } else if (this.oneBuyItem.getCount() > this.oneBuyItem.getSelectItem().getStock()) {
                    this.oneBuyItem.setCount(this.oneBuyItem.getSelectItem().getStock());
                    ToastUtil.showToast(quickBuyActivity, "供应链可购买库存" + this.oneBuyItem.getSelectItem().getStock());
                }
                //刷新价格
                item_total_price.setText(Utils.doublePriceToStr(this.oneBuyItem.getCount() * Double.parseDouble(this.oneBuyItem.getSelectItem().getPrice())));
                buyAdapter.refreshData();
            } else if (id == R.id.count_del_btn) {
                this.oneBuyItem.del();
                AddAndEditorAddr.hintNum(tv_hint, oneBuyItem);//显示周销量和采购值
                if (this.oneBuyItem.getCount() < 0)
                    this.oneBuyItem.setCount(0);
                //刷新价格
                item_total_price.setText(Utils.doublePriceToStr(this.oneBuyItem.getCount() * Double.parseDouble(this.oneBuyItem.getSelectItem().getPrice())));
                buyAdapter.refreshData();
            } else if (id == R.id.item_select_btn) {
                this.oneBuyItem.setSelect(!this.oneBuyItem.isSelect());
                buyAdapter.selectNotifyChanged();
            }
        }
    }

    /**
     * 限制回车换行
     *
     * @param et
     */
    public static void LimitsEditEnter(EditText et) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

}
