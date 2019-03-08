package com.gzdb.quickbuy.bean;

import java.util.List;

/**
 * Created by Zxy on 2016/12/22.
 *  编辑地址列表
 */

public class EditorAllrBean {


    /**
     * listSize : 1
     * list : [{"id":192,"userId":235880,"receiverSex":"1","receiverName":"z 先生的店","receiverMobile":"18871016668","receiverAddress":"广东省广州市天河区员村南街55号","receiverDetailAddress":"广东省广州市天河区员村南街55号","longitude":113.362598,"latitude":23.123322,"isDefault":"Y","createTime":"2016-12-05 18:11"}]
     */

    private int listSize;
    /**
     * id : 192
     * userId : 235880
     * receiverSex : 1
     * receiverName : z 先生的店
     * receiverMobile : 18871016666
     * receiverAddress : 广东省广州市天河区员村南街55号
     * receiverDetailAddress : 广东省广州市天河区员村南街55号
     * longitude : 113.362598
     * latitude : 23.123322
     * isDefault : Y
     * createTime : 2016-12-05 18:11
     */

    private List<ListBean> list;

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private int userId;
        private String receiverSex;
        private String receiverName;
        private String receiverMobile;
        private String receiverAddress;
        private String receiverDetailAddress;
        private double longitude;
        private double latitude;
        private String isDefault;
        private String createTime;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getReceiverSex() {
            return receiverSex;
        }

        public void setReceiverSex(String receiverSex) {
            this.receiverSex = receiverSex;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getReceiverMobile() {
            return receiverMobile;
        }

        public void setReceiverMobile(String receiverMobile) {
            this.receiverMobile = receiverMobile;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public String getReceiverDetailAddress() {
            return receiverDetailAddress;
        }

        public void setReceiverDetailAddress(String receiverDetailAddress) {
            this.receiverDetailAddress = receiverDetailAddress;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", receiverSex='" + receiverSex + '\'' +
                    ", receiverName='" + receiverName + '\'' +
                    ", receiverMobile='" + receiverMobile + '\'' +
                    ", receiverAddress='" + receiverAddress + '\'' +
                    ", receiverDetailAddress='" + receiverDetailAddress + '\'' +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", isDefault='" + isDefault + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }

}
