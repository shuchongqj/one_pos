package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.response.enums.PaymentTypeEnum;
import com.gzdb.supermarket.been.RechargeLogBean;
import com.gzdb.supermarket.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liubolin on 2017/12/28.
 */

public class RechargeLogAdapter extends BaseAdapter {

    private Context mContext;

    private List<RechargeLogBean> mDatas = new ArrayList<>();

    public RechargeLogAdapter(Context context) {
        mContext = context;
    }

    public void setRechargeLogDatas(List<RechargeLogBean> datas) {
        if (null == datas || datas.size() == 0) {
            return;
        }
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_recharge_log, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        RechargeLogBean bean = mDatas.get(position);
        holder.user_name.setText(bean.getShowName());
        holder.user_account.setText(bean.getDefaultName());

        holder.recharge_day.setText(String.valueOf(bean.getRenewDays()));
        holder.recharge_money.setText(Utils.toYuanStr(bean.getPayMoney()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(bean.getCreateTime());
        String reTime = simpleDateFormat.format(date);
        holder.recharge_time.setText(reTime);

        Date date2 = new Date(bean.getNewTime());
        String newTime = simpleDateFormat.format(date2);
        holder.new_time.setText(newTime);

        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getPaymentTypeEnum(bean.getPayType());
        if (null != paymentTypeEnum) {
            holder.recharge_pay_type.setText(paymentTypeEnum.getValue());
        } else {
            holder.recharge_pay_type.setText("未知");
        }

        return convertView;
    }

    class Holder {
        TextView user_name;

        TextView user_account;

        TextView recharge_day;

        TextView recharge_money;

        TextView recharge_pay_type;

        TextView new_time;

        TextView recharge_time;

        public Holder(View view) {
            user_name = (TextView) view.findViewById(R.id.user_name);
            user_account = (TextView) view.findViewById(R.id.user_account);
            recharge_day = (TextView) view.findViewById(R.id.recharge_day);
            recharge_money = (TextView) view.findViewById(R.id.recharge_money);
            recharge_pay_type = (TextView) view.findViewById(R.id.recharge_pay_type);
            new_time = (TextView) view.findViewById(R.id.new_time);
            recharge_time = (TextView) view.findViewById(R.id.recharge_time);
        }
    }
}
