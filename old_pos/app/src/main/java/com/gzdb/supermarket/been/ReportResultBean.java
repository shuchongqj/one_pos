package com.gzdb.supermarket.been;

import java.util.List;

/**
 * Created by nongyd on 17/6/8.
 */

public class ReportResultBean {


    private List<DatasBean> datas;

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * piId : 13343
         * itemName : 红豆牛奶
         * itemTemplateId : 1427536
         * itId : 103786
         * itTitle : 饮料
         * sumCount : 19
         * sumCostMoney : 4750
         * sumTotalMoney : 6650
         * normalPrice : 3.5
         * costPrice : 2.5
         * itemBarcode : 15666666
         * totalMoney : 66.5
         * marginMoney : 19
         * grossMargin : 28.57%
         * costMoney : 47.5
         */

        private int piId;
        private String itemName;
        private String itemTemplateId;
        private int itId;
        private String itTitle;
        private double sumCount;
        private String sumCostMoney;
        private String sumTotalMoney;
        private String normalPrice;
        private String costPrice;
        private String itemBarcode;
        private double totalMoney;
        private double marginMoney;
        private String grossMargin;
        private String costMoney;

        public int getPiId() {
            return piId;
        }

        public void setPiId(int piId) {
            this.piId = piId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemTemplateId() {
            return itemTemplateId;
        }

        public void setItemTemplateId(String itemTemplateId) {
            this.itemTemplateId = itemTemplateId;
        }

        public int getItId() {
            return itId;
        }

        public void setItId(int itId) {
            this.itId = itId;
        }

        public String getItTitle() {
            return itTitle;
        }

        public void setItTitle(String itTitle) {
            this.itTitle = itTitle;
        }

        public double getSumCount() {
            return sumCount;
        }

        public void setSumCount(double sumCount) {
            this.sumCount = sumCount;
        }

        public String getSumCostMoney() {
            return sumCostMoney;
        }

        public void setSumCostMoney(String sumCostMoney) {
            this.sumCostMoney = sumCostMoney;
        }

        public String getSumTotalMoney() {
            return sumTotalMoney;
        }

        public void setSumTotalMoney(String sumTotalMoney) {
            this.sumTotalMoney = sumTotalMoney;
        }

        public String getNormalPrice() {
            return normalPrice;
        }

        public void setNormalPrice(String normalPrice) {
            this.normalPrice = normalPrice;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
        }

        public String getItemBarcode() {
            return itemBarcode;
        }

        public void setItemBarcode(String itemBarcode) {
            this.itemBarcode = itemBarcode;
        }

        public double getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(double totalMoney) {
            this.totalMoney = totalMoney;
        }

        public double getMarginMoney() {
            return marginMoney;
        }

        public void setMarginMoney(double marginMoney) {
            this.marginMoney = marginMoney;
        }

        public String getGrossMargin() {
            return grossMargin;
        }

        public void setGrossMargin(String grossMargin) {
            this.grossMargin = grossMargin;
        }

        public String getCostMoney() {
            return costMoney;
        }

        public void setCostMoney(String costMoney) {
            this.costMoney = costMoney;
        }
    }
}
