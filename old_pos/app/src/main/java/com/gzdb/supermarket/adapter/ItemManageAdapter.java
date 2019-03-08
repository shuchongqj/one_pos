package com.gzdb.supermarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.been.GoodBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubolin on 2018/5/16.
 */

public class ItemManageAdapter extends BaseAdapter {

    Context context;
    List<ItemManageDao> list = new ArrayList<>();

    private ItemManageAdapterHost mHost;

    public interface ItemManageAdapterHost{
        void editItem(GoodBean item);
        void stampItem(GoodBean item);
        void deleteItem(GoodBean item);
    }

    public void setHost(ItemManageAdapterHost host) {
        this.mHost = host;
    }

    public List<ItemManageDao> getSelectList() {
        List<ItemManageDao> sList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect) {
                sList.add(list.get(i));
            }
        }
        return sList;
    }

    public ItemManageAdapter(Context context) {
        this.context = context;
    }

    public void clearList() {
        list.clear();
    }

    public void setList(List<GoodBean> goodBeanList) {
        this.list.clear();
        ItemManageDao dao = null;
        for (int i = 0; i < goodBeanList.size(); i++) {
            dao = new ItemManageDao();
            dao.item = goodBeanList.get(i);
            this.list.add(dao);
        }
    }

    public void selectAll(boolean isAll) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).isSelect = isAll;
        }
        notifyDataSetChanged();
    }

    public void notifyDataSort() {

        if (list.size() < 1) {
            super.notifyDataSetChanged();
            return;
        }

        List<GoodBean> items = new ArrayList<>();

        //将原来的-1位置的内容先不加进去
        for (int i = 0; i < list.size(); i++) {
            GoodBean item = ((ItemManageDao) getItem(i)).item;
            if (-1 == item.getSortId()) {
                continue;
            }
            if ("Y".equals(item.getIsShelve())) {
                items.add(item);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            GoodBean item = ((ItemManageDao) getItem(i)).item;
            if (-1 == item.getSortId()) {
                continue;
            }
            if ("N".equals(item.getIsShelve())) {
                items.add(item);
            }
        }

        List<GoodBean> mos = new ArrayList<>();

        int rowCount = -1;
        boolean addb = false;
        for (int i = 0; i < items.size(); i++) {
            GoodBean mo = items.get(i);
            rowCount++;
            if (rowCount == 4) {
                rowCount = 0;
            }
            if ("N".equals(mo.getIsShelve())) {
                if (!addb && rowCount > 0) {
                    int num = 4 - rowCount;
                    if (num > 0) {
                        for (int y = 0; y < num; y++) {
                            GoodBean mot = new GoodBean();
                            mot.setSortId(-1);
                            mos.add(mot);
                        }
                        rowCount = 0;
                    }
                }
                addb = true;
                mos.add(mo);
            } else {
                if ("Y".equals(mo.getIsShelve())) {
                    mos.add(mo);
                }
            }
        }
//        setList(mos);

        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_item_manage, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.item_id.setText(String.valueOf(position + 1));
        holder.reView((ItemManageDao) getItem(position));


        return convertView;
    }

    public class ItemManageDao {
        public GoodBean item;
        public boolean isSelect = false;
    }

    class Holder implements View.OnClickListener {
        ItemManageDao item;

        ImageView all_select_img;
        TextView item_id;
        TextView item_name;
        TextView item_code;
        TextView item_type;
        TextView bid_price;
        TextView selling_price;
        TextView vip_price;
        TextView stock_num;
        TextView market_type;

        View btn_edit;
        View btn_stamp;
        View btn_delete;

        private Holder(View view) {

            all_select_img = (ImageView) view.findViewById(R.id.all_select_img);
            item_id = (TextView) view.findViewById(R.id.item_id);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_code = (TextView) view.findViewById(R.id.item_code);
            item_type = (TextView) view.findViewById(R.id.item_type);
            bid_price = (TextView) view.findViewById(R.id.bid_price);
            selling_price = (TextView) view.findViewById(R.id.selling_price);
            vip_price= (TextView) view.findViewById(R.id.vip_price);
            stock_num = (TextView) view.findViewById(R.id.stock_num);
            market_type = (TextView) view.findViewById(R.id.market_type);

            btn_edit = view.findViewById(R.id.btn_edit);
            btn_stamp = view.findViewById(R.id.btn_stamp);
            btn_delete = view.findViewById(R.id.btn_delete);

            btn_edit.setOnClickListener(this);
            btn_stamp.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            all_select_img.setOnClickListener(this);
        }

        private void reView(ItemManageDao items) {
            this.item = items;
            item_name.setText(item.item.getItemName());
            item_code.setText(item.item.getBarcode());
            item_type.setText(item.item.getPosTypeName());
            bid_price.setText(String.valueOf(item.item.getStockPrice()));
            selling_price.setText(String.valueOf(item.item.getSalesPrice()));
            if(items.item.getMembershipPrice()>0){
                vip_price.setText(String.valueOf(items.item.getMembershipPrice()));
            }else{
                vip_price.setText("- -");
            }
            if(item.item.getItemType()==2){
                stock_num.setText(String.valueOf(item.item.getRepertory()/100));
            }else{
                stock_num.setText(String.valueOf(item.item.getRepertory()));
            }

            //销售类型
            market_type.setText(item.item.getSellType());
//                if (items.item.getSellType() == 0) {
//                    market_type.setText("线下");
//                } else if (items.item.getSellType() == 1) {
//                    market_type.setText("线上");
//                } else if(items.item.getSellType() == 2){
//                    market_type.setText("线上线下通用");
//                }


            if (item.isSelect) {
                all_select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.btn_blue));
            } else {
                all_select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.btn_normal));
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (null == mHost) {
                return;
            }
            if (id == R.id.btn_edit) {
                mHost.editItem(item.item);
            } else if (id == R.id.btn_stamp) {
                mHost.stampItem(item.item);
            } else if (id == R.id.btn_delete) {
                mHost.deleteItem(item.item);
            } else if (id == R.id.all_select_img) {
                item.isSelect = !item.isSelect;
                if (item.isSelect) {
                    all_select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.btn_blue));
                } else {
                    all_select_img.setImageDrawable(context.getResources().getDrawable(R.mipmap.btn_normal));
                }
            }
        }
    }

}
