package com.gzdb.supermarket.been;

/**
 * Created by liubolin on 2017/12/4.
 */

public class UserRechargeBean {


    private long money;

    private int type;

    private long renewTime;

    private long differ;

    private int days;

    public long getDiffer() {
        return differ;
    }

    public void setDiffer(long differ) {
        this.differ = differ;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(long renewTime) {
        this.renewTime = renewTime;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
