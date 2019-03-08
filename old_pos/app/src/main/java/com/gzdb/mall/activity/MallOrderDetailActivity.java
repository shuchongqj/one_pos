package com.gzdb.mall.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.mall.adapter.OrderProductAdapter;
import com.gzdb.mall.bean.MallOrderDetail;
import com.gzdb.sale.dialog.ConfirmDialog;
import com.gzdb.sunmi.bluetooth.PrinterBarCode;
import com.gzdb.sunmi.bluetooth.PrinterOrderBarCode;
import com.gzdb.supermarket.been.DetailBean;
import com.gzdb.supermarket.been.PrintBean;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.GsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MallOrderDetailActivity extends BaseActivity {

    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.tv_driver_price)
    TextView tvDriverPrice;
    @Bind(R.id.ll_driver)
    LinearLayout llDriver;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_pay_price)
    TextView tvPayPrice;
    @Bind(R.id.ll_item)
    LinearLayout llItem;
    @Bind(R.id.tv_order_num)
    TextView tvOrderNum;
    @Bind(R.id.tv_order_date)
    TextView tvOrderDate;
    @Bind(R.id.tv_order_type)
    TextView tvOrderType;
    @Bind(R.id.tv_shop_name)
    TextView tvShopName;
    @Bind(R.id.tv_username)
    TextView tvUsername;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_send_address)
    TextView tvSendAddress;
    @Bind(R.id.tv_remark)
    TextView tvRemark;
    @Bind(R.id.btn_apply)
    Button btnApply;
    @Bind(R.id.rv_list)
    RecyclerView rvList;
    @Bind(R.id.tv_order_status)
    TextView tvOrderStatus;
    @Bind(R.id.tv_preferential_price)
    TextView tvPreferentialPrice;

    private String orderid;

    private OrderProductAdapter adapter;
    private List<MallOrderDetail.ListBean> listBeans = new ArrayList<>();

    private MallOrderDetail orderDetail;

    private ConfirmDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_order_detail);
        ButterKnife.bind(this);
        PrinterOrderBarCode.intClass(MallOrderDetailActivity.this);
        textTitle.setText("订单详情");
        PrinterBarCode.intClass(this);
        adapter = new OrderProductAdapter(mContext, listBeans);
        rvList.setLayoutManager(new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvList.setAdapter(adapter);

        orderid = getIntent().getStringExtra("orderid");
        btnApply.setText("立即发货");
        getData();
    }

    @OnClick({R.id.img_back, R.id.btn_apply})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.img_back:
                    finish();
                    break;
                case R.id.btn_apply:
                    if (orderDetail.getDeliver_status() == 1) {//发货状态  0待发货，1已发货
                        print(orderid);
                    } else {
                        String noticeStr = "点击立即发货即确认发货，此订单的状态会变更为已完成";
                        if (orderDetail.getDistribution_type() == 0) {
                            noticeStr = "点击核销订单即确认发货，用户的订单状态也会变更为已完成，请确认用户提供的序列号和购买商品的信息核对正确";
                        }
                        dialog = new ConfirmDialog(mContext, noticeStr, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(btnApply.getText().equals("立即发货")){
                                    print(orderid);
                                }else {
                                    printupdateorder(orderid);
                                }
                                btnApply.setText("重打配送单");
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        try {
            OkGo.<NydResponse<MallOrderDetail>>post(Contonts.MALL_ORDER_DETAIL)
                    .params("passport_id", app.currentUser.getPassportId())
                    .params("orderid", orderid)
                    .execute(new JsonCallback<NydResponse<MallOrderDetail>>() {

                        @Override
                        public void onSuccess(Response<NydResponse<MallOrderDetail>> response) {
                            try {
                                orderDetail = response.body().response;
                                listBeans.addAll(orderDetail.getList());
                                adapter.notifyDataSetChanged();
                                tvDriverPrice.setText("¥ " + Arith.div(orderDetail.getDistribution_fee(), 100) + "");
                                tvTotalPrice.setText("¥ " + Arith.div(orderDetail.getOrdinary_last_pay(), 100) + "");
                                tvPayPrice.setText("¥ " + Arith.div(orderDetail.getTotal_price(), 100) + "");
                                tvPreferentialPrice.setText("¥ " + Arith.div(orderDetail.getPreferential_amount(), 100) + "");
                                String orderStatus = "待取货";
                                if (orderDetail.getDeliver_status() == 1) {
                                    if (orderDetail.getDistribution_type() != 0) {
                                        btnApply.setVisibility(View.VISIBLE);
                                        btnApply.setText("重打配送单");
                                    } else {
                                        btnApply.setVisibility(View.GONE);
                                    }
                                    orderStatus = "已完成";
                                }
                                tvOrderStatus.setText("订单状态：" + orderStatus);
                                tvOrderDate.setText("创建时间：" + orderDetail.getFormat_create_time());
                                tvOrderNum.setText("订单编号：" + orderDetail.getOrder_sequence_number());
                                tvShopName.setText("消费店铺：" + orderDetail.getShow_name());
                                String orderType = "快递配送";
                                if (orderDetail.getDistribution_type() == 0) {
                                    orderType = "上门自提";
                                    btnApply.setText("核销订单");
                                }
                                tvOrderType.setText("取货方式：" + orderType);
                                tvUsername.setText("收货人：" + orderDetail.getReceipt_nick_name());
                                tvPhone.setText("联系电话：" + orderDetail.getReceipt_phone());
                                tvSendAddress.setText("取货地址：" + (orderDetail.getDistribution_address() == null ? "" : orderDetail.getDistribution_address()));
                                tvRemark.setText("备注：" + (orderDetail.getRemarks() == null ? "" : orderDetail.getRemarks()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void print(String orderid) {
        try {
            OkGo.<NydResponse<Object>>post(Contonts.orderdistribution)
                    .params("orderid", orderid)
                    .execute(new JsonCallback<NydResponse<Object>>() {

                        @Override
                        public void onSuccess(Response<NydResponse<Object>> response) {
                            try {

                                Object obj = response.body().response;
                                if(obj==null){
                                    return;
                                }
                                String str = GsonUtil.toJson(obj);
                                JSONObject json = new JSONObject(str);
                                String daySortNumber = json.optString("daySortNumber");
                                String orderSequenceNumber = json.optString("order_sequence_number");
                                String receiptNickName = json.optString("receipt_nick_name");
                                String showName = json.optString("show_name");
                                String receiptPhone = json.optString("receipt_phone");
                                String distributionAddress = json.optString("distribution_address");
                                String createTime = json.optString("create_time");
                                String remarks = json.optString("remarks");
                                String qrCode = json.optString("qrCode");
                                long totalPrice = json.optLong("total_price");
                                JSONArray jsonArray = json.getJSONArray("detail");

                                PrintBean printBean = new PrintBean();


                                if (jsonArray != null) {
                                    List<DetailBean> detail = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        DetailBean detailBean = new DetailBean();
                                        detailBean.setItem_name(jsonObject.optString("item_name"));
                                        detailBean.setNormal_quantity(jsonObject.optInt("normal_quantity"));
                                        detailBean.setGroup_price(jsonObject.optLong("group_price"));
                                        detail.add(detailBean);

                                    }
                                    printBean.setDetail(detail);

                                }
                                printBean.setDaySortNumber(daySortNumber);
                                printBean.setOrderSequenceNumber(orderSequenceNumber);
                                printBean.setReceiptNickName(receiptNickName);
                                printBean.setShowName(showName);
                                printBean.setReceiptPhone(receiptPhone);
                                printBean.setDistributionAddress(distributionAddress);
                                printBean.setRemarks(remarks);
                                printBean.setTotalPrice(totalPrice);
                                printBean.setCreateTime(createTime);
                                printBean.setQrCode(qrCode);


                                PrinterOrderBarCode.outcomeTacket(printBean);
                                if(dialog!=null) {
                                    dialog.dismiss();
                                }
                                //  Printer.outcomeTacket(daySortNumber);//打印小票
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printupdateorder(String orderid) {
        try {
            OkGo.<NydResponse<Object>>post(Contonts.updateorderstatus)
                    .params("passport_id",App.getInstance().currentUser.getPassportId())
                    .params("orderid", orderid)
                    .execute(new JsonCallback<NydResponse<Object>>() {

                        @Override
                        public void onSuccess(Response<NydResponse<Object>> response) {

                        }
                    });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
