package com.gzdb.sale.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.baidu.tts.tools.DateTool;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.sale.adapter.SaleProductAdapter;
import com.gzdb.sale.bean.SaleInfo;
import com.gzdb.sale.bean.SaleProduct;
import com.gzdb.sale.bean.SelectProduct;
import com.gzdb.sale.event.RefreshPriceEvent;
import com.gzdb.sale.event.RefreshSaleEvent;
import com.gzdb.sale.event.SelectProductEvent;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class EditSaleActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.et_sale_name)
    EditText etSaleName;
    @Bind(R.id.ll_products)
    LinearLayout llProducts;
    @Bind(R.id.btn_add_product)
    Button btnAddProduct;
    @Bind(R.id.ll_rule)
    LinearLayout llRule;
    @Bind(R.id.tv_desc1)
    TextView tvDesc1;
    @Bind(R.id.et_value1)
    EditText etValue1;
    @Bind(R.id.tv_desc2)
    TextView tvDesc2;
    @Bind(R.id.et_value2)
    EditText etValue2;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_end)
    TextView tvEnd;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.tv_sale_price)
    TextView tvSalePrice;
    @Bind(R.id.btn_submit)
    Button btnSubmit;

    private String id;
    private int type;
    private int timeType = 1;
    private String method;
    private String desc1 = "", desc2 = "", hint1 = "", hint2 = "";

    public static final DateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private SaleProductAdapter adapter;
    private List<SaleProduct> products = new ArrayList<>();
    private List<Double> prices = new ArrayList<>();

    private String dateArray = "";

    private SaleInfo saleInfo;

    private double totalPrice = 0.00;

    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sale);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 1);
        method = getIntent().getStringExtra("method");

        if (method.equals("add")) {
            textTitle.setText("添加优惠");
            btnSubmit.setText("创建活动");
        } else {
            btnSubmit.setText("修改活动");
            textTitle.setText("编辑优惠");
            getInfo();
        }

        switch (type) {
            case 1:
                llRule.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
                tvSalePrice.setVisibility(View.VISIBLE);
                break;
            case 2:
                etValue1.setEnabled(false);
                etValue1.setBackgroundResource(R.drawable.bg_corner_gray);
                desc1 = "商品组合总价";
                desc2 = "优惠立减";
                hint1 = "商品组合总价";
                hint2 = "优惠立减";
                break;
            case 3:
                llProducts.setVisibility(View.GONE);
                btnAddProduct.setVisibility(View.GONE);
                desc1 = "订单总价≥";
                desc2 = "价格满减";
                hint1 = "订单总价";
                hint2 = "满减金额";
                break;
            case 4:
                desc1 = "购买数量≥";
                desc2 = "优惠立减";
                hint1 = "商品数量";
                hint2 = "优惠金额";
                break;
        }

        tvDesc1.setText(desc1);
        tvDesc2.setText(desc2);
        etValue1.setHint(hint1);
        etValue2.setHint(hint2);

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        startDate.set(2019, 0, 1);
        endDate.set(2020, 11, 31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (timeType == 0) {
                    tvStart.setText(sSimpleDateFormat.format(date));
                } else {
                    tvEnd.setText(sSimpleDateFormat.format(date));
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setSubmitColor(getResources().getColor(R.color.blue))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.gray1))//取消按钮文字颜色
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();

        adapter = new SaleProductAdapter(mContext, products, type);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
    }

    @OnClick({R.id.img_back, R.id.btn_add_product, R.id.tv_start, R.id.tv_end, R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_add_product:
                startActivity(new Intent(EditSaleActivity.this, SelectProductActivity.class));
                break;
            case R.id.tv_start:
                timeType = 0;
                if (method.equals("edit")) {
                    Date date = new Date(saleInfo.getStartTime());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    pvTime.setDate(c);
                }
                pvTime.show();
                break;
            case R.id.tv_end:
                timeType = 1;
                if (method.equals("edit")) {
                    Date date = new Date(saleInfo.getEndTime());
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    pvTime.setDate(c);
                }
                pvTime.show();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_submit:
                if (type != 3 && adapter.getmProducts().size() == 0) {
                    ToastUtil.showWrning(mContext, "请选择活动商品");
                    return;
                }
                if (type == 2 && adapter.getmProducts().size() < 2) {
                    ToastUtil.showWrning(mContext, "至少选择2个活动商品");
                    return;
                }
                List<SaleProduct> salePros = adapter.getmProducts();
                LogUtils.e(salePros);
                salePros = removeDuplicateProduct(salePros);
                LogUtils.e(salePros);
                dateArray = "";
                for (int i = 0; i < salePros.size(); i++) {
                    SaleProduct product = salePros.get(i);
                    if (type == 1 && product.getDiscount().equals("")) {
                        ToastUtil.showWrning(mContext, "请先设置优惠价格");
                        return;
                    }
                    if (i == adapter.getmProducts().size() - 1) {
                        if (type == 1) {
                            dateArray += product.getItemId() + ":" + product.getDiscount();
                        } else {
                            dateArray += product.getItemId();
                        }
                    } else {
                        if (type == 1) {
                            dateArray += product.getItemId() + ":" + product.getDiscount() + "-->";
                        } else {
                            dateArray += product.getItemId() + "-->";
                        }
                    }
                    prices.add(Double.valueOf(product.getSalesPrice()));
                }
                LogUtils.e(dateArray);
                addSafe();
                break;
        }
    }

    private void addSafe() {
        String name = etSaleName.getText().toString().trim();
        String start = tvStart.getText().toString().trim();
        String end = tvEnd.getText().toString().trim();
        String value1 = etValue1.getText().toString().trim();
        String value2 = etValue2.getText().toString().trim();

        if (name.equals("")) {
            ToastUtil.showWrning(mContext, "请输入活动名称");
            return;
        }
        if (value1.equals("")) {
            ToastUtil.showWrning(mContext, "请输入" + hint1);
            return;
        }
        if (value2.equals("")) {
            ToastUtil.showWrning(mContext, "请输入" + hint2);
            return;
        }

        if (start.equals("")) {
            ToastUtil.showWrning(mContext, "请输入活动开始时间");
            return;
        }
        if (end.equals("")) {
            ToastUtil.showWrning(mContext, "请输入活动结束时间");
            return;
        }
        if (!timeCompare(strToDate(start), strToDate(end))) {
            ToastUtil.showWrning(mContext, "结束时间必须大于开始时间");
            return;
        }
//        if (timeCompare(strToDate(start), new Date())) {
//            ToastUtil.showWrning(mContext, "开始时间必须大于当前时间");
//            return;
//        }
        if (timeCompare(strToDate(end), new Date())) {
            ToastUtil.showWrning(mContext, "结束时间必须大于当前时间");
            return;
        }
        if (type != 1 && 0 > Double.valueOf(value2)) {
            ToastUtil.showWrning(mContext, "优惠金额必须大于0");
            return;
        }
        if (type == 4 && 0 > Double.valueOf(value1)) {
            ToastUtil.showWrning(mContext, "商品数量必须大于0");
            return;
        }
        if (type == 2 && Double.valueOf(value2) >= Double.valueOf(value1)) {
            ToastUtil.showWrning(mContext, "优惠金额必须小于商品总价");
            return;
        }
        if (type == 3 && Double.valueOf(value2) >= Double.valueOf(value1)) {
            ToastUtil.showWrning(mContext, "优惠金额必须小于订单总额");
            return;
        }
        if (type == 4) {
            double price = Collections.min(prices);
            if (Double.valueOf(value2) >= Arith.mul(price, Double.valueOf(value1))) {
                ToastUtil.showWrning(mContext, "优惠金额必须小于" + Arith.mul(price, Double.valueOf(value1)));
                return;
            }
        }
        OkGo.<NydResponse<String>>post(Contonts.ADD_SALE)
                .tag(getClass().getSimpleName())
                .params("id", id)
                .params("activityName", name)
                .params("startTimeString", start)
                .params("endTimeString", end)
                .params("activityType", type)
                .params("price", value1)
                .params("discount", value2)
                .params("dateArray", dateArray)
                .params("deleteArray", getDelete())
                .execute(new DialogCallback<NydResponse<String>>(mContext) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<String>> response) {
                        ToastUtil.showSuccess(mContext, response.body().msg);
                        EventBus.getDefault().post(new RefreshSaleEvent());
                        finish();
                    }
                });
    }

    private void getInfo() {
        OkGo.<NydResponse<SaleInfo>>post(Contonts.SALE_INFO)
                .tag(getClass().getSimpleName())
                .params("activityId", id)
                .execute(new DialogCallback<NydResponse<SaleInfo>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<SaleInfo>> response) {
                        saleInfo = response.body().response;
                        products.addAll(response.body().response.getItems());
                        adapter.notifyDataSetChanged();
                        etSaleName.setText(saleInfo.getActivityName());
                        etValue2.setText(saleInfo.getDiscount());
                        tvStart.setText(DateTool.format(saleInfo.getStartTime(), "yyyy-MM-dd HH:mm"));
                        tvEnd.setText(DateTool.format(saleInfo.getEndTime(), "yyyy-MM-dd HH:mm"));
                        if (type == 2) {
                            totalPrice = 0;
                            for (int i = 0; i < response.body().response.getItems().size(); i++) {
                                totalPrice = Arith.add(totalPrice, Double.valueOf(response.body().response.getItems().get(i).getSalesPrice()));
                            }
                            etValue1.setText(totalPrice + "");
                        } else {
//                            totalPrice = Double.valueOf(saleInfo.getPrice());
                            etValue1.setText(saleInfo.getPrice());
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectProduct(SelectProductEvent event) {
        List<SelectProduct> selectProducts = event.getProducts();
        LogUtils.e(selectProducts);
        for (int i = 0; i < selectProducts.size(); i++) {
            SelectProduct selectProduct = selectProducts.get(i);
            /**
             * 防止重复插入
             */
            boolean flag = false;
            for (int j = 0; j < products.size(); j++) {
                if (products.get(j).getItemId() == selectProduct.getItemId()) {
                    flag = true;
                }
            }
            if (!flag) {
                SaleProduct product = new SaleProduct();
                product.setItemId(selectProduct.getItemId());
                product.setItemName(selectProduct.getItemName());
                product.setBarcode(selectProduct.getBarcode());
                product.setStockPrice(selectProduct.getStockPrice());
                product.setSalesPrice(selectProduct.getSalesPrice());
                product.setDiscount("");
                products.add(product);
                totalPrice = Arith.add(totalPrice, Double.valueOf(selectProduct.getSalesPrice()));
            }
        }
        if (type != 4) {
            etValue1.setText(totalPrice + "");
        }
        adapter.notifyDataSetChanged();
    }

    private String getDelete() {
        String dataArray = "";
        List<SaleProduct> list = adapter.getDeleteProducts();
        if (list.size() == 0) {
            return dataArray;
        }
        LogUtils.e(list);
        if (saleInfo != null) {
            list.retainAll(saleInfo.getItems());
        }
        list = removeDuplicateProduct(list);
        LogUtils.e(list);
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                dataArray += list.get(i).getItemId();
            } else {
                dataArray += list.get(i).getItemId() + "-->";
            }
        }

        return dataArray;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPriceEvent(RefreshPriceEvent event) {
        getPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getPrice() {
        List<SaleProduct> salePros = adapter.getmProducts();
        LogUtils.e(salePros);
        salePros = removeDuplicateProduct(salePros);
        totalPrice = 0;
        for (SaleProduct saleProduct : salePros) {
            totalPrice = Arith.add(totalPrice, Double.valueOf(saleProduct.getSalesPrice()));
        }
        etValue1.setText(totalPrice + "");
    }

    public static boolean timeCompare(Date date1, Date date2) {
        return date2.compareTo(date1) == 1 ? true : false;
    }

    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sSimpleDateFormat.parse(strDate, pos);
        return strtodate;
    }

    private static ArrayList<SaleProduct> removeDuplicateProduct(List<SaleProduct> users) {
        Set<SaleProduct> set = new TreeSet<SaleProduct>(new Comparator<SaleProduct>() {
            @Override
            public int compare(SaleProduct o1, SaleProduct o2) {
                //字符串,则按照asicc码升序排列
                return (o1.getItemId() + "").compareTo(o2.getItemId() + "");
            }
        });
        set.addAll(users);
        return new ArrayList<SaleProduct>(set);
    }


}
