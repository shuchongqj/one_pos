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

import com.apkfuns.logutils.LogUtils;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseActivity;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.util.Arith;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 上下架输入商品信息
 */
public class DrapUpActivity extends BaseActivity {


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
    @Bind(R.id.vip_price)
    EditText vipPrice;
    @Bind(R.id.updown)
    XCDropDownListView updown;
    @Bind(R.id.ll_select_sell_type)
    View ll_select_sell_type;
    List<XCDropDownListView.XCDropDownItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_drapup_dialog);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        ll_select_sell_type.setVisibility(View.VISIBLE);
        updown.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp190), getResources().getDimensionPixelSize(R.dimen.dp240));
        initView();
        initSellType();
    }

    private void initView() {

        item = app.getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Id.eq(getIntent().getStringExtra("itemId"))).unique();
        LogUtils.e(item);
        editGoodsname.setText(item.getItemName());
        editCode.setText(item.getBarcode());
        editPrice.setText(item.getSalesPrice() + "");
        editStockPrice.setText(item.getStockPrice() + "");
        vipPrice.setText(item.getMembershipPrice() + "");

        if (item.getItemType() == 2) {
            editRepertory.setText(Arith.div(item.getRepertory(), 100) + "");
        } else {
            editRepertory.setText(item.getRepertory() + "");
        }

    }

    private void initSellType() {
        list = updown.getItems();
        list.add(new XCDropDownListView.XCDropDownItemStr("线下"));
        list.add(new XCDropDownListView.XCDropDownItemStr("线上"));
        list.add(new XCDropDownListView.XCDropDownItemStr("线上线下通用"));

        if (item != null) {
            if (item.getSellType().equals("线下")) {
                updown.setShowIndex(0);

            } else if (item.getSellType().equals("线上")) {
                updown.setShowIndex(1);

            } else if (item.getSellType().equals("线上线下通用")) {
                updown.setShowIndex(2);

            }
        }

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

    @OnClick({R.id.btn_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    public void submit() {
        final String iName = null;
        double dPrice = 0;
        double repertory = 0;
        try {
            dPrice = Double.parseDouble(editPrice.getText().toString());
            if (dPrice <= 0) {
                ToastUtil.showToast(DrapUpActivity.this, "售价有误，请重新输入");
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(DrapUpActivity.this, "售价有误，请重新输入");
            return;
        }
        try {
            dPrice = Double.parseDouble(editStockPrice.getText().toString());
            if (dPrice <= 0) {
                ToastUtil.showToast(DrapUpActivity.this, "售价有误，请重新输入");
                return;
            }
        } catch (Exception e) {
            ToastUtil.showToast(DrapUpActivity.this, "进价有误，请重新输入");
            return;
        }
        try {
            repertory = Double.parseDouble(editRepertory.getText().toString());
        } catch (Exception e) {
            ToastUtil.showToast(DrapUpActivity.this, "库存有误，请重新输入");
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
        if (updown.getSelectedItem().getXCDropDownItemText().equals("线下")) {
            httpParams.put("sellType", 0);

        } else if (updown.getSelectedItem().getXCDropDownItemText().equals("线上")) {
            httpParams.put("sellType", 1);

        } else if (updown.getSelectedItem().getXCDropDownItemText().equals("线上线下通用")) {
            httpParams.put("sellType", 2);

        }

        double vipPrice = Double.valueOf(vPrice);
        double price = Double.valueOf(editPrice.getText().toString());
        if (vipPrice >= price) {
            ToastUtil.showToast(mContext, "会员价必须小于售价");
            return;
        }

        OkGo.<NydResponse<GoodBean>>post(Contonts.URL_EDIT_GOOD)
                .params(httpParams)
                .execute(new DialogCallback<NydResponse<GoodBean>>(this) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodBean>> response) {
                        GoodBean goodBean = response.body().response;
                        goodBean.setState(item.getState());
                        goodBean.setActivityType(item.getActivityType());
                        goodBean.setActivityId(item.getActivityId());
                        goodBean.setDiscount(item.getDiscount());
                        LogUtils.e(goodBean);
                        App.getDaoInstant().getGoodBeanDao().insertOrReplace(goodBean);
                        ToastUtil.showSuccess(mContext, "上架成功！");
                        setResult(SupermarketIndexActivity.requestCode_UPDATE_ALL);
                        finish();
                    }
                });
    }
}
