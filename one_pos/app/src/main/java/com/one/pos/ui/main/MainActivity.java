package com.one.pos.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.anlib.GActivity;
import com.anlib.http.HttpCallback;
import com.anlib.util.ToastUtils;
import com.anlib.widget.GridViewWithHeaderAndFooter;
import com.gongwen.marqueen.SimpleMF;
import com.gongwen.marqueen.SimpleMarqueeView;
import com.one.pos.R;
import com.one.pos.TestActivity;
import com.one.pos.db.Item;
import com.one.pos.db.ItemCache;
import com.one.pos.db.ItemType;
import com.one.pos.service.scane.ScanGunKeyEventHelper;
import com.one.pos.service.test.ForegroundService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * @author zhumg
 */
public class MainActivity extends GActivity implements View.OnClickListener {

    ListView itemtype_list_view;
    ItemTypeAdapter itemTypeAdapter;
    ItemType selectItemType;
    List<ItemType> itemTypeModels;
    PtrClassicFrameLayout itemTypePtr;

    GridViewWithHeaderAndFooter item_grid_view;
    List<Item> itemModels;
    ItemAdapter itemAdapter;
    PtrClassicFrameLayout itemPtr;

    ListView cart_list_view;
    List<CartModel> cartModels;
    CartAdapter cartAdapter;

    EditText et_input_code;
    EditText et_input_free;

    TextView tv_user_name;
    TextView tv_item_count;
    TextView tv_all_price;

    TextView btn_save_order;
    TextView btn_clear;
    TextView btn_down_order;

    TextView btn_add;
    TextView btn_excel;
    TextView btn_refresh;

    SimpleMarqueeView tv_market_name;
    SimpleMarqueeView tv_message;

    Map<Long, ItemGroup> itemGroupMap = new HashMap<>();

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.ll_menu).setOnClickListener(this);

        itemTypeModels = new ArrayList<>();
        itemTypeAdapter = new ItemTypeAdapter(this, itemTypeModels);
        itemTypePtr = view.findViewById(R.id.l_itemtype_ptr);
        itemtype_list_view = view.findViewById(R.id.itemtype_list_view);
        itemtype_list_view.setAdapter(itemTypeAdapter);
        itemtype_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectItemType != null) {
                    selectItemType.setSelect(false);
                }
                selectItemType = itemTypeModels.get(i);
                selectItemType.setSelect(true);
                itemTypeAdapter.notifyDataSetChanged();
                ItemGroup itemGroup = itemGroupMap.get(selectItemType.getId());
                if (itemGroup == null) {
                    itemGroup = new ItemGroup();
                    itemGroupMap.put(selectItemType.getId(), itemGroup);
                }
                //未加载，去加载
                if (itemGroup.isNullItems()) {
                    itemGroup.setStatusToLoading();
                    //去加载，并刷新
                    new ItemLoadTask(selectItemType.getId()).run();
                } else if (itemGroup.isLoadSuccess()) {
                    //已成功加载
                    itemAdapter.clearAndRefresh(itemGroup.getItems());
                } else {
                    //内容加载中，则直接忽略，不执行
                }
            }
        });
        itemTypePtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新数据
                loadItemTypes();
            }
        });

        cartModels = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartModels);
        itemPtr = view.findViewById(R.id.l_item_ptr);
        cart_list_view = view.findViewById(R.id.cart_list_view);
        cart_list_view.setAdapter(cartAdapter);
        itemPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新数据
                new ItemLoadTask(selectItemType.getId()).run();
            }
        });

        itemModels = new ArrayList<>();
        itemAdapter = new ItemAdapter(this, itemModels);
        item_grid_view = view.findViewById(R.id.item_grid_view);
        item_grid_view.setAdapter(itemAdapter);

        et_input_code = view.findViewById(R.id.et_input_code);
        et_input_free = view.findViewById(R.id.et_input_free);

        btn_save_order = view.findViewById(R.id.btn_save_order);
        btn_save_order.setOnClickListener(this);

        btn_clear = view.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        btn_down_order = view.findViewById(R.id.btn_down_order);
        btn_down_order.setOnClickListener(this);

        btn_excel = view.findViewById(R.id.btn_excel);
        btn_excel.setOnClickListener(this);

        btn_refresh = view.findViewById(R.id.btn_refresh);
        btn_refresh.setOnClickListener(this);

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);

        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_item_count = view.findViewById(R.id.tv_item_count);
        tv_all_price = view.findViewById(R.id.tv_all_price);

        tv_market_name = view.findViewById(R.id.tv_market_name);
        SimpleMF<String> marqueeFactory = new SimpleMF(this);
        List<String> datas = new ArrayList<String>();
        datas.add("店铺名称跑马灯");
        marqueeFactory.setData(datas);
        tv_market_name.setMarqueeFactory(marqueeFactory);
        tv_market_name.startFlipping();

        tv_message = view.findViewById(R.id.tv_message);
        SimpleMF<String> marqueeFactory2 = new SimpleMF(this);
        datas = new ArrayList<>();
        datas.add("店铺名称跑马灯");
        marqueeFactory2.setData(datas);
        tv_message.setMarqueeFactory(marqueeFactory2);
        tv_message.startFlipping();

        showLoadingDialog();
        loadItemTypes();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add) {
            //添加自由价
            addFreePrice();
        } else if (id == R.id.btn_refresh) {
            //刷新
            showLoadingDialog();
            //重新加载商品分类，直接远程加载
            ItemCache.loadRemoteItemTypes(localItemTypeCallback);
        } else if (id == R.id.btn_clear) {
            //清除
            startActivity(new Intent(this, TestActivity.class));
        }
    }

    void addFreePrice() {
        String freePrice = et_input_free.getText().toString();
        double d = 0;
        try {
            d = Double.parseDouble(freePrice);
        } catch (Exception e) {
            ToastUtils.showToast(this, "自由价输入有误，请重新输入");
            return;
        }
        //添加自由价商品
    }

    private ItemCache.LocalItemTypeCallback localItemTypeCallback = new ItemCache.LocalItemTypeCallback() {
        @Override
        public void onCallback(List<ItemType> itemTypes, int status, String msg) {

            if (selectItemType != null) {
                selectItemType.setSelect(false);
                selectItemType = null;
            }

            itemTypePtr.refreshComplete();
            itemTypeAdapter.clearAndRefresh(itemTypes);

            if (itemTypes != null && itemTypes.size() > 0) {
                selectItemType = itemTypes.get(0);
                selectItemType.setSelect(true);
                new ItemLoadTask(selectItemType.getId()).run();
            } else {
                //清除所有商品
                itemAdapter.removeAll();
                itemAdapter.notifyDataSetChanged();
                ToastUtils.showToast(MainActivity.this, msg);
            }
            closeLoadingDialog();
        }
    };

    void loadItemTypes() {
        ItemCache.loadItemTypes(localItemTypeCallback);
    }

    //商品加载任务
    class ItemLoadTask extends HttpCallback<List<Item>> implements Runnable {

        //当前加载的类型
        long id;
        //加载到的数据
        List<Item> datas;

        public ItemLoadTask(long id) {
            this.id = id;
        }

        @Override
        public void onSuccess(List<Item> data) {
            this.datas = data;
            run();
        }

        @Override
        public void run() {
            showLoadingDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Item> items = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        items.add(new Item("商品_" + id + "_" + i, i, "1.3", "2.3", "2-3-2"));
                    }
                    //添加到
                    ItemGroup itemGroup = itemGroupMap.get(id);
                    if (itemGroup == null) {
                        itemGroup = new ItemGroup();
                        itemGroupMap.put(selectItemType.getId(), itemGroup);
                    }
                    boolean haveItems = items != null && items.size() > 0;
                    if (haveItems) {
                        itemGroup.setItems(items);
                        itemGroup.setStatusToLoadSuccess();
                    } else {
                        itemGroup.setStatusToDefault();
                    }
                    //如果是当前页面，则直接刷新
                    if (selectItemType != null && selectItemType.getId() == id) {
                        itemAdapter.clearAndRefresh(items);
                    }
                    itemPtr.refreshComplete();
                    closeLoadingDialog();
                }
            }, 300);
        }
    }

}
