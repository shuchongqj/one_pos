package com.gzdb.vaservice.bean;

public class YctRecordDetail {


    /**
     * id : 2
     * orderId : null
     * orderStatus : 1
     * productName : 羊城通
     * cardNumber : 54647464
     * createTimeString : null
     * amount : 20.0
     * merchantNumber : 111111111111
     * terminalNumber : 222222222222222
     * type : 1
     * psam : 1111111111
     * flowNumber : 1111111111111
     * voucherNo : 1111111111
     * balance : 20.0
     * passportId : null
     * createTime : 1525512195000
     */

    private int id;
    private String orderId;
    private int orderStatus;
    private String productName;
    private String cardNumber;
    private String createTimeString;
    private double amount;
    private String merchantNumber;
    private String terminalNumber;
    private int type;
    private String psam;
    private String flowNumber;
    private String voucherNo;
    private double balance;
    private String passportId;
    private long createTime;
    private String prebalance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCreateTimeString() {
        return createTimeString;
    }

    public void setCreateTimeString(String createTimeString) {
        this.createTimeString = createTimeString;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPsam() {
        return psam;
    }

    public void setPsam(String psam) {
        this.psam = psam;
    }

    public String getFlowNumber() {
        return flowNumber;
    }

    public void setFlowNumber(String flowNumber) {
        this.flowNumber = flowNumber;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getPrebalance() {
        return prebalance;
    }

    public void setPrebalance(String prebalance) {
        this.prebalance = prebalance;
    }
}
