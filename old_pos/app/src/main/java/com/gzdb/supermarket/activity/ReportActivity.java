package com.gzdb.supermarket.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.ReportAdapter;
import com.gzdb.supermarket.been.DataBean;
import com.gzdb.supermarket.been.ItemType;
import com.gzdb.supermarket.been.ReportResultBean;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.dialog.SendMailDialog;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.yct.util.DateUtil;
import com.lzy.okgo.OkGo;
import com.xw.repo.XEditText;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ReportActivity extends BaseActivity implements ScanGunKeyEventHelper.OnScanSuccessListener {

    @Bind(R.id.classification)
    XCDropDownListView classification;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_end)
    TextView tvEnd;
    @Bind(R.id.et_condition)
    XEditText etCondition;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.tv_sale_num)
    TextView tvSaleNum;
    @Bind(R.id.tv_total_money)
    TextView tvTotalMoney;
    @Bind(R.id.tv_profit)
    TextView tvProfit;
    @Bind(R.id.iv_sale)
    ImageView ivSale;
    @Bind(R.id.iv_profit)
    ImageView ivProfit;
    @Bind(R.id.iv_rate)
    ImageView ivRate;
    @Bind(R.id.btn_product1)
    Button btnProduct1;
    @Bind(R.id.btn_product2)
    Button btnProduct2;
    @Bind(R.id.ll_profit)
    LinearLayout llProfit;
    @Bind(R.id.ll_rate)
    LinearLayout llRate;
    @Bind(R.id.tv_sale)
    TextView tvSale;
    @Bind(R.id.tv_desc)
    TextView tvDesc;

    private ReportAdapter adapter;
    private List<ReportResultBean.DatasBean> goodsList = new ArrayList<>();
    private SendMailDialog dialog;

    private int timeType = 1;
    private static final DateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TimePickerView pvTime;

    private int sale = 1;
    private int profit = 0;
    private int rate = 0;

    String typeId = "";
    String itemName = "";
    String start = "";
    String end = "";

    private int itemType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

//        if(!Build.MODEL.equals("S2")){
//            btnProduct1.setVisibility(View.GONE);
//            btnProduct2.setVisibility(View.GONE);
//        }

        tvStart.setText(DateUtil.getTime2());
        tvEnd.setText(DateUtil.getTime2());

        setData();

        adapter = new ReportAdapter(mContext, goodsList, itemType);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        getType();

        loadData();
    }

    private void setData() {
        final List<XCDropDownListView.XCDropDownItem> list = classification.getItems();
        list.clear();
        ItemType itemType = new ItemType();
        itemType.setItemTypeTitle("全部分类");
        LimitsEditEnter(etCondition);
        itemType.setId("");
        list.add(itemType);
        classification.notifyDataChange();
        classification.setShowIndex(0);
        classification.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp160), getResources().getDimensionPixelSize(R.dimen.dp240));

        classification.setSelectCallback(new XCDropDownListView.SelectCallback() {
            @Override
            public void selected(int index) {
                loadData();
            }
        });

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2015, 0, 1);
        endDate.set(2025, 11, 31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (timeType == 0) {
                    tvStart.setText(sSimpleDateFormat.format(date));
                } else {
                    tvEnd.setText(sSimpleDateFormat.format(date));
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
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
    }

    /***
     * 加载列表数据
     */
    private void loadData() {
        start = tvStart.getText().toString();
        end = tvEnd.getText().toString();
        if (!timeCompare(strToDate(start), strToDate(end))) {
            ToastUtil.showWrning(mContext, "结束时间必须大于等于开始时间");
            return;
        }
        typeId = ((ItemType) classification.getSelectedItem()).getItemTypeId();
        itemName = etCondition.getText().toString();
        OkGo.<NydResponse<ReportResultBean>>post(Contonts.URL_REPOT)
                .params("itId", typeId)
                .params("itemName", itemName)
                .params("email", "")
                .params("countSort", sale)
                .params("marginSort", profit)
                .params("grossMarginSort", rate)
                .params("createTime", start)
                .params("endTime", end)
                .params("itemType", itemType)
                .execute(new DialogCallback<NydResponse<ReportResultBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<ReportResultBean>> response) {
                        if (response.body().response.getDatas().size() < 1) {
                            goodsList.clear();
                            adapter = new ReportAdapter(mContext, goodsList, itemType);
                            rvList.setAdapter(adapter);
                        } else {
                            if (itemType == 1) {
                                tvSale.setText("销量(件)");
                            } else {
                                tvSale.setText("销量(千克)");
                            }
                            goodsList.clear();
                            adapter = new ReportAdapter(mContext, goodsList, itemType);
                            goodsList.addAll(response.body().response.getDatas());
                            rvList.setAdapter(adapter);
                            double sum = 0;
                            double sumMoney = 0;
                            double sumProfit = 0;
                            for (ReportResultBean.DatasBean bean : goodsList) {
                                sum = Arith.add(sum, bean.getSumCount());
                                sumMoney = Arith.add(sumMoney, bean.getTotalMoney());
                                sumProfit = Arith.add(sumProfit, bean.getMarginMoney());
                            }
                            tvTotalMoney.setText(sumMoney + "");
                            tvProfit.setText(sumProfit + "");

                            if (itemType == 1) {
                                tvSaleNum.setText((int) sum + "");
                                tvDesc.setText("件商品，总营业额");
                            } else {
                                tvSaleNum.setText(sum + "");
                                tvDesc.setText("千克商品，总营业额");
                            }
                        }
                    }
                });

    }

    private void getType() {
        OkGo.<NydResponse<DataBean<ItemType>>>get(Contonts.ITEM_TYPE).params("itemType", itemType).execute(new JsonCallback<NydResponse<DataBean<ItemType>>>() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<DataBean<ItemType>>> response) {
                final List<XCDropDownListView.XCDropDownItem> list = classification.getItems();
                list.clear();
                ItemType itemType = new ItemType();
                itemType.setItemTypeTitle("全部分类");
                LimitsEditEnter(etCondition);
                itemType.setId("");
                list.add(itemType);
                list.addAll(response.body().response.getDatas());//从缓存中读取分类
                classification.notifyDataChange();
                classification.setShowIndex(0);
                loadData();
            }
        });
    }

    @OnClick({R.id.iv_close, R.id.btn_search, R.id.btn_export, R.id.tv_start, R.id.tv_end, R.id.ll_sale, R.id.ll_profit, R.id.ll_rate, R.id.btn_product1, R.id.btn_product2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.btn_search:
                sale = 1;
                profit = 0;
                rate = 0;
                ivSale.setImageResource(R.mipmap.ic_desc);
                ivProfit.setImageResource(R.mipmap.ic_order_default);
                ivRate.setImageResource(R.mipmap.ic_order_default);
                loadData();
                break;
            case R.id.btn_export:
                dialog = SendMailDialog.create(mContext, itemType, sale, profit, rate, typeId, itemName, start, end);
                dialog.show();
                break;
            case R.id.tv_start:
                timeType = 0;
                pvTime.show();
                break;
            case R.id.tv_end:
                timeType = 1;
                pvTime.show();
                break;
            case R.id.ll_sale:
                switch (sale) {
                    case 0:
                        sale = 1;
                        ivSale.setImageResource(R.mipmap.ic_desc);
                        break;
                    case 1:
                        sale = 2;
                        ivSale.setImageResource(R.mipmap.ic_asc);
                        break;
                    case 2:
                        sale = 1;
                        ivSale.setImageResource(R.mipmap.ic_desc);
                        break;
                }
                profit = 0;
                rate = 0;
                ivProfit.setImageResource(R.mipmap.ic_order_default);
                ivRate.setImageResource(R.mipmap.ic_order_default);
                loadData();
                break;
            case R.id.ll_profit:
                switch (profit) {
                    case 0:
                        profit = 1;
                        ivProfit.setImageResource(R.mipmap.ic_desc);
                        break;
                    case 1:
                        profit = 2;
                        ivProfit.setImageResource(R.mipmap.ic_asc);
                        break;
                    case 2:
                        profit = 1;
                        ivProfit.setImageResource(R.mipmap.ic_desc);
                        break;
                }
                sale = 0;
                rate = 0;
                ivSale.setImageResource(R.mipmap.ic_order_default);
                ivRate.setImageResource(R.mipmap.ic_order_default);
                loadData();
                break;
            case R.id.ll_rate:
                switch (rate) {
                    case 0:
                        rate = 1;
                        ivRate.setImageResource(R.mipmap.ic_desc);
                        break;
                    case 1:
                        rate = 2;
                        ivRate.setImageResource(R.mipmap.ic_asc);
                        break;
                    case 2:
                        rate = 1;
                        ivRate.setImageResource(R.mipmap.ic_desc);
                        break;
                }
                sale = 0;
                profit = 0;
                ivSale.setImageResource(R.mipmap.ic_order_default);
                ivProfit.setImageResource(R.mipmap.ic_order_default);
                loadData();
                break;
            case R.id.btn_product1:
                btnProduct1.setBackgroundResource(R.drawable.bg_corner_theme);
                btnProduct2.setBackgroundResource(R.drawable.bg_line_gray);
                btnProduct1.setTextColor(getResources().getColor(R.color.white));
                btnProduct2.setTextColor(getResources().getColor(R.color.blue));
                itemType = 1;
                getType();
                break;
            case R.id.btn_product2:
                btnProduct2.setBackgroundResource(R.drawable.bg_corner_theme);
                btnProduct1.setBackgroundResource(R.drawable.bg_line_gray);
                btnProduct2.setTextColor(getResources().getColor(R.color.white));
                btnProduct1.setTextColor(getResources().getColor(R.color.blue));
                itemType = 2;
                getType();
                break;
        }
    }

    @Override
    public void onScanSuccess(String barcode) {
        etCondition.getText().clear();
        String code = barcode.replace(" ", "");
        etCondition.setText(code);
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

    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sSimpleDateFormat.parse(strDate, pos);
        return strtodate;
    }

    public static boolean timeCompare(Date date1, Date date2) {
        if (date2.compareTo(date1) == -1) {
            return false;
        } else {
            return true;
        }
    }
}
