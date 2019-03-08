package com.gzdb.supermarket.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by nongyd on 17/6/2.
 */
@Entity
public class ItemSnapshotsBean {

    @Id
    private Long autoId;

    private Long OffLineOrderId;

    private String lineId;
    /**
     * itemUnitName :
     * marketPrice : 0.0
     * userMark :
     * introductionPage :
     * orderId : 100552
     * totalPrice : 1.0
     * itemBarcode : 88588
     * itemCode :
     * costPrice : 1.0
     * returnPrice : 0.0
     * itemTemplateId : 100084
     * itemId : 10027
     * itemBatches : 0
     * itemName : 旅途
     * normalPrice : 1.0
     * itemUnitId : 0
     * itemTypeName : 零食糖果
     * id : 100758
     * itemTypeId : 11000
     * normalQuantity : 1
     */

    private String itemUnitName;
    private double marketPrice;
    private String userMark;
    private String introductionPage;
    private String orderId;
    private String itemCode;
    private double returnPrice;
    private int itemBatches;
    private double normalPrice;
    private String itemUnitId;
    private String discountQuantity;
    private String discountPrice;
    @Unique
    private String id;

    public String getDiscountQuantity() {
        return discountQuantity;
    }

    public void setDiscountQuantity(String discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    private String itemId;
    //商品模板ID
    private String itemTemplateId;
    //商品名称
    private String itemName;
    //商品类型ID
    private String itemTypeId;
    //商品类型名称
    private String itemTypeName;
    //商品单位ID
    private String itemTypeUnitId;//这个是离线订单要用
    //商品单位名称
    private String itemTypeUnitName;//这个是离线订单要用
    //商品条码
    private String itemBarcode;
    //商品成本价
    private double costPrice;
    //商品售价
    private double totalPrice;
    //商品购买数量
    private int normalQuantity;
    //商品类型 1普通商品 2生鲜类
    private int itemType;

    @Generated(hash = 593207247)
    public ItemSnapshotsBean(Long autoId, Long OffLineOrderId, String lineId,
            String itemUnitName, double marketPrice, String userMark,
            String introductionPage, String orderId, String itemCode,
            double returnPrice, int itemBatches, double normalPrice,
            String itemUnitId, String discountQuantity, String discountPrice,
            String id, String itemId, String itemTemplateId, String itemName,
            String itemTypeId, String itemTypeName, String itemTypeUnitId,
            String itemTypeUnitName, String itemBarcode, double costPrice,
            double totalPrice, int normalQuantity, int itemType) {
        this.autoId = autoId;
        this.OffLineOrderId = OffLineOrderId;
        this.lineId = lineId;
        this.itemUnitName = itemUnitName;
        this.marketPrice = marketPrice;
        this.userMark = userMark;
        this.introductionPage = introductionPage;
        this.orderId = orderId;
        this.itemCode = itemCode;
        this.returnPrice = returnPrice;
        this.itemBatches = itemBatches;
        this.normalPrice = normalPrice;
        this.itemUnitId = itemUnitId;
        this.discountQuantity = discountQuantity;
        this.discountPrice = discountPrice;
        this.id = id;
        this.itemId = itemId;
        this.itemTemplateId = itemTemplateId;
        this.itemName = itemName;
        this.itemTypeId = itemTypeId;
        this.itemTypeName = itemTypeName;
        this.itemTypeUnitId = itemTypeUnitId;
        this.itemTypeUnitName = itemTypeUnitName;
        this.itemBarcode = itemBarcode;
        this.costPrice = costPrice;
        this.totalPrice = totalPrice;
        this.normalQuantity = normalQuantity;
        this.itemType = itemType;
    }

    @Generated(hash = 809365966)
    public ItemSnapshotsBean() {
    }

    public Long getAutoId() {
        return this.autoId;
    }
    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }
    public Long getOffLineOrderId() {
        return this.OffLineOrderId;
    }
    public void setOffLineOrderId(Long OffLineOrderId) {
        this.OffLineOrderId = OffLineOrderId;
    }
    public String getItemUnitName() {
        return this.itemUnitName;
    }
    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }
    public double getMarketPrice() {
        return this.marketPrice;
    }
    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }
    public String getUserMark() {
        return this.userMark;
    }
    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }
    public String getIntroductionPage() {
        return this.introductionPage;
    }
    public void setIntroductionPage(String introductionPage) {
        this.introductionPage = introductionPage;
    }
    public String getOrderId() {
        return this.orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getItemCode() {
        return this.itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public double getReturnPrice() {
        return this.returnPrice;
    }
    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }
    public int getItemBatches() {
        return this.itemBatches;
    }
    public void setItemBatches(int itemBatches) {
        this.itemBatches = itemBatches;
    }
    public double getNormalPrice() {
        return this.normalPrice;
    }
    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }
    public String getItemUnitId() {
        return this.itemUnitId;
    }
    public void setItemUnitId(String itemUnitId) {
        this.itemUnitId = itemUnitId;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getItemId() {
        return this.itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemTemplateId() {
        return this.itemTemplateId;
    }
    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }
    public String getItemName() {
        return this.itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemTypeId() {
        return this.itemTypeId;
    }
    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }
    public String getItemTypeName() {
        return this.itemTypeName;
    }
    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }
    public String getItemTypeUnitId() {
        return this.itemTypeUnitId;
    }
    public void setItemTypeUnitId(String itemTypeUnitId) {
        this.itemTypeUnitId = itemTypeUnitId;
    }
    public String getItemTypeUnitName() {
        return this.itemTypeUnitName;
    }
    public void setItemTypeUnitName(String itemTypeUnitName) {
        this.itemTypeUnitName = itemTypeUnitName;
    }
    public String getItemBarcode() {
        return this.itemBarcode;
    }
    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }
    public double getCostPrice() {
        return this.costPrice;
    }
    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
    public double getTotalPrice() {
        return this.totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public int getNormalQuantity() {
        return this.normalQuantity;
    }
    public void setNormalQuantity(int normalQuantity) {
        this.normalQuantity = normalQuantity;
    }
    public String getLineId() {
        return this.lineId;
    }
    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
