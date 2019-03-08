package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by nongyd on 17/6/28.
 */

public class ChargeRecordResultBean {

    /**
     * sumCount : 1
     * wxMoney : 0.02
     * cashMoney : 80.01
     * sumMoney : 80.01
     * cashSum : 1
     * transactionRecordList : [{"id":11111625,"itemName":"有肉吗等...3种商品","sequenceNumber":"90820170628113752407326189","transType":"CASH","paymentTime":"2017-06-28 11:37:53","totalPrice":80.01,"actualPrice":80.01,"count":3}]
     * zfbMoney : 0.01
     * wxSum : 2
     * zfbSum : 1
     */

    private int sumCount;
    private double wxMoney;
    private double cashMoney;
    private double sumMoney;//税后
    private int cashSum;
    private double zfbMoney;
    private double normalMoney;//总收
    private double outMoney;//税费
    private int wxSum;
    private int zfbSum;
    private int hykSum;
    private double hykMoney;
    private List<TransactionRecordListBean> transactionRecordList;

    public int getSumCount() {
        return sumCount;
    }

    public void setSumCount(int sumCount) {
        this.sumCount = sumCount;
    }

    public double getWxMoney() {
        return wxMoney;
    }

    public void setWxMoney(double wxMoney) {
        this.wxMoney = wxMoney;
    }

    public double getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(double cashMoney) {
        this.cashMoney = cashMoney;
    }

    public double getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(double sumMoney) {
        this.sumMoney = sumMoney;
    }

    public double getNormalMoney() {
        return normalMoney;
    }

    public void setNormalMoney(double normalMoney) {
        this.normalMoney = normalMoney;
    }

    public double getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(double outMoney) {
        this.outMoney = outMoney;
    }

    public int getCashSum() {
        return cashSum;
    }

    public void setCashSum(int cashSum) {
        this.cashSum = cashSum;
    }

    public double getZfbMoney() {
        return zfbMoney;
    }

    public void setZfbMoney(double zfbMoney) {
        this.zfbMoney = zfbMoney;
    }

    public int getWxSum() {
        return wxSum;
    }

    public void setWxSum(int wxSum) {
        this.wxSum = wxSum;
    }

    public int getZfbSum() {
        return zfbSum;
    }

    public void setZfbSum(int zfbSum) {
        this.zfbSum = zfbSum;
    }

    public int getHykSum() {
        return hykSum;
    }

    public void setHykSum(int hykSum) {
        this.hykSum = hykSum;
    }

    public double getHykMoney() {
        return hykMoney;
    }

    public void setHykMoney(double hykMoney) {
        this.hykMoney = hykMoney;
    }

    public List<TransactionRecordListBean> getTransactionRecordList() {
        return transactionRecordList;
    }

    public void setTransactionRecordList(List<TransactionRecordListBean> transactionRecordList) {
        this.transactionRecordList = transactionRecordList;
    }

    public static class TransactionRecordListBean {
        /**
         * id : 11111625
         * itemName : 有肉吗等...3种商品
         * sequenceNumber : 90820170628113752407326189
         * transType : CASH
         * paymentTime : 2017-06-28 11:37:53
         * totalPrice : 80.01
         * actualPrice : 80.01
         * count : 3
         */

        private String id;
        private String itemName;
        private String sequenceNumber;
        private String transType;
        private String paymentTime;
        private double totalPrice;
        private double actualPrice;
        private double ratePrice;
        private double cashPrice;
        private int count;

        public double getRatePrice() {
            return ratePrice;
        }

        public double getCashPrice() {
            return cashPrice;
        }

        public void setCashPrice(double cashPrice) {
            this.cashPrice = cashPrice;
        }

        public void setRatePrice(double ratePrice) {
            this.ratePrice = ratePrice;
        }

        public String getId() {
            return id;
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

        public String getSequenceNumber() {
            return sequenceNumber;
        }

        public void setSequenceNumber(String sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public String getTransType() {
            return transType;
        }

        public void setTransType(String transType) {
            this.transType = transType;
        }

        public String getPaymentTime() {
            return paymentTime;
        }

        public void setPaymentTime(String paymentTime) {
            this.paymentTime = paymentTime;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public double getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(double actualPrice) {
            this.actualPrice = actualPrice;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }


    }
}
