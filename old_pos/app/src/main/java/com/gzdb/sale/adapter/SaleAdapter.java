package com.gzdb.sale.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.sale.activity.EditSaleActivity;
import com.gzdb.sale.bean.Sale;
import com.gzdb.sale.dialog.ConfirmDialog;
import com.gzdb.sale.event.RefreshSaleEvent;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.CountDownUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cc.solart.turbo.BaseViewHolder;
import okhttp3.Call;
import okhttp3.Response;

public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {

    private ConfirmDialog confirmDialog;
    private Sale mSale;
    private int status;
    private Activity mContext;
    private List<Sale> sales;

    public SaleAdapter(Activity context, List<Sale> sales, int status) {
        this.sales = sales;
        this.status = status;
        this.mContext = context;
        if (status == 1) {
            confirmDialog = new ConfirmDialog(context, "确认结束此活动吗？", listener);
        } else {
            confirmDialog = new ConfirmDialog(context, "确认删除此活动吗？", listener);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (status == 1) {
                finish(mSale.getId());
            } else {
                delete(mSale.getId());
            }
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sale sale = sales.get(position);
        holder.tvName.setText(sale.getActivityName());
        String type = "";
        switch (sale.getActivityType()) {
            case 1:
                type = "商品价格折扣";
                break;
            case 2:
                type = "组合购买优惠";
                break;
            case 3:
                type = "价格满减";
                break;
            case 4:
                type = "数量满减";
                break;
        }
        holder.tvType.setText("活动类型：" + type);
        long totalSeconds = sale.getEndTime() / 1000 - System.currentTimeMillis() / 1000;
        long startSeconds = sale.getStartTime() / 1000 - System.currentTimeMillis() / 1000;

        if (totalSeconds <= 0) {
            holder.tvTime.setText("活动剩余时间：0天0小时0分钟");
        } else if (startSeconds > 0) {
            holder.tvTime.setText("离活动开始时间：" + CountDownUtils.getData(startSeconds));
        } else {
            holder.tvTime.setText("活动剩余时间：" + CountDownUtils.getData(totalSeconds));
        }

        if (status == 1) {
            holder.btnDelete.setText("结束");
            holder.btnEdit.setText("编辑");
        } else if (status == 2) {
            holder.btnEdit.setText("编辑");
            holder.btnDelete.setText("删除");
        } else {
            holder.btnEdit.setText("重新开始");
            holder.btnDelete.setText("删除");
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSale = sale;
                confirmDialog.show();
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditSaleActivity.class);
                intent.putExtra("method", "edit");
                intent.putExtra("id", sale.getId() + "");
                intent.putExtra("type", sale.getActivityType());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    public class ViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.btn_edit)
        Button btnEdit;
        @Bind(R.id.btn_delete)
        Button btnDelete;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void finish(int id) {
        OkGo.<NydResponse<String>>post(Contonts.FINISH_SALE)
                .params("activityId", id + "")
                .execute(new DialogCallback<NydResponse<String>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<String>> response) {
                        confirmDialog.dismiss();
                        ToastUtil.showSuccess(mContext, response.body().msg);
                        EventBus.getDefault().post(new RefreshSaleEvent());
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<String>> response) {
                        super.onError(response);
                        confirmDialog.dismiss();
                    }
                });
    }

    private void delete(int id) {
        OkGo.<NydResponse<String>>post(Contonts.DELETE_SALE)
                .params("activityId", id + "")
                .execute(new DialogCallback<NydResponse<String>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<String>> response) {
                        confirmDialog.dismiss();
                        ToastUtil.showSuccess(mContext, response.body().msg);
                        EventBus.getDefault().post(new RefreshSaleEvent());
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<String>> response) {
                        super.onError(response);
                        confirmDialog.dismiss();
                    }
                });
    }
}
