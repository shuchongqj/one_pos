package com.gzdb.sale.bean;

import java.util.List;

public class SaleInfo {

    /**
     * activityId : 3
     * activityName : 蝇营狗苟
     * startTime : 1522315200000
     * endTime : 1524993600000
     * price : 100
     * discount : 10
     * activityType : 3
     * items : [{"itemId":10293,"itemName":"500M","barcode":"856510124567","stockPrice":2805,"salesPrice":3000,"discount":10},{"itemId":10292,"itemName":"200M","barcode":"856510124567","stockPrice":1402,"salesPrice":1500,"discount":10}]
     */

    private int activityId;
    private String activityName;
    private long startTime;
    private long endTime;
    private String price;
    private String discount;
    private int activityType;
    private List<SaleProduct> items;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public List<SaleProduct> getItems() {
        return items;
    }

    public void setItems(List<SaleProduct> items) {
        this.items = items;
    }
}
