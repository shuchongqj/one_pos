package com.gzdb.sale.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.sale.bean.SaleProduct;
import com.gzdb.sale.event.RefreshPriceEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SaleProductAdapter extends RecyclerView.Adapter<SaleProductAdapter.ViewHolder> {

    private Context mContext;
    private List<SaleProduct> mProducts;

    private List<SaleProduct> deleteProducts=new ArrayList<>();
    private SparseBooleanArray mCheckStates = new SparseBooleanArray();
    private int mType;

    public SaleProductAdapter(Context context, List<SaleProduct> products, int type) {
        this.mContext = context;
        this.mProducts = products;
        this.mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_product, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.setIsRecyclable(false);
        holder.btnDelete.setTag(position);
        holder.etSalePrice.setTag(position);
        final SaleProduct product = mProducts.get(position);
        holder.tvName.setText(product.getItemName());
        holder.tvNo.setText(product.getBarcode());
        holder.tvBuyingPrice.setText(product.getStockPrice());
        holder.tvPrice.setText(product.getSalesPrice());
        holder.etSalePrice.setText(product.getDiscount());

//        int pos = (int) holder.btnDelete.getTag();
//        mCheckStates.put(pos, true);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)holder.btnDelete.getTag() == position) {
                    deleteProducts.add(product);
                    mProducts.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(0,mProducts.size());
                    EventBus.getDefault().post(new RefreshPriceEvent());
                }
            }
        });

        if (mType == 1) {
            holder.etSalePrice.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // 输入的内容变化的监听
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // 输入前的监听

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // 输入后的监听
                    if ((int)holder.etSalePrice.getTag() == position) {//设置tag解决错乱问题
                        if(s.toString().equals("")||s.toString().equals(".")){
                            return;
                        }
                        if(Double.valueOf(s.toString())>=Double.valueOf(product.getSalesPrice())){
                            ToastUtil.showWrning(mContext,"优惠价格要小于商品价格");
                            holder.etSalePrice.setText("");
                        }else{
                            product.setDiscount(s.toString());
                            mProducts.set(position, product);
                        }
                    }
                }
            });
            holder.line.setVisibility(View.VISIBLE);
            holder.etSalePrice.setVisibility(View.VISIBLE);
        }
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
        @Bind(R.id.et_sale_price)
        EditText etSalePrice;
        @Bind(R.id.btn_delete)
        Button btnDelete;
        @Bind(R.id.line)
        View line;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<SaleProduct> getmProducts() {
        return mProducts;
    }

    public List<SaleProduct> getDeleteProducts() {
        return deleteProducts;
    }
}
