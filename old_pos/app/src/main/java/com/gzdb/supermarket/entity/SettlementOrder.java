package com.gzdb.supermarket.entity;

/**
 * Created by Even on 2016/5/26.
 */
public class SettlementOrder implements java.io.Serializable {
    private  String orderType;//订单类型 包括现金订单、微信扫码、支付宝扫码、外卖订单、门店订单
    private  String quantity;//订单数量
    private  String income;//订单金额
    //类型
    private int soType = 0;

    public int getSoType() {
        return soType;
    }

    public void setSoType(int soType) {
        this.soType = soType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "SettlementOrder{" +
                "orderType='" + orderType + '\'' +
                ", quantity='" + quantity + '\'' +
                ", income='" + income + '\'' +
                '}';
    }
}
