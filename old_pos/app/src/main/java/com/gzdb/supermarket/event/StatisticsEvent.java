package com.gzdb.supermarket.event;

public class StatisticsEvent {
    private String orderId;
    private double actualPrice;
    private double totalPrice;
    private double totaldiscountPrice;
    private double discountPercent;

    public StatisticsEvent(String orderId, double actualPrice, double totalPrice, double totaldiscountPrice, double discountPercent) {
        this.orderId = orderId;
        this.actualPrice = actualPrice;
        this.totalPrice = totalPrice;
        this.totaldiscountPrice = totaldiscountPrice;
        this.discountPercent = discountPercent;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotaldiscountPrice() {
        return totaldiscountPrice;
    }

    public void setTotaldiscountPrice(double totaldiscountPrice) {
        this.totaldiscountPrice = totaldiscountPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }
}
