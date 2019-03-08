package com.gzdb.mall.bean;

import java.util.List;

public class MallOrderDetail {

    /**
     * all_group_price : 200
     * all_commodity_price : 200
     * item_total_price : 200
     * member_total_price : 120
     * show_name : 小泡芙
     * address_alias : 小泡芙
     * item_names : 测试836
     * ca_pay_member_type : 0
     * passport_id : 1927939
     * member_last_pay : 170
     * group_purchase_id : 0
     * receipt_nick_name : 猪大蒜
     * cancel_logger : null
     * id : 1077
     * ordinary_last_pay : 250
     * receipt_phone : 15800000000
     * trans_type : MEMBER_CARD
     * total_price : 170
     * create_time : 1538114685000
     * ca_head_portrait : https://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo3qia4e1LiaLUACHUXxzgLGwA0wbxKkEHV8ibf6zx3G0NC8PubPc4ayT7JziaLyGtthWhAm4VYJa3DSA/132
     * buy_count : 1
     * distribution_fee : 50
     * preferential_amount : 80
     * order_sequence_number : 201809281927939140445621
     * list : [{"original_price":350,"item_total_price":200,"item_type_name":null,"item_type_id":7,"item_attr":1,"member_price":120,"group_price":200,"passport_id":1927939,"push_batch_num":0,"business_ratio":9500,"id":1666,"barcode":"4561256221","cost_price":200,"imgs":"https://qidianimg.oss-cn-shenzhen.aliyuncs.com/2018/0927/MallImgs/15380456630407.png","create_time":1538114685000,"item_id":180,"distribution_fee":50,"item_name":"测试836","order_sequence_number":"201809281927939140445621","platform_ratio":0,"distribution_address":"广东省广州市番禺区汉兴西路9区1栋908","name":"测试836","reward_gold":0,"normal_quantity":1,"distribution_type":1,"order_id":1077}]
     * distribution_address : 广东省广州市番禺区汉兴西路9区1栋908
     * user_id : 1928048
     * all_member_price : 120
     * format_create_time : 2018-09-28 14:04:45
     * distribution_type : 1
     * deliver_status : 0
     * remarks : null
     * status : 1
     */

    private int all_group_price;
    private int all_commodity_price;
    private int item_total_price;//商品总费用（普通价）
    private int member_total_price;//商品总费用（会员价）
    private String show_name;
    private String address_alias;
    private String item_names;
    private String ca_pay_member_type;
    private int passport_id;//所属商家id
    private int member_last_pay;
    private int group_purchase_id;//订单类型/拼团id(-2pos订单；-1待支付拼团订单；0普通订单，大于0成功支付的拼团订单)
    private String receipt_nick_name;//取货人姓名
    private String cancel_logger;//取消订单的理由
    private int id;
    private int ordinary_last_pay;
    private String receipt_phone;//取货人电话
    private String trans_type;//支付类型 微信支付：WEIXIN
    private int total_price;//实付总金额
    private long create_time;//订单创建时间
    private String ca_head_portrait;
    private int buy_count;
    private int distribution_fee;
    private int preferential_amount;
    private String order_sequence_number;
    private String distribution_address;
    private int user_id;
    private String all_member_price;
    private String format_create_time;
    private int distribution_type;
    private int deliver_status;
    private String remarks;
    private int status;
    private List<ListBean> list;

    public int getAll_group_price() {
        return all_group_price;
    }

    public void setAll_group_price(int all_group_price) {
        this.all_group_price = all_group_price;
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

    public String getItem_names() {
        return item_names;
    }

    public void setItem_names(String item_names) {
        this.item_names = item_names;
    }

    public String getCa_pay_member_type() {
        return ca_pay_member_type;
    }

    public void setCa_pay_member_type(String ca_pay_member_type) {
        this.ca_pay_member_type = ca_pay_member_type;
    }

    public int getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(int passport_id) {
        this.passport_id = passport_id;
    }

    public int getMember_last_pay() {
        return member_last_pay;
    }

    public void setMember_last_pay(int member_last_pay) {
        this.member_last_pay = member_last_pay;
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

    public String getCancel_logger() {
        return cancel_logger;
    }

    public void setCancel_logger(String cancel_logger) {
        this.cancel_logger = cancel_logger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrdinary_last_pay() {
        return ordinary_last_pay;
    }

    public void setOrdinary_last_pay(int ordinary_last_pay) {
        this.ordinary_last_pay = ordinary_last_pay;
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

    public int getPreferential_amount() {
        return preferential_amount;
    }

    public void setPreferential_amount(int preferential_amount) {
        this.preferential_amount = preferential_amount;
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

    public String getAll_member_price() {
        return all_member_price;
    }

    public void setAll_member_price(String all_member_price) {
        this.all_member_price = all_member_price;
    }

    public String getFormat_create_time() {
        return format_create_time;
    }

    public void setFormat_create_time(String format_create_time) {
        this.format_create_time = format_create_time;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
         * original_price : 350
         * item_total_price : 200
         * item_type_name : null
         * item_type_id : 7
         * item_attr : 1
         * member_price : 120
         * group_price : 200
         * passport_id : 1927939
         * push_batch_num : 0
         * business_ratio : 9500
         * id : 1666
         * barcode : 4561256221
         * cost_price : 200
         * imgs : https://qidianimg.oss-cn-shenzhen.aliyuncs.com/2018/0927/MallImgs/15380456630407.png
         * create_time : 1538114685000
         * item_id : 180
         * distribution_fee : 50
         * item_name : 测试836
         * order_sequence_number : 201809281927939140445621
         * platform_ratio : 0
         * distribution_address : 广东省广州市番禺区汉兴西路9区1栋908
         * name : 测试836
         * reward_gold : 0
         * normal_quantity : 1
         * distribution_type : 1
         * order_id : 1077
         */

        private int original_price;
        private int item_total_price;
        private String item_type_name;
        private int item_type_id;
        private int item_attr;
        private int member_price;
        private int group_price;
        private int passport_id;
        private String push_batch_num;
        private int business_ratio;
        private int id;
        private String barcode;
        private int cost_price;
        private String imgs;
        private long create_time;
        private int item_id;
        private int distribution_fee;
        private String item_name;
        private String order_sequence_number;
        private int platform_ratio;
        private String distribution_address;
        private String name;
        private int reward_gold;
        private int normal_quantity;
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

        public String getItem_type_name() {
            return item_type_name;
        }

        public void setItem_type_name(String item_type_name) {
            this.item_type_name = item_type_name;
        }

        public int getItem_type_id() {
            return item_type_id;
        }

        public void setItem_type_id(int item_type_id) {
            this.item_type_id = item_type_id;
        }

        public int getItem_attr() {
            return item_attr;
        }

        public void setItem_attr(int item_attr) {
            this.item_attr = item_attr;
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

        public String getPush_batch_num() {
            return push_batch_num;
        }

        public void setPush_batch_num(String push_batch_num) {
            this.push_batch_num = push_batch_num;
        }

        public int getBusiness_ratio() {
            return business_ratio;
        }

        public void setBusiness_ratio(int business_ratio) {
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

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
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

        public int getPlatform_ratio() {
            return platform_ratio;
        }

        public void setPlatform_ratio(int platform_ratio) {
            this.platform_ratio = platform_ratio;
        }

        public String getDistribution_address() {
            return distribution_address;
        }

        public void setDistribution_address(String distribution_address) {
            this.distribution_address = distribution_address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getReward_gold() {
            return reward_gold;
        }

        public void setReward_gold(int reward_gold) {
            this.reward_gold = reward_gold;
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
