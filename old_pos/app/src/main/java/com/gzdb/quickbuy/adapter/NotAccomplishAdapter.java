package com.gzdb.quickbuy.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.util.BaseUtils;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.bean.OrderDetailBean;
import com.gzdb.quickbuy.bean.OrderDetailItemBean;
import com.gzdb.quickbuy.dialog.OrderDetailDialog;
import com.gzdb.quickbuy.dialog.PhoneDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zxy on 2016/12/13.
 *  订单列表适配器
 */

public class NotAccomplishAdapter extends BaseAdapter {

    public Context cx;
    public List<OrderDetailBean> listNb = new ArrayList<>();
    PhoneDialog phoneDialog;
    OrderDetailDialog orderDetailDialog;
    Dialog dialog;

    public NotAccomplishAdapter(Activity at,List<OrderDetailBean> listNb){
       this.cx = at;
        this.listNb = listNb;
    }

    @Override
    public int getCount() {
        return listNb.size();
    }

    @Override
    public Object getItem(int position) {
        return listNb.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHander viewHander = null;
        if (viewHander == null){
            convertView = View.inflate(cx, R.layout.qu_adap_item,null);
            viewHander = new ViewHander((ViewGroup) convertView);
            convertView.setTag(viewHander);
        }else {
            viewHander = (ViewHander) convertView.getTag();
        }
        OrderDetailBean notAccomplishBean = listNb.get(position);
            viewHander.supplier_name.setText(notAccomplishBean.getShippingNickName());//供应商名字
            viewHander.qu_name.setText(notAccomplishBean.getReceiptNickName());        //采购名字
            viewHander.qu_Allmoney.setText(notAccomplishBean.getTotalPrice()+""); //总金额
            viewHander.merchandise_allNum.setText("...等"+notAccomplishBean.getItemSnapshots().size()+"件商品");
            viewHander.qu_orderNum.setText(notAccomplishBean.getSequenceNumber()+""); //采购订单
            viewHander.qu_orderTime.setText(BaseUtils.convertToStrSS(notAccomplishBean.getCreateTime())); //下单时间
//            viewHander.qu_already_quantity.setText(notAccomplishBean.getRec()+""); //已收货
//            viewHander.qu_middle_quantity.setText(notAccomplishBean.getTotalDeliveryQuantity()+"");
//            viewHander.qu_not_quantity.setText(notAccomplishBean.getTotalNotDeliveryQuantity()+"");

        if (notAccomplishBean.getItemSnapshots().size() > 1){
            //2
            OrderDetailItemBean itemsBean=notAccomplishBean.getItemSnapshots().get(1);
            viewHander.two_merchandise_name.setText(itemsBean.getItemName());
            viewHander.two_merchandise_quantity.setText(itemsBean.getNormalQuantity()+"");
            viewHander.rl_two.setVisibility(View.VISIBLE);
        }
        if (notAccomplishBean.getItemSnapshots().size() > 0){
            //1
            OrderDetailItemBean itemsBean=notAccomplishBean.getItemSnapshots().get(0);
            viewHander.one_merchandise_name.setText(itemsBean.getItemName());
            viewHander.one_merchandise_quantity.setText(itemsBean.getNormalQuantity()+"");
            viewHander.rl_one.setVisibility(View.VISIBLE);
        }
        int orderStatus = notAccomplishBean.getStatus();
        if (orderStatus == 1){
            viewHander.qu_mode.setText("待支付");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_select_red);
        }else if (orderStatus ==2){
            viewHander.qu_mode.setText("已取消");
            viewHander.qu_mode.setBackgroundResource(R.color.font_hint);
        }else if (orderStatus ==4){
            viewHander.qu_mode.setText("订单已失效");
            viewHander.qu_mode.setBackgroundResource(R.color.font_hint);
        }else if (orderStatus ==8){
            viewHander.qu_mode.setText("已支付");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_blue);
        }else if (orderStatus ==16){
            viewHander.qu_mode.setText("已接单");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }else if (orderStatus ==32){
            viewHander.qu_mode.setText("发货中");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_green);
        }else if (orderStatus ==64){
            viewHander.qu_mode.setText("配送中");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }else if (orderStatus ==128){
            viewHander.qu_mode.setText("送达");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }else if (orderStatus ==256){
            viewHander.qu_mode.setText("确认收货");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }else if (orderStatus ==512){
            viewHander.qu_mode.setText("已收款");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }else if (orderStatus ==1024){
            viewHander.qu_mode.setText("分批配送");
            viewHander.qu_mode.setBackgroundResource(R.drawable.bg_list_orange);
        }
        viewHander.notAccomplishBean = notAccomplishBean;

        viewHander.ly_phone.setOnClickListener(viewHander);
        viewHander.ly_conten.setOnClickListener(viewHander);
        return convertView;
    }

    public class ViewHander implements View.OnClickListener {
        TextView qu_mode,qu_orderNum,qu_orderTime,qu_name,one_merchandise_name,one_merchandise_quantity
                ,two_merchandise_name,two_merchandise_quantity,merchandise_allNum,qu_already_quantity
                ,qu_middle_quantity,qu_not_quantity,supplier_name,qu_Allmoney;
        LinearLayout ly_phone,ly_conten;
        RelativeLayout rl_one,rl_two;
        OrderDetailBean notAccomplishBean;

       public ViewHander(ViewGroup vg){
            qu_mode  = (TextView) vg.findViewById(R.id.qu_mode);
            qu_orderNum  = (TextView) vg.findViewById(R.id.qu_orderNum);
            qu_orderTime  = (TextView) vg.findViewById(R.id.qu_orderTime);
            qu_name = (TextView) vg.findViewById(R.id.qu_name);
            one_merchandise_name  = (TextView) vg.findViewById(R.id.one_merchandise_name);
            one_merchandise_quantity  = (TextView) vg.findViewById(R.id.one_merchandise_quantity);
            two_merchandise_name  = (TextView) vg.findViewById(R.id.two_merchandise_name);
            two_merchandise_quantity  = (TextView) vg.findViewById(R.id.two_merchandise_quantity);
            merchandise_allNum  = (TextView) vg.findViewById(R.id.merchandise_allNum);
            qu_already_quantity  = (TextView) vg.findViewById(R.id.qu_already_quantity);
            qu_middle_quantity  = (TextView) vg.findViewById(R.id.qu_middle_quantity);
            qu_not_quantity  = (TextView) vg.findViewById(R.id.qu_not_quantity);
            supplier_name  = (TextView) vg.findViewById(R.id.supplier_name);
            qu_Allmoney  = (TextView) vg.findViewById(R.id.qu_Allmoney);
            ly_phone  = (LinearLayout) vg.findViewById(R.id.ly_phone);
           ly_conten  = (LinearLayout) vg.findViewById(R.id.ly_conten);
           rl_one  = (RelativeLayout) vg.findViewById(R.id.rl_one);
           rl_two  = (RelativeLayout) vg.findViewById(R.id.rl_two);
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ly_phone:
                    if (phoneDialog == null){
                        phoneDialog = new PhoneDialog(cx, notAccomplishBean.getShippingPhone());
                    }
                    phoneDialog.show();
                    break;
                case R.id.ly_conten:
                    if(orderDetailDialog == null) {
                        orderDetailDialog = new OrderDetailDialog(cx);
                    }
                    orderDetailDialog.refreshOrderDetail(notAccomplishBean);
                    orderDetailDialog.show();
                    break;
            }
        }
    }
}
