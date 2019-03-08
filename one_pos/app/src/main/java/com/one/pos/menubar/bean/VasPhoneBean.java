package com.one.pos.menubar.bean;

import java.util.List;

/**
 * Author: even
 * Date:   2019/3/6
 * Description:
 */
public class VasPhoneBean {

    /**
     * phoneInfo : {"areaCode":"020","printType":3,"id":1881689,"city":"广州市","province":"广东","company":"移动188卡","zip":"510000","code":440000}
     * menuList : [{"isShelve":"Y","passportId":-1,"salesPrice":30,"stockPrice":29.82,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"30元","id":1441520,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:25","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0},{"isShelve":"Y","passportId":-1,"salesPrice":50,"stockPrice":49.7,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"50元","id":1441521,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:25","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0},{"isShelve":"Y","passportId":-1,"salesPrice":100,"stockPrice":99.4,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"100元","id":1441522,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:26","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0},{"isShelve":"Y","passportId":-1,"salesPrice":200,"stockPrice":198.8,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"200元","id":1441523,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:26","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0},{"isShelve":"Y","passportId":-1,"salesPrice":300,"stockPrice":298.2,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"300元","id":1441524,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:27","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0},{"isShelve":"Y","passportId":-1,"salesPrice":500,"stockPrice":497,"barcode":"694160714116","isDelete":"N","repertory":999,"itemTypeId":102764,"buyCount":0,"itemName":"500元","id":1441525,"posTypeName":"增值服务","createDate":"2017-06-21 23:50:27","itemImg":"","itemTemplateId":1331643,"itemUnitName":"件","itemUnitId":1,"shelfLife":0,"description":"","posTypeId":102205,"generatedDate":0,"warningRepertory":0}]
     */

    private PhoneInfoBean phoneInfo;
    private List<MenuListBean> menuList;

    public PhoneInfoBean getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(PhoneInfoBean phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public List<MenuListBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuListBean> menuList) {
        this.menuList = menuList;
    }

    public static class PhoneInfoBean {
        /**
         * areaCode : 020
         * printType : 3
         * id : 1881689
         * city : 广州市
         * province : 广东
         * company : 移动188卡
         * zip : 510000
         * code : 440000
         */

        private String areaCode;
        private int printType;
        private int id;
        private String city;
        private String province;
        private String company;
        private String zip;
        private int code;

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public int getPrintType() {
            return printType;
        }

        public void setPrintType(int printType) {
            this.printType = printType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public static class MenuListBean {
        /**
         * isShelve : Y
         * passportId : -1
         * salesPrice : 30.0
         * stockPrice : 29.82
         * barcode : 694160714116
         * isDelete : N
         * repertory : 999
         * itemTypeId : 102764
         * buyCount : 0
         * itemName : 30元
         * id : 1441520
         * posTypeName : 增值服务
         * createDate : 2017-06-21 23:50:25
         * itemImg :
         * itemTemplateId : 1331643
         * itemUnitName : 件
         * itemUnitId : 1
         * shelfLife : 0
         * description :
         * posTypeId : 102205
         * generatedDate : 0
         * warningRepertory : 0
         */

        private String isShelve;
        private int passportId;
        private double salesPrice;
        private double stockPrice;
        private String barcode;
        private String isDelete;
        private int repertory;
        private int itemTypeId;
        private int buyCount;
        private String itemName;
        private int id;
        private String posTypeName;
        private String createDate;
        private String itemImg;
        private int itemTemplateId;
        private String itemUnitName;
        private int itemUnitId;
        private int shelfLife;
        private String description;
        private int posTypeId;
        private int generatedDate;
        private int warningRepertory;

        public String getIsShelve() {
            return isShelve;
        }

        public void setIsShelve(String isShelve) {
            this.isShelve = isShelve;
        }

        public int getPassportId() {
            return passportId;
        }

        public void setPassportId(int passportId) {
            this.passportId = passportId;
        }

        public double getSalesPrice() {
            return salesPrice;
        }

        public void setSalesPrice(double salesPrice) {
            this.salesPrice = salesPrice;
        }

        public double getStockPrice() {
            return stockPrice;
        }

        public void setStockPrice(double stockPrice) {
            this.stockPrice = stockPrice;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
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

        public int getItemTypeId() {
            return itemTypeId;
        }

        public void setItemTypeId(int itemTypeId) {
            this.itemTypeId = itemTypeId;
        }

        public int getBuyCount() {
            return buyCount;
        }

        public void setBuyCount(int buyCount) {
            this.buyCount = buyCount;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPosTypeName() {
            return posTypeName;
        }

        public void setPosTypeName(String posTypeName) {
            this.posTypeName = posTypeName;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getItemImg() {
            return itemImg;
        }

        public void setItemImg(String itemImg) {
            this.itemImg = itemImg;
        }

        public int getItemTemplateId() {
            return itemTemplateId;
        }

        public void setItemTemplateId(int itemTemplateId) {
            this.itemTemplateId = itemTemplateId;
        }

        public String getItemUnitName() {
            return itemUnitName;
        }

        public void setItemUnitName(String itemUnitName) {
            this.itemUnitName = itemUnitName;
        }

        public int getItemUnitId() {
            return itemUnitId;
        }

        public void setItemUnitId(int itemUnitId) {
            this.itemUnitId = itemUnitId;
        }

        public int getShelfLife() {
            return shelfLife;
        }

        public void setShelfLife(int shelfLife) {
            this.shelfLife = shelfLife;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getPosTypeId() {
            return posTypeId;
        }

        public void setPosTypeId(int posTypeId) {
            this.posTypeId = posTypeId;
        }

        public int getGeneratedDate() {
            return generatedDate;
        }

        public void setGeneratedDate(int generatedDate) {
            this.generatedDate = generatedDate;
        }

        public int getWarningRepertory() {
            return warningRepertory;
        }

        public void setWarningRepertory(int warningRepertory) {
            this.warningRepertory = warningRepertory;
        }
    }
}
