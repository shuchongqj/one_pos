package com.gzdb.sale.bean;

import java.util.List;

public class GroupSale {


    /**
     * activityId : 20
     * activityName : 组合折扣
     * price : 36.0
     * discount : 10.0
     * items : [{"itemId":10358,"itemName":"软祥云真龙","barcode":"6901028013789","stockPrice":10,"salesPrice":13,"discount":0},{"itemId":10359,"itemName":"黄鹤楼软三口品","barcode":"6901028186384","stockPrice":21,"salesPrice":23,"discount":0}]
     */

    private int activityId;
    private String activityName;
    private double price;
    private double discount;
    private List<ItemsBean> items;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * itemId : 10358
         * itemName : 软祥云真龙
         * barcode : 6901028013789
         * stockPrice : 10.0
         * salesPrice : 13.0
         * discount : 0.0
         */

        private int itemId;
        private String itemName;
        private String barcode;
        private double stockPrice;
        private double salesPrice;
        private double discount;

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public double getStockPrice() {
            return stockPrice;
        }

        public void setStockPrice(double stockPrice) {
            this.stockPrice = stockPrice;
        }

        public double getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }
    }
}
