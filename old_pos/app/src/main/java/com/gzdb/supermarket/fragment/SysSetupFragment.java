package com.gzdb.supermarket.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.gzdb.basepos.R;
import com.gzdb.printer.GpUsbPrint;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.event.SetupEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author zhumg
 */
public class SysSetupFragment extends Fragment {

    @Bind(R.id.sw_stock)
    Switch swStock;
    @Bind(R.id.sw_order_voice)
    Switch swOrderVoice;
    @Bind(R.id.sw_pay_voice)
    Switch swPayVoice;
    @Bind(R.id.sw_usb_print)
    Switch swUsbPrint;

    private boolean stock, orderVoice, payVoice;

    public SysSetupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sys_setup, container, false);
        ButterKnife.bind(this, view);
        stock = SPUtil.getInstance().getBoolean(getContext(), "stock", true);
        orderVoice = SPUtil.getInstance().getBoolean(getContext(), "orderVoice", true);
        payVoice = SPUtil.getInstance().getBoolean(getContext(), "payVoice", true);

        GpUsbPrint.use = SPUtil.getInstance().getBoolean(getContext(), "usbPrint", false);

        swStock.setChecked(stock);
        swOrderVoice.setChecked(orderVoice);
        swPayVoice.setChecked(payVoice);
        swUsbPrint.setChecked(GpUsbPrint.use);

        swUsbPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.getInstance().putBoolean(getContext(), "usbPrint", b);
                GpUsbPrint.use = b;
                if (!b) {
                    GpUsbPrint.closePrinter();
                } else {
                    GpUsbPrint.connPrinter();
                }
            }
        });

        swStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.getInstance().putBoolean(getContext(), "stock", b);
                EventBus.getDefault().post(new SetupEvent());
            }
        });

        swOrderVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.getInstance().putBoolean(getContext(), "orderVoice", b);
            }
        });

        swPayVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SPUtil.getInstance().putBoolean(getContext(), "payVoice", b);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
