package com.gzdb.supermarket.entity;

import java.io.Serializable;

/**
 * Created by zhumg on 8/17.
 */
public class MerchandiseBean implements Serializable {

    private String id;
    private String name;//商品名字
    private String price;//售价
    private String image;
    private String typeId;
    private String merchantId;
    private String createTime;
    private String buyCount;
    private String intro;
    private String printType;
    private String display;
    private String repertory;
    private String todayRepertory;//库存
    private String beginTime;
    private String endTime;
    private String limitToday;
    private String isDelete;
    private String menuSort;
    private String priceOnline;
    private String isonline;
    private String barcode;
    private String menuSource; // 商品来源  ：ortherMerchant其他商家，oneself自己的
    private String stockPrice;//商品进货价
    private String typeName;//商品分类名称
    private String shelfLife;//保质期
    private String productionDate;//日期
    private String unit;
    private String unitId;

    private String warnInventory;//预警库存


    public String getWarnInventory() {
        return warnInventory;
    }

    public void setWarnInventory(String warnInventory) {
        this.warnInventory = warnInventory;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPrintType() {
        return printType;
    }

    public void setPrintType(String printType) {
        this.printType = printType;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getRepertory() {
        return repertory;
    }

    public void setRepertory(String repertory) {
        this.repertory = repertory;
    }

    public String getTodayRepertory() {
        return todayRepertory;
    }

    public void setTodayRepertory(String todayRepertory) {
        this.todayRepertory = todayRepertory;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLimitToday() {
        return limitToday;
    }

    public void setLimitToday(String limitToday) {
        this.limitToday = limitToday;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(String menuSort) {
        this.menuSort = menuSort;
    }

    public String getPriceOnline() {
        return priceOnline;
    }

    public void setPriceOnline(String priceOnline) {
        this.priceOnline = priceOnline;
    }

    public String getIsonline() {
        return isonline;
    }

    public void setIsonline(String isonline) {
        this.isonline = isonline;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMenuSource() {
        return menuSource;
    }

    public void setMenuSource(String menuSource) {
        this.menuSource = menuSource;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String toString() {
        return "MerchandiseBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", typeId='" + typeId + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", buyCount='" + buyCount + '\'' +
                ", intro='" + intro + '\'' +
                ", printType='" + printType + '\'' +
                ", display='" + display + '\'' +
                ", repertory='" + repertory + '\'' +
                ", todayRepertory='" + todayRepertory + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", limitToday='" + limitToday + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", menuSort='" + menuSort + '\'' +
                ", priceOnline='" + priceOnline + '\'' +
                ", isonline='" + isonline + '\'' +
                ", barcode='" + barcode + '\'' +
                ", menuSource='" + menuSource + '\'' +
                ", stockPrice='" + stockPrice + '\'' +
                ", typeName='" + typeName + '\'' +
                ", shelfLife='" + shelfLife + '\'' +
                ", productionDate='" + productionDate + '\'' +
                ", unit='" + unit + '\'' +
                ", unitId='" + unitId + '\'' +
                '}';
    }
}
