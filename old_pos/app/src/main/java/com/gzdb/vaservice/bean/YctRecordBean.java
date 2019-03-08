package com.gzdb.vaservice.bean;

public class YctRecordBean {

    /**
     * id : 2
     * orderNumber : 166972816239982
     * cardNumber : 54647464
     * amount : 20.0
     * status : 1
     * createTime : 1525512195000
     * count : 4
     * todayAmount : 60.0
     */

    private int id;
    private String orderNumber;
    private String cardNumber;
    private String amount;
    private int status;
    private long createTime;
    private int count;
    private String todayAmount;
    private String todayflyCharge;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTodayflyCharge() {
        return todayflyCharge;
    }

    public void setTodayflyCharge(String todayflyCharge) {
        this.todayflyCharge = todayflyCharge;
    }

    public String getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(String todayAmount) {
        this.todayAmount = todayAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
