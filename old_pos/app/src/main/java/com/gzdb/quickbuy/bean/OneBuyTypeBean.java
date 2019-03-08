package com.gzdb.quickbuy.bean;

/**
 * Created by Zxy on 2017/1/9.
 *      一键采购类型Bean
 */

public class OneBuyTypeBean{


    /**
     * costLunchBox : 0
     * createTime : 1480989942
     * id : 21781                   //系统内ID
     * merchantId : 4942
     * name : 饮料
     * sortNum : 1
     */

    private int costLunchBox;
    private int createTime;
    private int id;
    private int merchantId;
    private String name;
    private int sortNum;
    private boolean select;
    private boolean itemData;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isItemData() {
        return itemData;
    }

    public void setItemData(boolean itemData) {
        this.itemData = itemData;
    }

    public int getCostLunchBox() {
        return costLunchBox;
    }

    public void setCostLunchBox(int costLunchBox) {
        this.costLunchBox = costLunchBox;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }
}
