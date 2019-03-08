package com.gzdb.vaservice;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.vaservice.adapter.YctRecordAdapter;
import com.gzdb.vaservice.bean.YctRecordBean;
import com.lzy.okgo.OkGo;

import java.math.BigDecimal;
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
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

public class YctRecordActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_total_money)
    TextView tvTotalMoney;
    @Bind(R.id.tv_start)
    TextView tvStart;
    @Bind(R.id.tv_end)
    TextView tvEnd;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @Bind(R.id.spinner)
    Spinner spinner;
    @Bind(R.id.tv_total_fly_money)
    TextView tvTotalFlyMoney;

    private TimePickerView pvTime;
    private int timeType = 1;
    public static final DateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private List<YctRecordBean> yctRecordBeans = new ArrayList<>();
    private YctRecordAdapter adapter;

    private String type = "";
    private List<String> typeArr = new ArrayList<String>();

    private static Dialog dialog;

    private int page = 1;
    private int size = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yct_record);
        ButterKnife.bind(this);
        textTitle.setText("充值记录");

        dialog = DialogUtil.loadingDialog(mContext, "加载中...");

        refreshLayout.setDelegate(this);
        refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        refreshLayout.setIsShowLoadingMoreView(true);

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
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

        getData();


        typeArr.add("全部");
        typeArr.add("普通充值");
        typeArr.add("预存金充值");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_select_type, typeArr);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    type = "";
                } else {
                    type = i + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_start, R.id.tv_end, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_start:
                timeType = 0;
                pvTime.show();
                break;
            case R.id.tv_end:
                timeType = 1;
                pvTime.show();
                break;
            case R.id.btn_search:
                String start = tvStart.getText().toString().trim();
                String end = tvEnd.getText().toString().trim();
                if (!start.equals("") && !end.equals("")) {
                    if (!timeCompare(strToDate(start), strToDate(end))) {
                        ToastUtil.showWrning(mContext, "结束时间必须大于开始时间");
                        return;
                    }
                }
                page = 1;
                yctRecordBeans.clear();
                getData();
                break;
        }
    }

    private void getData() {
        dialog.show();
        OkGo.<NydResponse<List<YctRecordBean>>>post(Contonts.YCT_RECORD_LIST)
                .tag(getClass().getSimpleName())
                .params("startTimeString", tvStart.getText().toString())
                .params("endTimeString", tvEnd.getText().toString())
                .params("page", page)
                .params("rows", size)
                .params("type", type)
                .execute(new JsonCallback<NydResponse<List<YctRecordBean>>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<List<YctRecordBean>>> response) {
                        dialog.dismiss();
                        if (page == 1) {
                            yctRecordBeans.clear();
                            adapter = new YctRecordAdapter(mContext, yctRecordBeans);
                            rvList.setLayoutManager(new LinearLayoutManager(mContext));
                            rvList.setAdapter(adapter);
                        }
                        if (response.body().response.size() > 0) {
                            BigDecimal bg = new BigDecimal(0.00);
                            BigDecimal bg2 = new BigDecimal(0.00);
                            if (response.body().response.get(0).getTodayAmount() != null) {
                                bg = new BigDecimal(response.body().response.get(0).getTodayAmount());
                            }
                            if (response.body().response.get(0).getTodayflyCharge() != null) {
                                bg2 = new BigDecimal(response.body().response.get(0).getTodayflyCharge());
                            }
                            tvTotalMoney.setText(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
                            tvTotalFlyMoney.setText(bg2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
                        }
                        yctRecordBeans.addAll(response.body().response);
                        adapter.notifyDataSetChanged();
                        if (refreshLayout != null) {
                            refreshLayout.endLoadingMore();
                            refreshLayout.endRefreshing();
                        }
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<List<YctRecordBean>>> response) {
                        super.onError(response);
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page++;
        getData();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static boolean timeCompare(Date date1, Date date2) {
        return date2.compareTo(date1) == 1 ? true : false;
    }

    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = sSimpleDateFormat.parse(strDate, pos);
        return strtodate;
    }
}
