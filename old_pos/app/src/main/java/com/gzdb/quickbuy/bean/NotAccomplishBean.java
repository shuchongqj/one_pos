package com.gzdb.quickbuy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zxy on 2016/12/13.
 * 订单列表
 */

public class NotAccomplishBean implements Serializable{

    /**
     * createTime : 2016-12-12 20:00:56
     * items : [{"deliveryQuantity":0,"itemName":"乐事薯片青柠味104g","notDeliveryQuantity":10,"quantity":10,"receiptQuantity":0}]
     * orderId : 5730
     * orderStatus : 0
     * providerName : 测试九洲供应商2
     * providerPhone : 15920124566
     * supplyOrderNum : 433781544256381
     * total : 2000
     * totalDeliveryQuantity : 0
     * totalNotDeliveryQuantity : 10
     * totalReceiptQuantity : 0
     * userName : 飘仔连锁之冰室
     */

    private String createTime;
    private int orderId;
    private int orderStatus;
    private String providerName;
    private String providerPhone;
    private String supplyOrderNum;
    private String total;
    private int totalDeliveryQuantity;
    private int totalNotDeliveryQuantity;
    private int totalReceiptQuantity;
    private String userName;
    /**
     * deliveryQuantity : 0
     * itemName : 乐事薯片青柠味104g
     * notDeliveryQuantity : 10
     * quantity : 10
     * receiptQuantity : 0
     */

    private List<ItemsBean> items;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderPhone() {
        return providerPhone;
    }

    public void setProviderPhone(String providerPhone) {
        this.providerPhone = providerPhone;
    }

    public String getSupplyOrderNum() {
        return supplyOrderNum;
    }

    public void setSupplyOrderNum(String supplyOrderNum) {
        this.supplyOrderNum = supplyOrderNum;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getTotalDeliveryQuantity() {
        return totalDeliveryQuantity;
    }

    public void setTotalDeliveryQuantity(int totalDeliveryQuantity) {
        this.totalDeliveryQuantity = totalDeliveryQuantity;
    }

    public int getTotalNotDeliveryQuantity() {
        return totalNotDeliveryQuantity;
    }

    public void setTotalNotDeliveryQuantity(int totalNotDeliveryQuantity) {
        this.totalNotDeliveryQuantity = totalNotDeliveryQuantity;
    }

    public int getTotalReceiptQuantity() {
        return totalReceiptQuantity;
    }

    public void setTotalReceiptQuantity(int totalReceiptQuantity) {
        this.totalReceiptQuantity = totalReceiptQuantity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        private int deliveryQuantity;
        private String itemName;
        private int notDeliveryQuantity;
        private int quantity;
        private int receiptQuantity;

        public int getDeliveryQuantity() {
            return deliveryQuantity;
        }

        public void setDeliveryQuantity(int deliveryQuantity) {
            this.deliveryQuantity = deliveryQuantity;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getNotDeliveryQuantity() {
            return notDeliveryQuantity;
        }

        public void setNotDeliveryQuantity(int notDeliveryQuantity) {
            this.notDeliveryQuantity = notDeliveryQuantity;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getReceiptQuantity() {
            return receiptQuantity;
        }

        public void setReceiptQuantity(int receiptQuantity) {
            this.receiptQuantity = receiptQuantity;
        }
    }
}
