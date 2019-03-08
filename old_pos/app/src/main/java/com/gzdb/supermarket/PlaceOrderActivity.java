package com.gzdb.supermarket;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.baidu.platform.comapi.map.A;
import com.core.http.Http;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.okgo.callback.JsonCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.sale.bean.GroupSale;
import com.gzdb.sale.bean.SaleType3;
import com.gzdb.sale.dialog.ConfirmDialog;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.screen.ScreenManager;
import com.gzdb.screen.present.ImageDisplay;
import com.gzdb.screen.present.ImageMenuDisplay;
import com.gzdb.screen.present.TextDisplay;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.sunmi.bluetooth.Printer;
import com.gzdb.sunmi.util.OpenDrawers;
import com.gzdb.sunmi.util.ScreenUtil;
import com.gzdb.supermarket.activity.PayActivity;
import com.gzdb.supermarket.adapter.PlaceorderDetailAdapter;
import com.gzdb.supermarket.been.BottonItem;
import com.gzdb.supermarket.been.CouponBean;
import com.gzdb.supermarket.been.CreateOrderSuccessBean;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.been.FreePriceGoodBean;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.Group;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.cache.ShopCartItemPrice;
import com.gzdb.supermarket.common.BottonSelectLayout;
import com.gzdb.supermarket.dialog.DiscountDialog;
import com.gzdb.supermarket.entity.PlaceOderData;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.ButtonUtils;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.GsonUtil;
import com.gzdb.supermarket.util.Utils;
import com.gzdb.supermarket.widget.NumberKeyboardView;
import com.gzdb.vip.VipMoneyInDialog;
import com.hss01248.dialog.StyledDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;
import com.zhumg.anlib.utils.JsonUtils;
import com.zhumg.anlib.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import hdx.HdxUtil;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by dianba on 2016/5/10.
 * 立即下单
 */
public class PlaceOrderActivity extends BaseActivity implements ScanGunKeyEventHelper.OnScanSuccessListener, NumberKeyboardView.OnNumberClickListener, View.OnClickListener {

    @Bind(R.id.tv_sale_text)
    TextView tvSaleText;
    @Bind(R.id.tv_sale_text2)
    TextView tvSaleText2;
    @Bind(R.id.tv_sale_text3)
    TextView tvSaleText3;
    @Bind(R.id.tv_sale_text4)
    TextView tvSaleText4;
    @Bind(R.id.tv_sale_text7)
    TextView tvSaleText7;
    @Bind(R.id.tv_total_sale)
    TextView tvTotalSale;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_add_code)
    Button btnAddCode;
    @Bind(R.id.tv_sale_text5)
    TextView tvSaleText5;
    @Bind(R.id.ll_coupon)
    LinearLayout llCoupon;
    @Bind(R.id.tv_sale_text6)
    TextView tvSaleText6;
    @Bind(R.id.btn_discount)
    Button btnDiscount;
    @Bind(R.id.vip_total)
    TextView vipTotal;
    @Bind(R.id.tv_vip_total_sale)
    TextView tvVipTotalSale;
    @Bind(R.id.numberKeyView)
    NumberKeyboardView numberKeyView;
    @Bind(R.id.iv_unit_pay)
    ImageView ivUnitPay;
    @Bind(R.id.iv_cash_pay)
    ImageView ivCashPay;
    @Bind(R.id.iv_vip_pay)
    ImageView ivVipPay;
    @Bind(R.id.iv_scan_pay)
    ImageView ivScanPay;
    @Bind(R.id.ed_mobile_phone)
    EditText edMobilePhone;
//    @Bind(R.id.bottonselect)
//    BottonSelectLayout bottonselect;

    @Bind(R.id.tv_age_a)
    TextView tv_age_a;

    @Bind(R.id.tv_age_b)
    TextView tv_age_b;

    @Bind(R.id.tv_age_c)
    TextView tv_age_c;

    private BottonItem selectItem;

    private ListView mPlaceorderDetail;//商品显示列表
    private PlaceorderDetailAdapter mPlaceorderDetailAdapter;
    private List<PlaceOderData> list = new ArrayList<PlaceOderData>();

    private EditText mPaidIn;//输入收到的现金

    private TextView mGoodNumber;//总数量
    private TextView youhui_total;//优惠后总价
    private TextView mGoodTotalPrice;//应收金额
    private TextView tvTotalPrice;//商品总价

    ScanGunKeyEventHelper scanGunKeyEventHelper = null;//扫码

    RelativeLayout layoutOriginalPrice;
    LinearLayout layoutCharge;//找零
    TextView textcharge;//找零
    StringBuilder builder = new StringBuilder();
    StringBuilder builder1 = new StringBuilder();

    //-public static double toPrice;//原价

    private Dialog dialog;//支付时的过度框
    private int[] PAYTYPE = new int[]{1, 2, 3, 4, 5, 6};//1为微信，2为支付宝，3为现金,4为1号生活扫码支付,5会员卡支付,6聚合支付
    private List<ItemSnapshotsBean> orderItemBeen;//下单item集合，现金支付离线订单要用
    List<SaleType3> saleTypes = new ArrayList<>();

    private String saleText = "";
    //-private double totaldiscountPrice = 0;
    //-private double totalPayMoney = 0;            //应收金额

    private boolean payVoice = false;

    private String couponId = "";

    private ConfirmDialog confirmDialog;
    private DiscountDialog discountDialog;

    //-private double allMoney = 0;

    //总折扣价，默认不打折，即1
    private double mDiscount = 1;

    //-private double oTotaldiscountPrice = 0;      //原始折扣金额
    //-private double oTotalPayMoney = 0;          //原始应收金额

    //-private double oVTotaldiscountPrice = 0;      //会员原始折扣金额
    //-private double oVTotalPayMoney = 0;      //会员原始折扣金额

    //-private double vipTotalMoney = 0;               //会员价总计金额（参与活动前）
    //-private double vipTotalPayMoney = 0;            //会员价支付总计金额
    //-private double vipTotalDiscountPrice = 0;       //会员总计优惠金额

    private ScreenManager screenManager = ScreenManager.getInstance();
    private ImageMenuDisplay imageMenuDisplay = null;
    private TextDisplay textDisplay = null;
    private ImageDisplay imageDisplay = null;

    int customerType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);
        ButterKnife.bind(this);
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        saleTypes = App.getDaoInstant().getSaleType3Dao().loadAll();
        initView();
//        bottonselect.setMLayoutClickListener(new BottonSelectLayout.mlayoutClickListener() {
//            @Override
//            public void onItemClick(BottonItem mEntity) {
//                selectItem = mEntity;
//            }
//        });

        tv_age_a.setOnClickListener(this);
        tv_age_a.setText("老");
        tv_age_b.setOnClickListener(this);
        tv_age_b.setText("中");
        tv_age_c.setOnClickListener(this);
        tv_age_c.setText("青");

        changeAgeButton(tv_age_a, tv_age_b, tv_age_c);

        disableShowSoftInput();

//        if (SPUtil.getInstance().getInt(mContext, "isCode") == 1) {
//            layoutLiftPay.setVisibility(View.VISIBLE);
//        } else {
//            layoutLiftPay.setVisibility(View.GONE);
//        }

        payVoice = SPUtil.getInstance().getBoolean(this, "payVoice", true);

        int couponState = SPUtil.getInstance().getInt(mContext, "couponState", 0);

        if (couponState != 0) {
            llCoupon.setVisibility(View.VISIBLE);
        }
        LimitsEditEnter(etCode);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        etCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b && !isOpen) {
                    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                }
            }
        });

        numberKeyView.setOnNumberClickListener(this);

//        mPaidIn.addTextChangedListener(new MoneyTextWatcher(mPaidIn));

        initScreen();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        dialog = DialogUtil.loadingDialog(mContext, "正在发起支付");

        mPlaceorderDetail = (ListView) findViewById(R.id.placeorder_detail);
        layoutCharge = (LinearLayout) findViewById(R.id.layout_charge);
        textcharge = (TextView) findViewById(R.id.text_charge);
        mPaidIn = (EditText) findViewById(R.id.paid_in);

        //这里的实收，当价格大于应收时只能用现金支付，
        mPaidIn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    cashPayStatus(false);
                    unitPayStatus(false);
                    vipPayStatus(false);
                    scanPayStatus(false);
                    return;
                }
                if (s.length() > 0 && Arith.del(s.toString(), mGoodTotalPrice.getText().toString()) != 0) {
                    if (Arith.del(s.toString(), mGoodTotalPrice.getText().toString()) < 0) {
                        layoutCharge.setVisibility(View.GONE);
                    } else {
                        layoutCharge.setVisibility(View.VISIBLE);
                    }
                    NumberFormat nf = NumberFormat.getCurrencyInstance();
                    nf.setGroupingUsed(false);
                    textcharge.setText(Arith.del(s.toString(), mGoodTotalPrice.getText().toString()) + "");
                    unitPayStatus(false);
                    vipPayStatus(false);
                    scanPayStatus(false);
                } else {
                    layoutCharge.setVisibility(View.GONE);
                    unitPayStatus(true);
                    vipPayStatus(true);
                    scanPayStatus(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGoodNumber = (TextView) findViewById(R.id.good_number);
        youhui_total = (TextView) findViewById(R.id.youhui_total);
        mGoodTotalPrice = (TextView) findViewById(R.id.good_total_price);
        layoutOriginalPrice = (RelativeLayout) findViewById(R.id.layout_originalPrice);
        tvTotalPrice = (TextView) findViewById(R.id.total_price);

        mPlaceorderDetailAdapter = new PlaceorderDetailAdapter(this, R.layout.layout_placeorder_detail_item, list);
        mPlaceorderDetail.setAdapter(mPlaceorderDetailAdapter);

        //发起折扣询问
        SupermarketIndexActivity.showWaitDialog();
        //refreshPrice();
        itemPeriodDiscountQuery();
    }


    private void itemPeriodDiscountQuery() {
        final List<Long> itemIds = new ArrayList<>();
        for (ShopCart.ShopCartItem carItem : ShopCart.nowShopCart.gets()) {
            itemIds.add(Long.parseLong(carItem.item.getId()));
        }
        HttpParams params = new HttpParams();
        params.put("itemIdArray", JsonUtils.toJson(itemIds));
        OkGo.<NydResponse<Object>>post(Contonts.itemPeriodDiscountQuery)
                .params(params)
                .execute(new JsonCallback<NydResponse<Object>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Object>> response) {
                        try {
                            SupermarketIndexActivity.closeDialog();
                            Object obj = response.body().response;
                            if (obj == null) {
                                handlerActivityTry(null);
                                return;
                            }
                            Map<Long, Integer> itemMap = new HashMap<>();
                            String str = GsonUtil.toJson(obj);
                            JSONObject json = new JSONObject(str);
                            JSONArray array = json.getJSONArray("datas");
                            if (array != null) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject item = array.getJSONObject(i);
                                    itemMap.put(item.getLong("itemId"), item.getInt("discount"));
                                }
                            }
                            handlerActivityTry(itemMap);
                            //创建充值订单
                        } catch (Exception e) {
                            e.printStackTrace();
                            handlerActivityTry(null);
                            //ToastUtil.showEorr(PlaceOrderActivity.this, "接口返回参数异常");
                        }
                    }

                    @Override
                    public void onError(Response<NydResponse<Object>> response) {
                        SupermarketIndexActivity.closeDialog();
                        super.onError(response);
                        handlerActivityTry(null);
                    }
                });
    }


//    private void refreshPrice() {
//
//        /**
//         * 新增购物车优惠计算方法
//         */
//        List<String> cardId = new ArrayList<>();
//        List<Integer> cardNum = new ArrayList<>();
//
//        double totalOrderMoney = 0;             //订单总额
//        double totalSaleMoney = 0;              //订单优惠金额（单品优惠、数量满减）
//        double vipTotalSaleMoney = 0;           //会员订单优惠金额（单品优惠、数量满减）
//        double totalGroupMoney = 0;             //组合优惠金额
//        double totalFullSaleMoney = 0;          //满减优惠金额
//        double vipTotalFullSaleMoney = 0;       //会员满减优惠金额
//
//        String saleItemText = "", saleItemText2 = "", saleItemText3 = "";
//        List<Group> groups = new ArrayList<>();
//        /**
//         *
//         */
//        for (ShopCart.ShopCartItem carItem : ShopCart.nowShopCart.gets()) {
//
//            PlaceOderData placeOderData = new PlaceOderData();  //总对象
//
//            double discountMoney = 0;       //称重商品折扣价
//            double vipDiscountMoney = 0;    //称重商品会员价折扣
//            double saleMoney = 0;           //单品优惠金额（单品优惠、数量满减）
//            double vipSaleMoney = 0;        //会员单品优惠金额（单品优惠、数量满减，去除活动优惠）
//            double payMoney = 0;
//            double count = carItem.count;
//            double weightDiscount = 1;//稳重折扣
//
//            //这个代表称重商品
//            if (carItem.item.getItemType() == 2) {
//                //如果是称重，则数量变成重量
//                count = carItem.weight;
//                weightDiscount = carItem.discount;
//            }
//
//            //单品总价，销售价 * 数量
//            double totalMoney = Arith.mul(carItem.item.getSalesPrice(), count);
//            //稳重商品的单品总价，1-称重折扣 * 单品总价
//            discountMoney = Arith.mul(Arith.del(1, weightDiscount), totalMoney);
//
//            //如果商品存在会员价，并且，会员价 < 销售价的情况 下
//            if (carItem.item.getMembershipPrice() > 0 && carItem.item.getMembershipPrice() < carItem.item.getSalesPrice()) {
//                //会员总价
//                vipTotalMoney = Arith.add(vipTotalMoney, Arith.mul(carItem.item.getMembershipPrice(), count));
//                //会员单品 优惠价
//                vipSaleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getMembershipPrice())), count);
//                if (weightDiscount < 1) {
//                    //稳重会员单品 优惠价
//                    vipDiscountMoney = Arith.mul(Arith.del(1, weightDiscount), Arith.mul(carItem.item.getMembershipPrice(), count));
//                }
//            } else {
//                //如果有活动
//                if (carItem.item.getState() == 1 && carItem.item.getActivityType() == 1) {
//                    vipTotalMoney = Arith.add(vipTotalMoney, Arith.mul(carItem.item.getDiscount(), count));
//                    vipSaleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getDiscount())), count);
//                    if (weightDiscount < 1) {
//                        vipDiscountMoney = Arith.mul(Arith.del(1, weightDiscount), Arith.mul(carItem.item.getDiscount(), count));
//                    }
//                } else {
//                    vipTotalMoney = Arith.add(vipTotalMoney, Arith.mul(carItem.item.getSalesPrice(), count));
//                    if (weightDiscount < 1) {
//                        vipDiscountMoney = Arith.mul(Arith.del(1, weightDiscount), Arith.mul(carItem.item.getSalesPrice(), count));
//                    }
//
//                }
//            }
//            vipSaleMoney = Arith.add(vipSaleMoney, vipDiscountMoney);
//            if (carItem.item.getState() == 1) {
//                switch (carItem.item.getActivityType()) {
//                    case 1:
//                        saleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getDiscount())), count);
//                        saleItemText += carItem.item.getItemName() + " <font color=\"red\"> - " + saleMoney + "</font> ";
//                        break;
//                    case 2:
//                        cardId.add(carItem.item.getId());
//                        cardNum.add(carItem.count);
//                        groups.add(new Group(carItem.item.getActivityId(), carItem.item.getId(), carItem.count, carItem.item.getSalesPrice()));
//                        break;
//                    case 4:
//                        saleMoney = Arith.mul(Double.valueOf(carItem.item.getDiscount()), carItem.count / (int) carItem.item.getPrice());
//                        vipSaleMoney = saleMoney;
//                        if (saleMoney > 0) {
//                            saleItemText2 += carItem.item.getItemName() + "<font color=\"red\"> - " + saleMoney + "</font> ";
//                        }
//                        break;
//                }
//            }
//            saleMoney = Arith.add(saleMoney, discountMoney);
//            payMoney = Arith.del(totalMoney, saleMoney);
//            payMoney = payMoney > 0 ? payMoney : 0;
//
//            placeOderData.setPrice(carItem.item.getSalesPrice() + "");
//            placeOderData.setMenuId(carItem.item.getId());
//            if (carItem.getType() == 2) {
//                placeOderData.setCount(carItem.weight + "");
//            } else {
//                placeOderData.setCount(carItem.count + "");
//            }
//            placeOderData.setDiscountMoney(saleMoney + "");
//            placeOderData.setTotal("" + totalMoney);
//            placeOderData.setPromotionPrice("" + payMoney);//优惠后的价格
//            placeOderData.setName(carItem.item.getItemName());
//            placeOderData.setSalesPromotion("N");
//            placeOderData.setUnit(carItem.item.getItemUnitName());
//            placeOderData.setUnitId(carItem.item.getItemUnitId());
//
//            list.add(placeOderData);
//
//            totalOrderMoney = Arith.add(totalOrderMoney, totalMoney);      //订单总价
//            totalSaleMoney = Arith.add(totalSaleMoney, saleMoney);        //订单优惠金额（单品优惠、数量满减）
//            vipTotalSaleMoney = Arith.add(vipTotalSaleMoney, vipSaleMoney);
//        }
//
//        /**
//         * 组合优惠
//         */
//        String group_sale = SPUtil.getInstance().getString(mContext, "group_sale");
//        if (!group_sale.equals("")) {
//            Gson gson = new Gson();
//            Type listType = new TypeToken<List<GroupSale>>() {
//            }.getType();
//            List<GroupSale> groupSales = gson.fromJson(group_sale, listType);
//
//            Map<String, List<Group>> map = new HashMap<>();
//            for (Group project : groups) {
//                String key = project.getType() + "";
//                // 按照key取出子集合
//                List<Group> subProjects = map.get(key);
//
//                // 若子集合不存在，则重新创建一个新集合，并把当前Project加入，然后put到map中
//                if (subProjects == null) {
//                    subProjects = new ArrayList<>();
//                    subProjects.add(project);
//                    map.put(key, subProjects);
//                } else {
//                    // 若子集合存在，则直接把当前Project加入即可
//                    subProjects.add(project);
//                }
//            }
//
//            for (String key : map.keySet()) {
//                for (int i = 0; i < groupSales.size(); i++) {
//                    if (key.equals(groupSales.get(i).getActivityId() + "")) {
//                        List<Group> groups2 = map.get(key);
//                        List<String> group = new ArrayList<>();
//                        List<String> group3 = new ArrayList<>();
//                        List<Integer> nums = new ArrayList<>();
//                        for (int j = 0; j < groups2.size(); j++) {
//                            group.add(groups2.get(j).getId() + "");
//                            nums.add(groups2.get(j).getNum());
//                        }
//                        for (GroupSale.ItemsBean groupSale : groupSales.get(i).getItems()) {
//                            group3.add(groupSale.getItemId() + "");
//                        }
//                        Collections.sort(group);
//                        Collections.sort(group3);
//
//                        if (group.equals(group3)) {
//                            saleItemText3 += groupSales.get(i).getActivityName() + " <font color=\"red\"> - " + Arith.mul(groupSales.get(i).getDiscount(), Collections.min(nums)) + "</font> ";
//                            totalGroupMoney = Arith.add(totalGroupMoney, Arith.mul(groupSales.get(i).getDiscount(), Collections.min(nums)));     //套餐优惠金额乘以数组中的最小数量获取最大优惠值
//                        }
//                    }
//                }
//            }
//        }
//
//        allMoney = totalOrderMoney;
//
//        totalFullSaleMoney = getSaleMoney(totalOrderMoney, 1);     //订单满减
//        vipTotalFullSaleMoney = getSaleMoney(vipTotalMoney, 2);     //订单满减
//
//        totalPayMoney = Arith.del(totalOrderMoney, Arith.add(totalSaleMoney, Arith.add(totalGroupMoney, totalFullSaleMoney)));    //计算最后的订单金额
//        totalPayMoney = totalPayMoney > 0 ? totalPayMoney : 0;
//
//        vipTotalPayMoney = Arith.del(totalOrderMoney, Arith.add(vipTotalSaleMoney, Arith.add(totalGroupMoney, vipTotalFullSaleMoney)));
//        vipTotalPayMoney = vipTotalPayMoney > 0 ? vipTotalPayMoney : 0;
//
//        totaldiscountPrice = Arith.add(totalSaleMoney, Arith.add(totalGroupMoney, totalFullSaleMoney));
//        vipTotalDiscountPrice = Arith.add(vipTotalSaleMoney, Arith.add(totalGroupMoney, vipTotalFullSaleMoney));
//
//        mGoodNumber.setText("" + ShopCart.nowShopCart.getAllCount());
//        youhui_total.setText("" + totalPayMoney);
//        tvVipTotalSale.setText(vipTotalDiscountPrice + "");
//        vipTotal.setText(vipTotalPayMoney + "");
//        tvTotalPrice.setText("¥ " + totalOrderMoney);//原价总金额
//        mGoodTotalPrice.setText("" + totalPayMoney);//应收
//        mPaidIn.setText("" + totalPayMoney);//实收
//
//        if (!saleItemText.equals("")) {
//            tvSaleText.setVisibility(View.VISIBLE);
//            tvSaleText.setText(Html.fromHtml("价格折扣：" + saleItemText));
//        }
//        if (!saleItemText2.equals("")) {
//            tvSaleText2.setVisibility(View.VISIBLE);
//            tvSaleText2.setText(Html.fromHtml("数量满减：" + saleItemText2));
//        }
//        if (!saleItemText3.equals("")) {
//            tvSaleText3.setVisibility(View.VISIBLE);
//            tvSaleText3.setText(Html.fromHtml("组合优惠：" + saleItemText3));
//        }
//        if (!saleText.equals("")) {
//            tvSaleText4.setVisibility(View.VISIBLE);
//            tvSaleText4.setText(Html.fromHtml("价格满减：" + saleText));
//        }
//
//        tvTotalSale.setText(totaldiscountPrice + "");
//
//        if (!Sunmi.viceScreenMode) {
//            try {
//                App.mSerialPortOperaion.WriteData(0xC);
//                String str = mGoodTotalPrice.getText().toString();
//                App.mSerialPortOperaion.WriteData(27, 81, 65);
//                App.mSerialPortOperaion.WriteData(Utils.input(str));
//                App.mSerialPortOperaion.WriteData(13);
//                App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X32);//总计
//            } catch (Exception e) {
//            }
//        }
//
//        try {
//            toPrice = totalPayMoney;
////            toPrice = tempTotalPricel;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (toPrice > 0) {
//            cashPayStatus(true);
//        } else {
//            cashPayStatus(false);
//        }
//    }


    //添加组合
    private void addGroupItem(ShopCart.ShopCartItem carItem, Map<Integer, List<Group>> longListMap) {
        List<Group> groups = longListMap.get(carItem.item.getActivityId());
        if (groups == null) {
            groups = new ArrayList<>();
            longListMap.put(carItem.item.getActivityId(), groups);
        }
        groups.add(new Group(carItem.item.getActivityId(), carItem.item.getId(), carItem.count, carItem.item.getSalesPrice()));
    }

    private void handlerActivityTry(Map<Long, Integer> nightPriceMap) {
        try {
            handlerActivity(nightPriceMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 处理活动
     */
    private void handlerActivity(Map<Long, Integer> nightPriceMap) {

        //组合，以活动ID为主
        Map<Integer, List<Group>> longListMap = new HashMap<>();

        //循环计算
        for (ShopCart.ShopCartItem carItem : ShopCart.nowShopCart.gets()) {

            carItem.initItemPrice();
            ShopCartItemPrice itemPrice = carItem.itemPrice;

            //商品价格
            double orderPrice = itemPrice.price;
            //数量
            itemPrice.count = carItem.count;

            //稳重价
            if (carItem.item.getItemType() == 2) {
                itemPrice.weigetPrice = Arith.mul(orderPrice, carItem.discount);
                //数量变称重
                itemPrice.count = carItem.weight;
            }

            //晚上打折的单价
            Integer nightPrice = null;
            if (nightPriceMap != null) {
                nightPrice = nightPriceMap.get(Long.parseLong(carItem.item.getId()));
            }
            //没有打折，或者打折是1000即原价
            if (nightPrice == null || nightPrice == 1000) {
                itemPrice.nightPrice = orderPrice;
            } else {
                itemPrice.nightPrice = Arith.mul(orderPrice, Arith.div(nightPrice.intValue(), 1000));
            }
            //商品直接优惠单价
            if (carItem.item.getState() == 1) {
                if (carItem.item.getActivityType() == 1) {
                    //商品 单品 折扣
                    itemPrice.couponPrice = Arith.del(orderPrice, carItem.item.getDiscount());
                } else if (carItem.item.getActivityType() == 2) {
                    //商品 组合 优惠
                    addGroupItem(carItem, longListMap);
                } else if (carItem.item.getActivityType() == 4) {
                    //商品 单品数量满减
                    itemPrice.fullPrice = Arith.div(Arith.mul(orderPrice, carItem.count) - carItem.item.getDiscount(), carItem.count);
                }
            }
        }

        List<GroupSale> nowSales = new ArrayList<>();

        //计算组合价
        String group_sale = SPUtil.getInstance().getString(mContext, "group_sale");

        if (longListMap.size() > 0 && !group_sale.equals("")) {
            List<GroupSale> groupSales = null;
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<GroupSale>>() {
                }.getType();
                groupSales = gson.fromJson(group_sale, listType);
            } catch (Exception ex) {

            }
            if (groupSales != null) {
                for (int i = 0; i < groupSales.size(); i++) {
                    GroupSale sale = groupSales.get(i);
                    List<Group> groups = longListMap.get(sale.getActivityId());
                    if (groups != null) {
                        //查看是否满足
                        List<String> saleItemIdSet = new ArrayList<>();
                        for (GroupSale.ItemsBean groupSale : sale.getItems()) {
                            saleItemIdSet.add(groupSale.getItemId() + "");
                        }
                        List<String> groupItemIdSet = new ArrayList<>();
                        for (Group g : groups) {
                            groupItemIdSet.add(g.getId());
                        }
                        Collections.sort(saleItemIdSet);
                        Collections.sort(groupItemIdSet);
                        //组合完全一样
                        if (saleItemIdSet.equals(groupItemIdSet)) {
                            nowSales.add(sale);
                        }
                    }
                }
            }
        }

        //原价
        double allMoney = 0;
        //总金额
        double totalPrice = 0;
        //优惠总计
        double discountMoney = 0;
        //会员优惠总计
        double vipDiscountMoney = 0;
        //应收
        double payMoney = 0;
        //会员应收
        double vipPayMoney = 0;

        int allCount = 0;

        //价格折扣
        StringBuilder zkSb = new StringBuilder();
        //组合优惠
        StringBuilder groupSb = new StringBuilder();
        //价格满减
        StringBuilder mjSb = new StringBuilder();
        //数量满减
        StringBuilder slSb = new StringBuilder();
        //晚上折扣
        StringBuilder wsSb = new StringBuilder();


        for (ShopCart.ShopCartItem carItem : ShopCart.nowShopCart.gets()) {
            carItem.itemPrice.handlerOrderPrice();

            //原价
            double itemMoney = Arith.mul(carItem.itemPrice.price, carItem.itemPrice.count);
            //应收
            double itemTotalPrice = Arith.mul(carItem.itemPrice.orderPrice, carItem.itemPrice.count);
            //优惠
            double itemDiscountMoney = Arith.mul(Math.abs(carItem.itemPrice.price - carItem.itemPrice.orderPrice), carItem.itemPrice.count);
            //会员优惠
            double itemVipDiscountMoney = Arith.mul(Math.abs(carItem.itemPrice.price - carItem.itemPrice.vipPrice), carItem.itemPrice.count);

            allMoney = Arith.add(allMoney, itemMoney);
            totalPrice = Arith.add(totalPrice, itemTotalPrice);
            discountMoney = Arith.add(discountMoney, itemDiscountMoney);
            vipDiscountMoney = Arith.add(vipDiscountMoney, Math.max(itemVipDiscountMoney, itemDiscountMoney));

            if (carItem.itemPrice.orderPriceType == 1) {
                if (wsSb.length() < 1) {
                    wsSb.append("到点折扣：");
                }
                wsSb.append(carItem.item.getItemName()).append(" <font color=\"red\"> - ").append(itemDiscountMoney).append("</font>");
            } else if (carItem.itemPrice.orderPriceType == 2) {
                if (zkSb.length() < 1) {
                    zkSb.append("价格折扣：");
                }
                zkSb.append(carItem.item.getItemName()).append(" <font color=\"red\"> - ").append(itemDiscountMoney).append("</font>");
            } else if (carItem.itemPrice.orderPriceType == 4) {
                if (slSb.length() < 1) {
                    slSb.append("数量满减：");
                }
                slSb.append(carItem.item.getItemName()).append(" <font color=\"red\"> - ").append(itemDiscountMoney).append("</font>");
            }

            PlaceOderData placeOderData = new PlaceOderData();
            placeOderData.setPrice(carItem.item.getSalesPrice() + "");
            placeOderData.setMenuId(carItem.item.getId());
            if (carItem.getType() == 2) {
                placeOderData.setCount(carItem.weight + "");
            } else {
                placeOderData.setCount(carItem.count + "");
            }
            placeOderData.setDiscountMoney(itemDiscountMoney + "");
            placeOderData.setTotal("" + itemMoney);
            placeOderData.setPromotionPrice("" + itemTotalPrice);
            placeOderData.setName(carItem.item.getItemName());
            placeOderData.setSalesPromotion("N");
            placeOderData.setUnit(carItem.item.getItemUnitName());
            placeOderData.setUnitId(carItem.item.getItemUnitId());

            allCount += carItem.count;

            list.add(placeOderData);
        }

        //组合满减
        if (nowSales != null && nowSales.size() > 0) {
            groupSb.append("组合优惠：");
            for (int i = 0; i < nowSales.size(); i++) {
                GroupSale sale = nowSales.get(i);
                discountMoney = Arith.add(discountMoney, sale.getDiscount());
                vipDiscountMoney = Arith.add(vipDiscountMoney, sale.getDiscount());
                groupSb.append(sale.getActivityName()).append(" <font color=\"red\"> - ").append(sale.getDiscount()).append("</font>");
            }
        }

        //订单满减
        SaleType3 saleType = getSaleMoneyEntity(payMoney);
        if (saleType != null) {
            mjSb.append("订单满减：").append("满 <font color=\"red\">").append(saleType.getPrice()).append("</font>")
                    .append(" 减 ").append("<font color=\"red\">").append(saleType.getDiscount()).append("</font>");

            discountMoney = Arith.add(discountMoney, saleType.getDiscount());
            vipDiscountMoney = Arith.add(vipDiscountMoney, saleType.getDiscount());
        }

        payMoney = Arith.del(allMoney, discountMoney);
        vipPayMoney = Arith.del(allMoney, vipDiscountMoney);

        if (zkSb.length() > 0) {
            tvSaleText.setVisibility(View.VISIBLE);
            tvSaleText.setText(Html.fromHtml(zkSb.toString()));
        }

        if (slSb.length() > 0) {
            tvSaleText2.setVisibility(View.VISIBLE);
            tvSaleText2.setText(Html.fromHtml(slSb.toString()));
        }
        if (groupSb.length() > 0) {
            tvSaleText3.setVisibility(View.VISIBLE);
            tvSaleText3.setText(Html.fromHtml(groupSb.toString()));
        }
        if (mjSb.length() > 0) {
            tvSaleText4.setVisibility(View.VISIBLE);
            tvSaleText4.setText(Html.fromHtml(mjSb.toString()));
        }
        if (wsSb.length() > 0) {
            tvSaleText7.setVisibility(View.VISIBLE);
            tvSaleText7.setText(Html.fromHtml(wsSb.toString()));
        }

        //原价
        _allMoney = allMoney;
        //总金额
        _totalPrice = totalPrice;
        //优惠总计
        _discountMoney = discountMoney;
        //会员优惠总计
        _vipDiscountMoney = vipDiscountMoney;
        //应收
        _payMoney = payMoney;
        //会员应收
        _vipPayMoney = vipPayMoney;

        mGoodNumber.setText(String.valueOf(allCount));

        refreshPriceTxt(1);
        mPaidIn.setText(String.valueOf(_payMoney));
    }


    //原价
    double _allMoney = 0;
    //总金额
    double _totalPrice = 0;
    //优惠总计
    double _discountMoney = 0;
    //会员优惠总计
    double _vipDiscountMoney = 0;
    //应收
    double _payMoney = 0;
    //会员应收
    double _vipPayMoney = 0;

    void refreshPriceTxt(double discount) {
        double ak = Arith.mul(_payMoney, Arith.del(1, discount));
        tvTotalPrice.setText(String.valueOf(Arith.mul(_totalPrice, discount)));
        tvTotalSale.setText(String.valueOf(Arith.add(ak, _discountMoney)));
        youhui_total.setText(String.valueOf(Arith.mul(_payMoney, discount)));
        tvVipTotalSale.setText(String.valueOf(Arith.add(ak, _vipDiscountMoney)));
        vipTotal.setText(String.valueOf(Arith.mul(_vipPayMoney, discount)));
        mGoodTotalPrice.setText(String.valueOf(Arith.mul(_payMoney, discount)));

        if (!Sunmi.viceScreenMode && App.mSerialPortOperaion != null) {
            try {
                App.mSerialPortOperaion.WriteData(0xC);
                String str = mGoodTotalPrice.getText().toString();
                App.mSerialPortOperaion.WriteData(27, 81, 65);
                App.mSerialPortOperaion.WriteData(Utils.input(str));
                App.mSerialPortOperaion.WriteData(13);
                App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X32);//总计
            } catch (Exception e) {
            }
        }

        if (_payMoney > 0) {
            cashPayStatus(true);
        } else {
            cashPayStatus(false);
        }
    }

    private SaleType3 getSaleMoneyEntity(double money) {
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
                return saleTypes.get(i);
            }
        }
        return null;
    }


    private double getSaleMoney(double money, int type) {
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
                if (type == 1) {
                    saleText += "满 <font color=\"red\">" + saleTypes.get(i).getPrice() + "</font> 减 <font color=\"red\">" + saleTypes.get(i).getDiscount() + "</font>";
                }
                return saleTypes.get(i).getDiscount();
            }
        }
        return 0;
    }


    /***
     * 键盘相关
     * @param matchValue
     * @return
     */
    boolean isNumber(StringBuilder matchValue) {
        //第一位是.
        if (matchValue.charAt(0) == '.') {
            //清空
            return false;
        }
        //第一位是0，第二位不是.
        if (matchValue.length() > 1) {
            if (matchValue.charAt(0) == '0') {
                char c = matchValue.charAt(1);
                if (c != '.') {
                    return false;
                }
            }
        }
        int count = 0;
        //多个.
        for (int i = 0; i < matchValue.length(); i++) {
            if (matchValue.charAt(i) == '.') {
                count++;
                if (count > 1) {
                    return false;
                }
            }
        }
        if (count > 1) {
            //清空
            return false;
        }
        return true;
    }

    /***
     * 键盘相关
     * @return
     */
    boolean handlerPriceText(char c) {
        if (builder.length() > 0 && Double.valueOf(builder.toString()) > 1000) {
            Toasty.error(mContext, "单笔最多不能超过1000元").show();
            return false;
        }
        this.builder.append(c);
        try {
            if (!isNumber(this.builder)) {
                this.builder.delete(this.builder.length() - 1, this.builder.length());
                double aPrice = Double.parseDouble(mGoodTotalPrice.getText().toString());
                double iPrice = Double.parseDouble(this.builder.toString());
                cashPayStatus(iPrice >= aPrice);
                return false;
            }
            //最后一个是点
            if (this.builder.charAt(this.builder.length() - 1) == '.') {
                cashPayStatus(false);
            } else {
                double aPrice = Double.parseDouble(mGoodTotalPrice.getText().toString());
                double iPrice = Double.parseDouble(this.builder.toString());
                cashPayStatus(iPrice >= aPrice);
            }
            mPaidIn.setText(builder);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /***
     * 判断是否0金额订单
     * @return
     */
    boolean checkZeroPay() {
        String price = mGoodTotalPrice.getText().toString();
        try {
            double d = Double.parseDouble(price);
            if (d <= 0) {
                ToastUtil.showToast(this, "0金额订单，请使用现金收款");
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    //创建订单
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void submitCart(final int payType) {
        try {
            if (BaseUtils.isEmpty(mPaidIn.getText().toString())) {
                ToastUtil.showEorr(mContext, "价格有误！");
                return;
            }

            dialog.show();
            List<ShopCart.ShopCartItem> cartList = ShopCart.nowShopCart.gets();

            //如果不允许负库存，则判断是否有超过库存的内容
            boolean stock = SPUtil.getInstance().getBoolean(mContext, "stock", true);
            if (!stock) {
                for (int i = 0; i < cartList.size(); i++) {
                    ShopCart.ShopCartItem carItem = cartList.get(i);
                    if (carItem.count > carItem.item.getRepertory()) {
                        dialog.cancel();
                        ToastUtil.showWrning(mContext, "当前不允许负库存，部分商品库存不足，无法下单");
                        return;
                    }
                }
            }

            double ak = Arith.mul(_payMoney, Arith.del(1, mDiscount));
            double temp_totalPrice = Arith.mul(_totalPrice, mDiscount);
            double temp_discountMoney = Arith.add(ak, _discountMoney);
            double temp_payMoney = Arith.mul(_payMoney, mDiscount);
            double temp_vipDiscountMoney = Arith.add(ak, _vipDiscountMoney);
            double temp_vipPayMoney = Arith.mul(_vipPayMoney, mDiscount);

            orderItemBeen = new ArrayList<>();
            double sumFreePrice = 0;
            for (int i = 0; i < cartList.size(); i++) {
                ItemSnapshotsBean bean = new ItemSnapshotsBean();
                ShopCart.ShopCartItem carItem = cartList.get(i);
                if (FreePriceGoodBean.FREE_BARCODE.equals(carItem.item.getBarcode())) {//自由价商品
                    sumFreePrice = Arith.add(sumFreePrice, carItem.count * carItem.item.getSalesPrice());
                } else {
                    bean.setItemType(carItem.type);
                    bean.setItemId(carItem.item.getId());
                    bean.setItemTemplateId(carItem.item.getItemTemplateId());
                    bean.setItemName(carItem.item.getItemName());
                    bean.setItemTypeId(carItem.item.getPosTypeId());
                    bean.setItemTypeName(carItem.item.getPosTypeName());
                    bean.setItemTypeUnitId(carItem.item.getItemUnitId());
                    bean.setItemTypeUnitName(carItem.item.getItemUnitName());
                    bean.setItemBarcode(carItem.item.getBarcode());
                    bean.setCostPrice(carItem.item.getStockPrice());
                    bean.setTotalPrice(carItem.item.getSalesPrice());
                    bean.setNormalPrice(carItem.item.getSalesPrice());
                    double count = 0;
                    // if (carItem.item.getItemType() == 2) {
                    count = carItem.count;
                    //  }
                    double saleMoney = 0;
                    bean.setNormalQuantity(carItem.count);
                    if (carItem.type == 2) {
                        bean.setNormalQuantity((int) Arith.mul(carItem.weight, 100));
                        if (carItem.discount < 10 && carItem.discount > 0) {
                            bean.setDiscountPrice((carItem.item.getSalesPrice() * carItem.discount) + "");
                            //                        bean.setDiscountQuantity(bean.getNormalQuantity() + "");
                            //                        bean.setNormalQuantity(0);
                        }
                    } else if (carItem.item.getState() == 1) {
                        switch (carItem.item.getActivityType()) {
                            case 1:
                                saleMoney = Arith.mul(Double.valueOf(Arith.del(carItem.item.getSalesPrice(), carItem.item.getDiscount())), count);
                                bean.setDiscountPrice(saleMoney + "");
                                break;
                            case 2:

                                break;
                            case 4:

                                break;
                        }
                    } else {

                        if (carItem.item.getMembershipPrice() > 0) {
                            bean.setDiscountPrice((carItem.item.getSalesPrice() - carItem.item.getMembershipPrice()) * carItem.count + "");
                            //                        bean.setDiscountQuantity(bean.getNormalQuantity() + "");
                            //                        bean.setNormalQuantity(0);
                        }
                    }


                    orderItemBeen.add(bean);
                }

            }
            if (sumFreePrice > 0) {//有自由价商品
                ItemSnapshotsBean bean = new ItemSnapshotsBean();
                GoodBean goodBean = App.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Barcode.eq("BBBBBBBBBBBBBB")).unique();
                if (goodBean == null) {
                    ToastUtil.showEorr(mContext, "自由价商品数据异常，请退出后重新登录！");
                    return;
                }
                bean.setItemId(goodBean.getId());
                bean.setItemType(1);
                bean.setItemTemplateId(goodBean.getItemTemplateId());
                bean.setItemName(goodBean.getItemName());
                bean.setItemTypeId(goodBean.getPosTypeId());
                bean.setItemTypeName(goodBean.getPosTypeName());
                bean.setItemTypeUnitId(goodBean.getItemUnitId());
                bean.setItemTypeUnitName(goodBean.getItemUnitName());
                bean.setItemBarcode(goodBean.getBarcode());
                bean.setCostPrice(sumFreePrice);
                bean.setTotalPrice(sumFreePrice);
                bean.setNormalPrice(sumFreePrice);
                bean.setNormalQuantity(1);
                orderItemBeen.add(bean);
            }
            if (payType == 4) {//1号生活扫码支付
                offCreateOrder(4);
                return;
            }

            double totalPrice = Double.valueOf(mPaidIn.getText().toString());
            //-double actualPrice = Double.valueOf(mGoodTotalPrice.getText().toString());
            //-double totaldiscPrice = totaldiscountPrice;

            HttpParams httpParams = new HttpParams();
            httpParams.put("orderType", 7);
            //2019-3-8
//            httpParams.put("actualPrice", actualPrice + "");//应收金额
//            httpParams.put("totalPrice", totalPrice + "");//实收金额
//            httpParams.put("totaldiscountPrice", totaldiscPrice);//优惠金额
//            httpParams.put("membershipPrice", vipTotalPayMoney);//会员金额
//            httpParams.put("mDiscount", mDiscount + "");//会员金额
            httpParams.put("actualPrice", temp_payMoney + "");//应收金额
            httpParams.put("totalPrice", totalPrice + "");//实收金额
            httpParams.put("totaldiscountPrice", temp_discountMoney);//优惠金额
            httpParams.put("membershipPrice", temp_vipPayMoney);//会员金额
            httpParams.put("mDiscount", temp_vipDiscountMoney + "");//会员金额
            //2019-3-8

            httpParams.put("phoneNumber", edMobilePhone.getText().toString());//手机号
            //0表示老；1表示中；2表示青
            httpParams.put("customerType", String.valueOf(customerType));
            httpParams.put("items", new Gson().toJson(orderItemBeen));

            OkGo.<NydResponse<CreateOrderSuccessBean>>post(Contonts.URL_CREATE_ORDER)
                    .tag(Contonts.URL_CREATE_ORDER)
                    .params(httpParams)
                    .execute(new JsonCallback<NydResponse<CreateOrderSuccessBean>>() {
                        @Override
                        public void onSuccess(Response<NydResponse<CreateOrderSuccessBean>> response) {
                            try {
                                dialog.cancel();
                                pay(payType, response.body().response.getOrderId());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Response<NydResponse<CreateOrderSuccessBean>> response) {
                            super.onError(response);
                            try {
                                dialog.cancel();
                                if (payType == 3) {//网络下单失败，现金方式调用离线下单
                                    if (orderItemBeen.size() < 1) {
                                        Toasty.error(mContext, "数据异常，请重新下单！").show();
                                        finish();
                                    } else {
                                        offCreateOrder(payType);
                                    }
                                } else {
                                    ToastUtil.showEorr(mContext, "数据异常，请使用现金支付！");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    public void offCreateOrder(int payType) {

        double ak = Arith.mul(_payMoney, Arith.del(1, mDiscount));
        double temp_totalPrice = Arith.mul(_totalPrice, mDiscount);
        double temp_discountMoney = Arith.add(ak, _discountMoney);
        double temp_payMoney = Arith.mul(_payMoney, mDiscount);
        double temp_vipDiscountMoney = Arith.add(ak, _vipDiscountMoney);
        double temp_vipPayMoney = Arith.mul(_vipPayMoney, mDiscount);

        final FinishOrderData finishOrderData = new FinishOrderData();
        //2019-3-8
//        finishOrderData.setTotalPrice(totalPayMoney);
//        finishOrderData.setDiscountPrice(totaldiscountPrice);
//        finishOrderData.setMembershipPrice(vipTotalPayMoney);
        finishOrderData.setTotalPrice(temp_payMoney);
        finishOrderData.setDiscountPrice(temp_discountMoney);
        finishOrderData.setMembershipPrice(temp_vipPayMoney);
        //2019-3-8
        finishOrderData.setActualPrice(Double.valueOf(mPaidIn.getText().toString()));
        finishOrderData.setPassportId(app.getCurrentUser().getPassportId());
        finishOrderData.setItemSnapshots(orderItemBeen);
        finishOrderData.setId(UUID.randomUUID().toString());
        finishOrderData.setCreateTime(System.currentTimeMillis());
        if (payType == 3) {//离线订单只有扫码跟现金会调用
            finishOrderData.setTransType("现金支付");
            finishOrderData.setPaymentType("CASH");
        } else if (payType == 4) {
            finishOrderData.setTransType("1号生活扫码");
            finishOrderData.setPaymentType("UNKNOWN");
        }
        finishOrderData.setPaymentTime(System.currentTimeMillis());
        finishOrderData.setSequenceNumber(UUID.randomUUID().toString());
        finishOrderData.setMobilePhone(edMobilePhone.getText().toString());

        final long offId = app.getDaoInstant().getFinishOrderDataDao().insertOrReplace(finishOrderData);
        if (offId > 0) {//创建离线订单成功
            //存入商品列表
            Observable.from(orderItemBeen).map(new Func1<ItemSnapshotsBean, ItemSnapshotsBean>() {
                @Override
                public ItemSnapshotsBean call(ItemSnapshotsBean itemSnapshotsBean) {
                    itemSnapshotsBean.setOffLineOrderId(offId);
                    itemSnapshotsBean.setLineId(finishOrderData.getId());
                    return itemSnapshotsBean;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .toList().subscribe(new Action1<List<ItemSnapshotsBean>>() {
                @Override
                public void call(List<ItemSnapshotsBean> itemSnapshotsBeen) {
                    try {
                        app.getDaoInstant().getItemSnapshotsBeanDao().insertOrReplaceInTx(orderItemBeen);
                        SupermarketIndexActivity.doSyncOffOder();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    if(mDiscount>0){
//                        EventBus.getDefault().post(new StatisticsEvent(finishOrderData.getSequenceNumber(),Double.valueOf(mGoodTotalPrice.getText().toString()),Double.valueOf(mPaidIn.getText().toString()),totaldiscountPrice,mDiscount));
//                    }
                }
            });
            cashOk(finishOrderData, payType);
        }


    }

    /***
     * 调起扫码支付页面
     * @param orderId
     * @param paytype  1为微信，2为支付宝，3为现金
     */
    public void pay(int paytype, final String orderId) {
        try {
            Intent intent = new Intent(mContext, PayActivity.class);
            intent.putExtra("payType", paytype);
            intent.putExtra("orderId", orderId);
            intent.putExtra("couponId", couponId);
            //2019-3-8
//            intent.putExtra("actualPrice", Double.valueOf(mGoodTotalPrice.getText().toString()));
//            intent.putExtra("totalPrice", allMoney);
//            intent.putExtra("totaldiscountPrice", totaldiscountPrice);//优惠金额
//            intent.putExtra("discountPercent", mDiscount);
            intent.putExtra("actualPrice", _payMoney);
            intent.putExtra("totalPrice", _allMoney);
            intent.putExtra("totaldiscountPrice", _discountMoney);//优惠金额
            intent.putExtra("discountPercent", _vipDiscountMoney);
            //2019-3-8
            intent.putExtra("phoneNumber", edMobilePhone.getText().toString());//手机号
            intent.putExtra(Contonts.PRICE_SHOULD, mGoodTotalPrice.getText().toString());
            intent.putExtra(Contonts.PRICE_ACTURAL, mPaidIn.getText().toString());
            startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    //现金离线支付成功
    void cashOk(FinishOrderData finishOrderData, int payType) {
        try {
            if (payType == 4) {
                if (payVoice) {
                    SupermarketIndexActivity.speak("支付成功");
                }
                //            SupermarketIndexActivity.soundPool.play(SupermarketIndexActivity.SOUNDID_PAYSUCCESS,1, 1, 0, 0, 1);
            } else {
                LogUtils.e(finishOrderData.getChange());
                if (finishOrderData.getChange() > 0) {
                    if (payVoice) {
                        SupermarketIndexActivity.speak("现金收款" + finishOrderData.getActualPrice() + "元，找零" + finishOrderData.getChange() + "元");
                    }
                } else {
                    if (payVoice) {
                        SupermarketIndexActivity.speak("现金收款" + finishOrderData.getActualPrice() + "元");
                    }
                }
            }

            View toastLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_my_taost, null);
            ToastUtil.custom(mContext, toastLayout, Toast.LENGTH_LONG).show();

            //刷新
            try {
                ScreenUtil.getInstance().payActvity(finishOrderData, textDisplay);
                ScreenUtil.getInstance().waitShowScreen(imageDisplay);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //if(!offline) {
                Printer.outcomeTacket(finishOrderData);//打印小票
                //}
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
                e.printStackTrace();
            }
            if (payType == 3) {
                //钱箱
                try {
                    HdxUtil.SetV12Power(0);
                    HdxUtil.SetV12Power(1);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
                try {
                    OpenDrawers.openMoneyBox();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //清空
            Intent intent = new Intent();
            intent.putExtra("data", new Gson().toJson(finishOrderData));

            setResult(SupermarketIndexActivity.requestCode_PAY_RESULT, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SupermarketIndexActivity.requestCode_PAY_RESULT) {
            //直接关闭自己，并且将data传到父activity
            setResult(resultCode, data);
            finish();
        }
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput() {
        if (Build.VERSION.SDK_INT <= 10) {
            mPaidIn.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(mPaidIn, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(mPaidIn, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ivUnitPay.setEnabled(true);
        ivVipPay.setEnabled(true);
        ivCashPay.setEnabled(true);
        ivScanPay.setEnabled(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ScreenUtil.getInstance().showScreen();
        SupermarketIndexActivity.closeDialog();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(Contonts.URL_CREATE_ORDER);
    }

    /**
     * 限制回车换行
     *
     * @param et
     */
    public static void LimitsEditEnter(EditText et) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }

    /**
     * 检测优惠券是否可用
     *
     * @param code
     */
    public void checkCode(String code) {

//        OkGo.<NydResponse<CouponBean>>post(Contonts.CHECK_COUPON).params("code", code)
//                .execute(new DialogCallback<NydResponse<CouponBean>>(mContext) {
//                    @Override
//                    public void onSuccess(final Response<NydResponse<CouponBean>> response) {
//                        final double money = response.body().response.getAmount();
//                        if (money >= totalPayMoney) {
//                            ToastUtil.showToast(mContext, "优惠券优惠金额高于订单金额，不可用");
//                            return;
//                        }
//                        confirmDialog = new ConfirmDialog(mContext, "此优惠券可用，优惠券会在订单价格基础上减去优惠金额，订单完成后此优惠券将不可用，确认添加么？", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                couponId = response.body().response.getId();
//                                etCode.setEnabled(false);
//                                btnAddCode.setEnabled(false);
//                                etCode.setBackgroundResource(R.drawable.bg_corner_gray);
//                                btnAddCode.setBackgroundResource(R.drawable.bg_corner_gray);
//                                btnAddCode.setText("已添加");
//                                String text = "<font color=\"red\"> - " + money + "</font>";
//                                tvSaleText5.setVisibility(View.VISIBLE);
//                                tvSaleText5.setText(Html.fromHtml("优惠券折扣：" + text));
//
//                                totaldiscountPrice = Arith.add(totaldiscountPrice, money);
//                                totalPayMoney = Arith.del(totalPayMoney, money);
//
//                                vipTotalDiscountPrice = Arith.add(vipTotalDiscountPrice, money);
//                                vipTotalPayMoney = Arith.del(vipTotalPayMoney, money);
//
//                                tvTotalSale.setText(totaldiscountPrice + "");
//                                youhui_total.setText("" + totalPayMoney);
//                                vipTotal.setText(vipTotalPayMoney + "");
//                                tvVipTotalSale.setText(vipTotalDiscountPrice + "");
//                                mGoodTotalPrice.setText("" + totalPayMoney);//应收
//                                mPaidIn.setText("" + totalPayMoney);//实收
//
//                                scanPayStatus(false);
//
//                                Sunmi.showCardImg(totaldiscountPrice, vipTotalDiscountPrice, imageMenuDisplay);
//                                confirmDialog.dismiss();
//                            }
//                        });
//                        confirmDialog.show();
//                    }
//                });


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
            return super.dispatchKeyEvent(event);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return super.dispatchKeyEvent(event);
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            return false;
        } else {
            scanGunKeyEventHelper.analysisKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onScanSuccess(String barcode) {
        if (couponId.equals("")) {
            etCode.getText().clear();
            etCode.setText(barcode);
        }
    }

    @OnClick({R.id.iv_unit_pay, R.id.iv_cash_pay, R.id.iv_vip_pay, R.id.iv_scan_pay, R.id.comeback, R.id.btn_add_code, R.id.btn_discount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_unit_pay:
                if (ButtonUtils.isFastDoubleClick(R.id.Wechat)) {
                    ivVipPay.setEnabled(false);
                    ivCashPay.setEnabled(false);
                    ivScanPay.setEnabled(false);
                    Log.e("mWechat", "mWechat");
                } else {
                    if (!Http.isNetworkConnected(mContext)) {
                        ToastUtil.showToast(mContext, "联网失败，请使用1号生活或者现金支付");
                        return;
                    }
                    if (checkZeroPay()) {
                        return;
                    }
                    MobclickAgent.onEvent(mContext, "unitpay");
                    submitCart(PAYTYPE[5]);
                }
                break;
            case R.id.iv_cash_pay:
                if (!Sunmi.viceScreenMode) {
                    try {
                        App.mSerialPortOperaion.WriteData(0xC);
                        String str = mPaidIn.getText().toString();
                        App.mSerialPortOperaion.WriteData(27, 81, 65);
                        App.mSerialPortOperaion.WriteData(Utils.input(str));
                        App.mSerialPortOperaion.WriteData(13);
                        App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X33);//收款
                    } catch (Exception e) {
                    }
                }
                MobclickAgent.onEvent(mContext, "cash");
                submitCart(PAYTYPE[2]);

                break;
            case R.id.iv_vip_pay:
                if (ButtonUtils.isFastDoubleClick(R.id.rl_vip_pay)) {
                    ivUnitPay.setEnabled(false);
                    ivCashPay.setEnabled(false);
                    ivScanPay.setEnabled(false);
                } else {
                    if (!Http.isNetworkConnected(mContext)) {
                        ToastUtil.showToast(mContext, "联网失败，请使用1号生活或者现金支付");
                        return;
                    }
                    if (checkZeroPay()) {
                        return;
                    }
                    MobclickAgent.onEvent(mContext, "alipay");
                    submitCart(PAYTYPE[4]);

                }
                break;
            case R.id.iv_scan_pay:
                MobclickAgent.onEvent(mContext, "scanpay");
                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_scan_pay, null);
                final Dialog dialog = StyledDialog.buildCustom(v, Gravity.CENTER).show();
                (v.findViewById(R.id.btn_pay)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        submitCart(PAYTYPE[3]);

                    }
                });
                (v.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                (v.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                break;
            case R.id.comeback:
                finish();
                break;
            case R.id.btn_add_code:
                String code = etCode.getText().toString().trim();
                if (!couponId.equals("")) {
                    ToastUtil.showToast(mContext, "已经添加优惠券");
                    return;
                }
                if (code.equals("")) {
                    ToastUtil.showToast(mContext, "优惠券码不能为空");
                    return;
                }
                checkCode(code);
                break;
            case R.id.btn_discount:
                discountDialog = new DiscountDialog(mContext, "总价折扣", "", mDiscount, new DiscountDialog.PriorityListener() {
                    @Override
                    public void refreshPriorityUI(double discount) {
                        if (discount == 0) {
                            mDiscount = 1;
                            refreshPriceTxt(1);
                            btnDiscount.setText("总价折扣");
                            btnDiscount.setBackgroundResource(R.drawable.bg_corner_theme);
                            tvSaleText6.setVisibility(View.GONE);
                        } else {
                            double z = Arith.div(discount, 10);
                            mDiscount = z;
                            refreshPriceTxt(z);
                            btnDiscount.setText(discount + "折");
                            btnDiscount.setBackgroundResource(R.drawable.bg_nomal_green);
                            tvSaleText6.setVisibility(View.VISIBLE);

                            double discountMoney = Arith.mul(_payMoney, Arith.del(1, z));
                            String text = "<font color=\"red\"> - " + discountMoney + "</font>";
                            tvSaleText6.setText(Html.fromHtml("总价折扣：" + text));
                        }

//                        if (discount == 0) {
//                            discount = 10.0;
//                        }
//                        if (mDiscount == 0) {
//                            oTotaldiscountPrice = totaldiscountPrice;
//                            oTotalPayMoney = totalPayMoney;
//
//                            oVTotaldiscountPrice = vipTotalDiscountPrice;
//                            oVTotalPayMoney = vipTotalPayMoney;
//                            btnDiscount.setText("不打折");
//                            btnDiscount.setBackgroundResource(R.drawable.bg_blue_btn);
//                        }
//
//                        if (etCode.getText().toString().equals("")) {
//                            etCode.setEnabled(false);
//                            btnAddCode.setEnabled(false);
//                            etCode.setText("打折后不能使用优惠券");
//                            etCode.setBackgroundResource(R.drawable.bg_corner_gray);
//                            btnAddCode.setBackgroundResource(R.drawable.bg_corner_gray);
//                        }
//
//                        mDiscount = discount;
//
//                        double discountMoney = Arith.mul(oTotalPayMoney, Arith.del(1, Arith.div(mDiscount, 10)));
//                        double vDiscountMoney = Arith.mul(oVTotalPayMoney, Arith.del(1, Arith.div(mDiscount, 10)));
//                        String text = "<font color=\"red\"> - " + discountMoney + "</font>";
//                        tvSaleText6.setVisibility(View.VISIBLE);
//                        tvSaleText6.setText(Html.fromHtml("总价折扣：" + text));
//
//                        totaldiscountPrice = Arith.add(oTotaldiscountPrice, discountMoney);
//                        totalPayMoney = Arith.del(oTotalPayMoney, discountMoney);
//
//                        vipTotalDiscountPrice = Arith.add(oVTotaldiscountPrice, vDiscountMoney);
//                        vipTotalPayMoney = Arith.del(oVTotalPayMoney, vDiscountMoney);
//
//                        tvTotalSale.setText(totaldiscountPrice + "");
//                        youhui_total.setText("" + totalPayMoney);
//                        vipTotal.setText(vipTotalPayMoney + "");
//                        tvVipTotalSale.setText(vipTotalDiscountPrice + "");
//                        mGoodTotalPrice.setText("" + totalPayMoney);//应收
//                        mPaidIn.setText("" + totalPayMoney);//实收
//
//                        btnDiscount.setText(mDiscount + "折");
//                        btnDiscount.setBackgroundResource(R.drawable.bg_nomal_green);

                        Sunmi.showCardImg(_discountMoney, _vipDiscountMoney, imageMenuDisplay);
                    }
                });
                discountDialog.show();
                break;
            default:
        }
    }

    @Override
    public void onNumberReturn(String number) {
        handlerPriceText(number.charAt(0));

    }

    @Override
    public void onNumberDelete() {
        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }
        mPaidIn.setText(builder);
        if (builder.length() > 0) {
            double aPrice = Double.parseDouble(mGoodTotalPrice.getText().toString());
            double iPrice = Double.parseDouble(builder.toString());
            cashPayStatus(iPrice >= aPrice);
        } else {
            cashPayStatus(false);
        }
//        mPaidIn.setSelection(builder.length());
    }

    private void cashPayStatus(boolean status) {
        ivCashPay.setEnabled(status);
        if (status) {
            ivCashPay.setImageResource(R.mipmap.ic_cash_pay);
        } else {
            ivCashPay.setImageResource(R.mipmap.ic_cash_pay_gray);
        }
    }

    private void unitPayStatus(boolean status) {
        ivUnitPay.setEnabled(status);
        if (status) {
            ivUnitPay.setImageResource(R.mipmap.ic_unit_pay);
        } else {
            ivUnitPay.setImageResource(R.mipmap.ic_unit_pay_gray);
        }
    }

    private void vipPayStatus(boolean status) {
        ivVipPay.setEnabled(status);
        if (status) {
            ivVipPay.setImageResource(R.mipmap.ic_vip_pay);
        } else {
            ivVipPay.setImageResource(R.mipmap.ic_vip_pay_gray);
        }
    }

    private void scanPayStatus(boolean status) {
        ivScanPay.setEnabled(status);
        if (status) {
            ivScanPay.setImageResource(R.mipmap.ic_scan_pay);
        } else {
            ivScanPay.setImageResource(R.mipmap.ic_scan_pay_gray);
        }
    }

    private void initScreen() {
        screenManager.init(this);
        Display[] displays = screenManager.getDisplays();
        if (displays.length > 1) {
            imageMenuDisplay = new ImageMenuDisplay(this, displays[1], SPUtil.getInstance().getString(mContext, "payCode"));
            textDisplay = new TextDisplay(this, displays[1]);
            imageDisplay = new ImageDisplay(this, displays[1]);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_age_a) {
            customerType = 0;
            changeAgeButton(tv_age_a, tv_age_b, tv_age_c);
        } else if (id == R.id.tv_age_b) {
            customerType = 1;
            changeAgeButton(tv_age_b, tv_age_a, tv_age_c);
        } else if (id == R.id.tv_age_c) {
            customerType = 2;
            changeAgeButton(tv_age_c, tv_age_b, tv_age_a);
        }
    }

    void changeAgeButton(TextView select, TextView a, TextView b) {
        select.setBackgroundResource(R.drawable.old_shape_fare_bg_press);
        a.setBackgroundResource(R.drawable.old_shape_fare_bg_normal);
        b.setBackgroundResource(R.drawable.old_shape_fare_bg_normal);
    }
}
