package com.gzdb.supermarket.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.core.util.ToastUtil;
import com.gzdb.basepos.App;
import com.gzdb.basepos.R;
import com.gzdb.basepos.greendao.GoodBeanDao;
import com.gzdb.sale.util.SPUtil;
import com.gzdb.supermarket.been.GoodBean;
import com.gzdb.supermarket.event.PrintEvent;
import com.gzdb.supermarket.scan.ScanGunKeyEventHelper;
import com.gzdb.supermarket.util.Arith;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreshDialogFragment extends AppCompatDialogFragment implements View.OnClickListener, ScanGunKeyEventHelper.OnScanSuccessListener {

    private Button btnCancel;
    private Button btnAdd;
    private Button btnPrint;
    private TextView tvName;
    private TextView tvPrice;
    private TextView tvVipPrice;
    private TextView tvQuality;
    private TextView tvTotal;
    private TextView tvVipTotal;
    private Button btnDiscount9;
    private Button btnDiscount8;
    private Button btnDiscount7;
    private Button btnDiscount6;
    private Button btnDiscount5;
    private Button btnClean;
    private Button btnTare;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private double price;
    private double vipPrice;
    private double oTotalPrice;
    private double oTotalVipPrice;
    private long itemId;
    private String name;
    private String code;
    private String total = "0.00";//总价
    private String vipTotal = "0.00";//总价
    private double quality = 0;
    private double discount;
    boolean isShow = false;//防多次点击
    static int defaultNet = -10;
    private int now_net = defaultNet;
    private ScanGunKeyEventHelper scanGunKeyEventHelper = null;//扫码
    private GoodBean goodBean;
    private boolean shop_cart;

    public FreshDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        shop_cart = SPUtil.getInstance().getBoolean(getContext(), "shop_cart", false);
        scanGunKeyEventHelper = new ScanGunKeyEventHelper(this);
        return inflater.inflate(R.layout.fragment_fresh_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
        initData();
    }

    private void initView(View view) {
        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvVipPrice = (TextView) view.findViewById(R.id.tv_vip_price);
        tvQuality = (TextView) view.findViewById(R.id.tv_quality);
        tvTotal = (TextView) view.findViewById(R.id.tv_total);
        tvVipTotal = (TextView) view.findViewById(R.id.tv_vip_total);
        btnDiscount9 = (Button) view.findViewById(R.id.btn_discount9);
        btnDiscount8 = (Button) view.findViewById(R.id.btn_discount8);
        btnDiscount7 = (Button) view.findViewById(R.id.btn_discount7);
        btnDiscount6 = (Button) view.findViewById(R.id.btn_discount6);
        btnDiscount5 = (Button) view.findViewById(R.id.btn_discount5);
        btnClean = (Button) view.findViewById(R.id.btn_clean);
        btnTare = (Button) view.findViewById(R.id.btn_tare);
        tvQuality.setAlpha(0.5f);
    }

    private void initAction() {
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnDiscount9.setOnClickListener(this);
        btnDiscount8.setOnClickListener(this);
        btnDiscount7.setOnClickListener(this);
        btnDiscount6.setOnClickListener(this);
        btnDiscount5.setOnClickListener(this);
        btnAdd.setEnabled(false);
        btnPrint.setEnabled(false);
        btnClean.setOnClickListener(this);
        btnTare.setOnClickListener(this);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_TAB) {
                    return true;
                } else {
                    scanGunKeyEventHelper.analysisKeyEvent(event);
                }
                return false;
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            itemId = bundle.getLong("itemId");
            name = bundle.getString("name");
            code = bundle.getString("code");
            price = bundle.getDouble("price", 0);
            vipPrice = bundle.getDouble("vipPrice", 0);
            discount = bundle.getDouble("discount", 0);
            if (vipPrice == 0) {
                vipPrice = price;
            }
            tvName.setText(name);
            tvPrice.setText(price + "");
            tvQuality.setText("0.00");
            tvTotal.setText("0.00");
            tvVipTotal.setText("0.00");
            tvVipPrice.setText(vipPrice + "");
        }
        int net = bundle.getInt("net", 0);
        if (net != 0) {
            btnAdd.setEnabled(true);
            btnPrint.setEnabled(true);
            tvTotal.setAlpha(1);
            tvQuality.setAlpha(1);
            btnAdd.setAlpha(1);
            btnPrint.setAlpha(1);
            btnCancel.setAlpha(1);
            quality = Double.valueOf(decimalFormat.format(net * 1.0f / 1000));
            tvQuality.setText(quality + "");
            total = decimalFormat.format(Arith.mul(Arith.div(net, 1000), price));
            tvTotal.setText(total);
            vipTotal = decimalFormat.format(Arith.mul(Arith.div(net, 1000), vipPrice));
            oTotalPrice = Arith.mul(Arith.div(net, 1000), price);
            oTotalVipPrice = Arith.mul(Arith.div(net, 1000), vipPrice);
            discount = 1;
            setBg();
            tvVipTotal.setText(vipTotal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_add:
                if (shop_cart) {
                    if (listener != null) {
                        listener.onAddResult(goodBean, total, name, quality, discount);
                    }
                }
                dismiss();
                break;
            case R.id.btn_print:
                if (Double.valueOf(tvTotal.getText().toString()) <= 0) {
                    ToastUtil.showToast(this.getActivity(), "价格为0");
                    return;
                }
                if (shop_cart) {
                    if (listener != null) {
                        listener.onAddResult(goodBean, total, name, quality, discount);
                    }
                }
                EventBus.getDefault().post(new PrintEvent(name, code, itemId, price, vipPrice, quality + "", total, discount));
                break;
            case R.id.btn_discount9:
                setBg();
                btnDiscount9.setBackgroundResource(R.drawable.bg_nomal_green);
                discount = 0.9;
                setPrice();
                break;
            case R.id.btn_discount8:
                setBg();
                btnDiscount8.setBackgroundResource(R.drawable.bg_nomal_green);
                discount = 0.8;
                setPrice();
                break;
            case R.id.btn_discount7:
                setBg();
                btnDiscount7.setBackgroundResource(R.drawable.bg_nomal_green);
                discount = 0.7;
                setPrice();
                break;
            case R.id.btn_discount6:
                setBg();
                btnDiscount6.setBackgroundResource(R.drawable.bg_nomal_green);
                discount = 0.6;
                setPrice();
                break;
            case R.id.btn_discount5:
                setBg();
                btnDiscount5.setBackgroundResource(R.drawable.bg_nomal_green);
                discount = 0.5;
                setPrice();
                break;
            case R.id.btn_clean:
                functionListener.onClick(0);
                break;
            case R.id.btn_tare:
                functionListener.onClick(1);
                break;
            default:
                break;
        }
    }

    private void setBg() {
        btnDiscount9.setBackgroundResource(R.drawable.bg_nomal_blue);
        btnDiscount8.setBackgroundResource(R.drawable.bg_nomal_blue);
        btnDiscount7.setBackgroundResource(R.drawable.bg_nomal_blue);
        btnDiscount6.setBackgroundResource(R.drawable.bg_nomal_blue);
        btnDiscount5.setBackgroundResource(R.drawable.bg_nomal_blue);
    }

    private void setPrice() {
        tvTotal.setText(Arith.mul(oTotalPrice, discount) + "");
        tvVipTotal.setText(Arith.mul(oTotalVipPrice, discount) + "");
    }

    /**
     * 防抖动
     *
     * @param net
     * @return
     */
    private boolean unShake(int net) {
        if (Math.abs(net - now_net) < -defaultNet) {
            return false;
        }
        now_net = net;
        return true;
    }


    public void update(int status, int net) {
        if (status == 1 && net > 0) {
            btnAdd.setEnabled(true);
            btnPrint.setEnabled(true);
            tvTotal.setAlpha(1);
            tvQuality.setAlpha(1);
            btnAdd.setAlpha(1);
            btnPrint.setAlpha(1);
            btnCancel.setAlpha(1);
            if (!unShake(net)) {
                return;
            }
        } else {
            now_net = defaultNet;
            btnAdd.setEnabled(false);
            btnPrint.setEnabled(false);
            tvTotal.setAlpha(0.5f);
            tvQuality.setAlpha(0.5f);
            btnCancel.setAlpha(0.6f);
            btnAdd.setAlpha(0.6f);
            btnPrint.setAlpha(0.6f);
        }
        Log.d("SUNMI", "update: ----------------->" + decimalFormat.format(net * 1.0f / 1000));
        quality = Double.valueOf(decimalFormat.format(net * 1.0f / 1000));
        tvQuality.setText(Arith.div(net, 1000) + "");
        total = Arith.mul(Arith.div(net, 1000), price) + "";
        oTotalPrice = Arith.mul(Arith.div(net, 1000), price);
        oTotalVipPrice = Arith.mul(Arith.div(net, 1000), vipPrice);
        discount = 1;
        setBg();
        vipTotal = decimalFormat.format(oTotalVipPrice);
        tvTotal.setText(total);
        tvVipTotal.setText(vipTotal);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (isShow) {
            return;
        }
        super.show(manager, tag);
        isShow = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        isShow = false;
    }

    private AddListener listener = null;
    private FunctionListener functionListener = null;

    public void setListener(AddListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScanSuccess(String barcode) {
        String bcode = barcode.replace(" ", "");
        List<GoodBean> list = App.getInstance().getDaoInstant().getGoodBeanDao().queryBuilder().where(GoodBeanDao.Properties.Barcode.eq(bcode)).list();
        if (list.size() > 0) {//本地存有该商品刚放入购物车
            goodBean = list.get(0);//这个要从缓存中读取，现在还没实现
            if (goodBean.getItemType() == 2) {
                name = goodBean.getItemName();
                price = goodBean.getSalesPrice();
                code = goodBean.getBarcode();
                vipPrice = goodBean.getMembershipPrice();
                if (vipPrice == 0) {
                    vipPrice = price;
                }
                tvName.setText(name);
                tvPrice.setText(price + "");
                tvQuality.setText("0.00");
                tvTotal.setText("0.00");
                tvVipTotal.setText("0.00");
                tvVipPrice.setText(vipPrice + "");
                btnAdd.setEnabled(false);
                btnPrint.setEnabled(false);
                tvTotal.setAlpha(0.5f);
                tvQuality.setAlpha(0.5f);
                btnCancel.setAlpha(0.6f);
                btnAdd.setAlpha(0.6f);
                btnPrint.setAlpha(0.6f);
                setBg();
                vipTotal = decimalFormat.format(oTotalVipPrice);
            }
        } else {
            ToastUtil.showToastBig(getContext(), "商品不存在，或者条码有误");
        }
    }

    public interface AddListener {
        void onAddResult(GoodBean bean, String total, String name, double quality, double discount);
    }

    public interface FunctionListener {
        void onClick(int type);
    }

    public void setFunctionListener(FunctionListener listener) {
        this.functionListener = listener;
    }

}
