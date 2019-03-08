package com.gzdb.quickbuy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.util.BaseUtils;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.OrderDetailAdapter;
import com.gzdb.quickbuy.bean.OrderDetailBean;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zxy on 2016/12/8.
 * 采购订单--订单详情
 */

public class OrderDetailDialog extends Dialog implements View.OnClickListener {


    @Bind(R.id.cd_code_num)
    TextView cdCodeNum;
    @Bind(R.id.cd_time)
    TextView cdTime;
    @Bind(R.id.cd_name)
    TextView cdName;
    @Bind(R.id.cd_lv_detair)
    ListView cdLvDetair;
    @Bind(R.id.cd_allMoney)
    TextView cdAllMoney;
    @Bind(R.id.cd_songMoney)
    TextView cdSongMoney;
    @Bind(R.id.cd_preferentialMoney)
    TextView cdPreferentialMoney;
    @Bind(R.id.cd_overllMoney)
    TextView cdOverllMoney;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.cd_codeName)
    TextView cdCodeName;
    @Bind(R.id.cd_consigneeName)
    TextView cdConsigneeName;
    @Bind(R.id.cd_phone)
    TextView cdPhone;
    @Bind(R.id.cd_consigneeAddr)
    TextView cdConsigneeAddr;
    @Bind(R.id.cd_supplierName)
    TextView cdSupplierName;
    @Bind(R.id.supplierMan)
    TextView supplierMan;
    @Bind(R.id.cd_supplierAddr)
    TextView cdSupplierAddr;
    @Bind(R.id.close)
    ImageView close;
    @Bind(R.id.ly_detail)
    RelativeLayout lyDetail;
    Context cx;
    OrderDetailAdapter orderDetailAdapter;
    Dialog dialog;
    OrderDetailBean bean;


    public OrderDetailDialog(Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.code_detail_dialog);
        ButterKnife.bind(this);
        this.cx = context;
        close.setOnClickListener(this);

    }

    public void refreshOrderDetail(OrderDetailBean bean) {
        initView(bean);
    }

    void initView(OrderDetailBean bean) {

        cdCodeNum.setText(bean.getSequenceNumber() + "");
        cdTime.setText(BaseUtils.convertToStrSS(bean.getCreateTime()));
        cdName.setText(bean.getReceiptNickName() + "");
        cdAllMoney.setText("￥" + bean.getActualPrice());
        cdSongMoney.setText("￥" + bean.getDistributionFee());
        cdPreferentialMoney.setText("￥" +bean.getDiscountPrice());
        cdOverllMoney.setText("￥" +bean.getTotalPrice());
        cdCodeName.setText(bean.getReceiptNickName() + "");
        cdConsigneeName.setText(bean.getReceiptNickName());
        cdPhone.setText(bean.getReceiptPhone());
        cdConsigneeAddr.setText(bean.getReceiptAddress());
        cdSupplierName.setText(bean.getShippingNickName());
        supplierMan.setText(bean.getShippingNickName());
        cdSupplierAddr.setText(bean.getShippingAddress());

        orderDetailAdapter = new OrderDetailAdapter(cx, bean.getItemSnapshots());
        cdLvDetair.setAdapter(orderDetailAdapter);
        orderDetailAdapter.notifyDataSetChanged();
        lyDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
        }
    }
}
