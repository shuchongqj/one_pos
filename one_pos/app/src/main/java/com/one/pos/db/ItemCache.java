package com.one.pos.db;

import android.content.Context;
import android.os.Handler;

import com.anlib.util.LogUtils;
import com.anlib.util.ToastUtils;
import com.anlib.util.Utils;
import com.one.pos.db.greendao.DaoMaster;
import com.one.pos.db.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemCache {

    //1分钟之内允许手动刷新同步一次商品类型
    private static final int REFRESH_ITEM_TYPE_TIME = 1000 * 60;
    private static long refresh_item_type_last_time = 0;
    //商品类型
    private static List<ItemType> itemTypes;
    private static Map<Long, ItemType> itemTypeMap;
    private static Map<Long, Item> itemMap;
    //
    private static Context context;

    private static DaoSession daoSession;

    public static void init(Context c) {
        context = c;

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(c, "one_pos_db");
        Database database = devOpenHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

        itemTypes = new ArrayList<>();
        itemMap = new HashMap<>();
        itemTypeMap = new HashMap<>();
    }

    public static ItemType getItemType(int id) {
        return itemTypeMap.get(id);
    }

    public static Item getItem(long id) {
        return itemMap.get(id);
    }

    public static void resizeItemStatus() {

    }

    public static void loadItemTypes(LocalItemTypeCallback localItemTypeCallback) {
//        if (Http.getNetWorkConnectionType(context) == -1) {
        //没有网络，直接加载本地
        ToastUtils.showToast(context, "当前网络异常，已从本地加载商品分类数据");
        loadLocalItemTypes(localItemTypeCallback);
//        } else {
//            loadRemoteItemTypes(localItemTypeCallback);
//        }
    }

    public static void loadLocalItemTypes(LocalItemTypeCallback localItemTypeCallback) {
        List<ItemType> itemTypes = daoSession.getItemTypeDao().loadAll();
        if (itemTypes == null || itemTypes.size() < 1) {
            //本地没有，则加载远程的
            loadRemoteItemTypes(localItemTypeCallback);
        } else {
            localItemTypeCallback.onCallback(itemTypes, 0, "加载成功");
        }
    }

    public static void loadRemoteItemTypes(final LocalItemTypeCallback localItemTypeCallback) {
        LogUtils.info("从 远程 加载itemTypes");
        //请求网络
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = Utils.random(0, 10) > 5 ? true : false;
                if (error) {
                    localItemTypeCallback.onCallback(null, 0, "网络异常，请稍后重试");
                } else {
                    //删除本地所有数据
                    daoSession.getItemTypeDao().deleteAll();
                    List<ItemType> list = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        ItemType itemType = new ItemType((i + 1), "分类" + (i + 1), i == 0);
                        list.add(itemType);
                        //保存到数据库
                        long id = daoSession.getItemTypeDao().insert(itemType);
                        itemType.setId(id);
                    }
                    refreshItemTypes(list);
                    localItemTypeCallback.onCallback(itemTypes, 1, null);
                }

            }
        }, 100);
    }

    public interface LocalItemTypeCallback {
        void onCallback(List<ItemType> itemTypes, int status, String msg);
    }

    public static void refreshItemTypes(List<ItemType> list) {
        if (list == null || list.size() < 1) {
            itemTypes.clear();
            itemTypeMap.clear();
            itemMap.clear();
        } else {
            for (int i = 0; i < itemTypes.size(); i++) {
                itemTypes.get(i).setStatus(ItemType.ITEM_TYPE_STATUS_TEMP);
            }
            List<ItemType> newItemTypes = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ItemType itemType = list.get(i);
                //取得本地的
                ItemType local = itemTypeMap.get(itemType.getId());
                if (local == null) {
                    local = itemType;
                    itemTypeMap.put(local.getId(), local);
                } else {
                    //将新数据拷贝
                    local.copy(itemType);
                }
                local.setStatus(ItemType.ITEM_TYPE_STATUS_DEFAULT);
                newItemTypes.add(local);
            }
            //删除已没有的
            for (int i = 0; i < itemTypes.size(); i++) {
                ItemType itemType = itemTypes.get(i);
                if (itemType.getStatus() == ItemType.ITEM_TYPE_STATUS_TEMP) {
                    itemTypeMap.remove(itemType.getId());
                    //本地也要删除
                    List<Item> items = itemType.getChilds();
                    if (items != null && items.size() > 0) {
                        for (int j = 0; j < items.size(); j++) {
                            itemMap.remove(items.get(j).getId());
                        }
                    }
                }
            }
            itemTypes.clear();
            itemTypes.addAll(newItemTypes);
        }
    }
}
