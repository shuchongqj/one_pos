package com.gzdb.sale.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SaleType3 {
    @Id
    private Long id;
    private int activityId;
    private double price;
    private double discount;

    @Generated(hash = 1056314088)
    public SaleType3(Long id, int activityId, double price, double discount) {
        this.id = id;
        this.activityId = activityId;
        this.price = price;
        this.discount = discount;
    }

    @Generated(hash = 357576110)
    public SaleType3() {
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
