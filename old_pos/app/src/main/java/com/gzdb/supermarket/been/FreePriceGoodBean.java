package com.gzdb.supermarket.been;

import com.gzdb.supermarket.util.Arith;

import java.util.UUID;

/**
 * Created by nongyd on 17/6/21.
 * 自由价商品，只需输入价格
 */

public class FreePriceGoodBean extends GoodBean{

    public static String FREE_BARCODE="00000000";

    public FreePriceGoodBean(String salesPrice) {
        this.sortId = 0;
        this.id = UUID.randomUUID().toString();
        this.itemName = "其它";
        this.barcode = FREE_BARCODE;
        this.posTypeId = "8888888888";
        this.posTypeName = "自由价";
        this.itemUnitId = "8888888888";
        this.itemUnitName = "";
        this.itemTemplateId = "8888888888";
        this.createDate = "";
        this.buyCount = 0;
        this.description = "";
        this.isShelve = "Y";
        this.isDelete = "N";
        this.itemImg="";
        this.repertory = 100;
        this.warningRepertory = 100;
        this.shelfLife = 100;
        this.stockPrice = 0;
        this.salesPrice = Arith.round(Double.valueOf(salesPrice),2);
        this.generatedDate = 0;
        this.promotionPrice = 0;
    }
}
