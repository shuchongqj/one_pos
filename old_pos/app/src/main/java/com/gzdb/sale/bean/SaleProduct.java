package com.gzdb.sale.bean;

import com.gzdb.sale.BaseBean;

public class SaleProduct extends BaseBean {


    /**
     * itemId : 10293
     * itemName : 500M
     * barcode : 856510124567
     * stockPrice : 2805
     * salesPrice : 3000
     * discount : 10
     */

    private int itemId;
    private String itemName;
    private String barcode;
    private String stockPrice;
    private String salesPrice;
    private String discount;

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

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
