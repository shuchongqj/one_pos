package com.gzdb.supermarket.event;

/**
 * @author zhumg
 */
public class PrintEvent {
    //商品ID
    private long itemId;
    //商品名称
    private String name;
    //二维码
    private String code;
    //单价
    private double price;
    //会员价
    private double vipPrice;
    //总价
    private String totalPrice;
    //数量
    private String weight;
    //折扣
    private double discount;

    public PrintEvent(String name, String code, long itemId, double price, double vipPrice, String weight, String totalPrice, double discount) {
        this.itemId = itemId;
        this.name = name;
        this.code = code;
        this.price = price;
        this.vipPrice = vipPrice;
        this.weight = weight;
        this.totalPrice = totalPrice;
        this.discount = discount;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(double vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
