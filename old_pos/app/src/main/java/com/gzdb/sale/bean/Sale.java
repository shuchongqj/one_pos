package com.gzdb.sale.bean;

public class Sale {

    /**
     * id : 1
     * activityName : 组合购买优惠
     * startTime : 1520927781000
     * endTime : 1522482986000
     * activityType : 1
     */

    private int id;
    private String activityName;
    private long startTime;
    private long endTime;
    private int activityType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
}
