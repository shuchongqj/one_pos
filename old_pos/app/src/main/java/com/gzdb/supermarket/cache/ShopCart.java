package com.gzdb.supermarket.cache;

import android.content.Context;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzdb.basepos.App;
import com.gzdb.sale.bean.GroupSale;
import com.gzdb.sale.bean.SaleType3;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.sunmi.entity.HeadBean;
import com.gzdb.sunmi.entity.ItemBean;
import com.gzdb.sunmi.entity.KVP;
import com.gzdb.sunmi.entity.ListingBean;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.Group;
import com.gzdb.supermarket.entity.PlaceOderData;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.FormatUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 * Created by zhumg on 7/21.
 */
public class ShopCart {

    private List<PlaceOderData> list = new ArrayList<PlaceOderData>();

    private double totalMoney = 0;
    private double totalCount = 0;
    private double totalPayMoney = 0;
    private double totalDiscountPrice = 0;

    private double vipTotalPayMoney = 0;
    private double vipTotalDiscountPrice = 0;

    private static final int cart_ver = 10;


    //是否读取过挂单数据
    public static boolean guai_read_bool = false;

    //购物车列表
    public static List<ShopCart> shopCarts = new ArrayList<>();
    //当前购物车
    public static ShopCart nowShopCart = new ShopCart();

    //购物车的所有商品
    private List<ShopCartItem> items = new ArrayList<>();
    //当前购物车总量
    private int all_count;
    //挂单时间
    private String time;
    //挂单营业员
    private String username;

    public String getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }

    public int getCartItemCount() {
        return items.size();
    }

    public int getAllCount() {
        return all_count;
    }

    public ShopCartItem get(int index) {
        try {
            return items.get(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ShopCartItem> gets() {
        return items;
    }

    public double getAllPrice() {
        double price = 0;
        for (int i = 0; i < items.size(); i++) {
            ShopCartItem sci = items.get(i);
            double totalMoney;
            if (sci.item.getItemType() == 2) {
                totalMoney = Arith.mul(Arith.mul(Double.valueOf(sci.item.getSalesPrice()), sci.weight), sci.discount);
            } else {
                totalMoney = Arith.mul(Double.valueOf(sci.item.getSalesPrice()), sci.count);          //单品总额
            }
            price = Arith.add(price, totalMoney);
        }
        return Arith.round(price, 2);
    }

    public void copy(ShopCart shopCart) {
        this.items.clear();
        this.all_count = shopCart.all_count;
        for (int i = 0; i < shopCart.items.size(); i++) {
            this.items.add(shopCart.items.get(i));
        }
    }

    //挂单调用
    public static boolean guai(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        ShopCart shopCart = new ShopCart();
        shopCart.time = str;
        shopCart.username = App.getInstance().getCurrentUser().getShowName();
        shopCart.all_count = nowShopCart.all_count;
        for (int i = 0; i < nowShopCart.items.size(); i++) {
            ShopCartItem shopCartItem = nowShopCart.items.get(i);
            shopCart.items.add(shopCartItem);
        }
        shopCarts.add(shopCart);
        //保存
        if (outputAll(context)) {
            nowShopCart.clear();
            return true;
        } else {
            //保存失败
            shopCarts.remove(shopCarts.size() - 1);
            return false;
        }
    }

    //保存输出所有
    public static boolean outputAll(Context context) {

        //保存的时候，如果未读取过，要先读取一次
        //否则保存就替换掉原来的数据了
        if (!guai_read_bool) {
            //读取一次
            inputAll_(context);
            guai_read_bool = true;
        }

        File dir = context.getFilesDir();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(dir.getPath() + File.separator + "carts_" + App.getInstance().getCurrentUser().getPassportId()));
            DataOutputStream dos = new DataOutputStream(fos);
            //写入版本
            dos.writeInt(cart_ver);
            dos.writeInt(shopCarts.size());
            for (int i = 0; i < shopCarts.size(); i++) {
                if (!shopCarts.get(i).output(dos)) {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("pos", "没有任何用户信息！");
            return false;
        } catch (IOException e) {
            Log.e("pos", "读取用户信息错误！");
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    Log.e("pos", "关闭用户流错误！");
                }
            }
        }
        return true;
    }

    public static void inputAll(Context context) {

        if (guai_read_bool) {
            return;
        }

        //读取一次
        guai_read_bool = true;
        inputAll_(context);
    }

    private static void inputAll_(Context context) {
        File dir = context.getFilesDir();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(dir.getPath() + File.separator + "carts_" + App.getInstance().getCurrentUser().getPassportId()));
            DataInputStream dis = new DataInputStream(fis);
            //写入版本
            int ver = dis.readInt();
            if (ver != cart_ver) {
                return;
            }
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                ShopCart shopCart = new ShopCart();
                if (shopCart.input(dis)) {
                    shopCarts.add(shopCart);
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("pos", "没有任何用户信息！");
        } catch (IOException e) {
            Log.e("pos", "读取用户信息错误！");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    Log.e("pos", "关闭用户流错误！");
                }
            }
        }
    }

    private boolean output(DataOutputStream dos) {
        try {
            dos.writeInt(items.size());
            for (int i = 0; i < items.size(); i++) {
                //输出其它
                items.get(i).output(dos);
            }
            dos.writeUTF(time);
            dos.writeUTF(username);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean input(DataInputStream dis) {
        try {
            int size = dis.readInt();
            boolean ok = true;
            for (int i = 0; i < size; i++) {
                ShopCartItem shopCartItem = new ShopCartItem();
                if (shopCartItem.input(dis)) {
                    all_count = all_count + shopCartItem.count;
                    items.add(shopCartItem);
                } else {
                    ok = false;
                }
            }
            time = dis.readUTF();
            username = dis.readUTF();
            return ok;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 当添加不成功时，返回为false,添加不成功是指库存不足
     */
    public boolean addItem(GoodBean item) {
        //判断是否已存在
        for (int i = 0; i < items.size(); i++) {
            ShopCartItem scItem = items.get(i);
            if (scItem.item.getId() == item.getId()) {
                SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_ADDSHOPCAR, 1, 1, 0, 0, 1);
                return scItem.add();
            }
        }
        ShopCartItem scItem = new ShopCartItem(1, item, 0, 10);
        if (scItem.add()) {
            items.add(scItem);
            SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_ADDSHOPCAR, 1, 1, 0, 0, 1);
            return true;
        }
        return false;
    }

    /**
     * 添加生鲜类商品
     */
    public boolean addFreshItem(GoodBean item, double weight, double discount) {
        item.setItemName(item.getItemName());
        ShopCartItem scItem = new ShopCartItem(2, item, weight, discount);
        if (scItem.add()) {
            items.add(scItem);
            SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_ADDSHOPCAR, 1, 1, 0, 0, 1);
            return true;
        }
        return false;
    }

    /**
     * 清空购物车
     */
    public void clear() {
        all_count = 0;
        items.clear();
    }

    /**
     * 重构json数据
     *
     * @return
     */
    public String rebuildData(double coupon, double vipSaleMoney) {

        ListingBean mListingBean = new ListingBean();

        //头部,标题
        mListingBean.setTitle("1号生活");

        //中部,表头
        HeadBean hb = new HeadBean();
        hb.setParam1("商品");
        hb.setParam2("数量");
        hb.setParam3("单价");
        mListingBean.setHead(hb);

        //数据
        List<ItemBean> ibs = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            ShopCartItem sci = items.get(i);
            //副屏bean
            ItemBean ib = new ItemBean();
            if (sci.getType() == 2) {
                if (sci.getDiscount() > 0 && sci.getDiscount() < 1) {
                    ib.setParam1(sci.item.getItemName() + "(" + Arith.mul(sci.getDiscount(), 10) + "折)");
                } else {
                    ib.setParam1(sci.item.getItemName());
                }
                ib.setParam2(sci.weight + "");
            } else {
                ib.setParam1(sci.item.getItemName());
                ib.setParam2(sci.count + "");
            }
            ib.setParam3(sci.item.getSalesPrice() + "");
            ibs.add(ib);
        }
        mListingBean.setList(ibs);

        //底部
        mListingBean.setKVPList(getSmBeen(coupon, vipSaleMoney));

        String json = new Gson().toJson(mListingBean);

        return json;
    }

    /**
     * 设置清算
     */
    public List<KVP> getSmBeen(double coupon, double vipSaleMoney) {
        calculate();
        List<KVP> kvps = new ArrayList<>();
        //数量
        KVP mKVP = new KVP();
        mKVP.setName("总价");
        mKVP.setValue("￥ " + totalMoney);
        kvps.add(mKVP);

        double discountPrice;
        double vipDiscountPrice;
        if (coupon > 0) {
            discountPrice = coupon;
        } else {
            discountPrice = totalDiscountPrice;
        }

        if (vipSaleMoney > 0) {
            vipDiscountPrice = vipSaleMoney;
        } else {
            vipDiscountPrice = vipTotalDiscountPrice;
        }

        //优惠
        KVP mKVP1 = new KVP();
        mKVP1.setName("优惠");
        mKVP1.setValue("￥ " + discountPrice);
        kvps.add(mKVP1);

        //数量
        KVP mKVP0 = new KVP();
        mKVP0.setName("会员价");
        mKVP0.setValue("￥ " + Arith.del(totalMoney, vipDiscountPrice));
        kvps.add(mKVP0);


        //收款
        KVP mKVP2 = new KVP();
        mKVP2.setName("结算价");
        mKVP2.setValue("￥ " + Arith.del(totalMoney, discountPrice));
        kvps.add(mKVP2);

        return kvps;
    }


    public class ShopCartItem {

        public GoodBean item;
        public int count;
        public int type;
        public double weight;
        public double discount;
        //下单时的价格计算对象
        public ShopCartItemPrice itemPrice;


        public ShopCartItem(int type, GoodBean item, double weight, double discount) {
            this.type = type;
            this.item = item;
            this.weight = weight;
            this.discount = discount;
        }

        protected ShopCartItem() {
        }

        /**
         * 初始化价格对象
         */
        public void initItemPrice() {
            itemPrice = new ShopCartItemPrice();
            itemPrice.initPrice(item.getSalesPrice());
            //会员价
            itemPrice.vipPrice = item.getMembershipPrice();
            if (itemPrice.vipPrice <= 0) {
                itemPrice.vipPrice = item.getSalesPrice();
            }
        }

        public GoodBean getItem() {
            return item;
        }

        public void setItem(GoodBean item) {
            this.item = item;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        /**
         * 超出库存，则返回false
         */
        public boolean add() {
            //是否需要判断库存
            boolean stock = SPUtil.getInstance().getBoolean(App.getInstance(), "stock", true);
            if (!stock) {
                if (this.count >= item.getRepertory()) {
                    return false;
                }
            }
            this.count++;
            all_count++;
            return true;
        }

        /**
         * 手输购买数量
         */
        public boolean EditorQuantity(int quantityAll) {
            all_count = all_count - count + quantityAll;
            this.count = quantityAll;
            return true;
        }


        /**
         * 小于0时，则返回false
         */
        public boolean del() {
            if (this.count == 0) {
                if (!nowShopCart.items.remove(this)) {
                    Log.e("zhumg", "删除失败！");
                }
                return false;
            } else {
                this.count--;
                all_count--;
                if (this.count == 0) {
                    if (!nowShopCart.items.remove(this)) {
                        Log.e("zhumg", "删除失败！");
                    }
                }
            }
            return true;
        }

        protected void output(DataOutputStream dos) {
            try {
                dos.writeInt(Integer.valueOf(item.getId()));
                dos.writeInt(count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        protected boolean input(DataInputStream dis) {
            try {
                int itemId = dis.readInt();
                count = dis.readInt();
//                this.item = GoodBean.getById(itemId);
                if (this.item != null) {
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }

    }

    private void calculate() {
        /**
         * 新增购物车优惠计算方法
         */
        List<String> cardId = new ArrayList<>();
        List<Integer> cardNum = new ArrayList<>();
        double totalOrderMoney = 0;             //订单总额
        double vipTotalOrderMoney = 0;             //订单总额
        double totalSaleMoney = 0;              //订单优惠金额（单品优惠、数量满减）
        double vipTotalSaleMoney = 0;           //会员订单优惠金额（单品优惠、数量满减）
        double totalGroupMoney = 0;             //组合优惠金额
        double totalFullSaleMoney = 0;          //满减优惠金额
        double vipTotalFullSaleMoney = 0;       //会员满减优惠金额

        List<Group> groups = new ArrayList<>();
        /**
         *
         */
        for (ShopCartItem carItem : ShopCart.nowShopCart.gets()) {
            LogUtils.e(carItem.item);
            PlaceOderData placeOderData = new PlaceOderData();  //总对象
            double discountMoney = 0;       //称重商品会员价
            double vipDiscountMoney = 0;    //称重商品会员价折扣
            double saleMoney = 0;           //单品优惠金额（单品优惠、数量满减）
            double vipSaleMoney = 0;        //会员单品优惠金额（单品优惠、数量满减，去除活动优惠）
            double payMoney = 0;
            double count = carItem.count;
            double discount = 1;
            if (carItem.item.getItemType() == 2) {
                count = carItem.weight;
                discount = carItem.discount;
            }
            double totalMoney = Arith.mul(carItem.item.getSalesPrice(), count);          //单品总额
            discountMoney = Arith.mul(Arith.del(1, discount), totalMoney);
            if (carItem.item.getMembershipPrice() > 0 && carItem.item.getMembershipPrice() < carItem.item.getSalesPrice()) {
                vipTotalOrderMoney = Arith.add(vipTotalOrderMoney, Arith.mul(carItem.item.getMembershipPrice(), count));
                vipSaleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getMembershipPrice())), count);
                if (discount < 1) {
                    vipDiscountMoney = Arith.mul(Arith.del(1, discount), Arith.mul(carItem.item.getMembershipPrice(), count));
                }
            } else {
                if (carItem.item.getState() == 1 && carItem.item.getActivityType() == 1) {
                    vipTotalOrderMoney = Arith.add(vipTotalOrderMoney, Arith.mul(carItem.item.getDiscount(), count));
                    vipSaleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getDiscount())), count);
                    if (discount < 1) {
                        vipDiscountMoney = Arith.mul(Arith.del(1, discount), Arith.mul(carItem.item.getDiscount(), count));
                    }
                } else {
                    vipTotalOrderMoney = Arith.add(vipTotalOrderMoney, Arith.mul(carItem.item.getSalesPrice(), count));
                    if (discount < 1) {
                        vipDiscountMoney = Arith.mul(Arith.del(1, discount), Arith.mul(carItem.item.getSalesPrice(), count));
                    }
                }
            }
            vipSaleMoney = Arith.add(vipSaleMoney, vipDiscountMoney);
            if (carItem.item.getState() == 1) {
                switch (carItem.item.getActivityType()) {
                    case 1:
                        saleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getDiscount())), carItem.count);
                        break;
                    case 2:
                        cardId.add(carItem.item.getId());
                        cardNum.add(carItem.count);
                        groups.add(new Group(carItem.item.getActivityId(), carItem.item.getId(), carItem.count, carItem.item.getSalesPrice()));
                        break;
                    case 4:
                        saleMoney = Arith.mul(Double.valueOf(carItem.item.getDiscount()), carItem.count / (int) carItem.item.getPrice());
                        vipSaleMoney = saleMoney;
                        break;
                }
            }
            saleMoney = Arith.add(saleMoney, discountMoney);
            payMoney = Arith.del(totalMoney, saleMoney);
            payMoney = payMoney > 0 ? payMoney : 0;

            placeOderData.setPrice(carItem.item.getSalesPrice() + "");
            placeOderData.setMenuId(carItem.item.getId());
            placeOderData.setCount(carItem.count + "");

            placeOderData.setDiscountMoney(saleMoney + "");
            placeOderData.setTotal("" + totalMoney);
            placeOderData.setPromotionPrice("" + payMoney);//优惠后的价格
            placeOderData.setName(carItem.item.getItemName());
            placeOderData.setSalesPromotion("N");
            placeOderData.setUnit(carItem.item.getItemUnitName());
            placeOderData.setUnitId(carItem.item.getItemUnitId());

            list.add(placeOderData);

            totalOrderMoney = Arith.add(totalOrderMoney, totalMoney);      //订单总价
            totalSaleMoney = Arith.add(totalSaleMoney, saleMoney);        //订单优惠金额（单品优惠、数量满减）
            vipTotalSaleMoney = Arith.add(vipTotalSaleMoney, vipSaleMoney);
        }

        /**
         * 组合优惠
         */
        String group_sale = SPUtil.getInstance().getString(App.getInstance(), "group_sale");
        if (!group_sale.equals("")) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<GroupSale>>() {
            }.getType();
            List<GroupSale> groupSales = gson.fromJson(group_sale, listType);

            Map<String, List<Group>> map = new HashMap<>();
            for (Group project : groups) {
                String key = project.getType() + "";
                // 按照key取出子集合
                List<Group> subProjects = map.get(key);

                // 若子集合不存在，则重新创建一个新集合，并把当前Project加入，然后put到map中
                if (subProjects == null) {
                    subProjects = new ArrayList<>();
                    subProjects.add(project);
                    map.put(key, subProjects);
                } else {
                    // 若子集合存在，则直接把当前Project加入即可
                    subProjects.add(project);
                }
            }

            for (String key : map.keySet()) {
                for (int i = 0; i < groupSales.size(); i++) {
                    if (key.equals(groupSales.get(i).getActivityId() + "")) {
                        List<Group> groups2 = map.get(key);
                        List<String> group = new ArrayList<>();
                        List<String> group3 = new ArrayList<>();
                        List<Integer> nums = new ArrayList<>();
                        for (int j = 0; j < groups2.size(); j++) {
                            group.add(groups2.get(j).getId() + "");
                            nums.add(groups2.get(j).getNum());
                        }
                        for (GroupSale.ItemsBean groupSale : groupSales.get(i).getItems()) {
                            group3.add(groupSale.getItemId() + "");
                        }
                        LogUtils.e(group);
                        LogUtils.e(group3);
                        Collections.sort(group);
                        Collections.sort(group3);
                        LogUtils.e(group.equals(group3));
                        if (group.equals(group3)) {
                            totalGroupMoney = Arith.add(totalGroupMoney, Arith.mul(groupSales.get(i).getDiscount(), Collections.min(nums)));     //套餐优惠金额乘以数组中的最小数量获取最大优惠值
                        }
                    }
                }
            }
        }

        totalMoney = totalOrderMoney;

        totalFullSaleMoney = getSaleMoney(totalOrderMoney);     //订单满减
        vipTotalFullSaleMoney = getSaleMoney(vipTotalOrderMoney);     //订单满减

        totalPayMoney = Arith.del(totalOrderMoney, Arith.add(totalSaleMoney, Arith.add(totalGroupMoney, totalFullSaleMoney)));    //计算最后的订单金额
        totalPayMoney = totalPayMoney > 0 ? totalPayMoney : 0;

        vipTotalPayMoney = Arith.del(totalOrderMoney, Arith.add(vipTotalSaleMoney, Arith.add(totalGroupMoney, vipTotalFullSaleMoney)));
        vipTotalPayMoney = vipTotalPayMoney > 0 ? vipTotalPayMoney : 0;

        totalDiscountPrice = Arith.add(totalSaleMoney, Arith.add(totalGroupMoney, totalFullSaleMoney));
        vipTotalDiscountPrice = Arith.add(vipTotalSaleMoney, Arith.add(totalGroupMoney, vipTotalFullSaleMoney));
    }

    private double getSaleMoney(double money) {
        List<SaleType3> saleTypes = App.getDaoInstant().getSaleType3Dao().loadAll();
        /**
         * 排序
         */
        Collections.sort(saleTypes, new Comparator<SaleType3>() {

            @Override
            public int compare(SaleType3 o1, SaleType3 o2) {
                int i = (int) o1.getPrice() - (int) o2.getPrice();
                if (i == 0) {
                    return (int) o1.getDiscount() - (int) o2.getDiscount();
                }
                return i;
            }
        });

        /**
         * 反转
         */
        Collections.reverse(saleTypes);

        for (int i = 0; i < saleTypes.size(); i++) {
            if (money >= saleTypes.get(i).getPrice()) {
                return saleTypes.get(i).getDiscount();
            }
        }
        return 0;
    }

}
