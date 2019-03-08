package com.gzdb.quickbuy.bean;

/**
 * Created by nongyd on 17/6/3.
 */

public class CreateOrderResult {


    /**
     * priceLogger : 无优惠
     * actualPrice : 16200
     * totalPrice : 16200
     * discountPrice : 0
     * distributionFee : 0
     * refundStatus : 0
     * receiptPhone : 18824906653
     * receiptUserId : 100005
     * type : 3
     * deliverStatus : 0
     * paymentType : -1
     * shippingNickName : 广州天河仓
     * itemSnapshots : [{"itemUnitName":"罐","marketPrice":2800,"userMark":"100005","orderId":100825,"totalPrice":16200,"itemBarcode":"6901012021967","itemCode":"G100000562","costPrice":2050,"returnPrice":0,"itemTemplateId":100003,"itemId":100007,"itemBatches":0,"itemName":"鹰金钱豆豉鲮鱼227g","normalPrice":2700,"itemUnitId":100017,"itemTypeName":"厨房酱料","id":101088,"itemTypeId":14001,"normalQuantity":6}]
     * id : 100825
     * paymentTime : 0
     * receiptLocation : 23.14009700663712,113.36541871434336
     * sequenceNumber : 90820170603165935332658072
     * shippingPhone : 13800138000
     * transType : UNKNOWN
     * createTime : 1496480375000
     * partnerUserId : 100005
     * shippingPassportId : 100000
     * partnerId : xlb908100000
     * cancelLogger :
     * status : 1
     */

    private String priceLogger;
    private String actualPrice;
    private String totalPrice;
    private String discountPrice;
    private String distributionFee;
    private String receiptPhone;
    private String receiptUserId;
    private String shippingNickName;
    private String id;
    private String sequenceNumber;

    public String getPriceLogger() {
        return priceLogger;
    }

    public void setPriceLogger(String priceLogger) {
        this.priceLogger = priceLogger;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDistributionFee() {
        return distributionFee;
    }

    public void setDistributionFee(String distributionFee) {
        this.distributionFee = distributionFee;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getReceiptUserId() {
        return receiptUserId;
    }

    public void setReceiptUserId(String receiptUserId) {
        this.receiptUserId = receiptUserId;
    }

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
