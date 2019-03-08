package com.gzdb.vaservice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.vaservice.PrintTicket;
import com.gzdb.vaservice.bean.YctRecordBean;
import com.gzdb.vaservice.bean.YctRecordDetail;
import com.gzdb.yct.util.DateUtil;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class YctRecordAdapter extends RecyclerView.Adapter<YctRecordAdapter.ViewHolder> {

    private Context mContext;
    private List<YctRecordBean> yctRecords;

    public YctRecordAdapter(Context context, List<YctRecordBean> mYctRecords) {
        this.mContext = context;
        this.yctRecords = mYctRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yct_record, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.btnPrint.setTag(position);
        final YctRecordBean yctRecord = yctRecords.get(position);

        holder.tvOrderId.setText(yctRecord.getOrderNumber());
        holder.tvCardNo.setText(yctRecord.getCardNumber());
        BigDecimal bg = new BigDecimal(yctRecord.getAmount());
        holder.tvMoney.setText(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "");
        if (yctRecord.getType() == 1) {
            holder.tvType.setText("普通充值");
        } else {
            holder.tvType.setText("预存金充值");
        }
        if (yctRecord.getStatus() == 1) {
            holder.tvStatus.setText("成功");
            holder.btnPrint.setBackgroundResource(R.drawable.bg_corner_theme);
        } else if (yctRecord.getStatus() == 2) {
            holder.tvStatus.setText("疑似成功");
            holder.btnPrint.setBackgroundResource(R.drawable.bg_corner_theme);
        } else {
            holder.tvStatus.setText("失败");
            holder.btnPrint.setBackgroundResource(R.drawable.bg_corner_gray);
        }
        holder.tvTime.setText(DateUtil.stampToDetailDateJava(yctRecord.getCreateTime()));

        holder.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (yctRecord.getStatus() == -1) {
                    return;
                }
                if ((int) holder.btnPrint.getTag() == position) {
                    ToastUtil.showToast(mContext, "重打小票");
                    getData(yctRecord.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return yctRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_order_id)
        TextView tvOrderId;
        @Bind(R.id.tv_card_no)
        TextView tvCardNo;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.btn_print)
        Button btnPrint;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void getData(int id) {
        OkGo.<NydResponse<YctRecordDetail>>post(Contonts.YCT_RECORD_DETAIL)
                .tag(getClass().getSimpleName())
                .params("yctId", id)
                .execute(new JsonCallback<NydResponse<YctRecordDetail>>() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<YctRecordDetail>> response) {
                        EventBus.getDefault().post(new PrintTicket(response.body().response, 2));
                    }
                });
    }
}
