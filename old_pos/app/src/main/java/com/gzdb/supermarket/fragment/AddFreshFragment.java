package com.gzdb.supermarket.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.supermarket.activity.AddItemTypeActivity;
import com.gzdb.supermarket.activity.AddItemUnitActivity;
import com.gzdb.supermarket.activity.SearchItemActivity;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.been.GoodListBean;
import com.gzdb.supermarket.been.GoodTypesBean;
import com.gzdb.supermarket.been.GoodUnitBean;
import com.gzdb.supermarket.common.XCDropDownListView;
import com.gzdb.supermarket.event.RefreshProductEvent;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.xw.repo.XEditText;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
/**
 * A simple {@link Fragment} subclass.
 */
public class AddFreshFragment extends BaseFragment {


    @Bind(R.id.goods_code)
    XEditText goodsCode;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.name)
    XEditText name;
    @Bind(R.id.stcokPrice)
    XEditText stcokPrice;
    @Bind(R.id.selling_price)
    XEditText sellingPrice;
    @Bind(R.id.stock)
    XEditText stock;
    @Bind(R.id.classification)
    XCDropDownListView classification;
    @Bind(R.id.add_type)
    Button addType;
    @Bind(R.id.unit)
    XCDropDownListView unit;
    @Bind(R.id.add_unit)
    Button addUnit;
    @Bind(R.id.edit_warnInventory)
    XEditText editWarnInventory;
    @Bind(R.id.date_of_manufacture)
    TextView dateOfManufacture;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.shelf_life)
    XEditText shelfLife;
    @Bind(R.id.select_data)
    XCDropDownListView selectData;
    @Bind(R.id.updown)
    XCDropDownListView updown;
    @Bind(R.id.cancel)
    Button cancel;
    @Bind(R.id.submit)
    Button submit;

    //商品分类ID
    String itemTypeId;
    //商品模板ID
    String itemTemplateId;
    String item_img;

    Dialog dialog = null;
    Dialog showdialog = null;

    private String unitId;

    public AddFreshFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_fresh, container, false);
        ButterKnife.bind(this, view);

        dialog = DialogUtil.loadingDialog(mContext, "请稍候");
        showdialog = DialogUtil.loadingDialog(mContext, "正在提交");
        initView();
        return view;
    }


    private void initView() {

        BaseUtils.setEditTextInhibitInputSpeChat(goodsCode);//控制不能输入特殊字符

        dateOfManufacture.setText("2017年01月05日");
        goodsCode.setOnFocusChangeListener(onFocusChangeListener);

        initTypeAndUnit();//加载商品分类跟单位
        setupdown();
        setselectdata();

        updown.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp190), getResources().getDimensionPixelSize(R.dimen.dp240));
        classification.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp190), getResources().getDimensionPixelSize(R.dimen.dp240));
        selectData.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp80), getResources().getDimensionPixelSize(R.dimen.dp100));
        unit.setListviewWH(getResources().getDimensionPixelSize(R.dimen.dp190), getResources().getDimensionPixelSize(R.dimen.dp240));

//        if (getIntent() != null && getIntent().getStringExtra("barcode") != null) {//从首页
//            goodsCode.setText(getIntent().getStringExtra("barcode"));
//            search();
//        }

    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                if(goodsCode==null){
                    return;
                }
                String code = goodsCode.getText().toString();
                if (code == null || code.trim().length() < 1) return;
                search();
            }
        }
    };

    private void initTypeAndUnit() {
        setunit(app.getDaoInstant().getGoodUnitBeanDao().loadAll());
        setclassification(app.getDaoInstant().getGoodTypesBeanDao().loadAll());
    }

    private void setunit(List<GoodUnitBean> tempList) {
        final List<XCDropDownListView.XCDropDownItem> list = unit.getItems();
        list.clear();
        GoodUnitBean itemType = new GoodUnitBean();
        itemType.setTitle("-选择商品单位-");
        itemType.setId("0");
        list.add(itemType);
        list.addAll(tempList);
        unit.notifyDataChange();
        unit.setShowIndex(0);
    }

    private void setclassification(List<GoodTypesBean> tempList) {

        final List<XCDropDownListView.XCDropDownItem> list = classification.getItems();
        list.clear();
        GoodTypesBean itemType = new GoodTypesBean();
        itemType.setTitle("-选择商品分类-");
        itemType.setId("0");
        list.add(itemType);
        list.addAll(tempList);
        classification.notifyDataChange();
        classification.setShowIndex(0);
    }


    private void setselectdata() {
        final List<XCDropDownListView.XCDropDownItem> list = selectData.getItems();
        list.add(new XCDropDownListView.XCDropDownItemStr("天"));
        list.add(new XCDropDownListView.XCDropDownItemStr("月"));
        selectData.setShowIndex(0);
        selectData.notifyDataChange();
    }


    private void setupdown() {
        final List<XCDropDownListView.XCDropDownItem> list = updown.getItems();
        list.add(new XCDropDownListView.XCDropDownItemStr("上架"));
        list.add(new XCDropDownListView.XCDropDownItemStr("下架"));
        updown.setShowIndex(0);
        updown.notifyDataChange();
    }

    public void search() {
        if (BaseUtils.isEmpty(goodsCode.getText().toString())) {
            ToastUtil.showEorr(mContext, "请输入条形码！");
            return;
        }
        dialog.show();
        OkGo.<NydResponse<GoodListBean>>post(Contonts.URL_ADD_FIND_GOOD)
                .tag(getClass().getSimpleName())
                .params("barcode", goodsCode.getText().toString())
                .execute(new DialogCallback<NydResponse<GoodListBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        dialog.cancel();
                        if (response.body().response.getDatas().size() == 0) {
                            ToastUtil.showToast(mContext, "没有匹配的模板数据！");
                            stcokPrice.setText("");
                            name.setText("");
                            sellingPrice.setText("");
                            stock.setText("");
                            return;
                        }
                        if (response.body().response.getDatas().size() == 1) {
                            refreshData(response.body().response.getDatas().get(0));
                            return;
                        }
                        Intent intent = new Intent(mContext, SearchItemActivity.class);
                        intent.putExtra("beanList", new Gson().toJson(response.body().response.getDatas()));
                        startActivityForResult(intent, 101);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodListBean>> response) {
                        super.onError(response);
                        dialog.cancel();
                    }
                });
    }

    /***
     * 生产日期
     */
    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(getActivity());
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(mContext, 20));
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        picker.setRangeEnd(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        picker.setRangeStart(1970, 1, 1);
        String tempDate = dateOfManufacture.getText().toString();
        if (BaseUtils.isEmpty(tempDate)) {
            picker.setSelectedItem(2017, 1, 1);
        } else {
            picker.setSelectedItem(Integer.valueOf(tempDate.substring(0, 4)), Integer.valueOf(tempDate.substring(6, tempDate.indexOf("月"))), Integer.valueOf(tempDate.substring(tempDate.indexOf("月") + 1, tempDate.length() - 1)));
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                dateOfManufacture.setText(year + "年" + picker.getSelectedMonth() + "月" + picker.getSelectedDay() + "日");
                try {
                    LogUtils.e("" + Utils.dateToStamp(dateOfManufacture.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "年" + picker.getSelectedMonth() + "月" + picker.getSelectedDay() + "日");
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "年" + month + "月" + picker.getSelectedDay() + "日");
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "年" + picker.getSelectedMonth() + "月" + day + "日");
            }
        });
        picker.show();
    }

    /***
     * 确认添加
     */
    private void setSubmit() {
        GoodTypesBean typeBean = ((GoodTypesBean) classification.getSelectedItem());
        GoodUnitBean unitBean = ((GoodUnitBean) unit.getSelectedItem());
        HttpParams httpParams = new HttpParams();
        httpParams.put("itemType", 2);
        httpParams.put("itemName", name.getText().toString().trim());
        httpParams.put("barcode", app.getCurrentUser().getPassportId() + System.currentTimeMillis());// 生成20位的条码
        httpParams.put("posTypeId", typeBean.getId());
        httpParams.put("posTypeName", typeBean.getTitle());
        httpParams.put("itemTypeId", typeBean.getItemTypeId());
        httpParams.put("itemUnitId", 10065);         //{"id":10065,"title":"kg","status":0}
        httpParams.put("itemUnitName", unitBean.getTitle());
        try {
            httpParams.put("generatedDate", Utils.dateToStamp(dateOfManufacture.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (itemTemplateId != null) {
            httpParams.put("itemTemplateId", itemTemplateId);
        }
        if (item_img != null) {
            httpParams.put("itemImg", item_img);
        }
         float stocks=Float.parseFloat(stock.getText().toString());
        httpParams.put("repertory", ((int)stocks* 100)+"");//库存
        httpParams.put("warningRepertory", Integer.parseInt(editWarnInventory.getText().toString()) * 100);//预警库存
        httpParams.put("stockPrice", stcokPrice.getText().toString());//进货价
        httpParams.put("salesPrice", sellingPrice.getText().toString());//售价

        httpParams.put("shelfLife", selectData.getSelectedItem().getXCDropDownItemText().equals("天") ? shelfLife.getText().toString() : Integer.valueOf(shelfLife.getText().toString()) * 30 + "");//保质期
        httpParams.put("isShelve", updown.getSelectedItem().getXCDropDownItemText().equals("上架") ? "Y" : "N");//是否上下架

        OkGo.<NydResponse<GoodBean>>post(Contonts.URL_ADD_GOOD)
                .tag(getClass().getSimpleName())
                .params(httpParams)
                .execute(new DialogCallback<NydResponse<GoodBean>>(mContext) {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<GoodBean>> response) {
                        App.getDaoInstant().getGoodBeanDao().insertOrReplace(response.body().response);
                        ToastUtil.showSuccess(mContext, "入库成功！");
                        stcokPrice.setText("");
                        name.setText("");
                        sellingPrice.setText("");
                        stock.setText("");
                        stcokPrice.setEnabled(true);
                        name.setEnabled(true);
                        sellingPrice.setEnabled(true);
                        stock.setEnabled(true);
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<GoodBean>> response) {
                        super.onError(response);
                    }
                });

    }


    void refreshData(GoodBean bean) {
        goodsCode.setText(bean.getBarcode());
        name.setText(bean.getItemName());
        stock.setText(bean.getRepertory() + "");
        sellingPrice.setText(bean.getSalesPrice() + "");
        stcokPrice.setText(bean.getStockPrice() + "");
        itemTypeId = bean.getItemTypeId();
        shelfLife.setText(bean.getShelfLife() + "");
        if (bean.getGeneratedDate() > 0) {
            dateOfManufacture.setText(Utils.stampToDate(bean.getGeneratedDate() + ""));
        }
        editWarnInventory.setText(bean.getWarningRepertory() + "");
        unitId = bean.getItemUnitId();
        itemTemplateId = bean.getItemTemplateId();
        item_img = bean.getItemImg();

        //根据类型ID
        for (int i = 0; i < classification.getItems().size(); i++) {
            XCDropDownListView.XCDropDownItem item = classification.getItems().get(i);
            LogUtils.e(item);
            if (item.getXCDropDownItemType() == 1) {
                //判断ID
                GoodTypesBean it = (GoodTypesBean) item;
                if (!BaseUtils.isEmpty(it.getItemTypeId()) && it.getItemTypeId().equals(itemTypeId)) {
                    classification.setShowIndex(i);
                    break;
                }
            }
        }
        //上架设置
        if ("N".equals(bean.getIsShelve())) {
            updown.setShowIndex(1);
        } else {
            updown.setShowIndex(0);
        }

        //根据类型ID
        for (int i = 0; i < unit.getItems().size(); i++) {
            XCDropDownListView.XCDropDownItem item = unit.getItems().get(i);
            if (item.getXCDropDownItemType() == 1) {
                //判断ID
                GoodUnitBean it = (GoodUnitBean) item;
                if (it.getId().equals(unitId)) {
                    unit.setShowIndex(i);
                    break;
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.e("requestCode", requestCode + "");
        Log.e("resultCode", resultCode + "");

        if (resultCode == 100) {

            name.setText("");
            stock.setText("0");
            sellingPrice.setText("0");
            stcokPrice.setText("0");

            name.setEnabled(true);
            name.setBackgroundResource(R.drawable.bg_line_gray);
            sellingPrice.setEnabled(true);
            sellingPrice.setBackgroundResource(R.drawable.bg_line_gray);
            stcokPrice.setEnabled(true);
            stcokPrice.setBackgroundResource(R.drawable.bg_line_gray);

            classification.setEnabled(true);
            classification.setBackgroundResource(R.drawable.bg_line_gray);
            updown.setEnabled(true);
            updown.setBackgroundResource(R.drawable.bg_line_gray);

        } else if (resultCode == 101) {
            if (intent == null)
                return;
            stock.setText("");
            refreshData(new Gson().fromJson(intent.getStringExtra("bean"), GoodBean.class));

        } else if (resultCode == 102) {

            if (intent == null) return;

            GoodTypesBean itemType = new Gson().fromJson(intent.getStringExtra("itemtype"), GoodTypesBean.class);
            //添加到分类列表
            classification.addItem(itemType);
            //添加进新建缓存
            app.getDaoInstant().getGoodTypesBeanDao().insertOrReplace(itemType);
            //弹出提示
            ToastUtil.showToast(mContext, "分类创建成功");
        } else if (requestCode == 109) {
            if (intent == null) return;
            GoodUnitBean itemType = new Gson().fromJson(intent.getStringExtra("name"), GoodUnitBean.class);
            //添加到分类列表
            unit.addItem(itemType);
            //添加进新建缓存
//            newItemUnits.add(itemType);
            //弹出提示
            ToastUtil.showToast(mContext, "单位创建成功");
        }
    }

    private boolean setIsNull() {
        String sName = name.getText().toString().trim();
        if (Utils.isNull(sName)) {
            ToastUtil.showWrning(mContext, "商品名称不能为空哦");
            return false;
        }
        String sp = sellingPrice.getText().toString();
        if (Utils.isNull(sp) || !isPoint(sp) || Double.valueOf(sp) <= 0) {
            ToastUtil.showWrning(mContext, "售价 输入有误");
            return false;
        }
        String stop = stcokPrice.getText().toString().trim();
        if (BaseUtils.isEmpty(stop) || !isPoint(stop) || Double.valueOf(stop) <= 0) {
            ToastUtil.showWrning(mContext, "进价 输入有误");
            return false;
        }
        if (Double.valueOf(sp) < Double.valueOf(stop)) {
            ToastUtil.showWrning(mContext, "售价不能低于进价哦。");
            return false;
        }
        String sk = stock.getText().toString();
        if (Utils.isNull(sk)) {
            ToastUtil.showToast(mContext, "新增库存 输入有误");
            return false;
        }
        if (classification == null || classification.getSelectedItem() == null || classification.getSelectedItem().getXCDropDownItemType() == 0 || classification.getSelectedItem().getXCDropDownItemText().equals("-选择商品分类-")) {
            ToastUtil.showToast(mContext, "请选择商品分类");
            return false;
        }
//        if (unit == null || unit.getSelectedItem() == null || unit.getSelectedItem().getXCDropDownItemType() == 0 || unit.getSelectedItem().getXCDropDownItemText().equals("-选择商品单位-")) {
//            ToastUtil.showToast(mContext, "请选择商品单位");
//            return false;
//        }
        return true;
    }

    public static boolean isZero(String str) {
        return "0".equals(str.trim());
    }

    public static boolean isPoint(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        OkGo.getInstance().cancelTag(getClass().getSimpleName());
    }

    @OnClick({R.id.btn_search, R.id.add_type, R.id.add_unit, R.id.date_of_manufacture, R.id.cancel, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                search();
                break;
            case R.id.add_type:
                Intent intent = new Intent(mContext, AddItemTypeActivity.class);
                startActivityForResult(intent, 102);
                break;
            case R.id.add_unit:
                Intent intent2 = new Intent(mContext, AddItemUnitActivity.class);
                startActivityForResult(intent2, 109);
                break;
            case R.id.date_of_manufacture:
                onYearMonthDayPicker();
                break;
            case R.id.cancel:
                EventBus.getDefault().post(new RefreshProductEvent());
                break;
            case R.id.submit:
                if (goodsCode.hasFocus()) {
                    goodsCode.clearFocus();
                } else {
                    if (setIsNull()) {
                        setSubmit();
                    }
                }
                break;
        }
    }
}
