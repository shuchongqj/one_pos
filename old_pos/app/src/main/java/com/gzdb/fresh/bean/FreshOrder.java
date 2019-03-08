package com.gzdb.fresh.bean;

import java.util.List;

public class FreshOrder {

    /**
     * all_commodity_price : 4673
     * item_total_price : 4673
     * member_total_price : 4673
     * detail_list : [{"name":"长寿鱼","count":"1"},{"name":"多宝鱼","count":"1"}]
     * show_name : 二号生活费
     * address_alias : 二号生活费
     * delivery_time : 1542855780000
     * item_names : 长寿鱼,多宝鱼
     * distribution_order_number : 0
     * passport_id : 1919889
     * group_purchase_id : -2
     * receipt_nick_name : 伟哥
     * cancel_logger : null
     * id : 1191
     * receipt_phone : 15900000000
     * trans_type : WEIXIN
     * total_price : 4673
     * create_time : 1542852209000
     * ca_head_portrait : https://wx.qlogo.cn/mmopen/vi_32/GuTxN2keHM9nVj6WY9DeyZm0rw6qs6ebd94a4tsib8TFLh4SA7cq6hSGvetNCpICKJTNeuwO0gRhVCWrC91edYA/132
     * buy_count : 2
     * distribution_fee : 0
     * order_sequence_number : 201811221919889100329096
     * distribution_address : 广东省广东市
     * user_id : 1928098
     * format_create_time : 2018-11-22 10:03:29
     * distribution_order_info : null
     * distribution_type : 0
     * deliver_status : 0
     * remarks : null
     * normal_quantitys : 1,1
     * status : 1
     */

    private int all_commodity_price;
    private int item_total_price;//商品总费用（普通价）
    private int member_total_price;//商品总费用（会员价）
    private String show_name;
    private String address_alias;
    private long delivery_time;//计划送达时间
    private String item_names;
    private String distribution_order_number;//配送单号（供应链系统）
    private int passport_id;//所属用户id
    private int group_purchase_id;//订单类型/拼团（-2 pos订单，-1待支付拼团订单，0普通订单，大于0成功支付的拼团订单）
    private String receipt_nick_name;//取货人姓名
    private Object cancel_logger;//取消订单的理由
    private int id;
    private String receipt_phone;//取货人电话
    private String trans_type;//支付类型 微信支付：WEIXIN
    private int total_price;//实付总金额
    private long create_time;//订单创建时间
    private String ca_head_portrait;
    private int buy_count;
    private int distribution_fee;//配送费用
    private String order_sequence_number;//订单的序列号
    private String distribution_address;//配送地址/送货地址
    private int user_id;//所属用户id
    private String format_create_time;
    private Object distribution_order_info;//配送单扩展信息
    private int distribution_type;//配送方式 0 到店自取 1 快递配送
    private int deliver_status;//发货状态 0未发货（待发货） 1已发货
    private Object remarks;//取货人备注
    private String normal_quantitys;
    private int status;//订单状态 0创建订单（未支付） 1已支付 2取消订单 3 已成功退款 4 申请退款不通过 5发起退款申请
    private List<DetailListBean> detail_list;

    public int getAll_commodity_price() {
        return all_commodity_price;
    }

    public void setAll_commodity_price(int all_commodity_price) {
        this.all_commodity_price = all_commodity_price;
    }

    public int getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(int item_total_price) {
        this.item_total_price = item_total_price;
    }

    public int getMember_total_price() {
        return member_total_price;
    }

    public void setMember_total_price(int member_total_price) {
        this.member_total_price = member_total_price;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getAddress_alias() {
        return address_alias;
    }

    public void setAddress_alias(String address_alias) {
        this.address_alias = address_alias;
    }

    public long getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(long delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getItem_names() {
        return item_names;
    }

    public void setItem_names(String item_names) {
        this.item_names = item_names;
    }

    public String getDistribution_order_number() {
        return distribution_order_number;
    }

    public void setDistribution_order_number(String distribution_order_number) {
        this.distribution_order_number = distribution_order_number;
    }

    public int getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(int passport_id) {
        this.passport_id = passport_id;
    }

    public int getGroup_purchase_id() {
        return group_purchase_id;
    }

    public void setGroup_purchase_id(int group_purchase_id) {
        this.group_purchase_id = group_purchase_id;
    }

    public String getReceipt_nick_name() {
        return receipt_nick_name;
    }

    public void setReceipt_nick_name(String receipt_nick_name) {
        this.receipt_nick_name = receipt_nick_name;
    }

    public Object getCancel_logger() {
        return cancel_logger;
    }

    public void setCancel_logger(Object cancel_logger) {
        this.cancel_logger = cancel_logger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceipt_phone() {
        return receipt_phone;
    }

    public void setReceipt_phone(String receipt_phone) {
        this.receipt_phone = receipt_phone;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getCa_head_portrait() {
        return ca_head_portrait;
    }

    public void setCa_head_portrait(String ca_head_portrait) {
        this.ca_head_portrait = ca_head_portrait;
    }

    public int getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(int buy_count) {
        this.buy_count = buy_count;
    }

    public int getDistribution_fee() {
        return distribution_fee;
    }

    public void setDistribution_fee(int distribution_fee) {
        this.distribution_fee = distribution_fee;
    }

    public String getOrder_sequence_number() {
        return order_sequence_number;
    }

    public void setOrder_sequence_number(String order_sequence_number) {
        this.order_sequence_number = order_sequence_number;
    }

    public String getDistribution_address() {
        return distribution_address;
    }

    public void setDistribution_address(String distribution_address) {
        this.distribution_address = distribution_address;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFormat_create_time() {
        return format_create_time;
    }

    public void setFormat_create_time(String format_create_time) {
        this.format_create_time = format_create_time;
    }

    public Object getDistribution_order_info() {
        return distribution_order_info;
    }

    public void setDistribution_order_info(Object distribution_order_info) {
        this.distribution_order_info = distribution_order_info;
    }

    public int getDistribution_type() {
        return distribution_type;
    }

    public void setDistribution_type(int distribution_type) {
        this.distribution_type = distribution_type;
    }

    public int getDeliver_status() {
        return deliver_status;
    }

    public void setDeliver_status(int deliver_status) {
        this.deliver_status = deliver_status;
    }

    public Object getRemarks() {
        return remarks;
    }

    public void setRemarks(Object remarks) {
        this.remarks = remarks;
    }

    public String getNormal_quantitys() {
        return normal_quantitys;
    }

    public void setNormal_quantitys(String normal_quantitys) {
        this.normal_quantitys = normal_quantitys;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DetailListBean> getDetail_list() {
        return detail_list;
    }

    public void setDetail_list(List<DetailListBean> detail_list) {
        this.detail_list = detail_list;
    }

    public static class DetailListBean {
        /**
         * name : 长寿鱼
         * count : 1
         */

        private String name;
        private String count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }
    }
}
