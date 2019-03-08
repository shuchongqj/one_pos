package com.gzdb.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.mall.activity.MallProductDetailActivity;
import com.gzdb.mall.bean.BaseBean;
import com.gzdb.mall.bean.MallProduct;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.yct.util.DateUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MallProductAdapter extends BaseRecyclerAdapter<MallProduct> {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_code)
    TextView tvCode;
    @Bind(R.id.tv_sale_price)
    TextView tvSalePrice;
    @Bind(R.id.tv_vip_price)
    TextView tvVipPrice;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.btn_edit)
    Button btnEdit;
    @Bind(R.id.btn_delete)
    Button btnDelete;

    private Context mContext;
    private List<MallProduct> mlist;

    public MallProductAdapter(Context context, List<MallProduct> list) {
        super(context, list);
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_mall_product;
    }

    @Override
    protected void covert(final BaseViewHolder holder, final MallProduct data, final int position) {
        ButterKnife.bind(this, holder.getView());
        tvName.setText(data.getNAME());
        tvCode.setText(data.getBarcode());
        tvSalePrice.setText(Arith.div(data.getGroup_price(), 100) + "");
        tvVipPrice.setText(Arith.div(data.getMember_price(), 100) + "");
        if (data.getStatus() == 1) {
            tvStatus.setText("上架");
            btnDelete.setBackgroundResource(R.drawable.bg_corner_red);
            btnDelete.setText("下架");
        } else {
            tvStatus.setText("下架");
            btnDelete.setBackgroundResource(R.drawable.bg_corner_theme);
            btnDelete.setText("上架");
        }
        String type = "";
        switch (data.getItem_attr()) {
            case 0:
                type = "平台商品";
                btnDelete.setVisibility(View.INVISIBLE);
                btnEdit.setText("查看");
                break;
            case 1:
                type = "商家商品";
                btnDelete.setVisibility(View.VISIBLE);
                btnEdit.setText("编辑");
                break;
            case 2:
                type = "自选商品";
                btnDelete.setVisibility(View.VISIBLE);
                btnEdit.setText("编辑");
                break;
            default:
        }

        tvType.setText(type);
        tvTime.setText(DateUtil.stampToDetailDateJava3(data.getStart_time()) + "至" + DateUtil.stampToDetailDateJava3(data.getEnd_time()));
        holder.getView().findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MallProductDetailActivity.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("type", data.getItem_attr());
                intent.putExtra("action", "编辑商品");
                mContext.startActivity(intent);
            }
        });
        holder.getView().findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getStatus() == 1) {
                    down(position, data.getId(), 0);
                } else {
                    down(position, data.getId(), 1);
                }

            }
        });
    }

    private void down(final int position, int item_id, final int type) {
        String url = Contonts.MALL_PRODUCT_ON;
        if (type == 0) {
            url = Contonts.MALL_PRODUCT_OFF;
        }
        OkGo.<NydResponse<Object>>post(url)
                .params("passport_id", App.getInstance().currentUser.getPassportId())
                .params("item_id", item_id)
                .execute(new DialogCallback<NydResponse<Object>>(mContext) {
                    @Override
                    public void onSuccess(Response<NydResponse<Object>> response) {
                        MallProduct mallProduct = mlist.get(position);
                        if (type == 0) {
                            mallProduct.setStatus(0);
                        } else {
                            mallProduct.setStatus(1);
                        }
                        mlist.set(position,mallProduct);
                        notifyDataSetChanged();
                    }
                });
    }
}
