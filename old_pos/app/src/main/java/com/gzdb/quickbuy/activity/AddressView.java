package com.gzdb.quickbuy.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.core.okgo.NydResponse;
import com.core.okgo.callback.DialogCallback;
import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.quickbuy.adapter.AddressAdapter;
import com.gzdb.quickbuy.bean.AddressBean;
import com.gzdb.quickbuy.util.OneBuyRightViewLogic;
import com.gzdb.supermarket.util.Contonts;
import com.lzy.okgo.OkGo;
import com.util.baidu.BaiduAddressActivity;
import com.util.baidu.EditAddress;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liubolin on 2018/1/31.
 */

public class AddressView implements OneBuyRightViewLogic,View.OnClickListener, AddressAdapter.AddressAdapterHost {

    public static final int requestCode_BaiduAddressActivity = 100;

    static QuickBuyActivity quickBuyActivity;

    private View rootView;

    private View address_list_layout;

    private View address_info_layout;

    private View tv_add_address;

    private ListView address_list;
    private AddressAdapter mAddressAdapter;

    private List<AddressBean> addressList = new ArrayList<>();

    private EditText et_name;

    private EditText et_phone;

    private EditText et_address;

    private EditText et_address_more;

    private View img_local_address;

    private View btn_clean;

    private View btn_submit;

    private boolean isUpdate = false;

    private EditAddress editAddress;

    private AddressBean updateAddressBean = null;

    public AddressView(QuickBuyActivity quickBuyActivity) {
        rootView = View.inflate(quickBuyActivity, R.layout.quickbuy_address_item, null);
        this.quickBuyActivity = quickBuyActivity;
        //初始化
        initView();
        getAddressList();
    }

    public int getAddressListSize() {
        return addressList.size();
    }

    public void setAddress(EditAddress e) {
        if (null != e && null != e.getAddress()) {
            et_address.setText(e.getAddress());
            editAddress = e;
            if (isUpdate && null != updateAddressBean) {
                updateAddressBean.setLongitude(e.getLongitude());
                updateAddressBean.setLatitude(e.getLatitude());
            }
        }
    }

    private void initView() {
        address_list_layout = rootView.findViewById(R.id.address_list_layout);
        address_info_layout = rootView.findViewById(R.id.address_info_layout);
        tv_add_address = rootView.findViewById(R.id.tv_add_address);
        address_list = (ListView) rootView.findViewById(R.id.address_list);
        et_name = (EditText) rootView.findViewById(R.id.et_name);
        et_phone = (EditText) rootView.findViewById(R.id.et_phone);
        et_address = (EditText) rootView.findViewById(R.id.et_address);
        et_address_more = (EditText) rootView.findViewById(R.id.et_address_more);
        btn_clean = rootView.findViewById(R.id.btn_clean);
        btn_submit = rootView.findViewById(R.id.btn_submit);
        img_local_address = rootView.findViewById(R.id.img_local_address);

        mAddressAdapter = new AddressAdapter(quickBuyActivity, addressList);
        mAddressAdapter.setAddressAdapterHost(this);
        address_list.setAdapter(mAddressAdapter);

        et_address.setCursorVisible(false);
        et_address.setFocusable(false);
        et_address.setFocusableInTouchMode(false);

        btn_clean.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_local_address.setOnClickListener(this);
        address_list_layout.setOnClickListener(this);
        tv_add_address.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        et_address.setOnClickListener(this);
    }

    private void getAddressList() {
        OkGo.<NydResponse<Datas>>post(Contonts.URL_ADDRESS_LIST)
                .params("passportId", ((App) quickBuyActivity.getApplication()).getCurrentUser().getPassportId())
                .execute(new DialogCallback<NydResponse<Datas>>(quickBuyActivity) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<Datas>> response) {
                        mAddressAdapter.setDatas(response.body().response.datas);
                    }
                });
    }

    private void operateAddress() {
        String name = et_name.getText().toString();
        String phone = et_phone.getText().toString();
        String address = et_address.getText().toString();
        String addressMore = et_address_more.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(quickBuyActivity, "请输入收货人名称！");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(quickBuyActivity, "请输入联系电话！");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtil.showToast(quickBuyActivity, "请输入收货人地址！");
            return;
        }

        if (TextUtils.isEmpty(addressMore)) {
            ToastUtil.showToast(quickBuyActivity, "请输入详细地址！");
            return;
        }

        Map<String, String> param = new HashMap<>();
        String url;
        param.put("passportId", ((App) quickBuyActivity.getApplication()).getCurrentUser().getPassportId());
        param.put("name", name);
        param.put("phoneNumber", phone);
        param.put("alias", address);
        param.put("detailAddress", addressMore);
        if (isUpdate) {
            url = Contonts.URL_UPDATE_ADDRESS;
            param.put("longitude", String.valueOf(updateAddressBean.getLongitude()));
            param.put("latitude", String.valueOf(updateAddressBean.getLatitude()));
            param.put("addressId", String.valueOf(updateAddressBean.getId()));
        } else {
            url = Contonts.URL_CREATE_ADDRESS;
            param.put("longitude", String.valueOf(editAddress.getLongitude()));
            param.put("latitude", String.valueOf(editAddress.getLatitude()));
        }

        OkGo.<NydResponse<JSONObject>>post(url)
                .params(param)
                .execute(new DialogCallback<NydResponse<JSONObject>>(quickBuyActivity) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                        ToastUtil.showToast(quickBuyActivity, response.body().msg);
                        if (response.body().code == 0) {
                            // 刷新
                            getAddressList();
                            initAddress();
                        }
                    }
                });
    }

    private void deleteAddressPost(long addressId) {
        OkGo.<NydResponse<JSONObject>>post(Contonts.URL_DELETE_ADDRESS)
                .params("passportId", ((App) quickBuyActivity.getApplication()).getCurrentUser().getPassportId())
                .params("addressId", addressId)
                .execute(new DialogCallback<NydResponse<JSONObject>>(quickBuyActivity) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                        ToastUtil.showToast(quickBuyActivity, response.body().msg);
                        if (response.body().code == 0) {
                            // 刷新
                            getAddressList();
                        }
                    }
                });
    }

    private void defaultAddressPost(long addressId) {
        OkGo.<NydResponse<JSONObject>>post(Contonts.URL_SET_DEFAULT_ADDRESS)
                .params("passportId", ((App) quickBuyActivity.getApplication()).getCurrentUser().getPassportId())
                .params("addressId", addressId)
                .execute(new DialogCallback<NydResponse<JSONObject>>(quickBuyActivity) {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<NydResponse<JSONObject>> response) {
                        ToastUtil.showToast(quickBuyActivity, response.body().msg);
                        if (response.body().code == 0) {
                            // 刷新
                            getAddressList();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_add_address) {
            // 新增
            address_list_layout.setVisibility(View.GONE);
            address_info_layout.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_clean) {
            // 取消
            initAddress();
        } else if (id == R.id.btn_submit) {
            // 提交
            operateAddress();
        } else if (id == R.id.img_local_address) {
            Intent intent = new Intent(quickBuyActivity, BaiduAddressActivity.class);
            quickBuyActivity.startActivityForResult(intent, requestCode_BaiduAddressActivity);
        } else if (id == R.id.et_address) {
            Intent intent = new Intent(quickBuyActivity, BaiduAddressActivity.class);
            quickBuyActivity.startActivityForResult(intent, requestCode_BaiduAddressActivity);
        }
    }

    private void initAddress() {
        address_list_layout.setVisibility(View.VISIBLE);
        address_info_layout.setVisibility(View.GONE);
        et_name.setText("");
        et_phone.setText("");
        et_address.setText("");
        et_address_more.setText("");
        isUpdate = false;
        updateAddressBean = null;
    }

    @Override
    public View getView() {
        return rootView;
    }

    @Override
    public void onVisible() { }

    @Override
    public void unVisible() {
        initAddress();
    }

    @Override
    public void updateAddress(AddressBean bean) {
        et_name.setText(bean.getName());
        et_phone.setText(bean.getPhoneNumber());
        et_address.setText(bean.getAddressAlias());
        et_address_more.setText(bean.getDetailAddress());
        isUpdate = true;
        updateAddressBean = bean;
        address_list_layout.setVisibility(View.GONE);
        address_info_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteAddress(long id) {
        deleteAddressPost(id);
    }

    @Override
    public void defaultAddress(long id) {
        defaultAddressPost(id);
    }

    class Datas {
        private List<AddressBean> datas;

        public List<AddressBean> getDatas() {
            return datas;
        }

        public void setDatas(List<AddressBean> datas) {
            this.datas = datas;
        }
    }
}