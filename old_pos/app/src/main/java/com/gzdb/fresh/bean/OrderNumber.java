package com.gzdb.fresh.bean;

import java.io.Serializable;

public class OrderNumber implements Serializable {


    private int order_count;//待处理数量
    private int refund_apply_count;//申请退款数量

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getRefund_apply_count() {
        return refund_apply_count;
    }

    public void setRefund_apply_count(int refund_apply_count) {
        this.refund_apply_count = refund_apply_count;
    }
}
