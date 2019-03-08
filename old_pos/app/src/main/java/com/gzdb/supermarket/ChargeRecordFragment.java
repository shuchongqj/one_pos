package com.gzdb.supermarket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ImageDisplayUtils;
import com.core.base.MRecyclerView;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.adapter.ChargeRecordAdapter;
import com.gzdb.supermarket.been.ChargeRecordResultBean;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by nongyd on 17/6/28.
 * 交易记录
 */

public class ChargeRecordFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @Bind(R.id.delete)
    ImageView delete;
    @Bind(R.id.edit_date)
    TextView editDate;
    @Bind(R.id.select_data)
    XCDropDownListView selectData;
    @Bind(R.id.text_totalNum)
    TextView textTotalNum;
    @Bind(R.id.text_totalPrice)
    TextView textTotalPrice;
    @Bind(R.id.recyclerView)
    MRecyclerView recyclerView;
    @Bind(R.id.text_aliNum)
    TextView textAliNum;
    @Bind(R.id.text_wxNum)
    TextView textWxNum;
    @Bind(R.id.text_wxPrice)
    TextView textWxPrice;
    @Bind(R.id.bt_search)
    Button btSearch;
    @Bind(R.id.text_aliyPrice)
    TextView textAliyPrice;
    @Bind(R.id.text_cashNum)
    TextView textCashNum;
    @Bind(R.id.text_cashPrice)
    TextView textCashPrice;
    @Bind(R.id.refreshLayout)
    BGARefreshLayout refreshLayout;
    @Bind(R.id.img_men)
    ImageView imgMen;
    @Bind(R.id.text_preTotal)
    TextView textPreTotal;
    @Bind(R.id.text_servicePrice)
    TextView textServicePrice;
    @Bind(R.id.tv_vip_num)
    TextView tvVipNum;
    @Bind(R.id.tv_vip_money)
    TextView tvVipMoney;


    private int searchType = 1;
    private int currentPage = 1;
    private int pageNum = 80;
    private ChargeRecordAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charege_record, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {
        refreshLayout.setDelegate(this);
        refreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(mContext, true));
        refreshLayout.setIsShowLoadingMoreView(true);

        final List<XCDropDownListView.XCDropDownItem> list = selectData.getItems();
        list.add(new XCDropDownListView.XCDropDownItemStr("天"));
        list.add(new XCDropDownListView.XCDropDownItemStr("月"));
        selectData.setShowIndex(0);
        selectData.notifyDataChange();
        selectData.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp80), getResources().getDimensionPixelSize(R.dimen.dp100));
        selectData.setSelectCallback(new XCDropDownListView.SelectCallback() {
            @Override
            public void selected(int index) {
                currentPage = 1;
                loadData();
            }
        });
        adapter = new ChargeRecordAdapter(mContext, new ArrayList<ChargeRecordResultBean.TransactionRecordListBean>());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        editDate.setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH));
        ImageDisplayUtils.showImage(this.getContext(), R.mipmap.charge_men, imgMen);
        loadData();
    }

    /***
     * 日期
     */
    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(getActivity(), 20));
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        picker.setRangeEnd(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        picker.setRangeStart(2014, 1, 1);
        String tempDate = editDate.getText().toString();
        if (BaseUtils.isEmpty(tempDate)) {
            picker.setSelectedItem(2017, 1, 1);
        } else {
            String[] strs = tempDate.split("-");
            picker.setSelectedItem(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                editDate.setText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
                currentPage = 1;
                loadData();
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "年" + picker.getSelectedMonth() + "月" + picker.getSelectedDay() + "日");
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "年" + month + "月" + picker.getSelectedDay() + "日");
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "年" + picker.getSelectedMonth() + "月" + day + "日");
            }
        });
        picker.show();
    }

    public void loadData() {
        if (selectData.getSelectedItem().getXCDropDownItemText().equals("天")) {
            searchType = 1;
        } else {
            searchType = 2;
        }
        OkGo.<NydResponse<ChargeRecordResultBean>>post(Contonts.URL_CHARGE_RECORD)
                .tag(getClass().getSimpleName())
                .params("createTime", editDate.getText().toString())
                .params("enterType", searchType)
                .params("pageNum", currentPage)
                .params("pageSize", pageNum)
                .execute(new DialogCallback<NydResponse<ChargeRecordResultBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<ChargeRecordResultBean>> response) {
                        ChargeRecordResultBean bean = response.body().response;
                        if (currentPage == 1) {
                            adapter.removeData(adapter.getData());
                        }
                        if (refreshLayout != null) {
                            refreshLayout.endLoadingMore();
                            refreshLayout.endRefreshing();
                        }
                        adapter.addData(bean.getTransactionRecordList());
                        textTotalNum.setText(bean.getSumCount() + "");
                        textTotalPrice.setText(bean.getSumMoney() + "");
                        textAliNum.setText(bean.getZfbSum() + "");
                        textAliyPrice.setText(bean.getZfbMoney() + "");
                        textWxNum.setText(bean.getWxSum() + "");
                        textWxPrice.setText(bean.getWxMoney() + "");
                        textCashNum.setText(bean.getCashSum() + "");
                        textCashPrice.setText(bean.getCashMoney() + "");
                        textPreTotal.setText(bean.getNormalMoney() + "");
                        textServicePrice.setText(bean.getOutMoney() + "");
                        tvVipNum.setText(bean.getHykSum()+"");
                        tvVipMoney.setText(bean.getHykMoney()+"");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.delete, R.id.bt_search, R.id.edit_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delete:
                getActivity().onBackPressed();
                break;
            case R.id.bt_search:
                currentPage = 1;
                loadData();
                break;
            case R.id.edit_date:
                onYearMonthDayPicker();
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        currentPage = 1;
        loadData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        ++currentPage;
        loadData();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(getClass().getSimpleName());
    }
}
