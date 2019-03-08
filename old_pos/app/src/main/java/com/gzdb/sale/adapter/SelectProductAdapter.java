package com.gzdb.sale.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.sale.bean.SelectProduct;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.ViewHolder> {

    private Context mContext;
    private List<SelectProduct> mProducts;
    private List<SelectProduct> selectProducts;
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    public SelectProductAdapter(Context context, List<SelectProduct> products, List<SelectProduct> selectProducts) {
        this.mContext = context;
        this.mProducts = products;
        this.selectProducts = selectProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_product, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btnSelect.setTag(position);
        final SelectProduct product = mProducts.get(position);
        holder.tvName.setText(product.getItemName());
        holder.tvNo.setText(product.getBarcode());
        holder.tvBuyingPrice.setText(product.getStockPrice());
        holder.tvPrice.setText(product.getSalesPrice());
        int pos = (int) holder.btnSelect.getTag();
        if (product.getChecked() == 1) {
            mCheckStates.put(pos, true);
        }
        if (mCheckStates.get(position, false)) {
            holder.btnSelect.setText("已选择");
            holder.btnSelect.setBackgroundResource(R.drawable.bg_corner_gray);
        } else {
            holder.btnSelect.setText("选择");
            holder.btnSelect.setBackgroundResource(R.drawable.bg_corner_theme);
        }
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<selectProducts.size();i++){
                    if(product.getItemId()==selectProducts.get(i).getItemId()){
                        ToastUtil.showWrning(mContext,"该商品已经参与过其他活动！");
                        return;
                    }
                }
                int pos = (int) view.getTag();
                if (product.getChecked() == 1) {
                    product.setChecked(0);
                    mCheckStates.delete(pos);
                } else {
                    product.setChecked(1);
                    mCheckStates.put(pos, true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_no)
        TextView tvNo;
        @Bind(R.id.tv_buying_price)
        TextView tvBuyingPrice;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.btn_select)
        Button btnSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<SelectProduct> getmProducts() {
        return mProducts;
    }
}
