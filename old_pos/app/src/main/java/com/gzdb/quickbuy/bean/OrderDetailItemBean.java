package com.gzdb.quickbuy.bean;

import java.io.Serializable;

/**
 * Created by nongyudi on 2017/6/4.
 */

public  class OrderDetailItemBean implements Serializable {
    /**
     * id : 100497
     * orderId : 100497
     * userMark : 100013
     * itemId : 100011
     * itemTemplateId : 100000
     * itemName : 农夫山泉4L*6
     * itemTypeId : 90018
     * itemTypeName : 瓶装水
     * itemUnitId : 100004
     * itemUnitName : 件
     * itemBarcode : 6921168559173
     * itemCode : G100000008
     * itemBatches : 0
     * introductionPage : null
     * createTime : 1496111570000
     * normalQuantity : 1
     * discountQuantity : 0
     * shipmentQuantity : 0
     * distributionQuantity : 0
     * receiptQuantity : 0
     * returnQuantity : 0
     * normalPrice : 3100
     * discountPrice : 0
     * costPrice : 3050
     * marketPrice : 3200
     * totalPrice : 3100
     * returnPrice : 0
     */

    private String id;
    private String orderId;
    private String userMark;
    private String itemId;
    private String itemTemplateId;
    private String itemName;
    private String itemTypeId;
    private String itemTypeName;
    private String itemUnitId;
    private String itemUnitName;
    private String itemBarcode;
    private String itemCode;

    private String itemBatches;
    private long createTime;
    private long unReceiptQuantity;
    private int normalQuantity;
    private int discountQuantity;
    private int shipmentQuantity;
    private int distributionQuantity;
    private int receiptQuantity;
    private int returnQuantity;
    private double normalPrice;
    private double discountPrice;
    private double costPrice;
    private double marketPrice;
    private double totalPrice;
    private double returnPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserMark() {
        return userMark;
    }

    public void setUserMark(String userMark) {
        this.userMark = userMark;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(String itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemBatches() {
        return itemBatches;
    }

    public void setItemBatches(String itemBatches) {
        this.itemBatches = itemBatches;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getNormalQuantity() {
        return normalQuantity;
    }

    public void setNormalQuantity(int normalQuantity) {
        this.normalQuantity = normalQuantity;
    }

    public int getDiscountQuantity() {
        return discountQuantity;
    }

    public void setDiscountQuantity(int discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

    public int getShipmentQuantity() {
        return shipmentQuantity;
    }

    public void setShipmentQuantity(int shipmentQuantity) {
        this.shipmentQuantity = shipmentQuantity;
    }

    public int getDistributionQuantity() {
        return distributionQuantity;
    }

    public void setDistributionQuantity(int distributionQuantity) {
        this.distributionQuantity = distributionQuantity;
    }

    public int getReceiptQuantity() {
        return receiptQuantity;
    }

    public void setReceiptQuantity(int receiptQuantity) {
        this.receiptQuantity = receiptQuantity;
    }

    public int getReturnQuantity() {
        return returnQuantity;
    }

    public void setReturnQuantity(int returnQuantity) {
        this.returnQuantity = returnQuantity;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getReturnPrice() {
        return returnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public long getUnReceiptQuantity() {
        return normalQuantity + discountQuantity - receiptQuantity;
    }
}