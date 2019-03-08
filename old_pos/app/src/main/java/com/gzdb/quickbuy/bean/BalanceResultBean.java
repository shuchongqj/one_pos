package com.gzdb.quickbuy.bean;

import java.util.List;

/**
 * Created by nongyd on 17/6/3.
 */

public class BalanceResultBean {

    private double surplusQuota;//可用额度
    private double nowQuota;//额度
    private List<DatasBean> datas;
    /**
     * accountPeriodDays : 15
     * nowQuota : 3000
     * cardName : 1号信用付
     * datas : [{"id":"34","name":"余额","type":"1","channelId":"100000","currentAmount":564.32,"freezeAmount":0},{"id":"40","name":"会员余额","type":"2","channelId":"100000","currentAmount":9.999954535E7,"freezeAmount":0}]
     * surplusQuota : 3000
     * busType : A
     * items : [{"fees":0.02,"rate":"1.2%","name":"技术费"},{"fees":-0.04,"rate":"-2.5%","name":"服务费"}]
     * content : 1号信用付为货到付款操作，享有专属支付优惠，但货品送到时需在1号生活商家端进行还款。
     */

    private int accountPeriodDays;
    private String cardName;
    private String busType;
    private String content;
    private double payAmount;
    private List<ItemsBean> items;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public double getSurplusQuota() {
        return surplusQuota;
    }

    public void setSurplusQuota(double surplusQuota) {
        this.surplusQuota = surplusQuota;
    }

    public double getNowQuota() {
        return nowQuota;
    }

    public void setNowQuota(double nowQuota) {
        this.nowQuota = nowQuota;
    }

    public int getAccountPeriodDays() {
        return accountPeriodDays;
    }

    public void setAccountPeriodDays(int accountPeriodDays) {
        this.accountPeriodDays = accountPeriodDays;
    }


    public String getCardName() {
        return cardName;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class DatasBean {
        /**
         * freezeAmount : 0
         * name : 余额
         * currentAmount : 0
         * id : 34
         * type : 1
         * channelId : 100000
         */

        private double freezeAmount;
        private String name;
        private double currentAmount;
        private String id;
        private String type;
        private String channelId;

        public double getFreezeAmount() {
            return freezeAmount;
        }

        public void setFreezeAmount(double freezeAmount) {
            this.freezeAmount = freezeAmount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getCurrentAmount() {
            return currentAmount;
        }

        public void setCurrentAmount(double currentAmount) {
            this.currentAmount = currentAmount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }
    }


    public static class ItemsBean {
        /**
         * fees : 0.02
         * rate : 1.2%
         * name : 技术费
         */

        private double fees;
        private String rate;
        private String name;

        public double getFees() {
            return fees;
        }

        public void setFees(double fees) {
            this.fees = fees;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
