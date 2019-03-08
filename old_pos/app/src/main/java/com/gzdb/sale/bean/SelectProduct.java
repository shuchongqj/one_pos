package com.gzdb.sale.bean;

import com.gzdb.sale.BaseBean;

public class SelectProduct extends BaseBean {


    /**
     * itemId : 10348
     * itemName : 其他
     * barcode : BBBBBBBBBBBBBB
     * stockPrice : 500.0
     * salesPrice : 500.0
     * checked : 0
     * total : 375
     */

    private int itemId;
    private String itemName;
    private String barcode;
    private String stockPrice;
    private String salesPrice;
    private int checked;
    private int total;

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

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
