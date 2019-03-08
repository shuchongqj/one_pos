package com.gzdb.supermarket.been;

public class MenuBean {
    private String name;
    private int imgge;
    private String color;

    public MenuBean(String name, int imgge, String color) {
        this.name = name;
        this.imgge = imgge;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgge() {
        return imgge;
    }

    public void setImgge(int imgge) {
        this.imgge = imgge;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
