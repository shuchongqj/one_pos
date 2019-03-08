package com.gzdb.supermarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.supermarket.SupermarketShopView;
import com.gzdb.supermarket.adapter.SingleLeftAdapter;
import com.gzdb.supermarket.adapter.SingleRightAdapter;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.cache.ShopCart;
import com.gzdb.supermarket.util.Utils;

/**
 * 挂单界面
 * Created by Even on 2016/5/31.
 */
public class SingleDialog extends Dialog implements View.OnClickListener {

    private SupermarketShopView supermarketShopView;

    SingleRightAdapter singleRightAdapter;
    SingleLeftAdapter singleLeftAdapter;
    ListView singleLeft;
    ListView singleRight;
    private Context mContext;

    public SingleDialog(Context context) {
        super(context, R.style.dialog);
        setContentView(R.layout.single_dialog);
        setCanceledOnTouchOutside(true);
        mContext=context;
        Button delete = (Button) findViewById(R.id.delete_order);
        ImageView close = (ImageView) findViewById(R.id.close);
        Button single = (Button) findViewById(R.id.single);

        delete.setOnClickListener(this);
        close.setOnClickListener(this);
        single.setOnClickListener(this);

        singleLeft = (ListView) findViewById(R.id.single_left);
        singleRight = (ListView) findViewById(R.id.single_right);

        singleLeftAdapter = new SingleLeftAdapter(context, ShopCart.shopCarts);
        singleLeft.setAdapter(singleLeftAdapter);


        singleRightAdapter = new SingleRightAdapter(context);
        singleRight.setAdapter(singleRightAdapter);

        singleLeftAdapter.setSingleRightAdapter(singleRightAdapter);


    }

    public void show() {
        //刷新

        singleRightAdapter.setList(ShopCart.shopCarts.get(0).gets());
        if(ShopCart.shopCarts.size()>0){

            singleLeftAdapter.setClickPossition(0);
        }
        super.show();
    }

    public void setSupermarketShopView(SupermarketShopView shopView) {
        this.supermarketShopView = shopView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.delete_order) {
            if (ShopCart.shopCarts.size() < 1){
                return;
            }
            if (singleLeftAdapter.getClickPossition() < 0){
                ToastUtil.showEorr(mContext,"请选择营业员！");
                return;
            }
            if (singleLeftAdapter.getClickPossition() >= ShopCart.shopCarts.size()){
                ToastUtil.showEorr(mContext,"请选择营业员！");
                return;
            }
            ShopCart.shopCarts.remove(singleLeftAdapter.getClickPossition());
            //重新保存
            ShopCart.outputAll(this.getContext());
            //如果还有数据
            if (ShopCart.shopCarts.size() > 0) {
                int index = singleLeftAdapter.getClickPossition() - 1;
                if (index < 0) {
                    index = 0;
                }
                singleLeftAdapter.setClickPossition(index);
                singleRightAdapter.setList(ShopCart.shopCarts.get(index).gets());
            } else {
                //刷新
                singleLeftAdapter.setClickPossition(-1);
                singleRightAdapter.setList(null);
            }
        } else if (id == R.id.single) {
            //取单
            if (ShopCart.shopCarts.size() < 1){
                return;
            }
            if (singleLeftAdapter.getClickPossition() < 0){
                ToastUtil.showEorr(mContext,"请选择营业员！");
                return;
            }
            if (singleLeftAdapter.getClickPossition() >= ShopCart.shopCarts.size()){
                ToastUtil.showEorr(mContext,"请选择营业员！");
                return;
            }
            //如果当前购物车有内容
            if (ShopCart.nowShopCart.getCartItemCount() > 0) {
                ToastUtil.showToast(this.getContext(), "当前购物车有商品，请清空商品再取单！");
                return;
            }
            ShopCart shopCart = ShopCart.shopCarts.get(singleLeftAdapter.getClickPossition());
            //拷贝进当前的购物车
            ShopCart.nowShopCart.copy(shopCart);
            //删除原来的
            ShopCart.shopCarts.remove(singleLeftAdapter.getClickPossition());

            if(!Sunmi.viceScreenMode) {
                try {
                    for(int i =0;i<=shopCart.getAllCount();i++){

                        if(i == shopCart.getAllCount()) {
                            GoodBean item = shopCart.get(i-1).item;
                            App.mSerialPortOperaion.WriteData(0xC);
                            String str = item.getSalesPrice()+"";
                            Log.e("str", str);
                            App.mSerialPortOperaion.WriteData(27, 81, 65);
                            App.mSerialPortOperaion.WriteData(Utils.input(str));
                            App.mSerialPortOperaion.WriteData(13);
                            App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X31);//单价
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (supermarketShopView != null) {
                supermarketShopView.refreshShopCart();
            }
            dismiss();
        }else if(id == R.id.close) {
            dismiss();
        }
    }
}