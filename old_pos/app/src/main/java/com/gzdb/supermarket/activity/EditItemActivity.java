package com.gzdb.supermarket.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.GoodTypesBean;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.dialog.SelectDialog;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 上下架输入商品信息
 */
public class EditItemActivity extends BaseActivity implements SelectDialog.SelectDialogHost {

    GoodBean item;
    @Bind(R.id.edit_goodsname)
    EditText editGoodsname;
    @Bind(R.id.edit_code)
    TextView editCode;
    @Bind(R.id.edit_price)
    EditText editPrice;
    @Bind(R.id.edit_stockPrice)
    EditText editStockPrice;
    @Bind(R.id.edit_repertory)
    EditText editRepertory;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.common_dialog_layout)
    LinearLayout commonDialogLayout;
    @Bind(R.id.ll_select_item_type)
    View ll_select_item_type;
    @Bind(R.id.tv_item_type)
    TextView tv_item_type;
    @Bind(R.id.vip_price)
    EditText vipPrice;
    @Bind(R.id.ll_select_sell_type)
    View ll_select_sell_type;
    @Bind(R.id.updown)
    XCDropDownListView updown;


    SelectDialog mSelectDialog;
    SelectDialog mSelectDialog1;
    List<GoodTypesBean> itemTypes = new ArrayList<>();
    List<String> itemType = new ArrayList<>();
     List<XCDropDownListView.XCDropDownItem> list;

    int selectIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drapup_dialog);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        initView();
    }

    private void initView() {
        ll_select_item_type.setVisibility(View.VISIBLE);
        ll_select_sell_type.setVisibility(View.VISIBLE);
        item = app.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Id.eq(getIntent().getStringExtra("itemId"))).unique();
        mSelectDialog = new SelectDialog(this, this);
        mSelectDialog1 = new SelectDialog(this, this);

        editGoodsname.setText(item.getItemName());
        editCode.setText(item.getBarcode());
        editPrice.setText(item.getSalesPrice() + "");
        editStockPrice.setText(item.getStockPrice() + "");
        vipPrice.setText(item.getMembershipPrice() + "");
        if (item.getItemType() == 2) {
            editRepertory.setText((int)Arith.div(item.getRepertory(), 100) + "");
        } else {
            editRepertory.setText(item.getRepertory() + "");
        }
        tv_item_type.setText(item.getPosTypeName());
        updown.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp190), getResources().getDimensionPixelSize(R.dimen.dp240));


//        if(item.getSellType() == 0){
//            tv_sell_type.setText("线下");
//        }else if(item.getSellType() == 1){
//            tv_sell_type.setText("线上");
//        }else if(item.getSellType() == 2){
//            tv_sell_type.setText("线上线下通用");
//        }

        initType();
        initSellType();
    }

    private void initType() {
        itemType.clear();
        itemTypes = app.getDaoInstant().getGoodTypesBeanDao().loadAll();
        for (int i = 0; i < itemTypes.size(); i++) {
            itemType.add(itemTypes.get(i).getTitle());
        }
        mSelectDialog.setListData(itemType);
    }
    private void initSellType() {
            list = updown.getItems();
            list.add(new XCDropDownListView.XCDropDownItemStr("线下"));
            list.add(new XCDropDownListView.XCDropDownItemStr("线上"));
            list.add(new XCDropDownListView.XCDropDownItemStr("线上线下通用"));
            updown.setShowIndex(0);
            updown.notifyDataChange();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editGoodsname.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editPrice.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editStockPrice.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_submit, R.id.ll_select_item_type,R.id.ll_select_sell_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
            case R.id.ll_select_item_type:
                mSelectDialog.showViewSize(tv_item_type);
                mSelectDialog.showAsDropDown(tv_item_type);
                break;
            case R.id.ll_select_sell_type:
                break;
            default:
                break;
        }
    }

    public void submit() {
        String iName = null;
        double dPrice = 0;
        double repertory = 0;
        try {
            dPrice = Double.parseDouble(editPrice.getText().toString());
            if (dPrice <= 0) {
                ToastUtil.showToast(EditItemActivity.this, "售价有误，请重新输入");
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(EditItemActivity.this, "售价有误，请重新输入");
            return;
        }
        try {
            dPrice = Double.parseDouble(editStockPrice.getText().toString());
            if (dPrice <= 0) {
                ToastUtil.showToast(EditItemActivity.this, "售价有误，请重新输入");
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(EditItemActivity.this, "进价有误，请重新输入");
            return;
        }
        try {
            repertory = Integer.parseInt(editRepertory.getText().toString());
        } catch (Exception e) {
            ToastUtil.showToast(EditItemActivity.this, "库存有误，请重新输入");
            return;
        }
        HttpParams httpParams = new HttpParams();
        httpParams.put("id", item.getId());
        httpParams.put("barcode", editCode.getText().toString());
        httpParams.put("itemName", editGoodsname.getText().toString());
        if (item.getItemType() == 2) {
            repertory = (int) Arith.mul(repertory, 100);
        }
        httpParams.put("repertory", (int) repertory);//库存
        httpParams.put("stockPrice", editStockPrice.getText().toString());//进货价
        httpParams.put("isShelve", "Y");
        httpParams.put("salesPrice", editPrice.getText().toString());//售价
        String vPrice = vipPrice.getText().toString();
        if (vPrice.equals("")) {
            vPrice = "0";
        }
        httpParams.put("membershipPrice", vPrice);




        double vipPrice = Double.valueOf(vPrice);
        double price = Double.valueOf(editPrice.getText().toString());
        if (vipPrice >= price) {
            ToastUtil.showToast(mContext, "会员价必须小于售价");
            return;
        }

        if (-1 != selectIndex) {
            httpParams.put("posTypeId", itemTypes.get(selectIndex).getId());
            httpParams.put("posTypeName", itemTypes.get(selectIndex).getTitle());
            httpParams.put("itemTypeId", itemTypes.get(selectIndex).getItemTypeId());
        }

        if(updown.getSelectedItem().getXCDropDownItemText().equals("线下")){
            httpParams.put("sellType",0);

        }else if(updown.getSelectedItem().getXCDropDownItemText().equals("线上")){
            httpParams.put("sellType",1);

        }else if(updown.getSelectedItem().getXCDropDownItemText().equals("线上线下通用")){
            httpParams.put("sellType",2);

        }




        OkGo.<NydResponse<GoodBean>>post(Contonts.URL_EDIT_GOOD)
                .params(httpParams)
                .execute(new DialogCallback<NydResponse<GoodBean>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodBean>> response) {
                        App.getDaoInstant().getGoodBeanDao().insertOrReplace(response.body().response);
                        ToastUtil.showSuccess(mContext, "修改成功！");
                        setResult(ItemManageActivity.CODE_UPDATE_ALL);
                        finish();
                    }
                });
    }

    @Override
    public void selectDialog(int position) {
        selectIndex = position;
        tv_item_type.setText(itemType.get(position));
    }
}
