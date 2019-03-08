package com.gzdb.quickbuy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zxy on 2016/12/13.
 */

public class NotAtOrderListBean implements Serializable {

    private List<NotAccomplishBean> orderList;

    public List<NotAccomplishBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<NotAccomplishBean> orderList) {
        this.orderList = orderList;
    }
}
