package com.gzdb.quickbuy.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.AddressBean;

import java.util.List;

/**
 * Created by liubolin on 2018/1/31.
 */

public class AddressAdapter extends BaseAdapter {

    private Context context;

    private List<AddressBean> datas;

    private AddressAdapterHost host;

    private boolean isSelect = false;

    public interface AddressAdapterHost{
        void updateAddress(AddressBean bean);
        void deleteAddress(long id);
        void defaultAddress(long id);
    }

    public void setAddressAdapterHost(AddressAdapterHost host) {
        this.host = host;
    }

    public AddressAdapter(Context context, List<AddressBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    public AddressAdapter(Context context, List<AddressBean> datas, boolean isSelect) {
        this(context, datas);
        this.isSelect = isSelect;
    }

    public void setDatas(List<AddressBean> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<AddressBean> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_quickbuy_address, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        AddressBean bean = (AddressBean) getItem(position);
        holder.bean = bean;
        holder.address_name.setText(bean.getName());
        holder.address_phone.setText(bean.getPhoneNumber());
        holder.address_more.setText(bean.getAddressAlias() + " " + bean.getAddress());

        if (bean.getStatus() == 2) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.public_btn_multiselect);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.default_address.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.public_btn_multiunselect);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.default_address.setCompoundDrawables(drawable, null, null, null);
        }

        if (isSelect) {
            holder.default_address.setVisibility(View.GONE);
            holder.btn_delete_address.setVisibility(View.GONE);
            holder.btn_update_address.setVisibility(View.GONE);
        }

        return convertView;
    }

    class Holder implements View.OnClickListener {

        AddressBean bean;

        TextView address_name;
        TextView address_phone;
        TextView address_more;
        TextView default_address;

        View btn_delete_address;
        View btn_update_address;

        public Holder(View view) {
            address_name = (TextView) view.findViewById(R.id.address_name);
            address_phone = (TextView) view.findViewById(R.id.address_phone);
            address_more = (TextView) view.findViewById(R.id.address_more);
            default_address = (TextView) view.findViewById(R.id.default_address);

            btn_delete_address = view.findViewById(R.id.btn_delete_address);
            btn_update_address = view.findViewById(R.id.btn_update_address);

            btn_delete_address.setOnClickListener(this);
            btn_update_address.setOnClickListener(this);
            default_address.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_delete_address) {
                // 删除地址
                host.deleteAddress(bean.getId());
            } else if (id == R.id.btn_update_address) {
                // 修改地址
                host.updateAddress(bean);
            } else if (id == R.id.default_address) {
                // 选中默认地址
                if (bean.getStatus() == 2) {
                    return;
                }
                host.defaultAddress(bean.getId());
            }
        }
    }
}
