package com.gzdb.supermarket.been;

public class Group {

    private int type;
    private String id;
    private int num;
    private double price;

    public Group() {
    }

    public Group(int type, String id, int num, double price) {
        this.type = type;
        this.id = id;
        this.num = num;
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
