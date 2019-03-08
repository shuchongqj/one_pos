package com.gzdb.supermarket.been;

/**
 * Created by liubolin on 2017/12/28.
 */

public class RechargeLogBean {

    /*
    *
renewDays: 30,
payMoney: 1,
payType: "-1",
showName: "小屁孩",
createTime: 1512698642000,
newTime: 1527609600000,
defaultName: "db_20170726112248678"
    * */

    private int renewDays;

    private String sequenceNumber;

    private long payMoney;

    private String payType;

    private String showName;

    private long createTime;

    private long newTime;

    private String defaultName;

    public int getRenewDays() {
        return renewDays;
    }

    public void setRenewDays(int renewDays) {
        this.renewDays = renewDays;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public long getNewTime() {
        return newTime;
    }

    public void setNewTime(long newTime) {
        this.newTime = newTime;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }
}

