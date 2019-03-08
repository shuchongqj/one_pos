package com.gzdb.fresh.bean;


import java.util.List;

public class FreshOrderDetail {


    /**
     * all_commodity_price : 4673
     * item_total_price : 4673
     * member_total_price : 4673
     * show_name : 二号生活费
     * address_alias : 二号生活费
     * delivery_time : 1542855780000
     * item_names : 多宝鱼,长寿鱼
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
     * list : [{"original_price":2007,"item_total_price":4673,"item_type_name":null,"item_type_id":100342,"description":"只是一条鱼","member_price":2007,"group_price":2007,"passport_id":1919889,"item_unit":"千克","push_batch_num":0,"business_ratio":null,"id":1804,"barcode":"59826448231","cost_price":1620,"create_time":1542852209000,"item_id":1111231,"distribution_fee":0,"item_name":"多宝鱼","order_sequence_number":"201811221919889100329096","platform_ratio":null,"distribution_address":"广东省广东市","item_img_url":"https://qidianimg.oss-cn-shenzhen.aliyuncs.com/2018/1120/positems/15427055970104.jpeg","name":"多宝鱼","normal_quantity":1,"distribution_type":0,"order_id":1191},{"original_price":2666,"item_total_price":4673,"item_type_name":null,"item_type_id":100342,"description":"只是一条鱼","member_price":2666,"group_price":2666,"passport_id":1919889,"item_unit":"千克","push_batch_num":0,"business_ratio":null,"id":1805,"barcode":"54306617113","cost_price":2150,"create_time":1542852209000,"item_id":1111236,"distribution_fee":0,"item_name":"长寿鱼","order_sequence_number":"201811221919889100329096","platform_ratio":null,"distribution_address":"广东省广东市","item_img_url":"https://qidianimg.oss-cn-shenzhen.aliyuncs.com/2018/1119/positems/15426099900775.jpeg","name":"长寿鱼","normal_quantity":1,"distribution_type":0,"order_id":1191}]
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
    private long delivery_time;
    private String item_names;
    private String distribution_order_number;//配送单号（供应链系统）
    private int passport_id;//所属商家id
    private int group_purchase_id;//订单类型/拼团ID(-2Pos订单，-1待支付拼团订单，0普通订单，大于0成功支付的拼团订单)
    private String receipt_nick_name;
    private Object cancel_logger;//取消订单的理由
    private int id;
    private String receipt_phone;
    private String trans_type;
    private int total_price;//实付总金额
    private long create_time;//订单创建时间
    private String ca_head_portrait;
    private int buy_count;
    private int distribution_fee;//配送费用
    private String order_sequence_number;
    private String distribution_address;
    private int user_id;//所属用户id
    private String format_create_time;
    private Object distribution_order_info;//配送单扩展信息
    private int distribution_type;//配送方式 0到店自取  1快递配送
    private int deliver_status;//发货状态 0到店自取 1快递配送
    private Object remarks;//取货人备注
    private String normal_quantitys;
    private int status;//订单状态 0创建订单（未支付） 1已支付 2 取消订单 3 已成功退款  4申请退款不通过  5 发起退款
    private int ordinary_last_pay;
    private int preferential_amount;
    private List<ListBean> list;

    public int getPreferential_amount() {
        return preferential_amount;
    }

    public void setPreferential_amount(int preferential_amount) {
        this.preferential_amount = preferential_amount;
    }

    public int getOrdinary_last_pay() {
        return ordinary_last_pay;
    }

    public void setOrdinary_last_pay(int ordinary_last_pay) {
        this.ordinary_last_pay = ordinary_last_pay;
    }

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * original_price : 2007
         * item_total_price : 4673
         * item_type_name : null
         * item_type_id : 100342
         * description : 只是一条鱼
         * member_price : 2007
         * group_price : 2007
         * passport_id : 1919889
         * item_unit : 千克
         * push_batch_num : 0
         * business_ratio : null
         * id : 1804
         * barcode : 59826448231
         * cost_price : 1620
         * create_time : 1542852209000
         * item_id : 1111231
         * distribution_fee : 0
         * item_name : 多宝鱼
         * order_sequence_number : 201811221919889100329096
         * platform_ratio : null
         * distribution_address : 广东省广东市
         * item_img_url : https://qidianimg.oss-cn-shenzhen.aliyuncs.com/2018/1120/positems/15427055970104.jpeg
         * name : 多宝鱼
         * normal_quantity : 1
         * distribution_type : 0
         * order_id : 1191
         */

        private int original_price;//当时商品原价
        private int item_total_price;//总价
        private Object item_type_name;//当时的归属分类名称
        private int item_type_id;//当时商品名称
        private String description;
        private int member_price;//当时会员价
        private int group_price;//当时团购价
        private int passport_id;
        private String item_unit;
        private int push_batch_num;//订单商品推送批次号
        private Object business_ratio;//当时商家占比（万分比）
        private int id;
        private String barcode;//当时商品条码
        private int cost_price;//当时成本价
        private long create_time;//创建时间
        private int item_id;//商品id
        private int distribution_fee;
        private String item_name;//当时商品名称
        private String order_sequence_number;
        private Object platform_ratio;//当时平台占比（万分比）
        private String distribution_address;
        private String item_img_url;
        private String name;
        private int normal_quantity;//购买数量
        private int distribution_type;
        private int order_id;

        public int getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(int original_price) {
            this.original_price = original_price;
        }

        public int getItem_total_price() {
            return item_total_price;
        }

        public void setItem_total_price(int item_total_price) {
            this.item_total_price = item_total_price;
        }

        public Object getItem_type_name() {
            return item_type_name;
        }

        public void setItem_type_name(Object item_type_name) {
            this.item_type_name = item_type_name;
        }

        public int getItem_type_id() {
            return item_type_id;
        }

        public void setItem_type_id(int item_type_id) {
            this.item_type_id = item_type_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMember_price() {
            return member_price;
        }

        public void setMember_price(int member_price) {
            this.member_price = member_price;
        }

        public int getGroup_price() {
            return group_price;
        }

        public void setGroup_price(int group_price) {
            this.group_price = group_price;
        }

        public int getPassport_id() {
            return passport_id;
        }

        public void setPassport_id(int passport_id) {
            this.passport_id = passport_id;
        }

        public String getItem_unit() {
            return item_unit;
        }

        public void setItem_unit(String item_unit) {
            this.item_unit = item_unit;
        }

        public int getPush_batch_num() {
            return push_batch_num;
        }

        public void setPush_batch_num(int push_batch_num) {
            this.push_batch_num = push_batch_num;
        }

        public Object getBusiness_ratio() {
            return business_ratio;
        }

        public void setBusiness_ratio(Object business_ratio) {
            this.business_ratio = business_ratio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getCost_price() {
            return cost_price;
        }

        public void setCost_price(int cost_price) {
            this.cost_price = cost_price;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public int getDistribution_fee() {
            return distribution_fee;
        }

        public void setDistribution_fee(int distribution_fee) {
            this.distribution_fee = distribution_fee;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getOrder_sequence_number() {
            return order_sequence_number;
        }

        public void setOrder_sequence_number(String order_sequence_number) {
            this.order_sequence_number = order_sequence_number;
        }

        public Object getPlatform_ratio() {
            return platform_ratio;
        }

        public void setPlatform_ratio(Object platform_ratio) {
            this.platform_ratio = platform_ratio;
        }

        public String getDistribution_address() {
            return distribution_address;
        }

        public void setDistribution_address(String distribution_address) {
            this.distribution_address = distribution_address;
        }

        public String getItem_img_url() {
            return item_img_url;
        }

        public void setItem_img_url(String item_img_url) {
            this.item_img_url = item_img_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNormal_quantity() {
            return normal_quantity;
        }

        public void setNormal_quantity(int normal_quantity) {
            this.normal_quantity = normal_quantity;
        }

        public int getDistribution_type() {
            return distribution_type;
        }

        public void setDistribution_type(int distribution_type) {
            this.distribution_type = distribution_type;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }
    }
}




