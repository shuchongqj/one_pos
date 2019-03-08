package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by nongyd on 17/6/6.
 */

public class SettlementResultBean {

    /**
     * totalAmount : 36.61
     * datas : [{"id":1,"passportId":100045,"merchantPassportId":100045,"paymentType":"CASH","amount":36.57,"count":27,"isPaid":"0","createTime":1496744418000,"paymentTypeTitle":"现金"},{"id":2,"passportId":100045,"merchantPassportId":100045,"paymentType":"WEIXIN_NATIVE","amount":0.04,"count":1,"isPaid":"0","createTime":1496744418000,"paymentTypeTitle":"微信"}]
     */

    private double totalAmount;//合计金额
    private List<DatasBean> datas;
    private int totalNum;
    /**
     * totalAmount : 36.61
     * isDirectStore : 0
     * datas : [{"id":1,"passportId":100045,"merchantPassportId":100045,"paymentType":"CASH","amount":36.57,"count":27,"isPaid":0,"createTime":1496744418000,"cashAmount":0,"paymentTypeTitle":"现金"},{"id":2,"passportId":100045,"merchantPassportId":100045,"paymentType":"WEIXIN_NATIVE","amount":0.04,"count":1,"isPaid":0,"createTime":1496744418000,"cashAmount":0,"paymentTypeTitle":"微信"}]
     * cashAmount : 0
     */

    private int isDirectStore;
    private double cashAmount;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public int getIsDirectStore() {
        return isDirectStore;
    }

    public void setIsDirectStore(int isDirectStore) {
        this.isDirectStore = isDirectStore;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public static class DatasBean {
        /**
         * id : 1
         * passportId : 100045
         * merchantPassportId : 100045
         * paymentType : CASH
         * amount : 36.57
         * count : 27
         * isPaid : 0
         * createTime : 1496744418000
         * paymentTypeTitle : 现金
         */

        private String id;
        private String passportId;
        private String merchantPassportId;
        private String paymentType;
        private double amount;
        private int count;
        private String isPaid;
        private long createTime;
        private String paymentTypeTitle;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassportId() {
            return passportId;
        }

        public void setPassportId(String passportId) {
            this.passportId = passportId;
        }

        public String getMerchantPassportId() {
            return merchantPassportId;
        }

        public void setMerchantPassportId(String merchantPassportId) {
            this.merchantPassportId = merchantPassportId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(String isPaid) {
            this.isPaid = isPaid;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPaymentTypeTitle() {
            return paymentTypeTitle;
        }

        public void setPaymentTypeTitle(String paymentTypeTitle) {
            this.paymentTypeTitle = paymentTypeTitle;
        }
    }
}
