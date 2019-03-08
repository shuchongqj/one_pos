package com.gzdb.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public enum OrderStatusEnum {

    /**
     * 1、默认--下单
     */
    ORDER_STATUS_DEFAULT(1, "未付款"),
    /**
     * 2、取消
     */
    ORDER_STATUS_CANCEL(2, "已取消"),
    /**
     * 4、失效
     */
    ORDER_STATUS_INVALID(4, "已失效"),
    /**
     * 8、已支付
     */
    ORDER_STATUS_PAYMENY(8, "已付款"),
    /**
     * 16、已接单
     */
    ORDER_STATUS_ACCEPT(16, "已接单"),
    /**
     * 32、发货中
     */
    ORDER_STATUS_DELIVER(32, "发货中"),
    /**
     * 64、配送中
     */
    ORDER_STATUS_DISTRIBUTION(64, "配送中"),
    /**
     * 128、送达
     */
    ORDER_STATUS_ARRIVE(128, "已送达"),
    /**
     * 256、确认收货
     */
    ORDER_STATUS_CONFIRM(256, "已完成"),
    /**
     * 260、已完成
     */
    ORDER_STATUS_CONFIRM_ONE(260, "确认收货"),
    /**
     * 512、删除订单
     */
    ORDER_STATUS_HIDE(512, "删除"),
    /**
     * 1024、分批配送
     */
    ORDER_STATUS_BATCH(1024, "分批配送"),
    /**
     * 2048、拒绝
     */
    ORDER_STATUS_REJECT(2048, "拒绝"),;

    private int type;
    private String name;

    private OrderStatusEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, OrderStatusEnum> ORDER_STATUS_TYPE_ENUM_MAP = new HashMap<>();

    static {
        OrderStatusEnum[] orderRoleTypeEnums = OrderStatusEnum.values();
        for (OrderStatusEnum orderRoleTypeEnum : orderRoleTypeEnums) {
            ORDER_STATUS_TYPE_ENUM_MAP.put(orderRoleTypeEnum.getType(), orderRoleTypeEnum);
        }
    }

    public static OrderStatusEnum getOrderRoleTypeEnum(int key) {
        return ORDER_STATUS_TYPE_ENUM_MAP.get(key);
    }

}
