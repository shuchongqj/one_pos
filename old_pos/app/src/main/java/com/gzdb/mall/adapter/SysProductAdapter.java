package com.gzdb.mall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.core.base.BaseRecyclerAdapter;
import com.core.base.BaseViewHolder;
import com.gzdb.basepos.R;
import com.gzdb.mall.activity.MallProductDetailActivity;
import com.gzdb.mall.bean.SysProduct;
import com.gzdb.supermarket.util.Arith;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SysProductAdapter extends BaseRecyclerAdapter<SysProduct> {

    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_vip_price)
    TextView tvVipPrice;
    @Bind(R.id.tv_reward)
    TextView tvReward;
    @Bind(R.id.btn_select)
    Button btnSelect;
    @Bind(R.id.iv_image)
    ImageView ivImage;

    private Context mContext;

    public SysProductAdapter(Context context, List<SysProduct> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected int getContentView(int viewType) {
        return R.layout.item_sys_product;
    }

    @Override
    protected void covert(BaseViewHolder holder, final SysProduct data, int position) {
        try {
            ButterKnife.bind(this, holder.getView());
            String imgs = data.getImgs();
            if (imgs != null && imgs.length() > 0) {
                String imgArray[] = data.getImgs().split(",");
                if (imgArray != null && imgArray.length > 0) {
                    Glide.with(mContext).load(imgArray[0]).into(ivImage);
                } else {
                    ivImage.setImageResource(R.mipmap.lift_pay_img);
                }
            } else {
                ivImage.setImageResource(R.mipmap.lift_pay_img);
            }
            tvName.setText("商品名称：" + data.getNAME());
            tvPrice.setText("建议优选价：" + Arith.div(data.getGroup_price(), 100) + "元");
            tvVipPrice.setText("建议会员优选价：" + Arith.div(data.getGroup_price(), 100) + "元");
            tvReward.setText("商品奖励金：" + Arith.div(data.getReward_gold(), 100) + "元");
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MallProductDetailActivity.class);
                    intent.putExtra("id", data.getId());
                    intent.putExtra("type", data.getItem_attr());
                    intent.putExtra("action", "创建商品");
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
