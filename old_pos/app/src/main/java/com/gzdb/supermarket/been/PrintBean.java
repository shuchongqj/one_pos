package com.gzdb.supermarket.been;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PrintBean   {



    public String daySortNumber;
    public String orderSequenceNumber;//订单号   （配送系统送货单号）
    public String createTime;//下单时间
    public String showName;//商家名称
    public String receiptNickName;//收货人姓名
    public String receiptPhone;//电话
    public String distributionAddress;//地址
    public String remarks;//备注
    public String qrCode;//备注
    public long totalPrice;
    private List<DetailBean> detail;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public List<DetailBean> getDetail() {
        return detail;
    }

    public void setDetail(List<DetailBean> detail) {
        this.detail = detail;
    }

    public String getDaySortNumber() {
        return daySortNumber;
    }

    public void setDaySortNumber(String daySortNumber) {
        this.daySortNumber = daySortNumber;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getDistributionAddress() {
        return distributionAddress;
    }

    public void setDistributionAddress(String distributionAddress) {
        this.distributionAddress = distributionAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }



    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
