package com.one.pos;


/**
 * Created by zhumg on 7/15.
 * 暂时测试数据接口，之后会全部更换
 */
public class Contonts {


    public static String PRICE_SHOULD;//应收金额
    public static String PRICE_ACTURAL;//实收金额


    public static final String homeurl = "http://no1.0085.com/";
    public static final String b_base_url = "http://120.76.188.213:8080";                                                     //B端主域名

    public static final String URL_LOGIN = homeurl + "pos/passport/loginPassport";                                      //登录接口
    public static final String URL_GET_GOODTYPELIST = homeurl + "pos/menu/posItem/getItemUnitAndType";                  //商品分类接口
    public static final String URL_ADD_GOOD = homeurl + "pos/menu/posItem/itemStorage";                                 //添加商品接口
    public static final String URL_ADD_FIND_GOOD = homeurl + "pos/menu/posItem/getPosItemInfoByBarcode";                //添加页面匹配模板
    public static final String URL_EDIT_GOOD = homeurl + "pos/menu/posItem/editPosItem";                                //编辑商品接口
    public static final String URL_DELETE_GOOD = homeurl + "pos/menu/posItem/deletePosItem";                            //删除商品接口
    public static final String URL_GET_GOODLIST = homeurl + "pos/menu/posItem/getItemByPassportId";                     //获取商品接口
    public static final String URL_PAY = homeurl + "pos/payment/order/pay_order";                                       //调起支付接口
    public static final String URL_PAY2 = homeurl + "pos/payment/order/supplychain_pay_order";                          //一键采购调起支付接口
    public static final String URL_CREATE_ORDER = homeurl + "pos/order/manager/create_order";                           //创建订单接口
    public static final String URL_ADD_GOODTYPE = homeurl + "pos/menu/posType/addPosType";                              //新增分类
    public static final String URL_SYNC_OFFLINE_ORDER = homeurl + "pos/order/manager/sync_offline_order";               //同步离线订单
    public static final String URL_SETTLEMENT_GET_DATA = homeurl + "pos/settlement/settlement_order/get_order";         //结算页面数据
    public static final String URL_SETTLEMENT_COUNT = homeurl + "pos/settlement/settlement_order/settlement_shift";     //结算交班
    public static final String URL_SETTLEMENT_PAY = homeurl + "pos/settlement/settlement_order/settlement_pay";         //结算支付
    public static final String URL_REPOT = homeurl + "pos/order/merchant/findMerchantDayReport";                        //报表
    public static final String URL_GET_RENEWLOG = homeurl + "pos/payment/wap/renewLog";                                 //续费管理

    public static final String URL_SCREEN = homeurl + "pos/passport/screen/list";                                       //副屏地址

    public static final String URL_SUPPLY_CREATE_ORDER = homeurl + "pos/purchase/onekey/create_purchase_order";         //一键采购下单
    public static final String URL_SUPPLY_MODIFY_ORDER_ADDRESS = homeurl + "pos/order/manager/modify_order_address";    //一键采购预下单
    public static final String ULR_SUPPLY_GET_ALL_DATA_1 = homeurl + "pos/purchase/onekey/oneKeyPurchaseItemList";      //一键采购获取商品数据
    public static final String ULR_SUPPLY_GET_ALL_DATA = homeurl + "pos/purchase/onekey/warnInventoryList";             //一键采购获取商品数据
    public static final String URL_SUPPLY_ORDER_LIST = homeurl + "pos/order/manager/get_order";                         //一键采购订单列表
    public static final String URL_SUPPLY_ORDER_STATUS_LIST = homeurl + "pos/purchase/onekey/purchaseOrderStatusList";  // 一键采购订单列表
    public static final String URL_CONFIRM_ORDER = homeurl + "pos/order/manager/confirmOrder";                          //确认收货
    public static final String URL_CANCEL_ORDER = homeurl + "pos/order/manager/cancel_order";                           // 取消订单
    public static final String ORDER_ALS = homeurl + "pos/order/manager/order_detail";                                //一键采购订单详情
    //    public static final String ORDER_DETAAIL = homeurl + "pos/order/manager/getOrders";                                 //一键采购订单详情
    public static final String URL_GET_BALANCE = homeurl + "pos/payment/order/passport_currency";                       //获取商家余额
    public static final String URL_GET_MERCHANT_BALANCE = homeurl + "pos/payment/order/merchantCurrency";               //一键采购获取商家余额
    public static final String URL_CREADIT_LINE_PAY = homeurl + "pos/payment/credit_loan/submit_order";                 //信用支付接口
    public static final String URL_ADDRESS_LIST = homeurl + "pos/passport/getAddresses";                                //获取收货地址列表
    public static final String URL_DELETE_ADDRESS = homeurl + "pos/passport/deletePassportAddress";                     //删除地址
    public static final String URL_CREATE_ADDRESS = homeurl + "pos/passport/addPassportAddress";                        // 新增地址
    public static final String URL_UPDATE_ADDRESS = homeurl + "pos/passport/updatePassportAddress";                     //修改地址
    public static final String URL_SET_DEFAULT_ADDRESS = homeurl + "pos/passport/setDefaultAddress";                    //默认地址

    public static final String URL_NOTICE_DETAIL = homeurl + "pos/passport/getPosProtocolById";                         //首页通知详情
    public static final String URL_NOTICE_LIST = homeurl + "pos/passport/getPosProtocolAll";                            //首页通知列表

    public static final String URL_RECHARGE = homeurl + "pos/passport/getRenew";                                        // 获取过期付款数据
    public static final String URL_PAY_RECHARGE_CODE = homeurl + "pos/qrcode/manager/show_renew_code_content";          // 获取商家对应付款CODE
    public static final String URL_PAY_QR_CODE = homeurl + "pos/payment/wap/renew/%1$s/%2$s";

    public static final String URL_VAS_GET_ITEM = homeurl + "pos/extended/charge19e/chargeMenu";                        //首页通知列表
    public static final String URL_CHARGE_RECORD = homeurl + "pos/order/manager/getOrderTransactionRecord";             //交易记录
    public static final String URL_GET_ORDER_DETAIL = homeurl + "pos/order/manager/order_detail ";                      //获取订单详情
    public static final String PAY_CODE = homeurl + "pos/payment/wap/qr_scan";                                          //获取订单详情
    public static final String ORDER_STATISTICS = homeurl + "pos/discountrecord/addAiscountRecord";                     //订单折扣统计
    public static final String ITEM_TYPE = homeurl + "pos/menu/posType/getitemTypes";

    public static final String SET_VIP_PRICE = homeurl + "pos/discountrecord/updateItemDiscount";                               //会员价批量修改

    /**
     * B端相关
     */
    public static final String VPI_CARD_PAY = b_base_url + "customer/pay/topay";                                        //会员卡支付

    /**
     * 折扣系统
     */
    public static final String SALE_LIST = homeurl + "pos/activity/activityList";                                       //获取折扣列表
    public static final String ADD_SALE = homeurl + "pos/activity/activitySaveAndUpdate";                               //编辑折扣
    public static final String FINISH_SALE = homeurl + "pos/activity/endActivity";                                      //结束折扣
    public static final String DELETE_SALE = homeurl + "pos/activity/deleteActivity";                                   //删除折扣
    public static final String SEARCH_PRODUCT_LIST = homeurl + "pos/item/getItemList";                                  //获取商品列表
    public static final String SALE_INFO = homeurl + "pos/activity/getActivity";                                        //获取活动信息
    public static final String SALE_TYPE_3 = homeurl + "pos/activity/getActivityFullcut";                               //获取满减信息
    public static final String SALE_TYPE_2 = homeurl + "pos/activity/getActivityCombination";

    /**
     * 羊城通
     */
    public static final String YCT_CONFIG = homeurl + "pos/yct/config";                                                 //羊城通相关配置
    public static final String YCT_PAY_ORDER = homeurl + "pos/payment/order/pay_order";                                 //支付订单
    public static final String YCT_RECORD_LIST = homeurl + "pos/yct/yctList";                                           //充值记录
    public static final String YCT_RECORD_DETAIL = homeurl + "pos/yct/yctDetails";                                      //记录详情

    /**
     * 优惠券
     */
    public static final String CHECK_COUPON = homeurl + "pos/coupon/checkCoupon";                                      //查询优惠劵

    /**
     * 扫码登录
     */
    public static final String EQUIPMENT_NUMBER = homeurl + "pos/passport/equipmentNumber";

    /**
     * 广告
     */
    public static final String AD_INFO = homeurl + "pos/extended/advert_info/getAdvertising";

    /**
     * 广告
     */
    public static final String AD_TEXT = homeurl + "pos/discountrecord/getPosCommon";                           //副屏支付广告
    public static final String SCREEN_FILE = homeurl + "pos/discountrecord/getScreenList";                      //副屏广告

    /**
     * 商城
     */
    public static final String APPLY_JOIN = b_base_url + "customer/business/applyjoin";                                 //申请入驻
    public static final String MALL_ORDER_LIST = b_base_url + "customer/order/busonlineorderlist";                      //商城订单列表
    public static final String getbusinesssendfee = b_base_url + "/pos/business/getbusinesssendfee";                      //商城订单列表
    public static final String setbusinesssendfee = b_base_url + "/pos/business/setbusinesssendfee";                      //商城订单列表
    public static final String MALL_ORDER_DETAIL = b_base_url + "customer/order/onlineorderdetail";                     //商城订单详情
    public static final String MALL_ORDER_QUERY = b_base_url + "customer/order/scansequencenumber";                     //扫取货码
    public static final String updateorderstatus = b_base_url + "customer/order/updateorderstatus";//订单核销（针对上门自提订单）
    public static final String orderdistribution = b_base_url + "/customer/orderprocessing/orderdistribution";//打印订单/生成配送订单（针对快递配送订单）
    public static final String MALL_PRODUCT_LIST = b_base_url + "customer/commoditymanage/commoditylist";               //商城商品列表
    public static final String MALL_PRODUCT_TYPE = b_base_url + "customer/commodity/commoditytype";                     //商城商品类型
    public static final String MALL_PRODUCT_ON = b_base_url + "customer/commoditymanage/up";                            //商城商品上架
    public static final String MALL_PRODUCT_OFF = b_base_url + "customer/commoditymanage/down";                         //商城商品下架
    public static final String MALL_PRODUCT_DETAIL = b_base_url + "customer/commoditymanage/detail";                    //商城商品详情
    public static final String MALL_PRODUCT_EDIT = b_base_url + "customer/commoditymanage/modify";                      //商城商品编辑
    public static final String MALL_SYS_PRODUCT_LIST = b_base_url + "/customer/commoditymanage/commoditytemplate";      //商城平台商品
    public static final String MALL_PRODUCT_SELETCT = b_base_url + "customer/commoditymanage/bindingcommodity";         //商城平台商品上架
    public static  final String FRESH_ORDER_LIST = b_base_url+"pos/order/list"; //生鲜系统的订单列表
    public static  final String FRESH_ORDER_DETAIL = b_base_url+"pos/order/detail"; //生鲜系统的订单详情
    public static  final String FRESH_ORDER_NUMBER = b_base_url+"pos/orderstatistics/waitingforshipment"; //生鲜系统统计待发货订单数量
    public static final  String WXREFUND = b_base_url + "customer/orderprocessing/wxrefund";//执行订单退款




    public static final int APP_TYPE_SUPERMARKER = 1;//1超市版
    public static final int APP_TYPE_DEFAULT = 2;//基础版

    public static final int APP_TYPE = APP_TYPE_SUPERMARKER;//app类型，1超市版，2基础版，3管家版

    //pos基础版6，超市版10
    public static int getUpdateType() {
        switch (APP_TYPE) {
            case APP_TYPE_DEFAULT:
                return 6;
            case APP_TYPE_SUPERMARKER:
                return 18;
            default:
                return 10;
        }
    }

    //旧接口
    public static final String BASE_URL = "http://no1.0085.com/";
    public static final String SESSION_KEY_LOGIN = BASE_URL + "/controller.do?login";//测试服 sessionkey 获取
    public static final String WEIXIN_URL = BASE_URL + "/ci/superMarketController.do?";
}
