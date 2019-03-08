package com.gzdb.quickbuy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zxy on 2016/12/14.
 * 详情
 */

public class OrderDetailBeanS implements Serializable{


    /**
     * providerMerchantName : 测试九洲供应商2
     * orderId : 5769
     * receiverName : 地址都没快了
     * receriverAddress : 广东省广州市天河区员村南街59号
     * discount : 0
     * orderStatus : 0
     * userName : 飘仔连锁之冰室
     * supplyOrderNum : 672481615784966
     * providerContactName : 测试九洲供应商2
     * providerPhone : 15920124111
     * total : 200
     * deliveryFee : 0
     * receiverMerchantName : 飘仔连锁之冰室
     * itemsTotal : 200
     * createTime : 2016-12-13 15:53:05
     * items : [{"itemName":"乐事薯片青柠味104g","quantity":1,"notDeliveryQuantity":1,"receiptQuantity":0,"deliveryQuantity":0}]
     * receriverPhone : 15920124111
     */

    private OrderDetailBean orderDetail;

    public OrderDetailBean getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailBean orderDetail) {
        this.orderDetail = orderDetail;
    }

    public static class OrderDetailBean {
        private String providerMerchantName;
        private int orderId;
        private String receiverName;
        private String receriverAddress;
        private int discount;
        private int orderStatus;
        private String userName;
        private String supplyOrderNum;
        private String providerContactName;
        private String providerPhone;
        private String total;
        private String deliveryFee;
        private String receiverMerchantName;
        private String itemsTotal;
        private String createTime;
        private String receriverPhone;
        /**
         * itemName : 乐事薯片青柠味104g
         * quantity : 1
         * notDeliveryQuantity : 1
         * receiptQuantity : 0
         * deliveryQuantity : 0
         */

        private List<ItemsBean> items;

        public String getProviderMerchantName() {
            return providerMerchantName;
        }

        public void setProviderMerchantName(String providerMerchantName) {
            this.providerMerchantName = providerMerchantName;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getReceriverAddress() {
            return receriverAddress;
        }

        public void setReceriverAddress(String receriverAddress) {
            this.receriverAddress = receriverAddress;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSupplyOrderNum() {
            return supplyOrderNum;
        }

        public void setSupplyOrderNum(String supplyOrderNum) {
            this.supplyOrderNum = supplyOrderNum;
        }

        public String getProviderContactName() {
            return providerContactName;
        }

        public void setProviderContactName(String providerContactName) {
            this.providerContactName = providerContactName;
        }

        public String getProviderPhone() {
            return providerPhone;
        }

        public void setProviderPhone(String providerPhone) {
            this.providerPhone = providerPhone;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDeliveryFee() {
            return deliveryFee;
        }

        public void setDeliveryFee(String deliveryFee) {
            this.deliveryFee = deliveryFee;
        }

        public String getReceiverMerchantName() {
            return receiverMerchantName;
        }

        public void setReceiverMerchantName(String receiverMerchantName) {
            this.receiverMerchantName = receiverMerchantName;
        }

        public String getItemsTotal() {
            return itemsTotal;
        }

        public void setItemsTotal(String itemsTotal) {
            this.itemsTotal = itemsTotal;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getReceriverPhone() {
            return receriverPhone;
        }

        public void setReceriverPhone(String receriverPhone) {
            this.receriverPhone = receriverPhone;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean {
            private String itemName;
            private String price;
            private int quantity;
            private int notDeliveryQuantity;
            private int receiptQuantity;
            private int deliveryQuantity;

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public int getNotDeliveryQuantity() {
                return notDeliveryQuantity;
            }

            public void setNotDeliveryQuantity(int notDeliveryQuantity) {
                this.notDeliveryQuantity = notDeliveryQuantity;
            }

            public int getReceiptQuantity() {
                return receiptQuantity;
            }

            public void setReceiptQuantity(int receiptQuantity) {
                this.receiptQuantity = receiptQuantity;
            }

            public int getDeliveryQuantity() {
                return deliveryQuantity;
            }

            public void setDeliveryQuantity(int deliveryQuantity) {
                this.deliveryQuantity = deliveryQuantity;
            }
        }
    }
}
