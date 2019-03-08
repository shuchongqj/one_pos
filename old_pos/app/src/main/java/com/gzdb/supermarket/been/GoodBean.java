package com.gzdb.supermarket.been;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

/**
 * Created by nongyd on 17/6/1.
 */

@Entity
public  class GoodBean implements Serializable {

    protected static final long serialVersionUID = -7132099477437693580L;

    /**
     * id : 10000
     * itemName : 鹰金钱豆豉鲮鱼227g
     * barcode : 6901012021967
     * posTypeId :
     * itemTemplateId :
     * item_img : http://oss.0085.com/2017/0106/supplychaint/14836894550829.jpeg
     * createDate : 111111
     * buyCount : 0
     * description :
     * isShelve : Y
     * isDelete : N
     * repertory : 0
     * warningRepertory : 0
     * shelfLife : 0
     * stockPrice : 0
     * salesPrice : 0
     */
    @Id(autoincrement = true)
    protected Long autoId;

    protected int sortId;
    @Unique
    protected String id;
    protected String itemName;
    protected int itemType;
    protected String barcode;
    protected String itemTypeId;
    protected String posTypeId;
    protected String posTypeName;
    protected String itemUnitId;
    protected String itemUnitName;
    protected String itemTemplateId;
    protected String itemImg;
    protected String createDate;
    protected int buyCount;
    protected String description;
    protected String isShelve;//是否上下架
    protected String isDelete;
    protected int repertory;
    protected int warningRepertory;
    protected int shelfLife;//保质期
    protected double stockPrice;
    protected double salesPrice;//?
    protected long generatedDate;//生产日期
    protected int activityType;
    protected double price;//?
    protected double discount;
    protected int activityId;
    protected  int state;
    //促销价格，旧版
    protected double promotionPrice;
    //会员价
    protected double membershipPrice;
    protected  String  sellType;//销售类型



    @Generated(hash = 929232382)
    public GoodBean(Long autoId, int sortId, String id, String itemName,
            int itemType, String barcode, String itemTypeId, String posTypeId,
            String posTypeName, String itemUnitId, String itemUnitName,
            String itemTemplateId, String itemImg, String createDate, int buyCount,
            String description, String isShelve, String isDelete, int repertory,
            int warningRepertory, int shelfLife, double stockPrice,
            double salesPrice, long generatedDate, int activityType, double price,
            double discount, int activityId, int state, double promotionPrice,
            double membershipPrice, String sellType) {
        this.autoId = autoId;
        this.sortId = sortId;
        this.id = id;
        this.itemName = itemName;
        this.itemType = itemType;
        this.barcode = barcode;
        this.itemTypeId = itemTypeId;
        this.posTypeId = posTypeId;
        this.posTypeName = posTypeName;
        this.itemUnitId = itemUnitId;
        this.itemUnitName = itemUnitName;
        this.itemTemplateId = itemTemplateId;
        this.itemImg = itemImg;
        this.createDate = createDate;
        this.buyCount = buyCount;
        this.description = description;
        this.isShelve = isShelve;
        this.isDelete = isDelete;
        this.repertory = repertory;
        this.warningRepertory = warningRepertory;
        this.shelfLife = shelfLife;
        this.stockPrice = stockPrice;
        this.salesPrice = salesPrice;
        this.generatedDate = generatedDate;
        this.activityType = activityType;
        this.price = price;
        this.discount = discount;
        this.activityId = activityId;
        this.state = state;
        this.promotionPrice = promotionPrice;
        this.membershipPrice = membershipPrice;
        this.sellType = sellType;
    }



    @Generated(hash = 1348485518)
    public GoodBean() {
    }



    public long getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(long generatedDate) {
        this.generatedDate = generatedDate;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public double getStockPrice() {
        return stockPrice;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getId() {
        return id;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPosTypeId() {
        return posTypeId;
    }

    public void setPosTypeId(String posTypeId) {
        this.posTypeId = posTypeId;
    }

    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItem_img(String item_img) {
        this.itemImg = item_img;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsShelve() {
        return isShelve;
    }

    public void setIsShelve(String isShelve) {
        this.isShelve = isShelve;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public int getRepertory() {
        return repertory;
    }

    public void setRepertory(int repertory) {
        this.repertory = repertory;
    }

    public int getWarningRepertory() {
        return warningRepertory;
    }

    public void setWarningRepertory(int warningRepertory) {
        this.warningRepertory = warningRepertory;
    }

    public int getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(int shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getPosTypeName() {
        return posTypeName;
    }

    public void setPosTypeName(String posTypeName) {
        this.posTypeName = posTypeName;
    }

    public String getItemUnitId() {
        return itemUnitId;
    }

    public void setItemUnitId(String itemUnitId) {
        this.itemUnitId = itemUnitId;
    }

    public String getItemUnitName() {
        return itemUnitName;
    }

    public void setItemUnitName(String itemUnitName) {
        this.itemUnitName = itemUnitName;
    }

    public Long getAutoId() {
        return this.autoId;
    }

    public void setAutoId(Long autoId) {
        this.autoId = autoId;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemTypeId() {
        return this.itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
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

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getMembershipPrice() {
        return membershipPrice;
    }

    public void setMembershipPrice(double membershipPrice) {
        this.membershipPrice = membershipPrice;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }
}