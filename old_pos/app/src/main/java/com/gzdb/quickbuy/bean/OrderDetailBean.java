package com.gzdb.quickbuy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nongyudi on 2017/6/4.
 */

public  class OrderDetailBean implements Serializable {
    /**
     * id : 100497
     * sequenceNumber : 90820170530103241367367897
     * orderSequenceNumber : 390820170530103250690712368
     * partnerId : xlb908100000
     * partnerUserId : 100013
     * type : 3
     * status : 8
     * deliverStatus : 0
     * refundStatus : 0
     * paymentType : -1
     * transType : BALANCE
     * userSource : 1
     * daySortNumber : 1
     * pushType : 2
     * shippingPassportId : 100008
     * shippingNickName : 1号生活商家专卖
     * shippingProvince : 广东省
     * shippingCity : 韶关市
     * shippingDistrict : 武江区
     * shippingAddress : 南华寺
     * shippingLocation : 116.401107,39.916706
     * shippingPhone : 13112333214
     * receiptUserId : 100013
     * receiptNickName : qeqeqeqe
     * receiptProvince : California
     * receiptCity : San Francisco
     * receiptDistrict : 001
     * receiptAddress : 100 Bush Street # 510,San FranciscoFowler
     * receiptPhone : 13878135701
     * receiptLocation : 0.0,0.0
     * courierPassportId : 0
     * courierNickName :
     * courierPhone :
     * totalDistance : 0
     * currentLocation : 0.0,0.0
     * collectingFees : 0
     * remark :
     * actualPrice : 3100
     * totalPrice : 3100
     * discountPrice : 0
     * distributionFee : 0
     * priceLogger : 无优惠
     * cancelLogger :
     * createTime : 1496111570000
     * paymentTime : 1496111575000
     * confirmTime : 0
     * body : null
     * detail : null
     * itemSnapshots : [{"id":100497,"orderId":100497,"userMark":"100013","itemId":100011,"itemTemplateId":100000,"itemName":"农夫山泉4L*6","itemTypeId":90018,"itemTypeName":"瓶装水","itemUnitId":100004,"itemUnitName":"件","itemBarcode":"6921168559173","itemCode":"G100000008","itemBatches":0,"introductionPage":null,"createTime":1496111570000,"normalQuantity":1,"discountQuantity":0,"shipmentQuantity":0,"distributionQuantity":0,"receiptQuantity":0,"returnQuantity":0,"normalPrice":3100,"discountPrice":0,"costPrice":3050,"marketPrice":3200,"totalPrice":3100,"returnPrice":0}]
     * stateLoggers : null
     */

    private String id;
    private String sequenceNumber;
    private String orderSequenceNumber;
    private String partnerId;
    private String partnerUserId;
    private String type;
    private int status;
    private String deliverStatus;
    private String refundStatus;
    private String paymentType;
    private String transType;
    private String userSource;
    private String daySortNumber;
    private String pushType;
    private String shippingPassportId;
    private String shippingNickName;
    private String shippingProvince;
    private String shippingCity;
    private String shippingDistrict;
    private String shippingAddress;
    private String shippingLocation;
    private String shippingPhone;
    private String receiptUserId;
    private String receiptNickName;
    private String receiptProvince;
    private String receiptCity;
    private String receiptDistrict;
    private String receiptAddress;
    private String receiptPhone;
    private String receiptLocation;
    private int courierPassportId;
    private String courierNickName;
    private String courierPhone;
    private String totalDistance;
    private String currentLocation;
    private String collectingFees;
    private String remark;
    private double actualPrice;
    private double totalPrice;
    private double discountPrice;
    private double distributionFee;
    private String priceLogger;
    private String cancelLogger;
    private long createTime;
    private long paymentTime;
    private long confirmTime;
    private List<OrderDetailItemBean> itemSnapshots;

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

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public void setPartnerUserId(String partnerUserId) {
        this.partnerUserId = partnerUserId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getUserSource() {
        return userSource;
    }

    public void setUserSource(String userSource) {
        this.userSource = userSource;
    }

    public String getDaySortNumber() {
        return daySortNumber;
    }

    public void setDaySortNumber(String daySortNumber) {
        this.daySortNumber = daySortNumber;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getShippingPassportId() {
        return shippingPassportId;
    }

    public void setShippingPassportId(String shippingPassportId) {
        this.shippingPassportId = shippingPassportId;
    }

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getShippingProvince() {
        return shippingProvince;
    }

    public void setShippingProvince(String shippingProvince) {
        this.shippingProvince = shippingProvince;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingDistrict() {
        return shippingDistrict;
    }

    public void setShippingDistrict(String shippingDistrict) {
        this.shippingDistrict = shippingDistrict;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingLocation() {
        return shippingLocation;
    }

    public void setShippingLocation(String shippingLocation) {
        this.shippingLocation = shippingLocation;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getReceiptUserId() {
        return receiptUserId;
    }

    public void setReceiptUserId(String receiptUserId) {
        this.receiptUserId = receiptUserId;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
    }

    public String getReceiptProvince() {
        return receiptProvince;
    }

    public void setReceiptProvince(String receiptProvince) {
        this.receiptProvince = receiptProvince;
    }

    public String getReceiptCity() {
        return receiptCity;
    }

    public void setReceiptCity(String receiptCity) {
        this.receiptCity = receiptCity;
    }

    public String getReceiptDistrict() {
        return receiptDistrict;
    }

    public void setReceiptDistrict(String receiptDistrict) {
        this.receiptDistrict = receiptDistrict;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getReceiptLocation() {
        return receiptLocation;
    }

    public void setReceiptLocation(String receiptLocation) {
        this.receiptLocation = receiptLocation;
    }

    public int getCourierPassportId() {
        return courierPassportId;
    }

    public void setCourierPassportId(int courierPassportId) {
        this.courierPassportId = courierPassportId;
    }

    public String getCourierNickName() {
        return courierNickName;
    }

    public void setCourierNickName(String courierNickName) {
        this.courierNickName = courierNickName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCollectingFees() {
        return collectingFees;
    }

    public void setCollectingFees(String collectingFees) {
        this.collectingFees = collectingFees;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getDistributionFee() {
        return distributionFee;
    }

    public void setDistributionFee(double distributionFee) {
        this.distributionFee = distributionFee;
    }

    public String getPriceLogger() {
        return priceLogger;
    }

    public void setPriceLogger(String priceLogger) {
        this.priceLogger = priceLogger;
    }

    public String getCancelLogger() {
        return cancelLogger;
    }

    public void setCancelLogger(String cancelLogger) {
        this.cancelLogger = cancelLogger;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public long getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(long confirmTime) {
        this.confirmTime = confirmTime;
    }

    public List<OrderDetailItemBean> getItemSnapshots() {
        return itemSnapshots;
    }

    public void setItemSnapshots(List<OrderDetailItemBean> itemSnapshots) {
        this.itemSnapshots = itemSnapshots;
    }


}