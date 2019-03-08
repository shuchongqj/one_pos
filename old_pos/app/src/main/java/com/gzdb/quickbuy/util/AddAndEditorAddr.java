package com.gzdb.quickbuy.util;

import android.view.View;
import android.widget.TextView;

import com.gzdb.quickbuy.bean.QuickBuyItem;

/**
 * Created by Zxy on 2016/12/6.
 *      一键采购添加地址和编辑地址
 */

public class AddAndEditorAddr {


    public static class AddrBent{
        public String receiverName; //门店名称
        public String receiverAddress; //门店地址
    }

    //显示周销量和采购值
    public static void hintNum(TextView tv, QuickBuyItem oneBuyItem){
        tv.setVisibility(View.GONE);
        tv.setText("该商品近3天销量:" + oneBuyItem.getStandardInventory() + ",周推荐采购值:" + oneBuyItem.getModernNum());

//        if (oneBuyItem.getCount() == oneBuyItem.getModernNum()){
//            tv.setVisibility(View.GONE);
//        }
    }

}
