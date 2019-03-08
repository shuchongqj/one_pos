package com.gzdb.quickbuy.event;

public class QuickBuyEvent {

    private int num;
    private double money;

    public QuickBuyEvent(int num, double money) {
        this.num = num;
        this.money = money;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
