package com.gzdb.sunmi.util;

import android.content.Context;

import com.google.gson.Gson;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.entity.HeadBean;
import com.gzdb.sunmi.entity.ItemBean;
import com.gzdb.sunmi.entity.KVP;
import com.gzdb.sunmi.entity.ListingBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 徐荣 on 2016/9/20.
 */
public class DemoDataModel {
    private Context mContext;
    //head
    HeadBean mHeadBean = new HeadBean();
    //lists
    List<ItemBean> items = new ArrayList<>();
    //statement
    List<KVP> smBeens = new ArrayList<>();

    private static DemoDataModel mDataModel = null;

    private DemoDataModel(Context mContext) {
        this.mContext = mContext;
    }

    public static DemoDataModel getInstance(Context mContext) {
        if (mDataModel == null) {
            mDataModel = new DemoDataModel(mContext);
        }
        return mDataModel;
    }

    /**
     * 设置表头
     *
     * @return
     */
    public HeadBean setHeadBean() {
        mHeadBean.setParam1(mContext.getResources().getString(R.string.main_tb_goods_no));
        mHeadBean.setParam2(mContext.getResources().getString(R.string.main_tb_goods_name));
        mHeadBean.setParam3(mContext.getResources().getString(R.string.main_tb_goods_price));
        return mHeadBean;
    }

    /**
     * 添加商品
     *
     * @param mItemBean
     * @return
     */
    public String addGoods(ItemBean mItemBean) {
        mItemBean.setParam1("" + (items.size() + 1));
        items.add(mItemBean);
        return rebuildData();
    }

    boolean isOK = false;

    /**
     * 设置清算
     */
    public List<KVP> setSmBeen() {
        smBeens.clear();
        //数量
        KVP mKVP = new KVP();
        mKVP.setName(mContext.getString(R.string.main_total_price));
        mKVP.setValue("￥" + getTotalPrice());
        smBeens.add(mKVP);

        //数量
        KVP mKVP0 = new KVP();
        mKVP0.setName(mContext.getString(R.string.main_num));
        mKVP0.setValue("" + items.size());
        smBeens.add(mKVP0);

        //优惠
        KVP mKVP1 = new KVP();
        mKVP1.setName(mContext.getString(R.string.main_favorable_money));
        mKVP1.setValue("￥0.00");
        smBeens.add(mKVP1);

        //收款
        KVP mKVP2 = new KVP();
        mKVP2.setName(mContext.getString(R.string.main_collect));
        mKVP2.setValue("￥0.00");
        smBeens.add(mKVP2);

        return smBeens;
    }

    /**
     * 设置清算
     */
    public List<KVP> setSmBeen1() {
        smBeens.clear();
        //数量
        KVP mKVP = new KVP();
        mKVP.setName(mContext.getString(R.string.main_total_price));
        mKVP.setValue("￥" + getTotalPrice());
        smBeens.add(mKVP);

        //数量
        KVP mKVP0 = new KVP();
        mKVP0.setName(mContext.getString(R.string.main_num));
        mKVP0.setValue("" + items.size());
        smBeens.add(mKVP0);

        //优惠
        KVP mKVP1 = new KVP();
        mKVP1.setName(mContext.getString(R.string.main_favorable_money));
        mKVP1.setValue("￥5.00");
        smBeens.add(mKVP1);

        //收款
        KVP mKVP2 = new KVP();
        mKVP2.setName(mContext.getString(R.string.main_collect));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        mKVP2.setValue("￥" + decimalFormat.format(Float.parseFloat(getTotalPrice()) - 5));
        smBeens.add(mKVP2);

        return smBeens;
    }

    /**
     * 获取总价格
     *
     * @return
     */
    private String getTotalPrice() {
        float totalPrice = 0;
        for (ItemBean ib : items) {
            totalPrice = totalPrice + Float.parseFloat(ib.getParam3());
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(totalPrice);
    }

    /**
     * 清除商品
     *
     * @return
     */
    public String cleanGoods() {
        items.clear();
        return rebuildData();
    }

    /**
     * 重构json数据
     *
     * @return
     */
    public String rebuildData() {
        ListingBean mListingBean = new ListingBean();

        //title
        mListingBean.setTitle("1号生活");
        mListingBean.setHead(setHeadBean());
        mListingBean.setList(items);
        mListingBean.setKVPList(setSmBeen());

        String json = new Gson().toJson(mListingBean);

        return json;
    }

    /**
     * 重构json数据
     *
     * @return
     */
    public String rebuildData1() {
        ListingBean mListingBean = new ListingBean();
        //title
        mListingBean.setTitle(mContext.getString(R.string.main_sunmilk));
        mListingBean.setHead(setHeadBean());
        mListingBean.setList(items);
        mListingBean.setKVPList(setSmBeen1());

        String json = new Gson().toJson(mListingBean);

        return json;
    }

    /**
     * 动态解析jsonObject获取值列表
     *
     * @param json
     * @return
     */
    public ArrayList<String> getValueListByJsonObject(JSONObject json) {
        ArrayList<String> valueLists = new ArrayList<String>();
        try {
            Iterator it = json.keys();
            while (it.hasNext()) {
                String value = json.getString(it.next().toString());
                valueLists.add(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueLists;
    }

    /**
     * 动态解析jsonArray获取值列表
     *
     * @param array
     * @return
     */
    public List<ArrayList<String>> getValueListByJsonArray(JSONArray array) {
        List<ArrayList<String>> valueLists = new ArrayList<ArrayList<String>>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                ArrayList<String> list = getValueListByJsonObject(json);
                valueLists.add(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueLists;
    }
}
