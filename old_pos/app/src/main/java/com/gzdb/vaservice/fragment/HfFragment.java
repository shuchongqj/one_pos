package com.gzdb.vaservice.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.core.http.Http;
import com.core.okgo.NydResponse;
import com.core.okgo.callback.JsonCallback;
import com.core.util.BaseUtils;
import com.core.util.ToastUtil;
import com.google.gson.Gson;
import com.gzdb.basepos.App;
import com.gzdb.basepos.BaseFragment;
import com.gzdb.basepos.R;
import com.gzdb.sunmi.Sunmi;
import com.gzdb.supermarket.SupermarketIndexActivity;
import com.gzdb.supermarket.activity.PayActivity;
import com.gzdb.supermarket.been.CreateOrderSuccessBean;
import com.gzdb.supermarket.been.ItemSnapshotsBean;
import com.gzdb.supermarket.util.ButtonUtils;
import com.gzdb.supermarket.util.Contonts;
import com.gzdb.supermarket.util.DialogUtil;
import com.gzdb.supermarket.util.Utils;
import com.gzdb.vaservice.bean.VasMainDataBean;
import com.gzdb.vaservice.bean.VasPareBean;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HfFragment extends BaseFragment {


    @Bind(R.id.edit_phone)
    EditText editPhone;
    @Bind(R.id.text_phone_state)
    TextView textPhoneState;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout idFlowlayout;
    @Bind(R.id.text_price)
    TextView textPrice;
    @Bind(R.id.paid_in)
    EditText paidIn;
    @Bind(R.id.one)
    TextView one;
    @Bind(R.id.two)
    TextView two;
    @Bind(R.id.three)
    TextView three;
    @Bind(R.id.four)
    TextView four;
    @Bind(R.id.five)
    TextView five;
    @Bind(R.id.six)
    TextView six;
    @Bind(R.id.seven)
    TextView seven;
    @Bind(R.id.eight)
    TextView eight;
    @Bind(R.id.nine)
    TextView nine;
    @Bind(R.id.delete)
    ImageButton delete;
    @Bind(R.id.point)
    TextView point;
    @Bind(R.id.zero)
    TextView zero;
    @Bind(R.id.Alipay)
    RelativeLayout Alipay;
    @Bind(R.id.Wechat)
    RelativeLayout Wechat;
    @Bind(R.id.cash_txt)
    TextView cashTxt;
    @Bind(R.id.cash)
    RelativeLayout cash;

    private TagAdapter<VasPareBean> fareAdapter;
    private TagAdapter<VasPareBean> dataAdapter;
    private Dialog dialog;
    private VasPareBean chooseBean;//选中的商品
    private TagAdapter<VasPareBean> tempFareAdapter;//填充数据
    private TagAdapter<VasPareBean> tempDataAdapter;//填充数据
    private VasMainDataBean bean;

    public HfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hf, container, false);
        ButterKnife.bind(this, view);

        dialog = DialogUtil.loadingDialog(mContext, "加载中...");
        disableShowSoftInput();//禁止弹出软键盘
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    textPhoneState.setText("手机号码为空");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    getData(editPhone.getText().toString());
                }
            }
        });

        showTempData();

        return view;
    }

    public void showTempData() {
        final List<VasPareBean> beenList = new ArrayList<>();
        VasPareBean vasPareBean1 = new VasPareBean();
        vasPareBean1.setItemName("30元");
        beenList.add(vasPareBean1);
        VasPareBean vasPareBean2 = new VasPareBean();
        vasPareBean2.setItemName("50元");
        beenList.add(vasPareBean2);
        VasPareBean vasPareBean3 = new VasPareBean();
        vasPareBean3.setItemName("100元");
        beenList.add(vasPareBean3);
        VasPareBean vasPareBean4 = new VasPareBean();
        vasPareBean4.setItemName("200元");
        beenList.add(vasPareBean4);
        VasPareBean vasPareBean5 = new VasPareBean();
        vasPareBean5.setItemName("300元");
        beenList.add(vasPareBean5);
        VasPareBean vasPareBean6 = new VasPareBean();
        vasPareBean6.setItemName("500元");
        beenList.add(vasPareBean6);
        tempFareAdapter = new TagAdapter<VasPareBean>(beenList) {
            @Override
            public View getView(FlowLayout parent, int position, VasPareBean vasPareBean) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.vas_pare_item,
                        idFlowlayout, false);
                tv.setText(vasPareBean.getItemName());
                return tv;
            }

        };

        idFlowlayout.setAdapter(tempFareAdapter);

        idFlowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.iterator().hasNext()) {
                    int temp = selectPosSet.iterator().next();
                    chooseBean = (VasPareBean) idFlowlayout.getAdapter().getItem(temp);
                    textPrice.setText("" + chooseBean.getSalesPrice());
                    paidIn.setText("" + chooseBean.getSalesPrice());
                }
            }
        });

    }

    public void getData(String phone) {
        dialog.show();

        OkGo.<NydResponse<VasMainDataBean>>post(Contonts.URL_VAS_GET_ITEM)
                .params("type", 1)
                .params("phone", phone)
                .execute(new JsonCallback<NydResponse<VasMainDataBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<VasMainDataBean>> response) {
                        dialog.cancel();
                        bean = response.body().response;
                        fareAdapter = new TagAdapter<VasPareBean>(bean.getMenuList()) {
                            @Override
                            public View getView(FlowLayout parent, int position, VasPareBean vasPareBean) {
                                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.vas_pare_item,
                                        idFlowlayout, false);
                                SpannableStringBuilder builder = new SpannableStringBuilder(vasPareBean.getItemName() + "\n售价：" + vasPareBean.getSalesPrice() + "元");
                                builder.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.font_28)), 0, vasPareBean.getItemName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                builder.setSpan(new AbsoluteSizeSpan((int) getResources().getDimensionPixelSize(R.dimen.font_20)), vasPareBean.getItemName().length() + 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tv.setText(builder);
                                return tv;
                            }

                        };

                        idFlowlayout.setAdapter(fareAdapter);
                        fareAdapter.setSelectedList(new int[]{0});
                        if (bean.getMenuList().size() != 0) {
                            chooseBean = bean.getMenuList().get(0);
                            textPrice.setText("" + chooseBean.getSalesPrice());
                            paidIn.setText("" + chooseBean.getSalesPrice());
                        }
                        textPhoneState.setTextColor(ContextCompat.getColor(mContext, R.color.font_3));
                        textPhoneState.setText(bean.getPhoneInfo().getCity() + "  " + bean.getPhoneInfo().getCompany());
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<VasMainDataBean>> response) {
                        super.onError(response);
                        dialog.cancel();
                        ToastUtil.showToast(mContext, response.message());
                        textPhoneState.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                        textPhoneState.setText(response.message());
                    }

                });
    }

    /**
     * 禁止Edittext弹出软件盘，光标依然正常显示。
     */
    public void disableShowSoftInput() {
        if (Build.VERSION.SDK_INT <= 10) {
            paidIn.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(paidIn, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(paidIn, false);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }

    @OnClick({R.id.Alipay, R.id.Wechat, R.id.cash})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.Alipay:
                if (verify()) {
                    return;
                }
                if (!Http.isNetworkConnected(getActivity())) {
                    ToastUtil.showToast(getActivity(), "联网失败，当前只能现金付款");
                    return;
                }
                if (checkZeroPay()) {
                    return;
                }
                creatOrder(2);
                break;
            case R.id.Wechat:
                if (verify()) {
                    return;
                }
                if (!Http.isNetworkConnected(getActivity())) {
                    ToastUtil.showToast(getActivity(), "联网失败，当前只能现金付款");
                    return;
                }
                if (checkZeroPay()) {
                    return;
                }
                creatOrder(1);
                break;
            case R.id.cash:
                if (verify()) {
                    return;
                }
                creatOrder(3);
                break;
        }
    }



    boolean verify() {
        if (BaseUtils.isEmpty(editPhone.getText().toString())) {
            ToastUtil.showToast(mContext, "请输入手机号码");
            return true;
        }
        if (chooseBean == null) {
            return true;
        }
        if (Float.valueOf(paidIn.getText().toString()) < Float.valueOf(textPrice.getText().toString())) {
            ToastUtil.showToast(mContext, "实收不能低于售价！");
            return true;
        }
        return false;
    }

    boolean checkZeroPay() {
        String price = textPrice.getText().toString();
        try {
            double d = Double.parseDouble(price);
            if (d <= 0) {
                ToastUtil.showToast(getActivity(), "0金额订单，请使用现金收款");
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    //创建订单
    public void creatOrder(final int payType) {

        dialog.show();
        List<ItemSnapshotsBean> orderItemBeen = new ArrayList<>();

        ItemSnapshotsBean bean = new ItemSnapshotsBean();
        bean.setItemId(chooseBean.getId());
        bean.setItemType(1);
        bean.setItemTemplateId(chooseBean.getItemTemplateId());
        bean.setItemName(chooseBean.getItemName());
        bean.setItemTypeId(chooseBean.getPosTypeId());
        bean.setItemTypeName(chooseBean.getPosTypeName());
        bean.setItemTypeUnitId(chooseBean.getItemUnitId());
        bean.setItemTypeUnitName(chooseBean.getItemUnitName());
        bean.setItemBarcode(chooseBean.getBarcode());
        bean.setCostPrice(chooseBean.getStockPrice());
        bean.setTotalPrice(chooseBean.getSalesPrice());
        bean.setNormalPrice(chooseBean.getSalesPrice());
        bean.setNormalQuantity(1);
        orderItemBeen.add(bean);

        HttpParams httpParams = new HttpParams();

        httpParams.put("orderType", 9);
        httpParams.put("actualPrice", chooseBean.getSalesPrice());//应收金额
        httpParams.put("totalPrice", chooseBean.getSalesPrice());//实收金额
        httpParams.put("items", new Gson().toJson(orderItemBeen));
        httpParams.put("phoneNumber", editPhone.getText().toString());

        OkGo.<NydResponse<CreateOrderSuccessBean>>post(Contonts.URL_CREATE_ORDER)
                .tag(Contonts.URL_CREATE_ORDER)
                .params(httpParams)
                .execute(new JsonCallback<NydResponse<CreateOrderSuccessBean>>() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<CreateOrderSuccessBean>> response) {
                        dialog.cancel();
                        pay(payType, response.body().response.getOrderId());
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<NydResponse<CreateOrderSuccessBean>> response) {
                        super.onError(response);
                        dialog.cancel();
                        if (payType == 3) {//网络下单失败，现金方式调用离线下单
//                            offCreateOrder();
                        } else {
                            ToastUtil.showEorr(mContext, "网络故障，请使用现金支付！");
                        }
                    }
                });
    }

    public void pay(int payType, final String orderId) {
        switch (payType) {
            case 1://微信
                if (ButtonUtils.isFastDoubleClick(R.id.Alipay)) {
                    Alipay.setEnabled(false);
                    Wechat.setEnabled(false);
                    Log.e("Alipay", "Alipay");
                } else {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtra("payType", payType);
                    intent.putExtra("orderId", orderId);
                    instance.startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
                }
                break;
            case 2://支付宝
                if (ButtonUtils.isFastDoubleClick(R.id.Wechat)) {
                    Alipay.setEnabled(false);
                    Wechat.setEnabled(false);
                    Log.e("Wechat", "Wechat");
                } else {
                    Intent intent = new Intent(getActivity(), PayActivity.class);
                    intent.putExtra("payType", payType);
                    intent.putExtra("orderId", orderId);
                    instance.startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
                }
                break;
            case 3://现金
                if (!Sunmi.viceScreenMode) {
                    try {
                        App.mSerialPortOperaion.WriteData(0xC);
                        String str = paidIn.getText().toString();
                        App.mSerialPortOperaion.WriteData(27, 81, 65);
                        App.mSerialPortOperaion.WriteData(Utils.input(str));
                        App.mSerialPortOperaion.WriteData(13);
                        App.mSerialPortOperaion.WriteData(0X1B, 0X73, 0X33);//收款
                    } catch (Exception e) {
                    }
                }

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.cancel();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
