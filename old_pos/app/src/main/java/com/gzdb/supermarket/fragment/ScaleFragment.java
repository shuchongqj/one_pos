package com.gzdb.supermarket.fragment;


import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.core.util.ToastUtil;
import com.gzdb.basepos.R;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.event.SetupEvent;
import com.sunmi.scalelibrary.ScaleManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScaleFragment extends Fragment {


    @Bind(R.id.sw_shop_cart)
    Switch swShopCart;
    private ScaleManager mScaleManager;
    private boolean shop_cart;

    public ScaleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scale, container, false);
        ButterKnife.bind(this, view);
        connectScaleService();
        shop_cart = SPUtil.getInstance().getBoolean(getContext(), "shop_cart",false);
        swShopCart.setChecked(shop_cart);
        swShopCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.getInstance().putBoolean(getContext(),"shop_cart",b);
                EventBus.getDefault().post(new SetupEvent());
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void connectScaleService() {
        mScaleManager = ScaleManager.getInstance(getContext());
        mScaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
            @Override
            public void onServiceConnected() {
                Log.e("@@@@@@@@", "电子称连接成功");
            }

            @Override
            public void onServiceDisconnect() {
                Log.e("@@@@@@@@", "电子称连接失败");

            }
        });
    }

    @OnClick({R.id.btn_clear, R.id.btn_tare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_clear:
                try {
                    if (mScaleManager != null) {
                        mScaleManager.zero();
                    }
                    ToastUtil.showToast(getContext(), "电子秤清零成功");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(getContext(), "电子秤清零失败");
                }
                break;
            case R.id.btn_tare:
                try {
                    if (mScaleManager != null) {
                        mScaleManager.tare();
                    }
                    ToastUtil.showToast(getContext(), "电子秤去皮成功");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    ToastUtil.showToast(getContext(), "电子秤去皮失败");
                }
                break;
        }
    }
}
