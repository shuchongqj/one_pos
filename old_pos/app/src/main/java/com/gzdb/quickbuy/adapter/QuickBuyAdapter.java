package com.gzdb.quickbuy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.QuickBuyItem;
import com.gzdb.quickbuy.event.QuickBuyEvent;
import com.gzdb.quickbuy.util.AddAndEditorAddr;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.util.Arith;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuickBuyAdapter extends RecyclerView.Adapter<QuickBuyAdapter.ViewHolder> {

    private List<QuickBuyItem> dataList;
    private int selectIndex;
    private Context mContext;
    boolean update_edit = false;

    public QuickBuyAdapter(List<QuickBuyItem> dataList, Context context) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_quickbuy, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        final QuickBuyItem oneBuyItem = dataList.get(position);
        if (oneBuyItem.getSelectItem() == null) {
            oneBuyItem.setSelectItem(oneBuyItem.getItems().get(0));
        }

        final QuickBuyItem.QuickItem selectItem = oneBuyItem.getSelectItem();

        if (oneBuyItem.isSelect()) {
            holder.itemSelectImg.setImageResource(R.mipmap.btn_blue);
        } else {
            holder.itemSelectImg.setImageResource(R.mipmap.btn_normal);
        }
//            item_code.setText(oneBuyItem.getBarcode());  隐藏商品条码
        holder.itemName.setText(selectItem.getName());
        holder.salesNum.setText(String.valueOf(oneBuyItem.getStandardInventory()));
        holder.itemInventory.setText(oneBuyItem.getRepertory());
        holder.itemPrice.setText(selectItem.getPrice() + "");
        //textStockPersent.setText(selectItem.getBuyRate());
        //textSellPersent.setText(selectItem.getSaleRate());
        //将所有的单位装进去
        final List<XCDropDownListView.XCDropDownItem> list = holder.itemUnit.getItems();
        holder.itemUnit.setListviewWH(170, 150);
        list.clear();
        for (int i = 0; i < oneBuyItem.getItems().size(); i++) {
            list.add(new XCDropDownListView.XCDropDownItemStr(oneBuyItem.getItems().get(i).getUnit()));
        }
        holder.itemUnit.setShowIndex(selectIndex);

        //item_standard.setText(selectItem.getStandard());
        holder.itemTotalPrice.setText(Arith.mul(Double.parseDouble(selectItem.getPrice()), oneBuyItem.getCount()) + "");
        if (oneBuyItem.getCount() < 0) {
            holder.countEdittext.setText("0");
        } else {
            holder.countEdittext.setText(oneBuyItem.getCount() + "");
        }

        AddAndEditorAddr.hintNum(holder.tvHint, oneBuyItem);//显示周销量和采购值
        //当你选中就改变数据
        holder.itemUnit.setSelectCallback(new XCDropDownListView.SelectCallback() {
            @Override
            public void selected(int index) {
                QuickBuyItem.QuickItem qi = oneBuyItem.getItems().get(index);
                oneBuyItem.setSelectItem(qi);
                selectIndex = index;
                notifyDataSetChanged();
            }
        });


        holder.countEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (update_edit) {
                    update_edit = false;
                    return;
                }
                int v = 0;
                try {
                    v = Integer.parseInt(s.toString());
                } catch (Exception e) {
                    e.printStackTrace();
//                    update_edit = true;
//                    holder.countEdittext.setText("0");
                    return;
                }
                if (v < 0) {
                    v = 0;
                } else if (v > oneBuyItem.getSelectItem().getStock()) {
                    ToastUtil.showToast(mContext, "供应链可购买库存" + oneBuyItem.getSelectItem().getStock());
                    update_edit = true;
                    holder.countEdittext.setText("" + oneBuyItem.getSelectItem().getDefaultPurchase());
                    oneBuyItem.setCount(oneBuyItem.getSelectItem().getDefaultPurchase());
                    return;
                } else if (v > 10000) {
                    ToastUtil.showToast(mContext, "超出最大购买数量10000了");
                    update_edit = true;
                    holder.countEdittext.setText("" + oneBuyItem.getSelectItem().getDefaultPurchase());
                    oneBuyItem.setCount(oneBuyItem.getSelectItem().getDefaultPurchase());
                    return;
                }
                update_edit = true;
//                holder.countEdittext.setText("" + v);
                oneBuyItem.setCount(v);
                //刷新价格
                holder.itemTotalPrice.setText(Arith.mul(oneBuyItem.getCount(), Double.parseDouble(oneBuyItem.getSelectItem().getPrice())) + "");
//                adapter.refreshData();
//                count_edittext.requestFocus();
                holder.itemInventory.setText("" + oneBuyItem.getRepertory());

                refreshData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.countEdittext.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if (holder.countEdittext.getText().toString().equals("") || holder.countEdittext.getText().toString().equals(".")) {
                        holder.countEdittext.setText("0");
                    }
                }
            }
        });

        holder.countDelBtn.setTag(position);
        holder.countDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBuyItem.del();
                AddAndEditorAddr.hintNum(holder.tvHint, oneBuyItem);//显示周销量和采购值
                if (oneBuyItem.getCount() < 0)
                    oneBuyItem.setCount(0);

                notifyDataSetChanged();
            }
        });

        holder.countAddBtn.setTag(position);
        holder.countAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBuyItem.add();
                AddAndEditorAddr.hintNum(holder.tvHint, oneBuyItem);//显示周销量和采购值
                if (oneBuyItem.getCount() > 10000) {
                    oneBuyItem.setCount(10000);
                    ToastUtil.showToast(mContext, "超出最大购买数量10000了");
                } else if (oneBuyItem.getCount() > oneBuyItem.getSelectItem().getStock()) {
                    oneBuyItem.setCount(oneBuyItem.getSelectItem().getStock());
                    if(oneBuyItem.getSelectItem().getStock()<1){
                        ToastUtil.showToast(mContext, "供应链可购买库存为0");
                    }else{
                        ToastUtil.showToast(mContext, "供应链可购买库存" + oneBuyItem.getSelectItem().getStock());
                    }

                }

                notifyDataSetChanged();
            }
        });

        holder.itemSelectImg.setTag(position);
        holder.itemSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oneBuyItem.setSelect(!oneBuyItem.isSelect());
                refreshData();
                notifyDataSetChanged();
            }
        });

        refreshData();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_select_img)
        ImageView itemSelectImg;
        @Bind(R.id.item_select_btn)
        RelativeLayout itemSelectBtn;
        @Bind(R.id.item_code)
        TextView itemCode;
        @Bind(R.id.item_name)
        TextView itemName;
        @Bind(R.id.item_inventory)
        TextView itemInventory;
        @Bind(R.id.sales_num)
        TextView salesNum;
        @Bind(R.id.item_price)
        TextView itemPrice;
        @Bind(R.id.count_del_btn)
        RelativeLayout countDelBtn;
        @Bind(R.id.count_edittext)
        EditText countEdittext;
        @Bind(R.id.count_add_btn)
        RelativeLayout countAddBtn;
        @Bind(R.id.tv_hint)
        TextView tvHint;
        @Bind(R.id.item_unit)
        XCDropDownListView itemUnit;
        @Bind(R.id.item_total_price)
        TextView itemTotalPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void refreshData() {
        int count = 0;
        double price = 0;
        for (int i = 0; i < dataList.size(); i++) {
            QuickBuyItem bi = dataList.get(i);
            if (bi.isSelect()) {
                if (bi.getSelectItem() == null) {
                    bi.setSelectItem(bi.getItems().get(0));
                }
                count += (bi.getCount() < 0 ? 0 : bi.getCount());
                price = Arith.add(price, (bi.getCount() < 0 ? 0 : bi.getCount()) * Double.parseDouble(bi.getSelectItem().getPrice()));
            }
        }
        EventBus.getDefault().post(new QuickBuyEvent(count, price));
    }


}

