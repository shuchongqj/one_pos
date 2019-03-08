package com.gzdb.supermarket.cache;

public class ShopCartItemPrice {

    //原单价
    public double price;
    //会员单价
    public double vipPrice;

    //数量
    public double count;
    //自打折后的单价，即称重价格
    public double weigetPrice;
    //当前晚上打折的单价
    public double nightPrice;
    //商品直接优惠单价
    public double couponPrice;
    //组合购买后分配到的单价
    public double groupPrice;
    //数量满减价
    public double fullPrice;

    //最终要结算的价格
    public double orderPrice;
    public int orderPriceType;
    //会员最终要结算的价格
    public double orderVipPrice;
    public int orderVipPriceType;


    //默认情况下，所有价格都是原价
    public void initPrice(double price) {
        this.price = price;
        //所有价格都是原价
        weigetPrice = price;
        nightPrice = price;
        couponPrice = price;
        groupPrice = price;
        fullPrice = price;
    }

    //计算价格
    public void handlerOrderPrice() {
        orderPrice = handlerOrderPrice(price);
        orderPriceType = handlerOrderPriceType(orderPrice, price);

        orderVipPrice = handlerOrderPrice(vipPrice);
        orderVipPriceType = handlerOrderPriceType(orderVipPrice, vipPrice);
    }

    private int handlerOrderPriceType(double nowPrice, double srcPrice) {
        //看看是用什么价格进行计算的了
        if (nowPrice == srcPrice) {
            //原价
            return 0;
        } else if (nowPrice == nightPrice) {
            //晚上折扣价
            return 1;
        } else if (nowPrice == couponPrice) {
            //单口优惠价
            return 2;
        } else if (nowPrice == groupPrice) {
            //组合 价
            return 3;
        } else if (nowPrice == fullPrice) {
            //数量满减价
            return 4;
        }
        return -1;
    }

    private double handlerOrderPrice(double srcPrice) {
        double op = srcPrice;
        op = Math.min(srcPrice, weigetPrice);
        op = Math.min(op, nightPrice);
        op = Math.min(op, couponPrice);
        op = Math.min(op, groupPrice);
        op = Math.min(op, fullPrice);
        return op;
    }
}
