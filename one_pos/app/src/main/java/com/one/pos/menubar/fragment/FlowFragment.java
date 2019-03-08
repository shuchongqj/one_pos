package com.one.pos.menubar.fragment;

import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anlib.GFragment;
import com.anlib.http.Http;
import com.anlib.http.HttpCallback;
import com.anlib.util.ToastUtils;
import com.google.gson.Gson;
import com.one.pos.Contonts;
import com.one.pos.R;
import com.one.pos.menubar.bean.VasPhoneBean;
import com.one.pos.util.ButtonUtils;
import com.one.pos.util.DialogUtil;
import com.one.pos.util.Util;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: even
 * Date:   2019/3/6
 * Description:
 */
public class FlowFragment extends GFragment implements View.OnClickListener {
    private EditText edit_phone;
    private TextView textPhoneState;
    private TagFlowLayout tagflowlayout;
    private TextView text_price;
    private RelativeLayout Alipay;
    private RelativeLayout Wechat;
    private RelativeLayout cash;
    private Dialog dialog;
    private TagAdapter<VasPhoneBean.MenuListBean> tempFareAdapter;//填充数据
    private TagAdapter<VasPhoneBean.MenuListBean> fareAdapter;
    private VasPhoneBean.MenuListBean chooseBean;//选中的商品



    @Override
    protected int getContentViewId() {
        return R.layout.fragment_flow;
    }

    @Override
    protected void initView(View view) {
        initId(view);
        dialog = DialogUtil.loadingDialog(getContext(), "加载中...");
        edit_phone.addTextChangedListener(new TextWatcher() {
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
                    getData(edit_phone.getText().toString());
                }
            }
        });
        showTempData();

    }

    private void initId(View view) {
        edit_phone = view.findViewById(R.id.edit_phone);
        textPhoneState = view.findViewById(R.id.text_phone_state);
        tagflowlayout = view.findViewById(R.id.tagflowlayout);
        text_price = view.findViewById(R.id.text_price);
        Alipay = view.findViewById(R.id.Alipay);
        Wechat = view.findViewById(R.id.Wechat);
        cash = view.findViewById(R.id.cash);
        Alipay.setOnClickListener(this);
        Wechat.setOnClickListener(this);
    }

    public void showTempData() {
        final List<VasPhoneBean.MenuListBean> beenList = new ArrayList<>();
        VasPhoneBean.MenuListBean vasPareBean1 = new VasPhoneBean.MenuListBean();
        vasPareBean1.setItemName("10M");
        beenList.add(vasPareBean1);
        VasPhoneBean.MenuListBean vasPareBean2 = new VasPhoneBean.MenuListBean();
        vasPareBean2.setItemName("30M");
        beenList.add(vasPareBean2);
        VasPhoneBean.MenuListBean vasPareBean3 = new VasPhoneBean.MenuListBean();
        vasPareBean3.setItemName("50M");
        beenList.add(vasPareBean3);
        VasPhoneBean.MenuListBean vasPareBean4 = new VasPhoneBean.MenuListBean();
        vasPareBean4.setItemName("100M");
        beenList.add(vasPareBean4);
        VasPhoneBean.MenuListBean vasPareBean5 = new VasPhoneBean.MenuListBean();
        vasPareBean5.setItemName("300M");
        beenList.add(vasPareBean5);
        VasPhoneBean.MenuListBean vasPareBean6 = new VasPhoneBean.MenuListBean();
        vasPareBean6.setItemName("500M");
        beenList.add(vasPareBean6);
        tempFareAdapter = new TagAdapter<VasPhoneBean.MenuListBean>(beenList) {
            @Override
            public View getView(FlowLayout parent, int position, VasPhoneBean.MenuListBean vasPareBean) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.vas_pare_item,
                        tagflowlayout, false);
                tv.setText(vasPareBean.getItemName());
                return tv;
            }

        };
        tagflowlayout.setAdapter(tempFareAdapter);
        tagflowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet.iterator().hasNext()) {
                    int temp = selectPosSet.iterator().next();
                    chooseBean = (VasPhoneBean.MenuListBean) tagflowlayout.getAdapter().getItem(temp);
                    text_price.setText("" + chooseBean.getSalesPrice());
                }
            }
        });

    }

    public void getData(String phone) {
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        map.put("phone", phone);
        Http.post(getContext(), map,Contonts.URL_VAS_GET_ITEM , new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                dialog.cancel();
                Gson gson = new Gson();
                VasPhoneBean bean = gson.fromJson(data.toString(), VasPhoneBean.class);
                fareAdapter = new TagAdapter<VasPhoneBean.MenuListBean>(bean.getMenuList()) {
                    @Override
                    public View getView(FlowLayout parent, int position, VasPhoneBean.MenuListBean vasPareBean) {
                        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.vas_pare_item,
                                tagflowlayout, false);
                        SpannableStringBuilder builder = new SpannableStringBuilder(vasPareBean.getItemName() + "\n售价：" + vasPareBean.getSalesPrice() + "元");
                        builder.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.font_28)), 0, vasPareBean.getItemName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new AbsoluteSizeSpan((int) getResources().getDimensionPixelSize(R.dimen.font_20)), vasPareBean.getItemName().length() + 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tv.setText(builder);
                        return tv;
                    }
                };
                tagflowlayout.setAdapter(fareAdapter);
                fareAdapter.setSelectedList(new int[]{0});
                if (bean.getMenuList().size() != 0) {
                    chooseBean = bean.getMenuList().get(0);
                    text_price.setText("" + chooseBean.getSalesPrice());
                }
                textPhoneState.setTextColor(ContextCompat.getColor(getContext(), R.color.font_3));
                textPhoneState.setText(bean.getPhoneInfo().getCity() + "  " + bean.getPhoneInfo().getCompany());
            }
        });
    }

    boolean verify() {
        if (Util.isEmpty(edit_phone.getText().toString())) {
            ToastUtils.showToast(getContext(), "请输入手机号码");
            return true;
        }
        if (chooseBean == null) {
            return true;
        }
        return false;
    }

    //创建订单
    public void creatOrder(final int payType) {

        dialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("orderType","9");
        map.put("actualPrice",chooseBean.getSalesPrice()+"");
        map.put("totalPrice",chooseBean.getSalesPrice()+"");
        map.put("phoneNumber",edit_phone.getText().toString());
        Http.post(getContext(),map, Contonts.URL_CREATE_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                dialog.cancel();
                pay(payType);

            }
        });
    }
    private void pay(int payType) {
        switch (payType){
            case 1://微信
                if (ButtonUtils.isFastDoubleClick(R.id.Alipay)) {
                    Alipay.setEnabled(false);
                    Wechat.setEnabled(false);
                    Log.e("Alipay", "Alipay");
                } else {
//                    Intent intent = new Intent(getActivity(), PayActivity.class);
//                    intent.putExtra("payType", payType);
//                    intent.putExtra("orderId", orderId);
//                    instance.startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
                }
                break;

            case 2://支付宝
                if (ButtonUtils.isFastDoubleClick(R.id.Wechat)) {
                    Alipay.setEnabled(false);
                    Wechat.setEnabled(false);
                    Log.e("Wechat", "Wechat");
                } else {
//                    Intent intent = new Intent(getActivity(), PayActivity.class);
//                    intent.putExtra("payType", payType);
//                    intent.putExtra("orderId", orderId);
//                    instance.startActivityForResult(intent, SupermarketIndexActivity.requestCode_PAY_RESULT);
                }
                break;


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Alipay:
                if(verify()){
                    return;
                }
                creatOrder(2);
                break;

            case R.id.Wechat:
                if(verify()){
                    return;
                }
                creatOrder(1);
                break;
        }

    }
}
