package com.gzdb.supermarket.entity;

import java.util.List;

/**
 * Created by Even on 2016/5/26.
 */
public class Settlement implements java.io.Serializable {
    private float totalMoney; //现金
    private int orderId;
    private String orderType;
    private String description;
    private String merchantName;//商家名称
    private String cashierName;//收银员姓名
    private String beginTime;// 营业开始时间
    private String endTime;//营业结束时间
    private String totalQuantity;// 订单总数量
    private String totalIncome;//订单总金额
    private String state;//‘Y' 表示需要输入现金 ‘N’表示不需要输入现金
    private String cash;//收银员首次输入的清点现金
    private String typeName;
    private String orderTypeName;



    private List<SettlementOrder> orders;
    private List<SettlementOrder> basicPosOrders;

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public List<SettlementOrder> getBasicPosOrders() {
        return basicPosOrders;
    }

    public void setBasicPosOrders(List<SettlementOrder> basicPosOrders) {
        this.basicPosOrders = basicPosOrders;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public List<SettlementOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<SettlementOrder> orders) {
        this.orders = orders;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
