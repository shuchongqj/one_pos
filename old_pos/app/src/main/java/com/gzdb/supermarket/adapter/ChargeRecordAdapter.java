package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.bluetooth.Printer;
import com.gzdb.supermarket.been.ChargeRecordResultBean;
import com.gzdb.supermarket.been.FinishOrderData;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by nongyd on 17/6/28.
 */

public class ChargeRecordAdapter extends BaseTurboAdapter<ChargeRecordResultBean.TransactionRecordListBean, ChargeRecordAdapter.ViewHolder> {

    public ChargeRecordAdapter(Context context, List<ChargeRecordResultBean.TransactionRecordListBean> data) {
        super(context, data);
    }

    @Override
    protected void convert(ViewHolder holder, final ChargeRecordResultBean.TransactionRecordListBean item) {
        holder.textTitle.setText(item.getItemName());
        holder.textDate.setText(item.getPaymentTime());
        holder.textOrderNo.setText("..." + item.getSequenceNumber().substring(item.getSequenceNumber().length() - 8));
        String payType=item.getTransType();
        if(item.getTransType().equals("CARD_JS")){
            payType="会员卡";
        }
        holder.textPayType.setText(payType);
        holder.textActualyPrice.setText(item.getCashPrice() + "");
        holder.textServicePrice.setText(item.getRatePrice()+"");
        holder.textOrderPrice.setText(item.getTotalPrice() + "");
        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<NydResponse<FinishOrderData>>post(Contonts.URL_GET_ORDER_DETAIL)
                        .params("convertRMBUnit", true)
                        .params("orderId", item.getId())
                        .execute(new DialogCallback<NydResponse<FinishOrderData>>(mContext) {
                            @Override
                            public void onSuccess(com.lzy.okgo.model.Response<NydResponse<FinishOrderData>> response) {
                                try {
                                    Printer.outcomeTacket(response.body().response);//打印小票
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }


    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflateItemView(R.layout.item_chargerecord, parent));
    }


    static class ViewHolder extends BaseViewHolder {

        @Bind(R.id.text_title)
        TextView textTitle;
        @Bind(R.id.text_payType)
        TextView textPayType;
        @Bind(R.id.text_date)
        TextView textDate;
        @Bind(R.id.text_orderPrice)
        TextView textOrderPrice;
        @Bind(R.id.text_servicePrice)
        TextView textServicePrice;
        @Bind(R.id.text_actualyPrice)
        TextView textActualyPrice;
        @Bind(R.id.text_orderNo)
        TextView textOrderNo;
        @Bind(R.id.btn_print)
        Button btnPrint;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}