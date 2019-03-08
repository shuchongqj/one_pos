package com.gzdb.supermarket.entity;

/**
 * Created by dianba on 2016/5/11.
 * 立即下单商品详情显示数据
 */
public class PlaceOderData implements java.io.Serializable {
    private String total;//单件商品的总价格   单价*数量
    private String menuId;//商品id
    private String salesPromotion;//是否促销   是Y  否N
    private String price;//商品单价
    private String count;//购买商品的数量
    private String name;//商品名称
    private String unit;//数量单位
    private String errMsg;//错误信息，默认成功下为“”，如库存不足、商品已下架等
    private String promotionPrice;//优惠后金额/促销价格
    private String discountMoney;//优惠金额
    private String unitId;


    public PlaceOderData() {
        super();
    }
    public PlaceOderData(String name,String price,String count,String total) {
        super();
        this.name = name;
        this.price = price;
        this.count = count;
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSalesPromotion() {
        return salesPromotion;
    }

    public void setSalesPromotion(String salesPromotion) {
        this.salesPromotion = salesPromotion;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(String promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getDiscountMoney() {
        return discountMoney;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public void setDiscountMoney(String discountMoney) {
        this.discountMoney = discountMoney;
    }
}
