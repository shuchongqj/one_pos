package com.gzdb.supermarket.entity;

import java.util.List;

/**
 * Created by dianba on 2016/5/11.
 */
public class PlaceOrderDataList {
    private int orderId;//订单id
    private String payId;//支付id
    private String totalPrice;//总价格
    private String totalCount;//总数量
    private String totalMoney;//当日结算金额
    private String totalDiscount;//优惠总金额
    private String totalOrigin;//总金额

    private List<PlaceOderData> menuList;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public List<PlaceOderData> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<PlaceOderData> menuList) {
        this.menuList = menuList;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTotalDiscount() {
        if(totalDiscount == null) {
            return "";
        }
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getTotalOrigin() {
        return totalOrigin;
    }

    public void setTotalOrigin(String totalOrigin) {
        this.totalOrigin = totalOrigin;
    }
}
