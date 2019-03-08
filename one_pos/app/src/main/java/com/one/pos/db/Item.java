package com.one.pos.db;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author zhumg
 */
@Entity
public class Item {

    private long id;
    private long templateId;
    private String code;
    private String name;
    private String vipPrice;
    private String price;
    private String storage;
    private int stock;

    //外键标志
    //@ForeignKey(saveForeignKeyModel = false)

    public Item() {
    }

    public Item(String name, int stock, String vipPrice, String price, String storage) {
        this.stock = stock;
        this.price = price;
        this.name = name;
        this.stock = stock;
        this.vipPrice = vipPrice;
        this.storage = storage;
        this.templateId = 1;
    }

    @Generated(hash = 1564227635)
    public Item(long id, long templateId, String code, String name, String vipPrice,
            String price, String storage, int stock) {
        this.id = id;
        this.templateId = templateId;
        this.code = code;
        this.name = name;
        this.vipPrice = vipPrice;
        this.price = price;
        this.storage = storage;
        this.stock = stock;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }
}
